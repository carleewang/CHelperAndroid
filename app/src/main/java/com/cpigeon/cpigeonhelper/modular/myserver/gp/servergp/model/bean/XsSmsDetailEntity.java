package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * Created by Administrator on 2017/12/25.
 */

public class XsSmsDetailEntity {

//    {"status":true,"errorCode":0,"msg":"","data":{
//        "slys":18,				//上笼羽数
//                "type":"bs",				//类型:bs=比赛|xf=训放
//                "sfdd":"0",				//司放地点
//                "sfsj":"2017-12-05 09:55:00",		//司放时间
//                "xmmc":"比赛加站测试",			//项目名称
//                "xsys":"全部"				//显示羽数
//    }}
    /**
     * slys : 18
     * type : bs
     * sfdd : 0
     * sfsj : 2017-12-05 09:55:00
     * xmmc : 比赛加站测试
     * xsys : 全部
     */

    private int slys;//上笼羽数
    private String type;//类型:bs=比赛|xf=训放
    private String sfdd;//司放地点
    private String sfsj;//司放时间
    private String xmmc;//项目名称
    private String xsys;//显示羽数

    public int getSlys() {
        return slys;
    }

    public void setSlys(int slys) {
        this.slys = slys;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSfdd() {
        return sfdd;
    }

    public void setSfdd(String sfdd) {
        this.sfdd = sfdd;
    }

    public String getSfsj() {
        return sfsj;
    }

    public void setSfsj(String sfsj) {
        this.sfsj = sfsj;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getXsys() {
        return xsys;
    }

    public void setXsys(String xsys) {
        this.xsys = xsys;
    }
}
