package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity2.GpsgHomeActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTView;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.daima.PickerChooseUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.DialogUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 赛鸽通申请试用
 * Created by Administrator on 2018/1/26.
 */

public class SGTApplyTrialActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_rp_start_time)
    TextView tvRpStartTime;
    @BindView(R.id.ll_start_time)
    LinearLayout llStartTime;

    @BindView(R.id.tv_krys)
    TextView tvKrys;
    @BindView(R.id.ll_wddd)
    LinearLayout llWddd;

    private String time;
    private String krys;
    private AlertDialog dialog;

    private SGTPresenter mSGTPresenter;//控制层

    @Override
    protected void swipeBack() {
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sgt_apply_trial;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, this::finish);
        setTitle("申请试用");//申请试用
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mSGTPresenter = new SGTPresenter(mSGTView);
        mSGTPresenter.getUserInfo_SGT();

        dialog = DialogUtil.initXiaohlDialog(this, "设置可容羽数", "",InputType.TYPE_CLASS_NUMBER, new DialogUtil.DialogClickListener() {
            @Override
            public void onDialogClickListener(View viewSure, View viewCel, AlertDialog dialog, String etStr) {
                dialog.dismiss();
                if (viewSure != null) {
                    krys = etStr;
                    tvKrys.setText(krys);
                }
            }
        });
    }

    @OnClick({R.id.ll_start_time, R.id.ll_wddd, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_start_time:
                PickerChooseUtil.showTimePicker(SGTApplyTrialActivity.this, tvRpStartTime, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        time = year + "-" + month + "-" + day;
                        tvRpStartTime.setText(time);
                    }
                });
                break;
            case R.id.ll_wddd:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                } else {
                    dialog.show();
                }

                break;
            case R.id.btn_submit:
                mSGTPresenter.applyTrial_SGT(time, krys);
                break;
        }
    }

    //数据回调
    private SGTView mSGTView = new SGTViewImpl() {
        //申请试用回调
        @Override
        public void getSetRpTimeResults(ApiResponse<Object> dataApiResponse, String msg) {
            try {
                if (dataApiResponse.getErrorCode() == 0) {
                    CommonUitls.showSweetDialog1(SGTApplyTrialActivity.this, msg, dialog -> {
                        dialog.dismiss();

//                        Intent intent = new Intent(SGTApplyTrialActivity.this, SGTHomeActivity2.class);
                        Intent intent = new Intent(SGTApplyTrialActivity.this, GpsgHomeActivity.class);
                        startActivity(intent);
                        AppManager.getAppManager().killActivity(mWeakReference);
                    });
                } else {
                    CommonUitls.showSweetDialog(SGTApplyTrialActivity.this, msg, dialog -> {
                        dialog.dismiss();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getErrorNews(String str) {
            CommonUitls.showSweetDialog(SGTApplyTrialActivity.this, str);
        }
    };
}
