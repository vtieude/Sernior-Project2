package com.example.wilson.humancharacteristics.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by enclaveit on 22/03/2018.
 */

public class FaceResult extends Object {

    private PointF midEye;
    private float eyeDist;
    private float confidence;
    private float pose;
    private int id;
    private long time;
    private String attracttiveHuman;
    private String trustworthyHuman;
    private String dominantHuman;
    private String threadHuman;
    private String likeabilityHuman;
    private String competentHuman;
    private String extrovertedHuman;
    private int countCharacters;
    private Bitmap bitmapFaceCrop;



    public FaceResult() {
        id = 0;
        midEye = new PointF(0.0f, 0.0f);
        eyeDist = 0.0f;
        confidence = 0.4f;
        pose = 0.0f;
        time = System.currentTimeMillis();
        attracttiveHuman = "";
        trustworthyHuman = "";
        dominantHuman = "";
        threadHuman = "";
        likeabilityHuman = "";
        competentHuman = "";
        extrovertedHuman = "";
        bitmapFaceCrop = null;

    }




    public void setFace(int id, PointF midEye, float eyeDist, float confidence, float pose, long time) {
        set(id, midEye, eyeDist, confidence, pose, time);
    }

    public void clear() {
        set(0, new PointF(0.0f, 0.0f), 0.0f, 0.4f, 0.0f, System.currentTimeMillis());
        setBitmapFaceCrop(null);
    }

    public synchronized void set(int id, PointF midEye, float eyeDist, float confidence, float pose, long time) {
        this.id = id;
        this.midEye.set(midEye);
        this.eyeDist = eyeDist;
        this.confidence = confidence;
        this.pose = pose;
        this.time = time;
//        this.attracttiveHuman = attracttiveHuman;
//        this.trustworthyHuman = trustworthyHuman;
//        this.dominantHuman = dominantHuman;
//        this.threadHuman = threadHuman;
//        this.likeabilityHuman = likeabilityHuman;
//        this.competentHuman = competentHuman;
//        this.extrovertedHuman = extrovertedHuman;
    }
    public Bitmap getBitmapFaceCrop() {return  bitmapFaceCrop;}
    public void setBitmapFaceCrop(Bitmap bitmap) {
        this.bitmapFaceCrop = bitmap;
    }
    public float eyesDistance() {
        return eyeDist;
    }

    public void setEyeDist(float eyeDist) {
        this.eyeDist = eyeDist;
    }

    public void getMidPoint(PointF pt) {
        pt.set(midEye);
    }

    public PointF getMidEye() {
        return midEye;
    }

    public void setMidEye(PointF midEye) {
        this.midEye = midEye;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public float getPose() {
        return pose;
    }

    public void setPose(float pose) {
        this.pose = pose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setAttractive(String attracttiveHuman){
        this.attracttiveHuman = attracttiveHuman;
    }

    public String getAttractive(){
        return this.attracttiveHuman;
    }

    public void setCompetnent(String competentHuman){
        this.competentHuman = competentHuman;
    }

    public String getCompetent(){
        return this.competentHuman;
    }

    public void setDominant(String dominantHuman) { this.dominantHuman = dominantHuman; }

    public String getDominant() { return this.dominantHuman; }

    public void setExtroverted( String extrovertedHuman) { this.extrovertedHuman = extrovertedHuman; }

    public String getExtroverted() { return this.extrovertedHuman; }

    public void setLikeability(String likeabilityHuman) {  this.likeabilityHuman = likeabilityHuman; }

    public String getLikeability(){ return this.likeabilityHuman; }

    public void setThread(String threadHuman) { this.threadHuman = threadHuman; }

    public String getThread(){ return this.threadHuman; }

    public void setTrustworthy(String trustworthyHuman) { this.trustworthyHuman = trustworthyHuman; }

    public String getTrustworthy() { return this.trustworthyHuman; }

    public void setCountCharacters(int number){ this.countCharacters = number; }

    public int getCountCharacters(){ return this.countCharacters; };
}
