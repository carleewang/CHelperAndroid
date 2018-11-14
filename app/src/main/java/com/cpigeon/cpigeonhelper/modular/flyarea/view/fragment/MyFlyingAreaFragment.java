package com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.presenter.FlyingPresenter;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter.FlyingAreaAdapter;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao.FlyingViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * 我的司放地（我的训放地）
 * Created by Administrator on 2017/6/14.
 */

public class MyFlyingAreaFragment extends BaseFragment implements SearchEditText.OnSearchClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;//列表
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;//错误图展示
    @BindView(R.id.fly_area_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;//文本输入框

    private FlyingPresenter presenter;//控制层
    private String key = "";//查询关键字
    private int pi = 1;//pi  页码【小于 0 时获取全部，默认值-1】
    private int ps = 10;//ps 页大小【一页记录条数，默认值 10】

    boolean canLoadMore = true;//可以加载更多


    private String bearingActivity = "";

    public static MyFlyingAreaFragment newInstance() {
        return new MyFlyingAreaFragment();
    }

    private FlyingAreaAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.layout_swipwithrecycler;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        bearingActivity = getActivity().getClass().getSimpleName();//获取当前的Activity的类名
        presenter = new FlyingPresenter(new FlyingViewImpl() {

            @Override
            public void getFlyingData(ApiResponse<List<FlyingAreaEntity>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用

                    if (mThrowable != null) {//抛出异常

                        SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                                mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                                    againRequest();//重新请求数据
                                });

                    } else {//成功获取到数据
                        if (listApiResponse.getErrorCode() == 0) {
                            if (listApiResponse.getData().size() > 0) {
                                if (listApiResponse.getData().size() == 0 && mAdapter.getData().size() == 0) {
                                    showErrorView();
                                } else {
                                    mAdapter.addData(listApiResponse.getData());
                                    mAdapter.notifyDataSetChanged();
                                    canLoadMore = listApiResponse.getData() != null && listApiResponse.getData().size() == ps;//数据不是空，并且数据的长度为一页能展示的条数（可以加载更多）
                                    finishTask();//加载下一页

                                    //发送广播，告诉当前的Activity序号，添加需要
                                    Intent intent = new Intent("modular.flyarea.view.activity.MyFlyingAreaFragment.action.FlyingAreaActivity");
                                    intent.putExtra("msgs", listApiResponse.getData().get(0).getFaid());//发送第一个item的序号，
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);//发送应用内广播
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    showErrorView();
                                }
                            }
                        } else {
                            showErrorView();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });//初始化控制层

        initRefreshLayout();//初始化刷新布局
        initRecyclerView();//初始化ResyclerView
        searchEdittext.setOnSearchClickListener(this);//输入文本设置监听
        isPrepared = true;//准备好
        lazyLoad();

    }


    //显示错误视图
    private void showErrorView() {
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sfd);
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_xfd);
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String listRefresh) {
        //结束比赛成功返回
        if (listRefresh.equals(EventBusService.FLYING_LIST_REFRESH)) {
            againRequest();//重新请求数据
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {//准备，可见
            return;
        }
        isPrepared = false;
    }

    @Override
    protected void initRecyclerView() {
        mAdapter = new FlyingAreaAdapter(null, bearingActivity);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());//设置加载更多的视图
        mAdapter.setOnLoadMoreListener(MyFlyingAreaFragment.this, mRecyclerView);//设置加载更多的监听器
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//打开加载动画
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.getItemAnimator();
        mAdapter.setEnableLoadMore(true);//设置启用加载更多
//        setRecycleNoScroll();
    }

    /**
     * 加载下一页
     */
    @Override
    public void finishTask() {
        if (canLoadMore) {//可以加载更多
            pi++;//页数+1
            mAdapter.loadMoreComplete();//负载更完整
        } else {
            mAdapter.loadMoreEnd(false);//加载更多的结束
        }

        mSwipeRefreshLayout.setRefreshing(false);//设置刷新
        mSwipeRefreshLayout.setEnabled(true);//设置启用

        hideEmptyView();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            mSwipeRefreshLayout.setEnabled(false);
            startRequest(key);//开始请求数据
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }

    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        //第一次进入刷新
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            againRequest();
        });

        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();//重新请求数据
        });
    }

    /**
     * 重新请求数据
     */
    private void againRequest() {
        mAdapter.getData().clear();//清除所有数据
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);
        SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
        pi = 1;
        ps = 10;
        startRequest(searchEdittext.getText().toString());//开始请求数据
    }

    /**
     * 开始请求数据
     */
    private void startRequest(String strKey) {
        presenter.getFlyingAreasData("user",//type 获取类型 【user ：用户列表 refer：参考司放地列表】【默认值 user】
                strKey,//key  查询关键字
                pi,//pi  页码【小于 0 时获取全部，默认值-1】
                ps);//ps 页大小【一页记录条数，默认值 10】
    }

    /**
     * 搜索监听
     */
    @Override
    public void onSearchClick(View view, String keyword) {
        againRequest();
    }
}
