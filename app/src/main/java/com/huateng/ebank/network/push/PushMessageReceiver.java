//package com.huateng.ebank.network.push;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.huateng.merchantjcb.app.Config;
//import com.huateng.merchantjcb.app.Constants;
//import com.huateng.merchantjcb.app.MainApplication;
//import com.huateng.merchantjcb.app.Paths;
//import com.huateng.merchantjcb.receiver.NotificationDO;
//import com.huateng.merchantjcb.utils.CommonUtils;
//import com.huateng.push.TagAliasOperatorHelper;
//import com.orhanobut.logger.Logger;
//import com.tools.utils.StringUtils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import cn.jpush.android.api.CmdMessage;
//import cn.jpush.android.api.CustomMessage;
//import cn.jpush.android.api.JPushMessage;
//import cn.jpush.android.api.NotificationMessage;
//import cn.jpush.android.service.JPushMessageReceiver;
//
//public class PushMessageReceiver extends JPushMessageReceiver {
//    private static final String TAG = "PushMessageReceiver";
//
//    @Override
//    public void onMessage(Context context, CustomMessage customMessage) {
//        Log.e(TAG, "[onMessage] " + customMessage);
//        processCustomMessage(context, customMessage);
//    }
//
//    @Override
//    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
//        Log.e(TAG, "[onNotifyMessageOpened] " + message);
//
//        //打开自定义的Activity
//        JSONObject jsonObject = null;
//        try {
//
//            jsonObject = new JSONObject(message.notificationExtras);
//            String type = jsonObject.optString("type");
//            if (Constants.PushType.ORDER.equals(type)) {
//                ARouter.getInstance().build(Paths.PAGE_TRADES)
//                        .navigation();
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
//        Log.e(TAG, "[onNotifyMessageArrived] " + message);
//    }
//
//    @Override
//    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
//        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
//    }
//
//    @Override
//    public void onRegister(Context context, String registrationId) {
//        Log.e(TAG, "[onRegister] " + registrationId);
//
//    }
//
//    @Override
//    public void onConnected(Context context, boolean isConnected) {
//        Log.e(TAG, "[onConnected] " + isConnected);
//    }
//
//    @Override
//    public void onCommandResult(Context context, CmdMessage cmdMessage) {
//        Log.e(TAG, "[onCommandResult] " + cmdMessage);
//    }
//
//    @Override
//    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
//        super.onTagOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
//        super.onCheckTagOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
//        super.onAliasOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
//        super.onMobileNumberOperatorResult(context, jPushMessage);
//    }
//
//    //send msg to ActivityMain
//    private void processCustomMessage(Context context, CustomMessage customMessage) {
//
//        String message = customMessage.message;
//        String extras = customMessage.extra;
//
//        if (!TextUtils.isEmpty(message) && CommonUtils.isJson(message)) {
//            final NotificationDO ndo = fromJson(message);
//            Logger.i(message);
//
//            if (ndo != null) {
//                //语音播报
//                Intent it = new Intent(context, MyReceiver.class);
//
//                JSONObject jo = ndo.getExtras();
//                if (null != jo) {
//                    //推送类型 type
//                    String type = jo.optString("type");
//                    it.putExtra("my_extra", jo.toString());
//                    it.putExtra("type", type);
//
//                    String content = ndo.getContent();
//
//                    if (StringUtils.isNotEmpty(type)) {
//                        if (Constants.PushType.ORDER.equals(type) && Config.getBoolean(Config.ENABLE_SPEAKER)) {
//                            //语音播报
//                            if (!TextUtils.isEmpty(content)) {
//                                MainApplication.getApplication().getSpeechClient().speak(content);
//                            }
//                        }
//                    }
//
////                    it.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
////
////                    if (TextUtils.isEmpty(ndo.getTitle())) {
////                        ndo.setTitle("收款成功");
////                    }
////
////                    if (TextUtils.isEmpty(ndo.getTicker())) {
////                        ndo.setTicker(ndo.getTicker());
////                    }
////
////                    Notifications.I.notify(ndo, it);
//                }
//
//            }
//        }
//    }
//
//    private NotificationDO fromJson(String message) {
//        try {
//            JSONObject messageDO = new JSONObject(message);
//            NotificationDO ndo = new NotificationDO();
//            ndo.setContent(messageDO.optString("content"));
//            ndo.setTitle(messageDO.optString("title"));
//            ndo.setTicker(messageDO.optString("ticker"));
//            ndo.setNid(messageDO.optInt("nid", 1));
//            ndo.setExtras(messageDO.optJSONObject("extras"));
//            return ndo;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Logger.i("推送消息json 格式错误");
//        }
//        return null;
//    }
//
//}
