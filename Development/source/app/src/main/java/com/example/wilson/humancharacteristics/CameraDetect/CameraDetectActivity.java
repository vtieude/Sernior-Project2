package com.example.wilson.humancharacteristics.CameraDetect;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.wilson.humancharacteristics.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.imgproc.Imgproc;

public class CameraDetectActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private OrientationListener orientationListener;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }
    private JavaCameraView javaCameraView;
    private Mat img, mRgbaT, mRgbaF;
    private TextView textView;
    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status){
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detect);
        javaCameraView = (JavaCameraView) this.findViewById(R.id.javaCameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.enableView();
        javaCameraView.setCvCameraViewListener(this);

        textView = (TextView) findViewById(R.id.textview);
        textView.setText(fromDetectFaceLib());


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (OpenCVLoader.initDebug()){
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else{
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, baseLoaderCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    public native void detectFace( long imgMat );
    public native String fromDetectFaceLib();

    @Override
    public void onCameraViewStarted(int width, int height) {
        img = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        img.release();
        mRgbaF.release();
        mRgbaT.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        img = inputFrame.rgba();

        // this is for make camera portrait
        Core.transpose(img, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);

        Core.flip(mRgbaF, img, 1);
        detectFace(img.getNativeObjAddr());

        return img;
    }


private class OrientationListener extends OrientationEventListener {
    final int ROTATION_O    = 1;
    final int ROTATION_90   = 2;
    final int ROTATION_180  = 3;
    final int ROTATION_270  = 4;

    private int rotation = 0;
    public OrientationListener(Context context) { super(context); }

    @Override public void onOrientationChanged(int orientation) {
        if( (orientation < 35 || orientation > 325) && rotation!= ROTATION_O){ // PORTRAIT
            rotation = ROTATION_O;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if( orientation > 145 && orientation < 215 && rotation!=ROTATION_180){ // REVERSE PORTRAIT
            rotation = ROTATION_180;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if(orientation > 55 && orientation < 125 && rotation!=ROTATION_270){ // REVERSE LANDSCAPE
            rotation = ROTATION_270;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(orientation > 235 && orientation < 305 && rotation!=ROTATION_90){ //LANDSCAPE
            rotation = ROTATION_90;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
}

