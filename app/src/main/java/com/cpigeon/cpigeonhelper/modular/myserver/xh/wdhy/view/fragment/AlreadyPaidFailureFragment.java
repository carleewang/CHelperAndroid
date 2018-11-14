package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter.AlreadyPaidFailureAdapter1;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter.AlreadyPaidFailureAdapter2;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * 已缴纳 tag = 1 ,未缴纳  tag  ==2;
 * Created by Administrator on 2018/3/31.
 */

public class AlreadyPaidFailureFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int tag;//1 已缴费  2： 未缴费

    private AlreadyPaidFailureAdapter1 mAdapter1;
    private AlreadyPaidFailureAdapter2 mAdapter2;


    @Override
    public int getLayoutResId() {
        return R.layout.layout_list;
//        return R.layout.activity_hyzc_home;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
        tag = getArguments().getInt("tag");
        mSwipeRefreshLayout.setEnabled(false);
        switch (tag) {
            case 1:
                initRecyclerView1();
                break;
            case 2:
                initRecyclerView2();
                break;
        }

    }


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(HFInfoEntity mHFInfoEntity) {
        Log.d(TAG, "订阅返回" + mHFInfoEntity.getTags());
        try {
            mAdapter1.notifyDataSetChanged();
            mAdapter1.getData().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mAdapter2.notifyDataSetChanged();
            mAdapter2.getData().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }


        SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
        switch (tag) {

            case 1:
                switch (mHFInfoEntity.getTags()) {
                    case 1:

                        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无已缴费记录");
                        break;
                    case 2:
                        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "请检查网络设置");
                        break;
                    default:
                        if (mHFInfoEntity.getYjflist() != null && mHFInfoEntity.getYjflist().size() > 0) {
                            try {
                                mAdapter1.setNewData(mHFInfoEntity.getYjflist());
                                mAdapter1.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无已缴费记录");
                        }
                }
                break;
            case 2:
                switch (mHFInfoEntity.getTags()) {
                    case 1:
                        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无未缴费记录");
                        break;
                    case 2:
                        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "请检查网络设置");
                        break;
                    default://正常数据
                        if (mHFInfoEntity.getWjflist() != null && mHFInfoEntity.getWjflist().size() > 0) {
                            try {
                                mAdapter2.setNewData(mHFInfoEntity.getWjflist());
                                mAdapter2.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无未缴费记录");
                        }

                }

                break;
        }
    }


    /**
     * 初始化RecyclerView
     */
    public void initRecyclerView1() {
        mAdapter1 = new AlreadyPaidFailureAdapter1(null);
        mAdapter1.setLoadMoreView(new CustomLoadMoreView());

        mAdapter1.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter1.setEnableLoadMore(true);

    }

    /**
     * 初始化RecyclerView
     */
    public void initRecyclerView2() {
        mAdapter2 = new AlreadyPaidFailureAdapter2(null);
        mAdapter2.setLoadMoreView(new CustomLoadMoreView());
        mAdapter2.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter2.setEnableLoadMore(true);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
