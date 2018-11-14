package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PlayListEntity implements Serializable {

//
//    {"status":true,"errorCode":0,"msg":"","data":[
//        {
//            "tq":"多云",				//天气
//                "sczt":"0",				//申请删除状态：0正常，1删除确认中
//                "sfzb":"105.413986/32.210312",		//司放地坐标
//                "sfkj":"201.026",			//空距/Km
//                "sfdd":"昭化",				//司放地点
//                "bsgm":273,				//比赛规模
//                "sfsj":"2017-12-11 10:00:00",		//司放时间
//                "xmmc":"高坪昭化奖赛",			//项目名称
//                "bsid":68391				//赛事ID
//        }
//]}

    /**
     * tq : 多云
     * sczt : 0
     * sfzb : 105.413986/32.210312
     * sfkj : 201.026
     * sfdd : 昭化
     * bsgm : 273
     * sfsj : 2017-12-11 10:00:00
     * xmmc : 高坪昭化奖赛
     * bsid : 68391
     */

    private String tq;//天气
    private String sczt;//申请删除状态：0正常，1删除确认中
    private String sfzb;//司放地坐标
    private String sfkj;//空距/Km
    private String sfdd;//司放地点
    private int bsgm;//比赛规模
    private String sfsj;    //司放时间
    private String xmmc;    //项目名称
    private int bsid;    //赛事ID

    public String getTq() {
        return tq;
    }

    public void setTq(String tq) {
        this.tq = tq;
    }

    public String getSczt() {
        return sczt;
    }

    public void setSczt(String sczt) {
        this.sczt = sczt;
    }

    public String getSfzb() {
        return sfzb;
    }

    public void setSfzb(String sfzb) {
        this.sfzb = sfzb;
    }

    public String getSfkj() {
        return sfkj;
    }

    public void setSfkj(String sfkj) {
        this.sfkj = sfkj;
    }

    public String getSfdd() {
        return sfdd;
    }

    public void setSfdd(String sfdd) {
        this.sfdd = sfdd;
    }

    public int getBsgm() {
        return bsgm;
    }

    public void setBsgm(int bsgm) {
        this.bsgm = bsgm;
    }

    public String getSfsj() {
        return sfsj;
    }

    public void setSfsj(String sfsj) {
        this.sfsj = sfsj;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public int getBsid() {
        return bsid;
    }

    public void setBsid(int bsid) {
        this.bsid = bsid;
    }
}
