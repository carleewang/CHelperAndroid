package com.cpigeon.cpigeonhelper.modular.menu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.menu.presenter.SettingPresenter;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.SettingView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页
 * Created by Administrator on 2017/9/13.
 */

public class SettingActivity extends ToolbarBaseActivity implements SettingView {

    @BindView(R.id.ll_appraise)
    RelativeLayout llAppraise;
    @BindView(R.id.ll_edition)
    RelativeLayout llEdition;
    @BindView(R.id.out_login)
    Button outLogin;
    @BindView(R.id.app_edition)
    TextView appEdition;//app版本号

    private SettingPresenter presenter;//设置页控制层

    @Override
    protected void swipeBack() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("设置");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        appEdition.setText("v " + CommonUitls.getVersionName(this));//设置当前app的版本号

        presenter = new SettingPresenter(this);//初始化控制层
    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @Override
    public void succeed() {
        CommonUitls.showToast(this, "退出登录成功");

        //跳转到登录页
        AppManager.getAppManager().startLogin(getApplicationContext());

    }

    @OnClick({R.id.ll_appraise, R.id.ll_edition, R.id.out_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_appraise://应用评价
                String mAddress = "market://details?id=" + getPackageName();
                Intent marketIntent = new Intent("android.intent.action.VIEW");
                marketIntent.setData(Uri.parse(mAddress));
                startActivity(marketIntent);
                break;
            case R.id.ll_edition://当前版本

                break;
            case R.id.out_login://退出登录
                presenter.startOutLogin();
                break;
        }
    }
}
