package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * 公棚入棚短信设置数据
 * Created by Administrator on 2017/12/22.
 */

public class GpRpdxSetEntity {


//    {
//        "sfkq":false,		//是否开启入棚短信设置
//            "sfxs":false,		//是否在入棚记录网页显示收鸽总数
//            "sffs":false,		//是否发送短信
//            "dzhh":false,		//是否发送电子环号
//            "gzxm":false,		//是否发送鸽主姓名
//            "gpjc":"中鸽网",	//公棚简称
//            "cskh":true		//是否发送参赛卡号
//    }
    /**
     * sfkq : false
     * sfxs : false
     * sffs : false
     * dzhh : false
     * gzxm : false
     * gpjc : 中鸽网
     * cskh : true
     */
    private boolean sfkq = false;//是否开启入棚短信设置
    private boolean sfxs = false;//是否在入棚记录网页显示收鸽总数
    private boolean sffs = false;//是否发送短信
    private boolean dzhh = false;//是否发送电子环号
    private boolean gzxm = false;//是否发送鸽主姓名
    private String gpjc = "";//公棚简称
    private boolean cskh = false;//是否发送参赛卡号
    private String rpkssj = "";//本届比赛入棚开始时间

    public boolean isSfkq() {
        return sfkq;
    }

    public void setSfkq(boolean sfkq) {
        this.sfkq = sfkq;
    }

    public boolean isSfxs() {
        return sfxs;
    }

    public void setSfxs(boolean sfxs) {
        this.sfxs = sfxs;
    }

    public boolean isSffs() {
        return sffs;
    }

    public void setSffs(boolean sffs) {
        this.sffs = sffs;
    }

    public boolean isDzhh() {
        return dzhh;
    }

    public void setDzhh(boolean dzhh) {
        this.dzhh = dzhh;
    }

    public boolean isGzxm() {
        return gzxm;
    }

    public void setGzxm(boolean gzxm) {
        this.gzxm = gzxm;
    }

    public String getGpjc() {
        return gpjc;
    }

    public void setGpjc(String gpjc) {
        this.gpjc = gpjc;
    }

    public boolean isCskh() {
        return cskh;
    }

    public void setCskh(boolean cskh) {
        this.cskh = cskh;
    }

    public String getRpkssj() {
        return rpkssj;
    }

    public void setRpkssj(String rpkssj) {
        this.rpkssj = rpkssj;
    }
}
