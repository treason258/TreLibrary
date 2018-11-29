
/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            佛祖保佑       永无BUG
*/


// ******************************** project ********************************
// ******************************** test ********************************
// **************** test ****************
// test
<!-- ******************************* test ******************************* -->
<!-- **************** test **************** -->
<!-- test -->
## ******************************** test ********************************
## **************** test ****************
## test
// -------------------------------- test --------------------------------
// ---------------- test ----------------
// //////////////////////////////// test ////////////////////////////////
// //////////////// test ////////////////


// ******************************** TreBundle项目结构 ********************************

lib_TreKernel	Tre依赖库
com.mjiayou.trekernel

lib_Umeng		第三方-友盟
com.umeng.example

lib_TreCore		Tre核心封装
com.mjiayou.trecore

lib_TreView		Tre自定义控件
com.mjiayou.treview

TreBundle 		Tre测试
com.mjiayou.trebundle

TreBundleDemo 	预开发APP
com.mjiayou.trebundledemo

// ******************************** TreBundle集成步骤 ********************************

// **************** A-新建项目 ****************

1、新建项目

2、修改.ignore文件

3、添加README.md文件

4、添加readme.txt文件

5、编译调试通过，提交SVN/GIT备份

// **************** B-集成TreBundle ****************

1、修改settings.gradle文件，添加
include ':lib_TreKernel', ':lib_TreCore', ':lib_TreView', ':lib_Umeng'
project(':lib_TreKernel').projectDir = new File('/Users/treason/Documents/AndroidStudio/TreBundle/lib_TreKernel')
project(':lib_TreCore').projectDir = new File('/Users/treason/Documents/AndroidStudio/TreBundle/lib_TreCore')
project(':lib_TreView').projectDir = new File('/Users/treason/Documents/AndroidStudio/TreBundle/lib_TreView')
project(':lib_Umeng').projectDir = new File('/Users/treason/Documents/AndroidStudio/TreBundle/lib_Umeng')

include ':TreBundle'
project(':TreBundle').projectDir = new File('/Users/treason/Documents/AndroidStudio/TreBundle/app')

2、修改app/build.gradle文件，添加
compile project(':lib_TreCore')

3、修改MainActivity，调用TreCore功能测试

4、编译调试通过，提交SVN/GIT备份

// ******************************** keystore.properties ********************************

#    storeFile file('trecore-debug-140608.jks') // 799b01452f716947c2d3623e21e98de5
#    storePassword 'android'
#    keyAlias 'androiddebugkey' // 0d5bd6d898273e2edd70a889517a5087 - 0D:5B:D6:D8:98:27:3E:2E:DD:70:A8:89:51:7A:50:87
#    keyPassword "android"
#
#    storeFile file('trecore-release-161214.jks') // c0597286cdcfce1b2881e4135e381559
#    storePassword 'xiaoya'
#    keyAlias 'trecore' // 2414b9cd69131a2b122024904fa3e0da - 24:14:B9:CD:69:13:1A:2B:12:20:24:90:4F:A3:E0:DA
#    keyPassword "xiaoya"
#    keyAlias 'trebundle' // 9b525eb4c5b006aea12e4e95bfabd732 - 9B:52:5E:B4:C5:B0:06:AE:A1:2E:4E:95:BF:AB:D7:32
#    keyPassword "xiaoya"

# 签名信息-debug
signing_debug_storeFile=local/trecore-debug-140608.jks
signing_debug_storePassword=android
signing_debug_keyAlias=androiddebugkey
signing_debug_keyPassword=android
# 签名信息-release
signing_release_storeFile=local/trecore-release-161214.jks
signing_release_storePassword=xiaoya
signing_release_keyAlias=trebundle
signing_release_keyPassword=xiaoya

// ******************************** project ********************************


