package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.RenewalFragment;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.UpgradeFragment;

/**
 * 续费升级的适配器，用于显示两个fragment
 * Created by Administrator on 2017/6/14.
 */

public class RenUpgPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES;

    private Fragment[] fragments;


    public RenUpgPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        TITLES = context.getResources().getStringArray(R.array.reup_sections);
        fragments = new Fragment[TITLES.length];
    }


    @Override
    public Fragment getItem(int position) {

        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = RenewalFragment.newInstance();
                    break;
                case 1:
                    fragments[position] = UpgradeFragment.newInstance();
                    break;
                default:
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
}
