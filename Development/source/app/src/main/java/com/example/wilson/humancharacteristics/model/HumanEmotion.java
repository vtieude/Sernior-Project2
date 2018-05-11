package com.example.wilson.humancharacteristics.model;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.example.wilson.Tensorflow.Classifier;
import com.example.wilson.Tensorflow.TensorFlowImageClassifier;

import java.util.List;

/**
 * Created by Wilson on 5/10/2018.
 */

public class HumanEmotion extends BaseHumanCharacteristic {
    private Classifier classifier;
    private static final String MODEL = "file:///android_asset/emotion.pb";
    private static final String Label= "file:///android_asset/emotion.txt";
    private boolean activeMode;
    public HumanEmotion(final AssetManager assetManager) {
        initTensorFlowAndLoadModel(assetManager);
        setMode(true);
        setNameModel("TrustWorthy");
    }
    void initTensorFlowAndLoadModel(AssetManager assetManager) {
        classifier = TensorFlowImageClassifier.create(
                assetManager,
                MODEL,
                Label,
                299,
                IMAGE_MEAN,
                IMAGE_STD,
                "pool_3/_reshape",
                OUTPUT_NAME);
    }

    @Override
    public String recognizeImage(Bitmap bitmap) {
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
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
        classifier.close();
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
