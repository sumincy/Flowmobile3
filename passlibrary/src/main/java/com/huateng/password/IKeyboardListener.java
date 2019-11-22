package com.huateng.password;

public interface IKeyboardListener {

    void onPasswordChange(String pwd);

    void onPasswordComplete(String pwd);

    void onForgetPwd();

}
