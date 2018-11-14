package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

/**
 * 获取鸽运通位置信息空距里程
 * Created by Administrator on 2018/2/27.
 */

public class KjLcEntity {
    private String kongju;//double类型，空距（单位：KM）
    private String licheng;//int类型，里程（单位：米）
    private String sfd;//字符串类型，司放地
    private String sfdjd;//字符串类型，司放地经度
    private String sfdwd;//字符串类型，司放地纬度
    private String jksc;//long类型，监控时长（单位：秒，转换为：n天n小时）
    private String tianqi;//String类型，司放地天气


    public String getKongju() {
        return kongju;
    }

    public void setKongju(String kongju) {
        this.kongju = kongju;
    }

    public String getLicheng() {
        return licheng;
    }

    public void setLicheng(String licheng) {
        this.licheng = licheng;
    }

    public String getSfd() {
        return sfd;
    }

    public void setSfd(String sfd) {
        this.sfd = sfd;
    }

    public String getSfdjd() {
        return sfdjd;
    }

    public void setSfdjd(String sfdjd) {
        this.sfdjd = sfdjd;
    }

    public String getSfdwd() {
        return sfdwd;
    }

    public void setSfdwd(String sfdwd) {
        this.sfdwd = sfdwd;
    }

    public String getJksc() {
        return jksc;
    }

    public void setJksc(String jksc) {
        this.jksc = jksc;
    }


    public String getTianqi() {
        return tianqi;
    }

    public void setTianqi(String tianqi) {
        this.tianqi = tianqi;
    }
}
