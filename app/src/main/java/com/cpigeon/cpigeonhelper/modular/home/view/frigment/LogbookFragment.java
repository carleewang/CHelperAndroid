package com.cpigeon.cpigeonhelper.modular.home.view.frigment;

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
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;
import com.cpigeon.cpigeonhelper.modular.menu.presenter.LogbookPresenter;
import com.cpigeon.cpigeonhelper.modular.menu.view.adapter.LogbookAdapter;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.LogbookViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.util.List;

import butterknife.BindView;

/**
 * 主页日志Fragment
 * Created by Administrator on 2017/11/23.
 */
public class LogbookFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

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
    public int getLayoutResId() {
        return R.layout.layout_rv_srl;
    }

    @Override
    public void finishCreateView(Bundle state) {

        mSwipeRefreshLayout.setBackgroundColor(getResources().getColor(R.color.color_ebebeb));
        initRefreshLayout();//刷新布局
        initRecyclerView();
        presenter = new LogbookPresenter(new LogbookViewImpl() {

            /**
             * 获取数据成功
             */
            @Override
            public void getDatas(ApiResponse<List<LogbookEntity>> listApiResponse, String msg, Throwable mThrowable) {

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
                                if (listApiResponse.getData().size() == 0 && mAdapter.getData().size() == 0) {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_rz);
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
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_rz);
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity(), dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_rz);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });//初始化控制层
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
            againRequest();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new LogbookAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    @Override
    public void onResume() {
        super.onResume();
        againRequest();
    }

    //重新请求数据
    private void againRequest() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();

        ps = 10;
        pi = 1;
        clearData();
        presenter.getLogboodData(ps, pi);//开始取数据
    }
}