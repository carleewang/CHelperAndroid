package com.cpigeon.cpigeonhelper.service.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter2;
import com.cpigeon.cpigeonhelper.utils.LocationManager;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/3/20.
 */

@SuppressLint("NewApi")
public class SendJobService extends JobService {

    private JobScheduler mJobScheduler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.print("service: onStartCommand");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++,
                    new ComponentName(getPackageName(), SendJobService.class.getName()));

            builder.setPeriodic(3000); //每隔5秒运行一次
            builder.setRequiresCharging(true);
            builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
            builder.setRequiresDeviceIdle(true);

            if (mJobScheduler.schedule(builder.build()) <= JobScheduler.RESULT_FAILURE) {
                //If something goes wrong
                LogUtil.print("工作失败");
            } else {
                LogUtil.print("工作成功");
            }

            return super.onStartCommand(intent, flags, startId);
        }

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.print("service: onStartJob");

//        if (isServiceRunning(this, CoreService.class.getName())){
//            startService(new Intent(this, CoreService.class));
//        }

        LocationManager.getInstalls(this).setLocationListener(aMapLocation -> {
            LogUtil.print(aMapLocation.toString());
            MonitorPresenter2.initWeatherSearch2(this, aMapLocation, 2);

            /*LogUtil.print("service: "+aMapLocation.getLatitude() + "|" + aMapLocation.getLongitude() + "|" +
                    aMapLocation.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
                    ""+ "|" +  "" + "|" +
                    "" + "|" +  "" + "|" +
                    "" + "|" + SetDialogData.lcZ);
            sendMsg(aMapLocation.getLatitude() + "|" + aMapLocation.getLongitude() + "|" +
                    aMapLocation.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
                    ""+ "|" +  "" + "|" +
                    "" + "|" +  "" + "|" +
                    "" + "|" + SetDialogData.lcZ);*/
        }).star();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtil.print("service: stop");
        return false;
    }

    // 服务是否运行
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();


        for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;

    }
}
