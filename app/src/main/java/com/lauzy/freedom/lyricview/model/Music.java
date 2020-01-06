package com.lauzy.freedom.lyricview.model;

public class Music {
    private String name;
    private String url;
    private String singer;
    private int id;
    private int rate;

    public Music() {
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String n) {
        url = n;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String n) {
        singer = n;
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
