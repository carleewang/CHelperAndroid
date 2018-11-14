package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter.GpSmsPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 上笼短信设置页面
 * Created by Administrator on 2017/12/25.
 */

public class SlSmsSetActivity extends ToolbarBaseActivity {

    @BindView(R.id.tb_fsdx)
    ToggleButton tb_fsdx;//发送短信
    @BindView(R.id.tx_org_name)
    EditText txOrgName;//公棚简称
    @BindView(R.id.tx_org_sl_xmmc)
    EditText txOrgXmmc;//项目名称
    @BindView(R.id.tb_fsnr)
    ToggleButton tb_fsnr;//发送内容
    @BindView(R.id.tb_gzxm)
    ToggleButton tb_gzxm;//鸽主姓名
    @BindView(R.id.tb_czzd)
    ToggleButton tb_czzd;//插组指定
    @BindView(R.id.ll_nr)
    LinearLayout llNr;//发送内容  内容
    @BindView(R.id.ll_dx)
    LinearLayout llDx;//底部全部布局

    @BindView(R.id.tv_hint)
    TextView tv_hint;//

    private GpSmsPresenter mGpSmsPresenter;//控制层
    private SlSmsSetEntity mSlSmsSetEntity;

    private AlertDialog dialog;
    private String tid;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sl_sms_set;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("上笼短信设置");
        setTopLeftButton(R.drawable.ic_back, SlSmsSetActivity.this::finish);

        setTopRightButton("保存", () -> {
            if (!tid.isEmpty()) {
                mGpSmsPresenter.getSmsSubSet_SL(tid, mSlSmsSetEntity, txOrgName, tb_fsnr, txOrgXmmc);
            }
        });

