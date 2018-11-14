package com.cpigeon.cpigeonhelper.modular.usercenter.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.RegisAndPasPresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.IRegAndPasView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/13.
 * 修改登录密码验证页面
 */
public class ModifyLogPasActivity extends ToolbarBaseActivity implements IRegAndPasView {

    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_new_confirm)
    EditText etNewConfirm;
    @BindView(R.id.ac_modify_determine)
    Button acModifyDetermine;
    @BindView(R.id.modify_pas_PasHint)
    ImageButton imgPasHint;

    private RegisAndPasPresenter presenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("修改密码");
        setTopLeftButton(R.drawable.ic_back, this::finish);

        presenter = new RegisAndPasPresenter(this);//初始化控制层
    }

    @OnClick({R.id.ac_modify_determine, R.id.modify_pas_PasHint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ac_modify_determine://开始修改密码
                presenter.resettingPas(AssociationData.getUserToken()//用户登录验证
                        , AssociationData.getUserId() + ""//用户id
                        , etOld.getText().toString()//旧密码
                        , etNew.getText().toString()//新密码
                        , etNewConfirm.getText().toString());//重新输入新密码
                break;
            case R.id.modify_pas_PasHint://显示，隐藏密码
                CommonUitls.setPasHint(etOld, imgPasHint);
                CommonUitls.setPasHint(etNew, imgPasHint);
                CommonUitls.setPasHint(etNewConfirm, imgPasHint);
                break;
        }
    }

    /**
     * 成功重置密码后回调
     */
    @Override
    public void validationSucceed() {
        //跳转到登录页
//        showDialog();
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("温馨提示");
        dialog.setContentText("密码已成功修改！请重新登录。");
        dialog.setCancelable(true);
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //跳转到登录界面
                AppManager.getAppManager().startLogin(ModifyLogPasActivity.this);
                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
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
