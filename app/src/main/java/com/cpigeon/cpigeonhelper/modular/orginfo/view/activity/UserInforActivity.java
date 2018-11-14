package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户信息页面
 * Created by Administrator on 2017/9/15.
 */

public class UserInforActivity extends ToolbarBaseActivity implements OrgInforView {

    @BindView(R.id.tx_userinfo_name)
    EditText etUserinfoName;//联系人
    @BindView(R.id.tx_userphone)
    EditText etUserphone;//联系电话
    @BindView(R.id.tx_useremail)
    EditText etUseremail;//邮箱
    @BindView(R.id.tx_useraddress)
    EditText etUseraddress;//详细地址
    //    @BindView(R.id.tx_usersetup)
//    TextView etUsersetup;//建立时间
    @BindView(R.id.userinfo_sure)
    Button userinfoSure;

    private OrgInforPresenter presenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("用户信息");
        setTopLeftButton(R.drawable.ic_back, UserInforActivity.this::finish);

        initTextContent();//初始化文本内容

        presenter = new OrgInforPresenter(this);//初始化控制层
    }

    /**
     * 初始化文本内容
     */
    private void initTextContent() {
        etUserinfoName.setText(getIntent().getStringExtra("txContactName"));//初始化联系人内容
        etUserphone.setText(getIntent().getStringExtra("txContactPhone"));//初始化联系人内容
        etUseremail.setText(getIntent().getStringExtra("txContactEmail"));//初始化联系人内容
        etUseraddress.setText(getIntent().getStringExtra("txContactAddress"));//初始化联系人内容
//        etUsersetup.setText(getIntent().getStringExtra("txContactTimeSetup"));//初始化联系人内容
    }


    @OnClick({R.id.userinfo_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userinfo_sure:
                mLoadDataDialog.show();
                presenter.amendUserInforData(etUserinfoName, etUserphone, etUseremail, etUseraddress);
                break;
        }
    }

    /**
     * 提交用户信息成功后回调
     *
     * @param data
     */
    @Override
    public void validationSucceed(OrgInfo data) {
        //发布事件（刷新数据）
        EventBus.getDefault().post(EventBusService.INFO_ORG_REFRESH);

        mLoadDataDialog.dismiss();
        CommonUitls.showSweetDialog2(this, "修改信息成功", dialog -> {
            dialog.dismiss();
            AppManager.getAppManager().killActivity(mWeakReference);
        });
    }

    @Override
    public void validationSucceed() {

    }

    /**
     * 申请中回调（当前页面没有使用到）
     */
    @Override
    public void checkStateFor() {

    }

    /**
     * 没有申请组织名称状态回调（当前页面没有使用到）
     */
    @Override
    public void checkStateNameNo() {

    }

    /**
     * 没有申请二级域名状态回调（当前页面没有使用到）
     */
    @Override
    public void checkStateDomainNo() {

    }

    @Override
    public void getUserTypeSuccend(ApiResponse<UserType> userTypeApiResponse, String msg, Throwable mThrowable) {

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
