package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

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
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GpRpdxSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter.GpSmsPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsViewImpl;
import com.cpigeon.cpigeonhelper.ui.MyTextView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.daima.PickerChooseUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 入棚短信设置
 * Created by Administrator on 2017/12/22.
 */

public class RpSmsSetActivity extends ToolbarBaseActivity {

    @BindView(R.id.tb_fsdx)
    ToggleButton tb_fsdx;//发送短信
    @BindView(R.id.tx_org_name)
    EditText txOrgName;//公棚简称
    @BindView(R.id.tb_fsnr)
    ToggleButton tb_fsnr;//发送内容
    @BindView(R.id.tb_gzxm)
    ToggleButton tb_gzxm;//鸽主姓名
    @BindView(R.id.tb_cskh)
    ToggleButton tb_cskh;//参赛卡号
    @BindView(R.id.tb_dzhpp)
    ToggleButton tb_dzhpp;//电子环匹配
    @BindView(R.id.ll_nr)
    LinearLayout llNr;//发送内容布局
    @BindView(R.id.tb_sgzs)
    ToggleButton tb_sgzs;//收鸽总数
    @BindView(R.id.tv_xy)
    TextView tvXy;
    @BindView(R.id.ll_dx)
    LinearLayout llDx;//底部
    @BindView(R.id.tv_hint)
    MyTextView tv_hint;//发送内容提示内容

    @BindView(R.id.tv_rp_start_time)
    TextView tv_rp_start_time;//入棚开始时间

    private GpSmsPresenter mGpSmsPresenter;//控制层
    private GpRpdxSetEntity mGpRpdxSetEntity;//公棚设置数据保存tag

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_rp_sms_set;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("入棚短信设置");
        setTopLeftButton(R.drawable.ic_back, RpSmsSetActivity.this::finish);
        setTopRightButton("保存", () -> {
            mLoadDataDialog.show();
            mGpSmsPresenter.subGpSmsSetData(mGpRpdxSetEntity, txOrgName, tb_fsnr, tv_rp_start_time);//提交如棚短信设置数据
        });

