package com.huateng.ebank.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;


/**
 * 网络配置
 * Created by shanyong on 2017/9/22.
 * TODO :Do not place Android context classes in static fields
 */

public final class NetworkConfig {

    public static final String MPUSH_CHANNEL_ID = "com.huateng.merchantjcb.mpush.channel";

    private static final String SP_KEY_API_MODE = "api_mode";
    private static final String SP_KEY_DOMAIN = "domain";
    private static final String SP_KEY_CUSTOM_IP = "custom_ip";
    private static final String SP_KEY_CUSTOM_PORT = "custom_port";
    private static final String SP_KEY_CUSTOM_PATH = "custom_path";
    private static final String SP_KEY_CUSOMT_URL = "custom_url";
    private static final String SP_KEY_COOKIE = "cookie";

    private static final String SP_KEY_PUSH_IP = "push_ip";
    private static final String SP_KEY_PUSH_PORT = "push_port";

    public static NetworkConfig C = new NetworkConfig();

    public static String mpushKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHYbtdqqPYTPIaLyNh+Gwzjqyuuxoe0lEkHCAu6HHqxrdwlcOBK99X2tOIvTBK1S5OhSwl14/w6IJ82OhQ3Gap46jmdgfnEsZk3l3szG3OZSa1Fer7ikpaLln60YIEtPR8tYpbKeGeXY//JqHmBJy8G1TDvbMm5nyLkWzmi3o1JwIDAQAB";

    public static String PUB_KEY;
    //    public static String CLIENT_BKS;
//    public static String B_PWD;
    public static String SERVER_CER;

    private Context ctx;
    private SharedPreferences sp;

    public void init(Context context) {
        /**
         *  scheme://ip:port/path/
         */
        ctx = context.getApplicationContext();
        sp = new SecurePreferences(ctx, "", "net_config.xml");
        String apiMode = getApiMode();
        String ip = getCustomIp();
        String port = getCustomPort();
        String path = getCustomPath();
        if (apiMode.equals(ApiConstants.API_MODE_DEVELOP)) {
            ApiConstants.DEVELOP_BASE_URL = String.format("https://%s:%s/%s/", ip, port, path);
        }
//        Logger.i(apiMode);
    }

    public NetworkConfig checkInit(Context context) {
        if (ctx == null) {
            init(context);
        }
        return this;
    }

    //API模式
    public void setApiMode(String appMode) {
        putString(SP_KEY_API_MODE, appMode);
    }

    public void setCustomPath(String path) {
        putString(SP_KEY_CUSTOM_PATH, path);
    }

    public void setCustomIp(String ip) {
        putString(SP_KEY_CUSTOM_IP, ip);
    }

    public void setCustomPort(String port) {
        putString(SP_KEY_CUSTOM_PORT, port);
    }

    public void setCustomURL(String url) {
        putString(SP_KEY_CUSOMT_URL, url);
    }

    public void setDomain(String domain) {
        putString(SP_KEY_DOMAIN, domain);
    }

    public void setCookie(String cookie) {
        putString(SP_KEY_COOKIE, cookie);
    }

    public void setPushIp(String ip) {
        putString(SP_KEY_PUSH_IP, ip);
    }

    public void setPushPort(int port) {
        putInt(SP_KEY_PUSH_PORT, port);
    }

    public String getApiMode() {
        return sp.getString(SP_KEY_API_MODE, ApiConstants.API_MODE_RELEASE);
    }

    public String getCustomIp() {
        return sp.getString(SP_KEY_CUSTOM_IP, "127.0.0.1");
    }

    public String getCustomPort() {
        return sp.getString(SP_KEY_CUSTOM_PORT, "8080");
    }

    public String getCustomPath() {
        return sp.getString(SP_KEY_CUSTOM_PATH, "app");
    }

    public String getCustomURL() {
        return sp.getString(SP_KEY_CUSOMT_URL, ApiConstants.RELEASE_BASE_URL);
    }

    public String getPushIp() {
        return sp.getString(SP_KEY_PUSH_IP, ApiConstants.PUSH_IP);
    }

    public int getPushPort() {
        return sp.getInt(SP_KEY_PUSH_PORT, ApiConstants.PUSH_PORT);
    }

    public String getDomain() {
        return sp.getString(SP_KEY_DOMAIN, ApiConstants.DOMAIN);
    }

    public String getCookie() {
        return sp.getString(SP_KEY_COOKIE, "");
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //根据mode 获取BaseUrl
    public String getBaseURL() {
        String apiMode = getApiMode();

        String baseUrl = ApiConstants.RELEASE_BASE_URL;
        if (apiMode.equals(ApiConstants.API_MODE_DEVELOP)) {
            baseUrl = ApiConstants.DEVELOP_BASE_URL;
        } else if (apiMode.equals(ApiConstants.API_MODE_RELEASE)) {
            baseUrl = ApiConstants.RELEASE_BASE_URL;
        } else if (apiMode.equals(ApiConstants.API_MODE_CUSTOM)) {
            baseUrl = getCustomURL();
        }

        return baseUrl;
    }

    public Context getCtx() {
        return ctx;
    }
}
