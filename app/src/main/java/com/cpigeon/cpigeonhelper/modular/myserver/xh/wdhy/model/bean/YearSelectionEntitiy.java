package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean;

import java.io.Serializable;

/**
 * 年度评选
 * Created by Administrator on 2018/4/4.
 */

public class YearSelectionEntitiy implements Serializable {


    /**
     * pxsj : 2018-02-26
     * pid : 8
     * pxcomment : 评语
     * pxyear : 2018
     * pxresult : 最佳鸽棚
     */

    private String pxsj;
    private String pid;
    private String pxcomment;
    private String pxyear;
    private String pxresult;

    public String getPxsj() {
        return pxsj;
    }

    public void setPxsj(String pxsj) {
        this.pxsj = pxsj;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPxcomment() {
        return pxcomment;
    }

    public void setPxcomment(String pxcomment) {
        this.pxcomment = pxcomment;
    }

    public String getPxyear() {
        return pxyear;
    }

    public void setPxyear(String pxyear) {
        this.pxyear = pxyear;
    }

    public String getPxresult() {
        return pxresult;
    }

    public void setPxresult(String pxresult) {
        this.pxresult = pxresult;
    }
}
