package com.example.wilson.humancharacteristics.Storage;

/**
 * Created by Wilson on 3/5/2018.
 */

public class HumanModel {
    private String name;
    private String flagImage;
    private int age;
    public HumanModel(String name, String flag, int age) {
        this.name = name;
        this.flagImage = flag;
        this.age = age;
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
}
