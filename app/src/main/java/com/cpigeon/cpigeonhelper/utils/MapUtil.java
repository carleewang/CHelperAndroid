package com.cpigeon.cpigeonhelper.utils;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 地图工具类
 * Created by Administrator on 2017/11/17.
 */
public class MapUtil {


    /**
     * 初始化定位 (定位一次)
     *
     * @param savedInstanceState
     */
    public static void initLocation(Bundle savedInstanceState, MapView mMapView, AMap aMap, AMapLocationClient mLocationClient, AMapLocationListener mLocationListener, AMapLocationClientOption mLocationOption, MyLocationStyle myLocationStyle) {
        //初始化定位
        mLocationClient = new AMapLocationClient(MyApplication.getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        }

        //设置蓝点
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        startLocate(mLocationOption, mLocationClient, mLocationListener);//开始定位当前位置
    }

    /**
     * 开始定位当前位置
     */
    public static AMapLocationClient startLocate(AMapLocationClientOption mLocationOption, AMapLocationClient mLocationClient, AMapLocationListener mLocationListener) {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //初始化定位
        mLocationClient = new AMapLocationClient(MyApplication.getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        return mLocationClient;
    }

    /**
     * 开始定位当前位置
     */
    public static AMapLocationClient startLocate1(Context mContext, AMapLocationListener mLocationListener) {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //初始化定位
        AMapLocationClient mLocationClient = new AMapLocationClient(mContext);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //启动定位
        mLocationClient.startLocation();

        return mLocationClient;
    }


    /**
     * 天气查询
     *
     * @param city 城市
     */
    public static WeatherSearch initWeatherSearch(Context mContext, String city,
                                                  WeatherSearch.OnWeatherSearchListener onWeatherSearchListener) {

        WeatherSearch mweathersearch = new WeatherSearch(mContext);
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        WeatherSearchQuery mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);


        mweathersearch.setOnWeatherSearchListener(onWeatherSearchListener);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
        return mweathersearch;
    }


    /**
     * 无视图持续定位
     */

    public static void noViewContinuousLocalize(AMapLocationClient mLocationClient, AMapLocationListener mLocationListener) {

        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();


        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);


        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(4000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    public static Map<String, Integer> initIcMap1() {
        Map<String, Integer> icMap1 = new HashMap<>();
        icMap1.put("阵雨", R.drawable.ic_weather_white_a_shower_b);
        icMap1.put("多云", R.drawable.ic_weather_white_cloudy_b);
        icMap1.put("大雨", R.drawable.ic_weather_white_heavy_rain_b);
        icMap1.put("中雨", R.drawable.ic_weather_white_moderate_rain_b);
        icMap1.put("小雨", R.drawable.ic_weather_white_light_rain_b);

        icMap1.put("小雪", R.drawable.ic_weather_light_snow_b);
        icMap1.put("中雪", R.drawable.ic_weather_light_snow_b);
        icMap1.put("大雪", R.drawable.ic_weather_light_snow_b);

        icMap1.put("雨夹雪", R.drawable.ic_weather_white_sleet_b);
        icMap1.put("霾", R.drawable.ic_weather_white_smog_b);
        icMap1.put("晴", R.drawable.ic_weather_white_sunny_b);
        icMap1.put("雷阵雨", R.drawable.ic_weather_white_thunder_shower_b);
        icMap1.put("阴", R.drawable.ic_weather_white_yin_b);

        icMap1.put("未知", R.drawable.ic_weather_unknown_b);

        return icMap1;
    }

    public static Map<String, Integer> initIcMap2() {
        Map<String, Integer> icMap2 = new HashMap<>();

        icMap2.put("阵雨", R.drawable.ic_weather_white_a_shower_l);
        icMap2.put("多云", R.drawable.ic_weather_white_cloudy_l);

        icMap2.put("大雨", R.drawable.ic_weather_white_heavy_rain_l);
        icMap2.put("中雨", R.drawable.ic_weather_white_moderate_rain_l);
        icMap2.put("小雨", R.drawable.ic_weather_white_light_rain_l);

        icMap2.put("小雪", R.drawable.ic_weather_light_snow_l);
        icMap2.put("中雪", R.drawable.ic_weather_light_snow_l);
        icMap2.put("大雪", R.drawable.ic_weather_light_snow_l);

        icMap2.put("雨夹雪", R.drawable.ic_weather_white_sleet_l);
        icMap2.put("霾", R.drawable.ic_weather_white_smog_l);
        icMap2.put("晴", R.drawable.ic_weather_white_sunny_l);
        icMap2.put("雷阵雨", R.drawable.ic_weather_white_thunder_shower_l);
        icMap2.put("阴", R.drawable.ic_weather_white_yin_l);

        icMap2.put("未知", R.drawable.ic_weather_unknown_l);

        return icMap2;
    }
}