        tid = getIntent().getStringExtra("tid");
        mGpSmsPresenter = new GpSmsPresenter(new GpSmsViewImpl() {

            /**
             * 获取上笼短信设置数据回调
             * @param dataApiResponse
             * @param msg
             */
            @Override
            public void getSmsSetData_SL(ApiResponse<SlSmsSetEntity> dataApiResponse, String msg) {
                try {
                    if (dataApiResponse.getErrorCode() == 0) {
                        //获取数据成功
                        mSlSmsSetEntity = dataApiResponse.getData();
                        tb_fsdx.setChecked(mSlSmsSetEntity.isSfkq());//发送短信
                        if (mSlSmsSetEntity.isSfkq()) {
                            llDx.setVisibility(View.VISIBLE);
                        } else {
                            llDx.setVisibility(View.GONE);
                        }

                        txOrgName.setText(mSlSmsSetEntity.getGpjc());//公棚简称
                        txOrgXmmc.setText(mSlSmsSetEntity.getXmmc());//项目名称

                        tb_gzxm.setChecked(mSlSmsSetEntity.isGzxm());//鸽主姓名
                        tb_czzd.setChecked(mSlSmsSetEntity.isZhiding());//插组指定
                        if (mSlSmsSetEntity.isGzxm() || mSlSmsSetEntity.isZhiding()) {
                            tb_fsnr.setChecked(true);//发送内容
                        } else {
                            tb_fsnr.setChecked(false);//发送内容
                        }
                    } else if (dataApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, SlSmsSetActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, SlSmsSetActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 提交上笼短信设置信息回调
             */
            @Override
            public void getSmsSubSetData_SL(ApiResponse<Object> dataApiResponse, String msg) {

                try {
                    if (dataApiResponse.getErrorCode() == 0) {
                        SweetAlertDialog mSweetAlertDialog = CommonUitls.showSweetDialog1(SlSmsSetActivity.this, msg, dialog -> {
                            dialog.dismiss();
                            AppManager.getAppManager().killActivity(mWeakReference);
                        });
                        mSweetAlertDialog.setCancelable(false);//点击返回按钮没反应
                    } else {
                        CommonUitls.showSweetDialog(SlSmsSetActivity.this, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //短信测试回调
            @Override
            public void getSmsSubCeShiData_SL(ApiResponse<Object> dataApiResponse, String msg) {
                try {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    CommonUitls.showSweetDialog(SlSmsSetActivity.this, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //错误提示
            @Override
            public void getErrorNews(String str) {
                try {
                    CommonUitls.showSweetDialog(SlSmsSetActivity.this, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //异常提示
            @Override
            public void getThrowable(Throwable throwable) {
                try {
                    CommonUitls.showSweetDialog(SlSmsSetActivity.this, throwable.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (!tid.isEmpty()) {
            //获取上笼短信设置数据
            mGpSmsPresenter.getSmsSet_SL(tid);
        }
        dialog = mGpSmsPresenter.initDialog(this, mGpSmsPresenter);
    }

    @OnClick({R.id.tb_fsdx, R.id.tb_fsnr, R.id.tb_gzxm, R.id.tb_czzd, R.id.tv_ceshi_sms, R.id.tv_xy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_fsdx:
                if (mSlSmsSetEntity == null) return;
                //发送短信
                if (tb_fsdx.isChecked()) {
                    mSlSmsSetEntity.setSfkq(true);
                    llDx.setVisibility(View.VISIBLE);


                    tb_gzxm.setChecked(true);
                    mSlSmsSetEntity.setGzxm(true);

                    tb_czzd.setChecked(true);
                    mSlSmsSetEntity.setZhiding(true);

                } else {
                    mSlSmsSetEntity.setSfkq(false);
                    llDx.setVisibility(View.GONE);

                    tb_gzxm.setChecked(false);
                    mSlSmsSetEntity.setGzxm(false);

                    tb_czzd.setChecked(false);
                    mSlSmsSetEntity.setZhiding(false);
                }

                showTvHint();
                break;
            case R.id.tb_fsnr:
                if (mSlSmsSetEntity == null) return;

                //发送内容
                if (tb_fsnr.isChecked()) {

                    tb_gzxm.setChecked(true);
                    mSlSmsSetEntity.setGzxm(true);

                    tb_czzd.setChecked(true);
                    mSlSmsSetEntity.setZhiding(true);
                } else {

                    tb_gzxm.setChecked(false);
                    mSlSmsSetEntity.setGzxm(false);

                    tb_czzd.setChecked(false);
                    mSlSmsSetEntity.setZhiding(false);
                }
                showTvHint();
                break;
            case R.id.tb_gzxm:
                if (mSlSmsSetEntity == null) return;

                //鸽主姓名
                if (tb_gzxm.isChecked()) {
                    mSlSmsSetEntity.setGzxm(true);
                } else {
                    mSlSmsSetEntity.setGzxm(false);
                }

                showTvHint();
                break;
            case R.id.tb_czzd:
                if (mSlSmsSetEntity == null) return;

                //插组指定
                if (tb_czzd.isChecked()) {
                    mSlSmsSetEntity.setZhiding(true);
                } else {
                    mSlSmsSetEntity.setZhiding(false);
                }

                showTvHint();
                break;
            case R.id.tv_ceshi_sms:
                if (mSlSmsSetEntity == null) return;

                //测试短信通道
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                break;
            case R.id.tv_xy:
                //协议
                Intent intent = new Intent(SlSmsSetActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.GP_SERVICE_SL);
                startActivity(intent);
                break;
        }
    }


    //显示发送内容提示。
    private void showTvHint() {

        if (tb_gzxm.isChecked() || tb_czzd.isChecked()) {
            tb_fsnr.setChecked(true);
        }

        if (!tb_gzxm.isChecked() && !tb_czzd.isChecked()) {
            tb_fsnr.setChecked(false);
        }

        String strHint = "";
        if (tb_gzxm.isChecked()) {
            strHint += "尊敬的李三鸽友，";
        }

        strHint += "您参赛观光赛鸽中心200公里预赛的爱鸽2018-22-123456已于9日23:12:11上笼";
        if (tb_czzd.isChecked()) {
            strHint += "，已指定A组B组";
        }
        strHint += "。";
        tv_hint.setText(strHint);
    }
}
