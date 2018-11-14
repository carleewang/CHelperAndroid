package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.BsdxSettingEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter.PlayAdminPrensenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdaoimpl.PlayAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.MyTextView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.R.id.tb_fs;
import static com.cpigeon.cpigeonhelper.R.id.tb_fsnr;
import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 比赛管理  发送短信设置
 * Created by Administrator on 2017/12/18.
 */

public class PlaySmsSetActivity extends ToolbarBaseActivity {

    @BindView(R.id.tb_fsdx)
    ToggleButton tbFsdx;//发送短信
    @BindView(R.id.tx_org_name)
    EditText txOrgName;//协会简称
    @BindView(tb_fsnr)
    ToggleButton tbFsnr;
    @BindView(R.id.tb_ssxm)
    ToggleButton tbSsxm;//比赛项目
    @BindView(R.id.tb_gcsj)
    ToggleButton tbGcsj;//归巢时间
    @BindView(tb_fs)
    ToggleButton tbFs;//分速
    @BindView(R.id.ll_nr)
    LinearLayout llNr;
    @BindView(R.id.tv_xy)
    TextView tvXy;
    @BindView(R.id.ll_dx)
    LinearLayout llDx;

    @BindView(R.id.tv_hint)
    MyTextView tv_hint;//发送内容提示内容

    private PlayListEntity playListEntity;

    private PlayAdminPrensenter mPlayAdminPrensenter;//控制层

    private BsdxSettingEntity mBsdxSettingEntity;//保存状态实体类

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play_sms_set;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        setTitle("发送短信设置");
        setTopLeftButton(R.drawable.ic_back, PlaySmsSetActivity.this::finish);

        playListEntity = (PlayListEntity) getIntent().getSerializableExtra("PlayListEntity");

