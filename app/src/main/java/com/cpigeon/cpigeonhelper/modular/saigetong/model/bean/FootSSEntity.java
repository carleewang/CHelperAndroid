package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.cpigeon.cpigeonhelper.utils.http.GsonUtil;

/**
 * Created by Administrator on 2017/12/4.
 */

public class FootSSEntity implements Parcelable {
    /**
     * id : 422005
     * color : 雨点
     * sex :
     * address : 成都市青羊区清江东路134号中开大厦16楼28号
     * foot : 2017-22-0094193
     * tel : 18380449953
     * eye :
     */

    private int id;//
    private String color;//羽色
    private String sex;//雌雄
    private String address;//地址
    private String foot;//足环
    private String tel;//电话
    private String eye;//眼睛
    private String sjhm;
    private String xingming;
    private String cskh;
    private String gpmc;//公棚名称


    public FootSSEntity(int id, String color, String sex, String address, String foot, String tel, String eye, String sjhm, String xingming, String cskh, String gpmc) {

        this.id = id;
        this.color = color;
        this.sex = sex;
        this.address = address;
        this.foot = foot;
        this.tel = tel;
        this.eye = eye;
        this.sjhm = sjhm;
        this.xingming = xingming;
        this.cskh = cskh;
        this.gpmc = gpmc;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getCskh() {
        return cskh;
    }

    public void setCskh(String cskh) {
        this.cskh = cskh;
    }

    public String getGpmc() {
        return gpmc;
    }

    public void setGpmc(String gpmc) {
        this.gpmc = gpmc;
    }


    public String toJsonString() {
        return GsonUtil.toJson(this);
    }


    //
//    /**
//     * sjhm : 18008073708
//     * xingming : 尹子叶
//     * foot : 2015-22-0950491
//     * id : 116415
//     * cskh : 0228
//     */
//
//    private String sjhm;
//    private String xingming;
//    private String foot;
//    private String id;
//    private String cskh;
//
//    protected FootSSEntity(Parcel in) {
//        sjhm = in.readString();
//        xingming = in.readString();
//        foot = in.readString();
//        id = in.readString();
//        cskh = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(sjhm);
//        dest.writeString(xingming);
//        dest.writeString(foot);
//        dest.writeString(id);
//        dest.writeString(cskh);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<FootSSEntity> CREATOR = new Creator<FootSSEntity>() {
//        @Override
//        public FootSSEntity createFromParcel(Parcel in) {
//            return new FootSSEntity(in);
//        }
//
//        @Override
//        public FootSSEntity[] newArray(int size) {
//            return new FootSSEntity[size];
//        }
//    };
//
//    public String getSjhm() {
//        return sjhm;
//    }
//
//    public void setSjhm(String sjhm) {
//        this.sjhm = sjhm;
//    }
//
//    public String getXingming() {
//        return xingming;
//    }
//
//    public void setXingming(String xingming) {
//        this.xingming = xingming;
//    }
//
//    public String getFoot() {
//        return foot;
//    }
//
//    public void setFoot(String foot) {
//        this.foot = foot;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getCskh() {
//        return cskh;
//    }
//
//    public void setCskh(String cskh) {
//        this.cskh = cskh;
//    }

    protected FootSSEntity(Parcel in) {
        id = in.readInt();
        color = in.readString();
        sex = in.readString();
        address = in.readString();
        foot = in.readString();
        tel = in.readString();
        eye = in.readString();
        sjhm = in.readString();
        xingming = in.readString();
        cskh = in.readString();
        gpmc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(color);
        dest.writeString(sex);
        dest.writeString(address);
        dest.writeString(foot);
        dest.writeString(tel);
        dest.writeString(eye);
        dest.writeString(sjhm);
        dest.writeString(xingming);
        dest.writeString(cskh);
        dest.writeString(gpmc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FootSSEntity> CREATOR = new Creator<FootSSEntity>() {
        @Override
        public FootSSEntity createFromParcel(Parcel in) {
            return new FootSSEntity(in);
        }

        @Override
        public FootSSEntity[] newArray(int size) {
            return new FootSSEntity[size];
        }
    };
}
