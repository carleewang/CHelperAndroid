package com.cpigeon.cpigeonhelper.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class OrderInfoEntity implements Parcelable {
    public String id;//订单 ID,
    public String time;//订单时间’,
    public String number;//订单编号’,
    public String item;//订单内容”,
    public String price;//所需金额,’
    public String scores;//所需积分”
    public String fpid;//所需积分”



    public OrderInfoEntity() {
    }


    protected OrderInfoEntity(Parcel in) {
        id = in.readString();
        time = in.readString();
        number = in.readString();
        item = in.readString();
        price = in.readString();
        scores = in.readString();
        fpid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(number);
        dest.writeString(item);
        dest.writeString(price);
        dest.writeString(scores);
        dest.writeString(fpid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel in) {
            return new OrderInfoEntity(in);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };
}
