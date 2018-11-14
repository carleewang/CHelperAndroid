package com.cpigeon.cpigeonhelper.modular.usercenter.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.RegisAndPasPresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.IRegAndPasView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import butterknife.BindView;

/**
 * 第二个注册页
 * Created by Administrator on 2017/9/7.
 */

public class RegistrationNewActivity extends ToolbarBaseActivity implements IRegAndPasView {

    @BindView(R.id.ac_newRegAndPas_et_pas)
    EditText etPas;
    @BindView(R.id.ac_newRegAndPas_et_againPas)
    EditText etAgainPas;
    @BindView(R.id.ll_yqm)
    LinearLayout llYqm;//邀请码布局
    @BindView(R.id.et_yqm)
    EditText etYqm;//输入邀请码
    //    @BindView(R.id.ac_newRegAndPas_determine)
//    Button acNewRegDetermine;
    private String userPhone;//用户手机
    private String userVerification;//验证码

    private RegisAndPasPresenter presenter;

    @Override
    protected void swipeBack() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_reg_and_pas;
    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("注册");
        setTopLeftButton(R.drawable.ic_back, RegistrationNewActivity.this::finish);

        llYqm.setVisibility(View.VISIBLE);

        setTopRightButton("完成", () -> {
            //点击确定开始注册
            presenter.userRegistration(userPhone//电话号码
                    , userVerification//验证码
                    , etPas.getText().toString()//输入密码
                    , etAgainPas.getText().toString(), etYqm);//再次输入密码
        });

        userPhone = getIntent().getStringExtra("userPhone");
        userVerification = getIntent().getStringExtra("userVerification");

        presenter = new RegisAndPasPresenter(this);

        etYqm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etYqm.getText().toString().equals("")) {
                    showPopupMenu(etYqm);
                }
            }
        });

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

    /**
     * 验证成功回调
     */
    @Override
    public void validationSucceed() {
        SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "注册成功", this, dialog -> {
            dialog.dismiss();
            AppManager.getAppManager().startLogin(this);
        });
    }

    @Override
    public void validationSucceed(ApiResponse<CheckCode> dataApiResponse) {

    }

    private ClipboardManager cbm;
    private PopupMenu popupMenu;

    private void showPopupMenu(View view) {

        //获取剪贴板的内容
        cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // View当前PopupMenu显示的相对View的位置
        popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_plpup_copy, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    if (cbm == null) {
                        CommonUitls.showToast(RegistrationNewActivity.this, "暂无粘贴内容");
                    } else {
                        etYqm.setText(cbm.getText().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                if (etYqm.getText().toString().equals("")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        popupMenu.show();
    }

//    @OnClick({R.id.ac_newRegAndPas_determine})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ac_newRegAndPas_determine:
//                //点击确定开始注册
//                presenter.userRegistration(userPhone//电话号码
//                        , userVerification//验证码
//                        , etPas.getText().toString()//输入密码
//                        , etAgainPas.getText().toString());//再次输入密码
//                break;
//        }
//    }
}
