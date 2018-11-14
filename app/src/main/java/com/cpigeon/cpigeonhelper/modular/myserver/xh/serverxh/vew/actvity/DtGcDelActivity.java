package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DelGcDtEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.GameGcPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.XhdtPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter.GameGcDtDelAdapter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameDtView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameGcView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 动态规程删除activity
 * Created by Administrator on 2018/1/10.
 */

public class DtGcDelActivity extends ToolbarBaseActivity implements GameDtView, GameGcView {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<DtListEntity> datasDt = new ArrayList<>();
    private List<GcListEntity> datasGc = new ArrayList<>();


    private GameGcDtDelAdapter delAdapter;//规程删除适配器

    private XhdtPrensenter mXhdtPrensenter;//控制层
    private GameGcPrensenter mGameGcPrensenter;


    private int tagState = 1;//全部选择的状态（1:没有点击全选，2：点击全选）

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.layout_list_gc_dt;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    private List<DelGcDtEntity> delData;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mSwipeRefreshLayout.setEnabled(false);//关闭刷新
        mCustomEmptyView.setVisibility(View.GONE);//隐藏错误视图
        delData = new ArrayList<>();//删除数据

        mXhdtPrensenter = new XhdtPrensenter(this);
        mGameGcPrensenter = new GameGcPrensenter(this);

        setTopLeftButton(R.drawable.ic_back, DtGcDelActivity.this::finish);

