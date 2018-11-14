package com.cpigeon.cpigeonhelper.modular.order.model.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 套餐服务bean
 * <p>
 * Created by Administrator on 2017/5/26.
 */

public class PackageInfo implements Parcelable {


    /**
     * scores : 0
     * packageName : 普通用户
     * unitname : 年
     * servicetimes : 1
     * expiretime : 2999-12-31 00:00:00
     * id : 10
     * flag : 1
     * name : 鸽运通开通
     * originalPrice : 0
     * opentime : 2017-05-25 12:01:05
     * brief : 鸽运通开通
     * price : 580
     * detial :
     */

    private String scores;
    private String packageName;//套餐等级
    private String unitname;
    private String servicetimes;
    private String expiretime;
    private int id;
    private String flag;
    private String name;
    private String originalPrice;
    private String opentime;
    private String brief;
    private String price;//鸽币
    private String detial;


    protected PackageInfo(Parcel in) {
        scores = in.readString();
        packageName = in.readString();
        unitname = in.readString();
        servicetimes = in.readString();
        expiretime = in.readString();
        id = in.readInt();
        flag = in.readString();
        name = in.readString();
        originalPrice = in.readString();
        opentime = in.readString();
        brief = in.readString();
        price = in.readString();
        detial = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scores);
        dest.writeString(packageName);
        dest.writeString(unitname);
        dest.writeString(servicetimes);
        dest.writeString(expiretime);
        dest.writeInt(id);
        dest.writeString(flag);
        dest.writeString(name);
        dest.writeString(originalPrice);
        dest.writeString(opentime);
        dest.writeString(brief);
        dest.writeString(price);
        dest.writeString(detial);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageInfo> CREATOR = new Creator<PackageInfo>() {
        @Override
        public PackageInfo createFromParcel(Parcel in) {
            return new PackageInfo(in);
        }

        @Override
        public PackageInfo[] newArray(int size) {
            return new PackageInfo[size];
        }
    };

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
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

    public String getServicetimes() {
        return servicetimes;
    }

    public void setServicetimes(String servicetimes) {
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetial() {
        return detial;
    }

    public void setDetial(String detial) {
        this.detial = detial;
    }
}
