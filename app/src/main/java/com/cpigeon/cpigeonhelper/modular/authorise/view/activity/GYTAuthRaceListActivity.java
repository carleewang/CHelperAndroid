package com.cpigeon.cpigeonhelper.modular.authorise.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AuthRaceListPresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.adapter.AuthRaceListAdapter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AuthRaceListViewImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 需要接受授权的鸽运通比赛列表
 * Created by Administrator on 2017/9/22.
 */

public class GYTAuthRaceListActivity extends ToolbarBaseActivity implements SearchEditText.OnSearchClickListener {

    //    @BindView(R.id.imgbtn_start_search)
//    ImageButton imgbtnStartSearch;//搜索按钮
//    @BindView(R.id.ac_search_input)
//    EditText acSearchInput;//输入文本
//    @BindView(R.id.celel_input_content)
//    ImageButton celelInputContent;//清除输入按钮
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;//列表展示
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;//错误图展示
    @BindView(R.id.swipe_refresh_ac_au_ra_list)
    SwipeRefreshLayout mSwipeRefreshLayout;//刷新布局

    @BindView(R.id.search_auth_list_et)
    SearchEditText mSearchEditText;//搜索按钮


    private AuthRaceListAdapter mAdapter;

    private int pi = -1;//页码【小于 0 时获取全部，默认值-1】
    private int ps = 6;//页大小【一页记录条数，默认值 10】

    private AuthRaceListPresenter presenter;//控制层

    private int auid = -1;//授权用户id

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gyt_auth_race_list;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("授权比赛选择");
        setTopLeftButton(R.drawable.ic_back, GYTAuthRaceListActivity.this::finish);

        auid = getIntent().getIntExtra("auid", -1);

        mSearchEditText.setOnSearchClickListener(this);//输入文本设置监听

        presenter = new AuthRaceListPresenter(new AuthRaceListViewImpl() {

            /**
             * 请求成功，没有数据
             */
            @Override
            public void getRaceListDataIsNo() {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                    mCustomEmptyView.setVisibility(View.VISIBLE);//显示错误视图
                    mRecyclerView.setVisibility(View.GONE);//隐藏recyclerView布局
                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "未搜索到比赛情况");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 请求成功，有数据回调
             *
             * @param data
             */
            @Override
            public void getRaceListData(List<GeYunTong> data) {
                try {
                    mCustomEmptyView.setVisibility(View.GONE);//隐藏错误视图
                    mRecyclerView.setVisibility(View.VISIBLE);//显示recyclerView视图
                    mSwipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                    mAdapter.getData().clear();//清除适配器之前的数据
                    mAdapter.setNewData(data);//给适配器设置新的值
                    mAdapter.notifyDataSetChanged();//刷新适配器
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void addAuthData(ApiResponse<String> dataApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (dataApiResponse != null) {
                        switch (dataApiResponse.getErrorCode()) {
                            case 0:
                                CommonUitls.showSweetDialog1(GYTAuthRaceListActivity.this, msg, dialog -> {
                                    dialog.dismiss();
                                    startActivity(new Intent(GYTAuthRaceListActivity.this, AuthoriseHomeActivity.class));
                                });

                                break;
                            default:
                                CommonUitls.showSweetDialog1(GYTAuthRaceListActivity.this, msg, dialog -> {
                                    dialog.dismiss();
                                });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });//初始化控制层

        initRefreshLayout();//刷新布局
        initRecyclerView();//初始化RecyclerView
    }


    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new AuthRaceListAdapter(null, presenter, "", auid);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.getItemAnimator();
    }

    /**
     * 刷新布局
     */

    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            presenter.getAuthRaceListData(mSearchEditText, pi, ps);//开始获取数据
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getAuthRaceListData(mSearchEditText, pi, ps);//开始获取数据
        });
    }


    /**
     * 显示错误信息
     *
     * @param tips
     */
    public void initErrorView(String tips) {
        try {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSearchClick(View view, String keyword) {
        presenter.getAuthRaceListData(mSearchEditText, pi, ps);
    }
}
