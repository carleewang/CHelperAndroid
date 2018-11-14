package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter.GYTListAdapter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 鸽运通列表展示页面
 * Created by Administrator on 2017/9/27.
 */
public class GYTListActivity extends ToolbarBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SearchEditText.OnSearchClickListener {
    @BindView(R.id.gyt_list_no_ll)
    CustomEmptyView mCustomEmptyView;//没有比赛列表时候的布局
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;//自己定义的文本输入框
    @BindView(R.id.gyt_recyclerView)
    RecyclerView mRecyclerView;//列表展示按钮
    @BindView(R.id.gyt_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件
    @BindView(R.id.ac_gyt_add_imgbtn)
    ImageButton acGytAddImgbtn;//按钮，添加比赛

    private GYTListPresenter presenter;//鸽运通比赛列表控制层
    private int ps = 10;//页大小【一页记录条数，默认值 10】
    private int pi = 1;//页码【小于 0 时获取全部，默认值-1】

    private GYTListAdapter mAdapter;

    private Intent intent;


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gyt;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        //初始化toobal
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle(getString(R.string.str_gyt));
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("训鸽通");
            acGytAddImgbtn.setBackground(getResources().getDrawable(R.drawable.selector_ac_xgt));
        }

        setTopLeftButton(R.drawable.ic_back, GYTListActivity.this::finish);
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
        setTopRightButton("编辑", new OnClickListener() {
            @Override
            public void onClick() {
                intent = new Intent(GYTListActivity.this, EditGytListActivity.class);
                startActivity(intent);
            }
        });

        searchEdittext.setOnSearchClickListener(this);//设置输入文本的输入监听

        presenter = new GYTListPresenter(new GYTListViewImpl() {
            @Override
            public void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable) {

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
                                    mAdapter.addData(listApiResponse.getData());
                                    mAdapter.notifyDataSetChanged();
                                    canLoadMore = true;
                                    finishTask();//加载下一页
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    canLoadMore = false;
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    showErrorView();
                                }
                            }
                        }else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, GYTListActivity.this, dialog -> {
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

            @Override
            public void addPlaySuccess() {
                againRequest();//重新请求数据
            }
        });//初始化控制层

        initRecyclerView();//初始化RecyclerView
        initRefreshLayout();//初始化刷新布局
    }

    //重新请求数据
    private void againRequest() {
        SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局

        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        pi = 1;
        ps = 10;
        presenter.getGYTRaceList(ps, pi, searchEdittext.getText().toString(), "", "");//开始获取数据
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String playListRefresh) {
        if (playListRefresh.equals("playListRefresh")) {
            againRequest();//重新请求数据
        }
    }

    //显示错误视图
    private void showErrorView() {
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_gyt);
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_xgt);
        }
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new GYTListAdapter(null, presenter);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(GYTListActivity.this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setEnableLoadMore(true);
        setRecycleNoScroll();
    }

    private boolean mIsRefreshing = false;
    boolean canLoadMore = true, isMoreDateLoading = false;


    /**
     * 初始化刷新布局
     */
    @Override
    public void initRefreshLayout() {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            againRequest();//重新请求数据
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();
        });
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
        mCustomEmptyView.setVisibility(View.GONE);//隐藏没有列表的情况
        mRecyclerView.setVisibility(View.VISIBLE);//显示有列表数据的情况
    }


    /**
     * hl  设置滚动事件
     */
    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {

        if (canLoadMore) {
            mSwipeRefreshLayout.setEnabled(false);
            isMoreDateLoading = true;
            Log.d("sssaaa", "onLoadMoreRequested: 加载更多");
            presenter.getGYTRaceList(ps, pi, "", "", "");//开始获取数据
        } else {
            Log.d("sssaaa", "onLoadMoreRequested: 加载更多结束");
            mAdapter.setEnableLoadMore(false);
        }
    }

    @OnClick(R.id.ac_gyt_add_imgbtn)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_gyt_add_imgbtn://添加比赛
                startActivity(new Intent(this, AddGytPlayActivity.class));
                break;
        }
    }

    /**
     * 搜索监听
     *
     * @param view
     * @param keyword
     */
    @Override
    public void onSearchClick(View view, String keyword) {
        mAdapter.getData().clear();//点击搜索，清空之前的数据
        pi = 1;
        presenter.getGYTRaceList(ps, -1, searchEdittext.getText().toString(), "", "");//开始获取数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
