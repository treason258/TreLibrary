
def saveImage(imageURL, name):
    import urllib3
    http = urllib3.PoolManager()
    print(imageURL)
    splitPath = imageURL.split('.')
    fileExt = splitPath.pop()
    fileName = name + "/" + str(1) + "." + fileExt

    r = http.request('GET', imageURL)
    data = r.data
    f = open(fileName, 'wb+')
    f.write(data)
    print(u'save as:%s', fileName)
    f.close()