# -*- coding:utf-8 -*-
import urllib2
import re
import os
import time
import RequestSender


class Spider(object):
    imageDir = '/Users/treason/Downloads/HelloPython/'
    index = 0

    def __init__(self):
        pass

    def getImagePageRange(self):
        imageDirMK = 'mkdir ' + Spider.imageDir
        print('---------------- begin ----------------')
        print(imageDirMK)
        os.system(imageDirMK)  # 创建保存图片的目录
        time.sleep(0.2)

        url = "http://m.yaohu.info/meinv/8/1412.html"
        print('url = ' + url)

        text = RequestSender.getWebpageContent(url)
        print('text = ' + text)

        patternStr = r"(?<=data-original=\").+?(?=\">)"
        pattern = re.compile(patternStr)
        imageUrlArray = pattern.findall(text)
        print(imageUrlArray)

        for imageUrl in imageUrlArray:
            print('\n网络图片地址：' + imageUrl)
            imageName = ("%d.jpg" % Spider.index)
            # RequestSender.saveImage2(imageUrl, Spider.imageDir, imageName)
            RequestSender.saveImage3(imageUrl, Spider.imageDir, imageName)
            Spider.index += 1


spider = Spider()
spider.getImagePageRange()
