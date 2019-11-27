# -*- coding:utf-8 -*-
import urllib
import urllib2
import re
import os
import time
from bs4 import BeautifulSoup

import ssl

ssl._create_default_https_context = ssl._create_unverified_context


class ImageSpider(object):

    def __init__(self):
        pass

    def saveImage(self, imageUrl, imagePath):
        try:
            request = urllib2.Request(imageUrl, headers=headers)
            imageData = urllib2.urlopen(request).read()
            with open(imagePath, "wb") as f:
                f.write(imageData)
            time.sleep(0.1)
        except BaseException:
            print '下载失败 | imagePath = ' + imagePath + " | imageUrl = " + imageUrl
        else:
            print '下载成功 | imagePath = ' + imagePath + " | imageUrl = " + imageUrl


if __name__ == '__main__':

    print "--------------------------------项目配置信息"
    headers = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.47 Safari/537.36"}
    homeDir = os.environ['HOME']
    imageDir = homeDir + '/Downloads/python/ImageSpider-8630vip/'
    print "用户目录 | home_dir = " + str(homeDir)
    print "下载目录 | imageDir = " + str(imageDir)

    print "--------------------------------创建下载目录"
    isdir = os.path.isdir(imageDir)
    if isdir:
        print "下载目录已存在，不需要创建"
    else:
        print "下载目录不存在，开始创建"
        os.makedirs(imageDir)

    print "--------------------------------解析网页代码"
    url = "https://yy.8630vip.com/home/book/capter/id/28504"
    print "网页地址 | url = " + url
    request = urllib2.Request(url, headers=headers)
    response = urllib2.urlopen(request)
    text = response.read()
    print "打印网页内容"
    print text

    # soup = BeautifulSoup(text, 'lxml')
    # print "title = " + soup.title.text

    # patternStr = r"(?<=img src=\").+?\.jpg(?=\" alt=\"图)"
    patternStr = r"(?<=img src=\").+?(?=1.jpg\" alt=\"图)"
    pattern = re.compile(patternStr)
    imageUrls = pattern.findall(text)
    print "打印图片列表"
    print imageUrls

    imageUrlPrefix = imageUrls[0]
    print "imageUrlPrefix = " + imageUrlPrefix

    imageSpider = ImageSpider()
    for i in range(1, 2):
        imageUrl = imageUrlPrefix + ("%d.jpg" % i)
        imagePath = imageDir + ("%d.jpg" % i)
        imageSpider.saveImage(imageUrl, imagePath)
