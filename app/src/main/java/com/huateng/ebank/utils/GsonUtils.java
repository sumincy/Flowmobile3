package com.huateng.ebank.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by shanyong on 2016/10/13.
 */

public class GsonUtils {

    private static Gson gson = null;

    static {
        gson = new Gson();
    }

    private GsonUtils() {

    }

    //解析Json数据
    public static <T> T fromJson(String jsonData, Class<T> entityType) {
        T t = gson.fromJson(jsonData, entityType);
        return t;
    }

    //解析Json数据
    public static <T> T fromJson(String jsonData, Type type) {
        T t = gson.fromJson(jsonData, type);
        return t;
    }

    //解析Json数据成map
    public static HashMap<String,String> fromJson(String jsonData) {
        HashMap<String,String> map= GsonUtils.fromJson(jsonData,new TypeToken<HashMap<String,String>>(){}.getType());
        return map;
    }

    //转成json
    public static String toJson(Object obj) {
        if (gson == null) {
            gson = new Gson();
        }
        String json = gson.toJson(obj);
        return json;
    }

    public static boolean isJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            Log.e("gson","bad json: " + json);
            return false;
        }
    }
}
