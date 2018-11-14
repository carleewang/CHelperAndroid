package com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment;

import android.os.Bundle;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter.FlyingAreaPagerAdapter;
import com.cpigeon.cpigeonhelper.ui.NoScrollViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;

/**
 * 司放地主页Fragment
 * Created by Administrator on 2017/6/14.
 */

public class FlyingAreaHomeFragment extends BaseFragment {


    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;//导航条
    @BindView(R.id.view_pager)
    NoScrollViewPager mViewPager;//ViewPager

    public static FlyingAreaHomeFragment newInstance() {
        return new FlyingAreaHomeFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_car_pager;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initViewPager();
    }


    private void initViewPager() {

        FlyingAreaPagerAdapter mHomeAdapter = new FlyingAreaPagerAdapter(getChildFragmentManager(),
                getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);//预加载
        mViewPager.setAdapter(mHomeAdapter);//设置关联的适配器
        mSlidingTab.setViewPager(mViewPager);//设置导航条关联的viewpager
        mViewPager.setCurrentItem(0);
    }

}
