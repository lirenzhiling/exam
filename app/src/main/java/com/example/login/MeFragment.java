package com.example.login;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        if (v == saying_tx) {
            Intent intent = new Intent(v.getContext(), EditSaying.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("id_saying", idd);
            editor.commit();
            //Toast.makeText(v.getContext(), "你按了"+position, Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        } else if (v == head) {
            // 将底板布局添加到界面中
            ModalBottomSheet modalBottomSheet = new ModalBottomSheet();
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(view.getContext(), "相册", Toast.LENGTH_SHORT).show();

            openGallery(333);
//            openAlbum();
            if (ContextCompat.checkSelfPermission(getContext(),READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{READ_EXTERNAL_STORAGE},1);
            }
        }
    }

//    private void openAlbum() {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent,CHOOSE_PHOTO);
//    }

    private void openGallery(int type) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        imageUri=gallery.getData();
        startActivityForResult(gallery, CHOOSE_PHOTO);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,TAKE_PHOTO);
                }else {
                    Toast.makeText(getContext(), "你禁止了权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO: {
//                resultCode==RESULT_OK
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                        head.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(head);
                break;
            }
            case CHOOSE_PHOTO:
                Glide.with(this)
                        .load(data.getData()).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .circleCrop()
                        .into(head);
//            case 333:  Glide.with(this)
//                    .load(data.getData()).skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .circleCrop()
//                    .into(head);

            default:
                break;
        }
    }


}