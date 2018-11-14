package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.adapter.OpenGytOrderAdapter;
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
 * 开通鸽运通页面
 * Created by Administrator on 2017/10/26.
 */

public class OpenGytActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderPresenter presenter;//初始化控制层

    private OpenGytOrderAdapter mAdapter;

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

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTitle("开通鸽运通");
        setTopLeftButton(R.drawable.ic_back, OpenGytActivity.this::finish);

        presenter = new OrderPresenter(new OrderViewImpl() {
            /**
             * 开通鸽运通订单列表获取成功
             */
            @Override
            public void setMealGytOrderList(ApiResponse<List<PackageInfo>> listApiResponse, String msg, Throwable mThrowable) {
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
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, OpenGytActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
                    }
                }
            }
        });//初始化控制层

        initRefreshLayout();
        initRecyclerView();//初始化RecyclerView
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
            presenter.openGeYunTong("gytopen");//获取开通鸽运通列表数据

        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.openGeYunTong("gytopen");//获取开通鸽运通列表数据
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new OpenGytOrderAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 重新请求数据
     */
    private void againRequest() {
        presenter.openGeYunTong("gytopen");//获取开通鸽运通列表数据
    }


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.ORDER_REFRESH)) {
            AppManager.getAppManager().killActivity(mWeakReference);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
