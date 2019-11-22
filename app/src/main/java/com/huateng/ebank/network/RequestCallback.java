package com.huateng.ebank.network;


import com.huateng.ebank.entity.api.RespBase;

/**
 * Created by shanyong on 2019/4/9.
 */
public abstract class RequestCallback<T extends RespBase> {

    public void beforeRequest() {

    }

    public void requestError(String code, String msg) {
        end();
    }

    public void requestComplete() {
        end();
    }

    public abstract void response(T resp);

    public void end() {

    }
}
