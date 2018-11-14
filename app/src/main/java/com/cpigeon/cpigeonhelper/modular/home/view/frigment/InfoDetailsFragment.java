package com.cpigeon.cpigeonhelper.modular.home.view.frigment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.menu.view.activity.AboutActivity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.MyOrderActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.UserInfoPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.ViewControlShare;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.activity.AccSecurityActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.activity.MyBalanceActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.activity.MyGeBiActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.activity.OrgInforActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.ServiceDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.utils.service.OrderService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 信息详情
 * Created by Administrator on 2017/12/18.
 */
public class InfoDetailsFragment extends BaseFragment implements UserInfoView {
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

    @BindView(R.id.tv_bszbpt)
    TextView tv_bszbpt;//比赛直播平台
    @BindView(R.id.ll_bszbpt)
    LinearLayout ll_bszbpt;

    private UserInfoPresenter mUserInfoPresenter;//控制层
    private OrderPresenter mOrderPresenter;//订单信息
    private ShareDialogFragment dialogFragment;

    private Intent intent;
    private String yue;//余额
    public static String mygebi = "0";//我的鸽币
    private boolean showTag = true;//第一次弹出到期提醒

    @OnClick({R.id.ll_jbxx, R.id.ll_wdye, R.id.ll_wddd, R.id.ll_wdgb, R.id.ll_zhaq, R.id.ll_gywm, R.id.ll_fxpy,
            R.id.ll_bszbpt, R.id.ll_help})
    public void onViewClicked(View view) {

        if (AntiShake.getInstance().check()) {
            return;
        }

        switch (view.getId()) {
            case R.id.ll_jbxx:
                //基本信息
                startActivity(new Intent(getActivity(), OrgInforActivity.class));
                break;
            case R.id.ll_wdye:
                //我的余额
                intent = new Intent(getActivity(), MyBalanceActivity.class);
                intent.putExtra("yue", yue);//余额
                startActivity(intent);
                break;
            case R.id.ll_wddd:
                //我的订单
                intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_wdgb:
                //我的鸽币
                intent = new Intent(getActivity(), MyGeBiActivity.class);
                intent.putExtra("mygebi", mygebi);
                startActivity(intent);
                break;
            case R.id.ll_zhaq:
                //账户安全
                startActivity(new Intent(getActivity(), AccSecurityActivity.class));
                break;
            case R.id.ll_gywm:
                //关于我们
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.ll_fxpy:
                //分享朋友
                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();
                mUserInfoPresenter.getShareCode();//分享创建分享码
                break;
            case R.id.ll_bszbpt:
                //直播比赛平台
                Intent intent = new Intent(getActivity(), OrderPlayActivity.class);
                intent.putExtra("sid", OrderService.BSZBPT_XF_SID);
                intent.putExtra("tag", 5);
                getActivity().startActivity(intent);
                return;

            case R.id.ll_help:
                //使用帮助
                Intent intent1 = new Intent(getActivity(), WebViewActivity.class);
                intent1.putExtra(WebViewActivity.EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.APP_HELP_URL);
                startActivity(intent1);
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
        try {
            if (myInfoApiResponse.getErrorCode() == 0) {
                yue = myInfoApiResponse.getData().getMoney();
                mygebi = myInfoApiResponse.getData().getGebi();
                tvWdye.setText("当前余额为" + myInfoApiResponse.getData().getMoney());//设置余额
                tvWdgb.setText("当前鸽币为" + myInfoApiResponse.getData().getGebi());//设置鸽币
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        //刷新
        if (strRefresh.equals(EventBusService.INFO_REFRESH)) {
            mUserInfoPresenter.getMyInfo();//获取我的信息
        }

        if (strRefresh.equals(EventBusService.ORDER_REFRESH)) {
            mOrderPresenter.getServesInfo();//获取各个服务到期时间
            mUserInfoPresenter.getMyInfo();//获取我的信息
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfoPresenter.getMyInfo();//获取我的信息
    }

    @Override
    public void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {
        try {
            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
            if (dialogFragment != null && dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing()) {
                dialogFragment.getDialog().dismiss();
                dialogFragment.dismiss();
            }

            if (myInfoApiResponse.getErrorCode() == 0) {
                if (dialogFragment != null) {
                    dialogFragment.setShareTitle("注册邀请");
                    dialogFragment.setShareContentString("分享中鸽助手，注册邀请得鸽币!!!");
                    dialogFragment.setShareContent(ApiConstants.BASE_URL + ApiConstants.SHARE_FRIND + myInfoApiResponse.getData().getYqm());
                    dialogFragment.setShareListener(ViewControlShare.getShareResultsDown(getActivity(), dialogFragment, ""));
                    dialogFragment.setShareType(1);
                    dialogFragment.show(getActivity().getFragmentManager(), "share");
                }
            } else if (myInfoApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity(), dialog -> {
                    dialog.dismiss();
                    //跳转到登录页
                    AppManager.getAppManager().startLogin(MyApplication.getContext());
                    RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                });
            } else {
                errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, getActivity());//弹出提示
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public int getLayoutResId() {
        return R.layout.activity_info_details;
    }

    @Override
    public void finishCreateView(Bundle state) {

        showTag = true;
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
        dialogFragment = new ShareDialogFragment();

        //个人不显示基本信息
        if (AssociationData.getUserAccountTypeString().equals("组织信息")) {
            ll_bszbpt.setVisibility(View.GONE);
            llJbxx.setVisibility(View.GONE);
        } else {
            ll_bszbpt.setVisibility(View.VISIBLE);
            llJbxx.setVisibility(View.VISIBLE);
        }

        mOrderPresenter = new OrderPresenter(new OrderViewImpl() {
            @Override
            public void getGetServesInfoData(ApiResponse<ServesInfoEntity> listApiResponse, String msg, Throwable mThrowable) {
                if (listApiResponse != null && listApiResponse.getData() != null) {
                    //比赛直播信息
                    if (listApiResponse.getData().getZbptdqsj() != null) {
                        tv_bszbpt.setText(String.valueOf(listApiResponse.getData().getZbptdqsj() + "到期"));
                    }

                    if (showTag) {//第一次弹出到期提示， 否则不弹出
                        showTag = false;
                        ServiceDialogUtil.initServiceTimelDialog(getActivity(), listApiResponse.getData());
                    }
                }
            }
        });

        mOrderPresenter.getServesInfo();//获取各个服务到期时间
        mUserInfoPresenter = new UserInfoPresenter(this);//初始化控制层
        mUserInfoPresenter.getMyInfo();//获取我的信息

    }
}
