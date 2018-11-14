package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

/**
 * 训鸽通升级续费实体类
 * Created by Administrator on 2017/12/8.
 */

public class XGTOpenAndRenewEntity {

    /**
     * scores : 0
     * packageName : 训鸽通用户
     * unitname : 年
     * servicetimes : 1
     * expiretime : 2999-12-31 00:00:00
     * id : 34
     * flag : 0
     * name : 训鸽通开通
     * originalPrice : 0
     * opentime : 2017-11-17 00:00:00
     * brief : 训鸽通用户，仅需380元一年（或5000鸽币兑换）
     * price : 380.0
     * detial :
     */

    private int scores;
    private String packageName;
    private String unitname;
    private int servicetimes;
    private String expiretime;
    private int id;
    private int flag;
    private String name;
    private int originalPrice;
    private String opentime;
    private String brief;
    private double price;
    private String detial;

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public int getServicetimes() {
        return servicetimes;
    }

    public void setServicetimes(int servicetimes) {
        this.servicetimes = servicetimes;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDetial() {
        return detial;
    }

    public void setDetial(String detial) {
        this.detial = detial;
    }
}
