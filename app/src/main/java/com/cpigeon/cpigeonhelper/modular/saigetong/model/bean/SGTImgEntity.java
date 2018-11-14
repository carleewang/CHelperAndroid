package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SGTImgEntity {
    /**
     * sj : 2018/1/23 17:38:55
     * imgurl : 原图图片地址
     * slturl : 缩略图地址
     * tag : 入棚拍照
     * updatefootinfo : //足环修改提示信息
     */

    private String sj;//时间
    private String imgurl;//原图图片地址
    private String slturl;//缩略图地址
    private String tag;//入棚拍照
    private String updatefootinfo;//足环修改提示信息
    private int bupai;

    public int getBupai() {
        return bupai;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getSlturl() {
        return slturl;
    }

    public void setSlturl(String slturl) {
        this.slturl = slturl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUpdatefootinfo() {
        return updatefootinfo;
    }

    public void setUpdatefootinfo(String updatefootinfo) {
        this.updatefootinfo = updatefootinfo;
    }


//    /**
//     * sj : 2017/9/18 11:05:29
//     * imgurl : http://118.123.244.89:818/uploadfiles/GYTRace/451926_6f5b13f731a88fe945de84bb6c33387b.jpg
//     * slturl : http://118.123.244.89:818/uploadfiles/GYTRace/thumb/i_451926_6f5b13f731a88fe945de84bb6c33387b.jpg
//     * tag : 标签名称
//     */
//
//    private String sj;
//    private String imgurl;
//    private String slturl;
//    private String tag;
//
//    public String getSj() {
//        return sj;
//    }
//
//    public void setSj(String sj) {
//        this.sj = sj;
//    }
//
//    public String getImgurl() {
//        return imgurl;
//    }
//
//    public void setImgurl(String imgurl) {
//        this.imgurl = imgurl;
//    }
//
//    public String getSlturl() {
//        return slturl;
//    }
//
//    public void setSlturl(String slturl) {
//        this.slturl = slturl;
//    }
//
//    public String getTag() {
//        return tag;
//    }
//
//    public void setTag(String tag) {
//        this.tag = tag;
//    }
}
