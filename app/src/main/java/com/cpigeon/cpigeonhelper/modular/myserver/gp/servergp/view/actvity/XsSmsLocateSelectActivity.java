package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.MapUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.RequestCodeService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 训赛短信定位选择
 * Created by Administrator on 2018/3/29.
 */

public class XsSmsLocateSelectActivity extends ToolbarBaseActivity {

    @BindView(R.id.mapview)
    MapView mMapView;//地图展示
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_lo)
    TextView tvLo;
    @BindView(R.id.tv_la)
    TextView tvLa;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private AMap aMap;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private MyLocationStyle myLocationStyle;//设置定位蓝点

    private String address;
    private double lo = -1, la = -1;//东经，北纬


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_xssms_locate;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("我的位置");
        setTopLeftButton(R.drawable.ic_back, XsSmsLocateSelectActivity.this::finish);

        MapUtil.initLocation(savedInstanceState, mMapView, aMap, mLocationClient, mLocationListener, mLocationOption, myLocationStyle);
    }

    //定位相关
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            try {
                address = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();//地址
                tvAddress.setText(address);//位置
                tvLo.setText(String.valueOf("东经：" + CommonUitls.GPS2AjLocation(aMapLocation.getLongitude())));
                tvLa.setText(String.valueOf("北纬：" + CommonUitls.GPS2AjLocation(aMapLocation.getLatitude())));
                lo = Double.valueOf(CommonUitls.GPS2AjLocation(aMapLocation.getLongitude()));
                la = Double.valueOf(CommonUitls.GPS2AjLocation(aMapLocation.getLatitude()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @OnClick({R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                //点击确定
                if (lo == -1 || la == -1) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "暂未获取到定位信息", this);
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("lo", lo);
                intent.putExtra("la", la);
                setResult(RequestCodeService.XS_SMS_SET, intent);
                AppManager.getAppManager().killActivity(mWeakReference);

                break;
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
}
