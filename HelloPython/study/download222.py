import requests

url = 'https://wx2.sinaimg.cn/bmiddle/0060lm7Tgy1fuwpcswgacj30u015stgh.jpg'
# url = 'http://img.q6pk.com/image/20181119/0ba051e0b7747bc8cce970b81cfa0584_938_1370.jpg'
response = requests.get(url)
img = response.content
with open( './a.jpg','wb' ) as f:
    f.write(img)