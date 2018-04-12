//
// Created by enclaveit on 01/03/2018.
//

#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/imgproc.hpp>
#include "opencv2/imgcodecs.hpp"
#include <opencv2/face.hpp>
#include "opencv2/photo.hpp" // seamlessClone()
#include <iostream>


// Dectect face
cv::Mat frame_gray;
std::vector< std::vector<cv::Point2f> > landmarks;
// Load Face Detector
cv::CascadeClassifier faceDetector("/sdcard/data/haarcascade_frontalface_alt2.xml");
// Find face
std::vector<cv::Rect> faces;

// Limit face detect
int id_max;
static std::vector<int> indexFaces;
static int countFaces = 1;
static double maxAreaValue = 0;
static int i=0, j=0;

bool checkSameValue(const std::vector<int>& indexFaces, int compare_value);
static void limitFaces(const std::vector<cv::Rect>&, std::vector<int>& );

static double areaROI(const cv::Rect&);
static void removeByIndex(std::vector<cv::Rect>&, std::vector<int>& );

static bool initialized = false;
bool success = false;
cv::Ptr<cv::face::Facemark> facemark;

void removeIntersect(std::vector<cv::Rect>& rects);
bool checkIntersect(cv::Rect rect1, cv::Rect rect2);

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_detectFace(
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat
        ) {
    cv::Mat* img = (cv::Mat*) imgMat;
    if (!initialized){
        facemark = cv::face::FacemarkLBF::create();
        facemark->loadModel("/sdcard/data/lbfmodel.yaml");
        initialized = true;
    }

    // Convert frame to grayscale because
    // faceDetector requires grayscale image.
    cv::cvtColor(*img, frame_gray, cv::COLOR_BGR2GRAY);
    // Detect faces
    faceDetector.detectMultiScale(frame_gray, faces);

    // Run landmark detector
    bool success = facemark->fit(*img,faces,landmarks);

    if(success)
    {
        // If successful, render the landmarks on the face
        for(int i = 0; i < landmarks.size(); i++)
        {
            for( size_t i = 0; i < faces.size(); i++ )
            {
                cv::rectangle(*img,faces[i],cv::Scalar( 255, 0, 0 ));
            }
            for(unsigned long i=0;i<faces.size();i++){
                for(unsigned long k=0;k<landmarks[i].size();k++)
                    cv::circle(*img,landmarks[i][k],5,cv::Scalar(0,0,255),cv::FILLED);
            }
        }
    }

}

JNIEXPORT jstring JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_fromDetectFaceLib(
        JNIEnv *env,
        jobject /* this */
        ) {
    std::string hello = "From CameraDetect Lib";
    return env->NewStringUTF(hello.c_str());

}
}


double areaROI(const cv::Rect& face){
    return face.width*face.height;
}


bool checkSameValue(const std::vector<int>& indexFaces, int compare_value){
    for (int idLargeFace = 0; idLargeFace < static_cast<int>(indexFaces.size()); ++idLargeFace) {
        if(compare_value == indexFaces[idLargeFace])
            return true;
    }
    return false;
}

static void limitFaces(const std::vector<cv::Rect>& faces, std::vector<int>& indexFaces_ ){
    if(faces.size() < countFaces){
        for ( i = 0; i < static_cast<int>(faces.size()); ++i) {
            indexFaces_.push_back(i);
        }
    }
    else{
        while(indexFaces_.size() < countFaces) {
            maxAreaValue = 0;
            for ( i = 0; i < static_cast<int>(faces.size()); ++i) {
                if ( checkSameValue(indexFaces, i) ) {
                    continue;
                }
                if( areaROI(faces[i]) >  maxAreaValue ){
                    maxAreaValue = areaROI(faces[i]);
                    id_max = i;
                }
            }
            indexFaces_.push_back(id_max);
        }
    }
}

static void removeByIndex(std::vector<cv::Rect>& faces_, std::vector<int>& indexArray) {
    int it = 0;
    for ( i = 0; i < static_cast<int>(indexArray.size()) ; ++i) {
        faces_.erase(faces_.begin()+it+indexArray[i]);
        it = it-1;
    }
}

// Haven't done
void removeIntersect(std::vector<cv::Rect>& rects) {
    std::vector<cv::Rect>::iterator rect;
    rect = rects.begin();
    std::vector<cv::Rect> unIntersect;

    for ( ; rect != rects.end()-1; ++rect ){
        if (checkIntersect(*rect, *(rect+1))){
        }
    }

}

bool checkIntersect(cv::Rect rect1, cv::Rect rect2){
    return false;
}
