package com.cpigeon.cpigeonhelper.modular.orginfo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.OrgInforViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 基本信息-->二级域名-->修改二级域名页面
 * Created by Administrator on 2017/9/19.
 */

public class AlterDomainActivity extends ToolbarBaseActivity {
    @BindView(R.id.tv_before_momain_name)
    TextView tvBeforeMomainName;//原域名
    @BindView(R.id.et_change_domain_name)
    EditText etChangeDomainName;//变更域名
    @BindView(R.id.et_domain_change_cause)
    EditText etDomainChangeCause;//变更原因
    @BindView(R.id.domain_org_submit)
    Button domainOrgSubmit;//提交按钮

    private OrgInforPresenter presenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_alter_domain;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("变更二级域名申请");
        setTopLeftButton(R.drawable.ic_back, AlterDomainActivity.this::finish);

        tvBeforeMomainName.setText(getIntent().getStringExtra("orgDomainNo"));//设置原域名

        presenter = new OrgInforPresenter(new OrgInforViewImpl() {

            /**
             * 申请成功后回调
             */
            @Override
            public void validationSucceed() {
                //发布事件（刷新数据）
                EventBus.getDefault().post(EventBusService.INFO_ORG_REFRESH);

                CommonUitls.showSweetDialog(AlterDomainActivity.this, "申请成功，请等待处理结果", dialog -> {
                    dialog.dismiss();
                    AppManager.getAppManager().killActivity(mWeakReference);
                });
            }

            @Override
            public void getErrorNews(String str) {
                AlterDomainActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                AlterDomainActivity.this.getThrowable(throwable);
            }
        });//初始化控制层
    }


    @OnClick({R.id.domain_org_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.domain_org_submit://点击提交
                presenter.changeDomain(etChangeDomainName, etDomainChangeCause);
                break;
        }
    }


}
