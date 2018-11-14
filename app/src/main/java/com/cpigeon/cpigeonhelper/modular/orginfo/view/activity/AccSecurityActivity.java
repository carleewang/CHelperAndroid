package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.ForgetPasActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.PlaySMSvalidationActivity;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.OnClick;

/**
 * 我的 -->账户安全
 * Created by Administrator on 2017/12/19.
 */

public class AccSecurityActivity extends ToolbarBaseActivity {

    private Intent intent;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_accs_ecurity;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("账户安全");
        setTopLeftButton(R.drawable.ic_back, AccSecurityActivity.this::finish);
    }

    @OnClick({R.id.ll_dlmm, R.id.ll_zfmm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_dlmm:
//                //修改登录密码
//                startActivity(new Intent(this, ModifyLogPasActivity.class));
                //忘记密码
                startActivity(new Intent(this, ForgetPasActivity.class));
                break;
            case R.id.ll_zfmm:
                //修改支付密码
                intent = new Intent(this, PlaySMSvalidationActivity.class);
                intent.putExtra("startActivityTag", 2);//1:支付页面修改密码
                startActivity(intent);
                break;
        }
    }
}
