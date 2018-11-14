package com.cpigeon.cpigeonhelper.modular.flyarea.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 司放地的bean
 * Created by Administrator on 2017/6/14.
 */

public class FlyingAreaEntity implements Parcelable {


    /**
     * longitude : 116.195665
     * faid : 3
     * area :  青华大学
     * latitude : 41.00346
     * alias : qqq
     */

    private double longitude;//经度
    private int faid;//序号（司放地坐标）
    private String area;//区域(司放地)
    private double latitude;//纬度
    private String alias;//别名（详细地址）
    private int number;//(不清楚，未知)

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFaid() {
        return faid;
    }

    public void setFaid(int faid) {
        this.faid = faid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public FlyingAreaEntity() {

    }

    protected FlyingAreaEntity(Parcel in) {
        longitude = in.readDouble();
        faid = in.readInt();
        area = in.readString();
        latitude = in.readDouble();
        alias = in.readString();
        number = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeInt(faid);
        dest.writeString(area);
        dest.writeDouble(latitude);
        dest.writeString(alias);
        dest.writeInt(number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlyingAreaEntity> CREATOR = new Creator<FlyingAreaEntity>() {
        @Override
        public FlyingAreaEntity createFromParcel(Parcel in) {
            return new FlyingAreaEntity(in);
        }

        @Override
        public FlyingAreaEntity[] newArray(int size) {
            return new FlyingAreaEntity[size];
        }
    };
}
