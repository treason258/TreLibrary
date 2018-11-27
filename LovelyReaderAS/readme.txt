
// ******************************** SVN ********************************

matengfei=matengfei321
svn://123.56.194.104/repo0/projects/Reader/android/LovelyReader
svn://123.56.194.104/repo0/projects/common/AppBase
svn://123.56.194.104/repo0/projects/common/JavaCommon
LovelyReader依赖AppBase
JavaCommon是一些通用的功能，里面有build.xml文件，用ant生成jar包，拷到lover的lib唧可

// ******************************** 接口 ********************************

http://112.126.80.1:9001/swagger-ui.html#/APP%E7%89%88%E6%9C%AC%E5%8D%87%E7%BA%A7%20Api%E6%8E%A5%E5%8F%A3/checkUsingPOST
http://112.126.80.1:9004/swagger-ui.html#/

http://112.126.80.1:9001/swagger-ui.html#/
http://112.126.80.1:9005/swagger-ui.html#/

@马腾飞-Android @侯力 @我叫咚咚枪 服务器地址变了。修改成这个  47.94.109.157  不要加端口，默认80

http://47.94.109.157:8080/swagger-ui.html#/
http://123.56.194.104:9005/swagger-ui.html
新的接口文档地址 @田宇 @马腾飞-Android @侯力 @我叫咚咚枪

// ******************************** 设计图 ********************************

https://lanhuapp.com/web/#/item/board?pid=7c8eb1c1-714c-4ce2-9183-54043266f4d1

接口定义
SUCCESS(900, "请求成功"),
SERVER_ERR(901, "服务异常"),
PARAM_ERR(902, "参数异常"),
URL_404(903, "404异常"),
HTTP_METHOD_ERR(904, "HTTP请求方法异常"),
TOKEN_EXPIRE(905, "TOKEN过期");

WIFI传书
https://blog.csdn.net/gorgle/article/details/52788701
https://github.com/baidusoso/WifiTransfer


http://112.126.80.1:8090/login.action?os_destination=%2Findex.action
常量定义
http://112.126.80.1:8090/pages/viewpage.action?pageId=589833
签名逻辑
http://112.126.80.1:8090/pages/viewpage.action?pageId=917511


后台地址
http://112.126.80.1:9002/
用户名密码 admin/111111


9001不变,先调功能.  9004 调签名

randomChar = MgwboM
sign

md5Secret = "111"
str = "111MgwboM111"


111randomChar=123111


@马腾飞-Android
WEB("WEB", "网站"),
ANDROID_PHONE("ANDROID_PHONE", "安卓手机"),
ANDROID_PAD("ANDROID_PAD", "安卓pad"),
IOS_PHONE("IOS_PHONE", "苹果手机"),
IOS_PAD("IOS_PAD", "苹果pad"),
WINDOWS("WINDOWS", "windows"),
MAC("MAC", "mac");