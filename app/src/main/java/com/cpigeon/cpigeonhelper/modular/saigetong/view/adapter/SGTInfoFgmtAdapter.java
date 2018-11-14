package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.utils.CreateOneFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/10.
 */

public class SGTInfoFgmtAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;

    public SGTInfoFgmtAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        this.fm = fm;
        mFragments = new ArrayList<>();
        mFragments.add(CreateOneFragment.getmZHNumFragment());//足环信息
//        mFragments.add(CreateOneFragment.getmGZNameFragment());//鸽主
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
