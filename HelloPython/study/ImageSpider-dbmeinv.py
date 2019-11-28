# -*- coding:utf-8 -*-
import re
import SpiderUtils


class ImageSpider(object):
    num = 0

    def __init__(self):
        pass

    def getImageFormUrl(self, url):
        print "--------------------------------解析网页代码"
        print "--网页地址 | url = " + url
        content = SpiderUtils.getHtmlContent(url)
        # print "--打印网页内容"
        # print content

        imageUrls = SpiderUtils.findTextArray(content, re.compile(r"(?<=onerror=\"img_error\(this\);\" src=\").+?\.jpg(?=\" referrerpolicy=\"no-referrer\")"))
        print "打印图片列表"
        print imageUrls
        for imageUrl in imageUrls:
            imagePath = imageDir + ("%d.jpg" % ImageSpider.num)
            SpiderUtils.saveImage(imageUrl, imagePath)
            ImageSpider.num += 1


if __name__ == '__main__':

    print "--------------------------------创建下载目录"
    imageDir = SpiderUtils.getHomeDir() + '/Downloads/python/ImageSpider-dbmeinv/'
    print "--下载目录 | imageDir = " + str(imageDir)
    SpiderUtils.makeDirs(imageDir)

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
