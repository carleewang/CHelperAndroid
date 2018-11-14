package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/4.
 */

public class HistoryLeagueEntity  implements Serializable {


    /**
     * xmmc : 棋盘关奖赛
     * zhhm : 足环号码
     * bsmc : 名次
     * bsgm : 1200
     * bsrq : 2015-05-05 00:00:00
     */

    private String xmmc;
    private String zhhm;
    private String bsmc;
    private String bsgm;
    private String bsrq;
    private String rid;

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getZhhm() {
        return zhhm;
    }

    public void setZhhm(String zhhm) {
        this.zhhm = zhhm;
    }

    public String getBsmc() {
        return bsmc;
    }

    public void setBsmc(String bsmc) {
        this.bsmc = bsmc;
    }

    public String getBsgm() {
        return bsgm;
    }

    public void setBsgm(String bsgm) {
        this.bsgm = bsgm;
    }

    public String getBsrq() {
        return bsrq;
    }

    public void setBsrq(String bsrq) {
        this.bsrq = bsrq;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
