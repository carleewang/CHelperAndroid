package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/22.
 */

public class AgentTakePlaceListEntity implements Serializable {

    /**
     * id : 1
     * lxr : 00000
     * dsd : 00000
     * uid : 5473
     * tel : 00000
     * diz : 00000
     */

    private String id;//索引ID
    private String lxr;//联系人姓名
    private String dsd;//代售点名称
    private String uid;//会员ID
    private String tel;//联系电话
    private String diz;//联系地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getDsd() {
        return dsd;
    }

    public void setDsd(String dsd) {
        this.dsd = dsd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDiz() {
        return diz;
    }

    public void setDiz(String diz) {
        this.diz = diz;
    }
}
