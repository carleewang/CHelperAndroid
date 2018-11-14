package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/9/23.
 */

public class GeYunTongs extends RealmObject implements Parcelable {


    //StateCode【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
    private int id = -1;//比赛id
    private String raceName;//竞赛名称
    private int stateCode = -1;//状态码
    private String mEndTime;//结束时间
    private String mTime;//监控启动时间
    private double longitude = 0;//经度
    private double latitude = 0;//纬度
    private String state;//监控状态
    private String flyingArea;//飞行区域
    private int stateSq;//授权状态


    public GeYunTongs() {
    }

    protected GeYunTongs(Parcel in) {
        id = in.readInt();
        raceName = in.readString();
        stateCode = in.readInt();
        mEndTime = in.readString();
        mTime = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        state = in.readString();
        flyingArea = in.readString();
        stateSq = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(raceName);
        dest.writeInt(stateCode);
        dest.writeString(mEndTime);
        dest.writeString(mTime);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(state);
        dest.writeString(flyingArea);
        dest.writeInt(stateSq);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeYunTongs> CREATOR = new Creator<GeYunTongs>() {
        @Override
        public GeYunTongs createFromParcel(Parcel in) {
            return new GeYunTongs(in);
        }

        @Override
        public GeYunTongs[] newArray(int size) {
            return new GeYunTongs[size];
        }
    };

    public String getFlyingArea() {
        return flyingArea;
    }

    public void setFlyingArea(String flyingArea) {
        this.flyingArea = flyingArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public int getStateSq() {
        return stateSq;
    }

    public void setStateSq(int stateSq) {
        this.stateSq = stateSq;
    }

}
