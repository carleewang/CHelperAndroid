package com.cpigeon.cpigeonhelper.modular.usercenter.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.db.UserLoginData;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseActivity;
import com.cpigeon.cpigeonhelper.modular.home.view.activity.HomeNewActivity2;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserLoginEntity;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.LoginPresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.ILoginView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.glide.GlideCircleTransform;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 登录页
 * Created by Administrator on 2017/9/6.
 */

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.civ_user_head_img)
    CircleImageView civUserHeadImg;//用户头像
    @BindView(R.id.login_et_username)
    EditText etUsername;//输入用户名
    @BindView(R.id.login_et_userPas)
    EditText etUserPas;//输入密码
    @BindView(R.id.ac_bt_login)
    Button acBtLogin;
    @BindView(R.id.ac_bt_registration)
    TextView acBtRegistration;
    @BindView(R.id.ac_bt_ftPassword)
    TextView acBtFtPassword;
    @BindView(R.id.login_pas_PasHint)
    ImageButton pasHint;

    LoginPresenter loginPresenter;
    private boolean userInfoTag = false;

    @Override
    protected void swipeBack() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        userInfoTag = false;

        loginPresenter = new LoginPresenter(this);//初始化Presenter

        if (RealmUtils.getInstance().existUserLoginEntity()) {
            userInfoTag = true;
            etUserPas.setText(UserLoginData.getUserLoginEntity().getUserPasword());
            etUsername.setText(UserLoginData.getUserLoginEntity().getUserName());
        }
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.ac_bt_login, R.id.ac_bt_registration, R.id.ac_bt_ftPassword, R.id.login_pas_PasHint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_bt_login:
                //登录
                loginPresenter.singleLoginCheck(etUsername, etUserPas);//单点登录检查
                break;
            case R.id.ac_bt_registration:
                //注册
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            case R.id.ac_bt_ftPassword:
                //忘记密码
                startActivity(new Intent(this, ForgetPasActivity.class));
                break;
            case R.id.login_pas_PasHint://点击显示隐藏密码
                if (userInfoTag) {
                    //保存用户信息
                    userInfoTag = false;
                    etUserPas.setText("");
                    //选择状态 显示明文--设置为可见的密码
                    etUserPas.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pasHint.setImageResource(R.mipmap.show2x);//设置图片
                } else {
                    CommonUitls.setPasHint(etUserPas, pasHint);
                }
                break;
        }
    }

    /**
     * 登录成功
     */
    @Override
    public void loginSucceed(UserBean bean) {
        RealmUtils.getInstance().insertUserLoginEntity(new UserLoginEntity(etUsername.getText().toString(), etUserPas.getText().toString()));

        startActivity(new Intent(this, HomeNewActivity2.class));

        AppManager.getAppManager().killActivity(mWeakReference);
    }

    /**
     * hl 输入用户名，显示头像
     */
    @OnTextChanged(value = R.id.login_et_username, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterNameEditTextChanged(Editable s) {
        loginPresenter.setUserImg(etUsername, civUserHeadImg, this);
    }

    @Override
    public void setUserImg(String urlUserImg) {
        Glide.with(this).load(TextUtils.isEmpty(urlUserImg) ? null : urlUserImg)
                .placeholder(R.mipmap.head)
                .error(R.mipmap.head)
                .transform(new GlideCircleTransform(this))
                .into(civUserHeadImg);
    }

    /**
     * 用户已经在别的的设备上登录
     * type :1 没有登录，2已在别的设备登录
     */
    @Override
    public void isNewEquipmentLogin(int type) {

        switch (type) {
            case 1:
                loginPresenter.userLogin(etUsername, etUserPas);//开始登录
                break;
            case 2:
                showNormalDialog();
                break;
        }
    }

    /**
     * 弹出提示框：是否已经在别的设备上登录
     */
    private void showNormalDialog() {
        CommonUitls.showSweetDialog(this, "该账号已经在别的设备上登录\n是否强制登录", dialog -> {
            dialog.dismiss();
            loginPresenter.userLogin(etUsername, etUserPas);//开始登录
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
}
