package com.cpigeon.cpigeonhelper.modular.usercenter.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.home.view.activity.HomeNewActivity2;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.RegisAndPasPresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.IRegAndPasView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;


import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/13.
 * 修改支付密码验证页面
 */
public class ModifyPlayPasActivity extends ToolbarBaseActivity implements IRegAndPasView {


    //    @BindView(R.id.et_new)
    @BindView(R.id.ac_newRegAndPas_et_pas)
    EditText etNew;
    //    @BindView(R.id.et_new_confirm)
    @BindView(R.id.ac_newRegAndPas_et_againPas)
    EditText etNewConfirm;
//    @BindView(R.id.ac_modify_determine)
//    Button acModifyDetermine;
//    @BindView(R.id.modify_pas_PasHint)
//    ImageButton imgPasHint;

    private String t;//手机号
    private String y;//验证码

    private RegisAndPasPresenter presenter;//控制层

    private int startActivityTag = -1;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
//        return R.layout.activity_modify_play_pas;
        return R.layout.activity_new_reg_and_pas;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("设置支付密码");
        setTopLeftButton(R.drawable.ic_back, this::finish);
        setTopRightButton("完成", () -> {
            presenter.setUserPayPwd(etNew.getText().toString(),
                    etNewConfirm.getText().toString(),
                    y,
                    t);
        });

        //只能输入数字
        etNew.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNewConfirm.setInputType(InputType.TYPE_CLASS_NUMBER);

        //控制输入长度
        etNew.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        etNewConfirm.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        t = getIntent().getStringExtra("PlayPhonePas");//手机号
        y = getIntent().getStringExtra("PlayVerificationPas");//验证码

        presenter = new RegisAndPasPresenter(this);//初始化控制层

        startActivityTag = getIntent().getIntExtra("startActivityTag", -1);
    }

    /**
     * 成功重置密码后回调
     */
    @Override
    public void validationSucceed() {
        //跳转到登录页
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("温馨提示");
        dialog.setContentText("设置支付密码成功");
        dialog.setCancelable(false);
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                switch (startActivityTag) {
                    case 1://跳转到订单支付页面
                        startActivity(new Intent(ModifyPlayPasActivity.this, OrderPlayActivity.class));
                        break;
//                    case 2://跳转到个人信息页
//                        startActivity(new Intent(ModifyPlayPasActivity.this, InfoDetailsActivity.class));
//                        break;
                    default:
                        startActivity(new Intent(ModifyPlayPasActivity.this, HomeNewActivity2.class));
                }

                AppManager.getAppManager().killActivity(mWeakReference);
            }
        });
        dialog.show();
    }

    @Override
    public void validationSucceed(ApiResponse<CheckCode> dataApiResponse) {

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
