package com.example.login;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.net.UriKt;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MeFragment extends Fragment implements View.OnClickListener, ModalBottomSheet.EditHead {

    private View view;
    private View view1;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    SharedPreferences preferences;
    private String id_name = "1";
    private boolean inited = false;
    private int idd = 0;
    private TextView number;
    private TextView saying_tx;
    private TextView take_photo;
    private TextView all_number;
    private TextView name;
    private ImageView head;
    private People my_people;

    private Uri takePictureUri;
    private Uri choosePictureUri;
    private ModalBottomSheet modalBottomSheet;

    // 创建临时文件的方法
    private Uri createPhotoFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File photoFile = new File(context.getCacheDir(), imageFileName);
        return FileProvider.getUriForFile(context, "com.example.login.fileprovider", photoFile);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);

        view1 = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        take_photo = view1.findViewById(R.id.take_photo);

        preferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        id_name = preferences.getString("id_name", null);
        name = view.findViewById(R.id.name);
        number = view.findViewById(R.id.number);
        head = view.findViewById(R.id.head);
        view.findViewById(R.id.head).setOnClickListener(this);
        all_number = view.findViewById(R.id.all_number);
        saying_tx = view.findViewById(R.id.saying_tx);
        view.findViewById(R.id.saying_tx).setOnClickListener(this);
        name.setText(id_name);
        NavigationView navigationView = view.findViewById(R.id.me_list);
//        navigationView.setCheckedItem(R.id.change);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.change) {
                    Intent intent = new Intent(requireContext(), EditSaying.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("id_saying", idd);
                    editor.commit();
                    //Toast.makeText(v.getContext(), "你按了"+position, Toast.LENGTH_SHORT).show();
                    requireContext().startActivity(intent);
                } else if (menuItem.getItemId() == R.id.leave) {
                    getActivity().finish();
                }
                return false;
            }
        });
        java.util.List<People> People = LitePal.findAll(People.class);
        for (People people : People) {
            if (people.getId_name().equals(id_name)) {
                String imagePath = people.getHead();
                my_people = people;
                File imageFile = new File(imagePath);
                Glide.with(this)
                        .load(imageFile)
                        .circleCrop()
                        .into(head);
            }
        }
//        LitePal.deleteAll(Saying.class,"id<?","50");
        return view;
    }

    private void CountNumber() {
        java.util.List<Diary> Diary = LitePal.findAll(Diary.class);
        int i = 0;
        int j = 0;
        for (Diary diary : Diary) {
            if (diary.getPeople_name().equals(id_name)) {
                i++;
                j = j + diary.getContent().length();
            }
        }
        number.setText(String.valueOf(i));
        all_number.setText(String.valueOf(j));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inited = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mounted();
        CountNumber();
    }

    public void mounted() {
        java.util.List<Saying> Saying = LitePal.findAll(Saying.class);
        for (Saying saying : Saying) {
            if (saying.getId_name().equals(id_name)) {
                saying_tx.setText(saying.getContent().toString());
                idd = saying.getId();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == head) {
            // 将底板布局添加到界面中
            modalBottomSheet = new ModalBottomSheet();
            modalBottomSheet.editHead = this;
            modalBottomSheet.show(getChildFragmentManager(), ModalBottomSheet.TAG);
        }
    }

    @Override
    public void onClicks(View v) {
//        Toast.makeText(requireContext(),"7777",Toast.LENGTH_SHORT).show();
        if (v.getId() == take_photo.getId()) {
//            Toast.makeText(view.getContext(), "拍照", Toast.LENGTH_SHORT).show();
            File outputImage = new File(getContext().getExternalCacheDir(), "output_image.jpg");
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "ji", Toast.LENGTH_SHORT).show();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(getContext(),
                        "com.example.login.fileprovider", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            cameraPerssion.launch(Manifest.permission.CAMERA);
            modalBottomSheet.dismiss();

        } else {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 1);
            }

            choosePictureLauncher.launch("image/*");
            modalBottomSheet.dismiss();
        }
    }


    private ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        Log.d("takePictureLauncher",result+"");
        if (result) {
            if (takePictureUri != null) {
                setAvatar(takePictureUri);
            }
        } else {
            takePictureUri = null;
            Toast.makeText(requireContext(), "您未拍照", Toast.LENGTH_SHORT).show();
            // 拍照失败或被取消，处理相应的逻辑
        }
    });

    private ActivityResultLauncher<String> choosePictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        Log.d("choosePictureLauncher",result+"");
        if (result!=null) {
            setAvatar(result);
        } else {
            choosePictureUri = null;
            Toast.makeText(requireContext(), "您未选择照片", Toast.LENGTH_SHORT).show();
            // 选择照片失败或被取消，处理相应的逻辑
        }
    });

    private ActivityResultLauncher cameraPerssion = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        Log.d("takePictureLauncher",result+"");
        if (result) {
            takePhoto(requireContext());
        } else {
            Toast.makeText(getContext(), "你禁止了权限", Toast.LENGTH_SHORT).show();
        }
    });


    private void takePhoto(Context context){
        takePictureUri = createPhotoFile(context);
        takePictureLauncher.launch(takePictureUri);
    }



    private void setAvatar(Uri uri) {
        Glide.with(this)
                .load(uri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .into(head);
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
//                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                File file;
                if (!uri.getScheme().equals("file")){
                    file = new File(requireContext().getFilesDir(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                    file.getParentFile().mkdirs();
                    FileCopy.ioCopyFromUri(requireContext(), uri, file.getAbsolutePath());
                }else {
                    file = UriKt.toFile(uri);
                }


                my_people.setHead(file.getPath());
                my_people.save();
                cursor.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

