package com.huateng.ebank.entity.orm;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.query.Query;

import com.huateng.ebank.db.DbUtil;
import com.huateng.ebank.db.UserHelper;
import com.huateng.ebank.entity.dao.DaoSession;
import com.huateng.ebank.entity.dao.UserDao;


/**
 * Created by shanyong on 2019/11/4.
 * 用户数据
 */
@Entity(active = true, nameInDb = "User")
public class User {

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String tel;
    private String name;
    private String idCardNum;
    private String birthday;
    private String gender;
    private String pwd;
    private String address;
    //0 未开户  1 已开户
    private String hasAccount;
    private String acctNo;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;


    @Generated(hash = 1646735358)
    public User(Long id, String tel, String name, String idCardNum, String birthday,
                String gender, String pwd, String address, String hasAccount, String acctNo) {
        this.id = id;
        this.tel = tel;
        this.name = name;
        this.idCardNum = idCardNum;
        this.birthday = birthday;
        this.gender = gender;
        this.pwd = pwd;
        this.address = address;
        this.hasAccount = hasAccount;
        this.acctNo = acctNo;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String hasAccount() {
        return hasAccount;
    }

    public void setHasAccount(String hasAccount) {
        this.hasAccount = hasAccount;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }


    public static User findUser(String tel) {
        UserHelper helper = DbUtil.getUserHelper();
        Query<User> query = helper.queryBuilder()
                .where(UserDao.Properties.Tel.eq(tel))
                .build();

        return query.unique();
    }

    //保存用户
    public static void save(User user) {
        UserHelper helper = DbUtil.getUserHelper();
        helper.save(user);
    }

    public String getHasAccount() {
        return this.hasAccount;
    }
}
