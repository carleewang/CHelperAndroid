package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * 查看选择的足环照片
 * Created by Administrator on 2018/1/15.
 */

public class CheckSelectActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_check_foot)
    TextView tvCheckFoot;//选择的足环

    @Override

    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_check_select;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, this::finish);
        setTitle("已选择足环号码");
    }

    private String strSelectFoot1;//选择的足环号码

    @Override
    protected void initViews(Bundle savedInstanceState) {
        strSelectFoot1 = getIntent().getStringExtra("strSelectFoot1");

        tvCheckFoot.setText(strSelectFoot1);
    }
}
