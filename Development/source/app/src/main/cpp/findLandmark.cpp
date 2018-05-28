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

#define PI 3.14159265



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

cv::Point2f get_left_eye_centroid(const std::vector<cv::Point2f> & landmark){
    cv::Point2f center;
    float x, y;
    for (int i = 36; i <= 41; ++i)
    {
        x += landmark[i].x;
        y += landmark[i].y;
    }

    center.x = x / 6.0;
    center.y = y / 6.0;

    return center;
}

cv::Point2f get_right_eye_centroid(const std::vector<cv::Point2f> & landmark){
    cv::Point2f center;
    float x, y;
    for (int i = 42; i <= 47; ++i)
    {
        x += landmark[i].x;
        y += landmark[i].y;
    }

    center.x = x / 6.0;
    center.y = y / 6.0;

    return center;
}

bool check_rotate_face(const std::vector<cv::Point2f> landmark){

    float distance = 0;
    cv::Point2f vector_eyes = cv::Point2f(landmark[29].x - landmark[27].x, landmark[29].y-landmark[27].y);
    float denominator = sqrt(vector_eyes.x*vector_eyes.x + vector_eyes.y*vector_eyes.y);

    cv::Point2f coor_mid_eyes = landmark[27];

    for (int i = 27; i <= 35; ++i)
    {
        // calculated eviation nose to eyes axis line
        distance += (vector_eyes.y*landmark[i].x - vector_eyes.x*landmark[i].y
                     - vector_eyes.y*coor_mid_eyes.x + vector_eyes.x*coor_mid_eyes.y) / denominator;
    }

    float dif = abs(distance);

    if(dif > 10.0)
        return true;

    return false;
}

bool check_tilted_face(const cv::Point2f& vector_1, const cv::Point2f& vector_2){

    float numerator = vector_1.x*vector_2.x + vector_1.y*vector_2.y;

    float denominator =  sqrt(vector_1.x*vector_1.x + vector_1.y*vector_1.y)
                         *sqrt(vector_2.x*vector_2.x + vector_2.y*vector_2.y);

    float ratio = numerator/denominator;

    float angle =  acos(ratio)* 180.0 / PI;;
    if (angle >= 5){
        return true;
    }
    return false;
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

void drawFacemarks(cv::InputOutputArray image, cv::InputArray points, cv::Scalar color){
    cv::Mat img = image.getMat();
    std::vector<cv::Point2f> pts = points.getMat();
    for(size_t i=0;i<pts.size();i++){
        circle(img, pts[i],10, color,-1);
    }
}

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_example_wilson_humancharacteristics_CameraDetect_CameraDetectActivity_findLandmark(
        JNIEnv *env,
        jobject /* this */,
        jlong imgMat) {
    cv::Mat *frame = (cv::Mat*) imgMat;
    cv::Mat dst = cv::Mat::zeros((*frame).size(), CV_8UC1);
    std::string status = "";

    if(!loadModelStatus){
        face_cascade_.load("/sdcard/data/lbpcascade_frontalface_improved.xml");
        facemark = cv::face::FacemarkKazemi::create(params);
        facemark->setFaceDetector((cv::face::FN_FaceDetector)myDetector, &face_cascade_);
        facemark->loadModel("/sdcard/data/face_landmark_model.dat");
        loadModelStatus = true;
    }
    else{
        faces.push_back(cv::Rect(0,0,frame->rows, frame->cols));
//        facemark->getFaces((*frame), faces);

        if(facemark->fit((*frame),faces, shapes)) {

            for (size_t i = 0; i < faces.size(); i++) {
                cv::rectangle(dst, faces[i], cv::Scalar(255));
                cv::Point2f leftEye = get_left_eye_centroid(shapes[i]);
                cv::Point2f rightEye = get_right_eye_centroid(shapes[i]);

                cv::Point2f vector_eyes = cv::Point2f(rightEye.x-leftEye.x, rightEye.y-leftEye.y);

                if(check_tilted_face(cv::Point2f(1,0),  vector_eyes)){
                    status+="1";
                } else
                    status+="0";
                if(check_rotate_face(shapes[i])){
                    status+="1";
                } else
                    status+="0";
            }
            for (unsigned long i = 0; i < faces.size(); i++) {
                drawFacemarks(dst, shapes[i], cv::Scalar(255));
            }
        }
        faces.clear();
    }
    (*frame) = dst.clone();
    return env->NewStringUTF(status.c_str());
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

