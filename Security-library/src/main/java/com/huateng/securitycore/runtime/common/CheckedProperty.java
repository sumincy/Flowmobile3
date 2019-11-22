package com.huateng.securitycore.runtime.common;

/**
 * Created by shanyong on 2019/7/24.
 */

public class CheckedProperty {
    private boolean isLegal;
    private String checkMsg;

    public boolean isLegal() {
        return isLegal;
    }

    public void setLegal(boolean legal) {
        isLegal = legal;
    }

    public String getCheckMsg() {
        return checkMsg;
    }

    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg;
    }
}
