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
cv::CascadeClassifier face_cascade("/sdcard/data/haarcascade_frontalface_alt.xml");

// Limit face detect
int id_max;
std::vector<int> indexFaces;
double maxAreaValue = 0;
int i=0, j=0;

bool checkSameValue(const std::vector<int>& indexFaces, int compare_value);
void limitFaces(const std::vector<cv::Rect>&, std::vector<int>&, int);

void haarCascadeDetect(cv::Mat& frame, int countFaces );
static bool myDetector(cv::InputArray image, cv::OutputArray faces, cv::CascadeClassifier *face_cascade);
double areaROI(const cv::Rect&);

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
        jlong imgMat,
        jint countFaces
        ) {
    cv::Mat* img = (cv::Mat*) imgMat;
    int number = (int) countFaces;
    haarCascadeDetect(*img, number);
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

void haarCascadeDetect(cv::Mat& frame, int countFaces ){
    if (!initialized) {
        initialized = true;
        facemark->setFaceDetector((cv::face::FN_FaceDetector)myDetector, &face_cascade);
        facemark->loadModel("/sdcard/data/face_landmark_model.dat");
    }

/* Use for find landmark */
    faces.clear();
    shapes.clear();

    facemark->getFaces(frame,faces);
    removeIntersect(faces);
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



//    if(face_cascade.empty()){
//        printf("--(!)Error loading\n"); return;
//    }
//
//    cv::cvtColor( frame, frame_gray, cv::COLOR_RGBA2GRAY );
//    cv::equalizeHist( frame_gray, frame_gray );
//
//    //-- Detect faces
//    face_cascade.detectMultiScale( frame_gray, faces, 1.3, 3, 0|cv::CASCADE_SCALE_IMAGE, cv::Size(50, 50) );
//
//    // -- Limiting face recognition.
//    indexFaces.clear();
//    limitFaces(faces, indexFaces, countFaces);
//
//    for( size_t i = 0; i < indexFaces.size(); i++ )
//    {
//        cv::rectangle(frame, cv::Point(faces[indexFaces[i]].x, faces[indexFaces[i]].y),
//                      cv::Point(faces[indexFaces[i]].x + faces[indexFaces[i]].width, faces[indexFaces[i]].y+faces[indexFaces[i]].height), cv::Scalar(255,0,255), 3);
//    }
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

void limitFaces(const std::vector<cv::Rect>& faces, std::vector<int>& indexFaces, int countFace){
    if(faces.size() < countFace){
        for ( i = 0; i < static_cast<int>(faces.size()); ++i) {
            indexFaces.push_back(i);
        }
    }
    else{
        while(indexFaces.size() < countFace) {
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
            indexFaces.push_back(id_max);
        }
    }
}

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
    if( rect1.x < rect2.x && rect1.height > rect2.height &&
            rect1.y < rect2.y && rect1.width > rect2.width ){
        return true;
    }
    else if( rect2.x < rect1.x && rect2.height > rect1.height &&
            rect2.y < rect1.y && rect2.width > rect1.width ){
        return true;
    }
    return false;
}
