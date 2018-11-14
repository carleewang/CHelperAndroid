package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.PigeonMonitorActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;
import com.cpigeon.cpigeonhelper.utils.OfflineFileManager;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MonitorPresenter {

    /**
     * 初始化定位
     */
    public static AMapLocationClient initLocalize(AMapLocationClient mLocationClient, Context mContext, AMapLocationListener mLocationListener,
                                                  AMapLocationClientOption mLocationOption) {

        //初始化定位
//        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());


        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //(单次定位)获取一次定位结果：
        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //（连续定位）设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(5000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(8000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);

        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//        mLocationClient.stopLocation();
        mLocationClient.startLocation();

        return mLocationClient;
    }


    /**
     * 初始化定位
     */
    public static AMapLocationClient initLocalize2(AMapLocationClient mLocationClient, Context mContext, AMapLocationListener mLocationListener,
                                                   AMapLocationClientOption mLocationOption) {

        //初始化定位
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());


        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //(单次定位)获取一次定位结果：
        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //（连续定位）设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(5000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        return mLocationClient;
    }

    /**
     * 设置定位蓝点
     */
    public static MyLocationStyle showLocationStyle(MyLocationStyle myLocationStyle) {
        //设置蓝点
        if (myLocationStyle == null) {
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位
//            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car));
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car2));


            myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        }
        return myLocationStyle;
    }

    /**
     * 定位监听回到后执行方法
     *
     * @param aMapLocation
     */
    public static void locationClientListener(AMapLocation aMapLocation, AlertDialog dialog, GYTMonitorPresenter presenter,
                                              AMapLocation aMapLocationTag,
                                              TextView dialogSpeed) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
//                Toast.makeText(getActivity(), aMapLocation.getLongitude() + "/" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show();

                //dialog 存在的情况下
                if (dialog != null) {
//                    presenter.getLocationInfo();//获取监控定位数据报表
                    if (aMapLocationTag != null) {
                        SetDialogData.sd = "速度：" + aMapLocation.getSpeed() + "公里/小时";

//                        double ss = getDistance(aMapLocation.getLongitude(), aMapLocation.getLatitude(),
//                                aMapLocationTag.getLongitude(), aMapLocationTag.getLatitude());
//                        if ((ss / 4) > 1 && dialogSpeed != null) {
//                            SetDialogData.sd = "速度：" + CommonUitls.strTwo(ss / 4) + "米/秒";
//                        } else {
//                            SetDialogData.sd = "速度：0 公里/小时";
//                        }

                        SetDialogData.dqzb = "当前坐标：" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(aMapLocation.getLongitude())) + "E/" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(aMapLocation.getLatitude())) + "N";
                    }
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.d("print", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 回放比赛
     */
    public static SmoothMoveMarker playback(AMap aMap, TextureMapView mMapView,
                                            List<LocationInfoEntity> datasa, List<LatLng> points,
                                            Context mContext, SmoothMoveMarker smoothMarker) {
        // 获取轨迹坐标点
//        List<LatLng> points = readLatLngs();
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。

            //显示缩放按钮
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        }
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。

        if (datasa == null || datasa.size() < 3) {
            CommonUitls.showSweetAlertDialog(mContext, "温馨提示", "该场比赛暂无数据");
            return null;
        }

        points.clear();
        for (int i = 0; i < datasa.size(); i++) {
            LatLng latLng = new LatLng(datasa.get(i).getLa(), datasa.get(i).getLo());
            points.add(latLng);
        }

        if (points.size() == 0) {
            return null;
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//地图缩放比例

        //视图移动到第一个点的位置
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(datasa.get(0).getLa(), datasa.get(0).getLo())));

        if (points.size() > 3) {
            LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));//界限
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));//动画相机
        }

        if (smoothMarker != null) {
            smoothMarker.destroy();//销毁上一个回放过程
        }

        smoothMarker = new SmoothMoveMarker(aMap); //光滑的标志
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.ic_car));

        //设置平滑移动的经纬度数组
//        smoothMarker.setPoints(points);

        LatLng drivePoint = points.get(0);//驾驶点
        //车距离最短的点
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(datasa.size() / 10);
//        smoothMarker.setTotalDuration(datasa.size() * 1);

        // 开始滑动  http://lbs.amap.com/api/android-sdk/guide/draw-on-map/smooth-move
        AMap finalAMap = aMap;
        SmoothMoveMarker finalSmoothMarker = smoothMarker;
        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(double v) {
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(smoothMarker.getPosition().latitude, smoothMarker.getPosition().longitude)));
                Log.d("dangqianweizhi", "move: " + v + "     -->" + finalSmoothMarker.getPosition().latitude + "        " + finalSmoothMarker.getPosition().longitude);
                finalAMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(finalSmoothMarker.getPosition().latitude, finalSmoothMarker.getPosition().longitude)));
            }
        });

        return smoothMarker;
    }


    //初始化地图
    public static MyLocationStyle initMaps(Bundle state, TextureMapView mMapView, MyLocationStyle myLocationStyle, AMap aMap) {

        //获取地图控件引用
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(state);//显示地图
        if (MonitorData.getMonitorStateCode() == 1) {//正在监控
            myLocationStyle = MonitorPresenter.showLocationStyle(myLocationStyle);//启动蓝点
        }

        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setTrafficEnabled(true);// 显示实时交通状况
            if (myLocationStyle != null && MonitorData.getMonitorStateCode() == 1) {
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
            }
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。

            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER); //显示缩放按钮
            uiSettings.setCompassEnabled(true);//指南针
            aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//地图缩放比例

