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
std::vector<cv::Rect> faces;
std::vector< std::vector<cv::Point2f> > shapes;
cv::CascadeClassifier face_cascade("/sdcard/data/lbpcascade_frontalface.xml");

// Limit face detect
int id_max;
static std::vector<int> indexFaces;
static int countFaces = 1;
static double maxAreaValue = 0;
static int i=0, j=0;

bool checkSameValue(const std::vector<int>& indexFaces, int compare_value);
static void limitFaces(const std::vector<cv::Rect>&, std::vector<int>& );

void haarCascadeDetect(cv::Mat& frame );
static bool myDetector(cv::InputArray image, cv::OutputArray faces, cv::CascadeClassifier *face_cascade);
static double areaROI(const cv::Rect&);
static void removeByIndex(std::vector<cv::Rect>&, std::vector<int>& );

static bool initialized;
cv::face::FacemarkKazemi::Params params;
cv::Ptr<cv::face::FacemarkKazemi> facemark = cv::face::FacemarkKazemi::create(params);

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
    haarCascadeDetect(*img );
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

void haarCascadeDetect(cv::Mat& frame ){
    if (!initialized) {
        initialized = true;
        facemark->setFaceDetector((cv::face::FN_FaceDetector)myDetector, &face_cascade);
        facemark->loadModel("/sdcard/data/face_landmark_model.dat");
    }

/* Use for find landmark */
    faces.clear();
    shapes.clear();

    facemark->getFaces(frame,faces);
    if(faces.size()==0){
        std::cout<<"No faces found in this frame"<<std::endl;
    }
    else{
        for (int i = 0; i < static_cast<int>(faces.size()); i++) {
            cv::rectangle(frame, faces[i], cv::Scalar(255, 0, 0));
        }
        //Initialise the shape of the faces
        if (facemark->fit(frame,faces,shapes)) {
            for ( i = 0; i < static_cast<int>(faces.size()); i++) {
                for ( j = 0; j < static_cast<int>(shapes[i].size()); j++)
                    cv::circle(frame, shapes[i][j], 2, cv::Scalar(0, 0, 255), cv::FILLED);
            }
        }
    }
}

double areaROI(const cv::Rect& face){
    return face.width*face.height;
}


static bool myDetector(cv::InputArray image, cv::OutputArray faces, cv::CascadeClassifier *face_cascade)
{
    cv::Mat gray;

    if (image.channels() > 1)
        cv::cvtColor(image, gray, cv::COLOR_BGR2GRAY);
    else
        gray = image.getMat().clone();

    cv::equalizeHist(gray, gray);

    std::vector<cv::Rect> faces_;
    face_cascade->detectMultiScale(gray, faces_, 1.3, 3, 0|cv::CASCADE_SCALE_IMAGE, cv::Size(50, 50));

//    // -- Limiting face recognition.
//    indexFaces.clear();
//    limitFaces(faces_, indexFaces);
//    removeByIndex(faces_, indexFaces);

    cv::Mat(faces_).copyTo(faces);
    return true;
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
