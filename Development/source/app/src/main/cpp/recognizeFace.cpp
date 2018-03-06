//
// Created by enclaveit on 01/03/2018.
//

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect.hpp>
#include "recognizeFace.h"



recognizeFace::recognizeFace(){
    this->numWeight = 10;
    this->weightCNNs.resize(numWeight);
}

recognizeFace::recognizeFace(std::vector<double> weightCNNs){
    if(weightCNNs.size()==0){
        recognizeFace();
    }
    else
        this->weightCNNs = weightCNNs;
}

recognizeFace::~recognizeFace(){
    weightCNNs.clear();
}


void recognizeFace::train(){

}
void recognizeFace::predict(){

}
void recognizeFace::recognize(cv::Mat& imgFace){

}
