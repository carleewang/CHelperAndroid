package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/3/15.
 */

public class SearchFootEntity implements Parcelable {

    /**
     * ttzb :
     * diqu : 成都
     * ispic : 0
     * id : 547558
     * ring : FFA70002B6C7DEE2
     * sjhm : 13730873310
     * scsj : 2018/1/23 17:14:01
     * sex :
     * eye :
     * foot : 2018-22-0011112
     * color : 雨白条
     * xingming : 中鸽科技
     * cskh : 00001
     * rpsj : 2018/1/23 16:34:16
     */

    private String ttzb;
    private String diqu;
    private int ispic;
    private int id;
    private String ring;
    private String sjhm;
    private String scsj;
    private String sex;
    private String eye;
    private String foot;
    private String color;
    private String xingming;
    private String cskh;
    private String rpsj;
    private String gpmc;//公棚名称
    private String address;//地址
    private String tel;//电话

    public String getTtzb() {
        return ttzb;
    }

    public void setTtzb(String ttzb) {
        this.ttzb = ttzb;
    }

    public String getDiqu() {
        return diqu;
    }

    public void setDiqu(String diqu) {
        this.diqu = diqu;
    }

    public int getIspic() {
        return ispic;
    }

    public void setIspic(int ispic) {
        this.ispic = ispic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getScsj() {
        return scsj;
    }

    public void setScsj(String scsj) {
        this.scsj = scsj;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getRpsj() {
        return rpsj;
    }

    public void setRpsj(String rpsj) {
        this.rpsj = rpsj;
    }


    public String getGpmc() {
        return gpmc;
    }

    public void setGpmc(String gpmc) {
        this.gpmc = gpmc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ttzb);
        dest.writeString(this.diqu);
        dest.writeInt(this.ispic);
        dest.writeInt(this.id);
        dest.writeString(this.ring);
        dest.writeString(this.sjhm);
        dest.writeString(this.scsj);
        dest.writeString(this.sex);
        dest.writeString(this.eye);
        dest.writeString(this.foot);
        dest.writeString(this.color);
        dest.writeString(this.xingming);
        dest.writeString(this.cskh);
        dest.writeString(this.rpsj);
        dest.writeString(this.gpmc);
        dest.writeString(this.address);
        dest.writeString(this.tel);
    }

    public SearchFootEntity() {
    }

    protected SearchFootEntity(Parcel in) {
        this.ttzb = in.readString();
        this.diqu = in.readString();
        this.ispic = in.readInt();
        this.id = in.readInt();
        this.ring = in.readString();
        this.sjhm = in.readString();
        this.scsj = in.readString();
        this.sex = in.readString();
        this.eye = in.readString();
        this.foot = in.readString();
        this.color = in.readString();
        this.xingming = in.readString();
        this.cskh = in.readString();
        this.rpsj = in.readString();
        this.gpmc = in.readString();
        this.address = in.readString();
        this.tel = in.readString();
    }

    public static final Parcelable.Creator<SearchFootEntity> CREATOR = new Parcelable.Creator<SearchFootEntity>() {
        @Override
        public SearchFootEntity createFromParcel(Parcel source) {
            return new SearchFootEntity(source);
        }

        @Override
        public SearchFootEntity[] newArray(int size) {
            return new SearchFootEntity[size];
        }
    };
}
