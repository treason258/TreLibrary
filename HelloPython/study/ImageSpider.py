# -*- coding:utf-8 -*-
import urllib
import urllib2
import re
import os
import time

import ssl

ssl._create_default_https_context = ssl._create_unverified_context


class ImageSpider(object):
    num = 0

    def __init__(self):
        pass

    def saveImage(self, imageUrl, imagePath):
        headers = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(imageUrl, headers=headers)
        imageData = urllib2.urlopen(request).read()
        # fileName = imageName[-15:]
        with open(imagePath, "wb") as f:
            f.write(imageData)
        print '正在保存图片 | imagePath = ' + imagePath + " | imageUrl = " + imageUrl
        time.sleep(0.1)

    def getImageFormUrl(self, url):
        print "--------------------------------解析网页代码"
        print "网页地址 | url = " + url
        headers = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(url, headers=headers)
        response = urllib2.urlopen(request)
        text = response.read()
        print "打印网页内容"
        print text
        patternStr = r"(?<=onerror=\"img_error\(this\);\" src=\").+?\.jpg(?=\" referrerpolicy=\"no-referrer\")"
        pattern = re.compile(patternStr)
        imageUrls = pattern.findall(text)
        print "打印图片列表"
        print imageUrls
        for imageUrl in imageUrls:
            imagePath = imageDir + ("/%d.jpg" % ImageSpider.num)
            self.saveImage(imageUrl, imagePath)
            ImageSpider.num += 1


if __name__ == '__main__':

    print "--------------------------------配置信息"
    homeDir = os.environ['HOME']
    imageDir = homeDir + '/Downloads/python/ImageSpider'
    print "用户目录 | home_dir = " + str(homeDir)
    print "下载目录 | imageDir = " + str(imageDir)

    print "--------------------------------创建下载目录"
    isdir = os.path.isdir(imageDir)
    if isdir:
        print "下载目录已存在，不需要创建"
    else:
        print "下载目录不存在，开始创建"
        os.makedirs(imageDir)

    print "--------------------------------输入页码"
    pageFrom = raw_input("输入开始页：")
    pageTo = raw_input("输入结束页：")
    # pageFrom = 1
    # pageTo = 1
    print "开始页码 | pageFrom = " + str(pageFrom)
    print "结束页码 | pageTo = " + str(pageTo)

    print "--------------------------------开始爬虫"
    imageSpider = ImageSpider()
    pageFromInt = int(pageFrom)
    pageToInt = int(pageTo)
    while pageFromInt <= pageToInt:
        print "--------------------------------开始解析"
        url = "http://www.dbmeinv.com/?pager_offset=" + str(pageFromInt)
        print ("正在处理第%d页" % pageFromInt) + " | url = " + url
        imageSpider.getImageFormUrl(url)
        pageFromInt += 1
        print "--------------------------------解析结束"
