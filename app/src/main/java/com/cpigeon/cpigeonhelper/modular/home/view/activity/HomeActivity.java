package com.cpigeon.cpigeonhelper.modular.home.view.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTHomePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.GYTHomeActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter.GYTListAdapter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTHomeView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListView;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.modular.home.presenter.HomePresenter;
import com.cpigeon.cpigeonhelper.modular.home.view.viewdao.IHomeView;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OpenGytActivity;
import com.cpigeon.cpigeonhelper.service.SingleLoginService;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.cpigeon.cpigeonhelper.utils.PermissonUtil;
import com.cpigeon.cpigeonhelper.utils.PicassoImageLoader;
import com.cpigeon.cpigeonhelper.utils.ScreenTool;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页  舍弃
 * Created by Administrator on 2017/9/5.
 */

public class HomeActivity extends ToolbarBaseActivity implements IHomeView, GYTHomeView, GYTListView {

    @BindView(R.id.home_banner)
    Banner mBanner;//轮播图
    @BindView(R.id.img_home_down_up)
    ImageButton imgHomeDownUp;
    @BindView(R.id.img_home_open_into)
    ImageButton imgHomeOpenInto;
    @BindView(R.id.img_home_menu)
    ImageButton imgHomeMenu;
    @BindView(R.id.img_home_order)
    ImageButton imgHomeOrder;
    @BindView(R.id.ac_home_buttonLl1)//底部线性布局
            LinearLayout acHomeButtonLl;
    @BindView(R.id.ac_home_buttonLl2)//底部线性布局
            LinearLayout acHomeButtonLl2;
    @BindView(R.id.ac_home_hint_ll)//隐藏，显示
            LinearLayout acHomeHintLl;
    @BindView(R.id.ac_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.home_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件

    private String TAG = "print";

    private ValueAnimator animator;//属性动画值
    private float curTranslationY;//动画Y轴移动距离

    private HomePresenter presenter;//控制层

    private long firstTime = 0;//双击返回退出应用，记录时间
    private int tag = 1;//点击图片设置tag，   1：向下   2：向上

    private boolean isOpenGyt = false;//是否开通鸽运通


    private GYTHomePresenter gytHomePresenter;//鸽运通控制层
    private GYTListPresenter listPresenter;


    private GYTListAdapter mAdapter;//比赛列表显示适配器

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("中鸽助手");
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        //登录成功，开始单点登录检查
        SingleLoginService.start(HomeActivity.this);//启动服务

        PermissonUtil.getAppDetailSettingIntent(this);//添加权限(权限检查)

        gytHomePresenter = new GYTHomePresenter(this);
//        gytHomePresenter.gytServerDatas(AssociationData.getUserType());//获取鸽运通服务数据

        presenter = new HomePresenter(this);//初始化控制层、
        presenter.getHeadData();//获取头部数据
        presenter.getGYTRaceList(3, 1);//获取鸽运通比赛列表

        listPresenter = new GYTListPresenter(this);
        initRefreshLayout();//初始化刷新控件
    }

