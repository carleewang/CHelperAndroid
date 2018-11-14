package com.cpigeon.cpigeonhelper.modular.usercenter.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2018/3/5.
 */

public class UserLoginEntity extends RealmObject {
    @PrimaryKey
    private int id;
    private String userName; //用户名
    private String userPasword;//用户密码

    public UserLoginEntity(){

    }

    public UserLoginEntity(String userName, String userPasword) {
        this.userName = userName;
        this.userPasword = userPasword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPasword() {
        return userPasword;
    }

    public void setUserPasword(String userPasword) {
        this.userPasword = userPasword;
    }
}
