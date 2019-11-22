package com.huateng.ebank.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;


import com.huateng.ebank.app.Constants;
import com.huateng.ebank.app.Perference;
import com.huateng.ebank.entity.event.LoginEvent;
import com.huateng.ebank.network.ApiConstants;
import com.huateng.ebank.network.RequestCallback;
import com.huateng.ebank.network.utils.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.bus.RxBus;


/**
 * Created by sumincy on 2018/9/30.
 * token 登录 service
 */

public class TokenLoginService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TokenLoginService() {
        super("token login");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("roleFlag", "10");
//        paramMap.put("registrationid", JPushInterface.getRegistrationID(MainApplication.getApplication()));

        RetrofitClient.getInstance().requestNetWork(ApiConstants.API_TOKEN_LOGIN, paramMap, new RequestCallback<RespTokenUser>() {

            @Override
            public void response(RespTokenUser resp) {

                RespTokenUser.DataBean.UserInfoBean userInfo = resp.getData().getUserInfo();

                Perference.set(Perference.USER_ID, userInfo.getId());

                //上次登录时间
                Perference.set(Perference.LAST_LOGIN_TIME, userInfo.getLastLoginTime());
                //保存客户经理号
                Perference.set(Perference.USER_ID, userInfo.getId());
                Perference.set(Perference.USER_NAME, userInfo.getUserName());
                Perference.set(Perference.ROLE_TYPE, userInfo.getRoleFlag());
                Perference.set(Perference.TELEPHONE, userInfo.getPhoneNo());

                RxBus.getDefault().post(new LoginEvent(Constants.AUTH_SUC));
                Perference.setBoolean(Constants.TOKEN_AUTH, true);

            }

            @Override
            public void requestError(String code, String msg) {
                super.requestError(code, msg);

                //登录失败
                if (ApiConstants.RETURN_STATUS_EOR.equals(code) || ApiConstants.RETURN_STATUS_OUTDATE.equals(code)) {
                    //登录失败统一算作 登录超时
                    Perference.setBoolean(Constants.TOKEN_AUTH, false);
                    //登录失败清除token
                    Perference.set(Perference.TOKEN, null);

                    LoginEvent loginEvent = new LoginEvent(Constants.AUTH_FAIL, msg);
                    RxBus.getDefault().post(loginEvent);
                } else {
                    RxBus.getDefault().post(new LoginEvent(Constants.AUTH_TIME_OUT));
                }
            }
        });

    }


    //启动
    public static void start(Context context) {
        Intent intent = new Intent(context, TokenLoginService.class);
        context.startService(intent);
    }

}
