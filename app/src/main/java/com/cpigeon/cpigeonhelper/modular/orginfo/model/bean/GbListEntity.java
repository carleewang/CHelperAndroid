package com.cpigeon.cpigeonhelper.modular.orginfo.model.bean;

/**
 * Created by Administrator on 2017/12/19.
 */

public class GbListEntity {


//    {
//        "item":"登录",//获得鸽币项目名称
//            "gebi":5,//获得鸽币数量
//            "datetime":"2016/1/24 14:33:00"//获得鸽币时间
//    }

    /**
     * item : 登录
     * gebi : 5
     * datetime : 2016/1/24 14:33:00
     */

    private String item;
    private int gebi;
    private String datetime;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getGebi() {
        return gebi;
    }

    public void setGebi(int gebi) {
        this.gebi = gebi;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
