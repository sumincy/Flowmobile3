package com.huateng.ebank.ui.base;

import android.app.Application;
import android.support.annotation.NonNull;


import com.huateng.ebank.app.Perference;
import com.huateng.ebank.entity.api.RespBase;
import com.huateng.ebank.network.ApiConstants;
import com.huateng.ebank.network.HttpMethod;
import com.huateng.ebank.network.RequestCallback;
import com.huateng.ebank.network.utils.RetrofitClient;
import com.huateng.ebank.utils.CommonUtils;
import com.huateng.ebank.utils.GsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import okhttp3.ResponseBody;

/**
 * Created by shanyong on 2019/4/9.
 * 网络请求封装
 */
public class BaseNetworkViewModel extends BaseViewModel {
    public BaseNetworkViewModel(@NonNull Application application) {
        super(application);
    }


    public <T extends RespBase> void requestNetWork(String api, Map paramMap, RequestCallback<T> requestCallback) {
        Observable<ResponseBody> observable = RetrofitClient.getInstance()
                .request(getLifecycleProvider(), HttpMethod.POST, api, Perference.get(Perference.TOKEN), paramMap);

        responseResolve(observable, requestCallback);
    }

    public <T extends RespBase> void requestGet(String api, Map paramMap, RequestCallback<T> requestCallback) {
        Observable<ResponseBody> observable = RetrofitClient.getInstance()
                .request(getLifecycleProvider(), HttpMethod.GET, api, Perference.get(Perference.TOKEN), paramMap);

        responseResolve(observable, requestCallback);
    }

    //返回数据处理
    public <T extends RespBase> void responseResolve(Observable<ResponseBody> observable, final RequestCallback<T> requestCallback) {
        observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                requestCallback.beforeRequest();
            }
        }).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody resp) throws Exception {
                String res = resp.string();
                if (!CommonUtils.isJson(res)) {
                    requestCallback.requestError("1001", res);
                    return;
                }

                Type type = ((ParameterizedType) requestCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                T data = GsonUtils.fromJson(res, type);


                String respCode = data.getResponseCode();
                if (ApiConstants.RETURN_STATUS_SUC.equals(respCode)) {
                    requestCallback.response(data);
                } else if (ApiConstants.RETURN_STATUS_EOR.equals(data.getResponseCode())) {
                    requestCallback.requestError(respCode, data.getResponseMsg());
                } else if (ApiConstants.RETURN_STATUS_OUTDATE.equals(respCode)) {
                    //超时退出
//                    MainApplication application = MainApplication.getApplication();
//                    application.outdate(data.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                if (throwable instanceof ResponseThrowable) {
                    ResponseThrowable responseThrowable = (ResponseThrowable) throwable;
                    requestCallback.requestError(String.valueOf(responseThrowable.code), responseThrowable.message);
                } else {
                    requestCallback.requestError("0", throwable.getMessage());
                }

            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                requestCallback.requestComplete();
            }
        });
    }

}
