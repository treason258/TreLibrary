# -*- coding:utf-8 -*-
import urllib2
import re
import os
import time
import Utils


class Spider(object):
    imageDir = Utils.getHelloPythonDir()
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

        text = Utils.getHTMLContent(url)
        print('text = ' + text)

        patternStr = r"(?<=data-original=\").+?(?=\">)"
        pattern = re.compile(patternStr)
        imageUrlArray = pattern.findall(text)
        print(imageUrlArray)

        for imageUrl in imageUrlArray:
            imageUrl = 'http:' + imageUrl
            imageName = ("%d.jpg" % Spider.index)
            # Utils.saveImage2(imageUrl, Spider.imageDir, imageName)
            Utils.saveImage3(imageUrl, Spider.imageDir, imageName)
            Spider.index += 1


spider = Spider()
spider.getImagePageRange()
