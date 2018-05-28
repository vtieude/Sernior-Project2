package com.example.wilson.humancharacteristics.model;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.example.wilson.Tensorflow.Classifier;
import com.example.wilson.Tensorflow.ClassifierEmotion;
import com.example.wilson.Tensorflow.TensorFlowImageClassifier;
import com.example.wilson.Tensorflow.TensorFlowImageClassifierEmotion;

import java.util.List;

/**
 * Created by Wilson on 5/10/2018.
 */

public class HumanEmotion extends BaseHumanCharacteristic {
    private ClassifierEmotion classifierEmotion;
    private static final String MODEL = "file:///android_asset/emotion.pb";
    private static final String Label= "file:///android_asset/emotion.txt";
    private boolean activeMode;
    public HumanEmotion(final AssetManager assetManager) {
        initTensorFlowAndLoadModel(assetManager);
        setMode(true);
        setNameModel("Emotion");
    }
    void initTensorFlowAndLoadModel(AssetManager assetManager) {
        classifierEmotion = TensorFlowImageClassifierEmotion.create(
                assetManager,
                MODEL,
                Label,
                224,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME);
    }

    @Override
    public String recognizeImage(Bitmap bitmap) {
        final List<ClassifierEmotion.Recognition> results = classifierEmotion.recognizeImage(bitmap);
        String result = results.toString();
        return result;
    }

    @Override
    void setMode(boolean mode) {
        this.activeMode = mode;
    }

    @Override
    boolean getMode() {
        return this.activeMode;
    }

    @Override
    public void onDestroy() {
        classifierEmotion.close();
    }

    @Override
    void setNameModel(String nameModel) {
        this.nameModel = nameModel;
    }

    @Override
    String getNameModel() {
        return this.nameModel;
    }
}
