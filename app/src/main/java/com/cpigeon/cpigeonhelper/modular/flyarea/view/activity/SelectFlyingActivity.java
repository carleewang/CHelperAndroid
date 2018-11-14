package com.cpigeon.cpigeonhelper.modular.flyarea.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment.MyFlyingAreaFragment;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.service.RequestCodeService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 选择司放地界面（选择训放地界面）
 * Created by Administrator on 2017/9/28.
 */
public class SelectFlyingActivity extends ToolbarBaseActivity {

    private int faid = 0;//序号（获取第一个item的序号，添加司放地需要）

    private LocalBroadcastManager localBroadcastManager;//应用内广播管理器
    private BroadcastReceiver mBroadcastReceiver;//广播

    private MyFlyingAreaFragment homeFragment;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_select_flying;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle("选择司放地");
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("选择训放地");
        }

        setTopLeftButton(R.drawable.ic_back, this::finish);

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTopRightButton("添加", new OnClickListener() {
            @Override
            public void onClick() {
                //跳转到添加司放地页面
                Intent intent = new Intent(SelectFlyingActivity.this, AddFlyingAreaActivity.class);
                intent.putExtra("faid", faid);
                startActivityForResult(intent, RequestCodeService.SELECTFLYING_ADD_SFD_XFD);
            }
        });

        initBroRec();//初始化广播接收器
        initFragments();//显示fragment
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RequestCodeService.ADD_SFD_XFD) {
                //发布事件
                EventBus.getDefault().post(data.getParcelableExtra("FlyingAreaEntity"));
                AppManager.getAppManager().killActivity(SelectFlyingActivity.this.mWeakReference);//关闭当前页面
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(FlyingAreaEntity item) {
        AppManager.getAppManager().killActivity(mWeakReference);//关闭当前页面
    }

    /**
     * 初始化广播接收器
     */
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
        homeFragment = MyFlyingAreaFragment.newInstance();

        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, homeFragment)
                .show(homeFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

}
