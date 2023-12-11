package com.example.login;


import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class People extends LitePalSupport {
    private String id_name;
    private String id_password;
    private String head;
    private int id;

    private List<Diary> diaryList=new ArrayList<Diary>();

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getId_password() {
        return id_password;
    }

    public void setId_password(String id_password) {
        this.id_password = id_password;
    }

    public java.util.List<Diary> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(java.util.List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}

