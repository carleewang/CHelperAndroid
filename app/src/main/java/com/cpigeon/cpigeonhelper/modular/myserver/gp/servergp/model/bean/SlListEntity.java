package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean;

/**
 * 上笼短信列表
 * Created by Administrator on 2017/12/25.
 */

public class SlListEntity {


//    {"status":true,"errorCode":0,"msg":"","data":[
//        {
//            "xmmc":"2017年12月05日上笼数据",		//项目名称
//                "slsj":"2017-12-05 09:55:00",			//上笼时间
//                "tid":14361					//索引ID
//        },
//        {"xmmc":"2017年12月04日上笼数据","slsj":"2017-12-04 14:11:53","tid":14350},
//        {"xmmc":"2017年12月03日上笼数据","slsj":"2017-12-03 11:27:00","tid":14337}
//]}

    /**
     * xmmc : 2017年12月05日上笼数据
     * slsj : 2017-12-05 09:55:00
     * tid : 14361
     */

    private String xmmc;//项目名称
    private String slsj;//上笼时间
    private int tid;//索引ID

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getSlsj() {
        return slsj;
    }

    public void setSlsj(String slsj) {
        this.slsj = slsj;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
