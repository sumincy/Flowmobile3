package com.huateng.ebank.ui.setting.vm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;


import com.huateng.ebank.app.Perference;
import com.huateng.ebank.entity.api.RespBase;
import com.huateng.ebank.network.ApiConstants;
import com.huateng.ebank.network.RequestCallback;
import com.huateng.ebank.network.utils.RetrofitClient;
import com.huateng.ebank.service.daemon.PushService;
import com.huateng.ebank.ui.auth.ActivityLogin;
import com.huateng.ebank.ui.base.BaseNetworkViewModel;
import com.tools.utils.AppUtils;
import com.tools.utils.RxActivityUtils;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * Created by shanyong on 2019/3/26.
 */

public class SettingViewModel extends BaseNetworkViewModel {

    public SettingViewModel(@NonNull Application application) {
        super(application);
    }

    //选择
    public ObservableInt switchButtonVisibility = new ObservableInt();

    //退出登录
    public SingleLiveEvent<Void> logoutEvent = new SingleLiveEvent<>();
//    public SingleLiveEvent<Void> finishAllEvent = new SingleLiveEvent<>();

    public BindingCommand logoutClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            logoutEvent.call();
        }
    });

    //退出登录
    public void requestLogout() {
        final Context context = AppUtils.getTopActivity();
        //退出登录
        Map<String, String> map = new HashMap<>();
        requestGet(ApiConstants.API_LOGOUT, map, new RequestCallback<RespBase>() {
            @Override
            public void beforeRequest() {
                super.beforeRequest();
                showDialog();
            }

            @Override
            public void response(RespBase resp) {

            }

            @Override
            public void end() {
                super.end();
               dismissDialog();

                RetrofitClient.clear();
                Perference.clear();

                if (null != context) {
                    RxActivityUtils.skipActivityAndFinishAll(context, ActivityLogin.class);
                } else {
                    RxActivityUtils.restartApp(getApplication());
                }
            }
        });
    }
}
