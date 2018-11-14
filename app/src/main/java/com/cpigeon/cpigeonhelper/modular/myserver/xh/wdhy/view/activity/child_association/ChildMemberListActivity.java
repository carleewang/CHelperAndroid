package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.child_association;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.ContactModel2;
import com.cpigeon.cpigeonhelper.modular.lineweather.view.activity.SelectAssociationActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AssociationListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter.ChildMemberAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.BarChartManager;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.MyAddressPicker2;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 下级协会 会员列表
 * Created by Administrator on 2018/6/25.
 */

public class ChildMemberListActivity extends ToolbarBaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_select1)
    TextView tv_select1;
    @BindView(R.id.tv_select2)
    TextView tv_select2;

    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;


    @BindView(R.id.bar_chart1)
    BarChart mBarChart;//柱状图

    @BindView(R.id.ll_bar_chart1)
    LinearLayout ll_bar_chart1;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;


    private int pi = 1, ps = 50;
    private boolean mIsRefreshing = false;
    boolean canLoadMore = true, isMoreDateLoading = false;

    private String xhuid;//协会用户id


    private MemberPresenter mMemberPresenter;
    private ChildMemberAdapter mAdapter;

    private BarChartManager mBarChartManager;

    private UserType.ShenggehuiBean mShenggehuiBean;
    private MyAddressPicker2 mMyAddressPicker2;
    private LinkagePicker mLinkagePicker;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_child_member_list;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, ChildMemberListActivity.this::finish);
        setTitle("会员列表");
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mShenggehuiBean = (UserType.ShenggehuiBean) getIntent().getSerializableExtra("region");
        xhuid = "";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        searchEdittext.setHint("请输入会员姓名");
        searchEdittext.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                againRequest();
            }
        });

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getHyListData(ApiResponse<HyglHomeListEntity> listApiResponse, String msg, Throwable mThrowable) {
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

                            try {
                                if (listApiResponse.getData().getCount_qb() == 0d && listApiResponse.getData().getCount_zc() == 0d && listApiResponse.getData().getCount_js() == 0d && listApiResponse.getData().getCount_cm() == 0d) {
                                    mBarChart.setVisibility(View.GONE);
                                    ll_bar_chart1.setVisibility(View.GONE);
                                } else {
                                    mBarChart.setVisibility(View.VISIBLE);
                                    ll_bar_chart1.setVisibility(View.VISIBLE);
                                }

                                ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组

                                yVals.add(new BarEntry(1, (float) listApiResponse.getData().getCount_qb(), 2));
                                yVals.add(new BarEntry(2, (float) listApiResponse.getData().getCount_zc(), 3));
                                yVals.add(new BarEntry(3, (float) listApiResponse.getData().getCount_js(), 4));
                                yVals.add(new BarEntry(4, (float) listApiResponse.getData().getCount_cm(), 5));

                                HyglHomeListEntity data = listApiResponse.getData();
                                List<Integer> color = new ArrayList<>();

                                color.add(getResources().getColor(R.color.member_list_member_count));
                                color.add(getResources().getColor(R.color.member_list_normal_count));
                                color.add(getResources().getColor(R.color.member_list_ban_count));
                                color.add(getResources().getColor(R.color.member_list_expel_count));

                                //showBarChart1(yVals, "xiaohl", R.color.red_btn_bg_color);
                                mBarChartManager = new BarChartManager(mBarChart);
                                mBarChartManager.showBarChart(Lists.newArrayList(0f, 1f, 2f, 3f)
                                        , Lists.newArrayList((float) data.getCount_qb(), (float) data.getCount_zc(), (float) data.getCount_js(), (float) data.getCount_cm())
                                        , "", color);
                            } catch (Exception e) {
                                Log.d("chmes", "getHyListData: " + e.getLocalizedMessage());
                            }

                            if (listApiResponse.getData().getDatalist().size() > 0) {
                                if (listApiResponse.getData().getDatalist().size() == 0 && mAdapter.getData().size() == 0) {
                                    showErrorView();
                                } else {
                                    mAdapter.addData(listApiResponse.getData().getDatalist());
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
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, ChildMemberListActivity.this, dialog -> {
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
        });
        initRecyclerView();
        initRefreshLayout();
        //initBarChart(mBarChart);
    }

    @OnClick({R.id.ll_select1, R.id.ll_select2})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.ll_select1:
                //选择地区
                onAddress3Picker(this, mShenggehuiBean.getProvince(), new LinkagePicker.OnStringPickListener() {
                    @Override
                    public void onPicked(String first, String second, String third) {

                        tv_select2.setText("全部协会");
                        xhuid = "";

                        tv_select1.setText(String.valueOf(second));
                        againRequest();
                    }
                });



//                onAddress3Picker(this, mShenggehuiBean.getProvince(), new AddressPickTask.Callback() {
//                    @Override
//                    public void onAddressInitFailed() {
//
//                    }
//
//                    @Override
//                    public void onAddressPicked(Province province, City city, County county) {
//                        tv_select1.setText(String.valueOf(province.getName() + city.getName() + county.getName()));
//                        againRequest();
//                    }
//                });
                break;
            case R.id.ll_select2:
                //选择协会
                Intent intent = new Intent(this, SelectAssociationActivity.class);

                intent.putExtra("region", mShenggehuiBean.getProvince());
                if (tv_select1.getText().toString().equals("全部地区")) {
                    intent.putExtra("parameter", "");
                } else {
                    intent.putExtra("parameter", tv_select1.getText().toString());
                }

                startActivityForResult(intent, 0x0032);
                break;
        }
    }


    public void onAddress3Picker(Activity activity, String mCity, LinkagePicker.OnPickListener mPickListener) {

        if (mLinkagePicker==null){
            List<String> s1 = new ArrayList<>();
            s1.add(mCity);

            List<String> s2 = new ArrayList<>();
            s2 = getDates(mCity);
            List<String> finalS = s2;
            LinkagePicker.Provider provider = new LinkagePicker.DataProvider() {
                @NonNull
                @Override
                public List<String> provideFirstData() {
                    return s1;
                }

                @NonNull
                @Override
                public List<String> provideSecondData(int firstIndex) {
                    return finalS;
                }

                @Nullable
                @Override
                public List<String> provideThirdData(int firstIndex, int secondIndex) {
                    return new ArrayList<>();
                }

                @Override
                public boolean isOnlyTwo() {
                    return true;
                }
            };

            mLinkagePicker = new LinkagePicker(activity, provider);
            mLinkagePicker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
            mLinkagePicker.setOnPickListener(mPickListener);
        }
        mLinkagePicker.show();

    }


    private void showErrorView() {
        mBarChart.setVisibility(View.GONE);
        ll_bar_chart1.setVisibility(View.GONE);
        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无会员数据");
    }

    private void againRequest() {
        try {
            SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
            pi = 1;
            ps = 50;
            //赛事规程
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mMemberPresenter.getDataHyList_XH2(pi, ps, searchEdittext.getText().toString(), "", tv_select1.getText().toString(), xhuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新布局
     */
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
    public void initRecyclerView() {
        mAdapter = new ChildMemberAdapter(null);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mAdapter.setEnableLoadMore(false);
                //加载更多
                if (canLoadMore) {
                    mSwipeRefreshLayout.setEnabled(false);
                    isMoreDateLoading = true;
                    mMemberPresenter.getDataHyList_XH2(pi, ps, searchEdittext.getText().toString(), "", tv_select1.getText().toString(), xhuid);
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

//    protected void onAddress3Picker(Activity activity, String ad, AddressPickTask.Callback callback) {
//        showPicker(this, ad);
//    }

    private void showPicker(Activity context, String s) {
        OptionPicker picker = new OptionPicker(context, getDates(s));
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(datePosition);
        picker.setCycleDisable(true);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                datePosition = index;
                tv_select1.setText(item);
                tv_select2.setText("全部协会");
                xhuid = "";
                againRequest();
            }
        });
        picker.show();
    }

    private int datePosition = 0;

    private List<String> getDates(String s) {
        List<String> dateList = Lists.newArrayList();

        try {
            ArrayList<Province> data = new ArrayList<>();

            String json = ConvertUtils.toString(this.getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));

            dateList = parseData(s, data, dateList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateList;
    }

    private List<String> parseData(String s, List<Province> data, List<String> dateList) {
        int provinceSize = data.size();

        dateList.add("全部地区");
        //添加省
        for (int x = 0; x < provinceSize; x++) {
            Province pro = data.get(x);
            List<City> cities = pro.getCities();

            if (pro.getAreaName().indexOf(s) != -1) {
                int citySize = cities.size();
                //添加地市
                for (int y = 0; y < citySize; y++) {
//                    dateList.add(cities.get(y).getName().replace("市", ""));
                    dateList.add(cities.get(y).getName());
                }
            }
        }
        return dateList;
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(ContactModel2.MembersEntity2 entity) {
        xhuid = ((AssociationListEntity) entity.getData()).getXhuid();
        tv_select2.setText(((AssociationListEntity) entity.getData()).getXhname());
        againRequest();
        Log.d("xiaohls", "onEventMainThread:-----------s1 ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
