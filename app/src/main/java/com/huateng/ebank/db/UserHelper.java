package com.huateng.ebank.db;


import com.huateng.ebank.entity.orm.User;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by shanyong on 2019/7/26.
 */

public class UserHelper extends BaseDbHelper<User, Long> {
    public UserHelper(AbstractDao dao) {
        super(dao);
    }
}
