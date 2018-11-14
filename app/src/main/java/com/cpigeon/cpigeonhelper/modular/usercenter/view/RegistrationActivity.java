package com.cpigeon.cpigeonhelper.modular.usercenter.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.RegisAndPasPresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.IRegAndPasView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.EncryptionTool;
import com.cpigeon.cpigeonhelper.utils.MyThreadUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 第一个注册页
 * Created by Administrator on 2017/9/6.
 */

public class RegistrationActivity extends ToolbarBaseActivity implements IRegAndPasView {
    @BindView(R.id.ac_et_phone)
    EditText acRegEtPhone;//用户输入电话
    @BindView(R.id.ac_ll1)
    LinearLayout acReLl1;
    @BindView(R.id.ac_et_validation)
    EditText acRegEtValidation;//输入验证码
    @BindView(R.id.ac_ingBtn_validation)
    TextView ingBtnValidation;
    @BindView(R.id.ac_determine)
    Button btnDetermine;

    private RegisAndPasPresenter presenter;//控制层

//    private boolean tag = false;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_reg_and_forget_pas;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("注册");
        setTopLeftButton(R.drawable.ic_back, RegistrationActivity.this::finish);

        presenter = new RegisAndPasPresenter(this);//初始化控制层
    }

//    private String TAG = "RegisAndPasPresenter";

    @OnClick({R.id.ac_ingBtn_validation, R.id.ac_determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_ingBtn_validation://注册获取验证码
                presenter.getValidation(acRegEtPhone, 1);
                break;
            case R.id.ac_determine://确定提交注册
//                Log.d(TAG, "服务器: " + checkCode);
//                Log.d(TAG, "输入的: " + EncryptionTool.MD5(acRegEtValidation.getText().toString()));

                if (acRegEtPhone.getText().toString().isEmpty() || acRegEtValidation.getText().toString().isEmpty()) {
                    CommonUitls.showToast(this, "手机号和验证码不能为空");
                } else if (!EncryptionTool.MD5(acRegEtValidation.getText().toString()).equals(checkCode)) {
                    CommonUitls.showToast(this, "请先获取，或验证码输入有误");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("userPhone", acRegEtPhone.getText().toString().trim());
                    bundle.putString("userVerification", acRegEtValidation.getText().toString().trim());
                    Intent intent = new Intent(this, RegistrationNewActivity.class);
                    intent.putExtras(bundle);
                    //开始跳转到第二个注册页
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void getErrorNews(String str) {
        CommonUitls.showToast(this, str); //提示用户错误消息
    }

    @Override
    public void getThrowable(Throwable throwable) {
        CommonUitls.exceptionHandling(this, throwable);//抛出获取数据过程中的异常
    }

    /**
     * 验证码发送成功回调
     */
    @Override
    public void validationSucceed() {
//        tag = true;
    }

    String checkCode;
    private Thread thread;

    @Override
    public void validationSucceed(ApiResponse<CheckCode> dataApiResponse) {
        if (dataApiResponse.getErrorCode() == 0 && dataApiResponse.getData() != null) {
            checkCode = dataApiResponse.getData().getCode();
            //获取验证码倒计时
            thread = new Thread(MyThreadUtil.getYzm(ingBtnValidation, this));
            thread.start();
            CommonUitls.showToast(this, "短信发送成功，请注意查收");
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
}
