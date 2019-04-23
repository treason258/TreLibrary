# -*- coding:utf-8 -*-
import urllib
import urllib2
import re
import os
import time


 
class HelloSpider(object):
    num = 0;
    dirStr = 'Downloads/python/HelloSpider44'
    imgStr = 'Downloads/python/HelloSpider44/0.jpg'

    
    # print urllib2.urlopen("https://img.q6pk.com/image/20181119/", context=context).read()
    
    def __init__(self):
        pass

    def getImagePageRange(self, fromPage, toPage):
        mkdirStr = 'mkdir ' + HelloSpider.dirStr;
        print mkdirStr
        os.system(mkdirStr)   #创建保存图片的目录
        # mkImgStr = 'touch file ' + HelloSpider.imgStr;
        # print mkImgStr
        # os.system(mkImgStr)   #创建保存图片的目录
        i = int(fromPage)
        while i <= int(toPage):
            # url = "http://www.dbmeinv.com/?pager_offset=" + str(i)
            # url = "https://www.dbmeinv.com/index.htm?pager_offset=" + str(i)
            url = "https://www.dbmeinv.com/index.htm?cid=4&pager_offset=" + str(i)
            print url
            print "\n第%d页" % i
            self.getImageFormUrl(url)
            i += 1 
 
    def getImageFormUrl(self, url):
        headers = {"User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(url, headers = headers)
        response = urllib2.urlopen(request)
        text = response.read()
        # print text
        p1 = r"(?<=\(this\);\" src=\").+?\.jpg(?=\" />)"
        pattern = re.compile(p1)
        imgs = pattern.findall(text)
        print imgs
        for img in imgs:
            imageName = HelloSpider.dirStr + ("/%d.jpg" % (HelloSpider.num))
            imageUrl = img
            if HelloSpider.num == 3:
                imageUrl = 'https://img.q6pk.com/image/20181119/0ba051e0b7747bc8cce970b81cfa0584_938_1370.jpg'
            print imageUrl
            self.saveImage(imageUrl, imageName)
            HelloSpider.num += 1

    def saveImage(self, imageUrl, imageName):
        import ssl
        ssl._create_default_https_context = ssl._create_unverified_context
        headers = {"User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(imageUrl, headers = headers)
        imageData = urllib2.urlopen(request).read()
        with open(imageName, "wb") as f:
             f.write(imageData)
        print '正在保存图片：', imageName
        time.sleep(0.1)


helloSpider = HelloSpider()
# fromPage = raw_input("输入开始页：")
# toPage = raw_input("输入结束页：")
# helloSpider.getImagePageRange(fromPage, toPage)
helloSpider.getImagePageRange(11, 11)









