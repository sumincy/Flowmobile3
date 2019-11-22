package com.huateng.ebank.db;


import com.huateng.ebank.entity.orm.Dic;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by shanyong on 2019/7/26.
 */

public class DicHelper extends BaseDbHelper<Dic, Long> {
    public DicHelper(AbstractDao dao) {
        super(dao);
    }
}
