package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    public List<Diary> diaryList=new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences preferences;
    private String id_name="1";
    private View view;

    private boolean inited = false;
    private ImageView weather;
    private ImageView mood;
    private TextView weather_list;
    private TextView mood_list;
    private Handler handler = new Handler();
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);
        preferences=getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        id_name=preferences.getString("id_name",null);
        weather=view.findViewById(R.id.weather);
        weather_list=view.findViewById(R.id.weather_list);
        mood=view.findViewById(R.id.mood);
        mood_list=view.findViewById(R.id.mood_list);
        view.findViewById(R.id.weather).setOnClickListener(this);
        view.findViewById(R.id.mood).setOnClickListener(this);
        view.findViewById(R.id.weather_list).setOnClickListener(this);
        view.findViewById(R.id.mood_list).setOnClickListener(this);
        weather_list.setText("全部天气");
        mood_list.setText("全部心情");
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(com.google.android.material.R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return view;
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mounted();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inited = true;
        mounted();
    }

    @Override
    public void onStart(){
        super.onStart();
        mounted();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    public void mounted() {
        if (!inited) return;
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DiaryFragment.DiaryAdapter adapter=new DiaryFragment.DiaryAdapter(initdiary());
        recyclerView.setAdapter(adapter);
    }
    private List<Diary> initdiary() {
            diaryList.removeAll(diaryList);
            java.util.List<Diary> Diary =LitePal.findAll(Diary.class);
            for (Diary diary : Diary) {
                if (diary.getPeople_name().equals(id_name)){
                    if (diary.getWeather().equals(weather_list.getText().toString())||
                            weather_list.getText().toString()=="全部天气"){
                        if (diary.getMood().equals(mood_list.getText().toString())||
                                mood_list.getText().toString()=="全部心情"){
                            if (diary.getIf_delete()==0) {
                                diaryList.add(diary);
                            }
                        }
                    }
                }
            }
        return diaryList;
    }
    @Override
    public void onClick(View v) {
        if (v==weather){
            showWeatherMenu(v);
        } else if (v == mood) {
            showMoodMenu(v);
        }
    }

    private void showWeatherMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.weather_home_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 处理菜单项的点击事件
                int itemId = item.getItemId();
                if (itemId == R.id.sunny) {
                    weather_list.setText("晴");
                    return true;
                } else if (itemId == R.id.cloudy) {
                    weather_list.setText("阴");
                    return true;
                } else if (itemId == R.id.rainy) {
                    weather_list.setText("雨");
                    return true;
                }
                else if (itemId==R.id.all_weather){
                    weather_list.setText("全部天气");
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
        inflater.inflate(R.menu.mood_home_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 处理菜单项的点击事件
                int itemId = item.getItemId();
                if (itemId == R.id.happy) {
                    mood_list.setText("开心");
                    return true;
                } else if (itemId == R.id.angry) {
                    mood_list.setText("生气");
                    return true;
                } else if (itemId == R.id.sad) {
                    mood_list.setText("伤心");
                    return true;
                }
                else if (itemId == R.id.scary) {
                    mood_list.setText("害怕");
                    return true;
                }
                else if (itemId == R.id.all_mood) {
                    mood_list.setText("全部心情");
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}