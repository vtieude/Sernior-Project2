//
// Created by enclaveit on 15/03/2018.
//


#include <opencv2/imgproc.hpp>
#include <opencv2/core.hpp>
#include <iostream>
#include "jni.h"

void drawFunny(cv::Mat& img );

extern "C"{
    JNIEXPORT void JNICALL Java_com_example_wilson_humancharacteristics_CameraDetect_FaceGraphic_calculateFaceRotation(
            JNIEnv *env,
            jobject /* this */,
            jlong imgMat){
        cv::Mat *img = (cv::Mat*) imgMat;
        drawFunny(*img);
    }
}

void drawFunny(cv::Mat& img){
    cv::circle(img, cv::Point(10,10), 3, cv::Scalar(0,0,255), -1);
    cv::circle(img, cv::Point(20,20), 3, cv::Scalar(0,0,255), -1);
}
