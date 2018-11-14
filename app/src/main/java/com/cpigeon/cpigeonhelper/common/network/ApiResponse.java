package com.cpigeon.cpigeonhelper.common.network;

import com.cpigeon.cpigeonhelper.utils.http.GsonUtil;

/**
 *封装Retrofit的Callback
 * Created by Administrator on 2017/5/25.
 */

public class ApiResponse<T> {
    public boolean status;
    public int errorCode;
    public String msg;
    public T data;

    public boolean isStatus() {
        return status;
    }

    public boolean isOk(){
        return errorCode != -1 ;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isHaveDate(){
        return isOk() && status;
    }


    public String toJsonString() {
        return GsonUtil.toJson(this);
    }
}