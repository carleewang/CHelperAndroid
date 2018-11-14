package com.cpigeon.cpigeonhelper.modular.menu.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;
import com.cpigeon.cpigeonhelper.modular.menu.presenter.LogbookPresenter;
import com.cpigeon.cpigeonhelper.modular.menu.view.adapter.LogbookAdapter;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.LogbookView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 操作日志Activity  舍弃（之前版本）
 * Created by Administrator on 2017/9/13.
 */
public class LogbookActivity extends ToolbarBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, LogbookView {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefreshing = false;
    private LogbookAdapter mAdapter;
    private int ps = 6;//页大小【一页记录条数，默认值 10】
    private int pi = 1;//页码【小于 0 时获取全部，默认值-1
    boolean canLoadMore = true, isMoreDateLoading = false;

    private LogbookPresenter presenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_rv_srl;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("操作日志");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        initRefreshLayout();//刷新布局
        initRecyclerView();
        presenter = new LogbookPresenter(this);//初始化控制层
    }


    /**
     * 刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            presenter.getLogboodData(ps, pi);//开始取数据
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            presenter.getLogboodData(ps, pi);//开始取数据
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new LogbookAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(LogbookActivity.this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setEnableLoadMore(true);
        setRecycleNoScroll();
    }

    @Override
    public void loadData() {

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


    private void clearData() {
        mIsRefreshing = true;
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            mSwipeRefreshLayout.setEnabled(false);
            isMoreDateLoading = true;
            presenter.getLogboodData(ps, pi);
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }


    /**
     * 获取数据成功
     *
     * @param msg
     */
    @Override
    public void getDatas(ApiResponse<List<LogbookEntity>> listApiResponse, String msg, Throwable mThrowable) {

        try {
            if (LogbookActivity.this.isDestroyed()) {
                return;
            }

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
                        if (listApiResponse.getData().size() == 0 && mAdapter.getData().size() == 0) {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                        } else {
                            mAdapter.addData(listApiResponse.getData());
                            mAdapter.notifyDataSetChanged();
                            canLoadMore = listApiResponse.getData() != null && listApiResponse.getData().size() == ps;//数据不是空，并且数据的长度为一页能展示的条数（可以加载更多）
                            finishTask();//加载下一页
                        }
                    } else {
                        if (mAdapter.getData().size() > 0) {
                            mAdapter.loadMoreEnd(false);//加载更多的结束
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                        }
                    }
                } else {
                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //重新请求数据
    private void againRequest() {
        clearData();
        presenter.getLogboodData(ps, pi);//开始取数据
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
    public void getErrorNews(String str) {
        super.getErrorNews(str);
        initErrorView(str);
    }
}
