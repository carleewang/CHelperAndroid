package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.cpigeon.cpigeonhelper.utils.CommonUitls.getDistance;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MonitorDialogPresenter {


    /**
     * 设置里程
     *
     * @param datas
     */
    public static String setLc(List<LocationInfoEntity> datas, String strLc, TextView dialogMiles) {
        double lc = 0;
        if (datas.size() > 2) {
            for (int i = 0; i < datas.size() - 1; i++) {
                lc += getDistance(datas.get(i).getLo(), datas.get(i).getLa(),
                        datas.get(i + 1).getLo(), datas.get(i + 1).getLa());
            }
        }

        BigDecimal b = new BigDecimal(lc);
        if (lc > 1000) {
//            dialogMiles.setText("里程：" + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 1000 + " km");
            strLc = "里程：" + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 1000 + " 公里";
        } else if (lc > 0) {
//            dialogMiles.setText("里程：" + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + " m");
            strLc = "里程：" + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + " 米";
        } else {
            strLc = "里程：0 公里";
        }

        if (MonitorData.getMonitorStateCode() == 2) {//监控结束
            dialogMiles.setText(strLc);
        }
        return strLc;
    }

    /**
     * 设置里程
     *
     * @param datas
     */
    public static String setLc(double datas) {
        String strLc = "里程：0 公里";
        BigDecimal b = new BigDecimal(datas);
        if (datas > 1000) {
            strLc = "里程：" + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 1000 + " 公里";
        } else if (datas > 0) {
            strLc = "里程：" + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + " 米";
        } else {
            strLc = "里程：0 公里";
        }
        return strLc;
    }

    /**
     * 设置空距
     */
    public static String setUllage(List<LocationInfoEntity> datas, String strKj, TextView dialogUllage) {
        if (datas.size() > 0 && MonitorData.getMonitorFlyLo() != 0 && MonitorData.getMonitorFlyLa() != 0) {//有监控数据并且 司放地有经纬度
            double m = getDistance(datas.get(0).getLo(), datas.get(0).getLa(), MonitorData.getMonitorFlyLo(), MonitorData.getMonitorFlyLa());
            BigDecimal b = new BigDecimal(m);

            if (m > 1000) {
                strKj = "空距：" + CommonUitls.strThree(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 1000) + " 公里";
            } else {
                strKj = "空距：" + CommonUitls.strTwo(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + " 米";
            }
        } else {
            strKj = "空距：0 公里";
        }

        if (MonitorData.getMonitorStateCode() == 2) {//监控结束
            dialogUllage.setText(strKj);//设置空距
        }
        return strKj;
    }


    /**
     * 设置空距
     */
    public static String setUllage(double datas) {
        String strKj = "空距：0 公里";
        if (datas != 0 && MonitorData.getMonitorFlyLo() != 0 && MonitorData.getMonitorFlyLa() != 0) {//有监控数据并且 司放地有经纬度
            strKj = "空距：" + datas + " 公里";
        } else {
            strKj = "空距：0 公里";
        }
        return strKj;
    }

    /**
     * 监控时长 动态设置
     *
     * @param dialog
     * @param mThread
     * @param handler
     * @param monitorTime
     * @return
     */
    public static int a = 0;
    public static int b = 0;
    public static int c = 0;

    public static Runnable myTharad(AlertDialog dialog, Thread mThread, Handler handler,
                                    TextView monitorTime,
                                    TextView lc,
                                    TextView kj,
                                    TextView dqzb,
                                    TextView dqtq,
                                    TextView dialogSpeed) {//速度
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    if (!dialog.isShowing()) {//dialog没有显示
                        if (mThread != null) {
                            mThread.interrupt();//线程结束
                        }
                    }

                    //延迟一秒更新
                    try {
                        Thread.sleep(1000 * 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;//捕获到异常之后，执行break跳出循环。
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (MonitorData.getMonitorStateCode() == 1 || MonitorData.getMonitorStateCode() == 3) {//正在监控
                                if (MonitorData.getMonitorMTime().equals("")) {
                                    SetDialogData.jksc = "已监控：暂无";
                                    monitorTime.setText(SetDialogData.jksc);

                                    lc.setText(SetDialogData.lc);//里程
                                    kj.setText(SetDialogData.kj);//空距

                                    dqzb.setText(SetDialogData.dqzb);//当前坐标
                                    dqtq.setText(SetDialogData.dqtq);//当前天气

                                    dialogSpeed.setText(SetDialogData.sd);//速度
                                } else {
                                    //设置时间   【时分秒】  转化 （【毫秒数】结束时间-开始时间）
                                    SetDialogData.jksc = "已监控：" + DateUtils.msToHsm1(System.currentTimeMillis() - DateUtils.DataToMs(MonitorData.getMonitorMTime()));
                                    monitorTime.setText(SetDialogData.jksc);


                                    lc.setText(SetDialogData.lc);//里程
                                    kj.setText(SetDialogData.kj);//空距


                                    dqzb.setText(SetDialogData.dqzb);//当前坐标
                                    dqtq.setText(SetDialogData.dqtq);//当前天气
                                    dialogSpeed.setText(SetDialogData.sd);//速度
                                }
                            } else if (MonitorData.getMonitorStateCode() == 2) {//监控结束
                                if (mThread != null && mThread.isAlive()) {//线程正在运行中
                                    mThread.interrupt();//线程结束
                                }
                            }
                            Log.d("mytimes", "run: 线程启动  5");
                        }
                    });
                }
            }
        };
        return mRunnable;
    }

    /**
     * 初始化dialog
     */
    public static AlertDialog initDialog(Context mContext, AlertDialog.Builder builder, AlertDialog dialog, LinearLayout dialogLayout) {
        //创建title控件
        TextView title = new TextView(mContext);
        title.setText("详细信息");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(mContext.getResources().getColor(R.color.black));
        title.setTextSize(18);

        builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();
        dialog.setCustomTitle(title);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setView(dialogLayout);
        //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
