package com.cpigeon.cpigeonhelper.modular.flyarea.model.bean;

/**
 * Created by Administrator on 2018/5/3.
 */

public class SelectPlaceSelect {
    private double lo;
    private double la;
    private String ad;//地址

    public SelectPlaceSelect(double lo, double la, String ad) {
        this.lo = lo;
        this.la = la;
        this.ad = ad;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }
}
