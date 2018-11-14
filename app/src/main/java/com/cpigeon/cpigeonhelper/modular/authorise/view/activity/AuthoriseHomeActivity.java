package com.cpigeon.cpigeonhelper.modular.authorise.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AuthHomeEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AuthHomePresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.adapter.AuthHomeAdapter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AuthHomeView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户授权主页
 * Created by Administrator on 2017/9/20.
 */
public class AuthoriseHomeActivity extends ToolbarBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, AuthHomeView {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;//列表展示
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;//错误裂变展示图
    @BindView(R.id.swipe_refresh_ac_au_home)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件
    @BindView(R.id.ac_newRegAndPas_determine)
    Button acNewRegAndPasDetermine;//添加授权按钮


    private AuthHomeAdapter mAdapter;//授权列表适配器
    private AuthHomePresenter presenter;//授权列表控制层


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_authorise_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("账户授权");
        setTopLeftButton(R.drawable.ic_back, AuthoriseHomeActivity.this::finish);

        presenter = new AuthHomePresenter(this);//初始化授权列表

        initRecyclerView();
        initRefreshLayout();//初始化刷新布局
    }


    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new AuthHomeAdapter(null, presenter);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.getItemAnimator();
    }


    /**
     * 显示错误信息
     *
     * @param tips
     */
    public void initErrorView(String tips) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mCustomEmptyView.setEmptyImage(R.mipmap.face);
            mCustomEmptyView.setEmptyText(tips);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.ac_newRegAndPas_determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_newRegAndPas_determine://添加授权
                startActivity(new Intent(this, AddAuthActivity.class));
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getAuthHomeData();//开始获取数据
    }

    /**
     * 刷新布局
     */

    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            presenter.getAuthHomeData();//开始获取数据
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();

            presenter.getAuthHomeData();//开始获取数据
        });
    }

    /**
     * 有数据成功后返回
     *
     * @param listData
     */
    @Override
    public void authHomeListData(List<AuthHomeEntity> listData) {

        try {
            if (listData.size() > 0) {
                mAdapter.getData().clear();//清除之前的数据
                mAdapter.addData(listData);//加入新的数据
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);//关闭刷新动画

                mCustomEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                initErrorView("暂无授权用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 没有数据成功后返回
     */
    @Override
    public void authHomeListDataNO() {
        initErrorView("暂无授权用户");
    }

    /**
     * 取消授权成功后回调
     */
    @Override
    public void cancelSucceed() {
        presenter.getAuthHomeData();//开始获取数据
        CommonUitls.showToast(this, "取消授权成功");
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
}
