def saveImage3(imageUrl, imageDir, imageName):
    print('imageUrl = ' + imageUrl)
    print('imageDir = ' + imageDir)
    print('imageName = ' + imageName)

    imagePath = imageDir + imageName
    print('imagePath = ' + imagePath)

    import urllib3
    http = urllib3.PoolManager()
    response = http.request('GET', imageUrl)
    data = response.data
    file = open(imagePath, 'wb+')
    file.write(data)
    print('图片已保存到：' + imagePath)
    file.close()
    return