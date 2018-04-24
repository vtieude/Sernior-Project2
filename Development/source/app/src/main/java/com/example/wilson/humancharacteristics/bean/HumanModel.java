package com.example.wilson.humancharacteristics.bean;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.wilson.humancharacteristics.model.HumanCharacteristicAttractiveness;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicCompetent;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicDominant;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicExtroverted;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicLikeability;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicThread;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicTrustworthy;

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
    private HumanCharacteristicAttractiveness attracttiveHuman;
    private HumanCharacteristicCompetent competentHuman;
    private HumanCharacteristicDominant dominantHuman;
    private HumanCharacteristicExtroverted extrovertedHuman;
    private HumanCharacteristicLikeability likeabilityHuman;
    private HumanCharacteristicThread threadHuman;
    private HumanCharacteristicTrustworthy trustworthyHuman;
    public HumanModel() {
        super();

    }
    public HumanModel( String name,  int age, String comment, String phone, String email, byte[] image) {
        this.name = name;
        this.age = age;
        this.comment = comment;
        this.phone = phone;
        this.email = email;
        this.image = image;
    }
    public HumanModel(AssetManager assetManager) {
        trustworthyHuman = new HumanCharacteristicTrustworthy(assetManager);
        attracttiveHuman = new HumanCharacteristicAttractiveness(assetManager);
        dominantHuman = new HumanCharacteristicDominant(assetManager);
        competentHuman = new HumanCharacteristicCompetent(assetManager);
        extrovertedHuman = new HumanCharacteristicExtroverted(assetManager);
        likeabilityHuman = new HumanCharacteristicLikeability(assetManager);
        threadHuman = new HumanCharacteristicThread(assetManager);
    }
    public HumanCharacteristicAttractiveness getAttracttiveHuman() {return  attracttiveHuman;}
    public HumanCharacteristicCompetent getCompetentHuman() {return competentHuman;}
    public HumanCharacteristicDominant getDominantHuman() {return dominantHuman;}
    public HumanCharacteristicExtroverted getExtrovertedHuman() {return extrovertedHuman;}
    public HumanCharacteristicLikeability getLikeabilityHuman() { return likeabilityHuman;}
    public HumanCharacteristicThread getThreadHuman() {return threadHuman;}
    public HumanCharacteristicTrustworthy getTrustworthyHuman() {return trustworthyHuman;}
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
        this.image = image;
    }
    //find image.

    public int getMipmapResIdByName(Context context, String resName)  {
        String pkgName = context.getPackageName();
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }
}
