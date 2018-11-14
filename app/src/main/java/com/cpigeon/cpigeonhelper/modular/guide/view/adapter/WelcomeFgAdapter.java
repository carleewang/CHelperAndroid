package com.cpigeon.cpigeonhelper.modular.guide.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.utils.CreateOneFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WelcomeFgAdapter extends FragmentPagerAdapter {


    private ArrayList<Fragment> mFragments = new ArrayList<>();


    public WelcomeFgAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragments.add(CreateOneFragment.getWelcomeFragment1());//首页fragment
        mFragments.add(CreateOneFragment.getWelcomeFragment2());//操作日志fragment
        mFragments.add(CreateOneFragment.getWelcomeFragment3());//设置fragment
        mFragments.add(CreateOneFragment.getWelcomeFragment4());//个人信息
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    private Fragment showFragment;

    @Override
    public Fragment getItem(int position) {
        showFragment = mFragments.get(position);
        return showFragment;
    }
}
