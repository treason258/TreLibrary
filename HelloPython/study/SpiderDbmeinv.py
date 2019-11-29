# -*- coding:utf-8 -*-
import re
import os
import Utils


class Spider(object):
    imageDir = Utils.getHelloPythonDir()
    index = 0

    def __init__(self):
        pass

    def downloadImage(self, category, fromPage, toPage):
        imageDirMK = 'mkdir ' + Spider.imageDir
        print('---------------- begin ----------------')
        print(imageDirMK)
        os.system(imageDirMK)  # 创建保存图片的目录
        Utils.sleep(0.1)

        categoryInt = int(category)
        fromPageInt = int(fromPage)
        toPageInt = int(toPage)

        while fromPageInt <= toPageInt:
            htmlUrl = 'https://www.dbmeinv.com/index.htm?cid=' + str(categoryInt) + '&pager_offset=' + str(fromPageInt)
            print('htmlUrl = ' + htmlUrl)
            print("\n开始处理第%d页" % fromPageInt)
            self.getImageFormUrl(htmlUrl)
            fromPageInt += 1

    def getImageFormUrl(self, htmlUrl):
        htmlContent = Utils.getHtmlContent(htmlUrl)
        print('\n网页内容：')
        print(htmlContent)

        # <a href="https://www.dbmeinv.com/topic/1800297" class="link" target="_blank">
        #     <img class="height_min" title="第一次这么害羞" alt="第一次这么害羞" onerror="img_error(this);" src="https://wx4.sinaimg.cn/bmiddle/0060lm7Tgy1fwacdm71lij30u014jtee.jpg" referrerpolicy="no-referrer">
        # </a>
        # patternStr = r"(?<=\(this\);\" src=\").+?\.jpg(?=\" />)"
        # patternStr = r"(?<=onerror=\"img_error\(this\);\" src=\").+?\.jpg(?=\" referrerpolicy=\"no-referrer\"/>)"
        patternStr = r"(?<=onerror=\"img_error\(this\);\" src=\").+?\.jpg(?=\" referrerpolicy=\"no-referrer\")"
        pattern = re.compile(patternStr)
        imageUrlArray = pattern.findall(htmlContent)
        print('\n解析到的图片列表：')
        print(imageUrlArray)

        for imageUrl in imageUrlArray:
            imageName = ("image_%d.jpg" % Spider.index)
            print('')
            # Utils.saveImage2(imageUrl, Spider.imageDir, imageName)
            Utils.saveImage3(imageUrl, Spider.imageDir, imageName)
            Spider.index += 1


spider = Spider()
# print('分类：0-所有；2-大胸妹；3-美腿控；4-有颜值；5-大杂烩；6-小翘臀；7-黑丝控')
# category = raw_input("请输入分类：")
# fromPage = raw_input("输入开始页：")
# toPage = raw_input("输入结束页：")
# spider.getImagePageRange(category, fromPage, toPage)
spider.downloadImage(4, 2, 2)
