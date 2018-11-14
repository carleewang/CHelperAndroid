package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.adapter.MyOrderPagerAdapter;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;

/**
 * 我的订单
 * Created by Administrator on 2017/12/19.
 */

public class MyOrderActivity extends ToolbarBaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;//导航条

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("我的订单");
        setTopLeftButton(R.drawable.ic_back, MyOrderActivity.this::finish);

        initViewPager();
    }

    /**
     * viewPager预加载
     */
    private void initViewPager() {
        MyOrderPagerAdapter mAdapter = new MyOrderPagerAdapter(getSupportFragmentManager(),
                getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);//预加载
        mViewPager.setAdapter(mAdapter);//设置关联的适配器
        mSlidingTab.setViewPager(mViewPager);//设置导航条关联的viewpager
        mViewPager.setCurrentItem(0);

        mViewPager.getCurrentItem();

    }
}
