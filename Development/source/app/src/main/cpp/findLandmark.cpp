//
// Created by enclaveit on 22/03/2018.
//

#include <jni.h>
#include <fstream>
#include "opencv2/face.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/objdetect.hpp"
#include "opencv2/imgproc.hpp"
#include <iostream>
JavaVM *jvm;       /* denotes a Java VM */
JNIEnv *env;       /* pointer to native method interface */

// Get the path to your CSV.
std::string fn_csv;
// These vectors hold the images and corresponding labels.
std::vector<cv::Mat> images;
std::vector<int> labels;
std::map<int, std::string> keyName;
bool trainStatus = false;
bool loadModelStatus = false;
cv::Mat faceMat;

//pass the face cascade xml file which you want to pass as a detector
cv::CascadeClassifier face_cascade_;
cv::face::FacemarkKazemi::Params params;
static cv::Ptr<cv::face::FacemarkKazemi> facemark;
std::vector<cv::Rect> faces;
std::vector< std::vector<cv::Point2f> > shapes;

cv::Mat norm_0_255(cv::InputArray _src) {
    cv::Mat src = _src.getMat();
    // Create and return normalized image:
    cv::Mat dst;
    switch(src.channels()) {
        case 1:
            cv::normalize(_src, dst, 0, 255, cv::NORM_MINMAX, CV_8UC1);
            break;
        case 3:
            cv::normalize(_src, dst, 0, 255, cv::NORM_MINMAX, CV_8UC3);
            break;
        default:
            src.copyTo(dst);
            break;
    }
    return dst;
}


std::string intToString(int number){
    std::stringstream ss;
    ss << number;
    return ss.str();
}

static bool myDetector(cv::InputArray image, cv::OutputArray faces, cv::CascadeClassifier *face_cascade)
{
    cv::Mat gray;

    if (image.channels() > 1)
        cvtColor(image, gray, cv::COLOR_BGR2GRAY);
    else
        gray = image.getMat().clone();

    equalizeHist(gray, gray);

    std::vector<cv::Rect> faces_;
    face_cascade->detectMultiScale(gray, faces_, 1.1, 3, 0, cv::Size(30, 30));
    cv::Mat(faces_).copyTo(faces);
    return true;
}

void read_csv(const std::string& filename, std::vector<cv::Mat>& images, std::vector<int>& labels, std::map<int, std::string>& keyName, char separator = ';') {
    std::ifstream file(filename.c_str(), std::ifstream::in);
    std::string nameHuman[3];
    if (!file) {
        std::string error_message = "No valid input file was given, please check the given filename.";
        CV_Error(CV_StsBadArg, error_message);
    }
    std::string line, path, classlabel;
    while (getline(file, line)) {
        std::stringstream liness(line);
        getline(liness, path, separator);
        getline(liness, classlabel);
        if(!path.empty() && !classlabel.empty()) {
            cv::Mat img = cv::imread(path, cv::IMREAD_GRAYSCALE);
            normalize(img, img, 0, 255, cv::NORM_MINMAX);
            images.push_back(img);
            labels.push_back(atoi(classlabel.c_str()));

            std::size_t pos = path.find('/');      // position of "live" in str
            std::string str = path.substr (pos+1);     // get from "live" to the end
            pos = str.find('/');
            str = str.substr(0,pos);
            keyName[ atoi(classlabel.c_str())] = str;
        }
    }
}

void trainModel(cv::face::LBPHFaceRecognizer* model){
    try {
            fn_csv = "/sdcard/data/at.txt";
            read_csv(fn_csv, images, labels, keyName);
            if (static_cast<int>(images.size())>0 && static_cast<int>(labels.size())>0 && !trainStatus){
                model->train(images, labels);
                trainStatus = true;
            }
            if (model->empty()){
                std::cerr << "Error " << std::endl;
                exit(-1);
            }

    } catch (cv::Exception& e) {
        std::cerr << "Error opening file \"" << fn_csv << "\". Reason: " << e.msg << std::endl;
        // nothing more we can do
        exit(1);
    }
}


void drawLine(cv::Mat& img){
    cv::line(img, cv::Point(0, 0), cv::Point(img.cols, img.rows),cv::Scalar(0,0,255,255), 3);
    cv::line(img, cv::Point(img.cols, 0), cv::Point(0, img.rows),cv::Scalar(0,0,255,255), 3);
}

extern "C" {

JNIEXPORT void JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_findLandmark(
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat) {
    cv::Mat *frame = (cv::Mat*) imgMat;
    cv::Mat dst = cv::Mat::zeros((*frame).size(), CV_8UC1);
    if(!loadModelStatus){
        face_cascade_.load("/sdcard/data/lbpcascade_frontalface_improved.xml");
        facemark = cv::face::FacemarkKazemi::create(params);
        facemark->setFaceDetector((cv::face::FN_FaceDetector)myDetector, &face_cascade_);
        facemark->loadModel("/sdcard/data/face_landmark_model.dat");
        loadModelStatus = true;
    }
    else{
        faces.push_back(cv::Rect(0,0,frame->rows, frame->cols));
//        facemark->getFaces((*frame),faces);
        if(facemark->fit((*frame),faces, shapes)) {
//            (*frame) = cv::Mat::zeros((*frame).size(), CV_8UC1);

            for (size_t i = 0; i < faces.size(); i++) {
                cv::rectangle((*frame), faces[i], cv::Scalar(255, 0, 0));
            }
            for (unsigned long i = 0; i < faces.size(); i++) {
                for (unsigned long k = 0; k < shapes[i].size(); k++)
                    cv::circle((*frame), shapes[i][k], 5, cv::Scalar(0, 0, 255), cv::FILLED);
                cv::face::drawFacemarks(dst, shapes[i], cv::Scalar(255));
            }
        }
        faces.clear();
    }
    (*frame) = dst.clone();
}

JNIEXPORT void JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_drawLine (
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat){
    cv::Mat *img = (cv::Mat*) imgMat;
    drawLine(*img);
}
}

