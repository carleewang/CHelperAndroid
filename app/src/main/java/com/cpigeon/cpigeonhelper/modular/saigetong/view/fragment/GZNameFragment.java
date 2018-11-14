package com.cpigeon.cpigeonhelper.modular.saigetong.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter.GZNameAdapter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;

import java.util.List;

import butterknife.BindView;


/**
 * 鸽主名称
 * Created by Administrator on 2017/12/5.
 */
public class GZNameFragment extends BaseFragment{
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private SGTPresenter mSGTPresenter;//控制层
    private GZNameAdapter mAdapter;
    private String str;

    private String TAG = "SGTHomeListAdapter";

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_gz_name;
    }

    @Override
    public void finishCreateView(Bundle state) {
        SGTHomeListEntity sgtHomeListEntity = (SGTHomeListEntity) getActivity().getIntent().getSerializableExtra("SGTHomeListEntity");

        str = sgtHomeListEntity.getCskh();

        mSGTPresenter = new SGTPresenter(new SGTViewImpl(){
            @Override
            public void getGZImgEntity(List<GZImgEntity> gzImgData, String str) {

                try {
                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用
                    if (gzImgData != null && gzImgData.size() > 0) {
                        Log.d("SGTHomeListAdapter", "getGZImgEntity: " + gzImgData.size() + "   str-->" + str);
                        mAdapter.setNewData(gzImgData);
                    } else {
                        Log.d("SGTHomeListAdapter", "   str-->" + str);
                        initErrorView(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void initRecyclerView() {
        mAdapter = new GZNameAdapter(null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            if (str != null && !str.equals("")) {
                mSGTPresenter.getSGTImagesGeZhu(str);
            } else {

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (str != null && !str.equals("")) {
                mSGTPresenter.getSGTImagesGeZhu(str);
            } else {

            }
        });
    }

    /**
     * 显示错误信息
     *
     * @param tips
     */
    public void initErrorView(String tips) {
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.face);
        mCustomEmptyView.setEmptyText(tips);
    }
}
