package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean;

/**
 * Created by Administrator on 2017/12/18.
 */

public class BsdxSettingEntity {

//    {
//        "gcsj":false,		//是否发送归巢时间
//            "xhjc":"",		//协会简称
//            "bsxm":false,		//是否发送比赛项目
//            "kqzt":false,		//是否开启发送比赛短信
//            "gzfs":false		//是否发送分速
//    }

    /**
     * gcsj : false
     * xhjc :
     * bsxm : false
     * kqzt : false
     * gzfs : false
     */

    private boolean gcsj;//是否发送归巢时间
    private String xhjc;//协会简称
    private boolean bsxm;//是否发送比赛项目
    private boolean kqzt;//是否开启发送比赛短信
    private boolean gzfs;//是否发送分速

    public boolean isGcsj() {
        return gcsj;
    }

    public void setGcsj(boolean gcsj) {
        this.gcsj = gcsj;
    }

    public String getXhjc() {
        return xhjc;
    }

    public void setXhjc(String xhjc) {
        this.xhjc = xhjc;
    }

    public boolean isBsxm() {
        return bsxm;
    }

    public void setBsxm(boolean bsxm) {
        this.bsxm = bsxm;
    }

    public boolean isKqzt() {
        return kqzt;
    }

    public void setKqzt(boolean kqzt) {
        this.kqzt = kqzt;
    }

    public boolean isGzfs() {
        return gzfs;
    }

    public void setGzfs(boolean gzfs) {
        this.gzfs = gzfs;
    }
}
