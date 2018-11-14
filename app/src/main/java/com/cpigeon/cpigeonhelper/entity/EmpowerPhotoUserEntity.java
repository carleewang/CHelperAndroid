package com.cpigeon.cpigeonhelper.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class EmpowerPhotoUserEntity implements Parcelable {
    public String touxiang; //头像地址
    public String username;//用户名
    public String uid;//会员ID
    public String authuid;//被授权ID
    public String shouji;//被授权用户手机号码


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.touxiang);
        dest.writeString(this.username);
        dest.writeString(this.uid);
        dest.writeString(this.authuid);
        dest.writeString(this.shouji);
    }

    public EmpowerPhotoUserEntity() {
    }

    protected EmpowerPhotoUserEntity(Parcel in) {
        this.touxiang = in.readString();
        this.username = in.readString();
        this.uid = in.readString();
        this.authuid = in.readString();
        this.shouji = in.readString();
    }

    public static final Parcelable.Creator<EmpowerPhotoUserEntity> CREATOR = new Parcelable.Creator<EmpowerPhotoUserEntity>() {
        @Override
        public EmpowerPhotoUserEntity createFromParcel(Parcel source) {
            return new EmpowerPhotoUserEntity(source);
        }

        @Override
        public EmpowerPhotoUserEntity[] newArray(int size) {
            return new EmpowerPhotoUserEntity[size];
        }
    };
}
