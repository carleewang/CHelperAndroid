package com.cpigeon.cpigeonhelper.utils.throwable;

import android.app.Activity;
import android.content.Context;

import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2018/2/26.
 */

public class ThrowableUtil {

    /**
     * hl 抛出异常
     *
     * @param context
     * @param throwable
     */
    public static String exceptionHandling(Context context, Throwable throwable) {

        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) return "";
        } else {
            if (context == null) return "";
        }

        if (throwable instanceof SocketTimeoutException) {
            return "连接超时";
        } else if (throwable instanceof ConnectException) {
            return "无法连接到服务器，请检查连接";
        } else if (throwable instanceof RuntimeException) {
            CommonUitls.showSweetDialog1(context, "发生错误:" + throwable.getMessage(), dialog -> {
                dialog.dismiss();
            });

            return "发生错误:" + throwable.getMessage();
        }
        return "";

    }
}
