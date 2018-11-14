package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * 处罚记录
 * Created by Administrator on 2018/3/30.
 */

public class PenalizeRecordEntity implements Serializable {

//    {"status":true,"errorCode":0,"msg":"","data":[{
//        "cfreson":"处罚原因",//处罚原因
//                "cfcxsj":"2018-02-28",//处罚时间
//                "cid":"6",//索引ID
//                "cfcxyy":"",//处罚撤销原因
//                "cfsj":"2018-02-28",//处罚撤销时间
//                "cfresult":"禁赛一年"//处罚结果
//    }]}

    /**
     * cfreson : 处罚原因
     * cfcxsj : 2018-02-28
     * cid : 6
     * cfcxyy :
     * cfsj : 2018-02-28
     * cfresult : 禁赛一年
     */

    private String cfreson;//处罚原因
    private String cfcxsj;//处罚撤销时间
    private String cid;//索引ID
    private String cfcxyy;//处罚撤销原因
    private String cfsj;//处罚时间
    private String cfresult;//处罚结果

    public String getCfreson() {
        return cfreson;
    }

    public void setCfreson(String cfreson) {
        this.cfreson = cfreson;
    }

    public String getCfcxsj() {
        return cfcxsj;
    }

    public void setCfcxsj(String cfcxsj) {
        this.cfcxsj = cfcxsj;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCfcxyy() {
        return cfcxyy;
    }

    public void setCfcxyy(String cfcxyy) {
        this.cfcxyy = cfcxyy;
    }

    public String getCfsj() {
        return cfsj;
    }

    public void setCfsj(String cfsj) {
        this.cfsj = cfsj;
    }

    public String getCfresult() {
        return cfresult;
    }

    public void setCfresult(String cfresult) {
        this.cfresult = cfresult;
    }
}
