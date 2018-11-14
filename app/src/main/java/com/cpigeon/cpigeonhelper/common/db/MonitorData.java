package com.cpigeon.cpigeonhelper.common.db;

import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTongs;

import io.realm.RealmResults;

/**
 * 监控数据管理类
 * Created by Administrator on 2017/10/11.
 */

public class MonitorData {

    public static RealmResults<GeYunTongs> info = RealmUtils.getInstance().queryGYTBeanInfo();

    /**
     * 判断是否存在监控数据
     *
     * @return
     */
    public static boolean checkIsMonitorData() {

        for (GeYunTongs gytBean : info) {
            if (gytBean.getId() != -1) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * 获取比赛的赛事id
     */
    public static int getMonitorId() {
        for (GeYunTongs monBean : info) {
            if (monBean.getId() != -1) {
                return monBean.getId();
            }
        }
        return -1;
    }

    /**
     * 获取比赛
     */
    public static GeYunTongs getMonitorGeYunTongs() {
        for (GeYunTongs monBean : info) {
            if (monBean.getId() != -1) {
                return monBean;
            }
        }
        return null;
    }

    /**
     * 获取比赛的状态码
     * //StateCode【0：未开始监控的；1：正在监控中：2：监控结束  3：授权人查看比赛】【默认无筛选】
     */
    public static int getMonitorStateCode() {
        for (GeYunTongs monBean : info) {
            if (monBean.getStateCode() != -1) {
                return monBean.getStateCode();
            }
        }

        return -1;
    }


    //获取监控授权状态
    public static int getMonitorStateSq() {
        for (GeYunTongs monBean : info) {
            if (monBean.getStateSq() != -1) {
                return monBean.getStateSq();
            }
        }
        return -1;
    }

    /**
     * 获取比赛的结束时间
     */
    public static String getMonitorMEndTime() {
        for (GeYunTongs monBean : info) {
            if (!monBean.getmEndTime().equals("")) {
                return monBean.getmEndTime();
            }
        }
        return "0";
    }

    /**
     * 获取比赛的启动时间
     *
     * @return
     */
    public static String getMonitorMTime() {
        for (GeYunTongs monBean : info) {
            if (!monBean.getmTime().equals("")) {
                return monBean.getmTime();
            }
        }
        return "";
    }

    /**
     * 获取比赛司放地经度坐标
     *
     * @return
     */
    public static double getMonitorFlyLo() {
        for (GeYunTongs monBean : info) {
            if (monBean.getLongitude() != 0) {
                return monBean.getLongitude();
            }
        }
        return 0;
    }

    /**
     * 获取比赛司放地纬度坐标
     *
     * @return
     */
    public static double getMonitorFlyLa() {
        for (GeYunTongs monBean : info) {
            if (monBean.getLatitude() != 0) {
                return monBean.getLatitude();
            }
        }
        return 0;
    }

    /**
     * 获取司放地点
     *
     * @return
     */
    public static String getMonitorFlyingArea() {
        for (GeYunTongs monBean : info) {
            if (!monBean.getFlyingArea().equals("")) {
                return monBean.getFlyingArea();
            }
        }
        return "";
    }

    /**
     * 获取比赛的监控状态
     *
     * @return
     */
    public static String getMonitorState() {
        for (GeYunTongs monBean : info) {
            if (!monBean.getmTime().equals("")) {
                return monBean.getState();
            }
        }
        return "无状态";
    }

    /**
     * 获取赛事名称
     */
    public static String getMonitorRaceName() {
        if (info.size() == 0) {
            return "鸽车监控";
        }
        for (GeYunTongs monBean : info) {
            if (monBean != null && monBean.getRaceName() != null) {
                return monBean.getRaceName();
            }
        }
        return "鸽车监控";
    }
}
