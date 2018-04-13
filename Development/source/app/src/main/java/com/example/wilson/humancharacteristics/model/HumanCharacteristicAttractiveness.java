package com.example.wilson.humancharacteristics.model;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.example.wilson.Tensorflow.Classifier;
import com.example.wilson.Tensorflow.TensorFlowImageClassifier;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Wilson on 4/12/2018.
 */

public class HumanCharacteristicAttractiveness extends BaseHumanCharacteristic {


    private Classifier classifier;
    private static final String MODEL_Attractiveness = "file:///android_asset/rounded_graph.pb";
    private static final String Label_Attractiveness = "file:///android_asset/retrained_labels.txt";
    private float Attractiveness;
    private boolean activeMode;

    public HumanCharacteristicAttractiveness(final  AssetManager assetManager) {
        initTensorFlowAndLoadModel(assetManager);
        setMode(true);
        setNameModel("Attractiveness");
    }
    public float getAttractiveness() { return Attractiveness;  }
    public void setAttractiveness(float acAttractiveness) { Attractiveness = acAttractiveness;}
    public void onDestroy() {
        classifier.close();
    }

    @Override
    void initTensorFlowAndLoadModel(AssetManager assetManager) {

        classifier = TensorFlowImageClassifier.create(
                assetManager,
                MODEL_Attractiveness,
                Label_Attractiveness,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME);
    }

    @Override
    public  String recognizeImage(Bitmap bitmap) {
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
        String result = results.toString();
        return result;
    }

    @Override boolean getMode(){
        return this.activeMode;
    }

    @Override void setMode(boolean mode){
        this.activeMode = mode;
    }

    @Override void setNameModel(String nameModel){
        this.nameModel = nameModel;
    }

    @Override String getNameModel(){
        return this.nameModel;
    }

}
