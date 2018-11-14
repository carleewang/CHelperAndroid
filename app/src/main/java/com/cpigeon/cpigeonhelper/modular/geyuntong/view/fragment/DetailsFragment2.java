package com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.mina.ConnectionManager;
import com.cpigeon.cpigeonhelper.mina.SessionManager;
import com.cpigeon.cpigeonhelper.modular.authorise.view.activity.AddNewAuthActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTongs;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.MyLocation;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTMonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorDialogPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.SetDialogData;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.INaviInfoCallbackImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.MonitorView;
import com.cpigeon.cpigeonhelper.service.DetailsService1;
import com.cpigeon.cpigeonhelper.service.DetailsService2;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;
import xiaofei.library.hermeseventbus.HermesEventBus;

import static com.cpigeon.cpigeonhelper.mina.SessionManager.contents;
import static com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.MonitorPresenter.playback;
import static com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.SetDialogData.lcZ;
import static com.cpigeon.cpigeonhelper.utils.DateUtils.sdf;

/**
 * 监控详情
 * Created by Administrator on 2017/10/10.
 */
public class DetailsFragment2 extends BaseFragment implements MonitorView {

    @BindView(R.id.location_information)
    ImageButton locationInformation;//位置信息
    @BindView(R.id.btn_end_playback)
    Button btnEndPlayback;//回放，结束监控
    @BindView(R.id.map)
    TextureMapView mMapView;//地图控件
    @BindView(R.id.location_navigation)
    ImageButton location_navigation;//导航按钮
    @BindView(R.id.ll_sqrck)
    LinearLayout ll_sqrck;

    private GYTMonitorPresenter presenter;

    //地图显示相关
    private AMap aMap;

    private AMapLocation aMapLocationTag;
    public AMapLocationClientOption mLocationOption = null;    //声明AMapLocationClientOption对象
    private MyLocationStyle myLocationStyle;//设置蓝点

    //创建fragment
    private static DetailsFragment2 fragment;
    private Handler handler = new Handler();

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private SweetAlertDialog mSweetAlertDialog;//持续提示对话框
    private List<LocationInfoEntity> datasa = new ArrayList<>();//获取监控数据保存的
    private List<LocationInfoEntity> datas = new ArrayList<>();
    private AMapNavi mAMapNavi = null;//语音播报

