# -*- coding:utf-8 -*-


def saveImage(imageUrl, imageDir, imageName):
    return


def saveImage0(imageUrl, imageDir, imageName):
    return


def saveImage1(imageUrl, imageDir, imageName):
    return


def saveImage2(imageUrl, imageDir, imageName):
    imagePath = imageDir + imageName
    # print('imageUrl = ' + imageUrl)
    # print('imageDir = ' + imageDir)
    # print('imageName = ' + imageName)
    # print('imagePath = ' + imagePath)

    headers = {
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
    import urllib2
    request = urllib2.Request(imageUrl, headers=headers)
    imageData = urllib2.urlopen(request).read()
    with open(imagePath, "wb") as f:
        f.write(imageData)
    print('保存图片：' + imagePath)
    import time
    time.sleep(0.1)
    return


def saveImage3(imageUrl, imageDir, imageName):
    imagePath = imageDir + imageName
    # print('imageUrl = ' + imageUrl)
    # print('imageDir = ' + imageDir)
    # print('imageName = ' + imageName)
    # print('imagePath = ' + imagePath)

    import urllib3
    urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
    http = urllib3.PoolManager()
    response = http.request('GET', imageUrl)
    data = response.data
    file = open(imagePath, 'wb+')
    file.write(data)
    file.close()
    print('保存图片：' + imagePath)
    import time
    time.sleep(0.1)
    return


def getWebpageContent(url):
    headers = {
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/1    7.0.963.56 Safari/535.11"}
    import urllib2
    request = urllib2.Request(url, headers=headers)
    response = urllib2.urlopen(request)
    text = response.read()
    return text
