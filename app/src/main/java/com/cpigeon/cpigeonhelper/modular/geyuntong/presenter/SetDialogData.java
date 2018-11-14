package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.widget.TextView;

import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.utils.DateUtils;

/**
 * 设置位置信息 dialog
 * Created by Administrator on 2018/2/27.
 */

public class SetDialogData {

    public static double lcZ = 0;//总里程
    //dialog显示内容
    public static String jksc = "已监控：暂无数据";//监控时长
    public static String sfdzb = "司放地坐标：用户未设置";//司放地坐标
    public static String sfdtq = "司放地天气：暂无数据";//司放地天气
    public static String dqzb = "当前坐标：暂无数据";//当前坐标
    public static String dqtq = "当前天气：暂无数据";//当前天气
    public static String kj = "空距：0 公里";//空距
    public static String lc = "里程：0 公里";//里程
    public static String sd = "速度：0米/秒";//速度

    public static void initDialogData() {
        SetDialogData.jksc = "已监控：暂无数据";//监控时长
        SetDialogData.sfdzb = "司放地坐标：用户未设置";//司放地坐标
        SetDialogData.sfdtq = "司放地天气：暂无";//司放地天气
        SetDialogData.dqzb = "当前坐标：暂无数据";//当前坐标
        SetDialogData.dqtq = "当前天气：暂无数据";//当前天气
        SetDialogData.kj = "空距：0 公里";//空距
        SetDialogData.lc = "里程：0 公里";//里程
        SetDialogData.sd = "速度：0米/秒";//速度
    }

    public static void showDialogData(TextView jkzt, TextView jksc, TextView sfdzb, TextView sfdtq, TextView dqzb, TextView dqtq, TextView kj, TextView lc, TextView sd) {

        //设置监控状态
        if (MonitorData.getMonitorState().equals("监控中")) {
            jkzt.setText("正在监控...");//dialog设置监控状态
        } else {
            jkzt.setText(MonitorData.getMonitorState() + "...");//dialog设置监控状态
        }

        if (MonitorData.getMonitorStateCode() == 2) {//监控结束，设置总时长
            if (!MonitorData.getMonitorMTime().isEmpty() && !MonitorData.getMonitorMEndTime().isEmpty()) {
                //设置时间   【时分秒】  转化 （【毫秒数】结束时间-开始时间）
                jksc.setText("已监控：" + DateUtils.msToHsm1(DateUtils.DataToMs(MonitorData.getMonitorMEndTime()) - DateUtils.DataToMs(MonitorData.getMonitorMTime())));
            }
        }

        sfdzb.setText(SetDialogData.sfdzb);
        sfdtq.setText(SetDialogData.sfdtq);
        dqzb.setText(SetDialogData.dqzb);
        dqtq.setText(SetDialogData.dqtq);
        kj.setText(SetDialogData.kj);
        lc.setText(SetDialogData.lc);
        sd.setText(SetDialogData.sd);
    }
}
