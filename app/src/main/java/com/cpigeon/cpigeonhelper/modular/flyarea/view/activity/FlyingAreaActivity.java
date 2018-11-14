package com.cpigeon.cpigeonhelper.modular.flyarea.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.FrameLayout;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment.FlyingAreaHomeFragment;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * 司放地界面（训放地界面）
 * Created by Administrator on 2017/6/14.
 */

public class FlyingAreaActivity extends ToolbarBaseActivity {

    private FlyingAreaHomeFragment homeFragment;

    private LocalBroadcastManager localBroadcastManager;//应用内广播管理器
    private BroadcastReceiver mBroadcastReceiver;//广播

    private int faid;//序号（Fragemnt传递过来，添加司放地需要）


    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_car_pager;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle("司放地管理");
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("训放地管理");
        }

        setTopLeftButton(R.drawable.ic_back, this::finish);
//        setTopRightButton("添加", R.mipmap.add, new OnClickListener() {
        setTopRightButton("添加", R.mipmap.top_add, new OnClickListener() {
            @Override
            public void onClick() {
                //跳转到添加司放地页面
                Intent intent = new Intent(FlyingAreaActivity.this, AddFlyingAreaActivity.class);
                intent.putExtra("faid", faid);
                startActivity(intent);
            }
        });
        initFragments();
        initBroRec();//初始化应用内广播加收器


    }

    private void initBroRec() {
        //注册应用内广播接收器(刷新布局,编辑页面修改，删除后刷新)
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                faid = intent.getIntExtra("msgs", 0);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("modular.flyarea.view.activity.MyFlyingAreaFragment.action.FlyingAreaActivity");
        localBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void initFragments() {
        homeFragment = FlyingAreaHomeFragment.newInstance();

        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, homeFragment)
                .show(homeFragment).commit();
    }

    /**
     * 解决App重启后导致Fragment重叠的问题
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
