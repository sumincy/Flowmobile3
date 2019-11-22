

package com.huateng.ebank.db;


import com.huateng.ebank.entity.dao.DicDao;
import com.huateng.ebank.entity.dao.UserDao;

/**
 * 文 件 名: DbUtil
 * 说   明:  获取表 Helper 的工具类
 * 创 建 人: 蒋朋
 * 创建日期: 16-7-19 10:44
 * 邮   箱: jp19891017@gmail.com
 * 博   客: http://jp1017.github.io
 * 修改时间：
 * 修改备注：
 */
public class DbUtil {
    private static DicHelper sDicHelper;
    private static UserHelper sUserHelper;

    //业务字典
    private static DicDao getDicDao() {
        return DbCore.getDaoSession().getDicDao();
    }

    //用户资料
    private static UserDao getUserDao() {
        return DbCore.getDaoSession().getUserDao();
    }

    public static DicHelper getDicHelper() {
        if (sDicHelper == null) {
            sDicHelper = new DicHelper(getDicDao());
        }
        return sDicHelper;
    }

    public static UserHelper getUserHelper() {
        if (sUserHelper == null) {
            sUserHelper = new UserHelper(getUserDao());
        }
        return sUserHelper;
    }
}
