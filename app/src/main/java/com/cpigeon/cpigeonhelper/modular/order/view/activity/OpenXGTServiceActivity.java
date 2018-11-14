package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.XGTPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.XGTView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开通续费训鸽通服务
 * Created by Administrator on 2017/12/8.
 */
public class OpenXGTServiceActivity extends ToolbarBaseActivity implements XGTView {

    @BindView(R.id.it_meal_grade2)
    TextView itMealGrade2;
    @BindView(R.id.it_meal_hint2)
    TextView itMealHint2;
    @BindView(R.id.it_meal_content2)
    TextView itMealContent2;
    @BindView(R.id.it_meal_cost2)
    TextView itMealCost2;
    @BindView(R.id.it_meal_start_order2)
    TextView itMealStartOrder2;
    @BindView(R.id.it_meal_click2)
    RelativeLayout itMealClick2;
    @BindView(R.id.it_meal_ll2)
    LinearLayout itMealLl2;

    private XGTPresenter mXGTPresenter;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_open_xgt_service;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTitle("续费服务");
        setTopLeftButton(R.drawable.ic_back, OpenXGTServiceActivity.this::finish);

        itMealLl2.setBackground(getResources().getDrawable(R.mipmap.xgt_openservice_item_bg));
        itMealStartOrder2.setText("立即续费");
        itMealClick2.setBackgroundColor(getResources().getColor(R.color.xgt_btn_bg));

        mXGTPresenter = new XGTPresenter(this);

        mXGTPresenter.getXTGRenewInfo();//获取续费的数据
    }

    @OnClick({R.id.it_meal_click2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_meal_click2:
                Intent intent = new Intent(OpenXGTServiceActivity.this, OrderPlayActivity.class);
                intent.putExtra("tag", 3);
                intent.putExtra("xgtplay", "xgtplay");
                startActivity(intent);
                break;
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.ORDER_REFRESH)) {
            AppManager.getAppManager().killActivity(mWeakReference);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Override
    public void isUploadIdCardInfo(ApiResponse apiResponse) {

    }

    @Override
    public void isXGTInfo(ApiResponse<XGTEntity> apiResponse, String msg) {

    }

    @Override
    public void getXTGOpenAndRenewInfo(List<XGTOpenAndRenewEntity> openInfo, List<XGTOpenAndRenewEntity> renewInfo, String msg) {
        if (renewInfo != null && renewInfo.size() > 0) {
            //设置续费信息
            itMealGrade2.setText(renewInfo.get(0).getName());//训鸽通开通
            itMealContent2.setText(renewInfo.get(0).getBrief());//训鸽通用户，仅需380元一年（或5000鸽币兑换）
            itMealCost2.setText(renewInfo.get(0).getPrice() + "元/" + renewInfo.get(0).getUnitname() + "(" + renewInfo.get(0).getScores() + "鸽币)");//380 / 年
        }
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
}
