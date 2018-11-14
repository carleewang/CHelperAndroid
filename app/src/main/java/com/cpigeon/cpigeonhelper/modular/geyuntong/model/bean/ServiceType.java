package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 当前服务类型（鸽运通，训鸽通）
 * Created by Administrator on 2017/11/27.
 */

public class ServiceType extends RealmObject {

    @PrimaryKey
    private int uid;//未使用
    String serviceType;// geyuntong xungetong


    public ServiceType() {

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public ServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
