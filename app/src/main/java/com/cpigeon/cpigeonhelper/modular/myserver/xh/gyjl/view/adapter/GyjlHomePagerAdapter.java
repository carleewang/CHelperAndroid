package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.fragment.GyjlMessageFragment;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.fragment.GyjlReviewFragment;

/**
 * 鸽友交流，用于显示两个fragment
 * Created by Administrator on 2017/6/14.
 */

public class GyjlHomePagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;

    private Fragment[] fragments;


    public GyjlHomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.gyjl_home_top);
        fragments = new Fragment[TITLES.length];
    }


    @Override
    public Fragment getItem(int position) {

        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = this.newMesInstance();
                    break;
                case 1:
                    fragments[position] = this.newRevInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    private GyjlMessageFragment myGyjlMessageFragment;//留言
    private GyjlReviewFragment myGyjlReviewFragment;//评论


    /**
     * 创建
     */
    private GyjlMessageFragment newMesInstance() {
        if (myGyjlMessageFragment == null) {
            synchronized (GyjlMessageFragment.class) {
                if (myGyjlMessageFragment == null)
                    myGyjlMessageFragment = new GyjlMessageFragment();
            }
        }
        return myGyjlMessageFragment;
    }

    /**
     * 创建
     */
    private GyjlReviewFragment newRevInstance() {
        if (myGyjlReviewFragment == null) {
            synchronized (GyjlMessageFragment.class) {
                if (myGyjlReviewFragment == null)
                    myGyjlReviewFragment = new GyjlReviewFragment();
            }
        }
        return myGyjlReviewFragment;
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
