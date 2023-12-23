package com.example.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFragment extends Fragment implements View.OnClickListener{

    private ImageView date;
    private MaterialDatePicker<Long> datePicker;
    SharedPreferences preferences;
    private EditText new_diary;
    private Button save;
    private String id_name;
    private View view;
    private TextView new_date;
    private ImageView weather;
    private ImageView mood;
    private TextView new_weather;
    private TextView new_mood;
    private AlertDialog.Builder save_builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_add, container, false);
        preferences=getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        id_name=preferences.getString("id_name",null);
        date=view.findViewById(R.id.date);
        new_diary=view.findViewById(R.id.new_diary);
        weather =view.findViewById(R.id.weather);
        mood=view.findViewById(R.id.mood);
        new_weather=view.findViewById(R.id.new_weather);
        new_mood=view.findViewById(R.id.new_mood);
        save=view.findViewById(R.id.save);
        new_date =view.findViewById(R.id.new_date);
        view.findViewById(R.id.new_date).setOnClickListener(this);
        view.findViewById(R.id.mood).setOnClickListener(this);
        view.findViewById(R.id.new_mood).setOnClickListener(this);
        view.findViewById(R.id.new_weather).setOnClickListener(this);
        view.findViewById(R.id.weather).setOnClickListener(this);
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.new_diary).setOnClickListener(this);
        view.findViewById(R.id.date).setOnClickListener(this);
        new_date.setText("无日期");
        new_weather.setText("无天气");
        new_mood.setText("无心情");
        save_builder = new AlertDialog.Builder(view.getContext());
        save_builder.setTitle("保存");  // 设置对话框标题
        save_builder.setMessage("确定要保存该日记吗？");  // 设置对话框内容
        save_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了确认按钮，执行相应的操作
                LitePal.getDatabase();
                Connector.getDatabase();
                Diary diary=new Diary();
                diary.setContent(new_diary.getText().toString());
                diary.setPeople_name(id_name);
                diary.setTime(new_date.getText().toString());
                diary.setWeather(new_weather.getText().toString());
                diary.setMood(new_mood.getText().toString());
                diary.save();
                new_diary.setText("");
                Toast.makeText(view.getContext(), "已保存", Toast.LENGTH_SHORT).show();
//            LitePal.deleteAll(Diary.class,"id<?","50");
//            replaceFragment(new HomeFragment());
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
        datePicker= MaterialDatePicker.Builder.datePicker().setTitleText("选择日期").build();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v==date){
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);
                // 创建SimpleDateFormat实例，指定日期格式
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                // 格式化日期为字符串
                String formattedDate = dateFormat.format(calendar.getTime());
                new_date.setText(formattedDate);
            });
            datePicker.show(getActivity().getSupportFragmentManager(), "tag");
        } else if (v == weather) {
            showWeatherMenu(v);
        }
        else if (v == mood) {
            showMoodMenu(v);
        }else if (v==save){
            AlertDialog dialog = save_builder.create();  // 创建对话框
            dialog.show();  // 显示对话框
        }
    }

    private void showWeatherMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.weather_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 处理菜单项的点击事件
                int itemId = item.getItemId();
                if (itemId == R.id.sunny) {
                    // 执行菜单项1的操作
                    new_weather.setText("晴");
                    return true;
                } else if (itemId == R.id.cloudy) {
                    // 执行菜单项2的操作
                    new_weather.setText("阴");
                    return true;
                } else if (itemId == R.id.rainy) {
                    // 执行菜单项3的操作
                    new_weather.setText("雨");
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void showMoodMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.mood_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 处理菜单项的点击事件
                int itemId = item.getItemId();
                if (itemId == R.id.happy) {
                    // 执行菜单项1的操作
                    new_mood.setText("开心");
                    return true;
                } else if (itemId == R.id.angry) {
                    // 执行菜单项2的操作
                    new_mood.setText("生气");
                    return true;
                } else if (itemId == R.id.sad) {
                    // 执行菜单项3的操作
                    new_mood.setText("伤心");
                    return true;
                }
                else if (itemId == R.id.scary) {
                    // 执行菜单项3的操作
                    new_mood.setText("害怕");
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    /*private void replaceFragment(Fragment fragment) {
            FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.replace(R.id.add_fragment,fragment);
            transaction.commit();
        }*/

}