package com.example.login;
import org.litepal.crud.LitePalSupport;
public class Saying extends LitePalSupport{
    private int id;
    private String content;
    private String id_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }
}
