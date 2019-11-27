import os
import urllib
import requests.packages.urllib3.util.ssl_
import ssl

ssl._create_default_https_context = ssl._create_unverified_context
requests.packages.urllib3.util.ssl_.DEFAULT_CIPHERS = 'ALL'


def save_img(img_url, file_name, file_path='img'):
    try:
        if not os.path.exists(file_path):
            print '111', file_path, '222'
            os.makedirs(file_path)
        file_suffix = os.path.splitext(img_url)[1]
        filename = '{}{}{}{}'.format(file_path, os.sep, file_name, file_suffix)
        urllib.urlretrieve(img_url, filename=filename)
    except IOError as e:
        print 'IOError : ', e
    except Exception as e:
        print 'Exception : ', e


if __name__ == '__main__':
    img_url = 'https://wx2.sinaimg.cn/bmiddle/0060lm7Tgy1fuwpcswgacj30u015stgh.jpg'
    # img_url = 'https://img.q6pk.com/image/20181119/0ba051e0b7747bc8cce970b81cfa0584_938_1370.jpg'
    save_img(img_url, '111-2')
