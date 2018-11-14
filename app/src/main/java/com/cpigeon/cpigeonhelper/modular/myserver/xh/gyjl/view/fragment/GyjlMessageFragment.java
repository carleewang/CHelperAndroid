package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlMessageEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.presenter.GyjlPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.adapter.MessageAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.viewdao.GyjlViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DpSpUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.util.List;

import butterknife.BindView;

/**
 * 鸽友交流 留言
 * Created by Administrator on 2018/3/22.
 */
public class GyjlMessageFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private GyjlPresenter mGyjlPresenter;
    private MessageAdapter mAdapter;

    private boolean mIsRefreshing = true;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_message;
//        return R.layout.layout_rv_srl;
    }

    @Override
    public void finishCreateView(Bundle state) {

        mGyjlPresenter = new GyjlPresenter(new GyjlViewImpl() {

            //管理员回复回调
            @Override
            public void getTraPLResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    switch (listApiResponse.getErrorCode()) {
                        case 0:
                            againRequest();
                            break;
                    }
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity());//弹出提示
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //删除留言结果
            @Override
            public void getDelPLResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    switch (listApiResponse.getErrorCode()) {
                        case 0:
                            againRequest();
                            break;
                    }
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity());//弹出提示
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getLyList(ApiResponse<List<GyjlMessageEntity>> listApiResponse, String msg, Throwable mThrowable) {

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
                                    showErrorView();
                                } else {
                                    mAdapter.setNewData(listApiResponse.getData());
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    showErrorView();
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, GyjlMessageFragment.this.getActivity(), dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            showErrorView();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        );

        initRecyclerView();
        initRefreshLayout();
    }

    private void showErrorView() {
        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_gyly_ly);
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
                mSwipeRefreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                mGyjlPresenter.getLY_XH();//获取用户给协会的留言
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            try {
                mIsRefreshing = true;
                //赛事规程
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                mGyjlPresenter.getLY_XH();//获取用户给协会的留言
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {

        mAdapter = new MessageAdapter(null, mGyjlPresenter);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(getActivity(), mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        View view = new View(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpSpUtil.dip2px(getActivity(), 10)));
        view.setBackgroundColor(getResources().getColor(R.color.color_f0f0f0));
        mAdapter.addHeaderView(view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setEnableLoadMore(true);

        mRecyclerView.requestFocus();

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    /**
     * 重新请求数据
     */
    private void againRequest() {
        mIsRefreshing = true;
//        //赛事规程
//        mAdapter.getData().clear();
//        mAdapter.notifyDataSetChanged();
        mGyjlPresenter.getLY_XH();//获取用户给协会的留言
    }


}
