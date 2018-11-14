package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetChaZuListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetJiangJinXianShiBiLiEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter.GpSmsPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.adapter.DesignatedDetailAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * 插组指定 详细 列表
 * Created by Administrator on 2018/4/28.
 */
public class DesignatedDetailActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private DesignatedDetailAdapter mAdapter;

    private GpSmsPresenter mGpSmsPresenter;
    private DesignatedListEntity mDesignatedListEntity;

    private int serviceType = -1;//1 协会 2 公棚

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTitle("插组管理");
        setTopLeftButton(R.drawable.ic_back, DesignatedDetailActivity.this::finish);

        setTopRightButton("奖金比例", () -> {
            mGpSmsPresenter.subGP_GetJiangJinXianShiBiLi(mDesignatedListEntity.getTid() + "", serviceType);
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        serviceType = getIntent().getIntExtra("serviceType", -1);

        mDesignatedListEntity = (DesignatedListEntity) getIntent().getSerializableExtra("data");
        mGpSmsPresenter = new GpSmsPresenter(new GpSmsViewImpl() {

            @Override
            public void getGetChaZuList(ApiResponse<List<GetChaZuListEntity>> listApiResponse, String msg, Throwable mThrowable) {
                try {
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
                            if (listApiResponse.getData().size() > 0) {
                                mAdapter.getData().clear();//清空数据
                                mAdapter.setNewData(listApiResponse.getData());//设置数据
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_czzd);
                                }
                            }
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, DesignatedDetailActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_czzd);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取金额比例
            @Override
            public void setGP_GetChaZu(ApiResponse<GetJiangJinXianShiBiLiEntity> listApiResponse, String msg, Throwable throwable) {

                try {
                    if (throwable != null) {
                        DesignatedDetailActivity.this.getThrowable(throwable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {
                        mGpSmsPresenter.initMoneySetDialog(DesignatedDetailActivity.this, listApiResponse.getData().getJjbl(), new GpSmsPresenter.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, CheckBox cb_checkbox1, CheckBox cb_checkbox2, EditText et_input) {

                                dialog.dismiss();
                                if (cb_checkbox2.isChecked()) {
                                    mGpSmsPresenter.setGP_SetChaZu(String.valueOf(mDesignatedListEntity.getTid()), "100", serviceType);
                                } else {
                                    mGpSmsPresenter.setGP_SetChaZu(String.valueOf(mDesignatedListEntity.getTid()), et_input.getText().toString(), serviceType);
                                }
                            }
                        });
                    } else {
                        DesignatedDetailActivity.this.getErrorNews(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //设置金额比例
            @Override
            public void setGP_SetChaZu(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {
                try {
                    if (throwable != null) {
                        DesignatedDetailActivity.this.getThrowable(throwable);
                        return;
                    }

                    DesignatedDetailActivity.this.getErrorNews(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


        initRefreshLayout();
        initRecyclerView();
    }

    private void againRequest() {
        if (mDesignatedListEntity != null) {
            mGpSmsPresenter.getGP_GetChaZuList(mDesignatedListEntity.getTid(), serviceType);
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
            againRequest();
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
        mSwipeRefreshLayout.setEnabled(false);
        //赛事规程
        mAdapter = new DesignatedDetailAdapter(null, String.valueOf(mDesignatedListEntity.getTid()), serviceType);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.DESIGNATED_REFRESH)) {
            againRequest();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
