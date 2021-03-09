package com.example.logowanie_inzynierka2.Model;

public class Notification {
    private String Title;
    private String Describe;
    private String Date;

    public Notification(String title, String describe, String date) {
        Title = title;
        Describe = describe;
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
