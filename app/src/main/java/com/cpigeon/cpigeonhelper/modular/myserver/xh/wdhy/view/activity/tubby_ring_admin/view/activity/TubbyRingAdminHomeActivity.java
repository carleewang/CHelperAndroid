package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PieChartFootEntity;
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
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.chart.MyPercentFormatter;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * https://www.jianshu.com/p/2f0ff59ec911
 * Created by Administrator on 2018/6/14.
 */

public class TubbyRingAdminHomeActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.pie_chart1)
    PieChart pie_chart1;
    @BindView(R.id.pie_chart2)
    PieChart pie_chart2;
    @BindView(R.id.pie_chart3)
    PieChart pie_chart3;

    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;

    @BindView(R.id.ll_chart)
    LinearLayout ll_chart;//统计图总布局

    private FootAdminPresenter mFootAdminPresenter;
    private FootAdminHomeListAdapter mAdapter;

    private String y = "";//年份
    private String s = "2";////是否出售：0为未售出，1为已出售  2全部
    private String j = "2";//是否缴费：0为未缴费，1为已交费，2为全部

    private boolean mIsRefreshing = false;
    private int pi = 1, ps = 50;
    boolean canLoadMore = true, isMoreDateLoading = false;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tubby_ring_admin;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, TubbyRingAdminHomeActivity.this::finish);
        setTitle("特比环管理");

        setTopRightButton(R.drawable.foot_admin_menu, () -> {
            new SaActionSheetDialog(TubbyRingAdminHomeActivity.this)
                    .builder()
                    .addSheetItem("代售点管理", OnSheetItemClickListenerState)
                    .addSheetItem("特比环录入", OnSheetItemClickListenerState)
                    .addSheetItem("年份选择", OnSheetItemClickListenerState)
                    .addSheetItem("足环单价", OnSheetItemClickListenerState)
                    .show();
        });
        y = DateUtils.getStringDateY();
        s = "2";
    }

    private List<PieEntry> strings1 = new ArrayList<>();
    private List<PieEntry> strings2 = new ArrayList<>();
    private List<PieEntry> strings3 = new ArrayList<>();
    int[] colors1 = {Color.rgb(255, 175, 162), Color.rgb(233, 87, 63)};
    int[] colors2 = {Color.rgb(255, 157, 159), Color.rgb(234, 44, 75)};
    int[] colors3 = {Color.rgb(255, 190, 132), Color.rgb(248, 131, 26)};

    @Override
    protected void initViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {

            @Override
            public void getFootAdminList(ApiResponse<FootAdminListDataEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mLoadDataDialog2.isShowing()) mLoadDataDialog2.dismiss();

                    setTitle(y + "特比环");
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

                            //显示特比环数量
                            try {
                                tv_tubby_Ring_size.setText(String.valueOf("特比环(" + listApiResponse.getData().getTotalcountcurr() + ")"));
                            } catch (Exception e) {
                                tv_tubby_Ring_size.setText("特比环(0)");
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
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, TubbyRingAdminHomeActivity.this, dialog -> {
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
                TubbyRingAdminHomeActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                TubbyRingAdminHomeActivity.this.getThrowable(throwable);
            }

            @Override
            public void getIssueFootsData(ApiResponse<IssueFoot> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (listApiResponse.getErrorCode() == 0) {

                        if (listApiResponse.getData().getType().equals("特比环")) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog6(errSweetAlertDialog, msg, TubbyRingAdminHomeActivity.this
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
                        TubbyRingAdminHomeActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void getXHHYGL_ZHGL_GetTotal(ApiResponse<PieChartFootEntity> listApiResponse, String msg, Throwable mThrowable) {
                //获取饼状图数据
                if (listApiResponse.getErrorCode() == 0) {

                    double ycs = Double.valueOf(listApiResponse.getData().getTotalcountys());
                    double wcs = Double.valueOf(listApiResponse.getData().getTotalcountws());

                    double yjf = Double.valueOf(listApiResponse.getData().getTotalcountyjf());
                    double wjf = Double.valueOf(listApiResponse.getData().getTotalcountwjf());
                    double yjfje = Double.valueOf(listApiResponse.getData().getTotalpaysumyjf());
                    double wjfje = Double.valueOf(listApiResponse.getData().getTotalpaysumwjf());

                    double yyg = Double.valueOf(listApiResponse.getData().getTotalcountyyg());
                    double wyg = Double.valueOf(listApiResponse.getData().getTotalcountwyg());


                    if (ycs == 0 && wcs == 0 &&
                            yjf == 0 && wjf == 0 && yjfje == 0 && wjfje == 0 &&
                            yyg == 0 && wyg == 0) {

                        ll_chart.setVisibility(View.GONE);
                        return;
                    } else {
                        ll_chart.setVisibility(View.VISIBLE);
                    }

                    strings1.clear();
                    try {
                        float zb1 = 0;
                        try {
                            zb1 = (float) (ycs / (ycs + wcs)) * 100;
                        } catch (Exception e) {
                            Log.d("yichang", "getXHHYGL_ZHGL_GetTotal: 0");
                        }
                        strings1.add(new PieEntry(zb1, "已售\n" + ycs, ycs));
                        strings1.add(new PieEntry(100 - zb1, "未售\n" + wcs, wcs));
                    } catch (Exception e) {
                        Log.d("yichang", "getXHHYGL_ZHGL_GetTotal: 1");
                    }

                    strings2.clear();
                    try {

                        float zb2 = 0;
                        try {
                            zb2 = (float) (yjf / (yjf + wjf)) * 100;
                        } catch (Exception e) {
                            Log.d("yichang", "getXHHYGL_ZHGL_GetTotal: 2");
                        }

                        strings2.add(new PieEntry(zb2, "已缴" + yjfje + "元\n(" + yjf + ")", yjf));
                        strings2.add(new PieEntry(100 - zb2, "未缴" + wjfje + "元\n(" + wjf + ")", wjf));
                    } catch (Exception e) {
                        Log.d("yichang", "getXHHYGL_ZHGL_GetTotal: 3");
                    }

                    strings3.clear();
                    try {
                        float zb3 = 0;
                        try {
                            zb3 = (float) (yyg / (yyg + wyg)) * 100;
                        } catch (Exception e) {
                            Log.d("yichang", "getXHHYGL_ZHGL_GetTotal: 4");
                        }

                        strings3.add(new PieEntry(zb3, "已验\n" + yyg, yyg));
                        strings3.add(new PieEntry(100 - zb3, "未验\n" + wyg, wyg));
                    } catch (Exception e) {
                        Log.d("yichang", "getXHHYGL_ZHGL_GetTotal: 5");
                    }

                    setPieChart(pie_chart1, strings1, false, colors1);
                    setPieChart(pie_chart2, strings2, false, colors2);
                    setPieChart(pie_chart3, strings3, false, colors3);

                } else {
                    ll_chart.setVisibility(View.GONE);
//                    setNoData();
                    TubbyRingAdminHomeActivity.this.getErrorNews(msg);
                }
            }

            @Override
            public void getXHHYGL_ZHGL_GetFootPrice(ApiResponse<FootPriceEntity> listApiResponse, String msg, Throwable mThrowable) {
                //设置足环单价
                try {
                    if (listApiResponse.getErrorCode() == 0) {
                        MyMemberDialogUtil.initInputDialog1(TubbyRingAdminHomeActivity.this, listApiResponse.getData().getPrice(), "输入单价", "请填写数字即可!", InputType.TYPE_CLASS_TEXT,
                                new MyMemberDialogUtil.DialogClickListener() {
                                    @Override
                                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                        dialog.dismiss();
                                        if (etStr.isEmpty() || etStr.length() == 0) {
                                            TubbyRingAdminHomeActivity.this.getErrorNews("输入价格不能为空");
                                            return;
                                        }

                                        mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice(y, "1", etStr);
                                    }
                                });
                    } else {
                        TubbyRingAdminHomeActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    TubbyRingAdminHomeActivity.this.getErrorNews(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mFootAdminPresenter.getXHHYGL_ZHGL_GetFoot();
        initRecyclerView();

        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                againRequest();
            }
        });
    }

    private void setNoData() {
        strings1.clear();
        double ycs = 0;
        double wcs = 0;

        try {
            float zb1 = 0;
            strings1.add(new PieEntry(zb1, "已售\n" + ycs, ycs));
            strings1.add(new PieEntry(100 - zb1, "未售\n" + wcs, wcs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        strings2.clear();
        double yjf = 0;
        double wjf = 0;

        String yjfje = "0";
        String wjfje = "0";

        try {
            float zb2 = 0;
            strings2.add(new PieEntry(zb2, "已缴" + yjfje + "元\n(" + yjf + ")", yjf));
            strings2.add(new PieEntry(100 - zb2, "未缴" + wjfje + "元\n(" + wjf + ")", wjf));
        } catch (Exception e) {
            e.printStackTrace();
        }

        strings3.clear();
        double yyg = 0;
        double wyg = 0;

        try {
            float zb3 = 0;
            strings3.add(new PieEntry(zb3, "已验\n" + yyg, yyg));
            strings3.add(new PieEntry(100 - zb3, "未验\n" + wyg, wyg));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPieChart(pie_chart1, strings1, false, colors1);
        setPieChart(pie_chart2, strings2, false, colors2);
        setPieChart(pie_chart3, strings3, false, colors3);
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
        mAdapter = new FootAdminHomeListAdapter(null, "特比环");
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
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetList(y, "特比环", s, pi, ps, "", searchEdittext.getText().toString());
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
            mFootAdminPresenter.getXHHYGL_ZHGL_GetList(y, "特比环", s, pi, ps, j, searchEdittext.getText().toString());

            RxUtils.delayed(500, aLong -> {
                mFootAdminPresenter.getXHHYGL_ZHGL_GetTotal(y);
            });

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

    @BindView(R.id.tv_select_state)
    TextView tv_select_state;
    @BindView(R.id.tv_pay_state)
    TextView tv_pay_state;
    @BindView(R.id.tv_tubby_Ring_size)
    TextView tv_tubby_Ring_size;

    @OnClick({R.id.search_edittext, R.id.ll_select_state, R.id.ll_pay_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_edittext:
//                intent = new Intent(TubbyRingAdminHomeActivity.this, SearchAgentTakePlaceActivity.class);
//                startActivity(intent);
                break;

            case R.id.ll_select_state:
                //出售状态
                new SaActionSheetDialog(TubbyRingAdminHomeActivity.this)
                        .builder()
                        .addSheetItem("已出售", OnSheetItemClickListenerState1)
                        .addSheetItem("未出售", OnSheetItemClickListenerState1)
                        .addSheetItem("全部", OnSheetItemClickListenerState1)
                        .show();
                break;

            case R.id.ll_pay_state:
                //缴费状态
                //出售状态
                if (tv_select_state.getText().toString().equals("未出售")) {
                    new SaActionSheetDialog(TubbyRingAdminHomeActivity.this)
                            .builder()
                            .addSheetItem("全部", OnSheetItemClickListenerState3)
                            .show();
                } else {
                    new SaActionSheetDialog(TubbyRingAdminHomeActivity.this)
                            .builder()
                            .addSheetItem("已缴费", OnSheetItemClickListenerState2)
                            .addSheetItem("未缴费", OnSheetItemClickListenerState2)
                            .addSheetItem("全部", OnSheetItemClickListenerState2)
                            .show();
                }

                break;
        }
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
                    //代售点管理
                    intent = new Intent(TubbyRingAdminHomeActivity.this, AgentTakePlaceActivity.class);
                    intent.putExtra("type", "add");
                    startActivity(intent);
                    break;
                case 2:
                    //特比环录入
                    intent = new Intent(TubbyRingAdminHomeActivity.this, TubbyRingEntryActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    //年份选择
                    PickerAdmin.showPicker(TubbyRingAdminHomeActivity.this, 0, new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            y = item;
                            againRequest();
                        }
                    });
                    break;
                case 4:
                    //足环单价
                    mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice(y, "1");
                    break;
            }
        }
    };

    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState1 = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //已出售
                    s = "1";
                    tv_select_state.setText("已出售");

                    break;
                case 2:
                    //未出售
                    s = "0";
                    tv_select_state.setText("未出售");

                    // 缴费状态 全部
                    j = "2";
                    tv_pay_state.setText("全部");

                    break;
                case 3:
                    //全部
                    s = "2";
                    tv_select_state.setText("全部");

                    break;
            }
            againRequest();
        }
    };


    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState2 = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //已缴费
                    j = "1";
                    tv_pay_state.setText("已缴费");
                    break;
                case 2:
                    //未缴费
                    j = "0";
                    tv_pay_state.setText("未缴费");
                    break;
                case 3:
                    //全部
                    j = "2";
                    tv_pay_state.setText("全部");
                    break;
            }
            againRequest();
        }
    };

    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState3 = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //全部
                    j = "2";
                    tv_pay_state.setText("全部");
                    break;
            }
            againRequest();
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

    private void setPieChartData(PieChart pieChart, List<PieEntry> entries, int[] colors) {

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(0f);//设置饼块选中时偏离饼图中心的距离
        dataSet.setColors(colors);//设置饼块的颜色

//        dataSet.setValueLinePart1Length();

        //设置数据显示方式有见图
//        dataSet.setValueLinePart1OffsetPercentage(50f);//数据连接线距图形片内部边界的距离，为百分数
//        dataSet.setValueLinePart1Length(0.3f);
//        dataSet.setValueLinePart2Length(0.4f);
//        dataSet.setValueLineColor(Color.YELLOW);//设置连接线的颜色
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new MyPercentFormatter());

        pieChart.setData(pieData);

        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    public void setPieChart(PieChart pieChart, List<PieEntry> entries, boolean showLegend, int[] colors) {

        pieChart.setUsePercentValues(false);//设置使用百分比（后续有详细介绍）
//        pieChart.getDescription().setEnabled(false);//设置描述
        //设置饼图右下角的文字描述
        pieChart.setDescription("");
        pieChart.setEntryLabelTextSize(8f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setExtraOffsets(0, 0, 0, 0); //设置边距
        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置摩擦系数（值越小摩擦系数越大）
        pieChart.setRotationEnabled(false);//是否可以旋转
        pieChart.setHighlightPerTapEnabled(true);//点击是否放大
//        pieChart.setCenterText("");//设置环中的文字
//        pieChart.setCenterTextSize(22f);//设置环中文字的大小
//        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setRotationAngle(0f);//设置旋转角度
        pieChart.setTransparentCircleRadius(9f);//设置半透明圆环的半径,看着就有一种立体的感觉
        //这个方法为true就是环形图，为false就是饼图
        pieChart.setDrawHoleEnabled(false);
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.BLACK);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.BLACK);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(110);

        //图例设置
        Legend legend = pieChart.getLegend();

        if (showLegend) {
            //显示色块说明
            legend.setEnabled(true);//是否显示图例
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例相对于图表横向的位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例相对于图表纵向的位置
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例显示的方向
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        } else {
            legend.setEnabled(false);
        }

        //设置饼图数据
        setPieChartData(pieChart, entries, colors);

//        pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画
        pieChart.animateX(1800);
    }

}
