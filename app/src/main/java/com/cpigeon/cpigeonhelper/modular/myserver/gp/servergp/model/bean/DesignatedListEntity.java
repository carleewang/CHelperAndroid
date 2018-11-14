package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/28.
 */

public class DesignatedListEntity implements Serializable {


    /**
     * sfsj : 2018-04-24 09:30:00
     * tid : 15090
     * slys : 1
     * sfdd :
     * xmmc : 项目名称
     */

    private String sfsj;
    private int tid;
    private int slys;
    private String sfdd;
    private String xmmc;

    public String getSfsj() {
        return sfsj;
    }

    public void setSfsj(String sfsj) {
        this.sfsj = sfsj;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getSlys() {
        return slys;
    }

    public void setSlys(int slys) {
        this.slys = slys;
    }

    public String getSfdd() {
        return sfdd;
    }

    public void setSfdd(String sfdd) {
        this.sfdd = sfdd;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }
}
