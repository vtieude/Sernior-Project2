package com.example.wilson.humancharacteristics.recognizer;

import android.graphics.Bitmap;
import android.graphics.Rect;
import org.opencv.face.FaceRecognizer;


/**
 * Created by enclaveit on 22/03/2018.
 */

public class PersonRecognizer {

    static  final int WIDTH= 128;
    static  final int HEIGHT= 128;;
    private int mProb=999;

    // Used to load the 'native-lib' library on application startup.

    private Bitmap mBitmap;
    private Rect mFaces;
    private String mPath;
    private int mCount;
    private FaceRecognizer mFaceRecognize;
    Labels mLabelsFile;

    PersonRecognizer(Bitmap bitmap, Rect faces, int count, String path){
        this.mFaces = faces;
        this.mBitmap = bitmap;
        this.mCount = count;
        this.mPath = path;
        mFaceRecognize = org.opencv.face.LBPHFaceRecognizer.create(2,8,8,8,200);
        mLabelsFile= new Labels(mPath);
    }


}
