package com.cpigeon.cpigeonhelper.modular.order.model.bean;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ServesInfoEntity {

//    {"status":true,"errorCode":0,"msg":"","data":{
//        "gytdqsj":"2018-03-13",//鸽运通到期时间。公棚、协会、个人登录后都要显示；返回空字符串表示服务不存在，请开通。
//                "zbptdqsj":"2018-03-13",//直播平台到期时间。公棚、协会登录后显示；返回空字符串表示服务不存在，请开通。
//                "xhwzdqsj":"2018-03-13",//协会网站到期时间。协会登录后显示；返回空字符串表示服务不存在，请开通。
//                "gpwzdqsj":"2018-03-13",//公棚网站到期时间。公棚登录后显示；返回空字符串表示服务不存在，请开通。
//                "xgtdqsj":"2018-03-13",//训鸽通到期时间。协会、个人登录后显示；返回空字符串表示服务不存在，请开通。
//                "gxtsyts":"0"//鸽信通剩余条数。公棚、协会、个人登录后显示；返回空字符串表示服务不存在，请开通。
//    }}


    /**
     * gytdqsj : 2018-03-13
     * zbptdqsj : 2018-03-13
     * xhwzdqsj : 2018-03-13
     * gpwzdqsj : 2018-03-13
     * xgtdqsj : 2018-03-13
     * gxtsyts : 0
     */

    private String gytdqsj;//鸽运通到期时间。公棚、协会、个人登录后都要显示；返回空字符串表示服务不存在，请开通。
    private String xgtdqsj;//训鸽通到期时间。协会、个人登录后显示；返回空字符串表示服务不存在，请开通。
    private String zbptdqsj;//直播平台到期时间。公棚、协会登录后显示；返回空字符串表示服务不存在，请开通。

    private String xhwzdqsj;//协会网站到期时间。协会登录后显示；返回空字符串表示服务不存在，请开通。
    private String gpwzdqsj;//公棚网站到期时间。公棚登录后显示；返回空字符串表示服务不存在，请开通。

    private String gxtsyts;//鸽信通剩余条数。公棚、协会、个人登录后显示；返回空字符串表示服务不存在，请开通。
    private String gpsgdqsj;//公棚赛鸽服务到期时间
    private String xhhydqsj;//我的会员到期时间

    public String getGytdqsj() {
        return gytdqsj;
    }

    public void setGytdqsj(String gytdqsj) {
        this.gytdqsj = gytdqsj;
    }

    public String getZbptdqsj() {
        return zbptdqsj;
    }

    public void setZbptdqsj(String zbptdqsj) {
        this.zbptdqsj = zbptdqsj;
    }

    public String getXhwzdqsj() {
        return xhwzdqsj;
    }

    public void setXhwzdqsj(String xhwzdqsj) {
        this.xhwzdqsj = xhwzdqsj;
    }

    public String getGpwzdqsj() {
        return gpwzdqsj;
    }

    public void setGpwzdqsj(String gpwzdqsj) {
        this.gpwzdqsj = gpwzdqsj;
    }

    public String getXgtdqsj() {
        return xgtdqsj;
    }

    public void setXgtdqsj(String xgtdqsj) {
        this.xgtdqsj = xgtdqsj;
    }

    public String getGxtsyts() {
        return gxtsyts;
    }

    public void setGxtsyts(String gxtsyts) {
        this.gxtsyts = gxtsyts;
    }

    public String getGpsgdqsj() {
        return gpsgdqsj;
    }

    public void setGpsgdqsj(String gpsgdqsj) {
        this.gpsgdqsj = gpsgdqsj;
    }

    public String getXhhydqsj() {
        return xhhydqsj;
    }

    public void setXhhydqsj(String xhhydqsj) {
        this.xhhydqsj = xhhydqsj;
    }
}
