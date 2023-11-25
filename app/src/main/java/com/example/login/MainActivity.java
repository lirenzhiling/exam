package com.example.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_password;
    private EditText et_password;
    private TextView btn_forget;
    private CheckBox ck_remember;
    private EditText et_phone;
    private ActivityResultLauncher<Intent> register;
    private Button btn_login;
    private String mPassword="111111";
    private String mverifyCode="1111111";
    private TextView btn_zc;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_password=findViewById(R.id.tv_password);
        et_phone=findViewById(R.id.et_phone);
        et_password=findViewById(R.id.et_password);
        btn_forget=findViewById(R.id.btn_forget);
        ck_remember=findViewById(R.id.ck_remember);
        btn_login=findViewById(R.id.btn_login);
        btn_zc=findViewById(R.id.btn_zc);
        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_zc.setOnClickListener(this);
        preferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("id_name","123");
        editor.commit();
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent=result.getData();
                if (intent!=null&&result.getResultCode()== Activity.RESULT_OK){
                    mPassword=intent.getStringExtra("new_password");
                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        reload();
    }
    private void reload() {
        String phone=preferences.getString("phone",null);
        boolean remember=preferences.getBoolean("remember",false);
        if (phone!=null){
            et_phone.setText(phone);
        }
        if (remember) {
            String password = preferences.getString("password", null);
            if (password != null) {
                et_password.setText(password);
                ck_remember.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {

        String phone=et_phone.getText().toString();
        if(v == btn_forget){
            /*Intent intent = new Intent(this, LoginForgetActivity.class);
            intent.putExtra("phone", phone);
            register.launch(intent);*/
        }
        else if(v==btn_login){
            //LitePal.getDatabase()
            java.util.List<People> People =LitePal.findAll(People.class);
            for (People p : People){
                if (p.getId_name().equals(et_phone.getText().toString())){
                    //Toast.makeText(this, "账号密码不匹配"+et_phone.getText().toString()+et_password.getText().toString(), Toast.LENGTH_SHORT).show();
                    People people1_password = LitePal.select("id_password").where("id_name=?",et_phone.getText().toString()).findFirst(People.class);
                    //String password=preferences.getString("password",null);
                    String passwordd=et_password.getText().toString();
                    String password=people1_password.getId_password();
                    if(password.equals(passwordd)){
                        loginSuccess();
                        Intent intent = new Intent(this, BottomList.class);
                        intent.putExtra("id_name",et_phone.getText().toString());
                        setResult(Activity.RESULT_OK,intent);
                        register.launch(intent);
                        return;
                    }
                    else {Toast.makeText(this, "账号密码不匹配", Toast.LENGTH_SHORT).show();
                        return; }
                }
            }
            Toast.makeText(this, "账号未注册", Toast.LENGTH_SHORT).show();
            return;
        } else if (v == btn_zc) {
            java.util.List<People> People =LitePal.findAll(People.class);
            Intent intent = new Intent(this, LoginForgetActivity.class);
            intent.putExtra("phone", phone);
            register.launch(intent);
        }
    }

    private void loginSuccess() {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("id_name",et_phone.getText().toString());
        editor.putString("phone",et_phone.getText().toString());
        editor.putString("password",et_password.getText().toString());
        editor.putBoolean("remember",ck_remember.isChecked());
//        LitePal.deleteAll(Saying.class,"id<?","50");
        editor.commit();

    }
}