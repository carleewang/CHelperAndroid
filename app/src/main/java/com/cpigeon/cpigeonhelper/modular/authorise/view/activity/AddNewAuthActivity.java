package com.cpigeon.cpigeonhelper.modular.authorise.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AddAuthPresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AuthRaceListPresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.adapter.AddNewAuthAdapter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AddAuthView;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AuthRaceListViewImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.GYTListActivity;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/9.
 */

public class AddNewAuthActivity extends ToolbarBaseActivity implements AddAuthView, SearchEditText.OnSearchClickListener {

    @BindView(R.id.tv_search_fail)
    TextView tvSearchFail;//未搜索到用户显示
    @BindView(R.id.imgbtn_sms_inform)
    ImageButton imgbtnSmsInform;//短信通知
    @BindView(R.id.ll_search_fail)
    LinearLayout llSearchFail;//未找到控件布局
    @BindView(R.id.ac_addauth_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_auth_edittext)
    SearchEditText mSearchEditText;//开始搜索按钮
    @BindView(R.id.tv_jg)
    TextView tvJg;//搜索结果

    private AddAuthPresenter presenter;//控制层
    private AuthRaceListPresenter mAuthRaceListPresenter;

    private AddNewAuthAdapter mAdapter;//适配器

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_auth;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        try {
            setTitle(getIntent().getStringExtra("title"));
        } catch (Exception e) {
            setTitle("添加授权");
        }
        setTopLeftButton(R.drawable.ic_back, AddNewAuthActivity.this::finish);

        presenter = new AddAuthPresenter(this);//初始化控制层

        mAuthRaceListPresenter = new AuthRaceListPresenter(new AuthRaceListViewImpl() {


            @Override
            public void addAuthData(ApiResponse<String> dataApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (dataApiResponse != null) {
                        switch (dataApiResponse.getErrorCode()) {
                            case 0:
                                EventBus.getDefault().post("playListRefresh");//通知比赛列表刷新
                                CommonUitls.showSweetDialog1(AddNewAuthActivity.this, msg, dialog -> {
                                    startActivity(new Intent(AddNewAuthActivity.this, GYTListActivity.class));
                                });
                                break;
                            default:
                                CommonUitls.showSweetDialog1(AddNewAuthActivity.this, msg, dialog -> {
                                    dialog.dismiss();
                                });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        initRecyclerView();//初始化RecyclerView
        mSearchEditText.setOnSearchClickListener(this);//输入文本设置监听
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new AddNewAuthAdapter(null, mAuthRaceListPresenter, getIntent().getParcelableExtra("MonitorGeYunTongs"), mSearchEditText, this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.getItemAnimator();
    }


    @OnClick({R.id.imgbtn_sms_inform})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_sms_inform://短信通知并提示
                presenter.smsCallUser(mSearchEditText, this);
                break;
        }
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

    /**
     * 通过手机号获取到用户数据成功返回
     *
     * @param data
     */
    @Override
    public void getAddAuthDatas(List<AddAuthEntity> data) {

        try {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.getData().clear();//清除适配器之前的数据
            mAdapter.setNewData(data);//设置值
            mAdapter.notifyDataSetChanged();//刷新布局
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDataIsNull() {
        mRecyclerView.setVisibility(View.GONE);//隐藏RecyclerView布局
        llSearchFail.setVisibility(View.VISIBLE);//显示未搜索到结果的布局
        tvSearchFail.setText("未搜索到用户" + mSearchEditText.getText().toString());
    }


    @Override
    public void onSearchClick(View view, String keyword) {
        presenter.startRquestAddAuthData(keyword);//开始搜索按钮
        tvJg.setVisibility(View.VISIBLE);
    }
}