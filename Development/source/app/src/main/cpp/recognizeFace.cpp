//
// Created by enclaveit on 22/03/2018.
//

#include <jni.h>
#include <fstream>
#include <opencv2/face.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/core.hpp>
#include <iostream>

cv::Ptr<cv::face::EigenFaceRecognizer> model = cv::face::EigenFaceRecognizer::create();
// Get the path to your CSV.
std::string fn_csv;
// These vectors hold the images and corresponding labels.
std::vector<cv::Mat> images;
std::vector<int> labels;

static cv::Mat norm_0_255(cv::InputArray _src) {
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
static void read_csv(const std::string& filename, std::vector<cv::Mat>& images, std::vector<int>& labels, char separator = ';') {
    std::ifstream file(filename.c_str(), std::ifstream::in);
    if (!file) {
        std::string error_message = "No valid input file was given, please check the given filename.";
        CV_Error(cv::Error::StsBadArg, error_message);
    }
    std::string line, path, classlabel;
    while (getline(file, line)) {
        std::stringstream liness(line);
        getline(liness, path, separator);
        getline(liness, classlabel);
        if(!path.empty() && !classlabel.empty()) {
            images.push_back(cv::imread(path, 0));
            labels.push_back(atoi(classlabel.c_str()));
        }
    }
}

void trainModel(){
    try {
        read_csv(fn_csv, images, labels);
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
JNIEXPORT int JNICALL
Java_com_example_wilson_humancharacteristics_recognizer_PersonRecognizer_trainModelLBPH(
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat) {
    cv::Mat *img = (cv::Mat*) imgMat;
    return 0;
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

