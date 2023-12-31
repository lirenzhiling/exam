package com.example.login;
import org.litepal.crud.LitePalSupport;
public class Diary extends LitePalSupport {
    private int id;
    private String content;
    private String time="未填写日期";
    private People people;
    private String weather="无";
    private String mood="无";
    private String people_name="无";
    private String image_file="1";
    private int if_delete=0;

    /*public Diary(String time, String content) {
        this.time = time;
        this.content=content;
    }*/

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }


    public int getIf_delete() {
        return if_delete;
    }

    public void setIf_delete(int if_delete) {
        this.if_delete = if_delete;
    }
}
