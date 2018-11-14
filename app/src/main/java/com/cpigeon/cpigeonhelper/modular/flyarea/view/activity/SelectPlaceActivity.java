package com.cpigeon.cpigeonhelper.modular.flyarea.view.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.SelectPlaceEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.SelectPlaceSelect;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter.SelectPlaceAdapter;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.http.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择地点界面
 * Created by Administrator on 2017/9/26.
 */

public class SelectPlaceActivity extends ToolbarBaseActivity implements AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener, SearchEditText.OnSearchClickListener {

    @BindView(R.id.it_current)
    TextView tvCurrent;//当前的位置
    @BindView(R.id.it_current_lo_la)
    TextView lolaView;//当前的经纬度
    @BindView(R.id.ac_select_place_recyview)
    RecyclerView mRecyclerView;//列表展示
    @BindView(R.id.ac_se_pl_determine)
    Button mImgBtn;//点击确定
    @BindView(R.id.ac_se_pl_search)
    Button mImgBtnSearch;//点击确定
    @BindView(R.id.search_edittext)
    SearchEditText mSearchEditText;//文本输入
    @BindView(R.id.fl_map)
    FrameLayout flMap;//地图布局
    @BindView(R.id.ll_weizhi)
    LinearLayout llWeiZhi;//当前位置布局

    private SelectPlaceAdapter mAdapter;//选择地点页地点展示适配器
    private List<SelectPlaceEntity> entityList;

    private String cityCode;//当前城市编码

    private PoiSearch poiSearch, poiSearch2;//检索对象

    private MapView mMapView = null;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private PoiSearch.Query query1;
    private PoiSearch.Query query2;

    private String strAd;//选择位置
    private double dqla;//选择点经度
    private double dqlo;//选择点纬度

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_select_place;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    public void initRecyclerView() {
        mAdapter = new SelectPlaceAdapter(null);
        entityList = new ArrayList<>();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.getItemAnimator();
        mAdapter.getPosition = new SelectPlaceAdapter.GetPosition() {
            @Override
            public void getClickPosition(int position) {
                positions = position;//保持点击的item下标
            }
        };
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("地点选择");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mSearchEditText.setOnSearchClickListener(this);//输入文本设置监听

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        startLocate();//开始定位当前位置

        // 中间点监听
        aMap.setOnCameraChangeListener(SelectPlaceActivity.this);

        //检索相关
        // 2、构造 PoiSearch.Query 对象，通过 PoiSearch.Query(String query, String ctgr, String city) 设置搜索条件。
        query1 = new PoiSearch.Query("地名", "", cityCode);
        query1.setPageSize(10);// 设置每页最多返回多少条poiitem
        query1.setPageNum(1);//设置查询页码


        //3、构造 PoiSearch 对象，并设置监听。
        poiSearch = new PoiSearch(SelectPlaceActivity.this, query1);
        poiSearch.setOnPoiSearchListener(this);
//        //4、调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
        poiSearch.searchPOIAsyn();

        initRecyclerView();//初始化RecyclerView
    }

