package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2018/4/7.
 */

public class MyLocation extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;//
    private String msg;//上传坐标数  数据格式为:纬度|经度|速度|定位时间戳|天气|风向|风力|天气时间|温度据
    private int monitorId;


    public MyLocation() {
    }

    public MyLocation(int id, String msg, int monitorId) {
        this.id = id;
        this.msg = msg;
        this.monitorId = monitorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyLocation(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(int monitorId) {
        this.monitorId = monitorId;
    }
}