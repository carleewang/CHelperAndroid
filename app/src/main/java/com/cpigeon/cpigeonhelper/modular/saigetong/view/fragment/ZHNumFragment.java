package com.cpigeon.cpigeonhelper.modular.saigetong.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.ViewControlShare;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTFootInfoEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2.SGTHomeActivity3;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter.ZHNumAdapter2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.CustomLoadMoreView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.DialogUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 足环号码
 * Created by Administrator on 2017/12/5.
 */

public class ZHNumFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView tvCskh, tvGzxm, tvZhhm, tvDzhh, tvSgys, tvDq, tvRpsj, text_modify;
    private LinearLayout ll_zhhm_z;
    private AlertDialog dialog;
    private SGTHomeListEntity.DataBean entity;

    private boolean mIsRefreshing = false;
    private int ps = 6;//页大小【一页记录条数，默认值 10】
    private int pi = 1;//页码【小于 0 时获取全部，默认值-1
    boolean canLoadMore = true, isMoreDateLoading = false;


    private SGTPresenter mSGTPresenter;//控制层
    private ZHNumAdapter2 mAdapter;
    private View headView;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_zh_num;
    }

    private String TAG = "SGTPresenter";

    @Override
    public void finishCreateView(Bundle state) {
//        layout_sg_headview

        dialogFragment = new ShareDialogFragment();

        mSGTPresenter = new SGTPresenter(new SGTViewImpl() {

            /**
             * 修改足环号码结果回调
             *
             * @param dataApiResponse
             * @param msg
             */
            @Override
            public void getSetRpTimeResults(ApiResponse<Object> dataApiResponse, String msg) {
                try {
                    mSGTPresenter.getFootInfo(entity.getId());//刷新数据

                    CommonUitls.showSweetDialog(getActivity(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void getFootInfo(SGTFootInfoEntity infoData, String str) {
                tvCskh.setText(infoData.getCskh());//
                tvGzxm.setText(infoData.getXingming());//姓名
                tvZhhm.setText(infoData.getFoot());//足环号码
                tvDzhh.setText(infoData.getRing());//电子环号
                tvSgys.setText(infoData.getColor());//羽色
                tvDq.setText(infoData.getDiqu());//地区
                tvRpsj.setText(infoData.getRpsj());//如鹏时间


                dialog = DialogUtil.initXiaohlDialog(getActivity(), "修改足环号码", infoData.getFoot(), InputType.TYPE_CLASS_TEXT, new DialogUtil.DialogClickListener() {
                    @Override
                    public void onDialogClickListener(View viewSure, View viewCel, AlertDialog dialog, String etStr) {
                        dialog.dismiss();
                        if (viewSure != null) {//点击确定
                            mSGTPresenter.editSGTFoot(entity.getId(), etStr); //修改足环号码
                        }

                        if (viewCel != null) {//点击取消

                        }
                    }
                });

                ll_zhhm_z.setOnClickListener(view -> {
                    if (dialog.isShowing()) dialog.dismiss();
                    dialog.show();
                });
            }


            @Override
            public void getErrorNews(String str) {
                try {
                    initErrorView(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void getFootImg(List<SGTImgEntity> imgDatas, String str) {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用
                    if (imgDatas != null && imgDatas.size() > 0) {
                        mAdapter.setNewData(imgDatas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        entity = (SGTHomeListEntity.DataBean) getActivity().getIntent().getSerializableExtra("DataBean");

        mSGTPresenter.getFootInfo(entity.getId());


        initRecyclerView();
        initRefreshLayout();

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String SGTFootRefresh) {
        if (SGTFootRefresh.equals("SGTFootRefresh")) {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            mSGTPresenter.getFootImg(entity.getId(), getActivity());
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);//取消注册
    }

    /**
     * 刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);

        //第一次进入自动刷新
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            if (entity != null) {
                mSGTPresenter.getFootImg(entity.getId(), getActivity());
            } else {

            }
        });

        //手动刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            if (entity != null) {
                mSGTPresenter.getFootImg(entity.getId(), getActivity());
            } else {

            }
        });
    }

    private ShareDialogFragment dialogFragment;

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        try {
            mAdapter = new ZHNumAdapter2(null);
            mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(getActivity(), mRecyclerView);
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sg_headview, null, false);
            tvCskh = (TextView) headView.findViewById(R.id.tv_cskh);
            tvGzxm = (TextView) headView.findViewById(R.id.tv_gzxm);
            tvZhhm = (TextView) headView.findViewById(R.id.tv_zhhm);
            tvDzhh = (TextView) headView.findViewById(R.id.tv_dzhh);
            tvSgys = (TextView) headView.findViewById(R.id.tv_sgys);
            tvDq = (TextView) headView.findViewById(R.id.tv_dq);
            tvRpsj = (TextView) headView.findViewById(R.id.tv_rpsj);
            text_modify = (TextView) headView.findViewById(R.id.text_modify);
            ll_zhhm_z = (LinearLayout) headView.findViewById(R.id.ll_zhhm_z);//修改足环号码




            if (SGTPresenter.isAuth || SGTHomeActivity3.isShowPhone == 1) {
                text_modify.setVisibility(View.GONE);
                ll_zhhm_z.setEnabled(false);
            }

            mAdapter.addHeaderView(headView);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter.setEnableLoadMore(true);

            List<LocalMedia> list = new ArrayList<>();//图片展示保存
            //查看图片详细
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mAdapter.getData().size() > 0) {
                        list.clear();//清空之前保存的数据
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(mAdapter.getData().get(i).getImgurl());
                            list.add(localMedia);
                        }
                        if (list.size() > 0) {
                            //图片预览展示
                            PictureSelector.create(ZHNumFragment.this).externalPicturePreview(position, list);
                        }
                    }
                }
            });

            mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                    if (dialogFragment != null) {
                        dialogFragment.setShareContent(mAdapter.getData().get(position).getImgurl());
                        dialogFragment.setShareListener(ViewControlShare.getShareResultsDown(getActivity(), dialogFragment, "tp"));
                        dialogFragment.setShareType(2);
                        dialogFragment.show((getActivity()).getFragmentManager(), "share");
                    }

                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * hl  设置滚动事件
     */
    private void setRecycleNoScroll() {
        mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
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

    private void clearData() {
        mIsRefreshing = true;
    }


}