    private boolean isFirst = true;//是否第一次定位
    private double firstLo, firstLa;//第一次定位点的坐标

//    private double lcZ = 0;//总的里程

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_details;
    }

    @Override
    public void finishCreateView(Bundle state) {

        isFirst = true;
        DetailsService1.aMapLocationTag = null;
        DetailsService1.locationTag = null;
        ConnectionManager.isConnect = true;
        SessionManager.isday = false;

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            EventBus.getDefault().register(this);//在当前界面注册一个订阅者

//            HermesEventBus.getDefault().register(this);

            //语音播报
            mAMapNavi = AMapNavi.getInstance(getActivity());
            mAMapNavi.setUseInnerVoice(true);

            initDialogData();//初始化dialog数据

            presenter = new GYTMonitorPresenter(this);//初始化控制层
            presenter.getLocationInfo();//获取监控定位数据报表

            myLocationStyle = MonitorPresenter.initMaps(state, mMapView, myLocationStyle, aMap);

            DetailsService1.getInstance(getActivity());//启动后台定位

            //【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
            if (MonitorData.getMonitorStateCode() == 2) {//监控结束，显示回放
                btnEndPlayback.setText("线路回放");
                presenter.getKjLc();
            } else if (MonitorData.getMonitorStateCode() == 1) {//正在监控
                location_navigation.setVisibility(View.VISIBLE);//导航按钮显示
                btnEndPlayback.setText("结束监控");

                startServiceDetails();

//                DetailsService1.mLocationClient = MonitorPresenter.initLocalize(DetailsService1.mLocationClient, getActivity(), mLocationListener, mLocationOption);// 初始化定位
                presenter.getGTYRaceLocation(0);//51.获取鸽运通定位信息
            } else if (MonitorData.getMonitorStateCode() == 0) {//监控未开始

                startServiceDetails();

                btnEndPlayback.setText("结束监控");
//                DetailsService1.mLocationClient = MonitorPresenter.initLocalize(DetailsService1.mLocationClient, getActivity(), mLocationListener, mLocationOption);// 初始化定位
            } else if (MonitorData.getMonitorStateCode() == 3) {//授权人查看比赛

                ll_sqrck.setVisibility(View.VISIBLE);
                btnEndPlayback.setVisibility(View.GONE);//隐藏监控结束回放按钮
                mSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);//持续提示对话框
                mSweetAlertDialog.setTitleText("正在获取数据，请稍后");
                mSweetAlertDialog.show();

                if (raceLocationData.size() > 0) {
                    presenter.getGTYRaceLocation(raceLocationData.get(raceLocationData.size() - 1).getLid());//51.获取鸽运通定位信息
                } else {
                    presenter.getGTYRaceLocation(0);//51.获取鸽运通定位信息
                }

                mThread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; ; ) {
                            try {
                                Thread.sleep(10 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (getActivity() == null || getActivity().isDestroyed()) {
                                return;
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (raceLocationData.size() > 0) {
                                        presenter.getGTYRaceLocation(raceLocationData.get(raceLocationData.size() - 1).getLid());//51.获取鸽运通定位信息
                                    } else {
                                        presenter.getGTYRaceLocation(0);//51.获取鸽运通定位信息
                                    }
                                }
                            });
                        }
                    }
                });
                mThread2.start();
            }
        } catch (Exception e) {

        }
    }

    private void startServiceDetails() {
        Intent intent1 = new Intent(getActivity(), DetailsService1.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startService(intent1);

        DetailsService1.isStartLocate = true;

        Intent intent2 = new Intent(getActivity(), DetailsService2.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startService(intent2);
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String playListRefresh) {
        //结束比赛成功返回
        if (playListRefresh.equals("endPlay")) {
            succeed();
        }
    }

    private List<LatLng> hLine = new ArrayList<>();

    private Polyline jklxPolyline;//监控路线


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(AMapLocation aMapLocation) {
        //定位结果
        Log.d("dingwei", "onEventMainThread: 定位结果");
        try {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {


                    try {
                        SetDialogData.sd = "速度：" + aMapLocation.getSpeed() * 3.6 + "公里/小时";

//                        //监控绘制路线
//                        if (jklxPolyline != null) {
//                            try {
//                                if (aMapLocationTag != null && aMapLocation != null) {
//                                    hLine.clear();
//                                    hLine.add(new LatLng(aMapLocationTag.getLatitude(), aMapLocationTag.getLongitude()));
//                                    hLine.add(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
////                                    mmmPolyline = aMap.addPolyline(new PolylineOptions().addAll(hLine).width(10).color(getResources().getColor(color.get(new Random().nextInt(6)))));
//                                    jklxPolyline = aMap.addPolyline(new PolylineOptions().addAll(hLine).width(10).color(getActivity().getResources().getColor(R.color.color_theme)));
//                                }
//                            } catch (Exception e) {
//
//                            }
//                        } else {
//                            try {
//                                jklxPolyline = aMap.addPolyline(new PolylineOptions().addAll(points).width(10).color(getActivity().getResources().getColor(R.color.color_theme)));
//                            } catch (Exception e) {
//                            }
//                        }

                    } catch (Exception e) {

                    }
                    aMapLocationTag = aMapLocation;//当前的位置信息坐标保存
                    try {
                        //获取空距  里程
                        isFirstGetKjLc(aMapLocation.getLatitude(), aMapLocation.getLongitude());


                        if (mPolyline == null) {
                            if (MonitorData.getMonitorFlyLo() != 0 || MonitorData.getMonitorFlyLa() != 0) {//有司放地
                                //地图上面绘制线 http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline
                                jkgclx.clear();
                                jkgclx.add(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));//当前点
                                jkgclx.add(new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));//司放地点

                                mPolylineOptions = new PolylineOptions().addAll(jkgclx).width(10).color(getActivity().getResources().getColor(R.color.colorRed));

                                try {
                                    mPolyline = aMap.addPolyline(mPolylineOptions);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (markerOptionStart == null) {
                                        markerOptionStart = new MarkerOptions();
                                        markerOptionStart.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                                        markerOptionStart.title("起点");

                                        markerOptionStart.draggable(false);//设置Marker可拖动
                                        markerOptionStart.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                                .decodeResource(getResources(), R.mipmap.amap_start)));
                                        aMap.addMarker(markerOptionStart);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (markerOptionEnd == null) {
                                        markerOptionEnd = new MarkerOptions();
                                        markerOptionEnd.position(new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));
                                        markerOptionEnd.title("终点");
                                        markerOptionEnd.draggable(false);//设置Marker可拖动
                                        markerOptionEnd.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.amap_end)));
                                        aMap.addMarker(markerOptionEnd);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (Exception e) {
                        Log.d("mads", "onLocationChanged:3");
                    }

                }
            }

        } catch (Exception e) {
        }

        presenter.gytRaceChk();//鸽运通权限检查
    }


    /**
     * 点击事件
     */
    @OnClick({R.id.location_information, R.id.btn_end_playback, R.id.location_navigation, R.id.btn_sqrck_bg1, R.id.btn_sqrck_bg2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location_information://位置信息
                try {
                    dialog.show();//弹出dialog
                    //线程开启
                    if (MonitorData.getMonitorStateCode() != 2) {//不是监控结束
                        if (mThread == null) {
                            mThread = new Thread(MonitorDialogPresenter.myTharad(dialog, mThread, handler, monitorTime, dialogMiles, dialogUllage, currentLoLa, currentWeather, dialogSpeed));
                        }

                        if (mThread != null && !mThread.isAlive()) {
                            mThread.start();
                        }
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.btn_end_playback://结束，回放
                try {
                    smoothMarker = MonitorPresenter.btnStopMonitor(getActivity(), presenter, btnEndPlayback, smoothMarker, aMap, mMapView, datasa, points);

                } catch (Exception e) {

                }
                break;
            case R.id.location_navigation://导航
                try {
                    //语音播报
                    mAMapNavi = AMapNavi.getInstance(getActivity());
                    mAMapNavi.setUseInnerVoice(true);

                    if (MonitorData.getMonitorFlyLo() == 0 || MonitorData.getMonitorFlyLa() == 0) {
                        //没有司放地坐标
                        startNavigation();
                    } else {
                        //有司放地坐标
                        startNavigation(null, new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.btn_sqrck_bg1:
                //授权人转移授权
                Intent intent = new Intent(getActivity(), AddNewAuthActivity.class);
                intent.putExtra("title", "变更授权");
                intent.putExtra("MonitorGeYunTongs", MonitorData.getMonitorGeYunTongs());//比赛id
                getActivity().startActivity(intent);

                break;
            case R.id.btn_sqrck_bg2:
                //授权人结束比赛
                MonitorPresenter.stopPlay(getActivity(), presenter, btnEndPlayback);
                break;
        }
    }


    //=================================================初始化======================================================

    private Thread mThread;
    private Thread mThread2;
    //===================================================定位===========================================================
    private List<LatLng> jkgclx = new ArrayList<>();//监控过程连线
    private PolylineOptions mPolylineOptions = null;
    private Polyline mPolyline = null;

    private PolylineOptions polylineOptionsLx;//监控路线画线


    //获取空距  里程  (纬度la ，经度lo)
    private void isFirstGetKjLc(double firstLa, double firstLo) {
        try {
            if (isFirst) {//是否第一次定位
//                if (MonitorData.getMonitorFlyLo() != 0 || MonitorData.getMonitorFlyLa() != 0) {

                RxUtils.delayed(300, aLong -> {
                    presenter.getKjLcInfo(MonitorData.getMonitorFlyLo(), MonitorData.getMonitorFlyLa(), firstLo, firstLa);
                });
//                }
                isFirst = false;
            }
        } catch (Exception e) {
            Log.d("mads", "onLocationChanged: 6");
        }
    }

    private String TAG = "DetailsFragment";
    private LatLonPoint latLonPoint;
    private RegeocodeQuery query;

    /**
     * @param lo 经度
     * @param la 纬度
     */
    private void getAddressByLatlng(double lo, double la) {
        //地理搜索类
        GeocodeSearch geocodeSearch = new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                try {
                    if (MonitorData.getMonitorStateCode() == 2) {
                        if (regeocodeResult.getRegeocodeAddress().getNeighborhood().length() != 0) {
                            currentLoLa.setText("司放地点：" + regeocodeResult.getRegeocodeAddress().getNeighborhood());//司放地点（当前位置）
                        } else {
                            currentLoLa.setText("司放地点：暂无");
                        }
                    }
                    //司放地查询天气
                    MonitorPresenter.initWeatherSearch(getActivity(), aMapLocationTag, dialog, flyWeather, currentWeather, regeocodeResult.getRegeocodeAddress().getCity(), MonitorData.getMonitorFlyLo(), MonitorData.getMonitorFlyLa(), 1, lcZ);

                } catch (Exception e) {

                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        latLonPoint = new LatLonPoint(la, lo);
        query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * @param lo 经度
     * @param la 纬度
     */
    private void getAddressByLatlngSq(double lo, double la) {
        //地理搜索类
        GeocodeSearch geocodeSearch = new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                try {
                    //司放地查询天气
                    MonitorPresenter.initWeatherSearch(getActivity(), aMapLocationTag, dialog, flyWeather, currentWeather, regeocodeResult.getRegeocodeAddress().getCity(), MonitorData.getMonitorFlyLo(), MonitorData.getMonitorFlyLa(), 2, lcZ);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        latLonPoint = new LatLonPoint(la, lo);
        query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }


    /**
     * 初始化天气搜索相关
     */
    private LocalWeatherLive weatherlive;

//====================================================监控数据==========================================================

    /**
     * 获取监控数据报表
     */
    @Override
    public void locationInfoDatas(List<LocationInfoEntity> datas) {

        try {
            this.datasa = datas;//保存服务器保存的地点位置数据

            if (aMap == null) {
                aMap = mMapView.getMap();
                aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。

                //显示缩放按钮(位置)
                UiSettings uiSettings = aMap.getUiSettings();
                uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            }

            //【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
            if (MonitorData.getMonitorStateCode() == 2) {//监控结束，显示回放
                jkjs(datas);
            }
        } catch (Exception e) {

        }
    }

    //获取监控数据监控结束设置数据
    private void jkjs(List<LocationInfoEntity> datas) {
        try {
            try {
                if (datas.size() > 3) {
                    //有数据  设置 位置信息  司放地位置信息等
                    SetDialogData.sfdzb = "司放地坐标：" + GPSFormatUtils.loLaToDMS(datas.get(datas.size() - 1).getLo()) + "E/" + GPSFormatUtils.loLaToDMS(datas.get(datas.size() - 1).getLa()) + "N";

                } else {
                    SetDialogData.sfdzb = "司放地坐标：暂无";//司放地坐标
                }

            } catch (Exception e) {

            }

            points.clear();//清空之前保留的数据
            //视图移动到第一个坐标点位置
            if (datasa.size() > 0) {
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(datasa.get(0).getLa(), datasa.get(0).getLo())));
            }
            for (int i = 0; i < datasa.size(); i++) {
                LatLng latLng = new LatLng(datasa.get(i).getLa(), datasa.get(i).getLo());
                points.add(latLng);
            }
            if (points.size() == 0) {
                CommonUitls.showSweetAlertDialog(getActivity(), "温馨提示", "该场监控暂无数据");
                return;
            }

            try {
                //地图上面绘制线 http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline
                jklxPolyline = aMap.addPolyline(new PolylineOptions().addAll(points).width(10).color(getActivity().getResources().getColor(R.color.color_theme)));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            if (points.size() > 3) {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(points.get(0));
                markerOption.title("起点");

                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.amap_start)));
                aMap.addMarker(markerOption);

                MarkerOptions markerOption2 = new MarkerOptions();
                markerOption2.position(points.get(points.size() - 1));
                markerOption2.title("终点");

                markerOption2.draggable(false);//设置Marker可拖动
                markerOption2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.amap_end)));
                aMap.addMarker(markerOption2);
            }
            smoothMarker = playback(aMap, mMapView, datasa, points, getActivity(), smoothMarker);//回放

        } catch (Exception e) {

        }
    }

    /**
     * 51.获取鸽运通定位信息  授权人查看数据
     */
    private List<RaceLocationEntity> raceLocationData = new ArrayList<>();
    private MarkerOptions markerOption;

    private MarkerOptions markerOptionStart;//起点maker
    private MarkerOptions markerOptionEnd;//终点maker

    private Marker mMarker;

    @Override
    public void getRaceLocation(List<RaceLocationEntity> raceLocationData) {
        try {
            if (raceLocationData == null) {
                return;
            }
            try {
                SetDialogData.sd = "速度：" + raceLocationData.get(raceLocationData.size() - 1).getSd() + "公里/小时";
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.raceLocationData.clear();
            this.raceLocationData = raceLocationData;

            if (MonitorData.getMonitorStateCode() == 3) {
                sqrckbs(this.raceLocationData);//授权人查看比赛
            } else if (MonitorData.getMonitorStateCode() == 1) {
//                jkzhzxl(this.raceLocationData);//监控中绘制线路
            }
        } catch (Exception e) {
            presenter.getGTYRaceLocation(0);//51.获取鸽运通定位信息
            Log.d("hqdwsj", "getdata: 结果11 " + e.getLocalizedMessage());
        }
    }

    //监控中绘制线路
    private LatLng latLngs;

    private void jkzhzxl(List<RaceLocationEntity> raceLocationData) {


        if (raceLocationData.size() > 1) {
            firstLo = raceLocationData.get(0).getJd();
            firstLa = raceLocationData.get(0).getWd();
            //获取空距,里程
            isFirst = true;
            isFirstGetKjLc(raceLocationData.get(0).getWd(), raceLocationData.get(0).getJd());
        }

        try {
            if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {//加载数据成功
                mSweetAlertDialog.dismissWithAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < raceLocationData.size(); i++) {
                latLngs.clone();
                latLngs = new LatLng(raceLocationData.get(i).getWd(), raceLocationData.get(i).getJd());
                points.add(latLngs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (markerOptionStart == null) {
                markerOptionStart = new MarkerOptions();
                markerOptionStart.position(points.get(0));
                markerOptionStart.title("起点");

                markerOptionStart.draggable(false);//设置Marker可拖动
                markerOptionStart.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.amap_start)));
                aMap.addMarker(markerOptionStart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            //地图上面绘制线 http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline
//            jklxPolyline = aMap.addPolyline(new PolylineOptions().addAll(points).width(10).color(getActivity().getResources().getColor(R.color.color_theme)));
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        }

        if (MonitorData.getMonitorFlyLa() != 0 && MonitorData.getMonitorFlyLo() != 0) {
            try {
                //地图上面绘制线 http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline
                jkgclx.clear();
                jkgclx.add(points.get(0));//当前点
                jkgclx.add(new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));//司放地点

                mPolylineOptions = new PolylineOptions().addAll(jkgclx).width(10).color(getActivity().getResources().getColor(R.color.colorRed));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            try {
                if (mPolyline == null) {
                    mPolyline = aMap.addPolyline(mPolylineOptions);
                } else {
                    mPolyline.remove();
                    mPolyline = aMap.addPolyline(mPolylineOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            markerOptionEnd = new MarkerOptions();
            markerOptionEnd.position(new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));
            markerOptionEnd.title("终点");
            markerOptionEnd.draggable(false);//设置Marker可拖动
            markerOptionEnd.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.amap_end)));
            aMap.addMarker(markerOptionEnd);
        }
    }

    //授权人查看比赛
    private void sqrckbs(List<RaceLocationEntity> raceLocationData) {

        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {//加载数据成功
            mSweetAlertDialog.dismissWithAnimation();
        }

        if (raceLocationData.size() > 1) {
            //获取空距,里程
            isFirst = true;
            isFirstGetKjLc(raceLocationData.get(0).getWd(), raceLocationData.get(0).getJd());
        }

        try {
            if (markerOption != null) {
                mMarker.remove();
            }

            markerOption = new MarkerOptions();
            markerOption.position(new LatLng(raceLocationData.get(raceLocationData.size() - 1).getWd(), raceLocationData.get(raceLocationData.size() - 1).getJd()));
            markerOption.title("监控车");

            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_car)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
            mMarker = aMap.addMarker(markerOption);
            mMarker.setAnchor(0.5f, 0.5f);

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < raceLocationData.size(); i++) {
            LatLng latLng = new LatLng(raceLocationData.get(i).getWd(), raceLocationData.get(i).getJd());
            points.add(latLng);
        }

        if (points.size() == 0) {
            return;
        }

        try {
            SetDialogData.dqzb = "当前坐标：" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(points.get(points.size() - 1).longitude)) + "E/" + GPSFormatUtils.strToDMs(CommonUitls.GPS2AjLocation(points.get(points.size() - 1).latitude)) + "N";
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取当前天气
        getAddressByLatlngSq(points.get(points.size() - 1).longitude, points.get(points.size() - 1).latitude);

        //地图上面绘制线 http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline

        try {
            jklxPolyline = aMap.addPolyline(new PolylineOptions().addAll(points).width(10).color(getActivity().getResources().getColor(R.color.color_theme)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (markerOptionStart == null) {
                markerOptionStart = new MarkerOptions();
                markerOptionStart.position(points.get(0));
                markerOptionStart.title("起点");

                markerOptionStart.draggable(false);//设置Marker可拖动
                markerOptionStart.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.amap_start)));
                aMap.addMarker(markerOptionStart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (MonitorData.getMonitorFlyLa() != 0 && MonitorData.getMonitorFlyLo() != 0) {

            //地图上面绘制线 http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline
            jkgclx.clear();
            jkgclx.add(points.get(0));//当前点
            jkgclx.add(new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));//司放地点

            mPolylineOptions = new PolylineOptions().addAll(jkgclx).width(10).color(getActivity().getResources().getColor(R.color.colorRed));

            try {
                if (mPolyline == null) {
                    mPolyline = aMap.addPolyline(mPolylineOptions);
                } else {
                    mPolyline.remove();
                    mPolyline = aMap.addPolyline(mPolylineOptions);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            markerOptionEnd = new MarkerOptions();
            markerOptionEnd.position(new LatLng(CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLa()), CommonUitls.Aj2GPSLocation(MonitorData.getMonitorFlyLo())));
            markerOptionEnd.title("终点");
            markerOptionEnd.draggable(false);//设置Marker可拖动
            markerOptionEnd.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.amap_end)));
            aMap.addMarker(markerOptionEnd);
        }

    }

    /**
     * 结束比赛成功
     */
    @Override
    public void succeed() {

        try {
            DetailsService1.intTag1 = false;

            ll_sqrck.setVisibility(View.GONE);
            btnEndPlayback.setVisibility(View.VISIBLE);//隐藏监控结束回放按钮

            points.clear();//清空数据
            aMap.clear();

            //停止线程
            if (mThread != null && mThread.isAlive()) {//如果现在正在执行
                mThread.interrupt();//线程结束
            }

            //停止线程
            if (mThread2 != null && mThread2.isAlive()) {//如果现在正在执行
                mThread2.interrupt();//线程结束
            }

            aMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            getActivity().stopService(new Intent(getActivity(), DetailsService1.class));//hl  停止服务

            presenter.getLocationInfo();//获取监控定位数据报表

            btnEndPlayback.setText("线路回放");

            //保存结束状态监控
            GeYunTongs geYunTongs = new GeYunTongs();
            geYunTongs.setId(MonitorData.getMonitorId());//比赛id
            //StateCode【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
            geYunTongs.setStateCode(2);//状态码
            geYunTongs.setmEndTime(sdf.format(new Date()));//结束时间

            geYunTongs.setmTime(MonitorData.getMonitorMTime());//监控启动时间
            geYunTongs.setLongitude(MonitorData.getMonitorFlyLo());//经度
            geYunTongs.setLatitude(MonitorData.getMonitorFlyLa());//纬度
            geYunTongs.setState("监控结束");//监控状态

            //删除单条鸽运通内容
            if (!RealmUtils.getInstance().existGYTBeanInfo()) {
                RealmUtils.getInstance().insertGYTBeanInfo(geYunTongs);//添加用户数据
            } else {
                RealmUtils.getInstance().deleteGYTBeanInfo();//删除数据
                RealmUtils.getInstance().insertGYTBeanInfo(geYunTongs);//添加用户数据
            }

            //发布事件（通知比赛列表刷新数据）
            EventBus.getDefault().post("playListRefresh");

            EventBus.getDefault().post("endPlay1");

            CommonUitls.showSweetDialog2(getActivity(), "结束比赛成功", dialog -> {
                dialog.dismiss();
                getActivity().finish();
            });

        } catch (Exception e) {

        }
    }

    /**
     * 结束比赛失败
     */
    @Override
    public void fail() {
        CommonUitls.showToast(getActivity(), "结束比赛失败");
    }

    /**
     * 回放比赛
     */
    private List<LatLng> points = new ArrayList<>();

    private SmoothMoveMarker smoothMarker;

