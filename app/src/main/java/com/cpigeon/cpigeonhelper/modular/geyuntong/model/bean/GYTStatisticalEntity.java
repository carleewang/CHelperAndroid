package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

/**
 * 鸽运通 统计信息实体类
 * Created by Administrator on 2017/9/20.
 */

public class GYTStatisticalEntity {


    /**
     * TotalMileage : 1510.17
     * TotalSeconds : 78815
     * MaxSpeed : 3
     * MonitorRaceCount : 4
     * AvgSpeed : 0.019160946520332425
     */

    private double TotalMileage;//总里程
    private int TotalSeconds;//总秒
    private int MaxSpeed;//最高速度
    private int MonitorRaceCount;//监控比赛数
    private double AvgSpeed;//平均车速

    public double getTotalMileage() {
        return TotalMileage;
    }

    public void setTotalMileage(double TotalMileage) {
        this.TotalMileage = TotalMileage;
    }

    public int getTotalSeconds() {
        return TotalSeconds;
    }

    public void setTotalSeconds(int TotalSeconds) {
        this.TotalSeconds = TotalSeconds;
    }

    public int getMaxSpeed() {
        return MaxSpeed;
    }

    public void setMaxSpeed(int MaxSpeed) {
        this.MaxSpeed = MaxSpeed;
    }

    public int getMonitorRaceCount() {
        return MonitorRaceCount;
    }

    public void setMonitorRaceCount(int MonitorRaceCount) {
        this.MonitorRaceCount = MonitorRaceCount;
    }

    public double getAvgSpeed() {
        return AvgSpeed;
    }

    public void setAvgSpeed(double AvgSpeed) {
        this.AvgSpeed = AvgSpeed;
    }
}
