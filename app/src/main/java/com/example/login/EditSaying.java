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

public class EditSaying extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_saying;
    private int id_saying;
    SharedPreferences preferences;
    private ImageView return_pic;
    private Button save;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_saying);
        edit_saying=findViewById(R.id.edit_saying);
        return_pic=findViewById(R.id.return_pic);
        save=findViewById(R.id.save);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.edit_saying).setOnClickListener(this);
        findViewById(R.id.return_pic).setOnClickListener(this);
        preferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        id_saying=preferences.getInt("id_saying",0);
        List<Saying> Saying = LitePal.findAll(Saying.class);
        for (Saying saying : Saying){
            if (Objects.equals(saying.getId(), id_saying)) {
                edit_saying.setText(saying.getContent());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v==return_pic){
            finish();
        } else if (v == save) {
            List<Saying> Saying = LitePal.findAll(Saying.class);
            for (Saying saying : Saying){
                if (Objects.equals(saying.getId(), id_saying)) {
                    saying.setContent(edit_saying.getText().toString());
                    saying.save();
                }
            }
            finish();
        }
    }
}