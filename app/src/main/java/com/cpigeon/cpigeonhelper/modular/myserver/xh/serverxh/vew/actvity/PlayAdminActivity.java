package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.XsSmsListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.PlayAdminPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter.PlayAdminAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdaoimpl.PlayAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * 比赛管理
 * Created by Administrator on 2017/12/18.
 */

public class PlayAdminActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRefreshing = false;

    private PlayAdminPrensenter mPlayAdminPrensenter;//控制层

    private PlayAdminAdapter mAdapter;//适配器

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (getIntent().getIntExtra("service_type", -1) == 1) { // 1：比赛管理   2：短信设置
            setTitle("比赛管理");
        } else {
            setTitle("短信设置");
        }

        setTopLeftButton(R.drawable.ic_back, PlayAdminActivity.this::finish);

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mPlayAdminPrensenter = new PlayAdminPrensenter(new PlayAdminViewImpl() {
            /**
             * 获取比赛管理列表数据
             *
             * @param msg
             */
            @Override
            public void getPlayAdminData(ApiResponse<List<PlayListEntity>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用
                    mIsRefreshing = false;

                    if (mThrowable != null) {//抛出异常
                        SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                                mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                                    SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    mIsRefreshing = true;
                                    againRequest();//重新请求数据
                                });
                    } else {//成功获取到数据
                        if (listApiResponse.getErrorCode() == 0) {
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
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, PlayAdminActivity.this, dialog -> {
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

                }

            }
        });
        initRecyclerView();
        initRefreshLayout();
    }

    //重新请求数据
    private void againRequest() {
        //赛事规程
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        mPlayAdminPrensenter.getPlayList();//获取比赛列表数据
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String playListRefresh) {
        if (playListRefresh.equals("playAdminDataRefresh")) {
            againRequest();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
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
            mPlayAdminPrensenter.getPlayList();//获取比赛列表数据
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            //赛事规程
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mPlayAdminPrensenter.getPlayList();//获取比赛列表数据
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {

        //赛事规程
        mAdapter = new PlayAdminAdapter(null, getIntent().getIntExtra("service_type", -1));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(GameGcActivity.this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setEnableLoadMore(true);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setRecycleNoScroll();
    }

    /**
     * hl  设置滚动事件
     */
    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }

    /**
     * 显示错误信息
     *
     * @param tips
     */
    public void initErrorView(String tips) {
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.face);
        mCustomEmptyView.setEmptyText(tips);
    }
}
