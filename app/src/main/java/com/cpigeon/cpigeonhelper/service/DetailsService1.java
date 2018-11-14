package com.cpigeon.cpigeonhelper.service;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.mina.ConnectionConfig;
import com.cpigeon.cpigeonhelper.mina.ConnectionManager;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorDialogPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter2;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.SetDialogData;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.PigeonMonitorActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.cpigeon.cpigeonhelper.utils.MinaUtil.sendMsg;

/**
 * Created by Administrator on 2018/3/1.
 */
public class DetailsService1 extends Service {

    public static Activity mContext;
    private static DetailsService1 instance;

    private ConnectionThread thread;
    public static ConnectionManager mManager;

    public static boolean isconcatenon = false;

    public class ConnectionThread extends Thread {

        @Override
        public void run() {
            for (; ; ) {
                DetailsService1.isconcatenon = mManager.connect();
                if (isconcatenon) {
                    Log.d(TAG, "连接成功跳出循环");
                    break;
                }
                try {
                    Log.d(TAG, "尝试重新连接");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void disConnect() {
        mManager.disConnect();
    }

    public static DetailsService1 getInstance(Activity mContent) {

        DetailsService1.mContext = mContent;

        if (instance == null) {
            synchronized (DetailsService1.class) {
                if (instance == null)
                    instance = new DetailsService1();
            }
        }
        return instance;
    }

    public static AMapLocationClient mAMapLocationClient;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            try {
                if (aMapLocation != null) {
                    Log.d("dingwei", "onLocationChanged: 1--> " + aMapLocation.toStr());
                    Log.d("dingwei", "onLocationCnged: 2-->" + aMapLocation.getLatitude() + "   " + aMapLocation.getLongitude());


                    //Gps 定位
                    if (aMapLocation.getLongitude() == 0 || aMapLocation.getLatitude() == 0) {
                        Log.d("dingwei", "onLocationChanged: 6--> ");
                        //获取海拔高度
                        LocationManager GpsManager = (LocationManager) DetailsService1.this.getSystemService(Context.LOCATION_SERVICE);

                        if (ActivityCompat.checkSelfPermission(DetailsService1.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailsService1.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Location location = GpsManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        Log.d("dingwei", "onLocationChanged: 7--> ");

                        if (locationTag != null) {
                            SetDialogData.lcZ = SetDialogData.lcZ + (CommonUitls.getDistance(locationTag.getLongitude(), locationTag.getLatitude(), location.getLongitude(), location.getLatitude()));
                        }


                        SetDialogData.sd = "速度：" + location.getSpeed() * 3.6 + "公里/小时";

//                        sendMsg(location.getLatitude() + "|" + location.getLongitude() + "|" +
//                                location.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
//                                MonitorPresenter2.weatherlive.getWeather() + "|" + MonitorPresenter2.weatherlive.getWindDirection() + "|" +
//                                MonitorPresenter2.weatherlive.getWindPower() + "|" + MonitorPresenter2.weatherlive.getReportTime() + "|" +
//                                MonitorPresenter2.weatherlive.getTemperature() + "|" + SetDialogData.lcZ);

                        if (location != null && MonitorData.getMonitorStateCode() == 1
                                && location.getLatitude() != 0//经度不是空
                                && location.getLatitude() != 0) {//纬度不是空
                            if (locationTag == null || (location.getLatitude() != locationTag.getLatitude() && location.getLongitude() != locationTag.getLongitude())) {
                                //向监控页面传递监控数据
                                EventBus.getDefault().post(new AMapLocation(location));

                                //数据格式为:纬度|经度|速度|定位时间戳|天气|风向|风力|天气时间|温度|里程

                                sendMsg(location.getLatitude() + "|" + location.getLongitude() + "|" +
                                        location.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
                                        MonitorPresenter2.weatherlive.getWeather() + "|" + MonitorPresenter2.weatherlive.getWindDirection() + "|" +
                                        MonitorPresenter2.weatherlive.getWindPower() + "|" + MonitorPresenter2.weatherlive.getReportTime() + "|" +
                                        MonitorPresenter2.weatherlive.getTemperature() + "|" + SetDialogData.lcZ);

                            }
                        }
                        locationTag = location;
                    }

                    if (aMapLocation.getErrorCode() == 0) {
                        if (MonitorData.getMonitorStateCode() == 1) {

                            //根据当前定位点查询天气
                            MonitorPresenter2.initWeatherSearch2(DetailsService1.this, aMapLocation, 2);

                            try {
                                SetDialogData.lcZ = SetDialogData.lcZ + (CommonUitls.getDistance(aMapLocationTag.getLongitude(), aMapLocationTag.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getLatitude()));

                                //里程
                                SetDialogData.lc = MonitorDialogPresenter.setLc(SetDialogData.lcZ);
                            } catch (Exception e) {

                            }

                            //当前坐标
                            SetDialogData.dqzb = "当前坐标：" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(aMapLocation.getLongitude())) + "E/" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(aMapLocation.getLatitude())) + "N";

                            SetDialogData.sd = "速度：" + aMapLocation.getSpeed() * 3.6 + "公里/小时";

//                            sendMsg(aMapLocation.getLatitude() + "|" + aMapLocation.getLongitude() + "|" +
//                                    aMapLocation.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
//                                    MonitorPresenter2.weatherlive.getWeather() + "|" + MonitorPresenter2.weatherlive.getWindDirection() + "|" +
//                                    MonitorPresenter2.weatherlive.getWindPower() + "|" + MonitorPresenter2.weatherlive.getReportTime() + "|" +
//                                    MonitorPresenter2.weatherlive.getTemperature() + "|" + SetDialogData.lcZ);


                            if (aMapLocation != null && MonitorData.getMonitorStateCode() == 1
                                    && aMapLocation.getLatitude() != 0//经度不是空
                                    && aMapLocation.getLatitude() != 0//纬度不是空
                                    ) {
                                if (aMapLocationTag == null || (aMapLocation.getLatitude() != aMapLocationTag.getLatitude() && aMapLocation.getLongitude() != aMapLocationTag.getLongitude())) {
                                    //向监控页面传递监控数据
                                    EventBus.getDefault().post(aMapLocation);

                                    //数据格式为:纬度|经度|速度|定位时间戳|天气|风向|风力|天气时间|温度|里程
                                    sendMsg(aMapLocation.getLatitude() + "|" + aMapLocation.getLongitude() + "|" +
                                            aMapLocation.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
                                            MonitorPresenter2.weatherlive.getWeather() + "|" + MonitorPresenter2.weatherlive.getWindDirection() + "|" +
                                            MonitorPresenter2.weatherlive.getWindPower() + "|" + MonitorPresenter2.weatherlive.getReportTime() + "|" +
                                            MonitorPresenter2.weatherlive.getTemperature() + "|" + SetDialogData.lcZ);
                                }
                            }
                            DetailsService1.this.aMapLocationTag = aMapLocation;
                        } else if (MonitorData.getMonitorStateCode() == 2) {
                            intTag1 = false;
                            DetailsService1.this.stopService(new Intent(DetailsService1.this, DetailsService2.class.getClass()));
                            DetailsService1.this.stopService(new Intent(DetailsService1.this, DetailsService1.class.getClass()));
//                            DetailsService1.this.stopService(new Intent(DetailsService1.this, CoreService.class.getClass()));
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("dingwei", "onLocationChanged:5" + e.getLocalizedMessage());
            }
        }
    };

    public AMapLocationClient getAMapLocationClient() {
        return mAMapLocationClient;
    }

    public AMapLocationListener getAMapLocationListener() {
        return mLocationListener;
    }

    public static final String TAG = "CoreService";

    public static boolean isStartLocate = false;//是否开始定位

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        ConnectionConfig config = new ConnectionConfig.Builder(getApplicationContext())
                .setIp(ApiConstants.BASE_IP)//连接的IP地址
                .setPort(5555)//连接的端口号
                .setReadBufferSize(1024)
                .setConnectionTimeout(10000).builder();
        mManager = new ConnectionManager(config);
//        mManager = ConnectionManager.getInstance(config);
        thread = new ConnectionThread();
        thread.start();

        showNotification(this, 1231232, "中鸽助手", "正在监控中");

        if (mAMapLocationClient == null && isStartLocate) {
            mAMapLocationClient = new AMapLocationClient(this);

            Log.d("printsss", "onStartCommand: aaa");
            mAMapLocationClient = MonitorPresenter.initLocalize(mAMapLocationClient, this, mLocationListener, new AMapLocationClientOption());// 初始化定位

        }

        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //延迟一秒发送一个空消息
        intTag1 = true;
        handler.sendEmptyMessageDelayed(0x001, 1000);
        return START_STICKY_COMPATIBILITY;
    }

    private Notification.Builder builder;
    private Intent resultIntent;

    private void showNotification(Context context, int id, String title, String text) {
        builder = new Notification.Builder(context);

        builder.setSmallIcon(R.mipmap.cpigeon_logo);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        resultIntent = new Intent(context, PigeonMonitorActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(id, builder.build());

        startForeground(id, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static AMapLocation aMapLocationTag;//保存上一次经纬度位置
    public static Location locationTag;//保存上一次经纬度位置(Gps定位)


    public static boolean intTag1 = true;//是否停止服务  true  不停止  false  停止
    private PowerManager.WakeLock wakeLock;


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            intTag1 = true;
            acquireWakeLock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            acquireWakeLock();
//            disConnect();
            intTag1 = false;
//            this.startService(new Intent(this, DetailsService1.class.getClass()));

            if (mAMapLocationClient != null) {
                mAMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                mAMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                mAMapLocationClient = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获得活动管理器的对象
    private ActivityManager activityManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                acquireWakeLock();
                startSelf1();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private List<ActivityManager.RunningServiceInfo> runningServices;

    public void startSelf1() {
//        Log.d("print1", "判断service2是否存活");
        try {
            boolean isRun = false;
            boolean isMinaServiceRun = false;
            runningServices = activityManager.getRunningServices(1000);//获得前100个正在运行的Service服务
            for (ActivityManager.RunningServiceInfo runningService : runningServices) {
                if (runningService.service.getClassName().equals(DetailsService2.class.getName())) {
                    isRun = true;
                    break;
                }
            }

            if (!isRun) {
                //如果没有发现本服务在运行，则启动该服务
                if (intTag1) {
                    Log.d("print1", "启动服务2");
                    this.startService(new Intent(this, DetailsService2.class.getClass()));
                } else {
                    //   Log.d("print1", "2停止服务");
                    if (wakeLock != null) {
                        wakeLock.release();
                        wakeLock = null;
                    }

                    if (mAMapLocationClient != null) {
                        mAMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        mAMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        mAMapLocationClient = null;
                    }

                    disConnect();
                    thread = null;

                    this.stopService(new Intent(this, DetailsService2.class.getClass()));
                }
            } else {
                //如果服务还存活
                Log.d("print1", "服务2正在运行");
                if (intTag1) {
                    handler.sendEmptyMessageDelayed(0x001, 1000);
                } else {
                    //                Log.d("print1", "2停止服务");
                    if (wakeLock != null) {
                        wakeLock.release();
                        wakeLock = null;
                    }

                    disConnect();
                    thread = null;

                    if (mAMapLocationClient != null) {
                        mAMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        mAMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                        mAMapLocationClient = null;
                    }

                    this.stopService(new Intent(this, DetailsService2.class.getClass()));
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private PowerManager pm;

    private void acquireWakeLock() {
        if (null == wakeLock) {
            pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    private void releaseWakeLock() {
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}