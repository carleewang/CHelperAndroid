package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.orderfragment.AllOrderFragment;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.orderfragment.AlreadyOrderFragment;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.orderfragment.OverdueOrderFragment;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.orderfragment.WaitOrderFragment;

/**
 * 我的订单的适配器，用于显示四个fragment
 * Created by Administrator on 2017/6/14.
 */
public class MyOrderPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES;

    private Fragment[] fragments;


    public MyOrderPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        TITLES = context.getResources().getStringArray(R.array.my_order_top);
        fragments = new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = AllOrderFragment.newInstance();
                    break;
                case 1:
                    fragments[position] = WaitOrderFragment.newInstance();
                    break;

                case 2:
                    fragments[position] = AlreadyOrderFragment.newInstance();
                    break;

                case 3:
                    fragments[position] = OverdueOrderFragment.newInstance();
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
