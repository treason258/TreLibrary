/*
 * This file provides some of the most commonly used application interfaces.
 */
#ifndef EASYPR_API_HPP
#define EASYPR_API_HPP

#include <string>
#include <vector>
#include <opencv2/opencv.hpp>
#include "../include/config.h"
#include "../include/plate.hpp"
#include "../include/plate_judge.h"
#include "../include/plate_locate.h"
#include "../include/plate_detect.h"
#include "../include/plate_recognize.h"

namespace easypr {
    
    namespace api {
        using namespace cv;
        using namespace std;
        
        static bool plate_judge(const char* image, const char* model) {
            cv::Mat src = cv::imread(image);
            
            assert(!src.empty());
            
            int result;
            PlateJudge::instance()->plateJudge(src, result);
            
            return result == 1;
        }
        
        static void plate_locate(const char* image, vector<Mat> &results, const bool life_mode = true) {
            cv::Mat src = cv::imread(image);
            
            assert(!src.empty());
            
            CPlateLocate plate;
            plate.setDebug(1);
            plate.setLifemode(life_mode);
            
            //std::vector<cv::Mat> results;
            
            plate.plateLocate(src, results);
        }
        
        static int plate_detect(const char* image, std::vector<CPlate> &resultVec, const bool life_mode = true, const int index = 0) {
            cv::Mat src = cv::imread(image);
            
            assert(!src.empty());
            
            CPlateDetect pd;
            pd.setDetectType(PR_DETECT_CMSER);
            pd.setPDLifemode(life_mode);
            
            int result = pd.plateDetect(src, resultVec, index);
            return result;
        }
        
        /////////////////add by liangmin 20180818 ///////////////////
        static int plate_detect(const char* image, std::vector<Point2f> &resultPts, const bool life_mode = true, const int index = 0) {
            cv::Mat src = cv::imread(image);
            assert(!src.empty());
            
            if (src.rows > 500 || src.cols > 700)
            {
                double scale = 1.0;
                src = scaleImage(src, cv::Size(500,700), scale);
            }
            
            
            CPlateDetect pd;
            pd.setDetectType(PR_DETECT_CMSER);
            pd.setPDLifemode(life_mode);
            
            int result = pd.plateDetect(src, resultPts, index);
            return result;
        }
        
        /////////////////add by liangmin 20180818 ///////////////////
        static int plate_detect(const unsigned char* data, int width, int height, std::vector<Point2f> &resultPts, const bool life_mode = true, const int index = 0) {
            cv::Mat src(height, width, CV_8UC3, (void*)data);
            
            if (0) {
                //
                imshow("src", src);
                waitKey(0);
                destroyWindow("src");
            }
            
            assert(!src.empty());
            
            if (0)
            {
                if (src.rows > 500 || src.cols > 700)
                {
                    double scale = 1.0;
                    src = scaleImage(src, cv::Size(500, 700), scale);
                }
            }
            
            CPlateDetect pd;
            pd.setDetectType(PR_DETECT_CMSER);
            pd.setPDLifemode(life_mode);
            
            int result = pd.plateDetect(src, resultPts, index);
            return result;
        }
        ///////////////////////////////////////////////////////////
        
        /////////////////add by liangdas///////////////////
        static int plate_detect(const char* pStream, int nStreamLen, std::vector<Point2f> &resultPts, const bool life_mode = true, const int index = 0) {
            
            std::vector<char> data(pStream, pStream + nStreamLen);
            Mat src = imdecode(Mat(data), 1);
            
            if (0) {
                //
                imshow("src", src);
                waitKey(0);
                destroyWindow("src");
            }
            
            assert(!src.empty());
            

			double scale = 1.0;
			
                if (src.rows > 500 || src.cols > 700)
                {
                    src = scaleImage(src, cv::Size(500, 700), scale);
                }

            
            CPlateDetect pd;
            pd.setDetectType(PR_DETECT_CMSER);
            pd.setPDLifemode(life_mode);
            
            int result = pd.plateDetect(src, resultPts, index);
			if (abs(scale - 1.0) > DBL_EPSILON){
				for (int i = 0; i < resultPts.size(); i++){
					resultPts[i].x = resultPts[i].x * scale;
					resultPts[i].y = resultPts[i].y * scale;
				}
			}
            return result;
        }
        ///////////////////////////////////////////////////////////
        
        ///////////////////////////////////////////////////////////
        static std::vector<std::string> plate_recognize(const char* image,
                                                        const char* model_svm,
                                                        const char* model_ann,
                                                        const bool life_mode = true) {
            cv::Mat img = cv::imread(image);
            
            assert(!img.empty());
            
            CPlateRecognize pr;
            pr.setLifemode(life_mode);
            pr.setDebug(false);
            
            std::vector<std::string> results;
            pr.plateRecognize(img, results);
            
            return std::move(results);
        }
        
        static Color get_plate_color(const char* image) {
            cv::Mat img = cv::imread(image);
            
            assert(!img.empty());
            
            return getPlateType(img, true);
        }
        
        /////////////////add by liangmin ///////////////////
        
        static void InitialModel(std::string path) {
            std::string svm_path = path; svm_path.append("/svm.xml");
            std::string ann_path = path; ann_path.append("/ann.xml");
            std::string annChinese_path = path; annChinese_path.append("/ann_chinese.xml");
            std::string etc_province_path = path; etc_province_path.append("/province_mapping");
            
            PlateJudge::instance(svm_path);
            CharsIdentify::instance(ann_path, annChinese_path, etc_province_path);
        }
        
        static void ReleaseModel() {
            /*PlateJudge* instance = PlateJudge::getInstance();
             instance->releaseModel();*/
        }
        /////////////////////////////////////////////////////
        
        /////////////////add by liangmin 20180818 ///////////////////
        static int plate_detect(cv::Mat& src, std::vector<Point2f> &resultPts, const bool life_mode = true, const int index = 0) {
            //cv::Mat src = cv::imread(image);
			cv::Mat srcTemp;
			src.copyTo(srcTemp);
			double scale = 1.0;
			if (srcTemp.rows > 500 || srcTemp.cols > 700){
                srcTemp = scaleImage(srcTemp, cv::Size(500, 700), scale);
			}


			//detect
            CPlateDetect pd;
            pd.setDetectType(PR_DETECT_CMSER);
            pd.setPDLifemode(life_mode);
			int result = pd.plateDetect(srcTemp, resultPts, index);
				
			//re-scale
			if (abs(scale - 1.0) > DBL_EPSILON && result != 0){
				for (int i = 0; i < resultPts.size(); i++){
					resultPts[i].x = resultPts[i].x * scale;
					resultPts[i].y = resultPts[i].y * scale;
				}
			}
            return result;
        }
    }
}

#endif  // EASYPR_API_HPP
