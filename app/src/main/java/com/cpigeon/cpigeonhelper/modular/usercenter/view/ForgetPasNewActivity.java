package com.cpigeon.cpigeonhelper.modular.usercenter.view;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.RegisAndPasPresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.IRegAndPasView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/9.
 * 第二个忘记密码页
 */

public class ForgetPasNewActivity extends ToolbarBaseActivity implements IRegAndPasView {


    @BindView(R.id.ac_newRegAndPas_et_pas)
    EditText etPas;
    @BindView(R.id.ac_newRegAndPas_et_againPas)
    EditText etAgainPas;
    private String userPhone;//用户手机
    private String userVerification;//验证码

    private RegisAndPasPresenter presenter;


    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_reg_and_pas;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("修改登录密码");
        setTopLeftButton(R.drawable.ic_back, ForgetPasNewActivity.this::finish);

        setTopRightButton("完成", () -> {
            presenter.forgetPas(userPhone//电话号码
                    , userVerification//验证码
                    , etPas.getText().toString()//输入密码
                    , etAgainPas.getText().toString());//再次输入密码
        });

        userPhone = getIntent().getStringExtra("userPhonePas");
        userVerification = getIntent().getStringExtra("userVerificationPas");
        etPas.setInputType(InputType.TYPE_CLASS_TEXT); //输入类型为普通文本
        etAgainPas.setInputType(InputType.TYPE_CLASS_TEXT); //输入类型为普通文本
        presenter = new RegisAndPasPresenter(this);
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
        CommonUitls.showToast(this, str); //提示用户错误消息
    }

    @Override
    public void getThrowable(Throwable throwable) {
        CommonUitls.exceptionHandling(this, throwable);
    }

    @Override
    public void validationSucceed() {
        SweetAlertDialog sweetAlertDialog = CommonUitls.showSweetDialog1(this, "修改登录密码成功，请重新登录", dialog -> {
            dialog.dismiss();
            AppManager.getAppManager().startLogin(this);
        });
        sweetAlertDialog.setCancelable(false);
    }

    @Override
    public void validationSucceed(ApiResponse<CheckCode> dataApiResponse) {

    }
}
