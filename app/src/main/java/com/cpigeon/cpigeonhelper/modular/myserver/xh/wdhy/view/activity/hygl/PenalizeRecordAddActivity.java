package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PenalizeRecordEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin2;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 处罚记录
 * Created by Administrator on 2018/3/30.
 */

public class PenalizeRecordAddActivity extends ToolbarBaseActivity {
    @BindView(R.id.tv_cfsj)
    TextView tvCfsj;
    @BindView(R.id.tv_cfjg)
    TextView tvCfjg;
    @BindView(R.id.tv_cfyy)
    TextView tvCfyy;


    @BindView(R.id.ll_cfsj)
    LinearLayout ll_cfsj;//处罚时间

    @BindView(R.id.btn_sure)
    Button btn_sure;//确定按钮
    @BindView(R.id.ll_del)
    LinearLayout ll_del;//删除按钮

    private MemberPresenter mMemberPresenter;
    private HyUserDetailEntity mHyUserDetailEntity;
    private PenalizeRecordEntity mPenalizeRecordEntity;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_penalize_record_add;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, PenalizeRecordAddActivity.this::finish);


        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");
        mPenalizeRecordEntity = (PenalizeRecordEntity) getIntent().getSerializableExtra("mPenalizeRecordEntity");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (mPenalizeRecordEntity != null) {
            setTitle("编辑处罚记录");
            btn_sure.setVisibility(View.GONE);
            ll_del.setVisibility(View.VISIBLE);

            tvCfsj.setText(mPenalizeRecordEntity.getCfsj());
            tvCfjg.setText(mPenalizeRecordEntity.getCfresult());
            tvCfyy.setText(mPenalizeRecordEntity.getCfreson());

            setTopRightButton("保存", () -> {
                mMemberPresenter.getPenalizeRecordEdit(mHyUserDetailEntity.getBasicinfo().getMid(), mPenalizeRecordEntity.getCid(),
                        tvCfsj.getText().toString(), tvCfjg.getText().toString(), tvCfyy.getText().toString(), "", "");
            });
        } else {
            setTitle("添加处罚记录");
        }

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.PENALIZE_RECORD_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, PenalizeRecordAddActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, PenalizeRecordAddActivity.this, dialog -> {
                        dialog.dismiss();
                        if (listApiResponse.getErrorCode() == 0) {
                            AppManager.getAppManager().killActivity(mWeakReference);
                        }
                    });//弹出提示
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    PenalizeRecordAddActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    PenalizeRecordAddActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.ll_cfsj, R.id.ll_cfjg, R.id.ll_cfyy, R.id.btn_sure})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }

        switch (view.getId()) {
            case R.id.ll_cfsj:
                //处罚时间
//                PickerChooseUtil.showTimePickerChooseYMD(this, tvCfsj);


                PickerAdmin2.showPicker(this, 1, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        tvCfsj.setText(year + "-" + month + "-" + day);
                    }
                });

                break;
            case R.id.ll_cfjg:
                //处罚结果
                new SaActionSheetDialog(PenalizeRecordAddActivity.this)
                        .builder()
                        .addSheetItem("记过", OnSheetItemClickListenerState)
                        .addSheetItem("禁赛一年", OnSheetItemClickListenerState)
                        .addSheetItem("警告", OnSheetItemClickListenerState)
                        .addSheetItem("严重警告", OnSheetItemClickListenerState)
                        .show();
                break;
            case R.id.ll_cfyy:
                //处罚原因
                MyMemberDialogUtil.initInputDialog(this, tvCfyy.getText().toString(), "请输入处罚原因", "请填写1-20个字符！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvCfyy.setText(etStr);
                            }
                        });
                break;

            case R.id.btn_sure:
                //确定添加
                try {
                    mMemberPresenter.getPenalizeRecordAdd(mHyUserDetailEntity.getBasicinfo().getMid(),
                            tvCfsj.getText().toString(), tvCfjg.getText().toString(), tvCfyy.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 弹出选择状态
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //记过
                    tvCfjg.setText("记过");
                    break;
                case 2:
                    //禁赛一年
                    tvCfjg.setText("禁赛一年");
                    break;
                case 3:
                    //警告
                    tvCfjg.setText("警告");
                    break;
                case 4:
                    //严重警告
                    tvCfjg.setText("严重警告");
                    break;
            }
        }
    };
}
