package com.huateng.ebank.entity.orm;

import com.huateng.ebank.app.Constants;
import com.huateng.ebank.db.DbUtil;
import com.huateng.ebank.db.DicHelper;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.huateng.ebank.entity.dao.DaoSession;
import com.huateng.ebank.entity.dao.DicDao;

/**
 * Created by shanyong on 2019/9/6.
 */
@Entity(active = true, nameInDb = "Dic")
public class Dic implements Serializable {

    //证件类型
    public static final String ID_CARD_TYPE = "artifCardType";
    //性别
    public static final String GENDER = "gender";


    private static final long serialVersionUID = -1798643301673970731L;

    @Id(autoincrement = true)
    public Long id;

    private String key;
    private String value;
    private String type;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1014288398)
    private transient DicDao myDao;

    @Generated(hash = 1934157947)
    public Dic(Long id, String key, String value, String type) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.type = type;
    }

    @Generated(hash = 114904903)
    public Dic() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1629093556)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDicDao() : null;
    }

    public static List<Dic> dics(String type) {

        DicHelper helper = DbUtil.getDicHelper();
        Query<Dic> query = helper.queryBuilder()
                .where(DicDao.Properties.Type.eq(type))
                .build();

        return query.list();
    }

    //    从数据库中取
    public static List<String> getOptions(String type) {
        List<Dic> dics = dics(type);
        List<String> options = new ArrayList<>();
        //给下拉填充数据中增加默认选项
        options.add(Constants.DEFAULT_DIC);

        for (Dic dic : dics) {
            options.add(dic.getValue());
        }
        return options;
    }

    //不存数据库
    public static List<String> getOptions(List<Dic> dics) {
        List<String> options = new ArrayList<>();
        //给下拉填充数据中增加默认选项
        options.add(Constants.DEFAULT_DIC);

        for (Dic dic : dics) {
            options.add(dic.getValue());
        }
        return options;
    }

    public static String queryValue(String type, String key) {
        List<Dic> dics = dics(type);
        for (Dic dic : dics) {
            if (dic.getKey().equals(key)) {
                return dic.getValue();
            }
        }
        return null;
    }

    public static String queryKey(String type, String value) {
        List<Dic> dics = dics(type);
        for (Dic dic : dics) {
            if (dic.getValue().equals(value)) {
                return dic.getKey();
            }
        }
        return null;
    }

    //不存数据库
    public static String queryKey(List<Dic> dics, String value) {
        if (null != dics && dics.size() > 0) {
            for (Dic dic : dics) {
                if (dic.getValue().equals(value)) {
                    return dic.getKey();
                }
            }
        }
        return null;
    }

    //根据类型 删除对应数据
    public static void clear(String type) {
        DicHelper helper = DbUtil.getDicHelper();
        Query<Dic> query = helper.queryBuilder()
                .where(DicDao.Properties.Type.eq(type))
                .build();
        helper.delete(query.list());
    }

    public static void save(List<Dic> dics) {
        DicHelper helper = DbUtil.getDicHelper();
        helper.save(dics);
    }

    public static Dic objectToDic(String key, String value, String type) {
        Dic dic = new Dic();
        dic.setKey(key);
        dic.setType(type);
        dic.setValue(value);
        return dic;
    }

}
