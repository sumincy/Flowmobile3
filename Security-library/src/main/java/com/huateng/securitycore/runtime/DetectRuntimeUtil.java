package com.huateng.securitycore.runtime;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;


import eu.chainfire.libsuperuser.Shell;

/**
 * Created by shanyong on 2019/7/23.
 * 检查运行环境
 */

public class DetectRuntimeUtil {

    public static boolean isRootAvailable() {
        return Shell.SU.available();
    }

    public static boolean isEmulator(Context context) {
        if (FindEmulator.hasKnownDeviceId(context)
                || FindEmulator.hasKnownImsi(context)
                || FindEmulator.hasEmulatorBuild(context)
                || FindEmulator.hasKnownPhoneNumber(context) || FindEmulator.hasPipes()
                || FindEmulator.hasQEmuDrivers() || FindEmulator.hasEmulatorAdb()
                || FindEmulator.hasQEmuFiles()
                || FindEmulator.hasGenyFiles()) {
            Logger.w("QEmu environment detected.");
            return true;
        } else {
            Logger.w("QEmu environment not detected.");
            return false;
        }
    }

    public static boolean isDebug() {
        boolean tracer = false;
        try {
            tracer = FindDebugger.hasTracerPid();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (FindDebugger.isBeingDebugged() || tracer) {
            Logger.w("Debugger was detected");
            return true;
        } else {
            Logger.w("No debugger was detected.");
            return false;
        }
    }

    //检查是否代理
    public static boolean isWifiProxy(Context context) {
        CharSequence proxyHost;
        int port;
        if (Build.VERSION.SDK_INT >= 14) {
            proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");
            if (proxyPort == null) {
                proxyPort = "-1";
            }
            port = Integer.parseInt(proxyPort);
        } else {
            proxyHost = Proxy.getHost(context);
            port = Proxy.getPort(context);
        }

        if (TextUtils.isEmpty(proxyHost) || port == -1) {
            return false;
        }
        return true;
    }

}
