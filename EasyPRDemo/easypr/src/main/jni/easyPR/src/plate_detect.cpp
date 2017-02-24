#include "../include/plate_detect.h"
#include "../include/util.h"
#include "../include/config.h"
#include "../include/core_func.h"
//#include <cv.h>
//#include <highgui.h>

namespace easypr {
    
    CPlateDetect::CPlateDetect() {
        m_plateLocate = new CPlateLocate();
        
        m_maxPlates = 3;
        m_type = 0;
        
        m_showDetect = false;
    }
    
    CPlateDetect::~CPlateDetect() { SAFE_RELEASE(m_plateLocate); }
    
    int CPlateDetect::plateDetect(Mat src, std::vector<CPlate> &resultVec, int type,
                                  bool showDetectArea, int img_index) {
        
        std::vector<CPlate> sobel_Plates;
        sobel_Plates.reserve(16);
        std::vector<CPlate> color_Plates;
        color_Plates.reserve(16);
        std::vector<CPlate> mser_Plates;
        mser_Plates.reserve(16);
        
        std::vector<CPlate> all_result_Plates;
        all_result_Plates.reserve(64);
        
#pragma omp parallel sections
        {
#pragma omp section
            {
                if (!type || type & PR_DETECT_SOBEL) {
                    m_plateLocate->plateSobelLocate(src, sobel_Plates, img_index);
                }
            }
#pragma omp section
            {
                if (!type || type & PR_DETECT_COLOR) {
                    m_plateLocate->plateColorLocate(src, color_Plates, img_index);
                }
            }
#pragma omp section
            {
                if (!type || type & PR_DETECT_CMSER) {
                    m_plateLocate->plateMserLocate(src, mser_Plates, img_index);
                }
            }
        }
        
        for (auto plate : sobel_Plates) {
            plate.setPlateLocateType(SOBEL);
            all_result_Plates.push_back(plate);
        }
        
        for (auto plate : color_Plates) {
            plate.setPlateLocateType(COLOR);
            all_result_Plates.push_back(plate);
        }
        
        for (auto plate : mser_Plates) {
            plate.setPlateLocateType(CMSER);
            all_result_Plates.push_back(plate);
        }
        
        // use nms to judge plate
        // m_maxPlates is the max plates enable to detect
        PlateJudge::instance()->plateJudgeUsingNMS(all_result_Plates, resultVec, m_maxPlates);
        
        if (showDetectArea || getDetectShow()) {
            int index = 0;
            
            Mat result;
            src.copyTo(result);
            
            for (size_t j = 0; j < resultVec.size(); j++) {
                CPlate item = resultVec[j];
                Mat plate = item.getPlateMat();
                
                int height = 36;
                int width = 136;
                if (height * index + height < result.rows) {
                    Mat imageRoi = result(Rect(0, 0 + height * index, width, height));
                    addWeighted(imageRoi, 0, plate, 1, 0, imageRoi);
                }
                index++;
                
                RotatedRect minRect = item.getPlatePos();
                Point2f rect_points[4];
                minRect.points(rect_points);
                
                Scalar lineColor = Scalar(255, 255, 255);
                
                if (item.getPlateLocateType() == SOBEL) lineColor = Scalar(255, 0, 0);
                if (item.getPlateLocateType() == COLOR) lineColor = Scalar(0, 255, 0);
                if (item.getPlateLocateType() == CMSER) lineColor = Scalar(0, 0, 255);
                
                for (int j = 0; j < 4; j++)
                    line(result, rect_points[j], rect_points[(j + 1) % 4], lineColor, 2, 8);
            }
            
            if (1) {
                std::stringstream ss(std::stringstream::in | std::stringstream::out);
                ss << "result_plate_" << img_index << ".jpg";
                imwrite(ss.str(), result);
                /////////////////add by liangmin///////////////////
                if (0) {
                    //
                    imshow("detect_result", result);
                    waitKey(0);
                    destroyWindow("detect_result");
                }
                /////////////////////////////////////
            }
        }
        
        return 0;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////
    // added by liangmin ,process the plate detected
    int CPlateDetect::plateCheck(Mat src, vector<CPlate> inVec, vector<CPlate>& resultVec, int img_index) {
        if (inVec.size() == 0)
            return 0;
        
        //border remove
        vector<CPlate> resultVec1;
        for (auto plate : inVec) {
            CPlate resultPlate;
            borderCheck(src, plate, resultPlate,img_index);
            resultVec1.push_back(resultPlate);
        }
        int num = resultVec1.size();
        
        //color check
        float percent_max = 0.0f;
        int index_max = -1;
        for (int i = 0; i < resultVec1.size(); i++) {
            CPlate plate = resultVec1[i];
            float percent = 0.0f;
            colorCheck(src, plate, percent, img_index);
            if (percent >= percent_max){
                percent_max = percent;
                index_max = i;
            }
        }
        
        resultVec.push_back(resultVec1[index_max]);
        num = resultVec.size();
        
        return num;
    }
    
    void CPlateDetect::borderCheck(Mat src, CPlate inPlate, CPlate& resultPlate, int img_index){
        // the threshld for the diff of histogram
        float threshld = 0.05f;
        
        //histgram parameters
        int b_bins = 50; int g_bins = 60;    //use channel of GB in RGB colar space
        int histSize[] = { b_bins, g_bins }; // use only Green and Blue channel, cause in chinese plate, the first letter, that
        float b_ranges[] = { 0, 256 };       //is the chinese letter, may be of red color, suan as "jing" "jun" in chinese
        float g_ranges[] = { 0, 256 };
        const float* ranges[] = { b_ranges, g_ranges };
        int channels[] = { 0, 1 };
        
        //plate info
        Mat inMat = inPlate.getPlateMat(); // the inMat is a rectangle which has been drewed as orthogonal
        RotatedRect platePos = inPlate.getPlatePos();
        
        //center of the rect
        int x = 0;
        int y = 0;
        int height = inMat.rows;
        int width = inMat.cols;
        Rect rect1 = { x + width / 3, y, width / 3, height };
        Mat img1 = inMat(rect1);
        Mat hsv1, h1;
        MatND hist1;
        calcHist(&img1, 1, channels, Mat(), hist1, 2, histSize, ranges, true, false);
        
        
        int left, right;
        int slideWidth = int(width / 20);
        int slideStep = int(width / 20);
        
        //left side
        for (int slideX = x; slideX < width - slideWidth - 1; slideX += slideStep) {
            Rect rect2 = { slideX, y, slideWidth, height };
            Mat img2 = inMat(rect2);
            MatND hist2;
            calcHist(&img2, 1, channels, Mat(), hist2, 2, histSize, ranges, true, false);
            double hist_diff = compareHist(hist1, hist2, 0);
            if (hist_diff > threshld){
                left = slideX + slideWidth/2;
                break;
            }
        }
        
        
        //right side
        for (int slideX = x + width - slideWidth; slideX > slideWidth + 1; slideX -= slideStep) {
            Rect rect2 = { slideX, y, slideWidth, height };
            Mat img2 = inMat(rect2);
            Mat hsv2;
            MatND hist2;
            calcHist(&img2, 1, channels, Mat(), hist2, 2, histSize, ranges, true, false);
            double hist_diff = compareHist(hist1, hist2, 0);
            if (hist_diff > threshld){
                right = slideX + slideWidth;
                break;
            }
        }
        
        //do not need to adjust
        if (left == slideWidth / 2 && right == width){
            resultPlate = inPlate;
            return;
        }
        
        // the ratio based on which the plate will be adjusted
        float leftRatio = (float)left / (float)width;
        float rightRatio = (float)(width - right) / float(width);
        
        //the original line of the plate, which will not be changed
        Vec4f line = inPlate.getPlateLine();
        float k = line[1] / line[0];
        float x_1 = line[2];
        float y_1 = line[3];
        Point2f rect_points[4];
        platePos.points(rect_points);
        
        //the original points which line in the line that go through the center of the plate
        int x0 = platePos.center.x;
        int y0 = platePos.center.y;
        int x1 = (rect_points[0].x + rect_points[1].x) / 2;
        int y1 = (rect_points[0].y + rect_points[1].y) / 2;
        int x2 = (rect_points[2].x + rect_points[3].x) / 2;
        int y2 = (rect_points[2].y + rect_points[3].y) / 2;
        
        //the points adjusted
        int x1_ = x1 + leftRatio * (x2 - x1);
        int x2_ = x1 + (1 - rightRatio)* (x2 - x1);
        int x0_ = (x1_ + x2_) / 2;
        int y0_ = k * (x0_ - x_1) + y_1;
        int y1_ = k * (x1_ - x_1) + y_1;
        int y2_ = k * (x2_ - x_1) + y_1;
        
        //the angle is not changed
        float angle = atan(k) * 180 / (float)CV_PI;
        
        //the new width and height of the plate
        width = sqrt((x2_ - x1_) *  (x2_ - x1_) + (y2_ - y1_) *  (y2_ - y1_));
        height = platePos.size.height;
        
        resultPlate = inPlate;
        RotatedRect newPlate(Point2f((float)x0_,(float)y0_),Size2f(width,height), angle);
        resultPlate.setPlatePos(newPlate);
        
        if (0){
            Mat tmp;
            src.copyTo(tmp);
            
            Mat plate1 = inPlate.getPlateMat();//the original plate
            RotatedRect minRect1 = inPlate.getPlatePos();
            Point2f rect_points1[4];
            minRect1.points(rect_points1);
            
            
            Mat plate = resultPlate.getPlateMat();//the new plate
            RotatedRect minRect = resultPlate.getPlatePos();
            Point2f rect_points[4];
            minRect.points(rect_points);
            
            for (int j = 0; j < 4; j++)
            {
                cv::line(tmp, rect_points[j], rect_points[(j + 1) % 4], Scalar(0, 0, 255), 2, 8);
                cv::line(tmp, rect_points1[j], rect_points1[(j + 1) % 4], Scalar(0, 255, 255), 2, 8);
            }
            
            std::stringstream ss(std::stringstream::in | std::stringstream::out);
            ss << "adjust_plate_" << img_index << ".jpg";
            imwrite(ss.str(), tmp);
            
            if (0){
                imshow("plate_adjust", tmp);
                waitKey(0);
                destroyWindow("plate_adjust");
            }
        }
        
        return;
    }
    
    void CPlateDetect::colorCheck(Mat src, CPlate inPlate, float& percent, int img_index){
        Mat inMat = inPlate.getPlateMat();
        int w = inMat.cols;
        int h = inMat.rows;
        Mat tmpMat = inMat(Rect_<double>(w * 0.15, h * 0.1, w * 0.7, h * 0.7));
        
        //if (1){
        //	imshow("inMat", tmpMat);
        //	waitKey(0);
        //	destroyWindow("inMat");
        //}
        
        float blue_percent = 0;
        float yellow_percent = 0;
        float white_percent = 0;
        bool adaptive_minsv = true;
        
        if (plateColorJudge(tmpMat, BLUE, adaptive_minsv, blue_percent) == true) {
            percent = blue_percent;
            return;
        }
        else if (plateColorJudge(tmpMat, YELLOW, adaptive_minsv, yellow_percent) == true) {
            percent = yellow_percent;
            return;
        }
        else if (plateColorJudge(tmpMat, WHITE, adaptive_minsv, white_percent) == true){
            percent = white_percent;
            return;
        }
        
        percent = max(max(blue_percent, yellow_percent), white_percent);
        return;
        
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    
    
    int CPlateDetect::plateDetect(Mat src, std::vector<Point2f> &resultPts, int type,
                                  bool showDetectArea, int img_index) {
        
        std::vector<CPlate> sobel_Plates;
        sobel_Plates.reserve(16);
        std::vector<CPlate> color_Plates;
        color_Plates.reserve(16);
        std::vector<CPlate> mser_Plates;
        mser_Plates.reserve(16);
        
        std::vector<CPlate> all_result_Plates;
        all_result_Plates.reserve(64);
		//
		//#pragma omp parallel sections
		//		{
		//#pragma omp section
		//			  {
		//				  if (!type || type & PR_DETECT_SOBEL) {
		//					  m_plateLocate->plateSobelLocate(src, sobel_Plates, img_index);
		//				  }
		//			  }
		//#pragma omp section
		//			  {
		//				  if (!type || type & PR_DETECT_COLOR) {
		//					  m_plateLocate->plateColorLocate(src, color_Plates, img_index);
		//				  }
		//			  }
		//#pragma omp section
		//			  {
		//				  if (!type || type & PR_DETECT_CMSER) {
		//					  m_plateLocate->plateMserLocate(src, mser_Plates, img_index);
		//				  }
		//			  }
		//		}
		//
		//		for (auto plate : sobel_Plates) {
		//			plate.setPlateLocateType(SOBEL);
		//			all_result_Plates.push_back(plate);
		//		}
		//
		//		for (auto plate : color_Plates) {
		//			plate.setPlateLocateType(COLOR);
		//			all_result_Plates.push_back(plate);
		//		}
		//
		//		for (auto plate : mser_Plates) {
		//			plate.setPlateLocateType(CMSER);
		//			all_result_Plates.push_back(plate);
		//		}
		//
		//		// use nms to judge plate
		//		// m_maxPlates is the max plates enable to detect
		//		vector<CPlate> resultVec2;
		//		PlateJudge::instance()->plateJudgeUsingNMS(all_result_Plates, resultVec2, m_maxPlates);

		//vector<CPlate> resultVec2;
		//m_plateLocate->plateMserLocate(src, mser_Plates, img_index);
		//PlateJudge::instance()->plateJudgeUsingNMS(mser_Plates, resultVec2, m_maxPlates);

		//if (resultVec2.size() == 0){
		//m_plateLocate->plateColorLocate(src, color_Plates, img_index);
		//PlateJudge::instance()->plateJudgeUsingNMS(color_Plates, resultVec2, m_maxPlates);
		//}



		vector<CPlate> resultVec2;
		m_plateLocate->plateMserLocate(src, mser_Plates, img_index);
		PlateJudge::instance()->plateJudgeUsingNMS(mser_Plates, resultVec2, m_maxPlates);

		if (resultVec2.size() == 0){
		m_plateLocate->plateColorLocate(src, color_Plates, img_index);
		PlateJudge::instance()->plateJudgeUsingNMS(color_Plates, resultVec2, m_maxPlates);
		}

		//////////////////// add liangmin re-check model by color may be added here /////////
        vector<CPlate> resultVec;
        plateCheck(src, resultVec2, resultVec,img_index);
        int resultNum = resultVec.size();
        /////////////////////////////////////////////////////////////////////////////////////
        
        if (showDetectArea || getDetectShow()) {
            int index = 0;
            
            Mat result;
            src.copyTo(result);
            
            for (size_t j = 0; j < resultVec.size(); j++) {
                CPlate item = resultVec[j];
                Mat plate = item.getPlateMat();
                
                int height = 36;
                int width = 136;
                if (height * index + height < result.rows) {
                    Mat imageRoi = result(Rect(0, 0 + height * index, width, height));
                    addWeighted(imageRoi, 0, plate, 1, 0, imageRoi);
                }
                index++;
                
                RotatedRect minRect = item.getPlatePos();
                Point2f rect_points[4];
                minRect.points(rect_points);
                
                Scalar lineColor = Scalar(255, 255, 255);
                
                if (item.getPlateLocateType() == SOBEL) lineColor = Scalar(255, 0, 0);
                if (item.getPlateLocateType() == COLOR) lineColor = Scalar(0, 255, 0);
                if (item.getPlateLocateType() == CMSER) lineColor = Scalar(0, 0, 255);
                
                for (int j = 0; j < 4; j++)
                {
                    line(result, rect_points[j], rect_points[(j + 1) % 4], lineColor, 2, 8);
                }
                
				resultPts.push_back(rect_points[1]);
				resultPts.push_back(rect_points[2]);
				resultPts.push_back(rect_points[3]);
				resultPts.push_back(rect_points[0]);
            }
            
			if (0) {
                std::stringstream ss(std::stringstream::in | std::stringstream::out);
                ss << "result_plate_" << img_index << ".jpg";
                imwrite(ss.str(), result);
                
                /////////////////add by liangmin///////////////////
				if (0) {
                    //
                    if (resultPts.size() != 0){
                        for (int i = 0; i < resultPts.size() / 4; i++){
							cv::circle(result, resultPts[i * 4 + 0], 3, Scalar(0, 0, 255), 2);
							cv::circle(result, resultPts[i * 4 + 1], 3, Scalar(0, 255, 0), 2);
							cv::circle(result, resultPts[i * 4 + 2], 3, Scalar(255, 0, 0), 2);
							cv::circle(result, resultPts[i * 4 + 3], 3, Scalar(0, 255, 255), 2);
                        }
                    }
                    
                    imshow("detect_result", result);
                    waitKey(0);
                    destroyWindow("detect_result");
                }
                /////////////////////////////////////
            }
        }
        
        return resultNum;
    }
    /////////////////////////////////////////////////////////////////
    
    int CPlateDetect::plateDetect(Mat src, std::vector<CPlate> &resultVec, int img_index) {
        //int result = plateDetect(src, resultVec, m_type, false, img_index);
        int result = plateDetect(src, resultVec, m_type, true, img_index);
        return result;
    }
    
    /////////////////add by liangmin 20180818 ///////////////////
    int CPlateDetect::plateDetect(Mat src, std::vector<Point2f> &resultPts, int img_index) {
        //int result = plateDetect(src, resultVec, m_type, false, img_index);
        int result = plateDetect(src, resultPts, m_type, true, img_index);
        return result;
    }
    
    
    
    ////////////////////////////////////////////////////////////
    
    void CPlateDetect::LoadSVM(std::string path) {
        PlateJudge::instance()->LoadModel(path);
    }
    
    
    Mat CPlateDetect::showResult(const Mat &result, int img_index) {
        namedWindow("EasyPR", CV_WINDOW_AUTOSIZE);
        
        const int RESULTWIDTH = kShowWindowWidth;   // 640 930
        const int RESULTHEIGHT = kShowWindowHeight;  // 540 710
        
        Mat img_window;
        img_window.create(RESULTHEIGHT, RESULTWIDTH, CV_8UC3);
        
        int nRows = result.rows;
        int nCols = result.cols;
        
        Mat result_resize;
        if (nCols <= img_window.cols && nRows <= img_window.rows) {
            result_resize = result;
            
        }
        else if (nCols > img_window.cols && nRows <= img_window.rows) {
            float scale = float(img_window.cols) / float(nCols);
            resize(result, result_resize, Size(), scale, scale, CV_INTER_AREA);
            
        }
        else if (nCols <= img_window.cols && nRows > img_window.rows) {
            float scale = float(img_window.rows) / float(nRows);
            resize(result, result_resize, Size(), scale, scale, CV_INTER_AREA);
            
        }
        else if (nCols > img_window.cols && nRows > img_window.rows) {
            Mat result_middle;
            float scale = float(img_window.cols) / float(nCols);
            resize(result, result_middle, Size(), scale, scale, CV_INTER_AREA);
            
            if (result_middle.rows > img_window.rows) {
                float scale = float(img_window.rows) / float(result_middle.rows);
                resize(result_middle, result_resize, Size(), scale, scale, CV_INTER_AREA);
            }
            else {
                result_resize = result_middle;
            }
        }
        else {
            result_resize = result;
        }
        
        Mat imageRoi = img_window(Rect((RESULTWIDTH - result_resize.cols) / 2,
                                       (RESULTHEIGHT - result_resize.rows) / 2,
                                       result_resize.cols, result_resize.rows));
        addWeighted(imageRoi, 0, result_resize, 1, 0, imageRoi);
        
        if (1) {
#if 0
            imshow("EasyPR", img_window);
            waitKey(0);
            destroyWindow("EasyPR");
#endif
            
        }
        
        if (0) {
            std::stringstream ss(std::stringstream::in | std::stringstream::out);
            ss << "resources/image/tmp/Result/plate_" << img_index << ".jpg";
            imwrite(ss.str(), img_window);
        }
        
        return img_window;
    }
}