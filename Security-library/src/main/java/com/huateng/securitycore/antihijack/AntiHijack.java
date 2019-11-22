package com.huateng.securitycore.antihijack;


import android.os.Handler;

import com.tools.utils.AppUtils;
import com.tools.utils.Utils;
import com.tools.view.RxToast;


/**
 * 反劫持  在app切入后台的时候 进行提示
 */
public class AntiHijack {

    public final static String ANTI_HIJACK = "AntiHijack";

    public static void init() {
        AppUtils.registerAppStatusChangedListener(ANTI_HIJACK, new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground() {

            }

            @Override
            public void onBackground() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RxToast.showToast(AppUtils.getAppName() + "进入后台运行");
                    }
                }, 500);
            }
        });
    }

    public static void cancel() {
        AppUtils.unregisterAppStatusChangedListener(ANTI_HIJACK);
    }

}
