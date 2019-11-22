package com.huateng.ebank.network;

/**
 * ClassName: NewsApi<p>
 * Author:oubowu<p>
 * Fuction: 请求接口<p>
 * CreateDate:2016/2/13 19:10<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class ApiConstants {


    /**
     * RELEASE 发布版API
     * DEVELOP  开发版API
     * CUSTOM  自定义
     */
    public static final String API_MODE_RELEASE = "MODE_RELEASE";
    public static final String API_MODE_DEVELOP = "MODE_DEVELOP";
    public static final String API_MODE_CUSTOM = "MODE_CUSTOM";


    public static final String RETURN_STATUS_SUC = "ACT000200";
    public static final String RETURN_STATUS_EOR = "ACT1004";
    public static final String RETURN_STATUS_OUTDATE = "timeout";


    public static final String RELEASE_BASE_URL = "http://170.252.199.162:18081/bank-account-app/";
//    public static final String PUSH_IP = "xxts.pay.jjccb.com";
//    public static final int PUSH_PORT = 20000;

    //   开发 测试  准生产
    public static final String PUSH_IP = "10.116.4.8";
    //    sit
    public static final int PUSH_PORT = 20016;


    public static String DEVELOP_BASE_URL = "";

    public static final String allocServer = "http://221.4.52.4:8060/alloc/";

    public static String DOMAIN = "221.4.52.4:8060";

    //api
    //开户
    public static final String API_OPEN_ACCOUNT = "openAccount";
    //活期信息查询
    public static final String API_ENQUIRY_CUSTOM = "enquiryCustom";
    //活期信息查询
    public static final String API_ENQUIRY_ACCOUNT = "enquiryAccount";
    //定期信息查询
    public static final String API_ENQUIRY_DEPOSIT = "enquiryDeposit";
    //交易查询
    public static final String API_ENQUIRY_TXN = "enquiryTxn";
    //转入
    public static final String API_TRANSFER_IN = "transferIn";
    //转出
    public static final String API_TRANSFER_OUT = "transferOut";
    //销户
    public static final String API_CLOSE_ACCOUNT = "closeAccount";
    //冻结
    public static final String API_FREEZE_ACCOUNT = "freezeAccount";
    //活期转定期
    public static final String API_TIME_DEPOSIT = "timeDeposit";
    //定期转活期
    public static final String API_SAVING_ACCOUNT = "SavingAccount";
    //定期转定期
    public static final String API_REPEATING = "repeating";
    //密码修改
    public static final String API_CHANGE_PASSWD = "changePasswd";


    //正常登录
    public static final String API_NORMAL_LOGIN = "auth/login";
    //token 登录
    public static final String API_TOKEN_LOGIN = "token/login";
    //登出
    public static final String API_LOGOUT = "users/logout";
    //短信验证码
    public static final String URI_SMS = "sms";


}
