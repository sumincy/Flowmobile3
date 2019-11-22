package com.huateng.ebank.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

/**
 * Created by shanyong on 2017/9/1.
 * 商户资料
 */

public class Perference {

    private static SharedPreferences preferences;

    public static final String TOKEN = "token";
    public static final String USER_NAME = "user_name";
    public static final String USER_INFO = "user_info";
    public static final String TELEPHONE = "telephone";
    //账户已开通
    public static final String ACCOUNT_ESTABLISHED = "account_ established";
    public static final String USER_ID = "user_id";

    //角色类型
    public static final String ROLE_TYPE = "role_type";
    //上次登录时间
    public static final String LAST_LOGIN_TIME = "last_login_time";
    //查询参数
    public static final String PARAM_MAP = "paramMap";

    public static void init(Context context) {
        if (null == preferences) {
            preferences = new SecurePreferences(context, "ebank", "preferences");
        }
    }

    public static String get(String key) {
        return getString(key, "");
    }

    public static void set(String key, String value) {
        putString(key, value);
    }

    public static void setBoolean(String key, boolean value) {
        putBoolean(key, value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static void setLong(String key, long value) {
        putLong(key, value);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }


    public static void clear() {
        preferences.edit().clear().apply();
    }

    private static void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private static String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    private static void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private static void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    private static long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

}
