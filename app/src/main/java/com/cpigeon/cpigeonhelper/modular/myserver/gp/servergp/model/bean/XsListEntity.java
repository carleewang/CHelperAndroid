package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

import java.io.Serializable;

/**
 * 公棚训赛项目列表 实体类
 * Created by Administrator on 2017/12/25.
 */

public class XsListEntity implements Serializable{

//    {"status":true,"errorCode":0,"msg":"","data":[
//        {"xmmc":"180公里热身赛",		//项目名称
//                "slys":4313,				//上笼羽数
//                "sfdd":"泊头",				//司放地点
//                "type":"bs",				//类型
//                "sfsj":"2017-12-03 08:08:00"	//司放时间
//        }]}

    /**
     * xmmc : 180公里热身赛
     * slys : 4313
     * sfdd : 泊头
     * type : bs
     * sfsj : 2017-12-03 08:08:00
     */
    private String tid;//索引id
    private String xmmc;//项目名称
    private int slys;//上笼羽数
    private String sfdd;//司放地点
    private String type;// 类型：bs|xf|jf
    private String sfsj;//司放时间
    private String xsys;//显示羽数

    public String getXsys() {
        return xsys;
    }

    public void setXsys(String xsys) {
        this.xsys = xsys;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSfsj() {
        return sfsj;
    }

    public void setSfsj(String sfsj) {
        this.sfsj = sfsj;
    }
}
