package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public class GYTHomeEntity {


    /**
     * grade : svip
     * authNumber : 2
     * openTime : 2017-07-05
     * reason : null
     * isClosed : false
     * usefulTime : 2018-07-10
     * isExpired : false
     * grades : [{"authNumber":2,"beginTime":"2017-07-10","grade":"svip","endTime":"2018-07-10"},{"authNumber":0,"beginTime":"2018-07-10","grade":"","endTime":"2019-07-10"}]
     * expireTime : 2019-07-10
     */

    private String grade = "-1";//用户等级
    private int authNumber;//授权次数
    private String openTime;//开通时间
    private String reason;//关闭原因
    private boolean isClosed;//是否已关闭
    private String usefulTime;//使用时间
    private boolean isExpired;//是否到期
    private String expireTime;//到期时间
    private String scores;//鸽币
    private List<GradesBean> grades;

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

    public List<GradesBean> getGrades() {
        return grades;
    }

    public void setGrades(List<GradesBean> grades) {
        this.grades = grades;
    }

    public static class GradesBean {
        /**
         * authNumber : 2
         * beginTime : 2017-07-10
         * grade : svip
         * endTime : 2018-07-10
         */

        private int authNumber;//授权个数
        private String beginTime;//开始监控时间
        private String grade;//用户等级
        private String endTime;//结束监控时间

        public int getAuthNumber() {
            return authNumber;
        }

        public void setAuthNumber(int authNumber) {
            this.authNumber = authNumber;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
