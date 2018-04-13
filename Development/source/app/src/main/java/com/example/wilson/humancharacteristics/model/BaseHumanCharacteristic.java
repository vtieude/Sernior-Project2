package com.example.wilson.humancharacteristics.model;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.example.wilson.Tensorflow.Classifier;

/**
 * Created by Wilson on 4/12/2018.
 */

public abstract class BaseHumanCharacteristic {
    protected static final int INPUT_SIZE = 224;
    protected static final int IMAGE_MEAN = 128;
    protected static final float IMAGE_STD = 128.0f;
    protected static final String INPUT_NAME = "input";
    protected static final String OUTPUT_NAME = "final_result";
    protected String nameModel;
    abstract void initTensorFlowAndLoadModel(AssetManager assetManager);
    abstract String recognizeImage(Bitmap bitmap);
    abstract void setMode(boolean mode);
    abstract boolean getMode();
    abstract void onDestroy();
    abstract void setNameModel(String nameModel);
    abstract String getNameModel();
}
