package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_password_first;
    private EditText et_password_second;
    private SharedPreferences preferences;
    private EditText et_id;
    private People people;
    //private String mDatabaseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        et_password_first = findViewById(R.id.et_password_first);
        et_password_second=findViewById(R.id.et_password_second);
        et_id=findViewById(R.id.et_id);
        //mDatabaseName=getFilesDir()+"/people.db";
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.return_pic).setOnClickListener(this);
        preferences=getSharedPreferences("config", Context.MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            String password_first = et_password_first.getText().toString();
            String password_second = et_password_second.getText().toString();
            String id_=et_id.getText().toString();
            java.util.List<People> People = LitePal.findAll(People.class);
            for (People p : People){
                if (p.getId_name().equals(et_id.getText().toString())){
                    people=p;
                }
            }
            if (id_.isEmpty()){
                Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password_first.length()<6){
                Toast.makeText(this, "请输入6位数字的密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password_first.equals(password_second)){
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            //SQLiteDatabase db=openOrCreateDatabase(mDatabaseName,Context.MODE_PRIVATE,null);
            //String desc=String.format("数据库%s创建%s",db.getPath(),(db!=null)?"成功":"失败");
            //Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("password","");
            editor.putString("phone",et_id.getText().toString());
//           editor.putBoolean("remember",false);
            editor.commit();
            Intent intent=new Intent();
            intent.putExtra("new_password",password_first);
            setResult(Activity.RESULT_OK,intent);
            LitePal.getDatabase();
            Connector.getDatabase();
            people.setId_password(et_password_first.getText().toString());
            people.save();
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
           /*people1.setId_password("666666");
           people1.updateAll("id_name=?",et_id.getText().toString());*/
            /*LitePal.deleteAll(people.class,"id_name=?","a3");*/
            finish();
        }
        if (v.getId() ==R.id.return_pic){
            finish();
        }
    }
}