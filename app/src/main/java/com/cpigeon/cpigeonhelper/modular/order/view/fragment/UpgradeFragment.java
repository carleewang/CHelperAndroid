package com.cpigeon.cpigeonhelper.modular.order.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.adapter.UpgradeAdapter;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.util.List;

import butterknife.BindView;


/**
 * 升级页面
 * Created by Administrator on 2017/10/30.
 */

public class UpgradeFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private UpgradeAdapter mAdapter;

    private OrderPresenter presenter;

    public static UpgradeFragment newInstance() {
        return new UpgradeFragment();
    }

    @Override
    public int getLayoutResId() {
//        return R.layout.fragment_upgrade;
        return R.layout.layout_rv_srl;
    }

    @Override
    public void finishCreateView(Bundle state) {

        presenter = new OrderPresenter(new OrderViewImpl() {

            /**
             * 获取升级套餐信息回调
             */
            @Override
            public void setMealGytOrderList(ApiResponse<List<PackageInfo>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (UpgradeFragment.this.getActivity().isDestroyed()) return;

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
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, UpgradeFragment.this.getActivity(), dialog -> {
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
        });//初始化控制层

        initRefreshLayout();
        initRecyclerView();//初始化RecyclerView


    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new UpgradeAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            presenter.getUpgradelInfo();//获取升级套餐信息

        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getUpgradelInfo();//获取升级套餐信息
        });
    }

    /**
     * 重新获取数据
     */
    private void againRequest() {
        presenter.getUpgradelInfo();//获取升级套餐信息
    }
}
