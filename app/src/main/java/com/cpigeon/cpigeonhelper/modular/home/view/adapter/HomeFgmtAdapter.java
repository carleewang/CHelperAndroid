package com.cpigeon.cpigeonhelper.modular.home.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.modular.home.view.frigment.HomeFragment;
import com.cpigeon.cpigeonhelper.modular.home.view.frigment.InfoDetailsFragment;
import com.cpigeon.cpigeonhelper.modular.home.view.frigment.LogbookFragment;
import com.cpigeon.cpigeonhelper.modular.home.view.frigment.SettingFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/10.
 */

public class HomeFgmtAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private FragmentManager fm;

    public HomeFgmtAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        mFragments = new ArrayList<>();
//        mFragments.add(CreateOneFragment.getmHomeFragment());//首页fragment
//        mFragments.add(CreateOneFragment.getmLogbookFragment());//操作日志fragment
//        mFragments.add(CreateOneFragment.getmSettingFragment());//设置fragment
//        mFragments.add(CreateOneFragment.getmInfoDetailsFragment());//个人信息

//        mFragments.add(new HomeFragment3());//首页fragment
        mFragments.add(new HomeFragment());//首页fragment
        mFragments.add(new LogbookFragment());//操作日志fragment
        mFragments.add(new SettingFragment());//设置fragment
        mFragments.add(new InfoDetailsFragment());//个人信息
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
