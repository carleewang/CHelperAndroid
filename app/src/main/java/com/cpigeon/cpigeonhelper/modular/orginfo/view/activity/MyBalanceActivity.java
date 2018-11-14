package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.BalanceReChargeActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.RechargeDetailActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.UserInfoPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的--> 我的余额
 * Created by Administrator on 2017/12/19.
 */
public class MyBalanceActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_ye)
    TextView tvYe;
    private String yue = "-1";
    private UserInfoPresenter mUserInfoPresenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_balance;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTitle("我的余额");
        setTopLeftButton(R.drawable.ic_back, MyBalanceActivity.this::finish);

        yue = getIntent().getStringExtra("yue");
        if (yue != null && !yue.equals("-1")) {
            tvYe.setText(yue + "元");//设置余额
        }

        mUserInfoPresenter = new UserInfoPresenter(new UserInfoView() {
            @Override
            public void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg) {
                if (myInfoApiResponse.getErrorCode() == 0) {
                    yue = myInfoApiResponse.getData().getMoney();
                    tvYe.setText(myInfoApiResponse.getData().getMoney() + "元");//设置余额
                }else if (myInfoApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, MyBalanceActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                }
            }

            @Override
            public void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable) {

            }

            @Override
            public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {

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
            public void getErrorNews(String str) {

            }

            @Override
            public void getThrowable(Throwable throwable) {

            }
        });
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        //刷新
        if (strRefresh.equals(EventBusService.INFO_REFRESH)) {
            mUserInfoPresenter.getMyInfo();//获取我的信息
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @OnClick({R.id.btn_cz, R.id.btn_mx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cz:
                //充值
                startActivity(new Intent(this, BalanceReChargeActivity.class));
                break;
            case R.id.btn_mx:
                //明细
                startActivity(new Intent(this, RechargeDetailActivity.class));
                break;
        }
    }
}
