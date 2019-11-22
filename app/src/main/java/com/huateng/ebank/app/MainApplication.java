package com.huateng.ebank.app;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.huateng.ebank.BuildConfig;
import com.huateng.ebank.R;
import com.huateng.ebank.db.DbCore;
import com.huateng.ebank.db.update.MigrationHelper;
import com.huateng.ebank.network.NetworkConfig;
import com.huateng.ebank.ui.main.SplashActivity;
import com.huateng.securitycore.SecuritySDK;
import com.kongzue.dialog.util.DialogSettings;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.tools.utils.SDCardUtils;
import com.tools.utils.Utils;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;

import static com.kongzue.dialog.util.DialogSettings.STYLE.STYLE_IOS;

/**
 * Created by shanyong on 2019/9/23.
 */
public class MainApplication extends Application {
    private static SecuritySDK securitySDK;
    private static MainApplication application;
    public static String cacheDir;
    public static String files;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        //mvvm
        BaseApplication.setApplication(this);
        //初始化数据库
        DbCore.init(this);
        DbCore.enableQueryBuilderLog(); //开启调试 log
        MigrationHelper.DEBUG = true;

        //工具类
        Utils.init(this);
        //安全相关处理
        securitySDK = SecuritySDK.getInstance(this);
        //反劫持提示
        securitySDK.initAntiHijack();

        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内部存储
         */
        if (getApplicationContext().getExternalCacheDir() != null && SDCardUtils.isSDCardEnable()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }

        files = String.format("%s%s", cacheDir, "/files/");

        //对话框样式
        DialogSettings.style = STYLE_IOS;
        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;
        DialogSettings.isUseBlur = false;   //设置是否启用模糊
        DialogSettings.cancelableTipDialog = true;

        initThirdParty(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // 获取ApplicationContext
    public static MainApplication getApplication() {
        return application;
    }

    public static SecuritySDK getSecuritySDK() {
        return securitySDK;
    }

    //第三方初始化
    public void initThirdParty(final Application app) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程的优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //网络配置
                NetworkConfig.C.checkInit(app);

                //Arouter
                if (BuildConfig.DEBUG) {
                    ARouter.openLog();     // 打印日志
                    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                }

                ARouter.init(app);

                //bugly
                CrashReport.initCrashReport(getApplicationContext(), "109d2328da", false);
                //日志组件
                Logger.addLogAdapter(new AndroidLogAdapter());

                //配置全局异常崩溃操作
                CaocConfig.Builder.create()
                        .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                        .enabled(true) //是否启动全局异常捕获
                        .showErrorDetails(true) //是否显示错误详细信息
                        .showRestartButton(true) //是否显示重启按钮
                        .trackActivities(true) //是否跟踪Activity
                        .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                        .errorDrawable(R.mipmap.ic_launcher) //错误图标
                        .restartActivity(SplashActivity.class) //重新启动后的activity
                        //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                        //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                        .apply();

            }
        }).start();

    }
}
