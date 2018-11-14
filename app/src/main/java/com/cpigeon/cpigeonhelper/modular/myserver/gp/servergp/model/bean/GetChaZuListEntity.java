package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * Created by Administrator on 2018/5/16.
 */

public class GetChaZuListEntity {


    /**
     * cz : A
     * gz :
     * csf : 0
     */

//    {
//        "cz":"A",//24个插组：A-X
//            "gz":"",//规则，返回空表示没有设置
//            "csf":"0"//参赛费，返回0表示没有设置
//    }

    private String cz;//24个插组：A-X
    private String gz;//规则，返回空表示没有设置
    private String csf;//参赛费，返回0表示没有设置
    private String bm;//别名

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getGz() {
        return gz;
    }

    public void setGz(String gz) {
        this.gz = gz;
    }

    public String getCsf() {
        return csf;
    }

    public void setCsf(String csf) {
        this.csf = csf;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }
}
