# coding=utf-8

# Python爬虫——爬取豆瓣top250完整代码
# https://www.cnblogs.com/zq-zq/p/13974807.html

# 目录操作
import os
# 正则表达式
import re
# 访问SSL页面
import ssl
# 模拟阻塞
import time
# 获取URL得到html文件
import urllib.request as req

# Excel表格操作
import xlwt
# 解析网页
from bs4 import BeautifulSoup as bf

ssl._create_default_https_context = ssl._create_unverified_context

# 各种目录和文件名
base_url = 'https://www.imdb.com/chart/top/'
base_path = os.environ['HOME'] + '/Downloads/HelloPython/imdb250/'
base_date = '20210908-'

save_html_path = base_path + ''  # html/
save_text_path = base_path + ''  # text/
save_excel_path = base_path + ''  # excel/

save_html_file = save_html_path + base_date + 'top250-page1.html'
save_text_file = save_text_path + base_date + 'top250.txt'
save_excel_file = save_excel_path + base_date + 'top250.xls'


# 主程序
def main():
    print('--------0-创建目录--------')
    make_dirs(save_html_path)
    make_dirs(save_text_path)
    make_dirs(save_excel_path)

    print('--------1-爬取网页，从网页上获取html文件并保存到本地目录下，该方法成功执行一次即可，保存html，接下来本地操作--------')
    save_html()

    print('--------2-解析数据，逐个解析保存在本地的html文件--------')
    datas = get_data()

    print('--------3-保存数据，保存爬取数据到本地txt文件--------')
    save_data_txt(datas, save_text_file)

    print('--------4-保存数据，保存爬取数据到本地excel文件--------')
    save_data_excel(datas, save_excel_file)


# 0-创建目录
def make_dirs(dir):
    isdir = os.path.isdir(dir)
    if isdir:
        print("目录已存在，不需要创建 | dir = " + dir)
    else:
        print("目录不存在，创建目录 | dir = " + dir)
        os.makedirs(dir)


# 1-爬取网页，从豆瓣上获取html文件并保存到本地目录下，该方法成功执行一次即可，保存html，接下来本地操作
def save_html():
    print('----爬取网页----')

    # 获取html保存在本地，方便之后爬虫操作，因为频繁爬取可能被豆瓣发现异常
    html = ask_url(base_url)

    # 将文件批量保存在 Data/html/ 目录下 i//25 是整除，命名格式如   html0.html  html1.html ...
    write_html(save_html_file, html)

    # 模拟阻塞
    time.sleep(3)


# 获取html信息，并返回html信息
def ask_url(url):
    # 设置传给服务器的header头部信息，伪装自己是正规浏览器访问
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36 SE 2.X MetaSr 1.0"
    }

    # 用于保存获取的html文件
    html = ""
    # 最好用 try-except 捕捉异常
    try:
        # 封装一个Request对象，将自定义的头部信息加入进去
        res = req.Request(url, headers=headers)
        # 向指定的url获取响应信息，设置超时，防止长时间耗在一个页面
        response = req.urlopen(res, timeout=20)
        # 读取html信息，使用decode('utf-8')解码
        html = response.read().decode('utf-8')
    # 如果出错，就捕捉报错信息并打印出，这里使用Exception 泛泛的意思一下
    except Exception as error:
        # 出现异常时候，打印报错信息
        print("Ask_url is Error : " + error)

    # 将获得的html页面信息返回
    return html


# 保存获取的html，避免出现ip异常的情况
def write_html(path, html):
    file = open(path, 'w', encoding='utf-8')
    file.write(str(html))
    file.close()


