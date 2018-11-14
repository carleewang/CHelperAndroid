package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.OrgItem;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.RaceItem;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTDetailsActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTSearchActivity2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.SGTHomeListAdapter3;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView2;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 赛格通主页    //入棚拍照      查看赛鸽：SGTHomeActivity3.isShowPhone = 1
 * Created by Administrator on 2017/12/1.
 */
public class SGTHomeActivity3 extends ToolbarBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private SGTPresenter mSGTPresenter;//控制层
    private SGTHomeListAdapter3 mAdapter;//适配器

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private boolean mIsRefreshing = false;
    private int ps = 6;//页大小【一页记录条数，默认值 10】
    private int pi = 1;//页码【小于 0 时获取全部，默认值-1
    boolean canLoadMore = true, isMoreDateLoading = false;

    private List<SGTHomeListEntity> datasTag;

    public static int isShowPhone = -1;//是否显示拍照功能   1  不显示  -1  显示


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sgt_home3;
//        return R.layout.layout_list;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者s

        setTitle("公棚赛鸽");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        datasTag = new ArrayList<>();
        mSGTPresenter = new SGTPresenter(new SGTViewImpl() {
            /**
             * 赛格通统计数据获取成功回调
             *
             * @param msg
             */
            @Override
            public void getSGTHomeData(ApiResponse<List<SGTHomeListEntity>> listApiResponse, String msg, Throwable mThrowable) {

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
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sggp);
                                } else {
                                    datasTag = listApiResponse.getData();
                                    for (int i = 0; i < datasTag.size(); i++) {
                                        datasTag.get(i).setTag(1);
                                    }
                                    mAdapter.addData(SGTHomeListAdapter3.setSGTHomeData(datasTag));

                                    mAdapter.notifyDataSetChanged();
                                    canLoadMore = listApiResponse.getData() != null && listApiResponse.getData().size() == ps;//数据不是空，并且数据的长度为一页能展示的条数（可以加载更多）
                                    finishTask();//加载下一页
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sggp);
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, SGTHomeActivity3.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sggp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


//            @Override
//            public void getTagData(List<TagEntitiy> tagDatas) {
//                mAdapter.setSgtTAG(tagDatas);//设置标签数据
//            }
        });

        initRecyclerView();
        initRefreshLayout();//初始化刷新布局
    }

    private String TAG = "SGTPresenter";


    //重新请求数据
    private void againRequest() {
        clearData();
        if (currentPosition >= 0) {
            mAdapter.collapse(currentPosition);
            currentPosition = -1;
        }

        pi = 1;
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        mSGTPresenter.getSGTGeZu(pi);
    }

    @OnClick({R.id.search_edittext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_edittext:
                startActivity(new Intent(SGTHomeActivity3.this, SGTSearchActivity2.class));
                break;
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
            if (this.isDestroyed()) {
                return;
            }
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            pi = 1;
            mSGTPresenter.getSGTGeZu(pi);
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();//重新获取数据
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new SGTHomeListAdapter3(SGTHomeListAdapter3.setSGTHomeData(null));
        mAdapter.setOnItemClickListener(onItemClickListener);
//        mAdapter.setOnItemChildClickListener(onItemChildClickListener);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);

        CustomLoadMoreView2 view = new CustomLoadMoreView2();
        mAdapter.setLoadMoreView(view);
        mAdapter.setOnLoadMoreListener(SGTHomeActivity3.this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setEnableLoadMore(true);
        setRecycleNoScroll();
    }

    @Override
    public void loadData() {

    }

    /**
     * 加载下一页
     */
    public void finishTask() {
        if (this.isDestroyed()) {
            return;
        }
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
        if (this.isDestroyed()) {
            return;
        }

        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.face);
        mCustomEmptyView.setEmptyText(tips);
    }

    private void clearData() {
        mIsRefreshing = true;
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            mSwipeRefreshLayout.setEnabled(false);
            isMoreDateLoading = true;
            mSGTPresenter.getSGTGeZu(++pi);
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }


    @Override
    public void getErrorNews(String str) {
//        super.getErrorNews(str);
        initErrorView(str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isShowPhone = -1;
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(SGTHomeListEntity clickItem) {

    }

    private int currentPosition = -1;//最后一个索引

    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            Object item = ((SGTHomeListAdapter3) adapter).getData().get(position);
            if (item instanceof OrgItem) {

                if (currentPosition == -1) { //当前没有展开项

                    if (((SGTHomeListAdapter3) adapter).getData().get(position) instanceof OrgItem) {
                        ((OrgItem) (((SGTHomeListAdapter3) adapter).getData().get(position))).getOrgInfo().setTag(2);
                    }

                    adapter.expand(position);

                    currentPosition = position;
                } else {
                    if (currentPosition == position) {
                        if (((SGTHomeListAdapter3) adapter).getData().get(position) instanceof OrgItem) {
                            ((OrgItem) (((SGTHomeListAdapter3) adapter).getData().get(position))).getOrgInfo().setTag(1);
                        }

                        adapter.collapse(position);

                        currentPosition = -1;
                    } else if (currentPosition > position) {

                        if (((SGTHomeListAdapter3) adapter).getData().get(currentPosition) instanceof OrgItem) {
                            ((OrgItem) (((SGTHomeListAdapter3) adapter).getData().get(currentPosition))).getOrgInfo().setTag(1);
                        }

                        adapter.collapse(currentPosition);

                        if (((SGTHomeListAdapter3) adapter).getData().get(position) instanceof OrgItem) {
                            ((OrgItem) (((SGTHomeListAdapter3) adapter).getData().get(position))).getOrgInfo().setTag(2);
                        }

                        adapter.expand(position);
                        currentPosition = position;
                    } else {
                        if (((SGTHomeListAdapter3) adapter).getData().get(currentPosition) instanceof OrgItem) {
                            ((OrgItem) (((SGTHomeListAdapter3) adapter).getData().get(currentPosition))).getOrgInfo().setTag(1);
                        }

                        adapter.collapse(currentPosition);

                        OrgItem orgItem = (OrgItem) mAdapter.getItem(currentPosition);
                        if (orgItem.getOrgInfo().getData() != null) {
                            int dataSize = orgItem.getOrgInfo().getData().size();
                            int expandPosition = position - dataSize;

                            if (((SGTHomeListAdapter3) adapter).getData().get(expandPosition) instanceof OrgItem) {
                                ((OrgItem) (((SGTHomeListAdapter3) adapter).getData().get(expandPosition))).getOrgInfo().setTag(2);
                            }

                            adapter.expand(expandPosition);

                            currentPosition = expandPosition;
                        }
                    }
                }
            } else if (item instanceof RaceItem) {//展开的item
                Intent intent = new Intent(SGTHomeActivity3.this, SGTDetailsActivity.class);
                RaceItem raceItem = (RaceItem) ((SGTHomeListAdapter3) adapter).getData().get(position);
                OrgItem orgItem = (OrgItem) ((SGTHomeListAdapter3) adapter).getData().get(currentPosition);
                intent.putExtra("DataBean", raceItem.getRace());
                intent.putExtra("SGTHomeListEntity", orgItem.getOrgInfo());
                startActivity(intent);
            }
        }
    };
}
