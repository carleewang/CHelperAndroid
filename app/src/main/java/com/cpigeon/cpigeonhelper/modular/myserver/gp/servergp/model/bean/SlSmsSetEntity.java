package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * 上笼短信设置信息
 * Created by Administrator on 2017/12/25.
 */

public class SlSmsSetEntity {


//    {"status":true,"errorCode":0,"msg":"","data":{
//        "zhiding":true,			//是否发送插组指定
//                "sfkq":true,			//是否开启
//                "gzxm":true,			//是否发送鸽主姓名
//                "gpjc":"公棚简称",		//公棚简称
//                "xmmc":"test"			//项目名称
//    }}

    /**
     * zhiding : true
     * sfkq : true
     * gzxm : true
     * gpjc : 公棚简称
     * xmmc : test
     */

    private boolean zhiding;//是否发送插组指定
    private boolean sfkq;//是否开启
    private boolean gzxm;//是否发送鸽主姓名
    private String gpjc;//公棚简称
    private String xmmc;//项目名称

    public boolean isZhiding() {
        return zhiding;
    }

    public void setZhiding(boolean zhiding) {
        this.zhiding = zhiding;
    }

    public boolean isSfkq() {
        return sfkq;
    }

    public void setSfkq(boolean sfkq) {
        this.sfkq = sfkq;
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

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }
}
