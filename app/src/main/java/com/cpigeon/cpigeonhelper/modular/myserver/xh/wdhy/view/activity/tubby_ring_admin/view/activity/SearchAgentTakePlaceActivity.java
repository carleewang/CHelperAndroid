package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity;

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
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.adapter.FootAdminHomeListAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 足环搜索
 * Created by Administrator on 2018/6/15.
 */

public class SearchAgentTakePlaceActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;

    private FootAdminPresenter mFootAdminPresenter;
    private int pi = 1, ps = 35;
    boolean canLoadMore = true, isMoreDateLoading = false;
    private FootAdminHomeListAdapter mAdapter;
    private boolean mIsRefreshing = false;

    private String y = "";//年份
    private String s = "2";////是否出售：0为未售出，1为已出售  2全部
    private String j = "2";//是否缴费：0为未缴费，1为已交费，2为全部

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
        searchEdittext.setHint("请输入足环号");


        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {
            @Override
            public void getFootAdminList(ApiResponse<FootAdminListDataEntity> listApiResponse, String msg, Throwable mThrowable) {

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

                            if (listApiResponse.getData().getFootlist().size() > 0) {
                                if (listApiResponse.getData().getFootlist().size() == 0 && mAdapter.getData().size() == 0) {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_rz);
                                } else {
                                    mAdapter.addData(listApiResponse.getData().getFootlist());
                                    mAdapter.notifyDataSetChanged();
                                    canLoadMore = listApiResponse.getData() != null && listApiResponse.getData().getFootlist().size() == ps;//数据不是空，并且数据的长度为一页能展示的条数（可以加载更多）
                                    finishTask();//加载下一页
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_zwzh);
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, SearchAgentTakePlaceActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_zwzh);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                againRequest();
            }
        });

        initRecyclerView();

    }


    private String serchStr = "";

    //请求数据
    private void againRequest() {
        try {
            pi = 1;
            ps = 35;

            mSwipeRefreshLayout.setRefreshing(true);
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局

            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
//            mFootAdminPresenter.getXHHYGL_ZHGL_GetList(y, "特比环", s, pi, ps, j);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        //足环管理列表
        mAdapter = new FootAdminHomeListAdapter(null,"特比环");
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (canLoadMore) {
                    mSwipeRefreshLayout.setEnabled(false);
                    isMoreDateLoading = true;
//                    mFootAdminPresenter.getXHHYGL_ZHGL_GetList(y, "特比环", s, pi, ps, "");
                } else {
                    mAdapter.setEnableLoadMore(false);
                }
            }
        }, mRecyclerView);
        mAdapter.setEnableLoadMore(true);
    }

    /**
     * 刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        //自动刷新
        mSwipeRefreshLayout.post(() -> {
            try {
                againRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            try {
                againRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 加载下一页
     */
    @Override
    public void finishTask() {
        if (canLoadMore) {
            pi++;
            mAdapter.loadMoreComplete();//负载更完整
        } else {
            mAdapter.loadMoreEnd(false);//加载更多的结束
        }
        isMoreDateLoading = mIsRefreshing = false;
        mSwipeRefreshLayout.setRefreshing(false);//设置刷新
        mSwipeRefreshLayout.setEnabled(true);//设置启用
        mIsRefreshing = false;
        hideEmptyView();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
