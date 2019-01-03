
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


http://123.56.194.104:9005/swagger-ui.html
http://123.56.194.104:9005/swagger-ui.html


http://47.94.109.157:8080/swagger-ui.html#
http://123.56.194.104:9005/swagger-ui.html


http://47.94.109.157:8888/index
这个是后台的地址,用户名和密码: admin/admin123
你们开发好的客户端就在这个里面维护,功能已经做好


@田宇 @马腾飞-Android @侯力 @我叫咚咚枪  api接口域名已经配置好了,用这个 :  https://apitest.readeryun.com/

http://oper.readeryun.com/   admin/admin123




2018-12-11 12:22:45.495 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:22:45.512 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:22:45.512 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
    request_method -> POST_STRING
    request_headers -> {sign=4579512fa65087ff3e82e15c90bf7169, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544502165511"},"param":{"data":"e4f558a2d7f53a312b5eb94377708fdd"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 12:22:46.310 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
    response_data -> {"statusCode":900,"data":{"hasExistFile":true,"url":"http://restest.readeryun.com/e4f558a2d7f53a312b5eb94377708fdd","fileVo":{"sevenFileName":"e4f558a2d7f53a312b5eb94377708fdd","sevenHash":"FtRlMYXCxLu6LsgLS0t6eT2g8MBo","sevenFileSize":398113}},"timestamp":1544502170520,"msg":null}
2018-12-11 12:22:46.312 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:22:46.312 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
    request_method -> POST_STRING
    request_headers -> {sign=21c99809dc8ee21c55d3192ed04679e0, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544502166311"},"param":{"data":"8MVjnh"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 12:22:47.185 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
    response_data -> {"statusCode":900,"data":"m8AzRuHoXbhXtql06Hy8XTNWvDCtaNvS0_R2Y_io:uhnt4p-ZAYflV1esr1ocvJrYeyY=:eyJzY29wZSI6InJlYWRlci1ib29rLXRlc3QiLCJyZXR1cm5Cb2R5Ijoie1wia2V5XCI6XCIkKGtleSlcIixcImhhc2hcIjpcIiQoZXRhZylcIixcImZzaXplXCI6JChmc2l6ZSl9IiwiZGVhZGxpbmUiOjE1NDQ1MjUwMzJ9","timestamp":1544502170908,"msg":null}
2018-12-11 12:22:48.251 I/LogUtils-BookAdapter: key -> e4f558a2d7f53a312b5eb94377708fdd
2018-12-11 12:22:48.260 I/LogUtils-BookAdapter: responseInfo -> {"duration":1034.0,"host":"upload-z1.qiniu.com","id":"1544502167197810","ip":"/10.70.128.76:8888","path":"/","port":80,"reqId":"SmoAALkq6AKLLG8V","response":{"nameValuePairs":{"key":"e4f558a2d7f53a312b5eb94377708fdd","hash":"FtRlMYXCxLu6LsgLS0t6eT2g8MBo","fsize":398113}},"sent":0,"statusCode":200,"timeStamp":1544502168,"xlog":"body;0s.ph;0s.put.in;0s.put.disk:3;1s.put.in;1s.put.disk:3;1s.ph;PFDS:3;0s.put.out:3;PFDS:4;body;rs13_shard.sel;rwro.ins/same entry;rs13_shard.sel;rwro.get;MQ;RS.not:;RS:2;rs.put:3;rs-upload.putFile:8;UP:10","xvia":""}
2018-12-11 12:22:48.261 I/LogUtils-BookAdapter: jsonObject -> {"nameValuePairs":{"key":"e4f558a2d7f53a312b5eb94377708fdd","hash":"FtRlMYXCxLu6LsgLS0t6eT2g8MBo","fsize":398113}}
2018-12-11 12:22:48.261 I/LogUtils-BookAdapter: Upload Success
2018-12-11 12:22:48.269 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:22:48.269 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/save
    request_method -> POST_STRING
    request_headers -> {sign=0bbf6540e11b46e33a0f3c2bd3298e26, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544502168262"},"param":{"bookId":"208","sevenFileName":"e4f558a2d7f53a312b5eb94377708fdd","sevenFileSize":"398113","sevenHash":"FtRlMYXCxLu6LsgLS0t6eT2g8MBo"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 12:22:48.427 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/save
    response_data -> {"statusCode":900,"data":true,"timestamp":1544502172960,"msg":null}









2018-12-11 12:25:51.689 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:25:51.696 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:25:51.697 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
    request_method -> POST_STRING
    request_headers -> {sign=64ba3a3b7cc8c01b01377713a3d961bf, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544502351695"},"param":{"data":"dc315b8f0f8e1829fe1769b1a9fe3467"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 12:25:51.934 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
    response_data -> {"statusCode":900,"data":{"hasExistFile":false,"url":null,"fileVo":null},"timestamp":1544502356448,"msg":null}
2018-12-11 12:25:51.936 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:25:51.937 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
    request_method -> POST_STRING
    request_headers -> {sign=8f4acb30a7a778c801a14ed113866072, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544502351936"},"param":{"data":"ClzkR6"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 12:25:52.011 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
    response_data -> {"statusCode":900,"data":"m8AzRuHoXbhXtql06Hy8XTNWvDCtaNvS0_R2Y_io:uhnt4p-ZAYflV1esr1ocvJrYeyY=:eyJzY29wZSI6InJlYWRlci1ib29rLXRlc3QiLCJyZXR1cm5Cb2R5Ijoie1wia2V5XCI6XCIkKGtleSlcIixcImhhc2hcIjpcIiQoZXRhZylcIixcImZzaXplXCI6JChmc2l6ZSl9IiwiZGVhZGxpbmUiOjE1NDQ1MjUwMzJ9","timestamp":1544502356523,"msg":null}
2018-12-11 12:25:52.251 I/LogUtils-BookAdapter: key -> dc315b8f0f8e1829fe1769b1a9fe3467
2018-12-11 12:25:52.252 I/LogUtils-BookAdapter: responseInfo -> {"duration":229.0,"host":"upload-z1.qiniu.com","id":"1544502167197810","ip":"/10.70.128.76:8888","path":"/","port":80,"reqId":"0BwAAEDhxtq1LG8V","response":{"nameValuePairs":{"key":"dc315b8f0f8e1829fe1769b1a9fe3467","hash":"FlGdVC1SLBmjIWDuxgrPnWZwhshv","fsize":144688}},"sent":0,"statusCode":200,"timeStamp":1544502352,"xlog":"body:1;0s.ph;0s.put.in;0s.put.disk:1;1s.put.in;1s.put.disk:1;1s.ph;PFDS:1;0s.put.out:1;PFDS:3;body;rs13_shard.sel/not found;rs12_5.sel:2/not found;rdb.g/no such key;DBD/404;v4.get/Document not found;rs13_shard.ins:3;rwro.ins:7;RS:8;rs.put:8;rs-upload.putFile:12;UP:16","xvia":""}
2018-12-11 12:25:52.252 I/LogUtils-BookAdapter: jsonObject -> {"nameValuePairs":{"key":"dc315b8f0f8e1829fe1769b1a9fe3467","hash":"FlGdVC1SLBmjIWDuxgrPnWZwhshv","fsize":144688}}
2018-12-11 12:25:52.252 I/LogUtils-BookAdapter: Upload Success
2018-12-11 12:25:52.253 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 12:25:52.253 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/save
    request_method -> POST_STRING
    request_headers -> {sign=6a97f4087cc6e93180db56aa32b5d028, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544502352252"},"param":{"bookId":"207","sevenFileName":"dc315b8f0f8e1829fe1769b1a9fe3467","sevenFileSize":"144688","sevenHash":"FlGdVC1SLBmjIWDuxgrPnWZwhshv"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 12:25:52.345 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/save
    response_data -> {"statusCode":900,"data":true,"timestamp":1544502356832,"msg":null}






2018-12-11 14:34:04.424 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 14:34:04.433 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 14:34:04.433 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
    request_method -> POST_STRING
    request_headers -> {sign=d9300e4a7b64a4ab3d5fc4db256b58dc, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544510044432"},"param":{"data":"b35279980a6bfe2b90bcd0d4496a5b1b"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 14:34:05.572 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
    response_data -> {"statusCode":900,"data":{"hasExistFile":false,"url":null,"fileVo":null},"timestamp":1544510050098,"msg":null}
2018-12-11 14:34:05.573 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 14:34:05.574 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
    request_method -> POST_STRING
    request_headers -> {sign=99799efc7b61aa3fb5846b32b8f6a236, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544510045573"},"param":{"data":"7Dfc7Q"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 14:34:05.634 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
    response_data -> {"statusCode":900,"data":"m8AzRuHoXbhXtql06Hy8XTNWvDCtaNvS0_R2Y_io:uhnt4p-ZAYflV1esr1ocvJrYeyY=:eyJzY29wZSI6InJlYWRlci1ib29rLXRlc3QiLCJyZXR1cm5Cb2R5Ijoie1wia2V5XCI6XCIkKGtleSlcIixcImhhc2hcIjpcIiQoZXRhZylcIixcImZzaXplXCI6JChmc2l6ZSl9IiwiZGVhZGxpbmUiOjE1NDQ1MjUwMzJ9","timestamp":1544510050183,"msg":null}
2018-12-11 14:34:06.173 I/LogUtils-BookAdapter: key -> b35279980a6bfe2b90bcd0d4496a5b1b
2018-12-11 14:34:06.173 I/LogUtils-BookAdapter: responseInfo -> {"duration":528.0,"host":"upload-z1.qiniu.com","id":"1544509746293713","ip":"/10.70.128.76:8888","path":"/","port":80,"reqId":"mSkAAF4_1ji1M28V","response":{"nameValuePairs":{"key":"b35279980a6bfe2b90bcd0d4496a5b1b","hash":"FghG90n9D77UOPEKQKojwtiEQusZ","fsize":231488}},"sent":0,"statusCode":200,"timeStamp":1544510046,"xlog":"body;0s.ph;0s.put.in;0s.put.disk:1;1s.put.in;1s.put.disk:11;1s.ph;PFDS:12;0s.put.out:1;PFDS:16;body;rs13_shard.sel:2/not found;rs12_1.sel:2/not found;rdb.g/no such key;DBD/404;v4.get/Document not found;rs13_shard.ins:3;rwro.ins:9;RS:9;rs.put:10;rs-upload.putFile:27;UP:28","xvia":""}
2018-12-11 14:34:06.173 I/LogUtils-BookAdapter: jsonObject -> {"nameValuePairs":{"key":"b35279980a6bfe2b90bcd0d4496a5b1b","hash":"FghG90n9D77UOPEKQKojwtiEQusZ","fsize":231488}}
2018-12-11 14:34:06.173 I/LogUtils-BookAdapter: Upload Success
2018-12-11 14:34:06.174 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
2018-12-11 14:34:06.174 D/LogUtils-RequestSender: send -> request_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/save
    request_method -> POST_STRING
    request_headers -> {sign=389c77a441271f028a269e4452957bd8, token=5095c8097aef4e6b9c578122a1f5a713}
    request_params -> {}
    request_files -> {}
    request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544510046173"},"param":{"bookId":"209","sevenFileName":"b35279980a6bfe2b90bcd0d4496a5b1b","sevenFileSize":"231488","sevenHash":"FghG90n9D77UOPEKQKojwtiEQusZ"}}
    request_filePath ->
    request_jsonObject -> {}
2018-12-11 14:34:06.224 D/LogUtils-RequestSender: send -> response_info |
    request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/save
    response_data -> {"statusCode":900,"data":true,"timestamp":1544510050756,"msg":null}






12-11 18:23:32.332 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
12-11 18:23:32.340 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
12-11 18:23:32.341 D/LogUtils-RequestSender: send -> request_info |
                                             request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
                                             request_method -> POST_STRING
                                             request_headers -> {sign=777d373ca3817cebbbe0bdbae8acf31f, token=5095c8097aef4e6b9c578122a1f5a713}
                                             request_params -> {}
                                             request_files -> {}
                                             request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544523812340"},"param":{"data":"901c1c8cbf0ffccff7d102f7ad800f63.epub"}}
                                             request_filePath ->
                                             request_jsonObject -> {}
12-11 18:23:32.611 D/LogUtils-RequestSender: send -> response_info |
                                             request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/exists
                                             response_data -> {"statusCode":900,"data":{"hasExistFile":true,"url":"http://restest.readeryun.com/901c1c8cbf0ffccff7d102f7ad800f63.epub","fileVo":{"sevenFileName":"901c1c8cbf0ffccff7d102f7ad800f63.epub","sevenHash":"FjX_l7-9nxXF3hXD06tJQDTvBOvS","sevenFileSize":165647}},"timestamp":1544523817295,"msg":null}
12-11 18:23:32.614 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
12-11 18:23:32.615 D/LogUtils-RequestSender: send -> request_info |
                                             request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
                                             request_method -> POST_STRING
                                             request_headers -> {sign=3c11ad8bfc83e31d9e46f9fb57ddf810, token=5095c8097aef4e6b9c578122a1f5a713}
                                             request_params -> {}
                                             request_files -> {}
                                             request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544523812613"},"param":{"data":"sHL6m2"}}
                                             request_filePath ->
                                             request_jsonObject -> {}
12-11 18:23:32.650 D/LogUtils-RequestSender: send -> response_info |
                                             request_url -> https://apitest.readeryun.com/doc/sevenfile/v1/token
                                             response_data -> {"statusCode":900,"data":"m8AzRuHoXbhXtql06Hy8XTNWvDCtaNvS0_R2Y_io:uhnt4p-ZAYflV1esr1ocvJrYeyY=:eyJzY29wZSI6InJlYWRlci1ib29rLXRlc3QiLCJyZXR1cm5Cb2R5Ijoie1wia2V5XCI6XCIkKGtleSlcIixcImhhc2hcIjpcIiQoZXRhZylcIixcImZzaXplXCI6JChmc2l6ZSl9IiwiZGVhZGxpbmUiOjE1NDQ1MjUwMzJ9","timestamp":1544523817345,"msg":null}
12-11 18:23:32.673 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.04923313620847156
12-11 18:23:32.673 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.09846627241694313
12-11 18:23:32.674 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.1476994086254147
12-11 18:23:32.674 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.19693254483388625
12-11 18:23:32.675 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.2461656810423578
12-11 18:23:32.675 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.2953988172508294
12-11 18:23:32.675 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.3446319534593009
12-11 18:23:32.676 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.3938650896677725
12-11 18:23:32.676 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.44309822587624403
12-11 18:23:32.676 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.4923313620847156
12-11 18:23:32.676 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.5415644982931872
12-11 18:23:32.677 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.5907976345016588
12-11 18:23:32.677 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.6400307707101303
12-11 18:23:32.677 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.6892639069186018
12-11 18:23:32.678 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.7384970431270734
12-11 18:23:32.678 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.787730179335545
12-11 18:23:32.678 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.8369633155440165
12-11 18:23:32.679 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.95
12-11 18:23:32.679 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.95
12-11 18:23:32.679 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.95
12-11 18:23:32.680 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 0.95
12-11 18:23:33.554 D/LogUtils-BaseCallback: 901c1c8cbf0ffccff7d102f7ad800f63.epub | 1.0
12-11 18:23:33.554 I/LogUtils-BaseCallback: key -> 901c1c8cbf0ffccff7d102f7ad800f63.epub
12-11 18:23:33.562 I/LogUtils-BaseCallback: responseInfo -> {"duration":880.0,"host":"upload-z1.qiniu.com","id":"1544523812658576","ip":"/10.70.128.76:8888","path":"/","port":80,"reqId":"qpAAAJa8j7w6QG8V","response":{"nameValuePairs":{"key":"901c1c8cbf0ffccff7d102f7ad800f63.epub","hash":"FjX_l7-9nxXF3hXD06tJQDTvBOvS","fsize":165647}},"sent":0,"statusCode":200,"timeStamp":1544523813,"xlog":"body;0s.ph;0s.put.in;0s.put.disk:4;1s.put.in;1s.put.disk:5;1s.ph;PFDS:6;0s.put.out:3;PFDS:7;body;rs13_shard.sel:2;rwro.ins:2/same entry;rs13_shard.sel:2;rwro.get:2;MQ;RS.not:;RS:6;rs.put:6;rs-upload.putFile:17;UP:18","xvia":""}
12-11 18:23:33.563 I/LogUtils-BaseCallback: jsonObject -> {"nameValuePairs":{"key":"901c1c8cbf0ffccff7d102f7ad800f63.epub","hash":"FjX_l7-9nxXF3hXD06tJQDTvBOvS","fsize":165647}}
12-11 18:23:33.563 I/LogUtils-BaseCallback: Upload Success
12-11 18:23:33.565 I/LogUtils-SharedUtils: GET key_account_token -> 5095c8097aef4e6b9c578122a1f5a713
12-11 18:23:33.566 D/LogUtils-RequestSender: send -> request_info |
                                             request_url -> https://apitest.readeryun.com/api/book/v1/confirm
                                             request_method -> POST_STRING
                                             request_headers -> {sign=440513106951a29987c5099a6ccb7b42, token=5095c8097aef4e6b9c578122a1f5a713}
                                             request_params -> {}
                                             request_files -> {}
                                             request_content -> {"commonData":{"terminal":"ANDROID_PHONE","timestamp":"1544523813563"},"param":{"bookId":"210","sevenFileName":"901c1c8cbf0ffccff7d102f7ad800f63.epub","sevenFileSize":"165647","sevenHash":"FjX_l7-9nxXF3hXD06tJQDTvBOvS"}}
                                             request_filePath ->
                                             request_jsonObject -> {}
12-11 18:23:34.632 D/LogUtils-RequestSender: send -> response_info |
                                             request_url -> https://apitest.readeryun.com/api/book/v1/confirm
                                             response_data -> {"statusCode":900,"data":{"url":"http://restest.readeryun.com/901c1c8cbf0ffccff7d102f7ad800f63.epub"},"timestamp":1544523818394,"msg":null}
