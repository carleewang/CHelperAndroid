package com.cpigeon.cpigeonhelper.modular.flyarea.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.presenter.FlyingPresenter;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter.SystemFlyingAreaAdapter;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao.FlyingViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;


/**
 * 参考司放地
 * Created by Administrator on 2017/6/14.
 */
public class SystemFlyingAreaFragment extends BaseFragment implements SearchEditText.OnSearchClickListener {

    public static SystemFlyingAreaFragment newInstance() {
        return new SystemFlyingAreaFragment();
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;//列表
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;//错误图展示
    @BindView(R.id.fly_area_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件

    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;//文本输入框

    private FlyingPresenter presenter;//控制层
    private String key = "";//查询关键字
    private int pi = -1;//pi  页码【小于 0 时获取全部，默认值-1】
    private int ps = 10;//ps 页大小【一页记录条数，默认值 10】

    private SystemFlyingAreaAdapter mAdapter;//参考司放地适配器

    @Override
    public int getLayoutResId() {
        return R.layout.layout_swipwithrecycler;
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }

        isPrepared = false;
        presenter = new FlyingPresenter(new FlyingViewImpl() {

            @Override
            public void getFlyingData(ApiResponse<List<FlyingAreaEntity>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用

                    if (mThrowable != null) {//抛出异常
                        SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                                mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                                    againRequest();//重新请求数据
                                });
                    } else {//成功获取到数据
                        if (listApiResponse.getErrorCode() == 0) {
                            if (listApiResponse.getData().size() > 0) {
                                if (listApiResponse.getData().size() == 0 && mAdapter.getData().size() == 0) {
                                    showErrorView();
                                } else {
                                    mAdapter.addData(listApiResponse.getData());
                                    mAdapter.notifyDataSetChanged();
                                    finishTask();//加载下一页

                                    //发送广播，告诉当前的Activity序号，添加需要
                                    Intent intent = new Intent("modular.flyarea.view.activity.MyFlyingAreaFragment.action.FlyingAreaActivity");
                                    intent.putExtra("msgs", listApiResponse.getData().get(0).getFaid());//发送第一个item的序号，
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);//发送应用内广播
                                }

                            } else {
                                EventBus.getDefault().post(EventBusService.FLYING_LIST_REFRESH);

                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    showErrorView();
                                }
                            }
                        } else {
                            showErrorView();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void addFlyingFlyingData(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {
                super.addFlyingFlyingData(listApiResponse, msg, throwable);

                try {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity());//弹出提示
                } catch (Exception e) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "网络异常，请检查网络连接", getActivity());//弹出提示
                }
            }

            /**
             * 没有数据返回
             *
             * @param msg 提示内容
             */
            @Override
            public void getFlyingDataNull(String msg) {
                try {
                    CommonUitls.showSweetDialog1(getActivity(), msg, dialog -> {
                        dialog.dismiss();
                    });

                    if (msg.equals("添加司放地成功") || msg.equals("添加训放地成功")) {
                        //列表刷新
                        EventBus.getDefault().post(EventBusService.FLYING_LIST_REFRESH);
                    } else {
                        SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        initRefreshLayout();//初始化刷新布局
        initRecyclerView();//初始化ResyclerView
        searchEdittext.setOnSearchClickListener(this);//输入文本设置监听
    }

    //显示错误视图
    private void showErrorView() {
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_sfd);
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_xfd);
        }
    }


    /**
     * 重新请求数据
     */
    private void againRequest() {
        mAdapter.getData().clear();//清除所有数据
        mAdapter.notifyDataSetChanged();
        SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
        mSwipeRefreshLayout.setRefreshing(true);
        pi = 1;
        ps = 10;
        startRequest(searchEdittext.getText().toString());//开始请求数据
    }

    /**
     * 开始请求数据
     */
    private void startRequest(String strKey) {
        presenter.getFlyingAreasData("refer",//type 获取类型 【user ：用户列表 refer：参考司放地列表】【默认值 user】
                strKey,//key  查询关键字
                pi,//pi  页码【小于 0 时获取全部，默认值-1】
                ps);//ps 页大小【一页记录条数，默认值 10】
    }

    @Override
    protected void initRecyclerView() {
        mAdapter = new SystemFlyingAreaAdapter(null, presenter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.getItemAnimator();
    }

    /**
     * 初始化刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            againRequest();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            againRequest();//重新设置数据
        });
    }

    @Override
    public void onSearchClick(View view, String keyword) {
        againRequest();
    }
}
