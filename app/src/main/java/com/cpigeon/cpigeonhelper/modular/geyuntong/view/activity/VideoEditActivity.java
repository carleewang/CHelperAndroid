package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.OfflineFileEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTMonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.UploadImgVideoPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.ImgVideoView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.MonitorViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.MapUtil;
import com.cpigeon.cpigeonhelper.utils.NetStateUtils;
import com.cpigeon.cpigeonhelper.utils.OfflineFileManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.http.GsonUtil;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.cpigeon.cpigeonhelper.R.id.watermark_time;

/**
 * Created by Administrator on 2017/10/24.
 */

public class VideoEditActivity extends ToolbarBaseActivity implements ImgVideoView {

    @BindView(R.id.watermark_dz)
    TextView watermark_dz;//地址
    @BindView(R.id.watermark_lo)
    TextView watermark_lo;//经度
    @BindView(R.id.watermark_la)
    TextView watermark_la;//经度

    @BindView(R.id.watermark_hb)
    TextView watermark_hb;//海拔
    @BindView(watermark_time)
    TextView watermarkTime;//水印时间
    @BindView(R.id.watermark_llz)
    LinearLayout videoWatermark;//视频水印布局

    @BindView(R.id.video_label)
    TextView videoLabel;
    @BindView(R.id.ll_label)
    LinearLayout llLabel;

    @BindView(R.id.edit_video)
    JCVideoPlayerStandard editVideo;//视频控件

    @BindView(R.id.btn_add_video)
    ImageView btnAddVideo;//添加视频按钮
    @BindView(R.id.video_edit_mapview)
    MapView mMapView;

    private List<TagEntitiy> datas;//标签数据
    private SaActionSheetDialog dialog;//选择标签

    private MyLocationStyle myLocationStyle;//设置定位蓝点
    private String we = "暂无", t = "暂无", wp = "暂无", wd = "暂无";//we：天气名称 t：温度   wp：风力 wd：风向
    private double lo = 0, la = 0;//经纬度
    private String address = "暂无";//地址

    private AMap aMap;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    /**
     * 初始化天气搜索相关
     */
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private String img_path;
    private View view;

    private SweetAlertDialog mSweetAlertDialog;

    private String video_path = "";//视频路径
    private UploadImgVideoPresenter presenter;//上传图片视频的控制层
    private GYTMonitorPresenter presenter2;//鸽车监控控制层

    private boolean uploadNetworkTag = false;//上传网络状态保存
    private SweetAlertDialog hintDialog;//网络状态提示框

    OfflineFileManager offlineFileManager;
    OfflineFileEntity offlineFileEntity;
    boolean isOfflineUpload;

    String tagId;

    private boolean isEndPlay = false;//是否结束比赛

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_video_edit;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        isEndPlay = false;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("上传视频");

