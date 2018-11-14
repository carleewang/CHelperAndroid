package com.cpigeon.cpigeonhelper.message.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;


/**
 * Created by Zhu TingYu on 2017/11/15.
 */

public class FragmentParentActivity extends BaseActivity {

    public static String KEY_FRAGMENT = "KEY_FRAGMENT";

    BaseFragment baseFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_with_toolbar_layout;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        Class clz = (Class) getIntent().getSerializableExtra(KEY_FRAGMENT);

        String cls = clz.getName();

        setContentView(R.layout.activity_with_toolbar_layout);
        Fragment fragment = Fragment.instantiate(getActivity(), cls);
        if (fragment instanceof BaseFragment)
            baseFragment = (BaseFragment) fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_holder, fragment, cls);
        ft.commitAllowingStateLoss();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(baseFragment!=null)
                baseFragment.onActivityResult(requestCode,resultCode,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}
