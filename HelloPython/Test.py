import RequestSender

print('---------------- begin ----------------')

# imageUrl = 'https://wx2.sinaimg.cn/bmiddle/0060lm7Tgy1fuwpcswgacj30u015stgh.jpg'
imageUrl = 'http://img.q6pk.com/image/20181119/0ba051e0b7747bc8cce970b81cfa0584_938_1370.jpg'
imageDir = '/Users/treason/Downloads/HelloPython'
imageName = '1.jpg'
print(imageUrl)
RequestSender.saveImage(imageUrl, imageDir)

print('---------------- end ----------------')