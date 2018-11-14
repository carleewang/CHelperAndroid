package com.cpigeon.cpigeonhelper.modular.menu.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.modular.menu.presenter.BulletinPresenter;
import com.cpigeon.cpigeonhelper.modular.menu.view.adapter.BullentinAdapter;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.BulletinView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 公告通知
 * Created by Administrator on 2017/9/20.
 */

public class BulletinActivity extends ToolbarBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, BulletinView {
    @BindView(R.id.ac_bulletin_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BullentinAdapter mAdapter;//公告通知列表适配器

    private BulletinPresenter presenter;//公告通知控制层

    private boolean mIsRefreshing = false;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bulletin;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("公告通知");
        setTopLeftButton(R.drawable.ic_back, this::finish);


        presenter = new BulletinPresenter(this);//初始化公告控制层


        initRecyclerView();//初始化RecyclerView
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
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            againRequest();//重新请求数据
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();//重新请求数据
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new BullentinAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        //保存公告通知的第一条数据
        if (mAdapter.getData().size() > 0) {
            RealmUtils.preservationBulletinEntity(mAdapter.getData().get(0));
        }
        super.onDestroy();
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
        mCustomEmptyView.setEmptyImage(R.mipmap.img_tips_error_load_error);
        mCustomEmptyView.setEmptyText(tips);
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

    /**
     * 获取数据成功后返回
     *
     * @param datas 公告数据
     */
    @Override
    public void getBulletData(List<BulletinEntity> datas) {

        try {
            if (this.isDestroyed()) return;
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局

            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用
            mIsRefreshing = false;

            if (datas.size() > 0) {
                mAdapter.addData(datas);
            } else {
                initErrorView("没有公告信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void getThrowable(Throwable mThrowable) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用
            mIsRefreshing = false;

            SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                    mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                        SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
                        mSwipeRefreshLayout.setRefreshing(true);
                        mIsRefreshing = true;
                        againRequest();//重新请求数据
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void againRequest() {
        try {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            presenter.getBullentinData();//开始数据获取
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
