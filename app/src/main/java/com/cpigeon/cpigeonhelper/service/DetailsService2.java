package com.cpigeon.cpigeonhelper.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.PigeonMonitorActivity;

import java.util.List;

import static com.cpigeon.cpigeonhelper.service.DetailsService1.intTag1;

/**
 * Created by Administrator on 2018/3/1.
 */

public class DetailsService2 extends Service {

    public static Activity mContext;
    private static DetailsService2 instance;


    public static DetailsService2 getInstance(Activity mContent) {

        DetailsService2.mContext = mContent;

        if (instance == null) {
            synchronized (DetailsService2.class) {
                if (instance == null)
                    instance = new DetailsService2();
            }
        }
        return instance;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //延迟一秒发送一个空消息
        handler.sendEmptyMessageDelayed(0x001, 1000);
        intTag1 = true;
        return START_STICKY_COMPATIBILITY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        intTag1 = true;
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, DetailsService2.class.getName());
        wakeLock.acquire();


    }

    //获得活动管理器的对象
    private ActivityManager activityManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startSelf2();
        }
    };

    private List<ActivityManager.RunningServiceInfo> runningServices;

    public void startSelf2() {
//        Log.d("print2", "判断service1是否存活");
        try {
            boolean isRun = false;
            boolean isRunCoreService = false;
            runningServices = activityManager.getRunningServices(1000);//获得前100个正在运行的Service服务

            try {
                for (ActivityManager.RunningServiceInfo runningService : runningServices) {
                    if (runningService.service.getClassName().equals(DetailsService1.class.getName())) {
                        isRun = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!isRun) {
                //如果没有发现本服务在运行，则启动该服务
                if (DetailsService1.intTag1) {
                    Log.d("print2", "启动服务1");
                    this.startService(new Intent(this, DetailsService1.class.getClass()));

                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        ComponentName cn = new ComponentName("com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity", String.valueOf(PigeonMonitorActivity.class.getName()));
                        intent.setComponent(cn);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //                Log.d("print2", "1停止服务");


                    if (DetailsService1.mAMapLocationClient != null) {
                        DetailsService1.mAMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        DetailsService1.mAMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        DetailsService1.mAMapLocationClient = null;
                    }

                    this.stopService(new Intent(this, DetailsService1.class.getClass()));
                }
            } else {
                //如果服务还存活
                Log.d("print2", "服务1正在运行");
                if (DetailsService1.intTag1) {
                    handler.sendEmptyMessageDelayed(0x001, 1000);
                } else {

                    if (DetailsService1.mAMapLocationClient != null) {
                        DetailsService1.mAMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        DetailsService1.mAMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        DetailsService1.mAMapLocationClient = null;
                    }
                    //                Log.d("print2", "1停止服务");
                    this.stopService(new Intent(this, DetailsService1.class.getClass()));
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
