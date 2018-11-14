package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.adapter.GyjlHomePagerAdapter;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 鸽友交流
 * Created by Administrator on 2018/3/22.
 */
public class GyjlHomeActivity extends BaseActivity {

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private GyjlHomePagerAdapter mHomeAdapter;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gyjl_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTitle("鸽友交流");
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initViewPager();
    }

    private void initViewPager() {
        mHomeAdapter = new GyjlHomePagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);//预加载
        mViewPager.setAdapter(mHomeAdapter);//设置关联的适配器
        mSlidingTab.setViewPager(mViewPager);//设置导航条关联的viewpager
        mViewPager.setCurrentItem(0);
    }

    @OnClick({R.id.imbtn_r})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imbtn_r:
                AppManager.getAppManager().killActivity(mWeakReference);
                break;
        }
    }
}