    /**
     * 开始定位当前位置
     */
    private void startLocate() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
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
    }

    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            try {
                Log.d("dingweisa", "onLocationChanged:1 ");
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));

                MarkerOptions markerOptionStart = new MarkerOptions();
                markerOptionStart.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                markerOptionStart.title("起点");

                markerOptionStart.draggable(false);//设置Marker可拖动
                markerOptionStart.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.dp_location)));

                markerOptionStart.anchor(0.5F, 0.5F);
                aMap.addMarker(markerOptionStart);

                cityCode = aMapLocation.getCityCode();//获取当前城市编码
                //设置当前位置以及经纬度
                strAd = aMapLocation.getAoiName();
                tvCurrent.setText("选择点位置：" + strAd);//当前位置名
                dqla = aMapLocation.getLatitude();
                dqlo = aMapLocation.getLongitude();
                lolaView.setText("经度：" + GPSFormatUtils.loLaToDMS(dqlo) + "N   纬度：" + GPSFormatUtils.loLaToDMS(dqla) + "E");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 镜头变化
     * (中心点监听回调)
     *
     * @param cameraPosition 相机的位置
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 在镜头前修改完成
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;

        dqla = target.latitude;
        dqlo = target.longitude;
        lolaView.setText("经度：" + GPSFormatUtils.loLaToDMS(dqlo) + "N   纬度：" + GPSFormatUtils.loLaToDMS(dqla) + "E");

        getAddressByLatlng(target.latitude, target.longitude);

        RxUtils.delayed(300, aLong -> {
            drawMarkers(target.latitude, target.longitude);
        });
    }

    public void drawMarkers(double la, double lo) {
        //设置周边搜索的中心点以及半径
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(la, lo), 3000));
        //4、调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
        poiSearch.searchPOIAsyn();
    }

    /**
     * 检索监听的两个方法
     *
     * @param poiResult
     * @param rCode
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        List<PoiItem> poiItems = poiResult.getPois();

        try {
            strAd = poiItems.get(0).getSnippet();
            tvCurrent.setText("选择点位置：" + strAd);//当前位置名
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (poiItems.size() > 0) {
            entityList.clear();//清除之前的数据
            for (int i = 0; i < poiItems.size(); i++) {
                SelectPlaceEntity entity = new SelectPlaceEntity();//创建一个新的实体类
                entity.setLocation(poiItems.get(i).getTitle());//地点
                entity.setSnippet(poiItems.get(i).getSnippet());//地点z
                entity.setLongitude(poiItems.get(i).getLatLonPoint().getLongitude());//经度
                entity.setLatitude(poiItems.get(i).getLatLonPoint().getLatitude());//纬度
                entityList.add(entity);//向集合中添加数据
            }
            mAdapter.setNewData(entityList);//向适配器理添加数据
            mAdapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "onPoiSearched: 2");
        }
    }


    /**
     * @param lo 经度
     * @param la 纬度
     */
    private void getAddressByLatlng(double la, double lo) {
        //地理搜索类
        GeocodeSearch geocodeSearch = new GeocodeSearch(SelectPlaceActivity.this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

            //得到逆地理编码异步查询结果
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                try {

                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();

                    if (regeocodeAddress.getAois().size() != 0) {
                        strAd = regeocodeAddress.getAois().get(0).getAoiName();
                        tvCurrent.setText("选择点位置：" + strAd);//当前位置名
                    }
                    Log.d("dingweijson", "onRegeocodeSearched1: ");
                    Log.d("dingweijson", "onRegeocodeSearched:2 " + GsonUtil.toJson(regeocodeAddress));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
//        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        LatLonPoint latLonPoint = new LatLonPoint(la, lo);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {

    }

    private int positions = -1;//点击tiem位置


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        EventBus.getDefault().unregister(this);//取消注册
        mMapView.onDestroy();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @OnClick({R.id.ac_se_pl_determine, R.id.edit_cancel, R.id.ac_se_pl_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_se_pl_determine://点击确定按钮


                SelectPlaceEntity xzPlaceEntity = new SelectPlaceEntity();
                xzPlaceEntity.setLongitude(dqlo);
                xzPlaceEntity.setLatitude(dqla);
                xzPlaceEntity.setLocation(strAd);
                Intent mIntent = new Intent();
                mIntent.putExtra("selectPlaceEntity", xzPlaceEntity);
                // 设置结果，并进行传送
                this.setResult(0x0084, mIntent);
                AppManager.getAppManager().killActivity(mWeakReference);//关闭当前的activity
                break;
            case R.id.edit_cancel://输入取消
                flMap.setVisibility(View.VISIBLE);
                llWeiZhi.setVisibility(View.VISIBLE);
                mImgBtnSearch.setVisibility(View.GONE);
                mImgBtn.setVisibility(View.VISIBLE);

                // 中间点监听
                aMap.setOnCameraChangeListener(SelectPlaceActivity.this);

                break;
            case R.id.ac_se_pl_search://搜索结果返回确定，地图定位
                flMap.setVisibility(View.VISIBLE);
                llWeiZhi.setVisibility(View.VISIBLE);
                mImgBtnSearch.setVisibility(View.GONE);
                mImgBtn.setVisibility(View.VISIBLE);

                if (positions != -1) {
                    // 中间点监听
                    aMap.setOnCameraChangeListener(SelectPlaceActivity.this);
                    SelectPlaceEntity selectPlaceEntity = entityList.get(positions);

                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(selectPlaceEntity.getLatitude(), selectPlaceEntity.getLongitude())));
//                    aMap.addMarker(markerOption);
                } else {
                    positions = -1;
                    if (lo != 0) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(la, lo)));
                    }
                    // 中间点监听
                    aMap.setOnCameraChangeListener(SelectPlaceActivity.this);
//                    CommonUitls.showSweetAlertDialog(this, "温馨提示", "请选择地点后，再继续");
                }
                break;
        }
    }

    /**
     * 输入文本监听
     *
     * @param view
     * @param keyword
     */
    private PoiSearch poiSearchs;
    private PoiSearch.Query querys;
    private double lo;//搜索位置的经度
    private double la;//搜索位置的纬度

    @Override
    public void onSearchClick(View view, String keyword) {
        aMap.setOnCameraChangeListener(null);

        DistrictSearch search = new DistrictSearch(mContext);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(keyword);//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult districtResult) {
                ArrayList<DistrictItem> districtItems = districtResult.getDistrict();

                flMap.setVisibility(View.GONE);
                llWeiZhi.setVisibility(View.GONE);
                mImgBtnSearch.setVisibility(View.VISIBLE);
                mImgBtn.setVisibility(View.GONE);

                try {
                    strAd = districtItems.get(0).getName() + "" + districtItems.get(0).getSubDistrict().get(0).getName();
                    tvCurrent.setText("选择点位置：" + strAd);//当前位置名
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < districtItems.size(); i++) {

                    //视图移动到中心位置
                    la = districtItems.get(i).getCenter().getLatitude();
                    lo = districtItems.get(i).getCenter().getLongitude();
                    entityList.clear();//清除之前的数据
                    int sizes = districtItems.get(i).getSubDistrict().size();
                    for (int y = 0; y < sizes; y++) {
                        SelectPlaceEntity entity = new SelectPlaceEntity();//创建一个新的实体类
                        entity.setLocation(districtItems.get(i).getSubDistrict().get(y).getName());//地点
                        entity.setSnippet(districtItems.get(i).getName() + "" + districtItems.get(i).getSubDistrict().get(y).getName());//地点2

                        entity.setLongitude(districtItems.get(i).getSubDistrict().get(y).getCenter().getLongitude());//经度
                        entity.setLatitude(districtItems.get(i).getSubDistrict().get(y).getCenter().getLatitude());//纬度)
                        entityList.add(entity);//向集合中添加数据
                    }
                    mAdapter.setNewData(entityList);//向适配器理添加数据
                    mAdapter.notifyDataSetChanged();
                }
            }
        });//绑定监听器
        search.searchDistrictAnsy();//开始搜索
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(SelectPlaceSelect item) {
        try {
            strAd = item.getAd();
            tvCurrent.setText("选择点位置：" + strAd);//当前位置名

            dqla = item.getLa();
            dqlo = item.getLo();

            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(dqla, dqlo)));
//            getAddressByLatlng(item.getLa(), item.getLo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

