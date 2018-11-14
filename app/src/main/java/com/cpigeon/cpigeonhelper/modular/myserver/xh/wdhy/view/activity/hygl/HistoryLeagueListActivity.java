package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter.HistoryLeagueAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
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
import butterknife.OnClick;


/**
 * 历史赛绩 列表
 * Created by Administrator on 2018/3/29.
 */
public class HistoryLeagueListActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.ll_drsj)
    LinearLayout ll_drsj;//导入赛绩

    private HistoryLeagueAdapter mAdapter;


    private HyUserDetailEntity mHyUserDetailEntity;

    private MemberPresenter mMemberPresenter;
    private int pi = 1, ps = -1;

    private boolean mIsRefreshing = false;
    boolean canLoadMore = true, isMoreDateLoading = false;


    private String type = "myself";//look :上级协会查看   myself：自己查看


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
//        item_head_lcsj
        return R.layout.layout_history_heague_list;
    }

    @Override
    protected void setStatusBar() {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者


        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, HistoryLeagueListActivity.this::finish);
        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");

        if (mHyUserDetailEntity != null) {
            setTitle(String.valueOf(mHyUserDetailEntity.getBasicinfo().getName() + "历次赛绩"));
        } else {
            setTitle("历次赛绩");
        }

        type = getIntent().getStringExtra("type");


    }


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.HISTORY_LEAGUE_LIST_REFRESH)) {
            againRequest();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
            setTopRightButton(R.mipmap.top_add, () -> {
                //历史赛绩添加
                Intent intent = new Intent(HistoryLeagueListActivity.this, HistoryLeagueAddActivity.class);
                intent.putExtra("data", mHyUserDetailEntity);
                startActivity(intent);
            });
        }else {
            ll_drsj.setVisibility(View.GONE);
        }

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getXHHYGL_SJ_GetList(ApiResponse<List<HistoryLeagueEntity>> listApiResponse, String msg, Throwable mThrowable) {
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
                                    canLoadMore = false;
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
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HistoryLeagueListActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            if (mAdapter.getData().size() > 0) {
                                canLoadMore = false;
                                mAdapter.loadMoreEnd(false);//加载更多的结束
                            } else {
                                showErrorView();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getXHHYGL_SJ_GetImport(ApiResponse<List<HistoryLeagueEntity>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (listApiResponse.getErrorCode() == 0) {
                        mAdapter.addData(listApiResponse.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HistoryLeagueListActivity.this, dialog -> {
                            dialog.dismiss();
                        });//弹出提示
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
        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_hygl_lssj);
    }

    private void againRequest() {
        try {
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
            pi = 1;
            ps = -1;
            //赛事规程
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mMemberPresenter.getXHHYGL_SJ_GetList(mHyUserDetailEntity.getBasicinfo().getMid(), pi, ps,mHyUserDetailEntity.getBasicinfo().getXhuid(),type);
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
                mMemberPresenter.getXHHYGL_SJ_GetList(mHyUserDetailEntity.getBasicinfo().getMid(), pi, ps,mHyUserDetailEntity.getBasicinfo().getXhuid(),type);
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
        mAdapter = new HistoryLeagueAdapter(null, mHyUserDetailEntity,type);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //加载更多
                if (canLoadMore) {
                    mSwipeRefreshLayout.setEnabled(false);
                    isMoreDateLoading = true;
                againRequest();
                } else {
                    mAdapter.setEnableLoadMore(false);
                }
            }
        }, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setEnableLoadMore(true);

        setRecycleNoScroll();
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


    @OnClick({R.id.ll_drsj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_drsj:
                //导入历次赛绩
                mMemberPresenter.getXHHYGL_SJ_GetImport(mHyUserDetailEntity.getBasicinfo().getMid());
                break;
        }
    }
}
