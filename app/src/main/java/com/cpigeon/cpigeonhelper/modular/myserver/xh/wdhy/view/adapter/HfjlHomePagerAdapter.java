package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.fragment.AlreadyPaidFailureFragment;

/**
 * Created by Administrator on 2018/3/31.
 */

public class HfjlHomePagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES;

    private Fragment[] fragments;


    public HfjlHomePagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        TITLES = context.getResources().getStringArray(R.array.hfjl_home_top);
        fragments = new Fragment[TITLES.length];
    }


    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = newInstance1();
                    break;
                case 1:
                    fragments[position] = newInstance2();
                    break;
            }
        }
        return fragments[position];
    }


    @Override
    public int getCount() {

        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return TITLES[position];
    }


    /**
     * 创建当前类对象
     */
    private AlreadyPaidFailureFragment mAlreadyPaidFragment1;
    private AlreadyPaidFailureFragment mAlreadyPaidFragment2;

    public AlreadyPaidFailureFragment newInstance1() {
        if (mAlreadyPaidFragment1 == null) {
            synchronized (AlreadyPaidFailureFragment.class) {
                if (mAlreadyPaidFragment1 == null){
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("tag", 1);
                    mAlreadyPaidFragment1 = new AlreadyPaidFailureFragment();
                    mAlreadyPaidFragment1.setArguments(mBundle);
                }
            }
        }
        return mAlreadyPaidFragment1;
    }

    public AlreadyPaidFailureFragment newInstance2() {
        if (mAlreadyPaidFragment2 == null) {
            synchronized (AlreadyPaidFailureFragment.class) {
                if (mAlreadyPaidFragment2 == null){
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("tag", 2);
                    mAlreadyPaidFragment2 = new AlreadyPaidFailureFragment();
                    mAlreadyPaidFragment2.setArguments(mBundle);
                }

            }
        }
        return mAlreadyPaidFragment2;
    }
}
