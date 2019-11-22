
package com.huateng.ebank.db;

import android.content.Context;

import com.huateng.ebank.db.update.MigrationHelper;
import com.huateng.ebank.entity.dao.DaoMaster;
import com.huateng.ebank.entity.dao.DicDao;
import com.huateng.ebank.entity.dao.UserDao;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.database.Database;


public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Logger.w("db version update from " + oldVersion + " to " + newVersion);

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, DicDao.class, UserDao.class);

    }
}
