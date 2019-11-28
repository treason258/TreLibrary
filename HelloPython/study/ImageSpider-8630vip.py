# -*- coding:utf-8 -*-
import re
import SpiderUtils


def get8630VipImage(url):
    print "--------------------------------解析网页代码"
    print "--网页地址 | url = " + url
    content = SpiderUtils.getHtmlContent(url)
    # print "--打印网页内容"
    # print content

    print "--------------------------------获得文件夹名字"
    title1 = SpiderUtils.findText(content, re.compile(r"(?<=<div class=\"title\">).+?(?=<span class=\"subtitle\">)"))
    title2 = SpiderUtils.findText(content, re.compile(r"(?<=<span class=\"subtitle\">).+?(?=</span>)"))
    folderName = title1 + "-" + title2 + "/"
    print "--打印标题1 | title1 = " + str(title1)
    print "--打印标题2 | title2 = " + str(title2)
    print "--打印文件夹名称 | folderName = " + folderName

    print "--------------------------------创建下载目录"
    imageDir = SpiderUtils.getHomeDir() + '/Downloads/python/ImageSpider-8630vip/' + folderName
    print "--下载目录 | imageDir = " + str(imageDir)
    SpiderUtils.makeDirs(imageDir)

    print "--------------------------------解析图片地址"
    imageUrlArray = SpiderUtils.findTextArray(content, re.compile(r"(?<=img src=\").+?(?=1.jpg\" alt=\"图)"))
    print "----打印图片列表"
    print imageUrlArray

    imageUrlPrefix = imageUrlArray[0]
    print "imageUrlPrefix = " + imageUrlPrefix

    for i in range(1, 3):
        imageUrl = imageUrlPrefix + ("%d.jpg" % i)
        imagePath = imageDir + ("%d.jpg" % i)
        SpiderUtils.saveImage(imageUrl, imagePath)


if __name__ == '__main__':
    urlPrefix = "https://yy.8630vip.com/home/book/capter/id/"
    beginIndex = 28500
    for i in range(1, 5):
        url = urlPrefix + str(beginIndex + i)
        get8630VipImage(url)
