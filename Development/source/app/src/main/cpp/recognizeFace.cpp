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
#include <fstream>

JavaVM *jvm;       /* denotes a Java VM */
JNIEnv *env;       /* pointer to native method interface */

// Get the path to your CSV.
std::string fn_csv;
// These vectors hold the images and corresponding labels.
std::vector<cv::Mat> images;
std::vector<int> labels;
std::map<int, std::string> keyName;
static bool trainStatus = false;
static bool loadModel = false;
cv::Mat faceMat;

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

bool fexists(const char *filename)
{
    std::ifstream ifile(filename);
    return ifile;
}


std::string intToString(int number){
    std::stringstream ss;
    ss << number;
    return ss.str();
}


static void read_csv(const std::string& filename, std::vector<cv::Mat>& images, std::vector<int>& labels, std::map<int, std::string>& keyName, char separator = ';') {
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
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_trainModelLBPH(
        JNIEnv *env,
        jobject /* this */,
        jlong model) {
    cv::face::LBPHFaceRecognizer* model_data = (cv::face::LBPHFaceRecognizer*) model;
    if(!trainStatus){
        trainModel(model_data);
        trainStatus = true;
    }
}


JNIEXPORT void JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_findLandmark(
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat) {
    cv::Mat *frame = (cv::Mat*) imgMat;

    if(loadModel == false){
        // Create an instance of Facemark
        cv::Ptr<cv::face::Facemark> facemark = cv::face::FacemarkLBF::create();

        // Load landmark detector
        facemark->loadModel("/sdcard/data/lbfmodel.yaml");


        // Find face
        std::vector<cv::Rect> rect_face;
        rect_face.push_back(cv::Rect(0, 0, (*frame).rows-1, (*frame).cols-1));

        // Variable for landmarks.
        // Landmarks for one face is a vector of points
        // There can be more than one face in the image. Hence, we
        // use a vector of vector of points.
        std::vector <std::vector <cv::Point2f> > landmarks;

        // Run landmark detector
        bool success = facemark->fit((*frame),rect_face,landmarks);

        if(success)
        {
            // If successful, render the landmarks on the face
            for(int i = 0; i < landmarks.size(); i++)
            {
                cv::face::drawFacemarks((*frame), landmarks[i], cv::Scalar(0,0,255,255));
            }
        }
        loadModel = true;
    }
}

JNIEXPORT jstring JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_faceRecognize(
        JNIEnv *env,
        jobject /* this */,
        jlong img,
        jlong model) {
//    cv::Mat* image = (cv::Mat*) img;
//    cv::face::LBPHFaceRecognizer* model_recog = (cv::face::LBPHFaceRecognizer*) model;
//    cv::Mat gray;
//    cv::cvtColor(*image, gray, cv::COLOR_BGR2GRAY);
//    cv::resize(gray, gray, cv::Size(92, 112));
//    int prediction = model_recog->predict(gray);
//    std::string box_text = cv::format("Prediction = %s", keyName[prediction].c_str());

    return env->NewStringUTF("");
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

