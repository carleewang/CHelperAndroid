package com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment.DetailsFragment2;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment.PhotoFragment;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment.VideoFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/10.
 */

public class PigMonFgmtAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;

    public PigMonFgmtAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        this.fm = fm;
        mFragments = new ArrayList<>();
        mFragments.add(DetailsFragment2.newInstance());//详情fragment
        mFragments.add(PhotoFragment.newInstance());//照片fragment
        mFragments.add(VideoFragment.newInstance());//视频fragment
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

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
