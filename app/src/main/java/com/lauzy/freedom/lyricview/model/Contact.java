package com.lauzy.freedom.lyricview.model;

public class Contact {
    private int id;
    private String name;
    private String image;
    private String phone;

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String n) {
        image = n;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String n) {
        phone = n;
    }

    public void setId(int n) {
        id = n;
    }
}
