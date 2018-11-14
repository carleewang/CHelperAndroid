package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * 年度缴费
 * <p>
 * Created by Administrator on 2018/4/4.
 */

public class YearPayCostEntity implements Serializable {


    /**
     * jstate : 已交费
     * jyear : 2018
     * jfrq : 2018-02-26
     * jid : 10
     */

    private String jstate;
    private String jyear;
    private String jfrq;
    private String jid;
    private String money;

    public String getJstate() {
        return jstate;
    }

    public void setJstate(String jstate) {
        this.jstate = jstate;
    }

    public String getJyear() {
        return jyear;
    }

    public void setJyear(String jyear) {
        this.jyear = jyear;
    }

    public String getJfrq() {
        return jfrq;
    }

    public void setJfrq(String jfrq) {
        this.jfrq = jfrq;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
