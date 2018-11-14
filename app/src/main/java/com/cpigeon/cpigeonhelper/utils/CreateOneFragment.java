package com.cpigeon.cpigeonhelper.utils;

import android.os.Bundle;

import com.cpigeon.cpigeonhelper.modular.guide.view.fragment.WelcomeFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.fragment.GZNameFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.fragment.ZHNumFragment;

/**
 * fragment 单例创建
 * Created by Administrator on 2017/11/24.
 */

public class CreateOneFragment {

//
//    //创建主页fragment
//    private static HomeFragment3 mHomeFragment;
//
//    /**
//     * 创建主页fragment对象
//     */
//    public static HomeFragment3 getmHomeFragment() {
////        if (mHomeFragment == null) {
////            mHomeFragment = new HomeFragment3();
////        }
//        mHomeFragment = new HomeFragment3();
//        return mHomeFragment;
//    }
//
//
//    //创建操作日志fragent
//    private static LogbookFragment mLogbookFragment;
//
//    /**
//     * 创建操作日志fragment对象
//     */
//    public static LogbookFragment getmLogbookFragment() {
////        if (mLogbookFragment == null) {
////            mLogbookFragment = new LogbookFragment();
////        }
//        mLogbookFragment = new LogbookFragment();
//        return mLogbookFragment;
//    }
//
//
//    //创建fragment
//    private static SettingFragment mSettingFragment;
//
//    /**
//     * 创建当前类对象
//     */
//    public static SettingFragment getmSettingFragment() {
////        if (mSettingFragment == null) {
////            mSettingFragment = new SettingFragment();
////        }
//        mSettingFragment = new SettingFragment();
//        return mSettingFragment;
//    }
//
//
//
//
//    //创建fragment
//    private static InfoDetailsFragment mInfoDetailsFragment;
//
//    /**
//     * 创建当前类对象
//     */
//    public static InfoDetailsFragment getmInfoDetailsFragment() {
////        if (mInfoDetailsFragment == null) {
////            mInfoDetailsFragment = new InfoDetailsFragment();
////        }
//        mInfoDetailsFragment = new InfoDetailsFragment();
//        return mInfoDetailsFragment;
//    }


    //-------------------------------------------赛格通-------------------------------------------

    //创建fragment
    private static GZNameFragment mGZNameFragment;

    /**
     * 创建当前类对象
     */
    public static GZNameFragment getmGZNameFragment() {
        if (mGZNameFragment == null) {
            mGZNameFragment = new GZNameFragment();
        }
        return mGZNameFragment;
    }


    //创建fragment
    private static ZHNumFragment mZHNumFragment;
//    private int positionHolder;

    /**
     * 创建当前类对象
     */
    public static ZHNumFragment getmZHNumFragment() {
        if (mZHNumFragment == null) {
            mZHNumFragment = new ZHNumFragment();
        }
        return mZHNumFragment;
    }

    //==============================================引导页==================================================

    private static WelcomeFragment mWelcomeFragment1;

    //引导页Fragment
    public static WelcomeFragment getWelcomeFragment1() {

        if (mWelcomeFragment1 == null) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("tag", 1);
            mWelcomeFragment1 = new WelcomeFragment();
            mWelcomeFragment1.setArguments(mBundle);
        }
        return mWelcomeFragment1;
    }


    private static WelcomeFragment mWelcomeFragment2;

    //引导页Fragment
    public static WelcomeFragment getWelcomeFragment2() {

        if (mWelcomeFragment2 == null) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("tag", 2);
            mWelcomeFragment2 = new WelcomeFragment();
            mWelcomeFragment2.setArguments(mBundle);
        }
        return mWelcomeFragment2;
    }


    private static WelcomeFragment mWelcomeFragment3;

    //引导页Fragment
    public static WelcomeFragment getWelcomeFragment3() {

        if (mWelcomeFragment3 == null) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("tag", 3);
            mWelcomeFragment3 = new WelcomeFragment();
            mWelcomeFragment3.setArguments(mBundle);
        }
        return mWelcomeFragment3;
    }


    private static WelcomeFragment mWelcomeFragment4;

    //引导页Fragment
    public static WelcomeFragment getWelcomeFragment4() {

        if (mWelcomeFragment4 == null) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("tag", 4);
            mWelcomeFragment4 = new WelcomeFragment();
            mWelcomeFragment4.setArguments(mBundle);
        }
        return mWelcomeFragment4;
    }

}
