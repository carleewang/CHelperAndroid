package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.GameGcPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.XhdtPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter.GameDtListAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter.GameGcListAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameDtView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameGcView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 协会  ---》赛事规程列表（协会动态列表）
 * Created by Administrator on 2017/12/12.
 */

public class GameGcActivity extends ToolbarBaseActivity implements GameGcView, GameDtView {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ll_del)
    LinearLayout llDel;//删除布局

    private GameGcPrensenter mGameGcPrensenter;//赛事规程控制层
    private GameGcListAdapter mAdapter;//赛事规程适配器

    private XhdtPrensenter mXhdtPrensenter;//协会动态控制层
    private GameDtListAdapter mGameDtListAdapter;//协会动态适配器

    private boolean mIsRefreshing = false;
//    private int ps = 6;//页大小【一页记录条数，默认值 10】
//    private int pi = 1;//页码【小于 0 时获取全部，默认值-1
//    boolean canLoadMore = true, isMoreDateLoading = false;


    public static String type;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
//        return R.layout.activity_game_gc;
        return R.layout.layout_list_gc_dt;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        type = getIntent().getStringExtra("type");
        if (type.equals("ssgc")) {
            //赛事规程
            setTitle("赛事规程");
            mGameGcPrensenter = new GameGcPrensenter(this);
        } else if (type.equals("xhdt")) {
            //协会动态
            setTitle("协会动态");
            mXhdtPrensenter = new XhdtPrensenter(this);
        }

        setTopLeftButton(R.drawable.ic_back, GameGcActivity.this::finish);
        setTopRightButton(R.mipmap.top_add, () -> {
            //跳转到添加赛事规程页面
            Intent intent = new Intent(GameGcActivity.this, AddGameGcActivity.class);
            startActivity(intent);
        });

        initRecyclerView();
        initRefreshLayout();
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String xhgcListRefresh) {
        try {
            if (xhgcListRefresh.equals("xhgcListRefresh")) {
                if (type.equals("ssgc")) {
                    //赛事规程
                    mAdapter.getData().clear();
                    mAdapter.notifyDataSetChanged();
                    mGameGcPrensenter.getXhgcList();//获取赛事规程列表
                } else if (type.equals("xhdt")) {
                    //协会动态
                    mGameDtListAdapter.getData().clear();
                    mGameDtListAdapter.notifyDataSetChanged();
                    mXhdtPrensenter.getXhdtList();//获取协会动态列表
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datasDt.clear();
        datasGc.clear();
        EventBus.getDefault().unregister(this);//取消注册
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
            if (type.equals("ssgc")) {
                //赛事规程
                mGameGcPrensenter.getXhgcList();//获取赛事规程列表
            } else if (type.equals("xhdt")) {
                //协会动态
                mXhdtPrensenter.getXhdtList();//获取协会动态列表
            }

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

        if (type.equals("ssgc")) {
            //赛事规程
            mAdapter = new GameGcListAdapter(null);
            mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(GameGcActivity.this, mRecyclerView);
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setEnableLoadMore(true);
        } else if (type.equals("xhdt")) {
            //协会动态
            mGameDtListAdapter = new GameDtListAdapter(null);
            mGameDtListAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(GameGcActivity.this, mRecyclerView);
            mGameDtListAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mRecyclerView.setAdapter(mGameDtListAdapter);
        }

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setRecycleNoScroll();

    }

    @Override
    public void loadData() {

    }


    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * hl  设置滚动事件
     */
    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
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
        mCustomEmptyView.setEmptyImage(R.mipmap.face);
        mCustomEmptyView.setEmptyText(tips);
    }

    private void clearData() {
        mIsRefreshing = true;
    }


    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_gc(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_dt(ApiResponse<Object> listApiResponse, String msg) {

    }


    @Override
    public void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg) {

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


    //========================================赛事规程=========================================================

    /**
     * 获取赛事规程列表数据成功回调
     *
     * @param datas
     * @param msg
     */
    @Override
    public void getGCList(List<GcListEntity> datas, String msg) {
        try {
            if (this.isDestroyed()) return;
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局

            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用
            mIsRefreshing = false;

            if (datas != null && datas.size() > 0) {
                llDel.setVisibility(View.VISIBLE);
                datasGc = datas;
                hideEmptyView();//隐藏错误数据，显示正常数据
                mAdapter.setNewData(datas);
            } else {
                llDel.setVisibility(View.GONE);
                initErrorView(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //========================================协会动态=========================================================

    /**
     * 获取协会动态
     *
     * @param datas
     * @param msg
     */
    public static List<DtListEntity> datasDt = new ArrayList<>();
    public static List<GcListEntity> datasGc = new ArrayList<>();

    @Override
    public void getDtList(List<DtListEntity> datas, String msg) {

        try {
            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用
            mIsRefreshing = false;
            if (datas != null && datas.size() > 0) {
                llDel.setVisibility(View.VISIBLE);
                datasDt = datas;
                hideEmptyView();//隐藏错误数据，显示正常数据
                mGameDtListAdapter.setNewData(datas);
            } else {
                llDel.setVisibility(View.GONE);
                initErrorView(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getThrowable(Throwable mThrowable) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用
            mIsRefreshing = false;
            llDel.setVisibility(View.GONE);

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

    //重新请求数据
    private void againRequest() {
        try {
            clearData();
            if (type.equals("ssgc")) {
                //赛事规程
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                mGameGcPrensenter.getXhgcList();//获取赛事规程列表
            } else if (type.equals("xhdt")) {
                //协会动态
                mGameDtListAdapter.getData().clear();
                mGameDtListAdapter.notifyDataSetChanged();
                mXhdtPrensenter.getXhdtList();//获取协会动态列表
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg) {

    }

    @OnClick({R.id.ll_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_del://点击删除
                if (type.equals("ssgc")) {
                    if (datasGc.size() == 0) {
                        CommonUitls.showSweetDialog(this, "没有可以删除的规程数据");
                        return;
                    }
                } else if (type.equals("xhdt")) {
                    if (datasDt.size() == 0) {
                        CommonUitls.showSweetDialog(this, "没有可以删除的动态数据");
                        return;
                    }
                }
                Intent intent = new Intent(this, DtGcDelActivity.class);
                startActivity(intent);
                break;
        }
    }
}
