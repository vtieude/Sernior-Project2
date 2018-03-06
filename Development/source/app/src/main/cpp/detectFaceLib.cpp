//
// Created by enclaveit on 01/03/2018.
//

#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/imgproc.hpp>
#include "recognizeFace.h"

// Dectect face
cv::Mat frame_gray;
cv::Mat faceROI;
std::vector<cv::Rect> faces;

// Limit face detect
int countFaces = 2;
int id_max;
std::vector<int> indexFaces;
double maxAreaValue = 0;
bool checkSameValue(const std::vector<int>& indexFaces, int compare_value);
void limitFaces(const std::vector<cv::Rect>&, std::vector<int>&, int);


//cv::CascadeClassifier face_cascade("/storage/emulated/0/data/haarcascade_frontalface_alt.xml");
cv::CascadeClassifier face_cascade("/sdcard/data/haarcascade_frontalface_alt.xml");

void haarCascadeDetect(cv::Mat& frame);

double areaROI(const cv::Rect&);

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_detectFace(
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat) {

    cv::Mat* img = (cv::Mat*) imgMat;
    haarCascadeDetect(*img);
}

JNIEXPORT jstring JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_fromDetectFaceLib(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "From CameraDetect Lib";
    return env->NewStringUTF(hello.c_str());
}


}

void haarCascadeDetect(cv::Mat& frame){
    if(face_cascade.empty()){
        printf("--(!)Error loading\n"); return;
    }

    cv::cvtColor( frame, frame_gray, cv::COLOR_RGBA2GRAY );
    cv::equalizeHist( frame_gray, frame_gray );

    //-- Detect faces
    face_cascade.detectMultiScale( frame_gray, faces, 1.3, 3, 0|cv::CASCADE_SCALE_IMAGE, cv::Size(50, 50) );

    // -- Limiting face recognition.
    indexFaces.clear();
    limitFaces(faces, indexFaces, countFaces);

    for( size_t i = 0; i < indexFaces.size(); i++ )
    {
        cv::rectangle(frame, cv::Point(faces[indexFaces[i]].x, faces[indexFaces[i]].y),
                      cv::Point(faces[indexFaces[i]].x + faces[indexFaces[i]].width, faces[indexFaces[i]].y+faces[indexFaces[i]].height), cv::Scalar(255,0,255), 3);
        faceROI = frame_gray( faces[indexFaces[i]] );
    }

}

double areaROI(const cv::Rect& face){
    return face.width*face.height;
}

bool checkSameValue(const std::vector<int>& indexFaces, int compare_value){
    for (int idLargeFace = 0; idLargeFace < indexFaces.size(); ++idLargeFace) {
        if(compare_value == indexFaces[idLargeFace])
            return true;
        else
            return false;
    }
    return false;
}

void limitFaces(const std::vector<cv::Rect>& faces, std::vector<int>& indexFaces, int countFace){
    if(faces.size() < countFace){
        for (int i = 0; i < faces.size(); ++i) {
            indexFaces.push_back(i);
        }
    }

    else{
        while(indexFaces.size() < countFace) {
            maxAreaValue = 0;
            for (int i = 0; i < faces.size(); ++i) {
                if ( checkSameValue(indexFaces, i) ) {
                    continue;
                }
                if( areaROI(faces[i]) >  maxAreaValue ){
                    maxAreaValue = areaROI(faces[i]);
                    id_max = i;
                }
            }
            indexFaces.push_back(id_max);
        }
    }
}


