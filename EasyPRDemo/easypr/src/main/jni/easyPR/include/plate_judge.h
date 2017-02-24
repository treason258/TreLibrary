#ifndef EASYPR_CORE_PLATEJUDGE_H_
#define EASYPR_CORE_PLATEJUDGE_H_

#include "../include/plate.hpp"
#include "../include/feature.h"

namespace easypr {
    
    class PlateJudge {
    public:
        static PlateJudge* instance();
        
        void LoadModel(std::string path);
        
        int plateJudge(const std::vector<CPlate> &, std::vector<CPlate> &);
        int plateJudgeUsingNMS(const std::vector<CPlate> &, std::vector<CPlate> &, int maxPlates = 5);
        
        int plateJudge(const std::vector<Mat> &, std::vector<Mat> &);
        
        int plateJudge(const Mat &inMat, int &result);
        int plateSetScore(CPlate& plate);
        
        /////////////////add by liangmin///////////////////
        static PlateJudge* instance(std::string svmpath);
        static PlateJudge* getInstance();
        void releaseModel();
        /////////////////////////////////////////////////////
        
    private:
        PlateJudge();
        PlateJudge(std::string svmpath);
        
        static PlateJudge* instance_;
        
        svmCallback extractFeature;
        
        cv::Ptr<ml::SVM> svm_;
        
    };
}

#endif  // EASYPR_CORE_PLATEJUDGE_H_