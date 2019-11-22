//package com.huateng.ebank.network.push;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.huateng.merchantjcb.app.Paths;
//import com.huateng.merchantjcb.utils.GsonUtils;
//import com.huateng.push.ExampleUtil;
//import com.orhanobut.logger.Logger;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import cn.jpush.android.api.JPushInterface;
//
///**
// * 自定义接收器
// * <p>
// * 如果不定义这个 Receiver，则：
// * 1) 默认用户会打开主界面
// * 2) 接收不到自定义消息
// */
//public class MyReceiver extends BroadcastReceiver {
//    private static final String TAG = "MyReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        try {
//            Bundle bundle = intent.getExtras();
//            Logger.d("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//
//            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                Logger.d("[MyReceiver] 接收Registration Id : " + regId);
//                //send the Registration Id to your server...
//
//            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                Logger.d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                Logger.d("[MyReceiver] 接收到推送下来的通知");
//                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                Logger.d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                Logger.d("[MyReceiver] 用户点击打开了通知");
//
//                Map<String, String> queryMap = new HashMap<>();
//                queryMap.put("transState", "02");
//
//                String initData = GsonUtils.toJson(queryMap);
//                ARouter.getInstance().build(Paths.PAGE_TRADES).
//                        navigation();
//
//            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                Logger.d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                Logger.w("[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
//            } else {
//                Logger.d("[MyReceiver] Unhandled intent - " + intent.getAction());
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    Logger.i(TAG, "This message has no Extra data");
//                    continue;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                    Iterator<String> it = json.keys();
//
//                    while (it.hasNext()) {
//                        String myKey = it.next();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
//                    }
//                } catch (JSONException e) {
//                    Logger.e(TAG, "Get message extra JSON error!");
//                }
//
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
//            }
//        }
//        return sb.toString();
//    }
//
//    private void processCustomMessage(Context context, Bundle bundle) {
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        if (!ExampleUtil.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//
//            } catch (JSONException e) {
//
//            }
//
//        }
//    }
//}
