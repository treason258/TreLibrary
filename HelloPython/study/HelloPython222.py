# -*- coding:utf-8 -*-
import urllib
import urllib2
import re
import os
import time
 
class HelloSpider(object):
    num = 0;
    dirStr = 'Downloads/python/HelloSpider2-1'
    
    def __init__(self):
        pass

    def getImagePageRange(self):
        mkdirStr = 'mkdir ' + HelloSpider.dirStr;
        print mkdirStr
        os.system(mkdirStr)   #创建保存图片的目录
        url = "http://m.yaohu.info/meinv/8/9494.html"
        print url
        self.getImageFormUrl(url)
 
    def getImageFormUrl(self, url):
        headers = {"User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(url, headers = headers)
        response = urllib2.urlopen(request)
        text = response.read()
        # print text
        # onerror="img_error(this);" src="https://wx3.sinaimg.cn/bmiddle/0060lm7Tgy1g1m6p23bv4j30u015ln06.jpg" /> <
        # p1 = r"(?<=\(this\);\" src=\").+?\.jpg(?=\" />)"
        # data-original="//img.q6pk.com/image/20181119/f22baa9e8dee3be4884ccdba0757d7a2_938_1407.jpg"><br/>
        p1 = r"(?<=data-original=\").+?(?=\">)"
        pattern = re.compile(p1)
        imgs = pattern.findall(text)
        print imgs
        for img in imgs:
            imageName = HelloSpider.dirStr + ("/%d.jpg" % (HelloSpider.num))
            imageUrl = 'https:' + img
            print imageUrl
            self.saveImage(imageUrl, imageName)
            HelloSpider.num += 1

    def saveImage(self, imageUrl, imageName):
        headers = {"User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
        request = urllib2.Request(imageUrl, headers = headers)
        imageData = urllib2.urlopen(request).read()
        with open(imageName, "wb") as f:
             f.write(imageData)
        print '正在保存图片：', imageName
        time.sleep(0.1)


helloSpider = HelloSpider()
helloSpider.getImagePageRange()









