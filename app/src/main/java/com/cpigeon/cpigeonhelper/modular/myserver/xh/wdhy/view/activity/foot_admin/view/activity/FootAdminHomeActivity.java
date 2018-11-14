package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.IssueFoot;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.adapter.FootAdminHomeListAdapter;
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
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by Administrator on 2018/6/14.
 */

public class FootAdminHomeActivity extends ToolbarBaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_statistics)
    TextView tv_statistics;//统计数据

    @BindView(R.id.tv_time)
    TextView tv_time;//时间
    @BindView(R.id.tv_status)
    TextView tv_status;//状态


    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;

    private String y = "";
    private String s = "2";

    private FootAdminPresenter mFootAdminPresenter;

    private FootAdminHomeListAdapter mAdapter;

    private boolean mIsRefreshing = false;

    private int pi = 1, ps = 50;
    boolean canLoadMore = true, isMoreDateLoading = false;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_foot_admin_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, FootAdminHomeActivity.this::finish);
        setTitle("足环管理");
        setTopRightButton(R.drawable.foot_admin_menu, () -> {

            new SaActionSheetDialog(FootAdminHomeActivity.this)
                    .builder()
                    .addSheetItem("设置单价", OnSheetItemClickListenerState)
                    .addSheetItem("足环录入", OnSheetItemClickListenerState)
                    .show();
        });

        y = DateUtils.getStringDateY();
        s = "2";
    }

    private Intent intent;

    @OnClick({R.id.ll_select_time, R.id.ll_select_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_time:
                //选择时间
                PickerAdmin.showPicker(FootAdminHomeActivity.this, 0, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        y = item;
                        tv_time.setText(item);
                        againRequest();

                    }
                });
                break;
            case R.id.ll_select_state:
                //选择状态
                new SaActionSheetDialog(FootAdminHomeActivity.this)
                        .builder()
                        .addSheetItem("已售出", OnSheetItemClickListenerState2)
                        .addSheetItem("未售出", OnSheetItemClickListenerState2)
                        .addSheetItem("全部", OnSheetItemClickListenerState2)
                        .show();
                break;
        }
    }

    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState2 = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            switch (which) {
                case 1:
                    //已售出
                    s = "1";
                    tv_status.setText("已售出(0)");
                    againRequest();
                    break;
                case 2:
                    //未售出
                    s = "0";
                    tv_status.setText("未售出(0)");
                    againRequest();
                    break;
                case 3:
                    //全部
                    s = "2";
                    tv_status.setText("全部(0)");
                    againRequest();
                    break;
            }
        }
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {

            @Override
            public void getFootAdminList(ApiResponse<FootAdminListDataEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {

                    if (mLoadDataDialog2.isShowing()) mLoadDataDialog2.dismiss();

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
                            String[] aa = tv_status.getText().toString().split("\\(");

                            try {
                                tv_status.setText(aa[0] + "(" + listApiResponse.getData().getTotalcountcurr() + ")");
                            } catch (Exception e) {
                                tv_status.setText(aa[0] + "(0)");
                            }

                            try {
                                tv_statistics.setText(String.valueOf("总计发行足环" + listApiResponse.getData().getTotalcount() + ",已售金额" + listApiResponse.getData().getTotalpaysum() + "元"));
                            } catch (Exception e) {
                                tv_statistics.setText("总计发行足环0,已售金额0元");
                            }

                            if (listApiResponse.getData().getFootlist().size() > 0) {
                                if (listApiResponse.getData().getFootlist().size() == 0 && mAdapter.getData().size() == 0) {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_rz);
                                } else {
                                    mAdapter.addData(listApiResponse.getData().getFootlist());
                                    mAdapter.notifyDataSetChanged();
                                    canLoadMore = listApiResponse.getData() != null && listApiResponse.getData().getFootlist().size() == ps;//数据不是空，并且数据的长度为一页能展示的条数（可以加载更多）
                                    finishTask();//加载下一页
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_zwzh);
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminHomeActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_zwzh);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                FootAdminHomeActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                FootAdminHomeActivity.this.getThrowable(throwable);
            }

            @Override
            public void getIssueFootsData(ApiResponse<IssueFoot> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (listApiResponse.getErrorCode() == 0) {

                        if (listApiResponse.getData().getType().equals("普通足环")) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog6(errSweetAlertDialog, msg, FootAdminHomeActivity.this
                                    , confirmDialog -> {
                                        confirmDialog.dismiss();
                                        //确定
                                        mLoadDataDialog2.show();
                                        mFootAdminPresenter.getXHHYGL_ZHGL_ImportFoot(listApiResponse.getData().getId());
                                    }, cancelDialog -> {
                                        cancelDialog.dismiss();
                                        //取消
                                        initRefreshLayout();
                                    });
                        } else {
                            initRefreshLayout();
                        }

                    } else {
                        initRefreshLayout();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getXHHYGL_ZHGL_ImportFootResult(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (mLoadDataDialog2.isShowing()) mLoadDataDialog2.dismiss();

                    if (listApiResponse.getErrorCode() == 0) {
                        initRefreshLayout();
                    } else {
                        initRefreshLayout();
                        FootAdminHomeActivity.this.getErrorNews(msg);
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
                        MyMemberDialogUtil.initInputDialog1(FootAdminHomeActivity.this, listApiResponse.getData().getPrice(), "输入单价", "请填写数字即可!", InputType.TYPE_CLASS_TEXT,
                                new MyMemberDialogUtil.DialogClickListener() {
                                    @Override
                                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                        dialog.dismiss();
                                        if (etStr.isEmpty() || etStr.length() == 0) {
                                            FootAdminHomeActivity.this.getErrorNews("输入价格不能为空");
                                            return;
                                        }
                                        mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice(y, "0", etStr);
                                    }
                                });
                    } else {
                        FootAdminHomeActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    FootAdminHomeActivity.this.getErrorNews(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        initRecyclerView();

        mFootAdminPresenter.getXHHYGL_ZHGL_GetFoot();

        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                againRequest();
            }
        });
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
                againRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            try {
                againRequest();
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
        //足环管理列表
        mAdapter = new FootAdminHomeListAdapter(null, "普通足环");
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (canLoadMore) {
                    mSwipeRefreshLayout.setEnabled(false);
                    isMoreDateLoading = true;
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetList(y, "普通足环", s, pi, ps, "", searchEdittext.getText().toString());
                } else {
                    mAdapter.setEnableLoadMore(false);
                }
            }
        }, mRecyclerView);
        mAdapter.setEnableLoadMore(true);
    }

    //请求数据
    private void againRequest() {
        try {
            pi = 1;
            ps = 50;

            mSwipeRefreshLayout.setRefreshing(true);
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局

            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mFootAdminPresenter.getXHHYGL_ZHGL_GetList(y, "普通足环", s, pi, ps, "", searchEdittext.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 弹出选择状态
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //足环单价
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice(y, "0");
                    break;
                case 2:
                    //足环录入
                    intent = new Intent(FootAdminHomeActivity.this, FootEntryActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.FOOT_ADMIN_REFRESH)) {
            againRequest();//刷新
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
