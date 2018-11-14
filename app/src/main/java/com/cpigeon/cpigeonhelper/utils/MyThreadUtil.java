package com.cpigeon.cpigeonhelper.utils;

import android.app.Activity;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;

/**
 * Created by Administrator on 2018/1/3.
 */

public class MyThreadUtil {

    //获取验证码  倒计时
    public static Runnable getYzm(TextView ingBtnValidation, Activity mContext) {

        return new Runnable() {
            @Override
            public void run() {
                mContext.runOnUiThread(() -> {
                    ingBtnValidation.setBackgroundColor(mContext.getResources().getColor(R.color.color_yzm_bg));
                    ingBtnValidation.setClickable(false);
                });

                for (int i = 300; i > 0; i--) {
                    try {
                        Thread.sleep(1000);
                        int finalI = i;
                        mContext.runOnUiThread(() -> {
                            ingBtnValidation.setText(finalI + "秒后获取");
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mContext.runOnUiThread(() -> {
                    //结束
                    ingBtnValidation.setText("获取验证码");
                    ingBtnValidation.setBackgroundColor(mContext.getResources().getColor(R.color.color_theme));
                    ingBtnValidation.setClickable(true);
                });
            }
        };
    }

}
