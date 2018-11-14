package com.cpigeon.cpigeonhelper.modular.order.view.fragment.orderfragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.adapter.OrderNewDetailsAdapter;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 待付款  我的订单
 * Created by Administrator on 2017/12/19.
 */

public class WaitOrderFragment extends BaseFragment {
    public static WaitOrderFragment newInstance() {
        return new WaitOrderFragment();
    }


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderNewDetailsAdapter mAdapter;
    private boolean mIsRefreshing = false;

    private OrderPresenter presenter;//控制层
    private int ps = 6;//页大小【一页记录条数，默认值 10】
    private int pi = -1;//页码【小于 0 时获取全部，默认值-1
    boolean canLoadMore = true, isMoreDateLoading = false;
    private int mCurrentCounter = 0;


    @Override
    public int getLayoutResId() {
        return R.layout.layout_list;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        presenter = new OrderPresenter(new OrderViewImpl() {
            /**
             * 删除订单成功
             */
            @Override
            public void playGbSucceed() {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();

                //发布事件（订单列表刷新数据）
                EventBus.getDefault().post(EventBusService.ORDER_REFRESH);

                CommonUitls.showSweetDialog(getActivity(), "删除成功");
            }

            /**
             * 删除订单失败
             *
             * @param msg
             */
            @Override
            public void playGbFail(String msg) {
                CommonUitls.showSweetDialog(getActivity(), msg);
            }

            //订单列表数据获取成功后回调
            @Override
            public void getPlayListDatas(ApiResponse<List<OrderList>> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (getActivity().isDestroyed()) {
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

                                mAdapter.getData().clear();
                                mAdapter.notifyDataSetChanged();
                                datasTag.clear();//清除数据
                                for (int i = 0; i < listApiResponse.getData().size(); i++) {
                                    //                        if (listApiResponse.getData().get(i).getStatusname().equals("待支付")) {
                                    if (listApiResponse.getData().get(i).getDdzt() == 0) {
                                        datasTag.add(listApiResponse.getData().get(i));
                                    }
                                }

                                if (datasTag.size()==0){
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_dz);
                                }else {
                                    mAdapter.setNewData(datasTag);

                                    mAdapter.notifyDataSetChanged();
                                    canLoadMore = listApiResponse.getData() != null && listApiResponse.getData().size() == ps;//数据不是空，并且数据的长度为一页能展示的条数（可以加载更多）
                                    finishTask();//加载下一页
                                }

                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_dz);
                                }
                            }
                        }else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, WaitOrderFragment.this.getActivity(), dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_dz);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        initRefreshLayout();//刷新布局
        initRecyclerView();
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        Log.d(TAG, "订阅返回");
        if (strRefresh.equals(EventBusService.ORDER_REFRESH)) {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            presenter.getOrderData(ps, pi, "");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    /**
     * 刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            try {
                mSwipeRefreshLayout.setRefreshing(true);
                mIsRefreshing = true;
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                presenter.getOrderData(ps, pi, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();

            presenter.getOrderData(ps, pi, "");
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new OrderNewDetailsAdapter(null, presenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setEnableLoadMore(true);
    }

    private List<OrderList> datasTag = new ArrayList<>();

    //订单列表数据获取成功后回调


    //重新获取数据
    private void againRequest() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();

        presenter.getOrderData(ps, pi, "");
    }

    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
        mCustomEmptyView.setEmptyImage(R.mipmap.zwdd);
        mCustomEmptyView.setEmptyText(tips);
    }


}