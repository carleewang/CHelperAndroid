package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl;

import android.os.Bundle;
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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearPayCostEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 年度缴费添加  编辑
 * Created by Administrator on 2018/3/29.
 */

public class YearPayCostAddActivity extends ToolbarBaseActivity {

    @BindView(R.id.ll_jfnf)
    LinearLayout ll_jfnf;//交费年份
    @BindView(R.id.tv_jfnf)
    TextView tvJfnf; //缴费年份
    @BindView(R.id.tv_jfzt)
    TextView tvJfzt; //缴费状态
    @BindView(R.id.btn_sure)
    Button btn_sure;//确定按钮
    @BindView(R.id.ll_del)
    LinearLayout ll_del;


    private MemberPresenter mMemberPresenter;//添加年度缴费
    private HyUserDetailEntity mHyUserDetailEntity;
    private YearPayCostEntity mYearPayCostEntity;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_year_pay_cost_add;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, YearPayCostAddActivity.this::finish);
        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");
        mYearPayCostEntity = (YearPayCostEntity) getIntent().getSerializableExtra("edit");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (mYearPayCostEntity != null) {
            setTitle("编辑交费记录");
            btn_sure.setVisibility(View.GONE);
            ll_del.setVisibility(View.GONE);
            ll_jfnf.setClickable(false);
            tvJfnf.setText(mYearPayCostEntity.getJyear());//交费年份
            tvJfzt.setText(mYearPayCostEntity.getJstate());//状态

            setTopRightButton("保存", () -> {
                //确定添加
                try {
                    mMemberPresenter.getYearPayCostEdit(mHyUserDetailEntity.getBasicinfo().getMid(),
                            mYearPayCostEntity.getJid(), tvJfzt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            setTitle("添加年度交费");
            ll_del.setVisibility(View.GONE);
        }

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.YEAR_PAY_COST_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, YearPayCostAddActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, YearPayCostAddActivity.this, dialog -> {
                        dialog.dismiss();
                        if (listApiResponse.getErrorCode() == 0) {
                            AppManager.getAppManager().killActivity(mWeakReference);
                        }
                    });//弹出提示

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    YearPayCostAddActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                super.getThrowable(throwable);
                try {
                    YearPayCostAddActivity.this.getErrorNews(throwable.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.ll_jfnf, R.id.ll_jfzt, R.id.btn_sure, R.id.ll_del})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }

        switch (view.getId()) {
            case R.id.ll_jfnf:
                //缴费年份
                PickerAdmin.showPicker(YearPayCostAddActivity.this, 0, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tvJfnf.setText(item);
                    }
                });
                break;
            case R.id.ll_jfzt:
                //缴费状态
                new SaActionSheetDialog(YearPayCostAddActivity.this)
                        .builder()
                        .addSheetItem("已交费", OnSheetItemClickListenerState)
                        .addSheetItem("未交费", OnSheetItemClickListenerState)
                        .show();
                break;
            case R.id.btn_sure:
                //确定添加
                try {
                    mMemberPresenter.getYearPayCostAdd(mHyUserDetailEntity.getBasicinfo().getMid(), tvJfnf.getText().toString(),
                            tvJfzt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_del://删除

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
                    //已缴
                    tvJfzt.setText("已交费");
                    break;
                case 2:
                    //禁赛一年
                    tvJfzt.setText("未交费");
                    break;
            }
        }
    };

}
