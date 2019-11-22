
package com.huateng.ebank.service.daemon;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.huateng.ebank.R;
import com.huateng.ebank.utils.CommonUtils;
import com.huateng.ebank.utils.RomUtil;
import com.tools.utils.AppUtils;


/**
 * Created by shanyong 2018/11/29
 *
 * @author sumincy@163.com
 */
public final class PushService extends Service {

    public static final int NOTIFICATION_ID = 1002;
    private static final String CHANNEL_ID = "com.huateng.merchantjcb.channel";

    //设置成前台服务
    public static void startForeground(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, PushService.class));
        } else {
            context.startService(new Intent(context, PushService.class));
        }
    }

    private int SERVICE_START_DELAYED = 5;
    public static final String ACTION_HEALTH_CHECK = "com.daemon.HEALTH_CHECK";
    public static int def_delay = 240000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        cancelAutoStartService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startAlarm(this, def_delay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForegroundService(this);
        } else {
            setForegroundServiceLowThanAndroidO(this);
        }

        flags = START_FLAG_REDELIVERY;
        SERVICE_START_DELAYED = 5;
        return super.onStartCommand(intent, flags, startId);
    }


    private static PendingIntent getOperation(Context context) {
        Intent intent = new Intent(context, PushService.class);
        PendingIntent operation = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return operation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        cancelAlarm(this);
//        startServiceAfterClosed(this, SERVICE_START_DELAYED);//5s后重启
    }


    /**
     * service停掉后自动启动应用
     *
     * @param context
     * @param delayed 延后启动的时间，单位为秒
     */
//    private static void startServiceAfterClosed(Context context, int delayed) {
//        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayed * 1000, getOperation(context));
//    }
//
//    public static void cancelAutoStartService(Context context) {
//        AlarmManager alarm = (AlarmManager) context
//                .getSystemService(Context.ALARM_SERVICE);
//        alarm.cancel(getOperation(context));
//    }
//    static void startAlarm(Context context, int delay) {
//        Intent it = new Intent(ACTION_HEALTH_CHECK);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, it, 0);
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, pi);
//        def_delay = delay;
//    }
//
//    static void cancelAlarm(Context context) {
//        Intent it = new Intent(ACTION_HEALTH_CHECK);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, it, 0);
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        am.cancel(pi);
//    }

    /**
     * 通过通知启动服务
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static void setForegroundService(Service context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //设定的通知渠道名称
            String channelName = "PushService";
            //设置通知的重要程度
            int importance = NotificationManager.IMPORTANCE_LOW;
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription("PushService");
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setAutoCancel(false) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态

        if (RomUtil.isFlyme() || RomUtil.isMiui() || RomUtil.isOppo()) {
            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", AppUtils.getAppPackageName(), null);
            i.setData(uri);
            PendingIntent pi = null;
            //intent 有效
            if (CommonUtils.isIntentAvailable(context, i)) {
                pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            builder.setContentTitle(AppUtils.getAppName() + "正在运行。")
                    .setContentText("触控来取得更多信息，或停止应用程序。")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pi);
        }

        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        context.startForeground(NOTIFICATION_ID, builder.build());
    }

    public static void setForegroundServiceLowThanAndroidO(Service context) {
        Notification.Builder builder = new Notification.Builder(context);

        if (RomUtil.isFlyme() || RomUtil.isMiui() || RomUtil.isOppo()) {
            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", AppUtils.getAppPackageName(), null);
            i.setData(uri);

            PendingIntent pi = null;

            //intent 有效
            if (CommonUtils.isIntentAvailable(context, i)) {
                pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            builder.setContentTitle(AppUtils.getAppName() + "正在运行。")
                    .setContentText("触控来取得更多信息，或停止应用程序。")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(false) //用户触摸时，自动关闭
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pi)
                    .setOngoing(true);//设置处于运行状态
        }

        context.startForeground(NOTIFICATION_ID, builder.build());
    }

}
