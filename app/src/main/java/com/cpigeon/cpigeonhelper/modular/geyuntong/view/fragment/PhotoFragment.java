package com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTMonitorPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.PigeonMonitorActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter.PhotoAdapter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.MonitorViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.OfflineFileManager;
import com.cpigeon.cpigeonhelper.utils.PermissonUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 比赛照片
 * Created by Administrator on 2017/10/10.
 */
public class PhotoFragment extends BaseFragment {

    @BindView(R.id.phoro_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.phoro_customEmptyView)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.photo_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件

    @BindView(R.id.btn_add_photo)
    Button btnAddPhoto;

    private PhotoAdapter mAdapter;//列表展示适配器
    private GYTMonitorPresenter presenter;//控制层

    private static PhotoFragment fragment;

    OfflineFileManager offlineFileManager;

    public static PhotoFragment newInstance() {
        if (fragment == null) {
            synchronized (PhotoFragment.class) {
                if (fragment == null)
                    fragment = new PhotoFragment();
            }
        }
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_photo;
    }

    @Override
    public void finishCreateView(Bundle state) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        presenter = new GYTMonitorPresenter(new MonitorViewImpl() {

            /**
             * 获取图片数据返回
             */
            @Override
            public void imgOrVideoData(ApiResponse<List<ImgOrVideoEntity>> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (getActivity() == null || getActivity().isDestroyed()) {
                        return;
                    }

                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用
                    if (mThrowable != null) {//抛出异常

                        SwipeRefreshUtil.swipeRefreshLayoutCustom2(mSwipeRefreshLayout, mCustomEmptyView,
                                mRecyclerView, mThrowable, mThrowable.getLocalizedMessage(), view -> {
                                    SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    againRequest();//重新请求数据
                                });

                    } else {//成功获取到数据
                        if (listApiResponse.getErrorCode() == 0) {
                            if (listApiResponse.getData().size() > 0) {
                                if (listApiResponse.getData().size() == 0 && mAdapter.getData().size() == 0) {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                                } else {
                                    PhotoFragment.this.datas.clear();
                                    PhotoFragment.this.datas = listApiResponse.getData();
                                    mCustomEmptyView.setVisibility(View.GONE);//隐藏错误视图
                                    mRecyclerView.setVisibility(View.VISIBLE);//展示数据视图
                                    mAdapter.addData(listApiResponse.getData());
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (mAdapter.getData().size() > 0) {
                                    mAdapter.loadMoreEnd(false);//加载更多的结束
                                } else {
                                    SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, "暂无数据");
                                }
                            }
                        } else {
                            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
                        }
                    }
                } catch (Exception e) {

                }

            }
        });//初始化控制层


        try {
            //鸽运通
            //训鸽通
            offlineFileManager = new OfflineFileManager(getContext(), String.valueOf(MonitorData.getMonitorId()),
                    OfflineFileManager.TYPE_IMG);

        } catch (Exception e) {

        }

        try {

            initRecyclerView();//初始化RecyclerView
            initRefreshLayout();//初始化刷新

        } catch (Exception e) {

        }

        try {
            //【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
            if (MonitorData.getMonitorStateCode() == 2 || MonitorData.getMonitorStateCode() == 3) {//监控结束，授权人查看隐藏按钮
                btnAddPhoto.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

    }

    /**
     * 初始化刷新布局
     */
    @Override
    public void initRefreshLayout() {

        try {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
            mSwipeRefreshLayout.post(() -> {

                try {
                    mSwipeRefreshLayout.setRefreshing(true);
                    presenter.getImgOrVideo("image");//获取比赛图片
                } catch (Exception e) {
                }
            });

            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
                againRequest();//重新请求数据
            });
        } catch (Exception e) {

        }

    }


    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String playListRefresh) {

        try {
            if (playListRefresh.equals("photoRefresh")) {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                presenter.getImgOrVideo("image");
            }

            if (playListRefresh.equals("endPlay") || playListRefresh.equals("endPlay1")) {
                btnAddPhoto.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);//取消注册
    }

    /**
     * 初始化RecyclerView
     */
    private List<ImgOrVideoEntity> datas;
    private List<LocalMedia> list;

    @Override
    public void initRecyclerView() {
        mAdapter = new PhotoAdapter(null);
        datas = new ArrayList<>();
        list = new ArrayList<>();
        mAdapter.setLoadMoreView(new CustomLoadMoreView());//设置负载更多的视图
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//打开加载动画

        //查看图片详细
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (datas.size() > 0) {
                    list.clear();//清空之前保存的数据
                    for (int i = 0; i < datas.size(); i++) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(datas.get(i).getUrl());
                        list.add(localMedia);
                    }

                    if (list.size() > 0) {
                        //图片预览展示
                        PictureSelector.create(PhotoFragment.this).externalPicturePreview(position, list);
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        mAdapter.setEnableLoadMore(true);//设置启用加载更多
    }


    //重新获取数据
    private void againRequest() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        presenter.getImgOrVideo("image");//获取比赛图片
    }

    @OnClick({R.id.btn_add_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_photo:
                PermissonUtil.getAppDetailSettingIntent(getActivity());//权限检查（添加权限）
                if (MonitorData.getMonitorStateCode() == 2) {
                    CommonUitls.showToast(getActivity(), "已完成监控，不能上传图片");
                } else {
                    ((PigeonMonitorActivity) getActivity()).showUploadDialog(offlineFileManager);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