        offlineFileEntity = getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        isOfflineUpload = getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);

        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                isOut();//点击是否退出
            }
        });

        setTopRightButton("上传", new OnClickListener() {
            @Override
            public void onClick() {

                if (AntiShake.getInstance().check()) {
                    return;
                }

                int APNType = NetStateUtils.getAPNType(VideoEditActivity.this);
                if (APNType == 0) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "当前暂无网络", VideoEditActivity.this);
                    return;
                }

                if (!uploadNetworkTag) {
                    //上传时网络判断
                    if (APNType != 1) {
                        hintDialog = CommonUitls.showSweetDialog(VideoEditActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                            uploadNetworkTag = true;//确定为流量上传
                            hintDialog.dismiss();//当前提示框关闭
                            uploadVideo();//上传视频
                        });
                        return;
                    }
                }
                uploadVideo();//上传视频
            }
        });

        datas = new ArrayList<>();//初始化存放标签数据集合

        presenter = new UploadImgVideoPresenter(this);

        presenter2 = new GYTMonitorPresenter(new MonitorViewImpl() {
            /**
             * 结束比赛成功
             */
            @Override
            public void succeed() {
                try {
                    mSweetAlertDialog.dismissWithAnimation();

                    CommonUitls.showSweetDialog1(VideoEditActivity.this, "上传视频成功,已结束比赛", dialog -> {

                        //发布事件（通知比赛图片列表刷新数据）
                        EventBus.getDefault().post("vodeoRefresh");
                        //结束比赛
                        EventBus.getDefault().post("endPlay");
                        //发布事件（通知比赛列表刷新数据）
                        EventBus.getDefault().post("playListRefresh");
                        AppManager.getAppManager().killActivity(VideoEditActivity.this.mWeakReference);//关闭当页面
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });//结束比赛控制层

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            presenter.getTag();//获取标签数据
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            llLabel.setVisibility(View.GONE);
        }
        offlineFileManager = new OfflineFileManager(getBaseContext()
                , String.valueOf(MonitorData.getMonitorId()), OfflineFileManager.TYPE_VIDEO);
//        initLocation(savedInstanceState);//初始化定位

        initData();
        //初始化定位（定位一次）
        if (isOfflineUpload) {
            mMapView.setVisibility(View.GONE);
            la = Double.parseDouble(offlineFileEntity.getLa());
            lo = Double.parseDouble(offlineFileEntity.getLo());
            t = offlineFileEntity.getT();
            tagId = offlineFileEntity.getTagid();
            wd = offlineFileEntity.getWd();
            we = offlineFileEntity.getWe();
            wp = offlineFileEntity.getWp();

            video_path = offlineFileEntity.getPath();
            editVideo.setVisibility(View.VISIBLE);//图片空间显示
            btnAddVideo.setVisibility(View.GONE);//添加视频按钮
            editVideo.backButton.setVisibility(View.GONE);
            editVideo.tinyBackImageView.setVisibility(View.GONE);
            editVideo.setUp(video_path, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);

            offlineFileManager.saveCache(video_path);
        } else {
            MapUtil.initLocation(savedInstanceState, mMapView, aMap, mLocationClient, mLocationListener, mLocationOption, myLocationStyle);
        }
    }

    @Override
    public void getErrorNews(String str) {
        super.getErrorNews(str);
        try {
            //隐藏dialog
            mSweetAlertDialog.dismissWithAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getThrowable(Throwable throwable) {
        super.getThrowable(throwable);
        try {
            //隐藏dialog
            mSweetAlertDialog.dismissWithAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否退出提示
     */
    private void isOut() {
        if (!StringValid.isStringValid(video_path)) {
            AppManager.getAppManager().killActivity(mWeakReference);
        } else {
            CommonUitls.showSweetDialog(this, "视频还未上传，是否继续退出", dialog -> {
                dialog.dismiss();
                saveUploadOfflineFile();
                AppManager.getAppManager().killActivity(mWeakReference);
            });
        }
    }

    @Override
    public void onBackPressed() {
        isOut();
    }

    private String TAG = "xiaohl";

    public void uploadVideo() {
        if (video_path == null) {
            CommonUitls.showToast(this, "请拍摄视频后上传");
        }

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通上传视频
            gytUploadVideo();

        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通上传视频
            startUploadVideo("");
        }
    }

    private void gytUploadVideo() {

        if (datas.size() > 0 && videoLabel.getText().toString().length() == 4) {
            for (int i = 0; i < datas.size(); i++) {
                if (videoLabel.getText().toString().equals(datas.get(i).getName())) {
                    if (videoLabel.getText().toString().equals("司放瞬间")) {
                        int posi = i;
                        CommonUitls.showSweetDialog(VideoEditActivity.this, "是否在发布成功后结束监控?", dialogConfir -> {
                            //确定
                            dialogConfir.dismiss();
                            isEndPlay = true;//是否结束比赛
                            //上传视频  司放瞬间
                            startUploadVideo(datas.get(posi).getTid() + "");
                        }, dialogCancel -> {
                            //取消
                            dialogCancel.dismiss();
                            //上传视频
                            if (video_path.isEmpty()) {
                                CommonUitls.showSweetAlertDialog(this, "温馨提示", "请拍摄视频后再上传");
                                return;
                            }
                            startUploadVideo(datas.get(posi).getTid() + "");
                        });

                    } else {
                        //上传视频
                        if (video_path.isEmpty()) {
                            CommonUitls.showSweetAlertDialog(this, "温馨提示", "请拍摄视频后再上传");
                            return;
                        }
                        startUploadVideo(datas.get(i).getTid() + "");
                    }
                    break;
                }
            }
        }
    }

    /**
     * 开始上传视频
     */
    private void startUploadVideo(String tagId) {
        mSweetAlertDialog = new SweetAlertDialog(VideoEditActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        CommonUitls.showLoadSweetAlertDialog(mSweetAlertDialog);
        presenter.uploadImgVideo("video",////文件类型【image video】
                tagId,////标签 ID
                new File(video_path),//图片的文件路径
                Double.valueOf(CommonUitls.GPS2AjLocation(lo)),//经度(安捷格式)
                Double.valueOf(CommonUitls.GPS2AjLocation(la)),//纬度（安捷格式）
                we,//we：天气名称
                t,//温度
                wp,//风力
                wd,//风向
                2, null, null);
    }


    /**
     * 弹出选择标签
     */
    private SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            if (datas.size() > 0) {
                videoLabel.setText(datas.get(which - 1).getName());
            }
        }
    };

    //定位相关
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            try {
                address = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();//地址

                lo = aMapLocation.getLongitude();//经度
                la = aMapLocation.getLatitude();//纬度F

                //天气查询
                initWeatherSearch(aMapLocation.getCity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 天气查询
     *
     * @param city 城市
     */
    public void initWeatherSearch(String city) {
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        mweathersearch = new WeatherSearch(VideoEditActivity.this);
        mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
                try {
                    if (rCode == 1000) {
                        if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                            weatherlive = weatherLiveResult.getLiveResult();
                            //we：天气名称 t：温度   wp：风力 wd：风向
                            we = weatherlive.getWeather();
                            t = weatherlive.getTemperature();
                            wd = weatherlive.getWindDirection();
                            wp = weatherlive.getWindPower();
                        } else {
                            //                        ToastUtil.show(WeatherSearchActivity.this, R.string.no_result);
                        }
                    } else {
                        //                    ToastUtil.showerror(WeatherSearchActivity.this, rCode);
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
    }


    @OnClick({R.id.ll_label})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_label://选择标签
                showDialog();//弹出选择标签
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (mLocationClient != null) {
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            }

            //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
            if (mMapView != null) {
                mMapView.onDestroy();
            }

            JCVideoPlayerStandard.releaseAllVideos();//切换页面去掉后台播放的视频
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }


    private void initData() {
        video_path = getIntent().getStringExtra("video_path");

        if (video_path == null) {
            video_path = getIntent().getStringExtra(IntentBuilder.KEY_DATA);
        }

        videoLabel.setText(getIntent().getStringExtra("label_tag"));

        //返回有视频路径
        editVideo.setVisibility(View.VISIBLE);//图片空间显示
        btnAddVideo.setVisibility(View.GONE);//添加视频按钮

        editVideo.backButton.setVisibility(View.GONE);
        editVideo.tinyBackImageView.setVisibility(View.GONE);
        editVideo.setUp(video_path, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST);

        offlineFileManager.saveCache(video_path);
    }

    @Override
    public void getTagData(List<TagEntitiy> datas) {
        this.datas = datas;
    }

    @Override
    public void uploadSucceed() {

        if (offlineFileEntity != null) {
            RealmUtils.getInstance().deleteOfflineFileEntity(offlineFileEntity);
            offlineFileManager.deleteOfflineCache();
        }

        if (isEndPlay) {
            presenter2.stopMonitor();
            isEndPlay = false;
        } else {
            //隐藏dialog
            mSweetAlertDialog.dismissWithAnimation();
            //发布事件（通知比赛图片列表刷新数据）
            EventBus.getDefault().post("vodeoRefresh");
            //发布事件（通知比赛列表刷新数据）
            EventBus.getDefault().post("playListRefresh");

            CommonUitls.showSweetDialog2(this, "上传视频成功", dialog -> {
                AppManager.getAppManager().killActivity(mWeakReference);//关闭当页面
                dialog.dismiss();
            });
        }
    }

    /**
     * 上传失败
     *
     * @param msg
     */
    @Override
    public void uploadFail(String msg) {

        try {
            //隐藏dialog
            mSweetAlertDialog.dismissWithAnimation();

            saveUploadOfflineFile();

            getErrorNews(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUploadOfflineFile() {

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            if (datas.size() > 0 && videoLabel.getText().toString().length() == 4) {
                for (int i = 0; i < datas.size(); i++) {
                    if (videoLabel.getText().toString().equals(datas.get(i).getName())) {
                        tagId = String.valueOf(datas.get(i).getTid());
                    }
                }
            }
        }

        offlineFileManager.convertToOffline();
        OfflineFileEntity offlineFileEntity = new OfflineFileEntity(
                String.valueOf(MonitorData.getMonitorId()),
                offlineFileManager.getFileType(),
                tagId,
                String.valueOf(Double.valueOf(CommonUitls.GPS2AjLocation(lo))),
                String.valueOf(Double.valueOf(CommonUitls.GPS2AjLocation(la))),
                we,
                t,
                wp,
                wd,
                offlineFileManager.getCache(true),
                DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DATETIME),
                String.valueOf(AssociationData.getUserId()));

        RealmUtils.getInstance().insertOfflineFileEntity(offlineFileEntity);

        LogUtil.print(GsonUtil.toJson(offlineFileEntity));
    }

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

    /**
     * 弹出dialog标签
     */
    private void showDialog() {
        dialog = new SaActionSheetDialog(VideoEditActivity.this)
                .builder();

        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                dialog.addSheetItem(datas.get(i).getName(), onSheetItemClickListener);
            }
        }

        dialog.show();
    }
}
