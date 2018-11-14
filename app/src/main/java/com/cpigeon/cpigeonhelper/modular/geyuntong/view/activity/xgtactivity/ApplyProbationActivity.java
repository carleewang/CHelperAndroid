package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.xgtactivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.idcard.IdCardCameraActivity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardNInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardPInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.XGTPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.GYTListActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.XGTView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.PermissonUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 训鸽通申请使用页面
 * Created by Administrator on 2017/12/7.
 */
public class ApplyProbationActivity extends ToolbarBaseActivity implements XGTView {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.id_card_z)
    ImageButton idCardZ;
    @BindView(R.id.id_card_f)
    ImageButton idCardF;
    @BindView(R.id.imtbtn_determine)
    Button imtbtnDetermine;

    @BindView(R.id.tv_err_hint)
    TextView tvErrHint;//错误提示

    private IdCardPInfoEntity idCardInfoEntity;
    private IdCardNInfoEntity idCardNInfoEntity;

    private XGTPresenter mXGTPresenter;
    private XGTEntity xgtEntity;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_probation;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //startActivity(new Intent(GYTHomeActivity.this, IdCardCameraActivity.class));

        PermissonUtil.getAppDetailSettingIntent(this);//权限检查（添加权限）

        setTitle("申请试用");
        setTopLeftButton(R.drawable.ic_back, ApplyProbationActivity.this::finish);

        xgtEntity = (XGTEntity) getIntent().getSerializableExtra("XGTEntity");

        if (xgtEntity != null) {
            Log.d(TAG, "initViews: " + xgtEntity.getStatuscode());
            idCardZ.setImageBitmap(BitmapFactory.decodeByteArray(xgtEntity.getSfzzm(), 0, xgtEntity.getSfzzm().length));//身份证正面照片
            idCardF.setImageBitmap(BitmapFactory.decodeByteArray(xgtEntity.getSfzbm(), 0, xgtEntity.getSfzbm().length));//身份证正面照片
            etName.setText(xgtEntity.getXingming());

            switch (xgtEntity.getStatuscode()) {
                case 2://身份证信息不属实
                    tvErrHint.setVisibility(View.VISIBLE);
                    tvErrHint.setText("身份证信息不属实,请重新申请");

                    CommonUitls.showSweetDialog(this, "身份证信息不属实,请重新申请");
                    break;
                case 3://信息不完整
                    tvErrHint.setVisibility(View.VISIBLE);
                    tvErrHint.setText("信息不完整,请重新申请");

                    CommonUitls.showSweetDialog(this, "信息不完整,请重新申请");
                    break;
                case 9://9为审核中

                    imtbtnDetermine.setText("正在审核中...");
                    imtbtnDetermine.setClickable(false);

//                    CommonUitls.showSweetDialog(this, "审核中，请等待处理结果");
                    etName.setFocusable(false);
                    etName.setFocusableInTouchMode(false);//设置文本输入框不可编辑
                    idCardZ.setClickable(false);
                    idCardF.setClickable(false);
                    break;
            }
        }
        mXGTPresenter = new XGTPresenter(this);
    }

    @OnClick({R.id.id_card_z, R.id.id_card_f, R.id.imtbtn_determine})
    public void onViewClicked(View view) {

        Intent intent = new Intent(ApplyProbationActivity.this, IdCardCameraActivity.class);
        switch (view.getId()) {
            case R.id.id_card_z://拍摄身份证正面
                intent.putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_P);
                startActivityForResult(intent, IdCardCameraActivity.CODE_ID_CARD_P);
                break;
            case R.id.id_card_f://拍摄身份证反面
                intent.putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_N);
                startActivityForResult(intent, IdCardCameraActivity.CODE_ID_CARD_N);
                break;
            case R.id.imtbtn_determine://提交申请(申请试用)
                if (xgtEntity != null) {
                    switch (xgtEntity.getStatuscode()) {
                        case 9://9为审核中
//                            CommonUitls.showSweetDialog(this, "审核中，请等待处理结果");
                            return;
                        default:
                    }
                }

                mXGTPresenter.openXGT(etName, idCardInfoEntity, idCardNInfoEntity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
            if (IdCardCameraActivity.CODE_ID_CARD_P == requestCode) {
                //正面
                idCardInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                if (idCardInfoEntity != null) {
                    etName.setText(idCardInfoEntity.name);//身份证名字
                    idCardZ.setImageBitmap(BitmapFactory.decodeFile(idCardInfoEntity.frontimage));//身份证正面照片
                }
            } else if (IdCardCameraActivity.CODE_ID_CARD_N == requestCode) {
                //反面
                idCardNInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                if (idCardNInfoEntity != null) {
                    //身份证反面照片
                    idCardF.setImageBitmap(BitmapFactory.decodeFile(idCardNInfoEntity.backimage));
                }
            }
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

    @Override
    public void isXGTInfo(ApiResponse<XGTEntity> apiResponse, String msg) {
    }

    @Override
    public void isUploadIdCardInfo(ApiResponse apiResponse) {

        if (apiResponse.getErrorCode() == 10013) {
            imtbtnDetermine.setText("正在审核中...");
            imtbtnDetermine.setClickable(false);
            CommonUitls.showSweetDialog2(ApplyProbationActivity.this, apiResponse.getMsg(), dialog -> {
                dialog.dismiss();
                AppManager.getAppManager().killActivity(mWeakReference);
            });
        }else if (apiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, apiResponse.getMsg(), ApplyProbationActivity.this, dialog -> {
                dialog.dismiss();
                //跳转到登录页
                AppManager.getAppManager().startLogin(MyApplication.getContext());
                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
            });
        }  else {
            CommonUitls.showSweetAlertDialog(ApplyProbationActivity.this, "温馨提示", apiResponse.getMsg());
        }


    }

    @Override
    public void getXTGOpenAndRenewInfo(List<XGTOpenAndRenewEntity> openInfo, List<XGTOpenAndRenewEntity> renewInfo, String msg) {

    }
}
