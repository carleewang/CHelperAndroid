package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.home.view.frigment.InfoDetailsFragment;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.UserInfoPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.adapter.MyGebiAdapter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的-->我的鸽币
 * Created by Administrator on 2017/12/19.
 */

public class MyGeBiActivity extends ToolbarBaseActivity implements UserInfoView {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefreshing = false;

    @BindView(R.id.tv_my_gebi)
    TextView myGebi;//我的鸽币数量

    private UserInfoPresenter mUserInfoPresenter;//控制层

    private MyGebiAdapter mAdapter;//适配器

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_gebi;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("我的鸽币");
        setTopLeftButton(R.drawable.ic_back, MyGeBiActivity.this::finish);
        myGebi.setText(InfoDetailsFragment.mygebi);

        initRecyclerView();
        initRefreshLayout();
        mUserInfoPresenter = new UserInfoPresenter(this);

    }

    /**
     * 账户信息 余额 鸽币
     */
    @Override
    public void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg) {
        try {
            if (myInfoApiResponse.getErrorCode() == 0) {
                myGebi.setText("" + myInfoApiResponse.getData().getGebi());

                mIsRefreshing = true;
                //赛事规程
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                mUserInfoPresenter.getGbDataMx();//获取鸽币明细
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mUserInfoPresenter.getMyInfo();//获取我的信息
    }

    private Intent intent;

    @OnClick({R.id.btn_gbdh, R.id.btn_gbhq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gbdh:
                //鸽币兑换
                intent = new Intent(this, GebiAcquireActivity.class);
                intent.putExtra("bgType", "gbdh");
                startActivity(intent);
                break;
            case R.id.btn_gbhq:
                //鸽币获取
                intent = new Intent(this, GebiAcquireActivity.class);
                intent.putExtra("bgType", "gbhq");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void getGbmxData(ApiResponse<List<GbListEntity>> listApiResponse, String msg, Throwable mThrowable) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用

            if (mThrowable != null) {//抛出异常
                SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                        mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
                            mSwipeRefreshLayout.setRefreshing(true);
                            againRequest();//重新请求数据
                        });

            } else {//成功获取到数据
                if (listApiResponse.getErrorCode() == 0) {
                    if (listApiResponse.getData().size() > 0) {
                        if (listApiResponse.getData().size() == 0 && mAdapter.getData().size() == 0) {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                        } else {
                            mAdapter.setNewData(listApiResponse.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (mAdapter.getData().size() > 0) {

                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                        }
                    }
                } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, MyGeBiActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                } else {
                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void againRequest() {

        try {
            mIsRefreshing = true;
            //赛事规程
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mUserInfoPresenter.getGbDataMx();//获取鸽币明细
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {

    }

    /**
     * 刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        //自动刷新
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            mUserInfoPresenter.getGbDataMx();//获取鸽币明细
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();//重新请求数据
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        //赛事规程
        mAdapter = new MyGebiAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}
