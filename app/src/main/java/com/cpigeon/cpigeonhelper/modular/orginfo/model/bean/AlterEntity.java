package com.cpigeon.cpigeonhelper.modular.orginfo.model.bean;

/**
 * Created by Administrator on 2017/9/19.
 */

public class AlterEntity  {


    /**
     * status : 审核中
     * statusCode : 0
     * failExplain : 失败原因
     */

    private String status;
    private String statusCode;
    private String failExplain;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getFailExplain() {
        return failExplain;
    }

    public void setFailExplain(String failExplain) {
        this.failExplain = failExplain;
    }
}
