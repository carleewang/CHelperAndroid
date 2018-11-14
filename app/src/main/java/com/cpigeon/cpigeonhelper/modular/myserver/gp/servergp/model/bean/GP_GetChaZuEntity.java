package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * Created by Administrator on 2018/5/16.
 */

public class GP_GetChaZuEntity {

//    {
//        "cz":"A",//组别
//            "bm":"100二十取一",//别名
//            "csf":"0",//参赛费
//            "gz1":"0",//规则一：取n名，n为数字
//            "gz2":"0",//规则二：n取一，n为数字
//            "gz3":"0"//规则三：n名内均分，n为数字
//    }


    /**
     * cz : A
     * bm : 100二十取一
     * csf : 0
     * gz1 : 0
     * gz2 : 0
     * gz3 : 0
     */

    private String cz;
    private String bm;
    private String csf;
    private String gz1;
    private String gz2;
    private String gz3;

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getCsf() {
        return csf;
    }

    public void setCsf(String csf) {
        this.csf = csf;
    }

    public String getGz1() {
        return gz1;
    }

    public void setGz1(String gz1) {
        this.gz1 = gz1;
    }

    public String getGz2() {
        return gz2;
    }

    public void setGz2(String gz2) {
        this.gz2 = gz2;
    }

    public String getGz3() {
        return gz3;
    }

    public void setGz3(String gz3) {
        this.gz3 = gz3;
    }
}
