package com.example.wilson.humancharacteristics.bean;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

public class HumanModel implements Serializable{
    private int id;
    private String name;
    private int age;
    private String comment;
    private String phone;
    private String email;
    private String flagImage;
    private byte[] image;
    public HumanModel() {
        super();
    }
    public HumanModel(int id, String name,  int age, String comment, String phone, String email, byte[] image) {
        this.name = name;
        this.image = image;
        this.age = age;
        this.comment = comment;
        this.phone = phone;
        this.email = email;
        this.id = id;
    }
    public HumanModel(String name, String flag, int age) {
        this.name = name;
        this.flagImage = flag;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFlagImage(String flagImage) {
        this.flagImage = flagImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        image = image;
    }

    //find image.
    public int getMipmapResIdByName(Context context, String resName)  {
        String pkgName = context.getPackageName();
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }
}
