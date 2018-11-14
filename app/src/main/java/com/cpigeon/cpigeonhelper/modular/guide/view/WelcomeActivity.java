package com.cpigeon.cpigeonhelper.modular.guide.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.guide.view.adapter.WelcomeFgAdapter;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * 欢迎页
 * Created by Administrator on 2017/9/5.
 */

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.id_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.circles)
    LinearLayout circles;

    private int NUM_PAGES = 4;//引导页数量

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 1);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mViewPager.setAdapter(new WelcomeFgAdapter(WelcomeActivity.this.getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);//view默认显示第一个页面

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        buildCircles();
    }

    //动态添加小圆点
    private void buildCircles() {
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0; i < NUM_PAGES - 1; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.button_welcome_dot);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }
        setIndicator(0);
    }

    private void setIndicator(int index) {
        if (index < NUM_PAGES) {
            for (int i = 0; i < NUM_PAGES - 1; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(R.color.text_selected));
                } else {
                    circle.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }
}
