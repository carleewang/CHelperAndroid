package com.cpigeon.cpigeonhelper.modular.saigetong.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.ChangeFootPhotoDetailsEntity;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.ChangeFootDetailsPre;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter.ZHNumChangePhotoAdapter;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.luck.picture.lib.PictureSelector;


/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class ChangeFootDetailsFragment extends BaseMVPFragment<ChangeFootDetailsPre> {

    TextView tvCskh, tvGzxm, tvDzhh, tvSgys, tvDq, tvRpsj, tvFoot;
    RecyclerView mRecyclerView;
    private View headView;
    private ZHNumChangePhotoAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recyclerview_not_white_no_padding_layout;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }

    @Override
    protected ChangeFootDetailsPre initPresenter() {
        return new ChangeFootDetailsPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initView();
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getFootInfo(list -> {
            hideLoading();
            mAdapter.setNewData(list);
            bindData(list.get(0));
        });
    }

    private void initView() {
        try {
            setTitle(mPresenter.searchFootEntity.getFoot());

            mRecyclerView = findViewById(R.id.list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new ZHNumChangePhotoAdapter(Lists.newArrayList(), getActivity());
            mAdapter.setmPre(mPresenter);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                PictureSelector.create(this).externalPicturePreview(position, mPresenter.images);
            });


            headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sg_headview, null, false);
            tvCskh = (TextView) headView.findViewById(R.id.tv_cskh);
            tvGzxm = (TextView) headView.findViewById(R.id.tv_gzxm);
            tvDzhh = (TextView) headView.findViewById(R.id.tv_dzhh);
            tvSgys = (TextView) headView.findViewById(R.id.tv_sgys);
            tvDq = (TextView) headView.findViewById(R.id.tv_dq);
            tvRpsj = (TextView) headView.findViewById(R.id.tv_rpsj);
            tvFoot = (TextView) headView.findViewById(R.id.tv_zhhm);
            headView.findViewById(R.id.text_modify).setVisibility(View.GONE);
            tvFoot.setTextColor(getResources().getColor(R.color.white));
            tvFoot.setBackgroundColor(getResources().getColor(R.color.color_61c0f6));

            mAdapter.bindToRecyclerView(mRecyclerView);
            showDialogLoading();
            mPresenter.getFootInfo(list -> {
                hideLoading();
                mAdapter.addHeaderView(headView);
                mAdapter.setNewData(list);
                bindData(list.get(0));
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void bindData(ChangeFootPhotoDetailsEntity entity) {
        try {
            tvCskh.setText(entity.cskh);
            tvGzxm.setText(entity.xingming);
            tvSgys.setText(entity.color);
            tvDq.setText(entity.diqu);
            tvRpsj.setText(entity.rpsj);
            tvDzhh.setText(entity.ring);
            tvFoot.setText(entity.foot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
