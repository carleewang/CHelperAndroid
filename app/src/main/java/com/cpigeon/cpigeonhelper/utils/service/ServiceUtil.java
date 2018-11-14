package com.cpigeon.cpigeonhelper.utils.service;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ServiceUtil {

    // 判断服务是否开启
    public static boolean isServiceAlive(Context context,
                                         String serviceClassName) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> running = manager
                .getRunningServices(200);

        for (int i = 0; i < running.size(); i++) {
            if (serviceClassName.equals(running.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
