package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.child_association;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter.ChildFoodAdminAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * 下级协会 足环管理列表
 * Created by Administrator on 2018/6/25.
 */

public class ChildFoodAdminListActivity extends ToolbarBaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;

    @BindView(R.id.tv_statistics)
    TextView tv_statistics;//统计数据

    private String y = "";
    private FootAdminPresenter mFootAdminPresenter;

    private ChildFoodAdminAdapter mAdapter;

    private int pi = 1, ps = 50;
    private boolean mIsRefreshing = false;
    boolean canLoadMore = true, isMoreDateLoading = false;

    private String footType = "发行普通足环";
    private boolean clickType = false;//是否点击录入足环
    private double price = 0;


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_child_food_admin_list;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, ChildFoodAdminListActivity.this::finish);
        setTitle("足环管理");
        setTopRightButton(R.drawable.foot_admin_menu, () -> {

            new SaActionSheetDialog(ChildFoodAdminListActivity.this)
                    .builder()
                    .addSheetItem("普通足环单价", OnSheetItemClickListenerState)
                    .addSheetItem("普通足环发行", OnSheetItemClickListenerState)
                    .addSheetItem("特比环单价", OnSheetItemClickListenerState)
                    .addSheetItem("特比环发行", OnSheetItemClickListenerState)
                    .show();
        });
        y = DateUtils.getStringDateY();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
        searchEdittext.setHint("请输入协会名称");

        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                againRequest();
            }
        });
        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {

            @Override
            public void getXHHYGL_SJGH_GetFootList(ApiResponse<ChildFoodAdminListEntity> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    try {
                        tv_statistics.setText("总计发行足环" + listApiResponse.getData().getFootcount() + "," + "已售金额" + listApiResponse.getData().getFootcount() + "元");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                            if (listApiResponse.getData().getFootlist().size() > 0) {
                                if (listApiResponse.getData().getFootlist().size() == 0 && mAdapter.getData().size() == 0) {
                                    showErrorView();
                                } else {
                                    mAdapter.addData(listApiResponse.getData().getFootlist());
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
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, ChildFoodAdminListActivity.this, dialog -> {
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
            public void getXHHYGL_ZHGL_GetFootPrice(ApiResponse<FootPriceEntity> listApiResponse, String msg, Throwable mThrowable) {
                //设置足环单价
                try {
                    if (listApiResponse.getErrorCode() == 0) {

                        price = Double.parseDouble(listApiResponse.getData().getPrice());
                        if (clickType) {
                            //点击
                            if (price == 0) {
                                //足环价格为0提示设置足环
                                errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "请先设置足环单价，然后才能录入足环", ChildFoodAdminListActivity.this, dialog -> {
                                    dialog.dismiss();
                                    setPrice(price, footType);
                                });//弹出提示
                            } else {
                                if (footType.equals("发行普通足环")) {
                                    intent = new Intent(ChildFoodAdminListActivity.this, ChildFootEntryActivity.class);
                                    intent.putExtra("type", "add_ordinary");
                                    intent.putExtra("price", String.valueOf(price));
                                    startActivity(intent);
                                } else if (footType.equals("发行特比环")) {
                                    intent = new Intent(ChildFoodAdminListActivity.this, ChildFootEntryActivity.class);
                                    intent.putExtra("type", "add_tubby");
                                    intent.putExtra("price", String.valueOf(price));
                                    startActivity(intent);
                                }

                            }
                        } else {
                            //单独设置单价
                            setPrice(price, footType);
                        }


                    } else {
                        ChildFoodAdminListActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {


                try {
                    if (mThrowable != null) {
                        ChildFoodAdminListActivity.this.getThrowable(mThrowable);
                    }

                    ChildFoodAdminListActivity.this.getErrorNews(msg);

                    footType = "发行普通足环";
                    clickType = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        initRecyclerView();
        initRefreshLayout();

        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                againRequest();
            }
        });

    }

    private void showErrorView() {
        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无足环数据");
    }

    //设置单价
    private void setPrice(double price, String footType) {
        String hintStr = "";
        if (footType.equals("发行普通足环")) {
            hintStr = "请输入普通足环单价";
        } else if (footType.equals("发行特比环")) {
            hintStr = "请输入特比环单价";
        }

        MyMemberDialogUtil.initInputDialog1(ChildFoodAdminListActivity.this, String.valueOf(price), hintStr, "请填写数字即可!", InputType.TYPE_CLASS_TEXT,
                new MyMemberDialogUtil.DialogClickListener() {
                    @Override
                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                        dialog.dismiss();
                        if (etStr.isEmpty() || etStr.length() == 0) {
                            ChildFoodAdminListActivity.this.getErrorNews("输入价格不能为空");
                            return;
                        }

                        if (footType.equals("发行普通足环")) {
                            mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice_Child(y, "2", etStr);
                        } else if (footType.equals("发行特比环")) {
                            mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice_Child(y, "3", etStr);
                        }
                    }
                });
    }

    private void againRequest() {
        try {
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
            pi = 1;
            ps = 50;
            //赛事规程
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mFootAdminPresenter.getXHHYGL_SJGH_GetFootList(DateUtils.getStringDateY(), pi, ps, searchEdittext.getText().toString());
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
        mAdapter = new ChildFoodAdminAdapter(null, errSweetAlertDialog);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mAdapter.setEnableLoadMore(false);
                //加载更多
                if (canLoadMore) {
                    mSwipeRefreshLayout.setEnabled(false);
                    isMoreDateLoading = true;
                    mFootAdminPresenter.getXHHYGL_SJGH_GetFootList(DateUtils.getStringDateY(), pi, ps, searchEdittext.getText().toString());
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

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String str) {
        if (str.equals(EventBusService.CHILD_FOOT_ENTRY_LIST_REFRESH)) {
            againRequest();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }


    /**
     * 弹出选择状态
     */
    private Intent intent;
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //普通足环单价
                    footType = "发行普通足环";
                    clickType = false;
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice_Child(y, "2");
                    break;
                case 2:
                    //普通足环录入
                    footType = "发行普通足环";
                    clickType = true;
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice_Child(y, "2");
                    break;
                case 3:
                    //特比环单价
                    footType = "发行特比环";
                    clickType = false;
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice_Child(y, "3");
                    break;
                case 4:
                    //特比环录入
                    footType = "发行特比环";
                    clickType = true;
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice_Child(y, "3");
                    break;
            }
        }
    };
}
