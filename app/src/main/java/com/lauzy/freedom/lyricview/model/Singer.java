package com.lauzy.freedom.lyricview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Singer {
    @SerializedName("count")
    @Expose
    private int count;
    private String image;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Singer() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String n) {
        image = n;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
