package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserIdInfo;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.PermissonUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员身份信息
 * Created by Administrator on 2018/3/24.
 */

public class IdInfoActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_hyxm)
    TextView tvHyxm;
    @BindView(R.id.tv_sfzhm)
    TextView tvSfzhm;
    @BindView(R.id.id_card_z)
    ImageButton idCardZ;
    @BindView(R.id.id_card_f)
    ImageButton idCardF;
    @BindView(R.id.ll_hyxm)
    LinearLayout llHyxm;
    @BindView(R.id.ll_sfzhm)
    LinearLayout llSfzhm;
    private HyUserIdInfo mHyUserIdInfo;

    private MemberPresenter mMemberPresenter;
    private HyUserDetailEntity mHyUserDetailEntity;

    private String type = "myself";//look :上级协会查看   myself：自己查看

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hy_sfxx;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, IdInfoActivity.this::finish);
        setTitle("身份信息");
        mHyUserIdInfo = (HyUserIdInfo) getIntent().getSerializableExtra("mHyUserIdInfo");
        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        PermissonUtil.getAppDetailSettingIntent(this);//权限检查（添加权限）

        if (mHyUserIdInfo != null) {
            tvHyxm.setText(mHyUserIdInfo.getXingming());
            tvSfzhm.setText(mHyUserIdInfo.getSfzhm());
            idCardZ.setImageBitmap(BitmapFactory.decodeByteArray(mHyUserIdInfo.getSfzzm(), 0, mHyUserIdInfo.getSfzzm().length));
            idCardF.setImageBitmap(BitmapFactory.decodeByteArray(mHyUserIdInfo.getSfzbm(), 0, mHyUserIdInfo.getSfzbm().length));

            if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {

                idCardZ.setClickable(false);
                idCardF.setClickable(false);

                setTopRightButton("编辑");

                setTopRightButton(getTopRightButtonStr(), () -> {
                    if (getTopRightButtonStr().equals("编辑")) {
                        setTopRightButton("上传");

                        tvHyxm.setText("");
                        tvSfzhm.setText("");

                        idCardZ.setClickable(true);
                        idCardF.setClickable(true);
                        idCardZ.setImageResource(R.mipmap.id_card_z);
                        idCardF.setImageResource(R.mipmap.id_card_f);
                    } else if (getTopRightButtonStr().equals("上传")) {
                        mMemberPresenter.uploadIdCardHyInfo(mHyUserDetailEntity.getBasicinfo().getMid(), idCardInfoEntity, idCardNInfoEntity);
                    }
                });
            }


        } else {
            if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
                setTopRightButton("上传", () -> {
                    mMemberPresenter.uploadIdCardHyInfo(mHyUserDetailEntity.getBasicinfo().getMid(), idCardInfoEntity, idCardNInfoEntity);
                });
            }
        }


        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, IdInfoActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }


                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, IdInfoActivity.this, dialog -> {
                        dialog.dismiss();
                        if (listApiResponse.getErrorCode() == 0) {
                            AppManager.getAppManager().killActivity(mWeakReference);
                        }
                    });//弹出提示
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    IdInfoActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    IdInfoActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (mHyUserIdInfo != null) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHyUserIdInfo = null;
    }

    private Intent intent;

    @OnClick({R.id.id_card_z, R.id.id_card_f, R.id.ll_hyxm, R.id.ll_sfzhm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_card_z:
                try {
                    if (getTopRightButtonStr().equals("编辑")) {
                        return;
                    }
                    intent = new Intent(this, IdCardCameraActivity.class);
                    intent.putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_P);
                    startActivityForResult(intent, IdCardCameraActivity.CODE_ID_CARD_P);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.id_card_f:
                try {
                    if (getTopRightButtonStr().equals("编辑")) {
                        return;
                    }
                    intent = new Intent(this, IdCardCameraActivity.class);
                    intent.putExtra(IntentBuilder.KEY_TYPE, IdCardCameraActivity.TYPE_N);
                    startActivityForResult(intent, IdCardCameraActivity.CODE_ID_CARD_N);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_hyxm:

                break;
            case R.id.ll_sfzhm:

                break;

        }
    }


    private IdCardPInfoEntity idCardInfoEntity;
    private IdCardNInfoEntity idCardNInfoEntity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
            if (IdCardCameraActivity.CODE_ID_CARD_P == requestCode) {
                //正面
                idCardInfoEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                if (idCardInfoEntity != null) {
                    idCardZ.setImageBitmap(BitmapFactory.decodeFile(idCardInfoEntity.frontimage));//身份证正面照片

                    tvHyxm.setText(idCardInfoEntity.name);
                    tvSfzhm.setText(idCardInfoEntity.id);
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

}
