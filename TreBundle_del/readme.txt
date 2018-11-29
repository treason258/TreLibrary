
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


// ******************************** test ********************************
// **************** test ****************
//
<!-- ******************************* test ******************************* -->
<!-- **************** test **************** -->
<!-- custom -->

// -------------------------------- test --------------------------------
// ---------------- test ----------------
//
// //////////////////////////////// test ////////////////////////////////
// //////////////// test ////////////////
//
## ******************************** test ********************************
## **************** test ****************


// ******************************** 打包注意事项 ********************************

1.build.gradle
applicationId
versionCode
versionName
minifyEnabled

2.TCApp

3.TCConfigs
mMode = MODE_RELEASE;

// ******************************** 更新日志 ********************************

// ******************************** 项目结构 ********************************

// ******************************** linux ********************************

javah -jni com.mjiayou.trecore.test.jni.TestJNIUtil     生成JNI的.h文件

// ******************************** adb shell ********************************

./adb shell

dumpsys activity        列出activity堆栈列表

ps | grep com.mjiayou   查看当前所存在的进程信息

// ******************************** TreCore集成步骤 ********************************


// **************** A-新建项目 ****************

1.
新建项目

2.
修改.ignore文件

2.
添加README.md文件

3.
添加readme.txt文件

4.
编译调试通过，提交SVN/GIT备份


// **************** B-导入依赖库 ****************

1.
分别拷贝 lib_TreCore lib_TreView lib_Umeng 文件夹到项目根目录下

2.
文件 settings.gradle 添加一行：
include ':lib_TreCore', ':lib_TreView', ':lib_Umeng'

3.
文件 app - build.gradle 在 dependencies 节点下添加：
compile project(':lib_TreCore')
compile project(':lib_TreView')
compile project(':lib_Umeng')

4.
文件 app - build.gradle 在 android 节点下添加：
packagingOptions {
    exclude 'META-INF/services/javax.annotation.processing.Processor'
}

5.
编译调试通过，提交SVN/GIT备份


// **************** C-导入TreCore相关资源和代码 ****************

1.
拷贝/app/src/main下的 res-trecore jni aidl 文件夹到 项目/app/src/main 目录下

2.
配置项目NDK目录:/Users/treason/Archives/dev/android-ndk-r13

3.
完全替换gradle.properties文件

4.
文件 app - build.gradle 在 android 节点下添加：
sourceSets {
    main.res.srcDirs += 'src/main/res-trecore'
}

5.
拷贝 com/mjiayou/trecore 文件夹到 项目/app/src/main/java 目录下

6.
执行gradle sync & build，根据提示解决错误（替换import错误和删除脏数据）
可以考虑全局替换:
import com.mjiayou.trecoredemo.R;
TO
import com.xxx.xxx.R;
需要操作的大概有：
com.mjiayou.trecore.net.*
com.mjiayou.trecore.ui.*
com.mjiayou.trecore.widget.*

7.
编译调试通过，提交SVN/GIT备份


// **************** D-接入TreCore功能 ****************

1.
文件 app - AndroidManifest 在 application 节点下修改：
android:theme="@style/tc_theme_default"

2.
文件 app - AndroidManifest 在 application 节点下添加：
android:name="com.mjiayou.trecore.TCApp"
android:largeHeap="true"

3.
文件 app - AndroidManifest 在 manifest 节点下添加：
从 TreCore-Start 到 TreCore-End 的 uses-permission

4.
文件 app - AndroidManifest 在 application 节点下添加：
从 TreCore-Start 到 TreCore-End 的内容（包括友盟系列和DebugActivity等）

5.
文件 MainActivity 修改继承 TCActivity ，在 onCreate 下添加：
addRightTextView("DEBUG", MENU_DEBUG);

6.
文件 app - build.gradle 在 android 节点下添加：
productFlavors {
    // -1.调试包 - alpha
    alpha {}
    // 0.未分渠道包 - beta
    beta {}
}
productFlavors.all { flavor -> flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name] }

7.
编译调试通过，提交SVN/GIT备份

// **************** E-预开发阶段 ****************

1.
文件 TCApp 修改：
APP_NAME = "proname"; // 全小写

2.
拷贝 proguard-rules.pro 全文，修改 custom 部分为项目包名

3.
新建 proname-release.jks 签名文件

4.
拷贝 proname-release.jks 和 trecore-debug.jks 文件到 app 目录

5.
文件 app - build.gradle 在 根节点下添加：
def buildTime(String yyMMddHHmm) {
    def simpleDateFormat = new SimpleDateFormat(yyMMddHHmm)
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"))
    return simpleDateFormat.format(new Date())
}

6.
文件 app - build.gradle 在 android 节点下按需修改：
versionName "1.0.0"

7.
文件 app - build.gradle 在 android 节点下添加并按需修改：
signingConfigs

8.
文件 app - build.gradle 在 android 节点下按需修改：
buildTypes

9.
编译调试通过，分别打debug和release包，获取签名MD5值

10.
编译调试通过，提交SVN/GIT备份

// **************** F-开发 ****************



// **************** Z-其他 ****************

assets
jniLibs

multiDexEnabled true // Dex突破65535的限制

lintOptions {
    abortOnError false
}

渠道：
// 1.官方网站 - official
official {}
// 2.友盟 - umeng
umeng {}
// 3.FIR.IM - firim
firim {}
// 4.云测 - ctestin
ctestin {}
// 5.爱加密 - ijiami
ijiami {}
// 6.阿里云测试 - aliyuntest
aliyuntest {}

// 1.应用宝 - yingyongbao
yingyongbao {}
// 2.豌豆夹 - wandoujia
wandoujia {}
// 3.百度 - baidu
baidu {}
// 4.91助手 - c91
c91 {}
// 5.安卓市场 - anzhuo
anzhuo {}
// 6.360 - c360
c360 {}
// 7.安智市场 - anzhi
anzhi {}
// 8.应用汇 - yingyonghui
yingyonghui {}
// 9.小米 - xiaomi
xiaomi {}
// 10.魅族 - meizu
meizu {}

// 11.华为 - huawei
huawei {}
// 12.联想 - lianxiang
lianxiang {}
// 13.OPPO - oppo
oppo {}
// 14.三星 - sanxing
sanxing {}

// ******************************** QUESTION ********************************

// ******************************** TREASON ********************************
