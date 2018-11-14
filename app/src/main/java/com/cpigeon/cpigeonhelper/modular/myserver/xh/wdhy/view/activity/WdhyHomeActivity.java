package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HYGLInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity.FootAdminHomeActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hfjl.HfjlHomeActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.HyglListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hyzc.HyzcHomeActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity.TubbyRingAdminHomeActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.utils.service.OrderService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的会员 会员管理主页
 * Created by Administrator on 2018/3/5.
 */

public class WdhyHomeActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_xtxf)
    TextView tv_xtxf;//系统续费

    private MemberPresenter mMemberPresenter;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hygl_home;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, WdhyHomeActivity.this::finish);
        setTitle("我的会员");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getHyglInfoData(ApiResponse<HYGLInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (listApiResponse.getErrorCode() == 0) {
                        tv_xtxf.setText(String.valueOf("到期时间：" + listApiResponse.getData().getDqsj()));
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, WdhyHomeActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    WdhyHomeActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mMemberPresenter.getXHHYGL_UserInfo();//获取会员管理信息
    }

    private Intent intent;

    @OnClick({R.id.ll_hyzc, R.id.ll_hfjl, R.id.ll_hygl, R.id.ll_xtxf, R.id.ll_zhgl, R.id.ll_tbhgl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_hyzc:
                //会员注册
                intent = new Intent(this, HyzcHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_hfjl:
                //会员缴纳
                intent = new Intent(this, HfjlHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_hygl:
                //会员管理
                intent = new Intent(this, HyglListActivity.class);
                intent.putExtra("type","ordinary");
                startActivity(intent);
                break;
            case R.id.ll_xtxf:
                //系统续费
                intent = new Intent(this, OrderPlayActivity.class);
                intent.putExtra("sid", OrderService.WDHY_XF_SID);
                intent.putExtra("tag", 5);
                startActivity(intent);
                break;
            case R.id.ll_zhgl:
                //足环管理
                intent = new Intent(WdhyHomeActivity.this, FootAdminHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_tbhgl:
                //特比环管理
                intent = new Intent(WdhyHomeActivity.this, TubbyRingAdminHomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.ORDER_REFRESH)) {
            mMemberPresenter.getXHHYGL_UserInfo();//获取会员管理信息
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
