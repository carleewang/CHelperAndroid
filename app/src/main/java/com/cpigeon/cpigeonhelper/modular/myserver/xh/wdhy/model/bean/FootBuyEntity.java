package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * 足环购买
 * Created by Administrator on 2018/3/30.
 */

public class FootBuyEntity implements Serializable {


    /**
     * ztype : 连号足环
     * zfoot :
     * zfoot1 : 2018-1-222
     * zfoot2 : 2018-1-12323
     * zid : 1
     * zgmsj : 2018-03-23
     */

    private String ztype;//类型
    private String zfoot;//足环号码，散号足环类型显示
    private String zfoot1;//连号足环开始号码，连号类型足环显示
    private String zfoot2;//连号足环结束号码，连号类型足环显示
    private String zid;//索引ID
    private String zgmsj;//足环购买时间

    public String getZtype() {
        return ztype;
    }

    public void setZtype(String ztype) {
        this.ztype = ztype;
    }

    public String getZfoot() {
        return zfoot;
    }

    public void setZfoot(String zfoot) {
        this.zfoot = zfoot;
    }

    public String getZfoot1() {
        return zfoot1;
    }

    public void setZfoot1(String zfoot1) {
        this.zfoot1 = zfoot1;
    }

    public String getZfoot2() {
        return zfoot2;
    }

    public void setZfoot2(String zfoot2) {
        this.zfoot2 = zfoot2;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getZgmsj() {
        return zgmsj;
    }

    public void setZgmsj(String zgmsj) {
        this.zgmsj = zgmsj;
    }
}
