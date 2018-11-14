package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GeZhuFootEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.SelectFootAdapter2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.searchview.SearchEditText;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.video.RecordedSGTActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 赛鸽通 鸽主足环选择页
 * Created by Administrator on 2018/1/13.
 */

public class SelectZhActivity2 extends ToolbarBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SearchEditText.OnSearchClickListener {

    @BindView(R.id.ll_et)
    LinearLayout llEt;//编辑框父布局
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;

    @BindView(R.id.search_edittext2)
    SearchEditText searchEdittext2;
    @BindView(R.id.edit_cancel)
    TextView editCancel;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRefreshing = false;
    private int ps = 6;//页大小【一页记录条数，默认值 10】
    private int pi = 1;//页码【小于 0 时获取全部，默认值-1
    boolean canLoadMore = true, isMoreDateLoading = false;

    private SelectFootAdapter2 mAdapter;
    private SGTPresenter mSGTPresenter;

    private int id = -1, tagid = -1;

    private String tagStr;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_select_zh;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, this::finish);
        setTitle("选择足环号码");//单个足环

        tagid = getIntent().getIntExtra("tagid", -1);
        id = getIntent().getIntExtra("id", -1);
        tagStr = getIntent().getStringExtra("tagStr");

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mSGTPresenter = new SGTPresenter(new SGTViewImpl(){
            @Override
            public void getGeZhuFootData(ApiResponse<GeZhuFootEntity> listApiResponse, String msg) {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);//设置刷新
                    mSwipeRefreshLayout.setEnabled(true);//设置启用

                    if (listApiResponse != null && listApiResponse.isStatus() && listApiResponse.getErrorCode() == 0) {
                        if (listApiResponse.getData() != null && listApiResponse.getData().getFootlist().size() > 0) {
                            footData.clear();

                            for (int i = 0; i < listApiResponse.getData().getFootlist().size(); i++) {
                                GeZhuFootEntity.FootlistBean mFootlistBean = new GeZhuFootEntity.FootlistBean();

                                mFootlistBean.setId(listApiResponse.getData().getFootlist().get(i).getId());
                                mFootlistBean.setFoot(listApiResponse.getData().getFootlist().get(i).getFoot());
                                mFootlistBean.setAddress(listApiResponse.getData().getFootlist().get(i).getAddress());
                                mFootlistBean.setColor(listApiResponse.getData().getFootlist().get(i).getColor());
                                mFootlistBean.setCskh(listApiResponse.getData().getFootlist().get(i).getCskh());
                                mFootlistBean.setEye(listApiResponse.getData().getFootlist().get(i).getEye());
                                mFootlistBean.setTel(listApiResponse.getData().getFootlist().get(i).getTel());
                                mFootlistBean.setSjhm(listApiResponse.getData().getFootlist().get(i).getSjhm());
                                mFootlistBean.setSex(listApiResponse.getData().getFootlist().get(i).getSex());
                                mFootlistBean.setGpmc(listApiResponse.getData().getFootlist().get(i).getGpmc());
                                mFootlistBean.setXingming(listApiResponse.getData().getFootlist().get(i).getXingming());

                                mFootlistBean.setClickTag(1);//未点击
                                footData.add(mFootlistBean);
                            }

                            mAdapter.addData(footData);
                        } else {
                            initErrorView(msg);
                        }
                    } else if (listApiResponse != null && !listApiResponse.isStatus() && mAdapter.getData().size() != 0) {
                        if (listApiResponse.getData().getFootlist().size() > 0 && listApiResponse.getData().getFootlist().size() < 100) {
                            mAdapter.loadMoreEnd(false);//加载更多的结束
                            return;
                        }
                    } else {
                        initErrorView("暂无足环号码");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        searchEdittext.setOnSearchClickListener(this);

        initRefreshLayout();
        initRecyclerView();
    }

    //搜索监听回调
    @Override
    public void onSearchClick(View view, String keyword) {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        mSGTPresenter.getFootList_SGT(id, searchEdittext.getText().toString(), tagid);//开始搜索
    }


    @OnClick({R.id.edit_cancel, R.id.btn_determine, R.id.search_edittext2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_cancel:
                //取消
                searchEdittext2.setVisibility(View.VISIBLE);
                llEt.setVisibility(View.GONE);
                break;
            case R.id.btn_determine:
                //确定
                determineFootNum1();
                break;
            case R.id.search_edittext2:

                searchEdittext2.setVisibility(View.GONE);
                llEt.setVisibility(View.VISIBLE);
                searchEdittext.setFocusable(true);
                searchEdittext.setFocusableInTouchMode(true);
//                searchEdittext.performClick();//默认点击
                searchEdittext.requestFocus();//获取焦点
                searchEdittext.setClickDraw();
                break;
        }
    }


    private void determineFootNum1() {

        CommonUitls.showSweetDialog(this, "您确定为选择的足环号码添加图片吗?", dialog -> {
            dialog.dismiss();
            setShootImgData();
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SGTImgUploadActivity2.uplodResultsTag == 1) {
            SGTImgUploadActivity2.uplodResultsTag = -1;

            clearData();
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            pi = 1;
            mSGTPresenter.getFootList_SGT(id, searchEdittext.getText().toString(), tagid);//开始搜索
        }
    }

    private List<FootSSEntity> listdetail = new ArrayList<FootSSEntity>();

    //跳转页面，传递数据
    private void setShootImgData() {
        listdetail.clear();

        mAdapter.notifyDataSetChanged();

        if (mAdapter.getData().size() == 0) {
            CommonUitls.showToast(this, "当前没有数据");
            return;
        }

        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (mAdapter.getData().get(i).getClickTag() == 2) {

                GeZhuFootEntity.FootlistBean mData = mAdapter.getData().get(i);
                FootSSEntity mFootSSEntity = new FootSSEntity(mData.getId(), mData.getColor(), mData.getSex(),
                        mData.getAddress(), mData.getFoot(), mData.getTel(), mData.getEye(), mData.getSjhm(),
                        mData.getXingming(), mData.getCskh(), mData.getGpmc());


                Log.d("xiaoxiao1", "startDrawWater: foot-->" + mData.getFoot() + "   color-->" + mData.getColor() + "     tel-->" + mData.getTel() + "     add-->" + mData.getAddress() + "      gpmc-->" + mData.getGpmc() + "     ");

                listdetail.add(mFootSSEntity);
            }
        }

        for (FootSSEntity data : listdetail) {
            Log.d("xiaoxiao", "startDrawWater: foot-->" + data.getFoot() + "   color-->" + data.getColor() + "     tel-->" + data.getTel() + "     add-->" + data.getAddress() + "      gpmc-->" + data.getGpmc() + "     ");
        }

        if (listdetail.size() == 0) {
            CommonUitls.showToast(this, "请先进行选择");
            return;
        }

        if (listdetail.size() >= 4) {
            CommonUitls.showToast(this, "足环最多选择三个");
            return;
        }

        Intent mIntent = new Intent(this, RecordedSGTActivity.class);

        mIntent.putExtra("tagStr", getIntent().getStringExtra("tagStr"));//标签名称
        mIntent.putExtra("tagid", getIntent().getIntExtra("tagid", -1));//标签id

        mIntent.putParcelableArrayListExtra("listdetail", (ArrayList<? extends Parcelable>) listdetail);


        if (listdetail.size() == 1) {
            SaActionSheetDialog mSaActionSheetDialog = SGTPresenter2.selectFootDialog(this, new SaActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    if (which == 1) {
                        mIntent.putExtra("IMG_NUM_TAG", 2);//左脚
                    } else {
                        mIntent.putExtra("IMG_NUM_TAG", 3);//右脚
                    }

                    startActivity(mIntent);
                }
            });

            mSaActionSheetDialog.show();
        } else {
            mIntent.putExtra("IMG_NUM_TAG", 1);//1   多张图片   2   足环在左脚   3 足环在右脚
            startActivity(mIntent);
        }

    }


    /**
     * 刷新布局
     */
    @Override
    public void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_theme);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            mIsRefreshing = true;
            pi = 1;
            mSGTPresenter.getFootList_SGT(id, searchEdittext.getText().toString(), tagid);//开始搜索
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            pi = 1;
            mSGTPresenter.getFootList_SGT(id, searchEdittext.getText().toString(), tagid);//开始搜索
        });
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {
        mAdapter = new SelectFootAdapter2(null,tagStr);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void loadData() {

    }

    /**
     * 加载下一页
     */
    @Override
    public void finishTask() {
        if (canLoadMore) {
            pi++;
            mAdapter.loadMoreComplete();//负载更完整
        } else {
            mAdapter.loadMoreEnd(false);//加载更多的结束
        }
        isMoreDateLoading = mIsRefreshing = false;
        mSwipeRefreshLayout.setRefreshing(false);//设置刷新
        mSwipeRefreshLayout.setEnabled(true);//设置启用
        mIsRefreshing = false;
        hideEmptyView();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {

    }

    private List<GeZhuFootEntity.FootlistBean> footData = new ArrayList<>();

}
