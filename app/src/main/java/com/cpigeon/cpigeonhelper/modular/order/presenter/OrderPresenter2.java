package com.cpigeon.cpigeonhelper.modular.order.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.utils.DateUtils;

/**
 * Created by Administrator on 2018/1/17.
 */

public class OrderPresenter2 {


    public static Thread startCountdown(Thread thread,Activity mActivity, long orderTime, TextView playCountdown) {
        long timePoor1 = System.currentTimeMillis() - orderTime;

        long timePoor = 2 * 60 * 60 * 1000 - timePoor1;//时间差:

//        Log.d(TAG, "当前系统毫秒: " + System.currentTimeMillis());
//        Log.d(TAG, "订单时间毫秒数: " + orderTime);
//        Log.d(TAG, "时间差: " + timePoor);

        if (timePoor1 > 24 * 60 * 60 * 1000) {
            playCountdown.setText("已失效");
            return null;
        } else {
            thread = new Thread(startThread(mActivity, timePoor, playCountdown));
            thread.start();
        }
        return thread;
    }

    public static Runnable startThread(Activity mActivity, long timePoor, TextView playCountdown) {

        return new Runnable() {//支付倒计时
            @Override
            public void run() {

                try {
                    //设置支付倒计时
                    for (long i = timePoor; i > 0; i = i - 1000) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                            Log.d("myTime", "run: 171717");
                            e.printStackTrace();
                            break;
                        }

                        long a = i;
                        mActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
    //                        playCountdown.setText(a + "");

                                Log.d("myTime", "run: 121212");
                                if (playCountdown != null) {
                                    playCountdown.setText(DateUtils.msToHsm1(a));
                                }
                            }
                        });
                    }

                    //倒计时结束
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("myTime", "run: 343434");
    //                        playCountdown.setText(a + "");
                            if (playCountdown != null) {
                                playCountdown.setText("支付已失效");
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.d("myTime", "run: 异常--》"+e.getLocalizedMessage());
                }
            }
        };
    }

}
