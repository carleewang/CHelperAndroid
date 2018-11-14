package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class GyjlMessageEntity {
//
//    获取用户给协会的留言
//    请求地址：【POST】/CHAPI/V1/XH_GetLY
//    说明：获取用户给协会的留言。
//    适用版本：V1
//    添加日期：2018-3-19
//    必需参数：
//    uid：会员ID
//
//    成功返回：
//    {"status":true,"errorCode":0,"msg":"","data":[{
//        "lyid":"4554",//留言ID
//                "uid":"21772",//留言用户ID
//                "nichen":"zg1330204415",//留言用户昵称
//                "lysj":"2017/9/15 22:36:26",//留言时间
//                "lynr":"留言内容",//留言内容
//                "lylx":"协会留言",//类型
//                "toux":"http://www.cpigeon.com/Content/faces/default.png"//留言用户头像
//    }]}


    /**
     * lyid : 4554
     * uid : 21772
     * nichen : zg1330204415
     * lysj : 2017/9/15 22:36:26
     * lynr : 留言内容
     * lylx : 协会留言
     * toux : http://www.cpigeon.com/Content/faces/default.png
     */

    private String lyid;//留言ID
    private String uid;//留言用户ID
    private String nichen;//留言用户昵称
    private String lysj;//留言时间
    private String lynr;//留言内容
    private String lylx;//类型
    private String toux;//留言用户头像

    public String getLyid() {
        return lyid;
    }

    public void setLyid(String lyid) {
        this.lyid = lyid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNichen() {
        return nichen;
    }

    public void setNichen(String nichen) {
        this.nichen = nichen;
    }

    public String getLysj() {
        return lysj;
    }

    public void setLysj(String lysj) {
        this.lysj = lysj;
    }

    public String getLynr() {
        return lynr;
    }

    public void setLynr(String lynr) {
        this.lynr = lynr;
    }

    public String getLylx() {
        return lylx;
    }

    public void setLylx(String lylx) {
        this.lylx = lylx;
    }

    public String getToux() {
        return toux;
    }

    public void setToux(String toux) {
        this.toux = toux;
    }

    private List<HflistBean> hflist;

    public List<HflistBean> getHflist() {
        return hflist;
    }

    public void setHflist(List<HflistBean> hflist) {
        this.hflist = hflist;
    }
}
