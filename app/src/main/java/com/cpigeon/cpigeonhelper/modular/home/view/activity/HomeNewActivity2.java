package com.cpigeon.cpigeonhelper.modular.home.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TabEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.presenter.HomePresenter;
import com.cpigeon.cpigeonhelper.modular.home.view.adapter.HomeFgmtAdapter;
import com.cpigeon.cpigeonhelper.modular.home.view.viewdao.IHomeViewImpl;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.modular.menu.presenter.BulletinPresenter;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.BulletinActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.SignActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.BulletinView;
import com.cpigeon.cpigeonhelper.service.SingleLoginService;
import com.cpigeon.cpigeonhelper.ui.CustomViewPager;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.PermissonUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/7.
 */

public class HomeNewActivity2 extends ToolbarBaseActivity implements BulletinView {

    @BindView(R.id.home_tabs)
    CommonTabLayout commonTabLayout;//导航条
    @BindView(R.id.home_view_pager)
    CustomViewPager mViewPager;//ViewPager

    private long firstTime = 0;//双击返回退出应用，记录时间

    //导航条
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home, R.mipmap.tab_logbook,
            R.mipmap.tab_setting, R.mipmap.tab_wode};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_click, R.mipmap.tab_logbook_click,
            R.mipmap.tab_setting_click, R.mipmap.tab_wode_click};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    //公告通知
    private BulletinPresenter mBulletinPresenter;
    private HomePresenter mHomePresenter;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_home_new;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        PermissonUtil.getAppDetailSettingIntent(this);//权限检查（添加权限）

//        toolbar.setBackgroundColor(this.getResources().getColor(R.color.colorWhite));
////        toolbar.setPopupTheme(R.style.ToolbarItemColorTheme2);
////        //初始化ToolBar
//
//        tvTitle.setTextColor(this.getResources().getColor(R.color.black));

        setTopRightButton("签到", () -> {
            //签到
            startActivity(new Intent(HomeNewActivity2.this, SignActivity.class));

//            Intent intent = new Intent(HomeNewActivity.this, WebViewActivity.class);
//            intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.APP_SIGN_URL + AssociationData.getUserId());
//            startActivity(intent);
        });

        //初始化公告通知
        mBulletinPresenter = new BulletinPresenter(this);
        mBulletinPresenter.getBullentinData();

        setTopLeftButton(R.mipmap.hone_top_news_red, new ToolbarBaseActivity.OnClickListener() {
            @Override
            public void onClick() {
                if (getTopLeftButton() == R.mipmap.hone_top_news_red) {
                    HomeNewActivity2.this.setTopLeftButton(R.mipmap.hone_top_news);
                }
                startActivity(new Intent(HomeNewActivity2.this, BulletinActivity.class));
            }
        });

        //登录成功，开始单点登录检查
        SingleLoginService.start(HomeNewActivity2.this);//启动服务
        initViewPager();//初始化ViewPager

        mHomePresenter = new HomePresenter(new IHomeViewImpl() {
            @Override
            public void appInfoDataReturn(ApiResponse<AppInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (listApiResponse.getErrorCode() == 0) {
                        double gebi = Double.valueOf(listApiResponse.getData().getGebi());
                        if (gebi != 0) {
                            Toast.makeText(HomeNewActivity2.this,msg,Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mHomePresenter.setAppInfo();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        for (int i = 0; i < mIconUnselectIds.length; i++) {
            mTabEntities.add(new TabEntity("", mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mViewPager.setScanScroll(false);//禁止滑动
        mViewPager.setOffscreenPageLimit(4);//预加载
        mViewPager.setAdapter(new HomeFgmtAdapter(HomeNewActivity2.this.getSupportFragmentManager()));

        commonTabLayout.setTabData(mTabEntities);

        //切换fragment，导航条变化
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //点击导航条，fragment切换
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);//view默认显示第一个页面
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.setCurrentItem(0);//view默认显示第一个页面
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
                    CommonUitls.showToast(this, "再按一次退出程序");
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
     * 公告通知数据获取成功返回
     */
    @Override
    public void getBulletData(List<BulletinEntity> list) {
        if (list.size() > 0) {
            if (RealmUtils.getInstance().existBulletinEntity()) {//数据库是否存在公告通知数据
                //数据库存在公告数据
                if (RealmUtils.getInstance().getBulletinEntityTime().equals(list.get(0).getTime())) {
                    //保存的数据与服务器公告信息的数据一致
                    HomeNewActivity2.this.setTopLeftButton(R.mipmap.hone_top_news);
                } else {
                    //保存的数据与服务器公告信息的数据不同
                    HomeNewActivity2.this.setTopLeftButton(R.mipmap.hone_top_news_red);
                }
            } else {
                //数据库不存在公告数据
                HomeNewActivity2.this.setTopLeftButton(R.mipmap.hone_top_news_red);
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
