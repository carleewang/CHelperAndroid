package com.cpigeon.cpigeonhelper.modular.order.model.bean;

import java.io.Serializable;

/**
 * 订单列表bean
 * Created by Administrator on 2017/6/20.
 */

public class OrderList implements Serializable {


//    {"ddgb":16000,"ddbh":"GYTTA2018010602617600019","ddsj":"2018/1/6 17:11:36","ddid":8717,"ddje":80.00,"ddnr":"鸽运通单场授权购买_鸽运通单场授权","ddzt":2}


    /**
     * ddgb : 16000
     * ddbh : GYTTA2018010602617600019
     * ddsj : 2018/1/6 17:11:36
     * ddid : 8717
     * ddje : 80.0
     * ddnr : 鸽运通单场授权购买_鸽运通单场授权
     * ddzt : 2
     */

    private int ddgb;//订单鸽币
    private String ddbh;//订单编号
    private String ddsj;//订单时间
    private int ddid;//订单id
    private double ddje;//订单金额
    private String ddnr;//订单内容
    private int ddzt;//订单状态
    private String ddtype;//网页订单  中鸽助手订单
    private String ddly;//android/ios
    private String ddfp;//订单发票


    public int getDdgb() {
        return ddgb;
    }

    public void setDdgb(int ddgb) {
        this.ddgb = ddgb;
    }

    public String getDdbh() {
        return ddbh;
    }

    public void setDdbh(String ddbh) {
        this.ddbh = ddbh;
    }

    public String getDdsj() {
        return ddsj;
    }

    public void setDdsj(String ddsj) {
        this.ddsj = ddsj;
    }

    public int getDdid() {
        return ddid;
    }

    public void setDdid(int ddid) {
        this.ddid = ddid;
    }

    public double getDdje() {
        return ddje;
    }

    public void setDdje(double ddje) {
        this.ddje = ddje;
    }

    public String getDdnr() {
        return ddnr;
    }

    public void setDdnr(String ddnr) {
        this.ddnr = ddnr;
    }

    public int getDdzt() {
        return ddzt;
    }

    public void setDdzt(int ddzt) {
        this.ddzt = ddzt;
    }

    public String getDdtype() {
        return ddtype;
    }

    public void setDdtype(String ddtype) {
        this.ddtype = ddtype;
    }

    public String getDdly() {
        return ddly;
    }

    public void setDdly(String ddly) {
        this.ddly = ddly;
    }

    public String getDdfp() {
        return ddfp;
    }

    public void setDdfp(String ddfp) {
        this.ddfp = ddfp;
    }

    //    /**
//     * id : 4198
//     * time : 2017-05-2514:08:46
//     * payway :
//     * item :  足 环 查 询 服 务 _ 普 通 套 餐
//     * number : ZHCXA2017052501695200014
//     * statusname :  待 支 付
//     * price : 8
//     * scores : 1000
//     * ispay : 0
//     * serviceid : 5
//     */
//
//    private int id;//订单id
//    private String time;//下单时间
//    private String payway;//支付方式
//    private String item;//订单内容
//    private String number;//订单号
//    private String statusname;//支付状态：待支付，已支付，支付完成
//    private double price;//价格
//    private double scores;//积分
//    private int ispay;
//    private int serviceid;
//    private String ordertype;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getPayway() {
//        return payway;
//    }
//
//    public void setPayway(String payway) {
//        this.payway = payway;
//    }
//
//    public String getItem() {
//        return item;
//    }
//
//    public void setItem(String item) {
//        this.item = item;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }
//
//    public String getStatusname() {
//        return statusname;
//    }
//
//    public void setStatusname(String statusname) {
//        this.statusname = statusname;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public double getScores() {
//        return scores;
//    }
//
//    public void setScores(double scores) {
//        this.scores = scores;
//    }
//
//    public int getIspay() {
//        return ispay;
//    }
//
//    public void setIspay(int ispay) {
//        this.ispay = ispay;
//    }
//
//    public int getServiceid() {
//        return serviceid;
//    }
//
//    public void setServiceid(int serviceid) {
//        this.serviceid = serviceid;
//    }
//
//    public String getOrdertype() {
//        return ordertype;
//    }
//
//    public void setOrdertype(String ordertype) {
//        this.ordertype = ordertype;
//    }
}
