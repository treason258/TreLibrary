package com.mjiayou.trecore.common;


import com.mjiayou.trecore.R;
import com.mjiayou.trecore.base.TCApp;

/**
 * Created by treason on 15/6/8.
 */
public class Configs {

    private static final String TAG = "Configs";

    // DEBUG开关
    public static boolean DEBUG_SERVER; // 服务器
    public static boolean DEBUG_TEST; // TestActivity
    public static boolean DEBUG_LOG; // LOG
    public static boolean DEBUG_LOG_SHOW_PATH; // LOG PATH
    public static boolean DEBUG_VOLLEY;
    public static boolean DEBUG_GSON;
    public static boolean DEBUG_IMAGE_LOADER;
    public static boolean DEBUG_UMENG;
    public static boolean DEBUG_PULL_TO_REFRESH;
    public static boolean DEBUG_ALIYUN_OSS;
    // SWITCH开关
    public static boolean SWITCH_UMENG_ANALYTICS_ON; // 友盟统计

    // 打包模式
    private static final int MODE_DEBUG = 0; // 调试模式
    private static final int MODE_RELEASE = 1; // 生产模式
    // 当前APP打包模式
    private static int mMode = MODE_DEBUG;

    static {
        /**
         * 启动模式
         */
        switch (mMode) {
            case MODE_DEBUG:
                // DEBUG模式开关
                DEBUG_SERVER = true;
                DEBUG_TEST = true;
                DEBUG_LOG = true;
                DEBUG_LOG_SHOW_PATH = false;
                DEBUG_VOLLEY = false;
                DEBUG_GSON = false;
                DEBUG_IMAGE_LOADER = false;
                DEBUG_UMENG = true;
                DEBUG_PULL_TO_REFRESH = false;
                DEBUG_ALIYUN_OSS = false;
                // SWITCH-一些开关
                SWITCH_UMENG_ANALYTICS_ON = true;
                break;
            case MODE_RELEASE:
                // DEBUG模式开关
                DEBUG_SERVER = false;
                DEBUG_TEST = false;
                DEBUG_LOG = false;
                DEBUG_LOG_SHOW_PATH = false;
                DEBUG_VOLLEY = false;
                DEBUG_GSON = false;
                DEBUG_IMAGE_LOADER = false;
                DEBUG_UMENG = false;
                DEBUG_PULL_TO_REFRESH = false;
                DEBUG_ALIYUN_OSS = false;
                // SWITCH-一些开关
                SWITCH_UMENG_ANALYTICS_ON = true;
                break;
        }
    }

    // 第三方账号
    // 友盟
    public static final String UMENG_APP_KEY = TCApp.get().getString(R.string.umeng_app_key);
    public static final String UMENG_CHANNEL = TCApp.get().getString(R.string.umeng_channel);
    // 新浪
    public static final String SINA_APP_KEY = TCApp.get().getString(R.string.sina_app_key);
    public static final String SINA_APP_SECRET = TCApp.get().getString(R.string.sina_app_secret);
    // QQ
    public static final String QQ_APP_ID = TCApp.get().getString(R.string.qq_app_id);
    public static final String QQ_APP_KEY = TCApp.get().getString(R.string.qq_app_key);
    // 微信
    public static final String WEIXIN_APP_ID = TCApp.get().getString(R.string.weixin_app_id);
    public static final String WEIXIN_APP_SECRET = TCApp.get().getString(R.string.weixin_app_secret);

    // meta-data
    public static final String META_TC_KEY = "TC_KEY";
    public static final String META_UMENG_APPKEY = "UMENG_APPKEY";
    public static final String META_UMENG_CHANNEL = "UMENG_CHANNEL";

    // DEFAULT
    public static final String DEFAULT_SHARE_IMAGE_URL = "http://img.soccerapp.cn/userfiles/f13d9da1f5e54ac7a7be3e7d074b6d03/images/cms/imageinfo/2015/10/33.jpg";
    public static final String DEFAULT_SHARE_TARGET_TITLE = "Soccer";
    public static final String DEFAULT_SHARE_TARGET_CONTENT = "招聘贴:视频包装设计师";
    public static final String DEFAULT_SHARE_TARGET_URL = "http://m.soccerapp.cn/f/share/view-d2de7170d8be4232b9ba6e587241cd96-17c43c75184d4e3eae7be33254077b69.html";
    public static final String DEFAULT_URL = "http://m.soccerapp.cn/f/share/view-d2de7170d8be4232b9ba6e587241cd96-17c43c75184d4e3eae7be33254077b69.html";

    // URL
    public static final String URL_PREFIX_HTTP = "http";

    // DELAY
    public static final long DELAY_CRASH_FINISH = 3000; // 全局捕获崩溃时，崩溃出现到程序结束之间的时间

    // ImageLoader
    public static final long IMAGELOADER_CACHE_MAX_SIZE = 200 * 1024 * 1024; // ImageLoader缓存最多200M
    public static final int IMAGELOADER_MAX_WIDTH = 480; // 480,720
    public static final int IMAGELOADER_MAX_HEIGHT = 800; // 800,1280

    public static final long TIME_GET_VERIFY_CODE = 10000; // 获取验证码点击一次之后等待时间-60s
    public static final long TIME_GET_VERIFY_CODE_INTERVAL = 1000; // 获取验证码点击一次之后等待时间内间隔时间-1s

    // 系统的Loading样式
    public static int LOADING_STYLE = Params.LOADING_STYLE_DEFAULT_PROGRESS;

    public static final float BANNER_SCALE = 1f;  // Banner缩小时的缩放比例

    public static final int AVATAR_MAX_NUM_OF_PIXELS = 200 * 200;
    public static final int FEED_BACK_PIXELS = 200 * 200;

    public static final int CAMERA_PREVIEW_MIN_WIDTH = 720; // 相机预览尺寸最小宽度
    public static final int CAMERA_PICTURE_MIN_WIDTH = 1080; // 相机拍照尺寸最小宽度
    public static final int CAMERA_DISPLAY_ORIENTATION = 90; // 相机默认拍摄角度

    // volley
    public static final int DEFAULT_TIMEOUT_MS = 100 * 1000; // 默认超时时间 - DefaultRetryPolicy.DEFAULT_TIMEOUT_MS=2500
    public static final int DEFAULT_MAX_RETRIES = 1; // 默认重连次数 - DefaultRetryPolicy.DEFAULT_MAX_RETRIES=1
    public static final float DEFAULT_BACKOFF_MULT = 1.0f; // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT=1.0f

    public static final String TAG_VOLLEY = "net_volley";
    public static final String TAG_REQUEST_URL = "request_url";
    public static final String TAG_REQUEST_METHOD = "request_method";
    public static final String TAG_REQUEST_HEADERS = "request_headers";
    public static final String TAG_REQUEST_BODY = "request_body";
    public static final String TAG_REQUEST_PARAMS = "request_params";
    public static final String TAG_RESPONSE = "response_data";
    public static final String TAG_RESPONSE_STRING = "response_data_string";
    public static final String TAG_RESPONSE_OBJECT = "response_data_object";

    // ******************************** project ********************************
}

