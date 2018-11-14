package com.cpigeon.cpigeonhelper.modular.home.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ServiceType;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.GYTHomeActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.WdhyHomeActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OpenGytActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGTApplyTrialActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2.GpsgHomeActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.service.OrderService;

/**
 * Created by Administrator on 2018/3/16.
 */

public class HomePresenter2 {


    //是否开通鸽运通
    public static void isOpenGYTs(UserType mUserType, Activity mContent) {
//        Log.d("kqbs", "isOpenGYTs: 跳转鸽运通   是否开通鸽运通：" + mUserType.getGytstatus() + "   用户类型：" + mUserType.getUsertype() + "   " + mUserType.isAuthuser());

        if (mUserType.getGytstatus() == 0 || mUserType.isAuthuser()) {//开通，或者授权用户

            RealmUtils.preservationServiceType(new ServiceType("geyuntong"));
            UserBean userBean = AssociationData.getUserBean();

            UserBean bean = new UserBean();
            bean.setUid(userBean.getUid());
            bean.setUsername(userBean.getUsername());
            bean.setToken(userBean.getToken());
            bean.setNickname(userBean.getNickname());
            bean.setSign(userBean.getSign());
            bean.setHeadimgurl(userBean.getHeadimgurl());

            if (mUserType.getUsertype().equals("cphelper.gongpeng") || mUserType.getUsertype().equals("cphelper.xiehui")) {
                bean.setAccountType(mUserType.getUsertype());
            } else if (mUserType.isAuthuser()) {
                bean.setAccountType("cphelper.authuser");
            }

            bean.setPassword(userBean.getPassword());
            bean.setType(userBean.getType());
            bean.setAtype(userBean.getAtype());

            if (RealmUtils.getInstance().existUserInfo()) {//判断是否存在用户数据
//                Log.d(TAG, "isOpenGYTs: 存在用户数据删除");
                RealmUtils.getInstance().deleteUserInfo();//删除用户数据
            }

            RealmUtils.getInstance().insertUserInfo(bean);//保存用户类型

            mContent.startActivity(new Intent(mContent, GYTHomeActivity.class));//跳转到鸽运通列表页面
        } else if (mUserType.getUsertype().equals("cphelper.xiehui") || mUserType.getUsertype().equals("cphelper.gongpeng") && mUserType.getGytstatus() == 10000) {
            //公棚或协会用户  没有开通鸽运通
            mContent.startActivity(new Intent(mContent, OpenGytActivity.class));//跳转到开通鸽运通页面
        } else {
            CommonUitls.showSweetDialog(mContent, "您不是协会（公棚），或授权用户，不能进入鸽运通");
        }
    }


    //是否开通赛鸽通
    public static void isOpenSGT(ApiResponse<UserType> userTypeApiResponse, Activity mContent) {
        switch (userTypeApiResponse.getData().getSgtstatus()) {
            case 0:
                //未开通赛鸽通
                CommonUitls.showSweetDialog(mContent, userTypeApiResponse.getData().getSgtmsg(), dialog -> {

                    dialog.dismiss();

                    Intent intent = new Intent(mContent, SGTApplyTrialActivity.class);
                    mContent.startActivity(intent);

                });
                break;
            case 1:
                mContent.startActivity(new Intent(mContent, GpsgHomeActivity.class));
                break;
            default:
        }
    }

    //停止刷新
    public static void stopSRL(SwipeRefreshLayout mSwipeRefreshLayout) {

        try {
            mSwipeRefreshLayout.setRefreshing(false);//设置刷新
            mSwipeRefreshLayout.setEnabled(true);//设置启用
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //是否开通我的会员
    public static void isOpenWdhy(ApiResponse<UserType> userTypeApiResponse, Activity mContent) {

        //0为未开通；1为正常；2为已到期
        switch (userTypeApiResponse.getData().getHyglxt()) {
            case 0:
                CommonUitls.showSweetDialog(mContent, "您尚未开通我的会员服务", dialog -> {
                    dialog.dismiss();

                    Intent intent1 = new Intent(mContent, OrderPlayActivity.class);
                    intent1.putExtra("sid", OrderService.WDHY_KT_SID);
                    intent1.putExtra("tag", 5);
                    mContent.startActivity(intent1);
                });
                break;
            case 1:
                Intent intent2 = new Intent(mContent, WdhyHomeActivity.class);
                mContent.startActivity(intent2);
                break;
            default:
                CommonUitls.showSweetDialog(mContent, "我的会员服务已到期,请及时续费", dialog -> {
                    dialog.dismiss();
                    Intent intent3 = new Intent(mContent, OrderPlayActivity.class);
                    intent3.putExtra("sid", OrderService.WDHY_XF_SID);
                    intent3.putExtra("tag", 5);
                    mContent.startActivity(intent3);
                });
        }
    }
}
