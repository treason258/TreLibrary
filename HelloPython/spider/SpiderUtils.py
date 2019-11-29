# -*- coding:utf-8 -*-

import sys
import os
import re

# .decode('utf-8')
# reload(sys)
# sys.setdefaultencoding("utf-8")

# pattern
pattern_default = re.compile(r"(?<=888888).+?(?=888888)")

# headers
headers_chrome_old = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.47 Safari/537.36"}
headers_chrome_new = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36"}
headers_ios = {"User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"}


def findTextArray(content, pattern):
    array = pattern.findall(content)
    return array


# 利用正则查找字符串
def findText(content, pattern):
    textArray = pattern.findall(content)
    text = textArray[0]
    return text


# 利用正则查找字符串
def findTextByStr(content, patternStr):
    pattern = re.compile(patternStr)
    textArray = pattern.findall(content)
    text = textArray[0]
    return text


# 获取用户根目录 /Users/xin
def getHomeDir():
    homeDir = os.environ['HOME']
    return homeDir


# 创建目录
def makeDirs(dir):
    isdir = os.path.isdir(dir)
    if isdir:
        print "目录已存在，不需要创建 | dir = " + dir
    else:
        print "目录不存在，创建目录 | dir = " + dir
        os.makedirs(dir)


# 获取网页内容
def getHtmlContent(url):
    import urllib2
    import ssl
    ssl._create_default_https_context = ssl._create_unverified_context
    try:
        request = urllib2.Request(url=url, headers=headers_ios)
        response = urllib2.urlopen(request)
        content = response.read()
    except urllib2.HTTPError:
        print '获取网页内容失败 | url = ' + url
        return ""
    else:
        return content


# 下载保存图片
def saveImage(imageUrl, imagePath):
    import urllib2
    import time
    try:
        request = urllib2.Request(url=imageUrl, headers=headers_ios)
        response = urllib2.urlopen(request).read()
        with open(imagePath, "wb") as imageFile:
            imageFile.write(response)
        time.sleep(0.2)
    except urllib2.HTTPError:
        print '下载保存图片失败 | imageUrl = ' + imageUrl
    else:
        print '下载保存图片成功 | imageUrl = ' + imageUrl + " | imagePath = " + imagePath
