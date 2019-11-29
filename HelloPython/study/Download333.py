import re
import urllib3


def getHtml(url):
    http = urllib3.PoolManager()
    r = http.request('GET', url)
    htmlStr = r.data.decode('utf-8')
    print(htmlStr)
    return htmlStr


def getImg(htmlStr):
    reg = r':"(http://[^"]+\.(?:jpg|png|gif))"?'
    imgre = re.compile(reg)
    imglist = imgre.findall(htmlStr)
    saveImage(imglist, 'Downloads/python/HelloSpider44')


def saveImage(imglist, name):
    number = 1
    http = urllib3.PoolManager()

    for imageURL in imglist:
        print(imageURL)
        splitPath = imageURL.split('.')
        fileExt = splitPath.pop()
        fileName = name + "/" + str(number) + "." + fileExt

        r = http.request('GET', imageURL)
        data = r.data
        f = open(fileName, 'wb+')
        f.write(data)
        print(u'save as:%s', fileName)
        f.close()
        number += 1

    print('\ntotal number of image:%s', (name, number))


def saveSimpleImage(imageURL, name):
    http = urllib3.PoolManager()
    print(imageURL)
    splitPath = imageURL.split('.')
    fileExt = splitPath.pop()
    fileName = name + "/333-2." + fileExt

    r = http.request('GET', imageURL)
    data = r.data
    f = open(fileName, 'wb+')
    f.write(data)
    print(u'save as:%s', fileName)
    f.close()


if __name__ == '__main__':
    # s=r'http://image.baidu.com/search/index?ct=201326592&z=0&s=0&tn=baiduimage&ipn=r&word=%E8%B4%AD%E7%89%A9%E4%B8%AD%E5%BF%83%E5%B9%B3%E9%9D%A2%E5%9B%BE&pn=0&istype=2&ie=utf-8&oe=utf-8&cl=2&lm=7&st=-1&fr=&fmq=1508290519080_R&ic=0&se=&sme=&width=0&height=0&face=0'
    # html = getHtml(s)
    # getImg(html)
    # url = 'http://img.q6pk.com/image/20181119/0ba051e0b7747bc8cce970b81cfa0584_938_1370.jpg'
    url = 'https://wx2.sinaimg.cn/bmiddle/0060lm7Tgy1fuwpcswgacj30u015stgh.jpg'
    saveSimpleImage(url, './img')
