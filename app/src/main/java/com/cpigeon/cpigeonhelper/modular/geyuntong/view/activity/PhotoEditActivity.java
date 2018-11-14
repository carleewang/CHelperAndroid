package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.cpigeon.cpigeonhelper.camera.util.BitmapUtils;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.OfflineFileEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTMonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.UploadImgVideoPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.ImgVideoViewImpl;
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
import com.cpigeon.cpigeonhelper.utils.glide.GlideCacheUtil;
import com.cpigeon.cpigeonhelper.utils.http.GsonUtil;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.utils.CommonUitls.showLoadSweetAlertDialog;

/**
 * 图片编辑上传
 * Created by Administrator on 2017/10/18.
 */
public class PhotoEditActivity extends ToolbarBaseActivity {

    @BindView(R.id.img_label)
    TextView imgLabel;//标签
    @BindView(R.id.ll_label)
    LinearLayout llLabel;//点击添加标签
    @BindView(R.id.edit_img)
    ImageView editImg;//需要上传的图片
    @BindView(R.id.photo_edit_mapview)
    MapView mMapView;//地图展示


    @BindView(R.id.water_life_top)
    TextView waterLifeTop;

    private UploadImgVideoPresenter presenter;//控制层
    private GYTMonitorPresenter presenter2;//鸽车监控控制层

    private double lo = 0, la = 0;//经纬度

    private SaActionSheetDialog dialog;//选择标签

    private AMap aMap;

    private String TAG = "PhotoEditActivitys";

    private MyLocationStyle myLocationStyle;//设置定位蓝点

    OfflineFileManager offlineFileManager;
    OfflineFileEntity offlineFileEntity;
    boolean isOfflineUpload = false;

    String tagId;
    private boolean isEndPlay = false;//是否结束比赛

