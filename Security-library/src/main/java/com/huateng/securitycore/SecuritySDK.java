package com.huateng.securitycore;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.huateng.securitycore.antihijack.AntiHijack;
import com.huateng.securitycore.runtime.CheckHook;
import com.huateng.securitycore.runtime.DetectRuntimeUtil;
import com.huateng.securitycore.runtime.common.CheckedProperty;

/**
 * Created by shanyong on 2019/7/23.
 */

public class SecuritySDK {
    private static SecuritySDK sdkInit = null;
    private Context context = null;

    private SecuritySDK(Context context) {
        this.context = context;
    }

    public static synchronized SecuritySDK getInstance(Context context) {
        if (sdkInit == null)
            sdkInit = new SecuritySDK(context);
        return sdkInit;
    }

    public void initAntiHijack() {
        //安全相关处理
        AntiHijack.init();
    }

    public void cancelAntiHijack() {
        //安全相关处理
        AntiHijack.cancel();
    }

    public CheckedProperty checkRuntimeEnvironment() {
        CheckedProperty property = new CheckedProperty();

        String checkMsg = null;
        boolean isLegal = true;
        if (DetectRuntimeUtil.isRootAvailable()) {
            checkMsg = "应用无法在root设备上运行";
            isLegal = false;
        }

        if (DetectRuntimeUtil.isEmulator(context)) {
            isLegal = false;
            checkMsg = "应用无法在模拟器上运行";
        }

        if (CheckHook.isHook(context)) {
            isLegal = false;
            checkMsg = "应用存在被HOOK风险";
        }

        if (DetectRuntimeUtil.isDebug()) {
            isLegal = false;
            checkMsg = "处于调试模式，应用存在被调试的风险";
        }

        if (DetectRuntimeUtil.isWifiProxy(context)) {
            isLegal = false;
            checkMsg = "检查到设置了网络代理，存在数据被抓包风险";
        }

        property.setLegal(isLegal);
        property.setCheckMsg(checkMsg);

        return property;
    }
}
