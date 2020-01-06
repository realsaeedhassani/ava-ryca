package com.lauzy.freedom.lyricview.model;

public class Comment {
    private String name;
    private String date;
    private String comment;
    private int id;
    private int rate;

    public Comment() {
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String n) {
        date = n;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String n) {
        comment = n;
    }

    public int getId() {
        return id;
    }

    public void setId(int n) {
        id = n;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int n) {
        rate = n;
    }
}
