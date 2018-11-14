package com.cpigeon.cpigeonhelper.modular.order.model.bean;

import java.io.Serializable;

/**
 * 充值明细
 * Created by Administrator on 2017/12/21.
 */

public class RechargeMxEntity implements Serializable{


//    {"id":1057,"time":"2017-06-01
//        11:26:13","number":"ZGCZ2017060110275473","statusname":" 充 值 完 成
//        ","price":0.01,"payway":" 支 付 宝 充 值 "},{"id":1026,"time":"2017-05-09
//        15:54:03","number":"ZGCZ201705098635473","statusname":" 充 值 完 成
//        ","price":0.01,"payway":"支付宝充值"}
    /**
     * id : 1057
     * time : 2017-06-01 11:26:13
     * number : ZGCZ2017060110275473
     * statusname :  充 值 完 成
     * price : 0.01
     * payway :  支 付 宝 充 值
     */

    private int id;//充值id
    private String time;//充值时间
    private String number;//订单号
    private String statusname;//充值状态名称
    private double price;//价格
    private String payway;//充值方式

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }
}
