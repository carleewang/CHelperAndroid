package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

import java.io.Serializable;

/**
 * 定位数据
 * Created by Administrator on 2017/12/6.
 */

public class LocationEntity   implements Serializable {

    String lo;//经度
    String la;//纬度
    String we;//天气
    String t;//温度
    String wd;//风向


    public LocationEntity(String lo, String la, String we, String t, String wd) {
        this.lo = lo;
        this.la = la;
        this.we = we;
        this.t = t;
        this.wd = wd;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }

    public String getLa() {
        return la;
    }

    public void setLa(String la) {
        this.la = la;
    }

    public String getWe() {
        return we;
    }

    public void setWe(String we) {
        this.we = we;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }
}
