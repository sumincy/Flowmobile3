package com.huateng.ebank.entity.event;

/**
 * Created by shanyong on 2019/4/18.
 */

public class LoginEvent {
    public String status;
    public String msg;

    public LoginEvent(String status) {
        this.status = status;
    }

    public LoginEvent(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
