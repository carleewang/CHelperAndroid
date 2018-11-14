package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class GyjlReviewEntity {


    /**
     * nichen : zg13618082936
     * plsj : 2018/3/13 12:09:38
     * pllx : 协会规程评论
     * uid : 11225
     * plid : 4854
     * plnr : 评论内容
     * toux : http://www.cpigeon.com/Content/faces/default.png
     */

    private String nichen;
    private String plsj;
    private String pllx;
    private String uid;
    private String plid;
    private String plnr;
    private String toux;
    private String title;
    private List<HflistBean> hflist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNichen() {
        return nichen;
    }

    public void setNichen(String nichen) {
        this.nichen = nichen;
    }

    public String getPlsj() {
        return plsj;
    }

    public void setPlsj(String plsj) {
        this.plsj = plsj;
    }

    public String getPllx() {
        return pllx;
    }

    public void setPllx(String pllx) {
        this.pllx = pllx;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlid() {
        return plid;
    }

    public void setPlid(String plid) {
        this.plid = plid;
    }

    public String getPlnr() {
        return plnr;
    }

    public void setPlnr(String plnr) {
        this.plnr = plnr;
    }

    public String getToux() {
        return toux;
    }

    public void setToux(String toux) {
        this.toux = toux;
    }

    public List<HflistBean> getHflist() {
        return hflist;
    }

    public void setHflist(List<HflistBean> hflist) {
        this.hflist = hflist;
    }

}
