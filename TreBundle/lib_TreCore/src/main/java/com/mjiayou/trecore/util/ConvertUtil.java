package com.mjiayou.trecore.util;

import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.mjiayou.trecorelib.bean.entity.TCSinaStatuses;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.StreamUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

/**
 * Created by treason on 16/5/14.
 */
public class ConvertUtil<T> {

    // ******************************** List & Array & String ********************************

    /**
     * Array TO List
     */
    public static List<String> parseStringList(String[] array) {
        return Arrays.asList(array);
    }

    /**
     * String TO List
     *
     * @param space 间隔符
     */
    public static List<String> parseStringList(String str, String space) {
        return Arrays.asList(str.split(space));
    }

    /**
     * List TO Array
     */
    public static String[] parseStringArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    /**
     * String TO Array
     *
     * @param space 间隔符
     */
    public static String[] parseStringArray(String str, String space) {
        return str.split(space);
    }

    /**
     * List TO String
     *
     * @param space 间隔符
     */
    public static String parseString(List<String> list, String space) {
        if (list == null) {
            return null;
        }

        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("")) {
                continue;
            }
            if (result.equals("")) {
                result += list.get(i);
            } else {
                result += (space + list.get(i));
            }
        }
        return result;
    }

    /**
     * Array TO String
     *
     * @param space 间隔符
     */
    public static String parseString(String[] array, String space) {
        if (array == null) {
            return null;
        }

        String result = "";
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("")) {
                continue;
            }
            if (result.equals("")) {
                result += array[i];
            } else {
                result += (space + array[i]);
            }
        }
        return result;
    }

    // ******************************** parseString ********************************

    /**
     * InputStream TO String
     */
    public static String parseString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(is), 1024);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } finally {
            StreamUtil.closeQuietly(is);
        }
    }

    /**
     * GZIP解压缩操作
     */
    public static String parseString(byte[] data) {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = (data[0] << 8) | data[1] & 0xFF;
        boolean t = head == 0x1f8b;
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (t) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Map TO String
     */
    public static String parseString(Map data) {
        if (data == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Object key : data.keySet()) {
            builder.append(key).append(" = ").append(data.get(key) == null ? "null" : data.get(key).toString()).append("\r\n");
        }
        return builder.toString();
    }

    /**
     * Bundle TO String
     */
    public static String parseString(Bundle data) {
        if (data == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String key : data.keySet()) {
            builder.append(key).append(" = ").append(data.get(key) == null ? "null" : data.get(key).toString()).append("\r\n");
        }
        return builder.toString();
    }

    /**
     * Properties TO String
     */
    public static String parseString(Properties data) {
        if (data == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Object key : data.keySet()) {
            builder.append(key).append(" = ").append(data.get(key) == null ? "null" : data.get(key).toString()).append("\r\n");
        }
        return builder.toString();
    }

    /**
     * Class TO String
     */
    public static String parseString(Class clazz) {
        if (clazz == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                builder.append(field.getName()).append(" = ").append(field.get(null) == null ? "null" : field.get(null).toString()).append("\n");
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
            }
        }
        return builder.toString();
    }

    // ******************************** parseStringList ********************************

    /**
     * JsonArray TO List
     */
    public static List<String> parseStringListByJA(String jsonArray) {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return GsonHelper.get().fromJson(jsonArray, type);
    }

    /**
     * List<TCSinaStatuses> TO List<String>
     */
    public static List<String> parseStringList(List<TCSinaStatuses> data) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getUser().getName();
            String location = data.get(i).getUser().getLocation();
            String text = data.get(i).getText();
            result.add(name + "(" + location + ")" + ":" + text);
        }
        return result;
    }

    // ******************************** tools ********************************

    /**
     * 字符串保持显示count个字符之内
     */
    public static String limitString(String text, int count) {
        if (text.length() > count) {
            text = text.substring(0, count) + "...";
        }
        return text;
    }

    /**
     * 使Map按key进行排序
     */
    public static Map<String, String> sortByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        sortMap.putAll(map);
        return sortMap;
    }

    // ******************************** project ********************************

}
