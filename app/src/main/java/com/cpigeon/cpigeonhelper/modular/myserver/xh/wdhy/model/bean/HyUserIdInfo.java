package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/11.
 */

public class HyUserIdInfo implements Serializable {

//    {"status":true,"errorCode":0,"msg":"","data":{
//        "sfzhm":"身份证号码",//身份证号码
//                "xingming":"姓名",//姓名
//                "sfzid":"52",//索引ID
//                "sfzzm":[],//身份证正面图片，二进制数组
//        "sfzbm":[]//身份证背面图片，二进制数组
//    }}


    /**
     * sfzhm : 身份证号码
     * xingming : 姓名
     * sfzid : 52
     * sfzzm : []
     * sfzbm : []
     */

    private String sfzhm;
    private String xingming;
    private String sfzid;
    private byte[] sfzzm;
    private byte[] sfzbm;

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getSfzid() {
        return sfzid;
    }

    public void setSfzid(String sfzid) {
        this.sfzid = sfzid;
    }


    public byte[] getSfzzm() {
        return sfzzm;
    }

    public void setSfzzm(byte[] sfzzm) {
        this.sfzzm = sfzzm;
    }

    public byte[] getSfzbm() {
        return sfzbm;
    }

    public void setSfzbm(byte[] sfzbm) {
        this.sfzbm = sfzbm;
    }
}
