# python爬虫：BeautifulSoup 爬取豆瓣电影Top250详细信息
# https://blog.csdn.net/qsmx666/article/details/119832992

from bs4 import BeautifulSoup
import requests
import pandas as pd

# 解析列表页
def scrape_index(url):
    content = requests.get(url, headers=headers)
    soup = BeautifulSoup(content.text, "html.parser")
    print('--------------------------------')
    print('url = ' + str(url))
    for tag in soup.find_all(attrs={"class": "item"}):

        print('--------------------------------')

        # 爬取序号
        detail_data = {}
        num = tag.find('em').get_text()
        detail_data['序号'] = num
        print('num = ' + num)

        # 电影名称
        name = tag.find_all(attrs={"class": "title"})
        zwname = name[0].get_text()
        detail_data['电影名称'] = zwname
        print('zwname = ' + zwname)

        # 电影评分
        score = tag.find(attrs={"class": "rating_num"}).get_text()
        detail_data['评分'] = score
        print('score = ' + score)

        # 网页链接
        url_movie = tag.find(attrs={"class": "hd"}).a
        href = url_movie.attrs['href']
        print('href = ' + href)
        scrape_detail(href, detail_data)


# 解析详情页
def scrape_detail(url, detail_data):
    print('scrape_detail = ' + url)
    content = requests.get(url, headers=headers, stream=True)
    soup = BeautifulSoup(content.text, "html.parser")
    info = soup.find(attrs={"id": "info"})
    details = info.get_text().strip().split('\n')
    print('info = ' + str(info))
    print('detail_data = ' + str(detail_data))
    for detail in details[:10]:
        item, info = detail.split(':', 1)
        detail_data[item] = info
    movie_data.append(detail_data)


headers = {
    'User-Agent':
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) \
                      AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36'
}
INDEX_URL = 'https://movie.douban.com/top250?start={start}&filter='
movie_data = []
for start in range(0, 25, 25):
    url = INDEX_URL.format(start=start)
    scrape_index(url)
df = pd.DataFrame(movie_data)
df.to_csv('movie.csv', index=None)
