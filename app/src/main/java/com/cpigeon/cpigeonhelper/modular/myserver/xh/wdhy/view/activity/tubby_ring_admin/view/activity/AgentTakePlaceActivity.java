package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.adapter.AgentTakePlaceAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 代售点 列表
 * Created by Administrator on 2018/6/15.
 */

public class AgentTakePlaceActivity extends ToolbarBaseActivity {

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private FootAdminPresenter mFootAdminPresenter;
    private AgentTakePlaceAdapter mAdapter;

    private String type = "";

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agent_take_place;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, AgentTakePlaceActivity.this::finish);
        setTitle("代售点（0）");
        type = getIntent().getStringExtra("type");

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {

            @Override
            public void getAgentTakePlaceList(ApiResponse<List<AgentTakePlaceListEntity>> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用

                    if (mThrowable != null) {//抛出异常
                        SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                                mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                                    SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    againRequest();//重新清除数据
                                });
                    } else {//成功获取到数据
                        if (listApiResponse.getErrorCode() == 0) {

                            try {
                                setTitle("代售点(" + listApiResponse.getData().size() + ")");
                            } catch (Exception e) {
                                setTitle("代售点(0)");
                            }

                            if (listApiResponse.getData().size() > 0) {
                                mAdapter.getData().clear();//清空数据
                                mAdapter.setNewData(listApiResponse.getData());//设置数据
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, AgentTakePlaceActivity.this, dialog -> {
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
        });

        initRefreshLayout();
        initRecyclerView();//初始化RecyclerView

    }


    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new AgentTakePlaceAdapter(null, type);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            againRequest();
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();
        });
    }

    private void againRequest() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局

            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mFootAdminPresenter.getAgentTakePlace_list("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent intent;

    @OnClick({R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                intent = new Intent(AgentTakePlaceActivity.this, AddAgentTakePlaceActivity.class);
                intent.putExtra("type", "add");
                startActivity(intent);
                break;
        }
    }


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.AGENT_TAKE_PLACE_REFRESH)) {
            againRequest();//刷新
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