        mGpSmsPresenter = new GpSmsPresenter(new GpSmsViewImpl() {

            //获取公棚入棚短信设置数据
            @Override
            public void getGpdxSetData_RP(ApiResponse<GpRpdxSetEntity> dataApiResponse, String msg) {
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                    if (dataApiResponse.getErrorCode() == 0) {
                        //获取数据成功
                        mGpRpdxSetEntity = dataApiResponse.getData();
                        tb_fsdx.setChecked(mGpRpdxSetEntity.isSfkq());//发送短信
                        tb_gzxm.setChecked(mGpRpdxSetEntity.isGzxm());//鸽主姓名
                        tb_cskh.setChecked(mGpRpdxSetEntity.isCskh());//参赛卡号
                        tb_dzhpp.setChecked(mGpRpdxSetEntity.isDzhh());//电子环号
                        tb_sgzs.setChecked(mGpRpdxSetEntity.isSfxs());//收鸽总数
                        txOrgName.setText(mGpRpdxSetEntity.getGpjc());//公棚简称

                        //设置入棚开始时间
                        tv_rp_start_time.setText(mGpRpdxSetEntity.getRpkssj());

                        //发送短信打开 显示底部内容
                        if (mGpRpdxSetEntity.isSfkq()) {
                            llDx.setVisibility(View.VISIBLE);
                        } else {
                            llDx.setVisibility(View.GONE);
                        }
                        //鸽主姓名，参赛卡号，电子环号 有一个为true 发送内容为true
                        if (mGpRpdxSetEntity.isGzxm() || mGpRpdxSetEntity.isCskh() || mGpRpdxSetEntity.isDzhh()) {
                            tb_fsnr.setChecked(true);
                        } else {

                        }

                        showTvHint();
                    } else if (dataApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, RpSmsSetActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, RpSmsSetActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 提交入棚短信设置数据
             *
             * @param dataApiResponse
             * @param msg
             */
            @Override
            public void subGpdxSetData_RP(ApiResponse<Object> dataApiResponse, String msg) {
                try {
                    if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
                    CommonUitls.showSweetDialog(RpSmsSetActivity.this, msg);
                    if (dataApiResponse.getErrorCode() != 0) {
                        mGpSmsPresenter.getGpSmsSetData(); //获取公棚入棚短信设置数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                RpSmsSetActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                RpSmsSetActivity.this.getThrowable(throwable);
            }
        });

        mLoadDataDialog.show();
        mGpSmsPresenter.getGpSmsSetData(); //获取公棚入棚短信设置数据
    }

    @OnClick({R.id.tb_fsdx, R.id.tb_fsnr, R.id.tb_gzxm, R.id.tb_cskh, R.id.tb_dzhpp, R.id.tb_sgzs, R.id.tv_xy, R.id.ll_rp_start_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_fsdx:
                //发送短信打开 显示底部内容
                if (mGpRpdxSetEntity == null) return;

                if (tb_fsdx.isChecked()) {
                    llDx.setVisibility(View.VISIBLE);
                    mGpRpdxSetEntity.setSfkq(true);
                    tb_fsnr.setChecked(true);

                    tb_gzxm.setChecked(true);
                    mGpRpdxSetEntity.setGzxm(true);

                    tb_cskh.setChecked(true);
                    mGpRpdxSetEntity.setCskh(true);

                    tb_dzhpp.setChecked(true);
                    mGpRpdxSetEntity.setDzhh(true);

                    tb_sgzs.setChecked(true);
                    mGpRpdxSetEntity.setSfxs(true);

                } else {
                    llDx.setVisibility(View.GONE);
                    mGpRpdxSetEntity.setSfkq(false);

                    tb_fsnr.setChecked(false);

                    tb_gzxm.setChecked(false);
                    mGpRpdxSetEntity.setGzxm(false);

                    tb_cskh.setChecked(false);
                    mGpRpdxSetEntity.setCskh(false);

                    tb_dzhpp.setChecked(false);
                    mGpRpdxSetEntity.setDzhh(false);

                    tb_sgzs.setChecked(false);
                    mGpRpdxSetEntity.setSfxs(false);
                }

                showTvHint();
                break;
            case R.id.tb_fsnr:
                //发送内容
                if (mGpRpdxSetEntity == null) return;

                if (tb_fsnr.isChecked()) {
                    tb_gzxm.setChecked(true);
                    mGpRpdxSetEntity.setGzxm(true);

                    tb_cskh.setChecked(true);
                    mGpRpdxSetEntity.setCskh(true);

                    tb_dzhpp.setChecked(true);
                    mGpRpdxSetEntity.setDzhh(true);
//                    llNr.setVisibility(View.VISIBLE);
                } else {
                    tb_gzxm.setChecked(false);
                    mGpRpdxSetEntity.setGzxm(false);

                    tb_cskh.setChecked(false);
                    mGpRpdxSetEntity.setCskh(false);

                    tb_dzhpp.setChecked(false);
                    mGpRpdxSetEntity.setDzhh(false);

//                    llNr.setVisibility(View.GONE);
                }

                showTvHint();
                break;
            case R.id.tb_gzxm:
                //鸽主姓名
                if (mGpRpdxSetEntity == null) return;

                if (tb_gzxm.isChecked()) {
                    mGpRpdxSetEntity.setGzxm(true);
                } else {
                    mGpRpdxSetEntity.setGzxm(false);
                }

                showTvHint();
                break;
            case R.id.tb_cskh:
                //参赛卡号
                if (mGpRpdxSetEntity == null) return;

                if (tb_cskh.isChecked()) {
                    mGpRpdxSetEntity.setCskh(true);
                } else {
                    mGpRpdxSetEntity.setCskh(false);
                }

                showTvHint();
                break;
            case R.id.tb_dzhpp:
                //电子环匹配
                if (mGpRpdxSetEntity == null) return;

                if (tb_dzhpp.isChecked()) {
                    mGpRpdxSetEntity.setDzhh(true);
                } else {
                    mGpRpdxSetEntity.setDzhh(false);
                }

                showTvHint();
                break;
            case R.id.tb_sgzs:
                if (mGpRpdxSetEntity == null) return;

                //收鸽总数
                if (tb_sgzs.isChecked()) {
                    mGpRpdxSetEntity.setSfxs(true);
                } else {
                    mGpRpdxSetEntity.setSfxs(false);
                }
                break;
            case R.id.tv_xy:
                //协议
                Intent intent = new Intent(RpSmsSetActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.GP_SERVICE_RP);
                startActivity(intent);
                break;
            case R.id.ll_rp_start_time:
                PickerChooseUtil.showTimePickerChooseYMD(this, tv_rp_start_time);
                break;
        }
    }

    //显示发送内容提示。
    private void showTvHint() {

        if (tb_gzxm.isChecked() || tb_cskh.isChecked() || tb_dzhpp.isChecked()) {
            tb_fsnr.setChecked(true);
        }

        if (!tb_gzxm.isChecked() && !tb_cskh.isChecked() && !tb_dzhpp.isChecked()) {
            tb_fsnr.setChecked(false);
        }

        String strHint = "";
        if (tb_gzxm.isChecked()) {
            strHint += "尊敬的李三鸽友，";
        }

        strHint += "您选送观光赛鸽中心的参赛鸽2018-22-1140995，雨点，";
        if (tb_cskh.isChecked()) {
            strHint += "参赛卡号0829，";
        }

        if (tb_dzhpp.isChecked()) {
            strHint += "已配电子环，";
        }

        strHint += "于今日登记入棚请核对。";
        tv_hint.setText(strHint);
    }
}
