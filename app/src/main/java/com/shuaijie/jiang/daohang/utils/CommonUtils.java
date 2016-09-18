package com.shuaijie.jiang.daohang.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者:姜帅杰
 * 版本:1.0
 * 创建日期:2016/9/4:11:20.
 */
public class CommonUtils {
    public static void setSpStr(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getSpStr(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setSpBool(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getSpBool(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setSpInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getSpInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static boolean isNetwork() {
        return false;
    }
}
