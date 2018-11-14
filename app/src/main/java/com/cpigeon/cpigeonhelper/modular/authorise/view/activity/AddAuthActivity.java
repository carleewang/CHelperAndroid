package com.cpigeon.cpigeonhelper.modular.authorise.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.presenter.AddAuthPresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.adapter.AddAuthAdapter;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AddAuthView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加用户授权界面
 * Created by Administrator on 2017/9/21.
 */
public class AddAuthActivity extends ToolbarBaseActivity implements AddAuthView, SearchEditText.OnSearchClickListener {

    //    @BindView(R.id.imgbtn_start_search)
//    ImageButton imgbtnStartSearch;//开始搜索按钮
//    @BindView(R.id.ac_add_phone)
//    EditText acAddPhone;//输入用户手机号码
//    @BindView(R.id.celel_input_content)
//    ImageButton celelInputContent;//清除输入内容
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

    private AddAuthAdapter mAdapter;//适配器

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
        setTitle("添加授权");
        setTopLeftButton(R.drawable.ic_back, AddAuthActivity.this::finish);

        presenter = new AddAuthPresenter(this);//初始化控制层

        initRecyclerView();//初始化RecyclerView

        mSearchEditText.setOnSearchClickListener(this);//输入文本设置监听

    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new AddAuthAdapter(null, presenter);
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
