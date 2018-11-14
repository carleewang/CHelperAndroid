package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.SGTSearchAdapter3;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 赛鸽通搜索页
 * Created by Administrator on 2017/12/4.
 */

public class SGTSearchActivity2 extends BaseActivity implements SearchEditText.OnSearchClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;
    @BindView(R.id.edit_cancel)
    TextView editCancel;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private SGTSearchAdapter3 mAdapter;//适配器

    private SGTPresenter mSGTPresenter;//控制层

    private boolean mIsRefreshing = false;
    private int ps = 6;//页大小【一页记录条数，默认值 10】
    private int pi = 1;//页码【小于 0 时获取全部，默认值-1
    boolean canLoadMore = true, isMoreDateLoading = false;

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
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mSGTPresenter = new SGTPresenter(new SGTViewImpl() {
            //足环搜索结果成功回调
            @Override
            public void getSGTSearchData(ApiResponse<List<FootSSEntity>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (SGTSearchActivity2.this.isDestroyed()) {
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
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sggp);
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
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sggp);
                                }
                            }
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sggp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void getTagData(List<TagEntitiy> tagDatas) {
                mAdapter.setSgtTAG(tagDatas);
            }
        });
        searchEdittext.setOnSearchClickListener(this);
        initRecyclerView();
        initRefreshLayout();//刷新布局
    }

    private void againRequest() {

        clearData();
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        pi = 1;
        mSGTPresenter.getGetFootSS(pi, searchEdittext.getText().toString());//开始搜索
    }


    //搜索监听回调
    @Override
    public void onSearchClick(View view, String keyword) {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        mSGTPresenter.getGetFootSS(pi, keyword);//开始搜索
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
            pi = 1;
            mSGTPresenter.getGetFootSS(pi, searchEdittext.getText().toString());//开始搜索
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            pi = 1;
            mSGTPresenter.getGetFootSS(pi, searchEdittext.getText().toString());//开始搜索
        });
    }

    /**
     * 初始化RecyclerView
     */
    public void initRecyclerView() {
        mAdapter = new SGTSearchAdapter3(this, null, mSGTPresenter);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(SGTSearchActivity2.this, mRecyclerView);
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
            mSGTPresenter.getGetFootSS(++pi, searchEdittext.getText().toString());//开始搜索
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }

    @Override
    public void getErrorNews(String str) {
        initErrorView(str);
    }

    @OnClick({R.id.edit_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_cancel:
                AppManager.getAppManager().killActivity(mWeakReference);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    private FootSSEntity clickItem;

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(FootSSEntity clickItem) {
        this.clickItem = clickItem;
        Log.d("SGTHomeListAdapter1", "订阅返回:" + clickItem.getFoot());
    }
}
