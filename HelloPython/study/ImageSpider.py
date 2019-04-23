# -*- coding:utf-8 -*-
import urllib
import urllib2
import re
import os
import time
 
class ImageSpider(object):
    num = 0;
    def __init__(self):
        pass
 
    def saveImage(self, imageUrl, imageName):
        headers = {"User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(imageUrl, headers = headers)
        imageData = urllib2.urlopen(request).read()
        # fileName = imageName[-15:]
        with open(imageName, "wb") as f:
             f.write(imageData)
 
        print '正在保存图片：', imageName
        time.sleep(0.1)
 
 
    def getImageFormUrl(self, url):
        headers = {"User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(url, headers = headers)
        response = urllib2.urlopen(request)
        text = response.read()
        p1 = r"(?<=\(this\);\" src=\").+?\.jpg(?=\" />)"
        pattern = re.compile(p1)
        imgs = pattern.findall(text)
        print imgs
        for img in imgs:
            imageName = "Downloads/python/ImageSpider/%d.jpg" % (ImageSpider.num)
            imageUrl = img
            print img
            self.saveImage(imageUrl, imageName)
            ImageSpider.num += 1
 
 
    def getImagePageRange(self, fromPage, toPage):
 
        # os.system('mkdir pic')   #创建保存图片的目录
        os.system('mkdir Downloads/python/ImageSpider')   #创建保存图片的目录
 
        i = int(fromPage)
        while i <= int(toPage):
            url = "http://www.dbmeinv.com/?pager_offset=" + str(i)
            print url
            print "\n第%d页" % i
            self.getImageFormUrl(url)
            i += 1
 
imageSpider = ImageSpider()
beginPage = raw_input("输入开始页：")
endPage = raw_input("输入结束页：")
imageSpider.getImagePageRange(beginPage, endPage)