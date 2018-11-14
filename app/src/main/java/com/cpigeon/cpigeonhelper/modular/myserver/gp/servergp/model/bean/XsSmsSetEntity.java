package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * 训赛短信设置数据
 * Created by Administrator on 2017/12/25.
 */

public class XsSmsSetEntity {


//    {"status":true,"errorCode":0,"msg":"","data":{
//        "fsfs":true,			//是否发送分速
//                "kqzt":true,			//是否开启
//                "fsdx":0,			//发送对象，为0则全部发送
//                "gzxm":true,			//是否发送鸽主姓名
//                "gpjc":"健翔云天公棚",		//公棚简称
//                "type":"bs",			//类型：bs|xf，比赛时不能设置发送对象
//                "fsmc":true,			//是否发送名次
//                "zhhm":true			//是否发送足环号码
//    }}

    /**
     * fsfs : true
     * kqzt : true
     * fsdx : 0
     * gzxm : true
     * gpjc : 健翔云天公棚
     * type : bs
     * fsmc : true
     * zhhm : true
     */

    private boolean fsfs = false;//是否发送分速
    private boolean kqzt = false;//是否开启
    private int fsdx = 0;//发送对象，为0则全部发送
    private boolean gzxm = false;//是否发送鸽主姓名
    private String gpjc;//公棚简称
    private String type;//类型：bs|xf，比赛时不能设置发送对象
    private boolean fsmc = false;//是否发送名次
    private boolean zhhm = false;//是否发送足环号码
    private double jingdu;//经度
    private double weidu;//经度

    public boolean isFsfs() {
        return fsfs;
    }

    public void setFsfs(boolean fsfs) {
        this.fsfs = fsfs;
    }

    public boolean isKqzt() {
        return kqzt;
    }

    public void setKqzt(boolean kqzt) {
        this.kqzt = kqzt;
    }

    public int getFsdx() {
        return fsdx;
    }

    public void setFsdx(int fsdx) {
        this.fsdx = fsdx;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFsmc() {
        return fsmc;
    }

    public void setFsmc(boolean fsmc) {
        this.fsmc = fsmc;
    }

    public boolean isZhhm() {
        return zhhm;
    }

    public void setZhhm(boolean zhhm) {
        this.zhhm = zhhm;
    }

    public double getJingdu() {
        return jingdu;
    }

    public void setJingdu(double jingdu) {
        this.jingdu = jingdu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }
}
