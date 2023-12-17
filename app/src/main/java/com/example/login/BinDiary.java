package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class BinDiary extends AppCompatActivity {
    public List<Diary> diaryList=new ArrayList<>();
    private String id_name="1";
    SharedPreferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_diary);
        preferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        id_name=preferences.getString("id_name",null);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(com.google.android.material.R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mounted();
    }

    public void mounted() {
        RecyclerView recyclerView;
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        BinDiaryFragment.DiaryAdapter adapter=new BinDiaryFragment.DiaryAdapter(initdiary());
        recyclerView.setAdapter(adapter);
    }

    private List<Diary> initdiary() {
        diaryList.removeAll(diaryList);
        java.util.List<Diary> Diary =LitePal.findAll(Diary.class);
        for (Diary diary : Diary) {
            if (diary.getPeople_name().equals(id_name)){
                if (diary.getIf_delete()==1){
                    diaryList.add(diary);
                }
            }
        }
        return diaryList;
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mounted();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}