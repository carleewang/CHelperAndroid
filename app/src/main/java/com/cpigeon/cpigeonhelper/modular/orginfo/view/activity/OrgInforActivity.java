package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforView;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 组织信息页面
 * Created by Administrator on 2017/9/14.
 */

public class OrgInforActivity extends ToolbarBaseActivity implements OrgInforView {
    @BindView(R.id.tv_orinfo_names)
    TextView tv_orinfo_names;//协会名称
    @BindView(R.id.tx_org_name)
    TextView txOrgName;//协会名称
    @BindView(R.id.alterOrgName)
    LinearLayout alterOrgName;//协会名称Item
    @BindView(R.id.tx_sld)
    TextView txSld;//二级域名
    @BindView(R.id.alterSLD)
    LinearLayout alterSLD;//二级域名item
    @BindView(R.id.tx_contact_name)
    TextView txContactName;//联系人
    @BindView(R.id.tx_contact_phone)
    TextView txContactPhone;//联系电话
    @BindView(R.id.tx_contact_email)
    TextView txContactEmail;//邮箱
    @BindView(R.id.tx_contact_address)
    TextView txContactAddress;//详细地址
    @BindView(R.id.tx_contact_time_regis)
    TextView txContactTimeRegis;//注册时间
    //    @BindView(R.id.tx_contact_time_setup)
//    TextView txContactTimeSetup;//建立时间
    @BindView(R.id.tx_contact_time_dueto)
    TextView txContactTimeDueto;//到期时间

    private OrgInforPresenter presenter;//控制层

    private Intent intent;
    private Bundle bundle;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_org_info;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者
        if (AssociationData.getUserType().equals("gongpeng")) {
            setTitle("公棚信息");
            tv_orinfo_names.setText("公棚名称");
        } else if (AssociationData.getUserType().equals("xiehui")) {
            setTitle("协会信息");
            tv_orinfo_names.setText("协会名称");
        } else {
            setTitle("组织信息");
            tv_orinfo_names.setText("组织名称");
        }

        setTopLeftButton(R.drawable.ic_back, OrgInforActivity.this::finish);
        setTopRightButton("编辑", () -> {
            startActivitys();//开始跳转页面
        });
        presenter = new OrgInforPresenter(this);//初始化控制层
        presenter.getOrgInforData();//获取组织数据
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        Log.d(TAG, "订阅返回");
        if (strRefresh.equals(EventBusService.INFO_ORG_REFRESH)) {
            presenter.getOrgInforData();//获取组织数据
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    /**
     * 开始跳转到修改个人信息页面
     */
    private void startActivitys() {
        intent = new Intent(OrgInforActivity.this, UserInforActivity.class);
        bundle = new Bundle();
        bundle.putString("txContactName", txContactName.getText().toString());//联系人
        bundle.putString("txContactPhone", txContactPhone.getText().toString());//联系电话
        bundle.putString("txContactEmail", txContactEmail.getText().toString());//邮箱
        bundle.putString("txContactAddress", txContactAddress.getText().toString());//详细地址
//        bundle.putString("txContactTimeSetup", txContactTimeSetup.getText().toString());//建立时间

        intent.putExtras(bundle);
        OrgInforActivity.this.startActivityForResult(intent, 0x0036);
    }

    /**
     * 修改成功返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getOrgInforData();//重新获取组织数据
    }

    /**
     * 数据获取成功
     */
    @Override
    public void validationSucceed(OrgInfo data) {
        try {
            txOrgName.setText(data.getName());//协会名称
            txSld.setText(data.getDomain());//二级域名
            txContactName.setText(data.getContacts());//联系人
            txContactPhone.setText(data.getPhone());//联系电话
            txContactEmail.setText(data.getEmail());//邮箱
            txContactAddress.setText(data.getAddr());//详细地址
            txContactTimeRegis.setText(data.getRegistTime());//注册时间
//        txContactTimeSetup.setText(data.getSetupTime());//建立时间
            txContactTimeDueto.setText(data.getExpireTime());//到期时间
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validationSucceed() {

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

    @OnClick({R.id.alterOrgName, R.id.alterSLD})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }
        switch (view.getId()) {
            case R.id.alterOrgName:
                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();
                //atype：申请类型 【orgname orgdomain】【默认值：orgname】
                presenter.acquireState("orgname");//检查组织名称修改状态
                break;
            case R.id.alterSLD:
                if (!mLoadDataDialog.isShowing()) mLoadDataDialog.show();
                presenter.acquireState("orgdomain");//检查二级域名修改状态
                break;
        }
    }

    /**
     * 申请中回调
     */
    @Override
    public void checkStateFor() {
        if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "申请中，请等待处理结果", this);
    }

    /**
     * 没有申请状态回调
     * //跳转到修改组织信息
     */
    @Override
    public void checkStateNameNo() {
        try {
            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

            //跳转到修改组织信息
            intent = new Intent(OrgInforActivity.this, AlterOrgActivity.class);
            bundle = new Bundle();
            bundle.putString("orgName", txOrgName.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 没有申请二级域名状态回调
     */
    @Override
    public void checkStateDomainNo() {
        try {
            if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

            //跳转到修改组织信息
            intent = new Intent(OrgInforActivity.this, AlterDomainActivity.class);
            bundle = new Bundle();
            bundle.putString("orgDomainNo", txSld.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserTypeSuccend(ApiResponse<UserType> userTypeApiResponse, String msg, Throwable mThrowable) {

    }
}
