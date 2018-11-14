package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import io.realm.RealmObject;

/**
 * 鸽运通的bean
 * Created by Administrator on 2017/6/30.
 */

public class GYTService extends RealmObject {

    /**
     * grade : svip
     * authNumber : 0
     * openTime : 2017-06-22 16:09:21
     * reason :
     * isClosed : false
     * usefulTime : 2017-07-30 16:21:20
     * isExpired : false
     * expireTime : 2017-07-30 16:09:30
     */

    private String grade;//用户等级
    private int authNumber;//授权次数
    private String openTime;//开通时间
    private String reason;//关闭原因
    private boolean isClosed;//是否已关闭
    private String usefulTime;//使用时间
    private boolean isExpired;//是否到期
    private String expireTime;//到期时间
    private String scores;//鸽币

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getAuthNumber() {
        return authNumber;
    }

    public void setAuthNumber(int authNumber) {
        this.authNumber = authNumber;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public String getUsefulTime() {
        return usefulTime;
    }

    public void setUsefulTime(String usefulTime) {
        this.usefulTime = usefulTime;
    }

    public boolean isIsExpired() {
        return isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

}
