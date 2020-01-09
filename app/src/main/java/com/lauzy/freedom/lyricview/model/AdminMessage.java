package com.lauzy.freedom.lyricview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminMessage {

    @SerializedName("msg")
    @Expose
    private String msg;

    public void setMsg(String name) {
        this.msg = name;
    }
    public String getMsg(){
        return this.msg;
    }
}