//====================================================导航==========================================================

    //开始导航  不传入起点、途径点、终点启动导航组件
    private void startNavigation() {
        AmapNaviPage.getInstance().showRouteActivity(getActivity(), new AmapNaviParams(null), new INaviInfoCallbackImpl() {
        });
    }

    //开始导航  传入起点，终点
    private void startNavigation(LatLng startLatLng, LatLng endLatLng) {
        Poi start = new Poi("当前位置", startLatLng, "");

        //终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点
        Poi end = new Poi("", endLatLng, "");
        AmapNaviPage.getInstance().showRouteActivity(getActivity(), new AmapNaviParams(start, null, end, AmapNaviType.DRIVER), new INaviInfoCallbackImpl() {
        });
    }

//====================================================未使用==========================================================


    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }


    @Override
    public void openMonitorResults(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void imgOrVideoData(ApiResponse<List<ImgOrVideoEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }


//=================================================生命周期======================================================

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            //长连接
            DetailsService1.aMapLocationTag = null;
            DetailsService1.locationTag = null;
            ConnectionManager.isConnect = true;
            SessionManager.isday = false;

            isFirst = true;
            polylineOptionsLx = null;
            raceLocationData.clear();
            points.clear();
            markerOptionEnd = null;
            markerOptionStart = null;
            mPolyline = null;
            SetDialogData.initDialogData();//初始化dialog显示数据
            strLc = "";
            strKj = "";
            lcZ = 0;

            if (mThread != null && mThread.isAlive()) {//如果现在正在执行
                mThread.interrupt();//线程结束
            }
            mThread = null;

            //停止线程
            if (mThread2 != null && mThread2.isAlive()) {//如果现在正在执行
                mThread2.interrupt();//线程结束
            }

            mThread2 = null;

//            if (mLocationClient != null) {
//                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
//            }

//            getActivity().stopService(new Intent(getActivity(), CoreService.class));//hl  停止服务
//            getActivity().stopService(new Intent(getActivity(), DetailsService1.class));//hl  停止服务

            try {
                DetailsService1.intTag1 = false;
//                getActivity().stopService(new Intent(getActivity(), CoreService.class));//hl  停止服务
                getActivity().stopService(new Intent(getActivity(), DetailsService1.class));//hl  停止服务
                getActivity().stopService(new Intent(getActivity(), DetailsService2.class));//hl  停止服务
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mMapView != null) {
                mMapView.onDestroy();
            }

            //授权人查看的数据
            points.clear();//
            raceLocationData.clear();
            myLocationStyle = null;//设置蓝点视图为空（第二次进入不会启动定位）
            aMap = null;//设置视图为空（第二次进入不会启动定位）
            this.datas = null;//清空本地保存的数据保存的地点位置数据

            HermesEventBus.getDefault().unregister(this);//取消注册
            EventBus.getDefault().unregister(this);//取消注册


        } catch (Exception e) {

        }
    }

    /**
     * 创建当前类对象
     */
    public static DetailsFragment2 newInstance() {
        if (fragment == null) {
            synchronized (DetailsFragment2.class) {
                if (fragment == null)
                    fragment = new DetailsFragment2();
            }
        }
        return fragment;
    }

    //====================================================dialog位置信息==========================================================
    private String strLc;//里程
    private String strKj;//空距

    private LinearLayout dialogLayout;//dialog总布局
    private TextView monitorState;//监控状态
    private TextView monitorTime;//监控时长
    private TextView flyLoLa;//司放地坐标
    private TextView flyWeather;//司放地天气
    private TextView currentLoLa;//当前位置经纬度
    private TextView currentWeather;//当前位置天气
    private TextView dialogUllage;//空距
    private TextView dialogMiles;//里程
    private TextView dialogSpeed;//速度
    private Button dialogDismiss;//关闭dialog

    /**
     * dialog布局视图相关
     */
    private void initDialogViewLayout() {
        dialogLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view, null);
        monitorState = (TextView) dialogLayout.findViewById(R.id.monitor_state);//监控状态
        monitorTime = (TextView) dialogLayout.findViewById(R.id.monitor_time);//监控时长
        flyLoLa = (TextView) dialogLayout.findViewById(R.id.fly_lo_la);//司放地坐标
        flyWeather = (TextView) dialogLayout.findViewById(R.id.fly_weather);//司放地天气
        currentLoLa = (TextView) dialogLayout.findViewById(R.id.current_lo_la);//当前位置经纬度
        currentWeather = (TextView) dialogLayout.findViewById(R.id.current_weather);//当前位置天气
        dialogUllage = (TextView) dialogLayout.findViewById(R.id.dialog_ullage);//空距
        dialogMiles = (TextView) dialogLayout.findViewById(R.id.dialog_miles);//里程
        dialogSpeed = (TextView) dialogLayout.findViewById(R.id.dialog_speed);//速度（结束显示：监控时长）
        dialogDismiss = (Button) dialogLayout.findViewById(R.id.dialog_dismiss);//确定隐藏按钮

        if (MonitorData.getMonitorStateCode() == 1 || MonitorData.getMonitorStateCode() == 3) {//正在监控  设置司放地数据
            //设置司放地信息
            if (MonitorData.getMonitorFlyLo() == 0 || MonitorData.getMonitorFlyLa() == 0) {
                //没有司放地数据
                SetDialogData.sfdzb = "司放地坐标：用户未设置";
                flyLoLa.setText(SetDialogData.sfdzb);
                flyWeather.setText(SetDialogData.sfdtq);
            } else {
                SetDialogData.sfdzb = "司放地坐标：" + GPSFormatUtils.strToDMs(MonitorData.getMonitorFlyLo() + "") + "E/" + GPSFormatUtils.strToDMs(MonitorData.getMonitorFlyLa() + "") + "N";
                flyLoLa.setText(SetDialogData.sfdzb);
                getAddressByLatlng(MonitorData.getMonitorFlyLo(), MonitorData.getMonitorFlyLa());
            }
        }

        //点击确定，关闭提示框
        dialogDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThread != null && mThread.isAlive()) {//线程正在运行中
                    mThread.interrupt();//线程结束
                }
                dialog.dismiss();
            }
        });
    }

    //初始化dialog 数据
    private void initDialogData() {

        initDialogViewLayout();//初始化dialog 布局控件
        dialog = MonitorDialogPresenter.initDialog(getActivity(), builder, dialog, dialogLayout);//初始化dialog

        SetDialogData.showDialogData(monitorState, dialogSpeed, flyLoLa, flyWeather, currentLoLa, currentWeather, dialogUllage, dialogMiles, dialogSpeed);

        monitorState.setVisibility(View.VISIBLE);//监控状态
        monitorTime.setVisibility(View.VISIBLE);//监控时长
        currentWeather.setVisibility(View.VISIBLE);//当前天气

        if (MonitorData.getMonitorStateCode() == 2) {//监控结束，显示回放
            monitorState.setVisibility(View.GONE);//监控状态
            monitorTime.setVisibility(View.GONE);//监控时长
            currentWeather.setVisibility(View.GONE);//当前天气

            dialogSpeed.setText("监控时长：0000:00:00");

            SetDialogData.dqzb = "司放地点：暂无数据";
            currentLoLa.setText(SetDialogData.dqzb);//设置司放地
        }
    }

    //结束监控  dialog 显示信息
    @Override
    public void getKjLcData(ApiResponse<KjLcEntity> dataApiResponse, String msg, Throwable mThrowable) {
        try {
            if (dataApiResponse != null && dataApiResponse.getErrorCode() == 0 && dataApiResponse.getData() != null) {
                if (MonitorData.getMonitorStateCode() == 2) {//监控结束，显示回放
                    strKj = SetDialogData.kj;
                    strLc = SetDialogData.lc;
                    flyLoLa.setText(SetDialogData.sfdzb);
                    flyWeather.setText(SetDialogData.sfdtq);//设置司放地天气
                    currentLoLa.setText(SetDialogData.dqzb);//设置司放地
                    dialogUllage.setText(SetDialogData.kj);
                    dialogMiles.setText(SetDialogData.lc);

                    dialogSpeed.setText("监控时长：" + DateUtils.msToHsm1(1000 * Long.valueOf(dataApiResponse.getData().getJksc())));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //监控未结束 获取空距，里程
    @Override
    public void getKjLcInfoData(ApiResponse<KjLcInfoEntity> dataApiResponse, String msg, Throwable mThrowable) {
        try {
            if (dataApiResponse != null && dataApiResponse.getErrorCode() == 0 && dataApiResponse.getData() != null) {
                //获取空距里程成功
                try {
                    strKj = MonitorDialogPresenter.setUllage(Double.valueOf(dataApiResponse.getData().getKongju()));
                    SetDialogData.kj = strKj;
                } catch (Exception e) {
                    strKj = "空距：0 公里";
                    SetDialogData.kj = strKj;
                }

                try {
                    lcZ = Double.valueOf(dataApiResponse.getData().getLicheng()) + lcZ;
                    SetDialogData.lc = MonitorDialogPresenter.setLc(lcZ);
                } catch (Exception e) {
                    strLc = "里程：0 公里";
                    SetDialogData.lc = strLc;
                }
            }

            dialogUllage.setText(SetDialogData.kj);
            dialogMiles.setText(SetDialogData.lc);
        } catch (Exception throwable) {
            Log.d("xiaohlss", "getKjLcInfoData: " + throwable.getLocalizedMessage());
            //抛出网络异常相关
            if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
                presenter.getKjLcInfo(MonitorData.getMonitorFlyLo(), MonitorData.getMonitorFlyLa(), firstLo, firstLa);
            }
        }
    }

    //被授权监控权限检测
    @Override
    public void gytRaceChkData(ApiResponse<Object> dataApiResponse, String msg, Throwable mThrowable) {
        try {
            if (mThrowable != null) return;

            switch (dataApiResponse.getErrorCode()) {
                case 1001://比赛信息不存在

                    break;
                case 1002://比赛已被其他用户监控
                    //发布事件（通知比赛列表刷新数据）
                    EventBus.getDefault().post("playListRefresh");

                    EventBus.getDefault().post("endPlay1");

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity(), dialog -> {
                        dialog.dismiss();
                        getActivity().finish();
                    });//弹出提示
                    break;
                case 1003://该比赛已被其他用户结束监控
                    //发布事件（通知比赛列表刷新数据）
                    EventBus.getDefault().post("playListRefresh");

                    EventBus.getDefault().post("endPlay1");

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity(), dialog -> {
                        dialog.dismiss();
                        getActivity().finish();
                    });//弹出提示
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gytOfflineUploadData(ApiResponse<Object> dataApiResponse, String msg, Throwable mThrowable) {
        Log.d("asdfsa", "gytOfflineUploadData:1 ");

        contents = "";
        SessionManager.isday = false;
        try {
            if (dataApiResponse.getErrorCode() == 0) {
                RealmUtils.getInstance().deleteLocation();//删除所有点坐标
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int resultsSize = 0;

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(RealmResults<MyLocation> results) {
        //结束比赛成功返回
        resultsSize = results.size();
        try {
            if (results.get(0).getMonitorId() == MonitorData.getMonitorId()) {
                //保存的是当前比赛的数据
                for (int i = resultsSize - 1; i >= 0; i--) {
                    if (!SessionManager.contents.isEmpty()) {
                        SessionManager.contents = SessionManager.contents + "," + results.get(i).getMsg();
                    } else {
                        SessionManager.contents = results.get(i).getMsg();
                    }
                }
                Log.d("asdfsa", "writeToServer: " + SessionManager.contents);

                presenter.gytOfflineUpload(SessionManager.contents);
            } else {
                //删除监控数据
                RealmUtils.getInstance().deleteLocation();//删除所有点坐标
            }
        } catch (Exception e) {
            RealmUtils.getInstance().deleteLocation();//删除所有点坐标
        }
    }
}
