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

    def getImagePageRange(self, category, fromPage, toPage):
        imageDirMK = 'mkdir ' + Spider.imageDir
        print('---------------- begin ----------------')
        print(imageDirMK)
        os.system(imageDirMK)  # 创建保存图片的目录
        time.sleep(0.2)

        categoryInt = int(category)
        fromPageInt = int(fromPage)
        toPageInt = int(toPage)

        while fromPageInt <= toPageInt:
            url = 'https://www.dbmeinv.com/index.htm?cid=' + str(categoryInt) + '&pager_offset=' + str(fromPageInt)
            print('url = ', url)
            print("\n第%d页" % fromPageInt)
            self.getImageFormUrl(url)
            fromPageInt += 1

    def getImageFormUrl(self, url):
        text = Utils.getHTMLContent(url)
        # print(text)

        patternStr = r"(?<=\(this\);\" src=\").+?\.jpg(?=\" />)"
        pattern = re.compile(patternStr)
        imageUrlArray = pattern.findall(text)
        print(imageUrlArray)

        for imageUrl in imageUrlArray:
            print('\n网络图片地址：' + imageUrl)
            imageName = ("%d.jpg" % Spider.index)
            # Utils.saveImage2(imageUrl, Spider.imageDir, imageName)
            Utils.saveImage3(imageUrl, Spider.imageDir, imageName)
            Spider.index += 1


spider = Spider()
# print('分类：0-所有；2-大胸妹；3-美腿控；4-有颜值；5-大杂烩；6-小翘臀；7-黑丝控')
# category = raw_input("请输入分类：")
# fromPage = raw_input("输入开始页：")
# toPage = raw_input("输入结束页：")
# spider.getImagePageRange(category, fromPage, toPage)
spider.getImagePageRange(4, 20, 20)