# 2-解析数据，逐个解析保存在本地的html文件
def get_data():
    # 获得多有页面有价值的信息，然后集中存放与data_list列表中
    data_list = []
    # 循环遍历，修改?start=起始排行序号，获取不同分页的豆瓣top信息，url分页格式去豆瓣换页内容试试
    print('----读取网页----')

    # 使用二进制读取，这点很重要，报错无数次
    html = open(save_html_file, 'rb')

    # 接下来是逐一解析数据
    bs = bf(html, 'html.parser')

    # 使用标签 + 属性组合查找，查找<div class="item"></div>的标签块
    # 注意：class是关键字，所以这里需要使用 class_ 代替
    f_list = bs.find_all('td', class_="posterColumn")

    # 使用re.findall(x, s) 或者 x.findall(s)效果一样
    for f in f_list:
        data = []

        # 将正则表达式提取的内容赋值给自定义变量，将所有需要的数据保存到data列表
        # titles = ['电影', '排行', '评分', '评论数', '链接', '封面', '概括', '别名']
        # data.append(set_film(str(f), re.compile(r'<span class="title">(.*?)</span>')))
        # data.append(set_film(str(f), re.compile(r'em class="">(.*?)</em>')))
        # data.append(set_film(str(f), re.compile(r'<span class="rating_num".*>(.*?)</span>')))
        # data.append(set_film(str(f), re.compile(r'<span>(\d*人)评价</span>')))
        # data.append(set_film(str(f), re.compile(r'<span class="other">(.*?)</span>')))
        data.append(set_film(str(f), re.compile(r'<a href="(.*?)">')))
        data.append(set_film(str(f), re.compile(r'<a href="(.*?)">')))
        data.append(set_film(str(f), re.compile(r'<a href="(.*?)">')))
        data.append(set_film(str(f), re.compile(r'<a href="(.*?)">')))
        data.append(set_film(str(f), re.compile(r'<a href="(.*?)">')))
        data.append(set_film(str(f), re.compile(r'<img.*src="(.*?)"', re.S)))  # re.S让换行符包含在字符中
        data.append(set_film(str(f), re.compile(r'<a href.*title="(.*?)"', re.S)))  # re.S让换行符包含在字符中
        data.append(set_film(str(f), re.compile(r'<span class="secondaryInfo">(.*?)</span>')))

        # 写入data（单条电影信息）列表，到总的 data_list（所有电影信息）列表
        data_list.append(data)

    html.close()
    return data_list


# 有些电影没有某些项，所以查找长度为0的时候，设置该项为空
def set_film(content, file):
    # 检查查找内容的长度，如果不为0，说明查找到内容，则将内容转换成字符串类型
    if len(re.findall(file, content)) != 0:
        film = str(re.findall(file, content)[0])
    else:
        film = "-"

    return film


# 3-保存数据，保存爬取数据到本地txt文件
def save_data_txt(datas, save_file):
    # 打开文本选择写模式，并指定编码格式
    file = open(save_file, 'w', encoding='utf-8')
    # 不能直接写入list，所以通过遍历一条条写入
    for data in datas:
        for dat in data:
            file.write(dat + '\n')
        file.write(split(10) + '\n')
    file.close()
    # 将读取的txt文本打印到控制台
    read_file(save_file)


# 定义分隔线长度，并返回分割线字符串
def split(num):
    str1 = ""
    for i in range(1, num):
        str1 += "--------"
    return str1


# 读取文件文本
def read_file(file_name):
    # 打开文本选择读模式
    file = open(file_name, 'r', encoding='utf-8')
    print(file.read())
    file.close()


# 4-保存数据，保存爬取数据到本地excel文件
def save_data_excel(datas, save_file):
    # 创建一个xlwt对象，使用utf-8编码格式
    excel = xlwt.Workbook(encoding='utf-8')
    # 创建一个工作表，命名为top250
    sheet = excel.add_sheet('top250')

    # 设置三种单元格样式 set_font(粗体，尺寸，居中)
    style_font_title = set_font(240, True, True)
    style_font_content_center = set_font(220, False, True)
    style_font_content_left = set_font(220, False, False)

    # 设置列宽
    width_c = [256 * 20, 256 * 6, 256 * 6, 256 * 12, 256 * 42, 256 * 72, 256 * 68, 256 * 40]
    for i in range(0, len(width_c)):
        sheet.col(i).width = width_c[i]

    # 表格各列的列名，将标题写入excel
    titles = ['电影', '排行', '评分', '评论数', '链接', '封面', '概括', '别名']
    index = 0
    for title in titles:
        # (单元格行序号，单元格列序号，单元格的内容，单元格样式)
        sheet.write(0, index, title, style_font_title)
        index += 1

    # 将数据写入excel
    index_r = 1
    # 从多条电影中每次取出一条
    for data in datas:
        index_c = 0
        # 从一条电影中每次取出一个属性
        for item in data:
            # 前三列设置居中对齐
            if index_c <= 3:
                sheet.write(index_r, index_c, item, style_font_content_center)
            # 后三列设置默认对齐，即左对齐
            else:
                sheet.write(index_r, index_c, item, style_font_content_left)
            index_c += 1
        index_r += 1

    # 保存excel文件到指定路径
    excel.save(save_file)


# 设置excel的单元格字体样式
def set_font(size, bold, center):
    # 创建xlwt格式对象
    style_font = xlwt.XFStyle()
    # 设置字体尺寸大小
    style_font.font.height = size
    # 设置字体是否为粗体
    style_font.font.bold = bold
    # 字体是否居中
    if center:
        # 设置字体水平居中
        style_font.alignment.horz = 0x02
        # 设置字体垂直居中
        style_font.alignment.vert = 0x01
    # 设置单元格自动换行
    style_font.alignment.wrap = False
    # 返回设置的字体样式
    return style_font


# 主程序入口
if __name__ == '__main__':
    main()
