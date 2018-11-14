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
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTService;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.XGTPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.XGTView;
import com.cpigeon.cpigeonhelper.modular.home.view.frigment.InfoDetailsFragment;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.SignActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OpenGytActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.RenewalUpgradeActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.UserInfoPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.ViewControlShare;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 鸽币获取  鸽币兑换
 * Created by Administrator on 2017/12/20.
 */
public class GebiAcquireActivity extends ToolbarBaseActivity implements XGTView, UserInfoView {


    @BindView(R.id.tv_my_gebi)
    TextView myGebi;//我的鸽币数量

    @BindView(R.id.ll_qd)
    LinearLayout ll_qd;//签到
    @BindView(R.id.ll_fxxz)
    LinearLayout ll_fxxz;//分享

    @BindView(R.id.ll_gytfw)
    LinearLayout ll_gytfw;//鸽运通服务
    @BindView(R.id.ll_xgtfw)
    LinearLayout ll_xgtfw;//训鸽通服务

    private XGTPresenter mXGTPresenter;//训鸽通控制层

    private UserInfoPresenter mUserInfoPresenter;//分享下载控制层
    private ShareDialogFragment dialogFragment;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gb_acquire;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        String bgType = getIntent().getStringExtra("bgType");
        if (bgType.equals("gbdh")) {
            //鸽币兑换
            setTitle("鸽币兑换");
            ll_qd.setVisibility(View.GONE);
            ll_fxxz.setVisibility(View.GONE);
        } else if (bgType.equals("gbhq")) {
            setTitle("鸽币获取");
            ll_gytfw.setVisibility(View.GONE);
            ll_xgtfw.setVisibility(View.GONE);
        }

        setTopLeftButton(R.drawable.ic_back, GebiAcquireActivity.this::finish);
//        myGebi.setText(InfoDetailsActivity.mygebi);
        myGebi.setText(InfoDetailsFragment.mygebi);

        mXGTPresenter = new XGTPresenter(this);//训鸽通控制层

        mUserInfoPresenter = new UserInfoPresenter(this);//初始化控制层

        dialogFragment = new ShareDialogFragment();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mUserInfoPresenter.getMyInfo();//获取我的信息
    }

    /**
     * 账户信息 余额 鸽币
     *
     * @param myInfoApiResponse
     * @param msg
     */
    @Override
    public void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg) {
        if (myInfoApiResponse.getErrorCode() == 0) {
            myGebi.setText("" + myInfoApiResponse.getData().getGebi());
        }
    }

    @OnClick({R.id.ll_qd, R.id.ll_fxxz, R.id.ll_gytfw, R.id.ll_xgtfw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_qd:
                //签到
                startActivity(new Intent(GebiAcquireActivity.this, SignActivity.class));

//            Intent intent = new Intent(HomeNewActivity.this, WebViewActivity.class);
//            intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.APP_SIGN_URL + AssociationData.getUserId());
//            startActivity(intent);

                break;
            case R.id.ll_fxxz:
                //分享下载得鸽币

                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();

                mUserInfoPresenter.getShareCode();//分享创建分享码
                break;
            case R.id.ll_gytfw:
                //鸽运通服务
                startGYTService();
                break;
            case R.id.ll_xgtfw:

                if (AssociationData.getUserAccountTypeStrings().equals("gongpeng")) {
                    CommonUitls.showSweetDialog1(this, "公棚用户暂无训鸽通服务", dialog -> {
                        dialog.dismiss();
                    });
                } else {
                    //训鸽通服务
                    mXGTPresenter.getXGTInfo();//获取训鸽通信息
                }

                break;
        }
    }

    /**
     * 鸽运通服务
     */
    private void startGYTService() {
        if (AssociationData.getUserAccountTypeString().equals("组织信息")) {
            CommonUitls.showToast(this, "您还不是公棚（协会）用户，无法兑换鸽运通服务");
            return;
        }
        if (RealmUtils.getInstance().existGYTInfo()) {
            GYTService gytService = RealmUtils.getInstance().queryGTYInfo().last();
            setGYTData(gytService);//获取鸽运通数据
            if (gytService.getGrade().equals("-1")) {
                Log.d(TAG, "getGrade: 为空");
                if (gytService.getUsefulTime() != null) {
                    //开通用户
                    //跳转到续费，充值 鸽运通服务页面
                    Intent intent = new Intent(new Intent(GebiAcquireActivity.this, RenewalUpgradeActivity.class));
                    intent.putExtra("vipGrade", vipGrade);
                    intent.putExtra("endTime", gytService.getExpireTime());
                    startActivity(intent);
                } else {
                    //跳转到开通鸽运通服务页面
                    startActivity(new Intent(GebiAcquireActivity.this, OpenGytActivity.class));
                }
            } else {
                //开通用户
                //跳转到续费，充值 鸽运通服务页面
                Intent intent = new Intent(new Intent(GebiAcquireActivity.this, RenewalUpgradeActivity.class));
                intent.putExtra("vipGrade", vipGrade);
                intent.putExtra("endTime", gytService.getExpireTime());
                startActivity(intent);
            }
        }
    }


    private int vipGrade;//1：普通用户   2：vip用户 3：svip用户
    private String endTime;//到期时间

    /**
     * 获取鸽运通数据
     */
    public void setGYTData(GYTService gytService) {
        endTime = gytService.getExpireTime();//到期时间
        //获取用户等级
        switch (gytService.getGrade()) {
            case "-1"://普通用户
                if (gytService.getUsefulTime() != null) {
                    //普通用户
                    vipGrade = 1;
                } else {
                    //未开通鸽运通
                    vipGrade = -1;
                }
                break;
            case "vip"://vip用户
                vipGrade = 2;
                break;
            case "svip"://svip用户
                vipGrade = 3;
                break;
        }
    }


    @Override
    public void isUploadIdCardInfo(ApiResponse apiResponse) {

    }

    /**
     * 开通训鸽通信息
     *
     * @param msg
     */
    @Override
    public void isXGTInfo(ApiResponse<XGTEntity> apiResponse, String msg) {
        if (apiResponse.getErrorCode() == 0) {
            XGTPresenter.initXTGView(apiResponse.getData(), this, ll_xgtfw, 2);//初始化训鸽通续费，申请开通
        } else {
            XGTPresenter.startShiyong(apiResponse.getData(), this, ll_xgtfw, 2);
        }
    }

    @Override
    public void getXTGOpenAndRenewInfo(List<XGTOpenAndRenewEntity> openInfo, List<XGTOpenAndRenewEntity> renewInfo, String msg) {

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
    public void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {
        Log.d(TAG, "getShareCodeData: 创建邀请码结果cod-->" + myInfoApiResponse.getErrorCode() + "   msg--->" + myInfoApiResponse.getMsg());

        if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

        if (dialogFragment != null && dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing()) {
            dialogFragment.dismiss();
        }
        if (myInfoApiResponse.getErrorCode() == 0) {
            if (dialogFragment != null) {
                dialogFragment.setShareContent(ApiConstants.BASE_URL + ApiConstants.SHARE_FRIND + myInfoApiResponse.getData().getYqm());
                dialogFragment.setShareListener(ViewControlShare.getShareResultsDown(this, dialogFragment, null));
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
}
