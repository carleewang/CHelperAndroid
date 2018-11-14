package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.presenter.GpSmsPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao.GpSmsViewImpl;
import com.cpigeon.cpigeonhelper.ui.MyTextView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.RequestCodeService;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 公棚  训赛短信设置
 * Created by Administrator on 2017/12/25.
 */
public class XsSmsSetActivity extends ToolbarBaseActivity {
    @BindView(R.id.tb_fsdx)
    ToggleButton tb_fsdx;//发送训赛短信
    @BindView(R.id.tx_org_name)
    EditText txOrgName;//公棚简称
    @BindView(R.id.tb_fsnr)
    ToggleButton tb_fsnr;//发送内容
    @BindView(R.id.tb_gzxm)
    ToggleButton tb_gzxm;//鸽主姓名
    @BindView(R.id.tb_zhhm)
    ToggleButton tb_zhhm;//足环号码
    @BindView(R.id.tb_zpmc)
    ToggleButton tb_zpmc;//暂排名次
    @BindView(R.id.tb_fs)
    ToggleButton tb_fs;//分速
    @BindView(R.id.ll_nr)
    LinearLayout llNr;//发送内容  内容
    @BindView(R.id.tb_fsdx_dx)
    ToggleButton tb_fsdx_dx;//发送对象
    @BindView(R.id.tb_qbfs)
    ToggleButton tb_qbfs;//全部发送
    @BindView(R.id.et_fsqng)
    EditText etFsqng;//发送前n个编辑
    @BindView(R.id.tb_fsqng)
    ToggleButton tb_fsqng;//发送前n个
    @BindView(R.id.ll_dx_dx)
    LinearLayout llDxDx;//发送对象布局
    @BindView(R.id.tv_ceshi_sms)
    TextView tvCeshiSms;//测试短信通道
    @BindView(R.id.tv_xy)
    TextView tvXy;//协议
    @BindView(R.id.ll_dx)
    LinearLayout llDx;//发送短信控制的布局
    @BindView(R.id.ll_fsdx)
    LinearLayout llFsdx;//发送对象布局

    @BindView(R.id.et_lo)
    EditText et_lo;//经度
    @BindView(R.id.et_la)
    EditText et_la;//纬度


    @BindView(R.id.tv_hint)
    MyTextView tv_hint;//发送内容提示内容

    private GpSmsPresenter mGpSmsPresenter;//控制层
    private XsListEntity mXsListEntity;
    private XsSmsSetEntity mXsSmsSetEntity;//训赛短信设置数据

    private AlertDialog dialog;//短信测试dialog

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_xs_sms_set;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("训赛短信设置");
        setTopLeftButton(R.drawable.ic_back, XsSmsSetActivity.this::finish);
        setTopRightButton("保存", () -> {
            if (mXsListEntity != null) {
                mGpSmsPresenter.subSmsSet_XS(mXsListEntity.getTid(), txOrgName, mXsSmsSetEntity, tb_qbfs, tb_fsqng, etFsqng, tb_fsnr, et_lo, et_la);
            }
        });

        //设置经纬度输入文本监听
        GPSFormatUtils.textChangedListener(this, et_lo, et_la);

