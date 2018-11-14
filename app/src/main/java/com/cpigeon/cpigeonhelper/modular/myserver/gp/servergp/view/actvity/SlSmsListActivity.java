package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter.GpSmsPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.adapter.SlSmsAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.util.List;

import butterknife.BindView;


/**
 * 上笼短信列表
 * Created by Administrator on 2017/12/25.
 */

public class SlSmsListActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRefreshing = false;

    private GpSmsPresenter mGpSmsPresenter;//控制层
    private SlSmsAdapter mAdapter;//适配器

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
        setTitle("上笼短信");
        setTopLeftButton(R.drawable.ic_back, SlSmsListActivity.this::finish);

        mGpSmsPresenter = new GpSmsPresenter(new GpSmsViewImpl() {
            @Override
            public void getSmsListData_SL(ApiResponse<List<SlListEntity>> listApiResponse, String msg, Throwable mThrowable) {

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
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sldx);
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, SlSmsListActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sldx);
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
                mGpSmsPresenter.getSmsListData_SL();//获取上笼短信列表
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
                mGpSmsPresenter.getSmsListData_SL();//获取上笼短信列表
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

        //赛事规程
        mAdapter = new SlSmsAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(GameGcActivity.this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setEnableLoadMore(true);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 重新请求数据
     */
    private void againRequest() {
        mIsRefreshing = true;
        //赛事规程
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        mGpSmsPresenter.getSmsListData_SL();//获取上笼短信列表
    }
}
