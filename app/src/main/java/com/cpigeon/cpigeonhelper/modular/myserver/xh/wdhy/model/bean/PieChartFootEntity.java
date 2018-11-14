package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

/**
 * Created by Administrator on 2018/6/21.
 */

public class PieChartFootEntity {


    /**
     * totalcount : 100010
     * totalcountwjf : 100010
     * totalcountyyg : 0
     * totalcountyjf : 0
     * totalcountys : 0
     * totalcountwyg : 100010
     * totalcountws : 100010
     */

    private String totalcount;//总计足环数量
    private String totalcountwjf;//未交费
    private String totalcountyjf;//已交费
    private String totalcountyyg;//已验鸽
    private String totalcountwyg;//未验鸽
    private String totalcountws;//未售出足环
    private String totalcountys;//已售足环
    private String totalpaysumyjf;//已缴费金额
    private String totalpaysumwjf;//未交费金额

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getTotalcountwjf() {
        return totalcountwjf;
    }

    public void setTotalcountwjf(String totalcountwjf) {
        this.totalcountwjf = totalcountwjf;
    }

    public String getTotalcountyyg() {
        return totalcountyyg;
    }

    public void setTotalcountyyg(String totalcountyyg) {
        this.totalcountyyg = totalcountyyg;
    }

    public String getTotalcountyjf() {
        return totalcountyjf;
    }

    public void setTotalcountyjf(String totalcountyjf) {
        this.totalcountyjf = totalcountyjf;
    }

    public String getTotalcountys() {
        return totalcountys;
    }

    public void setTotalcountys(String totalcountys) {
        this.totalcountys = totalcountys;
    }

    public String getTotalcountwyg() {
        return totalcountwyg;
    }

    public void setTotalcountwyg(String totalcountwyg) {
        this.totalcountwyg = totalcountwyg;
    }

    public String getTotalcountws() {
        return totalcountws;
    }

    public void setTotalcountws(String totalcountws) {
        this.totalcountws = totalcountws;
    }

    public String getTotalpaysumyjf() {
        return totalpaysumyjf;
    }

    public void setTotalpaysumyjf(String totalpaysumyjf) {
        this.totalpaysumyjf = totalpaysumyjf;
    }

    public String getTotalpaysumwjf() {
        return totalpaysumwjf;
    }

    public void setTotalpaysumwjf(String totalpaysumwjf) {
        this.totalpaysumwjf = totalpaysumwjf;
    }
}
