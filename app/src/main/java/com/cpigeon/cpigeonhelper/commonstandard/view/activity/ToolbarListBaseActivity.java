package com.cpigeon.cpigeonhelper.commonstandard.view.activity;

import android.os.Bundle;

import com.cpigeon.cpigeonhelper.R;

/**
 * Created by Administrator on 2018/3/24.
 */

public abstract class ToolbarListBaseActivity extends ToolbarBaseActivity {

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_rv_srl;
    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }
}
