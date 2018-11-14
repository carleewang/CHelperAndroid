package com.cpigeon.cpigeonhelper.modular.menu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于界面
 * Created by Administrator on 2017/6/20.
 */
public class AboutActivity extends ToolbarBaseActivity {
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.tv_copyright)
    TextView tv_copyright;

    @Override
    protected void swipeBack() {
//        Slidr.attach(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("关于我们");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        tvVersionName.setText("V " + CommonUitls.getVersionName(this));//设置版本号
        tv_copyright.setText(String.valueOf("Copyright © 2006 - " + DateUtils.getCurrentTimeYear() + " All rights reserved."));
    }

    @OnClick({R.id.tv_function_intro, R.id.tv_user_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_function_intro:
                //功能介绍
                Intent intent1 = new Intent(AboutActivity.this, WebViewActivity.class);
                intent1.putExtra(WebViewActivity.EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.FUNCTION_TYPE);
                startActivity(intent1);
                break;
            case R.id.tv_user_agreement:
                //用户协议
                Intent intent2 = new Intent(AboutActivity.this, WebViewActivity.class);
                intent2.putExtra(WebViewActivity.EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.USER_TYPE);
                intent2.putExtra(WebViewActivity.EXTRA_TITLE, "用户协议");
                startActivity(intent2);
                break;
        }
    }
}
