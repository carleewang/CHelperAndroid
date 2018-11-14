package com.cpigeon.cpigeonhelper.modular.orginfo.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/28.
 */

public class UserType {


//    GetUserType：中鸽助手获取用户信息
//
//    返回：
//    usertype：cphelper.xiehui|cphelper.gongpeng|cphelper.geren|cphelper.none
//
//    authuser：true|false 是否有鸽运通权限
//
//    gytstatus：鸽运通状态    0正常使用    10000未开通
//
//    gxtstatus：鸽信通状态
//                          10010;//开通鸽信通服务正在审核中。
//                          10011;//未开通鸽信通
//                          10012;//审核失败！身份证信息不属实。
//                          10013;//审核失败！信息不完善。
//                          10014;//您还未充值！请先充值。
//
//    sgtstatus = -1;//赛鸽通开通状态：0未开通；1已开通
//

    /**
     * usertype : cphelper.none
     */
    private String usertype;
    private boolean authuser;
    private int gytstatus = -1;
    private int gxtstatus = -1;
    private int sgtstatus = -1;//赛鸽通开通状态：0未开通；1已开通
    private String sgtmsg;//赛鸽通msg
    private int sqpzuid;//返回大于0，显示 授权拍照
    private int authusers = -1;//返回1，显示授权管理
    private int hyglxt = -1;//0为未开通；1为正常；2为已到期
    private String hyglxtdqsj;//协会会员管理系统到期时间
    private ShenggehuiBean shenggehui;


    public int getGytstatus() {
        return gytstatus;
    }

    public void setGytstatus(int gytstatus) {
        this.gytstatus = gytstatus;
    }

    public int getGxtstatus() {
        return gxtstatus;
    }

    public void setGxtstatus(int gxtstatus) {
        this.gxtstatus = gxtstatus;
    }

    public boolean isAuthuser() {
        return authuser;
    }

    public void setAuthuser(boolean authuser) {
        this.authuser = authuser;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }


    public int getSgtstatus() {
        return sgtstatus;
    }

    public void setSgtstatus(int sgtstatus) {
        this.sgtstatus = sgtstatus;
    }

    public String getSgtmsg() {
        return sgtmsg;
    }

    public void setSgtmsg(String sgtmsg) {
        this.sgtmsg = sgtmsg;
    }

    public int getSqpzuid() {
        return sqpzuid;
    }

    public void setSqpzuid(int sqpzuid) {
        this.sqpzuid = sqpzuid;
    }


    public int getAuthusers() {
        return authusers;
    }

    public void setAuthusers(int authusers) {
        this.authusers = authusers;
    }

    public int getHyglxt() {
        return hyglxt;
    }

    public void setHyglxt(int hyglxt) {
        this.hyglxt = hyglxt;
    }

    public String getHyglxtdqsj() {
        return hyglxtdqsj;
    }

    public void setHyglxtdqsj(String hyglxtdqsj) {
        this.hyglxtdqsj = hyglxtdqsj;
    }

    public ShenggehuiBean getShenggehui() {
        return shenggehui;
    }

    public void setShenggehui(ShenggehuiBean shenggehui) {
        this.shenggehui = shenggehui;
    }

    public static class ShenggehuiBean  implements Serializable {
        /**
         * province : 四川
         * powers : 1
         */

        private String province;
        private String powers;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getPowers() {
            return powers;
        }

        public void setPowers(String powers) {
            this.powers = powers;
        }
    }
}
