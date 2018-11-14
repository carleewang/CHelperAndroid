package com.cpigeon.cpigeonhelper.modular.flyarea.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 选择地点页面列表展示 tiem 实体类
 * Created by Administrator on 2017/9/26.
 */

public class SelectPlaceEntity implements Parcelable {

    private String location;//地点
    private double longitude;//经度
    private double latitude;//纬度
    private String snippet;//
    private int tag = 1;//标志（适配器区分item 1 未选中item，2，选中item）


    public SelectPlaceEntity() {
    }

    protected SelectPlaceEntity(Parcel in) {
        location = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        snippet = in.readString();
        tag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(snippet);
        dest.writeInt(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelectPlaceEntity> CREATOR = new Creator<SelectPlaceEntity>() {
        @Override
        public SelectPlaceEntity createFromParcel(Parcel in) {
            return new SelectPlaceEntity(in);
        }

        @Override
        public SelectPlaceEntity[] newArray(int size) {
            return new SelectPlaceEntity[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }


}
