package com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment.MyFlyingAreaFragment;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment.SystemFlyingAreaFragment;

/**
 * 司放地的适配器，用于显示两个fragment
 * Created by Administrator on 2017/6/14.
 */

public class FlyingAreaPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;

    private Fragment[] fragments;


    public FlyingAreaPagerAdapter(FragmentManager fm, Context context) {

        super(fm);

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            TITLES = context.getResources().getStringArray(R.array.flyingarea_sections);
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            TITLES = context.getResources().getStringArray(R.array.flying_section);
        }

        fragments = new Fragment[TITLES.length];
    }


    @Override
    public Fragment getItem(int position) {

        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = MyFlyingAreaFragment.newInstance();
                    break;
                case 1:
                    fragments[position] = SystemFlyingAreaFragment.newInstance();
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