        setTopRightButton("全选", () -> {
            try {
                int dataSize = 0;//数据长度
                dataSize = delAdapter.getData().size();
                //赛事规程
                //全部选择的状态（1:没有点击全选，2：点击全选）
                if (tagState == 1) {//全部选中
                    tagState = 2;
                    setTopRightButton("取消");
                    if (dataSize == 0) {//数据为空
                        return;
                    }

                    for (int i = 0; i < dataSize; i++) {
                        delAdapter.getData().get(i).setClickTag(2);//当前设置当前状态为选中状态
                    }
                } else if (tagState == 2) {//全部未选中
                    tagState = 1;
                    setTopRightButton("全选");

                    if (dataSize == 0) {//数据为空
                        return;
                    }

                    for (int i = 0; i < dataSize; i++) {
                        delAdapter.getData().get(i).setClickTag(1);//当前设置当前状态为未选中状态
                    }
                }
                delAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (GameGcActivity.type.equals("ssgc")) {
            //赛事规程
            setTitle("赛事规程");
            this.datasGc = GameGcActivity.datasGc;

            if (datasGc.size() > 0) {
                DelGcDtEntity mDelGcDtEntity;
                for (int i = 0; i < datasGc.size(); i++) {
                    mDelGcDtEntity = new DelGcDtEntity();
                    mDelGcDtEntity.setId(datasGc.get(i).getGcid());
                    mDelGcDtEntity.setTime(datasGc.get(i).getFbsj());
                    mDelGcDtEntity.setTitle(datasGc.get(i).getTitle());
                    mDelGcDtEntity.setClickTag(1);
                    delData.add(mDelGcDtEntity);
                }
            }

        } else if (GameGcActivity.type.equals("xhdt")) {
            //协会动态
            setTitle("协会动态");
            this.datasDt = GameGcActivity.datasDt;

            if (datasDt.size() > 0) {
                DelGcDtEntity mDelGcDtEntity;
                for (int i = 0; i < datasDt.size(); i++) {
                    mDelGcDtEntity = new DelGcDtEntity();
                    mDelGcDtEntity.setId(datasDt.get(i).getDtid());
                    mDelGcDtEntity.setTime(datasDt.get(i).getFbsj());
                    mDelGcDtEntity.setTitle(datasDt.get(i).getTitle());
                    mDelGcDtEntity.setClickTag(1);
                    delData.add(mDelGcDtEntity);
                }
            }
        }

        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    @Override
    public void initRecyclerView() {

        //赛事规程
        delAdapter = new GameGcDtDelAdapter(delData);


        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(delAdapter);
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


    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg) {

    }

    /**
     * 获取协会动态列表
     *
     * @param datas
     * @param msg
     */
    @Override
    public void getDtList(List<DtListEntity> datas, String msg) {

        try {
            if (datas != null && datas.size() > 0) {
                List<DelGcDtEntity> delData1 = new ArrayList<>();
                DelGcDtEntity mDelGcDtEntity;
                for (int i = 0; i < datas.size(); i++) {
                    mDelGcDtEntity = new DelGcDtEntity();
                    mDelGcDtEntity.setId(datas.get(i).getDtid());
                    mDelGcDtEntity.setTime(datas.get(i).getFbsj());
                    mDelGcDtEntity.setTitle(datas.get(i).getTitle());
                    mDelGcDtEntity.setClickTag(1);
                    delData1.add(mDelGcDtEntity);
                }
                delAdapter.getData().clear();
                delAdapter.addData(delData1);
                delAdapter.notifyDataSetChanged();
            } else {
                delAdapter.getData().clear();
                delAdapter.notifyDataSetChanged();
                initErrorView(msg);
            }

            //发布事件（通知比赛列表刷新数据）
            EventBus.getDefault().post("xhgcListRefresh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取赛事规程列表
     *
     * @param datas
     * @param msg
     */
    @Override
    public void getGCList(List<GcListEntity> datas, String msg) {

        try {
            if (datas != null && datas.size() > 0) {
                Log.d(TAG, "getGCList1: " + datas.size());
                List<DelGcDtEntity> delData1 = new ArrayList<>();

                for (int i = 0; i < datas.size(); i++) {
                    DelGcDtEntity mDelGcDtEntity = new DelGcDtEntity();
                    mDelGcDtEntity.setId(datas.get(i).getGcid());
                    mDelGcDtEntity.setTime(datas.get(i).getFbsj());
                    mDelGcDtEntity.setTitle(datas.get(i).getTitle());
                    mDelGcDtEntity.setClickTag(1);
                    delData1.add(mDelGcDtEntity);
                    Log.d(TAG, "getGCList: " + delData1.size());
                }

                delAdapter.getData().clear();
                delAdapter.notifyDataSetChanged();
                Log.d(TAG, "getGCList2: " + delData1.size());
                delAdapter.setNewData(delData1);
            } else {
                delAdapter.getData().clear();
                delAdapter.notifyDataSetChanged();
                initErrorView(msg);
            }
            //发布事件（通知比赛列表刷新数据）
            EventBus.getDefault().post("xhgcListRefresh");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 赛事规程删除回调
     *
     * @param listApiResponse
     * @param msg
     */
    @Override
    public void delResults_gc(ApiResponse<Object> listApiResponse, String msg) {

        try {

            if (listApiResponse.getErrorCode() == 0) {
                CommonUitls.showSweetDialog(this, msg);
                if (GameGcActivity.type.equals("ssgc")) {
                    //赛事规程
                    mGameGcPrensenter.getXhgcList();//获取赛事规程列表
                }
            } else {
                CommonUitls.showSweetDialog(this, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 协会动态删除结果回调
     *
     * @param listApiResponse
     * @param msg
     */
    @Override
    public void delResults_dt(ApiResponse<Object> listApiResponse, String msg) {

        try {
            if (listApiResponse.getErrorCode() == 0) {
                if (GameGcActivity.type.equals("xhdt")) {
                    CommonUitls.showSweetDialog(this, msg);
                    //协会动态
                    mXhdtPrensenter.getXhdtList();//获取协会动态列表
                }
            } else {
                CommonUitls.showSweetDialog(this, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg) {

    }


    @OnClick({R.id.ll_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_del:
                try {
                    String delPosition = "";
                    delAdapter.notifyDataSetChanged();

                    if (delAdapter.getData().size()==0){
                        CommonUitls.showToast(this, "当前没有数据");
                        return;
                    }

                    for (int i = 0; i < delAdapter.getData().size(); i++) {
                        if (delAdapter.getData().get(i).getClickTag() == 2) {
                            delPosition += delAdapter.getData().get(i).getId() + ",";
                        }
                    }
//                Log.d(TAG, "onViewClicked: -->" + delPosition);

                    if (delPosition.equals("")) {//选择的条数为空
                        CommonUitls.showToast(this, "请先进行选择");
                        return;
                    } else {
                        delPosition = delPosition.substring(0, delPosition.length() - 1);
                    }

//                Log.d(TAG, "选择的字符串拼接: ---》" + delPosition);

                    final String delString = delPosition;

                    if (GameGcActivity.type.equals("ssgc")) {
                        CommonUitls.showSweetDialog(this, "您确定要删除吗？删除后数据不可恢复!" , dialog -> {
                            dialog.dismiss();
                            mGameGcPrensenter.delGc_XH(delString);
                        });
                    } else if (GameGcActivity.type.equals("xhdt")) {

                        CommonUitls.showSweetDialog(this, "您确定要删除吗？删除后数据不可恢复!" , dialog -> {
                            dialog.dismiss();
                            mXhdtPrensenter.delDt_XH(delString);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
