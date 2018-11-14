package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;



import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Zhu TingYu on 2018/2/27.
 */

public class OfflineFileEntity extends RealmObject implements Parcelable {

    public OfflineFileEntity(){};

    public OfflineFileEntity(String rid
            , String ft
            , String tagid
            , String lo
            , String la
            , String we
            , String t
            , String wp
            , String wd
            , String path
            , String time
            , String userId
    ) {
        this.rid = rid;
        this.ft = ft;
        this.tagid = tagid;
        this.lo = lo;
        this.la = la;
        this.we = we;
        this.t = t;
        this.wp = wp;
        this.wd = wd;
        this.path = path;
        this.time = time;
        this.userId = userId;
    }

    private String userId;//用户id
    private String rid;//鸽运通赛事 ID
    private String ft;//文件类型【image video】
    private String tagid;//标签 ID
    private String lo;//经度
    private String la;//维度
    private String we;//天气名称
    private String t;//温度
    private String wp;//风力
    private String wd;//风向
    private String path;//文件

    private String time;

    public String getUserId() {
        return userId;
    }

    public String getRid() {
        return rid;
    }

    public String getFt() {
        return ft;
    }

    public String getTagid() {
        return tagid;
    }

    public String getLo() {
        return lo;
    }

    public String getLa() {
        return la;
    }

    public String getWe() {
        return we;
    }

    public String getT() {
        return t;
    }

    public String getWp() {
        return wp;
    }

    public String getWd() {
        return wd;
    }

    public String getPath() {
        return path;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.rid);
        dest.writeString(this.ft);
        dest.writeString(this.tagid);
        dest.writeString(this.lo);
        dest.writeString(this.la);
        dest.writeString(this.we);
        dest.writeString(this.t);
        dest.writeString(this.wp);
        dest.writeString(this.wd);
        dest.writeString(this.path);
        dest.writeString(this.time);
    }

    protected OfflineFileEntity(Parcel in) {
        this.userId = in.readString();
        this.rid = in.readString();
        this.ft = in.readString();
        this.tagid = in.readString();
        this.lo = in.readString();
        this.la = in.readString();
        this.we = in.readString();
        this.t = in.readString();
        this.wp = in.readString();
        this.wd = in.readString();
        this.path = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<OfflineFileEntity> CREATOR = new Parcelable.Creator<OfflineFileEntity>() {
        @Override
        public OfflineFileEntity createFromParcel(Parcel source) {
            return new OfflineFileEntity(source);
        }

        @Override
        public OfflineFileEntity[] newArray(int size) {
            return new OfflineFileEntity[size];
        }
    };
}
