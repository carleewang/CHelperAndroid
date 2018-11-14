package com.cpigeon.cpigeonhelper.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class UserGXTEntity implements Parcelable {
    public int tyxy; //是否已同意使用协议
    public int syts = 0; //短信剩余条数

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tyxy);
        dest.writeInt(this.syts);
    }

    public UserGXTEntity() {
    }

    protected UserGXTEntity(Parcel in) {
        this.tyxy = in.readInt();
        this.syts = in.readInt();
    }

    public static final Parcelable.Creator<UserGXTEntity> CREATOR = new Parcelable.Creator<UserGXTEntity>() {
        @Override
        public UserGXTEntity createFromParcel(Parcel source) {
            return new UserGXTEntity(source);
        }

        @Override
        public UserGXTEntity[] newArray(int size) {
            return new UserGXTEntity[size];
        }
    };
}
