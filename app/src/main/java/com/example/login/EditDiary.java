package com.example.login;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.net.UriKt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EditDiary extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    private int id_diary;
    private EditText edit_diary;
    private Diary diary;
    private FloatingActionButton setting;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AlertDialog.Builder save_builder;
    private AlertDialog.Builder delete_builder;
    private ImageView diary_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        edit_diary = findViewById(R.id.edit_diary);
        setting = findViewById(R.id.setting);
        diary_image =findViewById(R.id.diary_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.return_pic); // 设置返回按钮图标
        }
        collapsingToolbarLayout.setTitle("原神");
        findViewById(R.id.setting).setOnClickListener(this);
        findViewById(R.id.edit_diary).setOnClickListener(this);
        save_builder = new AlertDialog.Builder(EditDiary.this);
        save_builder.setTitle("保存");  // 设置对话框标题
        save_builder.setMessage("确定要保存该日记吗？");  // 设置对话框内容
        save_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了确认按钮，执行相应的操作
                diary.setContent(edit_diary.getText().toString());
                diary.save();
                finish();
            }
        });

        // 设置取消按钮的点击事件
        save_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了取消按钮，取消操作或执行其他操作
                dialog.dismiss();  // 关闭对话框
            }
        });
        delete_builder = new AlertDialog.Builder(EditDiary.this);
        delete_builder.setTitle("删除");  // 设置对话框标题
        delete_builder.setMessage("确定要删除该日记吗？");  // 设置对话框内容
        delete_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了确认按钮，执行相应的操作
                diary.delete();
                finish();
            }
        });

        // 设置取消按钮的点击事件
        delete_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了取消按钮，取消操作或执行其他操作
                dialog.dismiss();  // 关闭对话框
            }
        });
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        id_diary = preferences.getInt("id_diary", 0);
        List<Diary> Diary = LitePal.findAll(Diary.class);
        for (Diary diary1 : Diary) {
            if (Objects.equals(diary1.getId(), id_diary)) {
                diary = diary1;
                edit_diary.setText(diary.getContent());
            }
        }
        if (diary.getImage_file()!= "1") {
            String imagePath = diary.getImage_file();
            File imageFile = new File(imagePath);
            Glide.with(this)
                    .load(imageFile)
                    .into(diary_image);
        }

    }


    @Override
    public void onClick(View v) {
        if (v == setting) {
            showMenu(v);
        }
    }

    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(EditDiary.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.edit_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 处理菜单项的点击事件
                int itemId = item.getItemId();
                if (itemId == R.id.save) {
                    AlertDialog dialog = save_builder.create();  // 创建对话框
                    dialog.show();  // 显示对话框
                    return true;
                } else if (itemId == R.id.delete) {
                    AlertDialog dialog = delete_builder.create();  // 创建对话框
                    dialog.show();  // 显示对话框
                    return true;
                } else if (itemId == R.id.photo) {
                    Toast.makeText(EditDiary.this, "修改图片", Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(EditDiary.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditDiary.this, new String[]{READ_EXTERNAL_STORAGE}, 1);
                    }

                    choosePictureLauncher.launch("image/*");
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActivityResultLauncher<String> choosePictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        Log.d("choosePictureLauncher",result+"");
        if (result!=null) {
            setAvatar(result);
        } else {
            Toast.makeText(EditDiary.this, "您未选择照片", Toast.LENGTH_SHORT).show();
            // 选择照片失败或被取消，处理相应的逻辑
        }
    });

    private void setAvatar(Uri uri) {
        Glide.with(this)
                .load(uri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(diary_image);
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = EditDiary.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
//                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                File file;
                if (!uri.getScheme().equals("file")){
                    file = new File(EditDiary.this.getFilesDir(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                    file.getParentFile().mkdirs();
                    FileCopy.ioCopyFromUri(EditDiary.this, uri, file.getAbsolutePath());
                }else {
                    file = UriKt.toFile(uri);
                }

                diary.setImage_file(file.getPath());
                diary.save();
                cursor.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}