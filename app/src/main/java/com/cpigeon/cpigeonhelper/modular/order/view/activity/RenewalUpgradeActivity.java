package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTHomePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTHomeViewImpl;
import com.cpigeon.cpigeonhelper.modular.order.view.adapter.RenUpgPagerAdapter;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import butterknife.BindView;

/**
 * 续费升级页面
 * Created by Administrator on 2017/10/26.
 */

public class RenewalUpgradeActivity extends ToolbarBaseActivity {

    private String TAG = "RenewalUpgradeActivity";
    @BindView(R.id.org_name)
    TextView orgName;//组织名称
    @BindView(R.id.org_grade)
    ImageView orgGrade;//组织vip等级
    @BindView(R.id.org_come_due_time)
    TextView orgComeDueTime;//到期时间
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs_reup)
    SlidingTabLayout mSlidingTab;//导航条

    private OrgInforPresenter presenter;
    private GYTHomePresenter mGYTHomePresenter;//鸽运通主页控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_renewal_upgrade;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTitle("升级续费");
        setTopLeftButton(R.drawable.ic_back, RenewalUpgradeActivity.this::finish);
        initHead();

        initViewPager();
    }

    /**
     * viewPager预加载
     */
    private void initViewPager() {

        RenUpgPagerAdapter mAdapter = new RenUpgPagerAdapter(getSupportFragmentManager(),
                getApplicationContext());
        mViewPager.setOffscreenPageLimit(5);//预加载
        mViewPager.setAdapter(mAdapter);//设置关联的适配器
        mSlidingTab.setViewPager(mViewPager);//设置导航条关联的viewpager
        mViewPager.setCurrentItem(0);
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

    /**
     * 初始化头部控件数据
     */
    private void initHead() {

        if (this.isDestroyed()) {
            return;
        }

        //获取鸽运通服务数据
        mGYTHomePresenter = new GYTHomePresenter(new GYTHomeViewImpl() {

            @Override
            public void getServiceData(ApiResponse<GYTHomeEntity> dataApiResponse, String msg) {

                try {
                    if (dataApiResponse != null && dataApiResponse.getErrorCode() == 0 && dataApiResponse.getData() != null) {
                        //设置vip等级图片
                        switch (dataApiResponse.getData().getGrade()) {
                            case "vip"://vip用户
                                orgGrade.setImageResource(R.mipmap.grade_vip2x);
                                break;
                            case "svip"://svip用户
                                orgGrade.setImageResource(R.mipmap.grade_svip2x);
                                break;
                            default:
                                orgGrade.setImageResource(R.mipmap.grade_common);
                        }

                        orgComeDueTime.setText(DateUtils.compareDate(dataApiResponse.getData().getExpireTime(), DateUtils.sdf.format(new Date())));//设置到期时间
                        EventBus.getDefault().post("剩余：" + orgComeDueTime.getText().toString());
                    } else if (dataApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, RenewalUpgradeActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mGYTHomePresenter.gytServerDatas(AssociationData.getUserAccountTypeStrings(), "geyuntong");

        presenter = new OrgInforPresenter(new OrgInforViewImpl() {

            /**
             * 获取组织信息数据成功后回调
             */
            @Override
            public void validationSucceed(OrgInfo data) {
                orgName.setText(data.getName());//设置协会名称
                //发布事件（通知当前页的续费的fragment剩余时间）
                EventBus.getDefault().post("剩余：" + orgComeDueTime.getText().toString());
            }
        });
        presenter.getOrgInforData();// 获取组织信息数据
    }
}
