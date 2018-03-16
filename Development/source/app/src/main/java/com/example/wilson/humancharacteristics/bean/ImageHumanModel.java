package com.example.wilson.humancharacteristics.bean;

/**
 * Created by Wilson on 3/14/2018.
 */

public class ImageHumanModel {
    private int id;
    private int idHuman;
    private byte[] image;
    public ImageHumanModel() {
        super();
    }
    public ImageHumanModel(int id, int idHuman, byte[] image) {
        this.id = id;
        this.idHuman = idHuman;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdHuman() {
        return idHuman;
    }

    public void setIdHuman(int idHuman) {
        this.idHuman = idHuman;
    }
}