//            uiSettings.setScaleControlsEnabled(true);//比例尺控件
//            uiSettings.setAllGesturesEnabled(true);//设置所有手势启动
        }

        return myLocationStyle;
    }

    /**
     * 天气查询
     *
     * @param city 城市
     * @param lo   经度
     * @param la   纬度
     * @param type 类型（1：司放地  2  当前位置）
     */
    public static void initWeatherSearch(Context mContext, AMapLocation aMapLocationTag, AlertDialog dialog, TextView flyWeather,
                                         TextView currentWeather,
                                         String city, double lo, double la, int type, double lcZ) {

        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        WeatherSearchQuery mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch mweathersearch = new WeatherSearch(mContext);

        mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
                if (rCode == 1000) {
                    if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();

//                        //StateCode【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
//                        if (aMapLocationTag != null && MonitorData.getMonitorStateCode() == 1
//                                && aMapLocationTag.getLatitude() != 0//经度不是空
//                                && aMapLocationTag.getLatitude() != 0//纬度不是空
//                                ) {
//                            //数据格式为:纬度|经度|速度|定位时间戳|天气|风向|风力|天气时间|温度|里程
//                            sendMsg(aMapLocationTag.getLatitude() + "|" + aMapLocationTag.getLongitude() + "|" +
//                                    aMapLocationTag.getSpeed() * 3.6 + "|" + System.currentTimeMillis() / 1000 + "|" +
//                                    weatherlive.getWeather() + "|" + weatherlive.getWindDirection() + "|" +
//                                    weatherlive.getWindPower() + "|" + weatherlive.getReportTime() + "|" +
//                                    weatherlive.getTemperature() + "|" + lcZ);
//                        }

                        if (dialog != null) {
                            switch (type) {
                                case 1:
                                    SetDialogData.sfdtq = "司放地天气：" + weatherlive.getWeather() + "  " + weatherlive.getTemperature() + "℃  " + weatherlive.getWindDirection() + "风";
                                    flyWeather.setText(SetDialogData.sfdtq);
                                    break;
                                case 2:
                                    SetDialogData.dqtq = "当前天气：" + weatherlive.getWeather() + "  " + weatherlive.getTemperature() + "℃  " + weatherlive.getWindDirection() + "风";
                                    break;
                            }
                        }
                    } else {
//                        ToastUtil.show(WeatherSearchActivity.this, R.string.no_result);
                    }
                } else {
//                    ToastUtil.showerror(WeatherSearchActivity.this, rCode);
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int rCode) {

            }
        });
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }


    /**
     * 按钮结束回放
     */
    public static SmoothMoveMarker btnStopMonitor(Activity mContext, GYTMonitorPresenter presenter, TextView btnEndPlayback,
                                                  SmoothMoveMarker smoothMarker, AMap aMap, TextureMapView mMapView, List<LocationInfoEntity> datasa,
                                                  List<LatLng> points) {

        if (MonitorData.getMonitorStateCode() == 1) {
//            //结束监控
//            CommonUitls.showSweetDialog(mContext, "确定结束比赛？", dialog -> {
//
//                ((PigeonMonitorActivity) mContext).setOnAllUploadDialogCancelListener(() -> {
//                    presenter.stopMonitor();
//                    btnEndPlayback.setText("线路回放");
//                });
//
//                boolean isHaveData = ((PigeonMonitorActivity) mContext).showUploadDialog(
//                        new OfflineFileManager(mContext, String.valueOf(MonitorData.getMonitorId()), OfflineFileManager.TYPE_IMG),
//                        new OfflineFileManager(mContext, String.valueOf(MonitorData.getMonitorId()), OfflineFileManager.TYPE_VIDEO)
//                );
//
//                if (!isHaveData) {
//                    presenter.stopMonitor();
//                    btnEndPlayback.setText("线路回放");
//                }
//
//                dialog.dismiss();
//            });

            MonitorPresenter.stopPlay(mContext, presenter, btnEndPlayback);

        } else if (MonitorData.getMonitorStateCode() == 2) {
            //回放比赛
            //startMove();
            if (MonitorData.getMonitorStateCode() == 2) {

                if (smoothMarker != null) {
                    smoothMarker = MonitorPresenter.playback(aMap, mMapView, datasa, points, mContext, smoothMarker);
                    smoothMarker.startSmoothMove();//开始回放比赛
                }
            }
        }

        return smoothMarker;
    }

    public static void stopPlay(Activity mContext, GYTMonitorPresenter presenter, TextView btnEndPlayback) {
        //结束监控
        CommonUitls.showSweetDialog(mContext, "确定结束比赛？", dialog -> {

            ((PigeonMonitorActivity) mContext).setOnAllUploadDialogCancelListener(() -> {
                presenter.stopMonitor();
                btnEndPlayback.setText("线路回放");
            });

            boolean isHaveData = ((PigeonMonitorActivity) mContext).showUploadDialog(
                    new OfflineFileManager(mContext, String.valueOf(MonitorData.getMonitorId()), OfflineFileManager.TYPE_IMG),
                    new OfflineFileManager(mContext, String.valueOf(MonitorData.getMonitorId()), OfflineFileManager.TYPE_VIDEO)
            );

            if (!isHaveData) {
                presenter.stopMonitor();
                btnEndPlayback.setText("线路回放");
            }

            dialog.dismiss();
        });
    }

}
