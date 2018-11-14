package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MonitorPresenter2 {

    /**
     * 天气查询
     * @param city 城市
     * @param type 类型（1：司放地  2  当前位置）
     */
    public static LocalWeatherLive weatherlive;
    public static WeatherSearchQuery mquery;
    public static WeatherSearch mweathersearch;

    public static void initWeatherSearch2(Context mContext, AMapLocation aMapLocation, int type) {

        try {
            //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
            mquery = new WeatherSearchQuery(aMapLocation.getCity(), WeatherSearchQuery.WEATHER_TYPE_LIVE);
            mweathersearch = new WeatherSearch(mContext);
            mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
                @Override
                public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
                    try {
                        if (rCode == 1000) {
                            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                                weatherlive = weatherLiveResult.getLiveResult();
                                //StateCode【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
                                switch (type) {
                                    case 1:
                                        SetDialogData.sfdtq = "司放地天气：" + weatherlive.getWeather() + "  " + weatherlive.getTemperature() + "℃  " + weatherlive.getWindDirection() + "风";
                                        Log.d("dingwei", "司放地天气: " + SetDialogData.sfdtq);
                                        break;
                                    case 2:
                                        SetDialogData.dqtq = "当前天气：" + weatherlive.getWeather() + "  " + weatherlive.getTemperature() + "℃  " + weatherlive.getWindDirection() + "风";
                                        Log.d("dingwei", "当前天气: " + SetDialogData.dqtq);
                                        break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int rCode) {

                }
            });
            mweathersearch.setQuery(mquery);
            mweathersearch.searchWeatherAsyn(); //异步搜索
        } catch (Exception e) {
            Log.d("dingweitianqi", "initWeatherSearch2: 12");
            e.printStackTrace();
        }
    }


    /**
     * 定位监听回到后执行方法
     *
     * @param aMapLocation
     */

    public static void locationClientListener2(AMapLocation aMapLocation, AMapLocation aMapLocationTag) {
        try {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    if (aMapLocationTag != null) {
//                        double ss = getDistance(aMapLocation.getLongitude(), aMapLocation.getLatitude(),
//                                aMapLocationTag.getLongitude(), aMapLocationTag.getLatitude());
//                        if ((ss / 4) > 1) {
//                            SetDialogData.sd = "速度：" + CommonUitls.strTwo(ss / 4) + "米/秒";
//                        } else {
//                            SetDialogData.sd = "速度：0 公里/小时";
//                        }
                        SetDialogData.dqzb = "当前坐标：" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(aMapLocation.getLongitude())) + "E/" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(aMapLocation.getLatitude())) + "N";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监控优化
    //1、判断应用是否已经为白名单
    public static boolean isIgnoringBatteryOptimizations(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(packageName);
        }
        return false;
    }


    //2、否的话，则提示用户开启
    public final static int REQUEST_IGNORE_BATTERY_CODE = 1001;

    public static void gotoSettingIgnoringBatteryOptimizations(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = getPackageName();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                mActivity.startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
