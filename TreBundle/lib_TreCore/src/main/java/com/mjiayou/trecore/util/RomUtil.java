//package com.mjiayou.trecore.util;
//
//import android.os.Environment;
//
//import com.mjiayou.trecorelib.util.LogUtil;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.Properties;
//
///**
// * Created by treason on 2016/11/24.
// */
//
//public class RomUtil {
//
//    /**
//     * 考虑到的ROM枚举
//     */
//    public enum ROM {
//        MIUI("小米系统"), EMUI("华为系统"), FLYME("魅族系统"), OTHER("其他系统（非小米、华为、魅族）");
//
//        private String name;
//
//        ROM(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public String toString() {
//            return name;
//        }
//    }
//
//    // MIUI特性
//    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
//    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
//    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
//    // EMUI特性
//    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
//    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
//    private static final String KEY_EMUI_CONFIG_HW_SYSTEM_VERSION = "ro.confg.hw_systemversion";
//    // FLYME特性
//    private static final String KEY_FLYME_DISPLAY_ID = "ro.build.display.id";
//    private static final String KEY_FLYME = "flyme";
//
//    /**
//     * 获取系统ROM
//     * <p>
//     * 参考
//     * http://blog.csdn.net/jin_qing/article/details/53087164
//     * http://blog.csdn.net/xx326664162/article/details/52438706
//     */
//    public static ROM getRom() {
//        try {
//            Properties buildProperties = getBuildProperties();
//            if (buildProperties == null) {
//                return ROM.OTHER;
//            }
//            if (buildProperties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
//                    || buildProperties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
//                    || buildProperties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
//                return ROM.MIUI; // 小米系统
//            } else if (buildProperties.getProperty(KEY_EMUI_API_LEVEL, null) != null
//                    || buildProperties.getProperty(KEY_EMUI_VERSION, null) != null
//                    || buildProperties.getProperty(KEY_EMUI_CONFIG_HW_SYSTEM_VERSION, null) != null) {
//                return ROM.EMUI; // 华为系统
//            } else if (getSystemProperties(KEY_FLYME_DISPLAY_ID, "").toLowerCase().contains(KEY_FLYME)) {
//                return ROM.FLYME; // 魅族系统
//            } else {
//                return ROM.OTHER; // 其他系统
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ROM.OTHER;
//        }
//    }
//
//    /**
//     * 获取手机Properties文件内容
//     */
//    public static Properties getBuildProperties() {
//        try {
//            Properties properties = new Properties();
//            File propFile = new File(Environment.getRootDirectory(), "build.prop");
//            FileInputStream fis = null;
//            if (propFile.exists()) {
//                try {
//                    fis = new FileInputStream(propFile);
//                    properties.load(fis);
//                    fis.close();
//                    fis = null;
//                } catch (IOException e) {
//                    LogUtil.printStackTrace(e);
//                } finally {
//                    if (fis != null) {
//                        try {
//                            fis.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            return properties;
//        } catch (Exception e) {
//            LogUtil.printStackTrace(e);
//            return null;
//        }
//    }
//
//    public static Properties getPropertiesOld() {
//        try {
//            Properties properties = new Properties();
//            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
//            return properties;
//        } catch (IOException e) {
//            LogUtil.printStackTrace(e);
//            return null;
//        }
//    }
//
//    /**
//     * 获取SystemProperties中key对应的value
//     */
//    public static String getSystemProperties(String key, String defaultValue) {
//        try {
//            Class<?> clz = Class.forName("android.os.SystemProperties");
//            Method get = clz.getMethod("get", String.class, String.class);
//            return (String) get.invoke(clz, key, defaultValue);
//        } catch (Exception e) {
//            LogUtil.printStackTrace(e);
//            return defaultValue;
//        }
//    }
//}