        mXsListEntity = (XsListEntity) getIntent().getSerializableExtra("XsListEntity");
        mGpSmsPresenter = new GpSmsPresenter(new GpSmsViewImpl() {

            //获取训赛短信设置回调
            @Override
            public void getSmsSet_XS(ApiResponse<XsSmsSetEntity> dataApiResponse, String msg) {
                try {
                    if (dataApiResponse.getErrorCode() == 0) {
                        //获取数据成功
                        mXsSmsSetEntity = dataApiResponse.getData();

                        try {
                            et_lo.setText(String.valueOf(dataApiResponse.getData().getJingdu()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            et_la.setText(String.valueOf(dataApiResponse.getData().getWeidu()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (mXsSmsSetEntity.isKqzt()) {
                            //发送训赛短信打开
                            tb_fsdx.setChecked(true);
                            llDx.setVisibility(View.VISIBLE);

                            mXsSmsSetEntity.setKqzt(true);
                        } else {
                            //发送训赛短信关闭
                            tb_fsdx.setChecked(false);
                            llDx.setVisibility(View.GONE);

                            mXsSmsSetEntity.setKqzt(false);
                        }


                        txOrgName.setText(mXsSmsSetEntity.getGpjc());//公棚简称
                        //发送内容  内容
                        tb_gzxm.setChecked(mXsSmsSetEntity.isGzxm());//鸽主姓名
                        tb_zhhm.setChecked(mXsSmsSetEntity.isZhhm());//足环号码
                        tb_zpmc.setChecked(mXsSmsSetEntity.isFsmc());//暂排名次
                        tb_fs.setChecked(mXsSmsSetEntity.isFsfs());//发送分速


                        //发送内容
                        if (mXsSmsSetEntity.isGzxm() || mXsSmsSetEntity.isZhhm() || mXsSmsSetEntity.isFsmc() || mXsSmsSetEntity.isFsfs()) {
                            tb_fsnr.setChecked(true);
                        } else {
                            tb_fsnr.setChecked(false);
                        }

                        Log.d(TAG, "getSmsSet_XS: " + mXsSmsSetEntity.getType());

                        etFsqng.setText("" + mXsSmsSetEntity.getFsdx());

                        if (mXsSmsSetEntity.getType().equals("bs")) {
                            llFsdx.setVisibility(View.GONE);
                        } else {
                            llFsdx.setVisibility(View.VISIBLE);
                            if (mXsSmsSetEntity.getFsdx() == 0) {
                                //全部发送
                                llDxDx.setVisibility(View.VISIBLE);
                                tb_fsdx_dx.setChecked(true);
                                tb_qbfs.setChecked(true);
                                tb_fsqng.setChecked(false);
                                etFsqng.setEnabled(false);
                            } else if (mXsSmsSetEntity.getFsdx() > 0) {
                                //发送前n个
                                llDxDx.setVisibility(View.VISIBLE);
                                tb_fsdx_dx.setChecked(true);
                                tb_qbfs.setChecked(false);
                                tb_fsqng.setChecked(true);
                                etFsqng.setEnabled(true);
                            } else {
                                //全部关闭
                                llDxDx.setVisibility(View.GONE);
                                tb_fsdx_dx.setChecked(false);
                                tb_qbfs.setChecked(false);
                                tb_fsqng.setChecked(false);
                                etFsqng.setEnabled(false);
                            }
                        }

                        showTvHint();
                    } else if (dataApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, XsSmsSetActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    } else {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, XsSmsSetActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 保存训赛短信设置结果
             * @param dataApiResponse
             * @param msg
             */
            @Override
            public void subSmsSet_XS(ApiResponse<Object> dataApiResponse, String msg) {
                try {

                    CommonUitls.showSweetDialog(XsSmsSetActivity.this, msg);
                    if (dataApiResponse.getErrorCode() != 0) {
                        mGpSmsPresenter.getSmsSet_XS(mXsListEntity.getTid());
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
                    CommonUitls.showSweetDialog(XsSmsSetActivity.this, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //错误提示
            @Override
            public void getErrorNews(String str) {
                try {
                    CommonUitls.showSweetDialog(XsSmsSetActivity.this, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //异常提示
            @Override
            public void getThrowable(Throwable throwable) {
                try {
                    CommonUitls.showSweetDialog(XsSmsSetActivity.this, throwable.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (mXsListEntity != null) {
            //获取训赛短信设置数据
            mGpSmsPresenter.getSmsSet_XS(mXsListEntity.getTid());
        }

        dialog = mGpSmsPresenter.initDialog(this, mGpSmsPresenter);//初始化短信测试dialog
    }

    private Intent intent;

    @OnClick({R.id.tb_fsdx, R.id.tb_fsnr, R.id.tb_gzxm, R.id.tb_zhhm, R.id.tb_zpmc, R.id.tb_fs, R.id.tb_fsdx_dx,
            R.id.tb_qbfs, R.id.tb_fsqng, R.id.tv_ceshi_sms, R.id.tv_xy, R.id.img_locate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_fsdx:
                //发送短信
                if (mXsSmsSetEntity == null) return;

                if (tb_fsdx.isChecked()) {
                    mXsSmsSetEntity.setKqzt(true);
                    llDx.setVisibility(View.VISIBLE);

                    tb_gzxm.setChecked(true);
                    mXsSmsSetEntity.setGzxm(true);

                    tb_zhhm.setChecked(true);
                    mXsSmsSetEntity.setZhhm(true);

                    tb_zpmc.setChecked(true);
                    mXsSmsSetEntity.setFsmc(true);

                    tb_fs.setChecked(true);
                    mXsSmsSetEntity.setFsfs(true);


                } else {
                    mXsSmsSetEntity.setKqzt(false);
                    llDx.setVisibility(View.GONE);

                    tb_fsnr.setChecked(false);

                    tb_gzxm.setChecked(false);
                    mXsSmsSetEntity.setGzxm(false);

                    tb_zhhm.setChecked(false);
                    mXsSmsSetEntity.setZhhm(false);

                    tb_zpmc.setChecked(false);
                    mXsSmsSetEntity.setFsmc(false);

                    tb_fs.setChecked(false);
                    mXsSmsSetEntity.setFsfs(false);

                }

                showTvHint();
                break;
            case R.id.tb_fsnr:
                //发送内容  内容
                if (mXsSmsSetEntity == null) return;
                if (tb_fsnr.isChecked()) {
                    tb_gzxm.setChecked(true);
                    mXsSmsSetEntity.setGzxm(true);

                    tb_zhhm.setChecked(true);
                    mXsSmsSetEntity.setZhhm(true);

                    tb_zpmc.setChecked(true);
                    mXsSmsSetEntity.setFsmc(true);

                    tb_fs.setChecked(true);
                    mXsSmsSetEntity.setFsfs(true);


                } else {
                    tb_gzxm.setChecked(false);
                    mXsSmsSetEntity.setGzxm(false);

                    tb_zhhm.setChecked(false);
                    mXsSmsSetEntity.setZhhm(false);

                    tb_zpmc.setChecked(false);
                    mXsSmsSetEntity.setFsmc(false);

                    tb_fs.setChecked(false);
                    mXsSmsSetEntity.setFsfs(false);

                }

                showTvHint();
                break;
            case R.id.tb_gzxm:
                if (mXsSmsSetEntity == null) return;
                //鸽主姓名
                if (tb_gzxm.isChecked()) {
                    mXsSmsSetEntity.setGzxm(true);
                } else {
                    mXsSmsSetEntity.setGzxm(false);
                }

                showTvHint();
                break;
            case R.id.tb_zhhm:
                if (mXsSmsSetEntity == null) return;
                //足环号码
                if (tb_zhhm.isChecked()) {
                    mXsSmsSetEntity.setZhhm(true);
                } else {
                    mXsSmsSetEntity.setZhhm(false);
                }

                showTvHint();
                break;
            case R.id.tb_zpmc:
                if (mXsSmsSetEntity == null) return;
                //发送名次
                if (tb_zpmc.isChecked()) {
                    mXsSmsSetEntity.setFsmc(true);
                } else {
                    mXsSmsSetEntity.setFsmc(false);
                }

                showTvHint();
                break;
            case R.id.tb_fs:
                if (mXsSmsSetEntity == null) return;
                //分速
                if (tb_fs.isChecked()) {
                    mXsSmsSetEntity.setFsfs(true);
                } else {
                    mXsSmsSetEntity.setFsfs(false);
                }

                showTvHint();
                break;
            case R.id.tb_fsdx_dx:
                //发送对象
                if (tb_fsdx_dx.isChecked()) {
                    llDxDx.setVisibility(View.VISIBLE);
                    tb_qbfs.setChecked(true);
                    tb_fsqng.setChecked(false);
                    etFsqng.setEnabled(false);
                    etFsqng.setText("0");
                } else {

                    tb_fsqng.setChecked(false);
                    tb_qbfs.setChecked(false);
                    etFsqng.setText("0");
                    llDxDx.setVisibility(View.GONE);
                }
                break;
            case R.id.tb_qbfs:
                //全部发送
                etFsqng.setText("0");
                if (tb_qbfs.isChecked()) {
                    tb_fsqng.setChecked(false);
                    etFsqng.setEnabled(false);
                } else {
                    tb_fsqng.setChecked(true);
                    etFsqng.setEnabled(true);
                }
                break;
            case R.id.tb_fsqng:
                //发送前n个
                if (tb_fsqng.isChecked()) {
                    tb_qbfs.setChecked(false);
                    etFsqng.setEnabled(true);
                } else {
                    tb_qbfs.setChecked(true);
                    etFsqng.setEnabled(false);
                }
                break;
            case R.id.tv_ceshi_sms:
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                break;
            case R.id.tv_xy:
                //协议
                intent = new Intent(XsSmsSetActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.GP_SERVICE_XS);
                startActivity(intent);
                break;
            case R.id.img_locate:
                //选择定位
                intent = new Intent(this, XsSmsLocateSelectActivity.class);
                startActivityForResult(intent, RequestCodeService.XS_SMS_SET);
                break;
        }
    }

    //显示发送内容提示。
    private void showTvHint() {

        if (tb_gzxm.isChecked() || tb_zhhm.isChecked() || tb_zpmc.isChecked() || tb_fs.isChecked()) {
            tb_fsnr.setChecked(true);
        }

        if (!tb_gzxm.isChecked() && !tb_zhhm.isChecked() && !tb_zpmc.isChecked() && !tb_fs.isChecked()) {
            tb_fsnr.setChecked(false);
        }

        String strHint = "";
        if (tb_gzxm.isChecked()) {
            strHint += "李三鸽友:";
        }

        strHint += "您的爱鸽";
        if (tb_zhhm.isChecked()) {
            strHint += "2018-22-123456";
        }

        strHint += "在观光赛鸽中心200公里预赛中已归巢";

        if (tb_zpmc.isChecked()) {
            strHint += "，暂排1名";
        }

        if (tb_fs.isChecked()) {
            strHint += "，分速1000米";
        }

        strHint += "。";
        tv_hint.setText(strHint);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RequestCodeService.XS_SMS_SET) {
                et_lo.setText(String.valueOf(data.getDoubleExtra("lo", -1)));
                et_la.setText(String.valueOf(data.getDoubleExtra("la", -1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
