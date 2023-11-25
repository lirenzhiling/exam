package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.litepal.LitePal;

import java.util.List;
import java.util.Objects;

public class EditDiary extends AppCompatActivity implements View.OnClickListener {

    private ImageView return_pic;
    SharedPreferences preferences;
    private int id_diary;
    private EditText edit_diary;
    private Button save;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        return_pic=findViewById(R.id.return_pic);
        edit_diary=findViewById(R.id.edit_diary);
        save=findViewById(R.id.save);
        delete=findViewById(R.id.delete);
        findViewById(R.id.edit_diary).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.return_pic).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        preferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        id_diary=preferences.getInt("id_diary",0);
        List<Diary> Diary = LitePal.findAll(Diary.class);
        for (Diary diary : Diary){
            if (Objects.equals(diary.getId(), id_diary)) {
                edit_diary.setText(diary.getContent());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v==return_pic){
            finish();
        } else if (v==save) {
            List<Diary> Diary = LitePal.findAll(Diary.class);
            for (Diary diary : Diary){
                if (Objects.equals(diary.getId(), id_diary)) {
                    diary.setContent(edit_diary.getText().toString());
                    diary.save();
                }
            }
            finish();
        }else if (v==delete) {
            List<Diary> Diary = LitePal.findAll(Diary.class);
            for (Diary diary : Diary){
                if (Objects.equals(diary.getId(), id_diary)) {
                    diary.delete();
                }
            }
            finish();
        }
    }
}