    //定位相关
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            try {
                address = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();//地址
                altitude = aMapLocation.getAltitude();//高度
                lo = aMapLocation.getLongitude();
                la = aMapLocation.getLatitude();
                //天气查询
                initWeatherSearch(aMapLocation.getCity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private String address;//地址
    private double altitude;//高度

    /**
     * 初始化天气搜索相关
     */
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private String img_path;
    private View view;
    private Bitmap watermarkBitmap1;
    private Bitmap watermarkBitmap2;


    /**
     * 天气查询
     *
     * @param city 城市
     */
    public void initWeatherSearch(String city) {
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        mweathersearch = new WeatherSearch(PhotoEditActivity.this);
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


    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_photo_edit;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        isEndPlay = false;
    }

    private String we = "暂无", t = "暂无", wp = "暂无", wd = "暂无";//we：天气名称 t：温度   wp：风力 wd：风向

    private boolean uploadNetworkTag = false;//上传网络状态保存
    private SweetAlertDialog hintDialog;//网络状态提示框

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //初始化toobal

        offlineFileEntity = getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        isOfflineUpload = getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);

        setTitle("上传图片");
        setTopLeftButton(R.drawable.ic_back, () -> isOut());//是否结束当前页面

        setTopRightButton("上传", new OnClickListener() {
            @Override
            public void onClick() {

                if (AntiShake.getInstance().check()) {
                    return;
                }

                int APNType = NetStateUtils.getAPNType(PhotoEditActivity.this);
                if (APNType == 0) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "当前暂无网络", PhotoEditActivity.this);
                    return;
                }

                if (!uploadNetworkTag) {
                    //上传时网络判断
                    if (NetStateUtils.getAPNType(PhotoEditActivity.this) != 1) {
                        hintDialog = CommonUitls.showSweetDialog(PhotoEditActivity.this, "您正在使用手机流量进行上传", "开始上传", "停止上传", dia -> {
                            uploadNetworkTag = true;//确定为流量上传
                            hintDialog.dismiss();//当前提示框关闭
                            uploadPhoto();//上传图片
                        });
                        return;
                    }
                }

                uploadPhoto();//上传图片
            }
        });

        datas = new ArrayList<>();//初始化存放标签数据集合

        presenter = new UploadImgVideoPresenter(new ImgVideoViewImpl() {

            /**
             * 上传失败
             */
            @Override
            public void uploadFail(String msg) {
                try {
                    mSweetAlertDialog.dismissWithAnimation();

                    PhotoEditActivity.this.getErrorNews(msg);

                    saveUploadOfflineFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 获取标签数据成功
             *
             * @param datas
             */
            @Override
            public void getTagData(List<TagEntitiy> datas) {
                PhotoEditActivity.this.datas = datas;
            }

            /**
             * 上传成功
             */
            @Override
            public void uploadSucceed() {

                try {
                    if (isEndPlay) {
                        presenter2.stopMonitor();
                        isEndPlay = false;
                    } else {
                        //隐藏提示框
                        mSweetAlertDialog.dismissWithAnimation();

                        //发布事件（通知比赛图片列表刷新数据）
                        EventBus.getDefault().post("photoRefresh");

                        CommonUitls.showSweetDialog2(PhotoEditActivity.this, "上传图片成功", dialog -> {
                            dialog.dismiss();
                            if (offlineFileEntity != null) {
                                RealmUtils.getInstance().deleteOfflineFileEntity(offlineFileEntity);
                                offlineFileManager.deleteOfflineCache();
                                GlideCacheUtil.getInstance().clearImageAllCache(getBaseContext());
                            }
                            AppManager.getAppManager().killActivity(mWeakReference);//关闭当页面
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        presenter2 = new GYTMonitorPresenter(new MonitorViewImpl() {
            /**
             * 结束比赛成功后回调
             */
            @Override
            public void succeed() {
                mSweetAlertDialog.dismissWithAnimation();

                try {
                    Log.d("prints", "上传成功");
                    SweetAlertDialog dialog = new SweetAlertDialog(PhotoEditActivity.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("温馨提示");
                    dialog.setContentText("上传图片成功,已结束比赛");
                    dialog.setConfirmText("确定");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //发布事件（通知比赛图片列表刷新数据）
                            EventBus.getDefault().post("photoRefresh");

                            //结束比赛
                            EventBus.getDefault().post("endPlay");

                            //发布事件（通知比赛列表刷新数据）
                            EventBus.getDefault().post("playListRefresh");

                            AppManager.getAppManager().killActivity(mWeakReference);//关闭当页面
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            if (isOfflineUpload) {
                img_path = offlineFileEntity.getPath();
            } else {
                img_path = getIntent().getStringExtra("img_path");

            }

            if (RealmUtils.getServiceType().equals("geyuntong")) {
                //鸽运通
                presenter.getTag();//获取标签数据
            } else if (RealmUtils.getServiceType().equals("xungetong")) {
                //训鸽通
                llLabel.setVisibility(View.GONE);
            }
            offlineFileManager = new OfflineFileManager(getBaseContext()
                    , String.valueOf(MonitorData.getMonitorId()), OfflineFileManager.TYPE_IMG);

            if (img_path != null) {
                offlineFileManager.saveCache(img_path);
                LogUtil.print("show_img :" + img_path);
                editImg.setImageBitmap(BitmapFactory.decodeFile(img_path));//照片展示
            } else {
                LogUtil.print("show_img : 为空");
            }

            //初始化定位
            if (isOfflineUpload) {
                mMapView.setVisibility(View.GONE);
                la = Double.parseDouble(offlineFileEntity.getLa());
                lo = Double.parseDouble(offlineFileEntity.getLo());
                t = offlineFileEntity.getT();
                tagId = offlineFileEntity.getTagid();
                wd = offlineFileEntity.getWd();
                we = offlineFileEntity.getWe();
                wp = offlineFileEntity.getWp();

            } else {
                MapUtil.initLocation(savedInstanceState, mMapView, aMap, mLocationClient, mLocationListener, mLocationOption, myLocationStyle);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        isOut();//是否结束当前页面
    }

    /**
     * 是否结束当前页面
     */
    private void isOut() {
        if (img_path == null || img_path.isEmpty()) {
            AppManager.getAppManager().killActivity(mWeakReference);
        } else {
            SweetAlertDialog dialog = new SweetAlertDialog(PhotoEditActivity.this, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("温馨提示");
            dialog.setContentText("图片还未开始上传，是否继续退出");
            dialog.setCancelable(true);
            dialog.setConfirmText("确定");
            dialog.setCancelText("取消");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                    saveUploadOfflineFile();
                    AppManager.getAppManager().killActivity(mWeakReference);
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * 上传图片
     */
    private void uploadPhoto() {

        mSweetAlertDialog = new SweetAlertDialog(PhotoEditActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通上传图片
            gytUploadPhoto();

        } else if (RealmUtils.getServiceType().equals("xungetong")) {

            String uploadPath = img_path;
            if (StringValid.isStringValid(offlineFileManager.getCache(true))) {
                uploadPath = offlineFileManager.getCache(true);
            }

            //上传图片
            mSweetAlertDialog = showLoadSweetAlertDialog(mSweetAlertDialog);
            //训鸽通上传图片
            presenter.uploadImgVideo("image",////文件类型【image video】
                    "",////标签 ID
                    new File(uploadPath),//图片的文件路径
                    Double.valueOf(CommonUitls.GPS2AjLocation(lo)),//经度(安捷格式)
                    Double.valueOf(CommonUitls.GPS2AjLocation(la)),//纬度（安捷格式）
                    we,//we：天气名称
                    t,//温度
                    wp,//风力
                    wd,//风向
                    1,
                    offlineFileEntity != null ? String.valueOf(offlineFileEntity.getTime())
                            : DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DATETIME)
                    , null
            );
        }
    }

    //鸽运通上传图片
    private void gytUploadPhoto() {
        if (imgLabel.getText().toString().length() != 4) {
            CommonUitls.showToast(this, "请选择标签后上传");

            dialog = new SaActionSheetDialog(PhotoEditActivity.this)
                    .builder();

            if (datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    dialog.addSheetItem(datas.get(i).getName(), onSheetItemClickListener);
                }
            }
            dialog.show();
            return;
        }


        if (datas.size() > 0 && imgLabel.getText().toString().length() == 4) {
            for (int i = 0; i < datas.size(); i++) {
                if (imgLabel.getText().toString().equals(datas.get(i).getName())) {

                    if (imgLabel.getText().toString().equals("司放瞬间")) {
                        int posi = i;
                        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("温馨提示");
                        dialog.setContentText("是否在发布成功后结束监控?");
                        dialog.setConfirmText("确定");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Log.d("aaasd", "onClick: 111");

                                try {
                                    isEndPlay = true;

                                    //上传图片
                                    mSweetAlertDialog = showLoadSweetAlertDialog(mSweetAlertDialog);

                                    waterLifeTop.setText(imgLabel.getText());
                                    byte[] uplodImg = BitmapUtils.Bitmap2Bytes(
                                            BitmapUtils.createBitmapLeftTop(
                                                    BitmapFactory.decodeFile(img_path), BitmapUtils.getViewBitmap(waterLifeTop)));

                                    presenter.uploadImgVideo("image",////文件类型【image video】
                                            datas.get(posi).getTid() + "",////标签 ID
                                            new File(""),//图片的文件路径
                                            Double.valueOf(CommonUitls.GPS2AjLocation(lo)),//经度(安捷格式)
                                            Double.valueOf(CommonUitls.GPS2AjLocation(la)),//纬度（安捷格式）
                                            we,//we：天气名称
                                            t,//温度
                                            wp,//风力
                                            wd,//风向
                                            1,
                                            offlineFileEntity != null ? String.valueOf(offlineFileEntity.getTime()) : null
                                            , uplodImg
                                    );
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.setCancelText("取消");
                        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Log.d("aaasd", "onClick: 222");
                                isEndPlay = false;

                                try {
                                    //上传图片
                                    mSweetAlertDialog = showLoadSweetAlertDialog(mSweetAlertDialog);

                                    waterLifeTop.setText(imgLabel.getText());

                                    byte[] uplodImg = BitmapUtils.Bitmap2Bytes(
                                            BitmapUtils.createBitmapLeftTop(
                                                    BitmapFactory.decodeFile(img_path), BitmapUtils.getViewBitmap(waterLifeTop)));

                                    presenter.uploadImgVideo("image",////文件类型【image video】
                                            datas.get(posi).getTid() + "",////标签 ID
                                            new File(""),//图片的文件路径
                                            Double.valueOf(CommonUitls.GPS2AjLocation(lo)),//经度(安捷格式)
                                            Double.valueOf(CommonUitls.GPS2AjLocation(la)),//纬度（安捷格式）
                                            we,//we：天气名称
                                            t,//温度
                                            wp,//风力
                                            wd,//风向
                                            1,
                                            offlineFileEntity != null ? String.valueOf(offlineFileEntity.getTime()) : null
                                            , uplodImg
                                    );
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.setCancelable(true);
                        dialog.show();
                    } else {

                        try {
                            waterLifeTop.setText(imgLabel.getText());

                            byte[] uplodImg = BitmapUtils.Bitmap2Bytes(
                                    BitmapUtils.createBitmapLeftTop(
                                            BitmapFactory.decodeFile(img_path), BitmapUtils.getViewBitmap(waterLifeTop)));


                            mSweetAlertDialog = CommonUitls.showLoadSweetAlertDialog(mSweetAlertDialog);

                            //上传图片
                            presenter.uploadImgVideo("image",////文件类型【image video】
                                    datas.get(i).getTid() + "",////标签 ID
                                    new File(img_path),//图片的文件路径
                                    Double.valueOf(CommonUitls.GPS2AjLocation(lo)),//经度(安捷格式)
                                    Double.valueOf(CommonUitls.GPS2AjLocation(la)),//纬度（安捷格式）
                                    we,//we：天气名称
                                    t,//温度
                                    wp,//风力
                                    wd,//风向
                                    1,
                                    offlineFileEntity != null ? String.valueOf(offlineFileEntity.getTime()) : null
                                    , uplodImg
                            );
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
            }
        }
    }


    private SweetAlertDialog mSweetAlertDialog;

    private List<LocalMedia> list = new ArrayList<>();

    @OnClick({R.id.edit_img, R.id.ll_label})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_img:
                //图片查看详情
                if (img_path != null) {
                    list.clear();//清空之前保存的数据
                    LocalMedia localMedia = new LocalMedia();
                    LogUtil.print("大图 :" + img_path);
                    localMedia.setPath(img_path);
                    list.add(localMedia);
                    PictureSelector.create(PhotoEditActivity.this)
                            .externalPicturePreview(0, list);
                }
                break;
            case R.id.ll_label://选择标签
                dialog = new SaActionSheetDialog(PhotoEditActivity.this)
                        .builder();
                if (datas.size() > 0) {
                    for (int i = 0; i < datas.size(); i++) {
                        dialog.addSheetItem(datas.get(i).getName(), onSheetItemClickListener);
                    }
                }
                dialog.show();
                break;
        }
    }

    /**
     * 弹出选择标签
     */
    private SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {

            if (datas.size() > 0) {
                imgLabel.setText(datas.get(which - 1).getName());
            }
        }
    };

    private List<TagEntitiy> datas;//标签数据


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

    private void saveUploadOfflineFile() {
        try {
            int tagId = 0;

            if (datas.size() > 0 && imgLabel.getText().toString().length() == 4) {
                for (int i = 0; i < datas.size(); i++) {
                    if (imgLabel.getText().toString().equals(datas.get(i).getName())) {
                        tagId = datas.get(i).getTid();
                    }
                }
            }

            offlineFileManager.convertToOffline();
            OfflineFileEntity offlineFileEntity = new OfflineFileEntity(
                    String.valueOf(MonitorData.getMonitorId()),
                    offlineFileManager.getFileType(),
                    String.valueOf(tagId),
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (mMapView != null) {
                //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
                mMapView.onDestroy();
            }

            if (mLocationClient != null) {
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
