package com.cpigeon.cpigeonhelper.modular.usercenter.model.bean;

/**
 * Created by Administrator on 2017/9/9.
 */

public class LogInfoBean {


    /**
     * status : true
     * errorCode : 0
     * msg :
     * data : {"deviceid":"422cff4fd9552061797b1809a133932f"}
     */

    private boolean status;
    private int errorCode;
    private String msg;
    private String deviceid;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    /**
     * deviceid : 422cff4fd9552061797b1809a133932f
     */



    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

}
