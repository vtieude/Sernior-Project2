package com.example.wilson.humancharacteristics.utils;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by enclaveit on 22/03/2018.
 */

public class CameraErrorCallback implements Camera.ErrorCallback {

    private static final String TAG = "CameraErrorCallback";

    @Override
    public void onError(int error, Camera camera) {
        Log.e(TAG, "Encountered an unexpected camera error: " + error);
    }
}