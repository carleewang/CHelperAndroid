package com.cpigeon.cpigeonhelper.modular.guide.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2018/2/5.
 */

public class IsLoginAppBean  extends RealmObject {
    @PrimaryKey
    private int id;
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
