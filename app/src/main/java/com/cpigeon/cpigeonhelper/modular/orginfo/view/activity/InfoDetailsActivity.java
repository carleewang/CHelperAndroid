package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.AboutActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.MyOrderActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.UserInfoPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.ViewControlShare;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 信息详情 -->舍弃
 * Created by Administrator on 2017/12/18.
 */
public class InfoDetailsActivity extends ToolbarBaseActivity implements UserInfoView {
    @BindView(R.id.ll_jbxx)
    LinearLayout llJbxx;
    @BindView(R.id.tv_wdye)
    TextView tvWdye;
    @BindView(R.id.ll_wdye)
    LinearLayout llWdye;
    @BindView(R.id.ll_wddd)
    LinearLayout llWddd;
    @BindView(R.id.tv_wdgb)
    TextView tvWdgb;
    @BindView(R.id.ll_wdgb)
    LinearLayout llWdgb;
    @BindView(R.id.ll_zhaq)
    LinearLayout llZhaq;
    @BindView(R.id.ll_gywm)
    LinearLayout llGywm;
    @BindView(R.id.ll_fxpy)
    LinearLayout llFxpy;

    private UserInfoPresenter mUserInfoPresenter;//控制层
    private ShareDialogFragment dialogFragment;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_info_details;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTopLeftButton(R.drawable.ic_back, InfoDetailsActivity.this::finish);
        //个人不显示基本信息
        if (AssociationData.getUserAccountTypeString().equals("组织信息")) {
            llJbxx.setVisibility(View.GONE);
            setTitle(AssociationData.getName());
        } else {
            setTitle(RealmUtils.getInstance().queryOrgInfoName().getName());
            llJbxx.setVisibility(View.VISIBLE);
        }

//        setTopRightButton(R.mipmap.ic_qiandao, () -> {
        setTopRightButton("签到", () -> {
            //签到
            Intent intent = new Intent(InfoDetailsActivity.this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.APP_SIGN_URL + AssociationData.getUserId());
            startActivity(intent);
        });

        mUserInfoPresenter = new UserInfoPresenter(this);//初始化控制层
        mUserInfoPresenter.getMyInfo();//获取我的信息

        dialogFragment = new ShareDialogFragment();
    }

    private Intent intent;
    private String yue;//余额
    public static String mygebi = "0";//我的鸽币

    @OnClick({R.id.ll_jbxx, R.id.ll_wdye, R.id.ll_wddd, R.id.ll_wdgb, R.id.ll_zhaq, R.id.ll_gywm, R.id.ll_fxpy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_jbxx:
                //基本信息
                startActivity(new Intent(this, OrgInforActivity.class));
                break;
            case R.id.ll_wdye:
                //我的余额
                intent = new Intent(this, MyBalanceActivity.class);
                intent.putExtra("yue", yue);//余额
                startActivity(intent);
                break;
            case R.id.ll_wddd:
                //我的订单
                intent = new Intent(this, MyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_wdgb:
                //我的鸽币
                intent = new Intent(this, MyGeBiActivity.class);
                intent.putExtra("mygebi", mygebi);
                startActivity(intent);
                break;
            case R.id.ll_zhaq:
                //账户安全
                startActivity(new Intent(this, AccSecurityActivity.class));
                break;
            case R.id.ll_gywm:
                //关于我们
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.ll_fxpy:
                //分享朋友
                mUserInfoPresenter.getShareCode();//分享创建分享码
                break;
        }
    }

    /**
     * 我的信息获取结果
     *
     * @param myInfoApiResponse
     * @param msg
     */
    @Override
    public void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg) {
        if (myInfoApiResponse.getErrorCode() == 0) {
            yue = myInfoApiResponse.getData().getMoney();
            mygebi = myInfoApiResponse.getData().getGebi();
            tvWdye.setText("当前余额为" + myInfoApiResponse.getData().getMoney());//设置余额
            tvWdgb.setText("当前鸽币为" + myInfoApiResponse.getData().getGebi());//设置鸽币
        }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        mUserInfoPresenter.getMyInfo();//获取我的信息
    }

    @Override
    public void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {
        Log.d(TAG, "getShareCodeData: 创建邀请码结果cod-->" + myInfoApiResponse.getErrorCode() + "   msg--->" + myInfoApiResponse.getMsg());
        if (dialogFragment != null && dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing()) {
            dialogFragment.dismiss();
        }

        if (myInfoApiResponse.getErrorCode() == 0) {
            if (dialogFragment != null) {
                dialogFragment.setShareContent(ApiConstants.BASE_URL + ApiConstants.SHARE_FRIND + myInfoApiResponse.getData().getYqm());
                dialogFragment.setShareListener(ViewControlShare.getShareResultsDown(this, dialogFragment, ""));
                dialogFragment.setShareType(1);
                dialogFragment.show(getFragmentManager(), "share");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
