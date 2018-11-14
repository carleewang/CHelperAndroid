package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/8.
 */

public class XGTEntity implements Serializable {
    /**
     * sfzzm : [255,216,255,224,0,16,74,70]
     * sfzbm : [255,216,255,224,0,16,74,70]
     * xingming : 姓名
     * qmshenhe : 1
     */

    private byte[] sfzzm;//身份证正面
    private byte[] sfzbm;//身份证背面
    private String xingming;//姓名
    private int qmshenhe;//审核标记  0为正常；1为关闭；9为审核中
    private int statuscode;//审核标记：0为正常；1为已关闭；2为身份证信息不属实；3为信息不完整；9为待审核


    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
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

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public int getQmshenhe() {
        return qmshenhe;
    }

    public void setQmshenhe(int qmshenhe) {
        this.qmshenhe = qmshenhe;
    }


}
