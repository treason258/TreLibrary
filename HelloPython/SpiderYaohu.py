# -*- coding:utf-8 -*-
import os
import re
import Utils


class Spider(object):
    imageDir = Utils.getHelloPythonDir()
    index = 0

    def __init__(self):
        pass

    def downloadImage(self):
        imageDirMK = 'mkdir ' + Spider.imageDir
        print('---------------- begin ----------------')
        print(imageDirMK)
        os.system(imageDirMK)  # 创建保存图片的目录
        Utils.sleep(0.1)

        htmlUrl = "http://m.yaohu.info/meinv/8/1412.html"
        print('htmlUrl = ' + htmlUrl)

        htmlContent = Utils.getHtmlContent(htmlUrl)
        print('\n网页内容：')
        print(htmlContent)

        patternStr = r"(?<=data-original=\").+?(?=\">)"
        pattern = re.compile(patternStr)
        imageUrlArray = pattern.findall(htmlContent)
        print('\n解析到的图片列表：')
        print(imageUrlArray)

        for imageUrl in imageUrlArray:
            imageUrl = 'http:' + imageUrl
            imageName = ("image_%d.jpg" % Spider.index)
            print('')
            # Utils.saveImage2(imageUrl, Spider.imageDir, imageName)
            Utils.saveImage3(imageUrl, Spider.imageDir, imageName)
            Spider.index += 1


spider = Spider()
spider.downloadImage()
