package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTListPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter.EditGytListAdapter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListView;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DpSpUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.refresh.SwipeRefreshUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 编辑鸽运通列表Activity
 * Created by Administrator on 2017/9/30.
 */

public class EditGytListActivity extends ToolbarBaseActivity implements GYTListView {

    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView mRecyclerView;//列表展示

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新控件

    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;//错误图展示
    @BindView(R.id.selection_state_img)
    ImageView selectionStateImg;//全选状态的图片
    @BindView(R.id.selection_state_ll)
    LinearLayout selectionStateLl;//点击全选的布局
    @BindView(R.id.tv_del)
    TextView tvDel;//删除的按钮
    private GYTListPresenter presenter;//鸽运通

    private EditGytListAdapter mAdapter;//适配器

    private int tagState = 1;//全部选择的状态（1:没有点击全选，2：点击全选）

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_gyt_list;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //初始化toobal
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            //鸽运通
            setTitle("鸽运通编辑");
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            //训鸽通
            setTitle("训鸽通编辑");
        }

        setTopLeftButton(R.drawable.ic_back, EditGytListActivity.this::finish);

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        presenter = new GYTListPresenter(this);

        initRecyclerView();//初始化RecyclerView
        initRefreshLayout();//刷新布局刷新
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
            againRequest();
        });
    }


    @Override
    public void initRecyclerView() {
        // 设置菜单创建器。
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                //item点击监听
                switch (menuBridge.getPosition()) {
                    case 1://删除
                        SweetAlertDialog dialog = new SweetAlertDialog(EditGytListActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("温馨提示");
                        dialog.setContentText("确定要删除该场比赛");
                        dialog.setCancelText("取消");
                        dialog.setConfirmText("确定");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                presenter.deleteGYTRace(mAdapter.getData().get(menuBridge.getAdapterPosition()).getId() + "");//删除单条数据
                                mRecyclerView.smoothCloseMenu();//平滑关闭菜单
                                dialog.dismiss();

                                mLoadDataDialog.show();
                            }
                        });
                        dialog.setCancelable(true);
                        dialog.show();

                        break;
                    case 0://编辑
                        mRecyclerView.smoothCloseMenu();//平滑关闭菜单
                        if (mAdapter.getData().get(menuBridge.getAdapterPosition()).getStateCode() == 2) {
                            //监控结束
                            CommonUitls.showSweetDialog(EditGytListActivity.this, "监控已结束，无法进行编辑");
                            return;
                        }
                        //传到下一个界面一个数据，编辑
                        Intent intent = new Intent(EditGytListActivity.this, UpdateGYTActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("gyt_name", mAdapter.getData().get(menuBridge.getAdapterPosition()).getRaceName());
                        bundle.putString("gyt_lo", String.valueOf(mAdapter.getData().get(menuBridge.getAdapterPosition()).getLongitude()));
                        bundle.putString("gyt_la", String.valueOf(mAdapter.getData().get(menuBridge.getAdapterPosition()).getLatitude()));
                        bundle.putString("gyt_area", mAdapter.getData().get(menuBridge.getAdapterPosition()).getFlyingArea());
                        bundle.putInt("gyt_rid", mAdapter.getData().get(menuBridge.getAdapterPosition()).getId());
                        intent.putExtras(bundle);
                        startActivity(intent);

                        break;
                }
            }
        });

        mAdapter = new EditGytListAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        //给recycleView添加下划线
        Paint paint = new Paint();
        paint.setStrokeWidth(DpSpUtil.dip2px(this, 8));
        paint.setColor(getResources().getColor(R.color.color_gyt_item_bg));
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .paint(paint)
                .build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String playListRefresh) {
        if (playListRefresh.equals("playListRefreshs")) {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            presenter.getGYTRaceList(-1, -1, "", "", "");//获取数据
        }
    }


    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setText("编辑") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setBackgroundColor(getResources().getColor(R.color.colorRed))
                    .setTextSize(16) // 文字大小。
                    .setWidth(170)
                    .setHeight(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.

            SwipeMenuItem editItem = new SwipeMenuItem(mContext)
//                    .setBackground(R.drawable.selector_red)
//                    .setImage(R.mipmap.ic_action_delete) // 图标。
                    .setText("删除")
                    .setBackgroundColor(getResources().getColor(R.color.color_ccc))
                    .setTextSize(16) // 文字大小。
                    .setWidth(170)
                    .setHeight(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT);

            swipeRightMenu.addMenuItem(editItem); // 添加一个按钮到左侧菜单。

            // 上面的菜单哪边不要菜单就不要添加。
        }
    };


    private List<GeYunTong> datas = new ArrayList<>();

    @Override
    public void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable) {


        try {
            if (EditGytListActivity.this.isDestroyed()) {
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
                            showErrorView();
                        } else {
                            mAdapter.addData(listApiResponse.getData());
                            mAdapter.notifyDataSetChanged();
                            finishTask();//加载下一页
                        }
                    } else {
                        if (mAdapter.getData().size() > 0) {
                            mAdapter.loadMoreEnd(false);//加载更多的结束
                        } else {
                            showErrorView();
                        }
                    }
                } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, EditGytListActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                } else {
                    showErrorView();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //显示错误视图
    private void showErrorView() {
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_gyt);
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, R.string.str_hint_xgt);
        }
    }

    //重新请求数据
    private void againRequest() {
        SwipeRefreshUtil.showNormal(mCustomEmptyView, mRecyclerView);//显示正常布局
        selectionStateImg.setImageResource(R.mipmap.yuan);
        mAdapter.getData().clear();//清除所有数据
        mAdapter.notifyDataSetChanged();
        presenter.getGYTRaceList(-1, -1, "", "", "");//获取数据
    }

    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {

        datas.clear();//清空之前的数据
        if (geYunTongDatas.size() > 0) {
            for (int i = 0; i < geYunTongDatas.size(); i++) {
                if (geYunTongDatas.get(i).getStateCode() == 0 || geYunTongDatas.get(i).getStateCode() == 2) {
                    //状态为未开始监控，或者已经监控结束
                    geYunTongDatas.get(i).setTag(1);
                    datas.add(geYunTongDatas.get(i));//添加进新的集合
                }
            }
        }

        if (datas.size() > 0) {
            hideEmptyView();//显示正常数据
            mAdapter.getData().clear();
            mAdapter.addData(datas);
            datas.clear();
            mAdapter.notifyDataSetChanged();
        } else {
            hideNormalView();//显示错误提示
        }
    }

    /**
     * 隐藏错误提示，显示正常数据
     */
    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);//隐藏没有列表的情况
        mRecyclerView.setVisibility(View.VISIBLE);//显示有列表数据的情况
    }

    /**
     * 显示错误提示，隐藏正常数据
     */
    public void hideNormalView() {
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.mipmap.face);
        mCustomEmptyView.setEmptyText("没有可编辑的数据列表");
    }

    private int dataSize;

    /**
     * @param msg
     */
    @Override
    public void getReturnMsg(String msg) {

        try {
            //发布事件//通知比赛列表页面刷新
            EventBus.getDefault().post("playListRefresh");

            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

            switch (msg) {
                case "批量删除成功":
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog2(errSweetAlertDialog, "您已成功删除内容", this, dialog -> {
                        dialog.dismiss();
                    });
                    againRequest();

                    break;
                case "删除成功"://单条数据
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog2(errSweetAlertDialog, "您已成功删除选择内容", this, dialog -> {
                        dialog.dismiss();
                    });

                    againRequest();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addPlaySuccess() {

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


    @OnClick({R.id.selection_state_ll, R.id.tv_del})
    public void onViewClicked(View view) {
        dataSize = mAdapter.getData().size();
        switch (view.getId()) {
            case R.id.selection_state_ll://点击全选的布局
                //全部选择的状态（1:没有点击全选，2：点击全选）
                if (tagState == 1) {//全部选中
                    tagState = 2;
                    selectionStateImg.setImageResource(R.mipmap.yuan_ok);
                    if (dataSize == 0) {//数据为空
                        return;
                    }
                    for (int i = 0; i < dataSize; i++) {
                        mAdapter.getData().get(i).setTag(2);//当前设置当前状态为选中状态
                    }
                } else if (tagState == 2) {//全部未选中
                    tagState = 1;
                    selectionStateImg.setImageResource(R.mipmap.yuan);

                    if (dataSize == 0) {//数据为空
                        return;
                    }
                    for (int i = 0; i < dataSize; i++) {
                        mAdapter.getData().get(i).setTag(1);
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_del://删除按钮
                String delPosition = "";
                mAdapter.notifyDataSetChanged();
                for (int i = 0; i < dataSize; i++) {
                    if (mAdapter.getData().get(i).getTag() == 2) {
                        delPosition += mAdapter.getData().get(i).getId() + ",";
                    }
                }

                if (delPosition.equals("")) {//选择的条数为空
                    CommonUitls.showToast(this, "您还没有选择任何比赛");
                    return;
                } else {
                    delPosition = delPosition.substring(0, delPosition.length() - 1);
                }

                final String delString = delPosition;

                errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "你确定要删除吗？删除后数据不可恢复!", this, dialog -> {
                    dialog.dismiss();
                    mLoadDataDialog.show();
                    presenter.deleteGYTRaces(delString);//多选删除
                });

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