        if (playListEntity != null) {
            mPlayAdminPrensenter = new PlayAdminPrensenter(new PlayAdminViewImpl() {

                /**
                 * 获取比赛短信设置数据回调
                 */
                @Override
                public void getBsdxData(ApiResponse<BsdxSettingEntity> dxsetApiResponse, String msg) {

                    try {
                        if (dxsetApiResponse.getErrorCode() == 0) {
                            mBsdxSettingEntity = dxsetApiResponse.getData();
                            tbFsdx.setChecked(dxsetApiResponse.getData().isKqzt());//发送短信
                            txOrgName.setText(dxsetApiResponse.getData().getXhjc());//协会简称
                            tbSsxm.setChecked(dxsetApiResponse.getData().isBsxm());//比赛项目
                            tbGcsj.setChecked(dxsetApiResponse.getData().isGcsj());//归巢时间
                            tbFs.setChecked(dxsetApiResponse.getData().isGzfs());//分速

                            if (!tbFsdx.isChecked()) {
                                llDx.setVisibility(View.GONE);//底部关闭
                            } else {
                                llDx.setVisibility(View.VISIBLE);//底部打开
                                if (mBsdxSettingEntity.isBsxm() || mBsdxSettingEntity.isGcsj() || mBsdxSettingEntity.isGzfs()) {
                                    tbFsnr.setChecked(true);
//                                    llNr.setVisibility(View.VISIBLE);
                                } else {
                                    tbFsnr.setChecked(false);
//                                    llNr.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, PlaySmsSetActivity.this);
                        }

                    } catch (Exception e) {

                    }
                }

                /**
                 * 保存比赛短信设置结果回调
                 */
                @Override
                public void subBsdxData(ApiResponse<Object> dxsetApiResponse, String msg) {
                    try {
                        CommonUitls.showSweetAlertDialog(PlaySmsSetActivity.this, "温馨提示", msg);
                    } catch (Exception e) {

                    }
                }
            });
            mPlayAdminPrensenter.getBsdxSetting(playListEntity.getBsid());
        }

        setTopRightButton("保存", () -> {
            try {
                if (playListEntity != null && mBsdxSettingEntity != null) {
                    //提交保存内容
                    mPlayAdminPrensenter.subSmsSetting(playListEntity.getBsid(), txOrgName, mBsdxSettingEntity);
                }
            } catch (Exception e) {

            }
        });
    }

    @OnClick({R.id.tb_fsdx, tb_fsnr, R.id.tb_ssxm, R.id.tb_gcsj, tb_fs, R.id.tv_xy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_fsdx://发送短信

                if (mBsdxSettingEntity == null) return;
                mBsdxSettingEntity.setKqzt(tbFsdx.isChecked());

                if (tbFsdx.isChecked()) {
                    llDx.setVisibility(View.VISIBLE);//底部打开
                    if (mBsdxSettingEntity.isBsxm() || mBsdxSettingEntity.isGcsj() || mBsdxSettingEntity.isGzfs()) {
                        tbFsnr.setChecked(true);
//                        llNr.setVisibility(View.VISIBLE);
                    } else {
                        tbFsnr.setChecked(false);
//                        llNr.setVisibility(View.GONE);
                    }

                    tbSsxm.setChecked(true);
                    mBsdxSettingEntity.setBsxm(true);

                    tbGcsj.setChecked(true);
                    mBsdxSettingEntity.setGcsj(true);

                    tbFs.setChecked(true);
                    mBsdxSettingEntity.setGzfs(true);

                } else {
                    llDx.setVisibility(View.GONE);//底部关闭

                    tbSsxm.setChecked(false);
                    mBsdxSettingEntity.setBsxm(false);

                    tbGcsj.setChecked(false);
                    mBsdxSettingEntity.setGcsj(false);

                    tbFs.setChecked(false);
                    mBsdxSettingEntity.setGzfs(false);
                }

                showTvHint();
                break;
            case tb_fsnr:
                if (mBsdxSettingEntity == null) return;
                if (tbFsnr.isChecked()) {

                    tbSsxm.setChecked(true);
                    mBsdxSettingEntity.setBsxm(true);

                    tbGcsj.setChecked(true);
                    mBsdxSettingEntity.setGcsj(true);

                    tbFs.setChecked(true);
                    mBsdxSettingEntity.setGzfs(true);

//                    llNr.setVisibility(View.VISIBLE);
                } else {

                    tbSsxm.setChecked(false);
                    mBsdxSettingEntity.setBsxm(false);

                    tbGcsj.setChecked(false);
                    mBsdxSettingEntity.setGcsj(false);

                    tbFs.setChecked(false);
                    mBsdxSettingEntity.setGzfs(false);

//                    llNr.setVisibility(View.GONE);
                }

                showTvHint();
                break;
            case R.id.tb_ssxm://赛事项目

                if (mBsdxSettingEntity == null) return;

                mBsdxSettingEntity.setBsxm(tbSsxm.isChecked());

                showTvHint();
                break;
            case R.id.tb_gcsj://归巢时间

                if (mBsdxSettingEntity == null) return;

                mBsdxSettingEntity.setGcsj(tbGcsj.isChecked());

                showTvHint();
                break;
            case tb_fs://分速
                if (mBsdxSettingEntity == null) return;
                mBsdxSettingEntity.setGzfs(tbFs.isChecked());

                showTvHint();
                break;
            case R.id.tv_xy:
                //协议
                Intent intent = new Intent(PlaySmsSetActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.PLAY_SMS);
                startActivity(intent);
                break;
        }
    }


    //显示发送内容提示。
    private void showTvHint() {

        if (tbSsxm.isChecked() || tbGcsj.isChecked() || tbFs.isChecked()) {
            tbFsnr.setChecked(true);
        }

        if (!tbSsxm.isChecked() && !tbGcsj.isChecked() && !tbFs.isChecked()) {
            tbFsnr.setChecked(false);
        }


        String strHint = "您参加观光鸽协";
        if (tbSsxm.isChecked()) {
            strHint += "2018年五关赛第三关";
        }

        strHint += "的赛鸽2018-22-123456已";

        if (tbGcsj.isChecked()) {
            strHint += "于6日12:12:12";
        }

        strHint += "归巢";

        if (tbFs.isChecked()) {
            strHint += "，分速1081.1234米";
        }

        strHint += "。";
        tv_hint.setText(strHint);

    }
}
