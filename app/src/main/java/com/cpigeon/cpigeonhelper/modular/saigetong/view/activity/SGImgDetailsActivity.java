package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter.GZImgDetailsAdapter;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 鸽主图片详情页   舍弃
 * Created by Administrator on 2017/12/6.
 */

public class SGImgDetailsActivity extends ToolbarBaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private GZImgEntity gzImgEntity;

    private GZImgDetailsAdapter mAdapter;

    private List<LocalMedia> list;//图片展示保存

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sgt_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("图片详情");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        list = new ArrayList<>();
        gzImgEntity = (GZImgEntity) getIntent().getSerializableExtra("GZImgEntity");
        initRecyclerView();

        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void initRecyclerView() {
        if (gzImgEntity.getImglist().size() > 0) {
            mAdapter = new GZImgDetailsAdapter(gzImgEntity.getImglist());
            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            mRecyclerView.setAdapter(mAdapter);

            //查看图片详细
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (gzImgEntity.getImglist().size() > 0) {
                        list.clear();//清空之前保存的数据
                        for (int i = 0; i < gzImgEntity.getImglist().size(); i++) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(gzImgEntity.getImglist().get(i).getImgurl());
                            list.add(localMedia);
                        }
                        if (list.size() > 0) {
                            //图片预览展示
                            PictureSelector.create(SGImgDetailsActivity.this).externalPicturePreview(position, list);
                        }
                    }
                }
            });
        } else {
            initErrorView("暂无图片");
        }
    }

    /**
     * 显示错误信息
     *
     * @param tips
     */
    public void initErrorView(String tips) {
//        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.face);
        mCustomEmptyView.setEmptyText(tips);
    }
}
