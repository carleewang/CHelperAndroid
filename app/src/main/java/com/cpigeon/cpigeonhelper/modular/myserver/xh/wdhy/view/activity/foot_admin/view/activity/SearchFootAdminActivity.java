package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 足环 跳页面搜索  （舍弃）
 * Created by Administrator on 2018/6/15.
 */

public class SearchFootAdminActivity extends BaseActivity{

    private FootAdminPresenter mFootAdminPresenter;

    @BindView(R.id.search_edittext)
    EditText search_edittext;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sgt_search;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        search_edittext.setHint("请输入足环号");
    }


    @OnClick({R.id.edit_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_cancel:
                AppManager.getAppManager().killActivity(mWeakReference);
                break;
        }
    }

}
