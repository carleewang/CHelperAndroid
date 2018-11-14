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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearSelectionEntitiy;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
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
 * 年度评选 添加
 * Created by Administrator on 2018/3/29.
 */
public class YearSelectionAddActivity extends ToolbarBaseActivity {

    @BindView(R.id.ll_pxnf)
    LinearLayout ll_pxnf;
    @BindView(R.id.tv_pxnf)
    TextView tvPxnf;  //评选年份
    @BindView(R.id.tv_pxjg)
    TextView tvPxjg; //评选结果
    @BindView(R.id.tv_jypy)
    TextView tvJypy;//简要评语

    @BindView(R.id.btn_sure)
    Button btn_sure;//确定按钮
    @BindView(R.id.ll_del)
    LinearLayout ll_del;//删除按钮

    private MemberPresenter mMemberPresenter;
    private HyUserDetailEntity mHyUserDetailEntity;//
    private YearSelectionEntitiy mYearSelectionEntitiy;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_year_selection_add;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, YearSelectionAddActivity.this::finish);

        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");

        mYearSelectionEntitiy = (YearSelectionEntitiy) getIntent().getSerializableExtra("mYearSelectionEntitiy");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (mYearSelectionEntitiy != null) {
            setTitle("编辑年度评选");
            btn_sure.setVisibility(View.GONE);
            ll_del.setVisibility(View.GONE);
            ll_pxnf.setClickable(false);
            tvPxnf.setText(mYearSelectionEntitiy.getPxyear());
            tvPxjg.setText(mYearSelectionEntitiy.getPxresult());
            tvJypy.setText(mYearSelectionEntitiy.getPxcomment());
            setTopRightButton("保存", () -> {
                try {
                    mMemberPresenter.getXHHYGL_NDPX_GetEdit(mHyUserDetailEntity.getBasicinfo().getMid(),
                            mYearSelectionEntitiy.getPid(), tvPxjg.getText().toString(), tvJypy.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            setTitle("添加年度评选");
        }

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (listApiResponse.getErrorCode()==0){
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.YEAR_SELECTION_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, YearSelectionAddActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, YearSelectionAddActivity.this, dialog -> {
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
                    YearSelectionAddActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                super.getThrowable(throwable);
                try {
                    YearSelectionAddActivity.this.getErrorNews(throwable.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.ll_pxnf, R.id.ll_pxjg, R.id.ll_jypy, R.id.btn_sure, R.id.ll_del})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }

        switch (view.getId()) {
            case R.id.ll_pxnf:
                //评选年份
                PickerAdmin.showPicker(YearSelectionAddActivity.this, 0, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tvPxnf.setText(item);
                    }
                });

                break;
            case R.id.ll_pxjg:
                //评选结果
                new SaActionSheetDialog(YearSelectionAddActivity.this)
                        .builder()
                        .addSheetItem("最佳司放员", OnSheetItemClickListenerState)
                        .addSheetItem("最佳鸽棚", OnSheetItemClickListenerState)
                        .addSheetItem("最佳裁判", OnSheetItemClickListenerState)
                        .addSheetItem("最佳会员", OnSheetItemClickListenerState)
                        .show();
                break;
            case R.id.ll_jypy:
                //简要评语
                MyMemberDialogUtil.initInputDialog(YearSelectionAddActivity.this, tvJypy.getText().toString(), "请输入简要评语", "请填写1-100个字符!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvJypy.setText(etStr);
                            }
                        });
                break;
            case R.id.btn_sure:
                //确定添加
                try {
                    mMemberPresenter.getXHHYGL_NDPX_GetAdd(mHyUserDetailEntity.getBasicinfo().getMid(),
                            tvPxnf.getText().toString(), tvPxjg.getText().toString(), tvJypy.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.ll_del:
                //删除
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
                    //最佳司放员
                    tvPxjg.setText("最佳司放员");
                    CommonUitls.showToast(YearSelectionAddActivity.this, "最佳司放员");
                    break;
                case 2:
                    //最佳鸽棚
                    tvPxjg.setText("最佳鸽棚");
                    CommonUitls.showToast(YearSelectionAddActivity.this, "最佳鸽棚");
                    break;
                case 3:
                    //最佳裁判
                    tvPxjg.setText("最佳裁判");
                    CommonUitls.showToast(YearSelectionAddActivity.this, "最佳裁判");
                    break;
                case 4:
                    //最佳会员
                    tvPxjg.setText("最佳会员");
                    CommonUitls.showToast(YearSelectionAddActivity.this, "最佳会员");
                    break;
            }
        }
    };
}
