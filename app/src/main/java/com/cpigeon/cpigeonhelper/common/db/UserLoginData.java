package com.cpigeon.cpigeonhelper.common.db;

import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserLoginEntity;

import io.realm.RealmResults;

/**
 * Created by Administrator on 2018/3/5.
 */

public class UserLoginData {

    public static RealmResults<UserLoginEntity> info = RealmUtils.getInstance().queryUserLoginEntity();

    public static UserLoginEntity getUserLoginEntity() {

        for (UserLoginEntity monBean : info) {
            return monBean;
        }
        return null;
    }
}
