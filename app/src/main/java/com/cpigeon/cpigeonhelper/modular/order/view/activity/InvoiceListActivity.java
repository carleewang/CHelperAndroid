package com.cpigeon.cpigeonhelper.modular.order.view.activity;

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
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.NeedInvoice;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.adapter.InvoiceAdapter;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
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

/**
 * 发票列表
 * Created by Administrator on 2018/6/13.
 */

public class InvoiceListActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private OrderPresenter mOrderPresenter;
    private InvoiceAdapter mAdapter;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_list2;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTopLeftButton(R.drawable.ic_back, InvoiceListActivity.this::finish);
        setTitle("发票信息");

        setTopRightButton("添加", () -> {
            Intent intent = new Intent(InvoiceListActivity.this, InvoiceSetActivity.class);
            startActivity(intent);
        });

        mOrderPresenter = new OrderPresenter(new OrderViewImpl() {

            @Override
            public void getThrowable(Throwable throwable) {
                InvoiceListActivity.this.getThrowable(throwable);
            }

            @Override
            public void getErrorNews(String str) {
                InvoiceListActivity.this.getErrorNews(str);
            }

            @Override
            public void getInvoiceList(ApiResponse<List<InvoiceEntity>> listApiResponse, String msg, Throwable mThrowable) {
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
                                mAdapter.setNewData(listApiResponse.getData());
                                mAdapter.notifyDataSetChanged();
                            } else {
                                showErrorView();
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, InvoiceListActivity.this, dialog -> {
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
        });

        initRecyclerView();
        initRefreshLayout();

    }

    //错误提示
    private void showErrorView() {
        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_order_invoice);
    }

    private void againRequest() {
        try {
            //赛事规程
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mOrderPresenter.getUser_Invoice_GetList();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                againRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new InvoiceAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());

        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setEnableLoadMore(true);
    }

    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);//隐藏没有列表的情况
        mRecyclerView.setVisibility(View.VISIBLE);//显示有列表数据的情况
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String mStr) {
      if(mStr.equals(EventBusService.ORDER_INVOICE_REFRESH)){
          againRequest();
      }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(NeedInvoice mNeedInvoice) {
       if(mNeedInvoice.isNeedInvoice()){
           AppManager.getAppManager().killActivity(mWeakReference);
       }
    }
}