    /**
     * 初始化刷新布局
     */
    @Override
    public void initRefreshLayout() {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            Log.d("printaa", "initRefreshLayout: ---->1");

            presenter.getGYTRaceList(3, 1);//获取鸽运通比赛列表
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d("printaa", "initRefreshLayout: ---->2");

            presenter.getGYTRaceList(3, 1);//获取鸽运通比赛列表
        });
    }

    /**
     * 显示广告
     */
    private void showAd(List<HomeAd> homeAds) {
        ViewGroup.LayoutParams lp = mBanner.getLayoutParams();
        lp.height = ScreenTool.getScreenWidth(this) / 2;

        List<HomeAd> data = new ArrayList<>();
        data.clear();
        for (HomeAd dd : homeAds) {
            if (dd.getType().equals("2")) {
                data.add(dd);
            }
        }

        mBanner.setLayoutParams(lp);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new PicassoImageLoader());
        //设置图片集合
        mBanner.setImages(data);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(5000);

        mBanner.setOnBannerClickListener(position -> {
            //Logger.d("ADINFO:adList.size()=" + adList.size() + ";position=" + position);
            if (data != null && position > 0 && position <= data.size()) {
                //点击广告
                String url = data.get(position - 1).getAdUrl();

                //判断是不是网站URL
                if (CommonTool.Compile(url, CommonTool.PATTERN_WEB_URL)) {

                } else {
                    Uri uri = Uri.parse(url);
                    if (uri.getQueryParameter("url") != null && !uri.getQueryParameter("url").equals("")) {
                        final String web_url = uri.getQueryParameter("url");
                        Intent intent2 = new Intent(HomeActivity.this, WebViewActivity.class);
                        intent2.putExtra(WebViewActivity.EXTRA_URL, web_url);
                        startActivity(intent2);
                    }
                }
            }
        });

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }


    /**
     * 轮播数据获取完成
     *
     * @param homeAds
     */
    @Override
    public void getHomeAdData(List<HomeAd> homeAds) {
        if (homeAds.size() != 0) {
            Log.d(TAG, "getHomeAdData: " + homeAds.get(0).getAdImageUrl());
            showAd(homeAds);//加载轮播图
        } else {
            Log.d(TAG, "getHomeAdData: 数据为空");
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String playListRefresh) {
        Log.d(TAG, "订阅返回");
        if (playListRefresh.equals("playListRefresh")) {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            presenter.getGYTRaceList(3, 1);//获取鸽运通比赛列表
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }


    @Override
    public void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable) {

    }

    /**
     * 鸽运通数据成功下载后回调
     */
    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {

        mSwipeRefreshLayout.setRefreshing(false);//关闭刷新动画

        if (geYunTongDatas.size() > 0) {
            acHomeHintLl.setVisibility(View.GONE);//隐藏提示信息
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new GYTListAdapter(geYunTongDatas, listPresenter);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            acHomeHintLl.setVisibility(View.VISIBLE);//隐藏提示信息
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void diZhenInfo(String diZhenStr) {

    }

    @Override
    public void ciBaoInfo(String ciBaoStr) {

    }

    @Override
    public void diZhenCiBaoInfo(String ciBaoStr) {

    }

    @Override
    public void appInfoDataReturn(ApiResponse<AppInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getReturnMsg(String msg) {

    }

    @Override
    public void addPlaySuccess() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        presenter.getGYTRaceList(3, 1);//获取鸽运通比赛列表
    }

    @OnClick({R.id.img_home_down_up, R.id.img_home_open_into, R.id.img_home_menu, R.id.img_home_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_home_down_up://点击图片，上下移动

                startAnimator();//开启动画
                break;
            case R.id.img_home_open_into://开通，进入鸽运通

                if (isOpenGyt) {//开通鸽运通

                    startActivity(new Intent(this, GYTHomeActivity.class));//跳转到鸽运通列表页面
                } else { //没有开通鸽运通
                    startActivity(new Intent(this, OpenGytActivity.class));//跳转到鸽运通列表页面
                }

                break;
            case R.id.img_home_menu://进入菜单
                startActivity(new Intent(this, MenuActivity.class));
                break;
            case R.id.img_home_order://进入订单
//                startActivity(new Intent(HomeActivity.this, OrderDetailsActivity.class));
                break;
        }
    }


    /**
     * 开启动画
     */
    private void startAnimator() {
        if (tag == 1) {//向下移动
            imgHomeDownUp.setBackground(getResources().getDrawable(R.drawable.arrow_up2x));//设置背景图片
            curTranslationY = acHomeButtonLl.getTranslationY();//获取当前空间Y方向上的值
            animator = ValueAnimator.ofFloat(curTranslationY, acHomeButtonLl.getHeight() - acHomeButtonLl2.getHeight());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    acHomeButtonLl.setTranslationY(value);
                }
            });//设置监听
            animator.setDuration(700);//设置动画执行时间
            if (!animator.isRunning()) {
                animator.start();//开始动画
                tag = 2;
            }
        } else {//向上移动
            imgHomeDownUp.setBackground(getResources().getDrawable(R.drawable.arrow_down2x));
            curTranslationY = acHomeButtonLl.getTranslationY();
            animator = ValueAnimator.ofFloat(curTranslationY, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    acHomeButtonLl.setTranslationY(value);
                }
            });
            animator.setDuration(700);
            if (!animator.isRunning()) {
                animator.start();
                tag = 1;
            }
        }
    }


    /**
     * 按两次退出应用
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {//两次按键小于2秒时，退出应用
                    AppManager.getAppManager().killAllActivity();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }


    /**
     * 三个方法实现IHomeView接口
     */
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
    public void gytStatisticalData(GYTStatisticalEntity data) {

    }

    @Override
    public void getServiceData(ApiResponse<GYTHomeEntity> dataApiResponse, String msg) {

    }

    @Override
    public void getStatisticalData(ApiResponse<GYTStatisticalEntity> dataApiRespons, String msg) {

    }
}
