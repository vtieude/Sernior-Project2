//
// Created by enclaveit on 01/03/2018.
//

#ifndef SENIOR_PROJECT_RECOGNIZEFACE_H
#define SENIOR_PROJECT_RECOGNIZEFACE_H
#include <iostream>
#include <vector>

class recognizeFace {
private:
    int numWeight;
    std::vector<double> weightCNNs;
//    double* weightCNNs;
public:
    void train();
    void predict();
    void recognize(cv::Mat& imgFace);
    recognizeFace();
    recognizeFace(std::vector<double>);
    ~recognizeFace();
};

#endif //SENIOR_PROJECT_RECOGNIZEFACE_H
