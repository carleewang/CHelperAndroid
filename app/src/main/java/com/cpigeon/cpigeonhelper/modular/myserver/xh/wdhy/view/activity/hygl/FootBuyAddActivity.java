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
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity.EditGytListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootBuyEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.FootDialog;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/30.
 */

public class FootBuyAddActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_zhnx)
    TextView tvZhnx;//足环类型
    @BindView(R.id.tv_zhhm)
    TextView tvZhhm;//足环号码
    @BindView(R.id.btn_sure)
    Button btn_sure;//确定按钮
    @BindView(R.id.ll_del)
    LinearLayout ll_del;

    private MemberPresenter mMemberPresenter;
    private HyUserDetailEntity mHyUserDetailEntity;
    private FootBuyEntity mFootBuyEntity;//

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_foot_buy_add;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, FootBuyAddActivity.this::finish);


        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");
        mFootBuyEntity = (FootBuyEntity) getIntent().getSerializableExtra("mFootBuyEntity");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        if (mFootBuyEntity != null) {
            setTitle("编辑足环购买记录");
            btn_sure.setVisibility(View.GONE);
            ll_del.setVisibility(View.VISIBLE);

            tvZhnx.setText(mFootBuyEntity.getZtype());

            switch (mFootBuyEntity.getZtype()) {
                case "散号足环":
                    tvZhhm.setText(mFootBuyEntity.getZfoot());
                    break;
                case "连号足环":
                    tvZhhm.setText(mFootBuyEntity.getZfoot1() + "," + mFootBuyEntity.getZfoot2());
                    break;
                case "其它足环（连号）":
                    tvZhhm.setText(mFootBuyEntity.getZfoot1() + "," + mFootBuyEntity.getZfoot2());
                    break;
                case "其它足环（散号）":
                    tvZhhm.setText(mFootBuyEntity.getZfoot());
                    break;
            }

            setTopRightButton("保存", () -> {
                mMemberPresenter.getFootBuyEntityEdit(mHyUserDetailEntity.getBasicinfo().getMid(),
                        mFootBuyEntity.getZid(),
                        tvZhnx.getText().toString(),
                        tvZhhm.getText().toString());
            });
        } else {
            setTitle("添加足环购买记录");
        }


        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {

                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.FOOT_BUY_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootBuyAddActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootBuyAddActivity.this, dialog -> {
                        dialog.dismiss();
                        if (listApiResponse.getErrorCode() == 0) {
                            AppManager.getAppManager().killActivity(mWeakReference);
                        }
                    });//弹出提示
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    FootBuyAddActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    FootBuyAddActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @OnClick({R.id.ll_zhnx, R.id.ll_zhhm, R.id.btn_sure, R.id.ll_del})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_zhnx:
                //足环类型
                new SaActionSheetDialog(FootBuyAddActivity.this)
                        .builder()
                        .addSheetItem("散号足环", OnSheetItemClickListenerState)
                        .addSheetItem("连号足环", OnSheetItemClickListenerState)
                        .addSheetItem("其它足环（散号）", OnSheetItemClickListenerState)
                        .addSheetItem("其它足环（连号）", OnSheetItemClickListenerState)
                        .show();

                break;
            case R.id.ll_zhhm:
                //足环号码
                if (tvZhnx.getText().toString().isEmpty()) {
                    FootBuyAddActivity.this.getErrorNews("请选择足环类型");
                    return;
                }

                if (tvZhnx.getText().toString().indexOf("连号") != -1) {
                    FootDialog.initFootDialogTwo(this, tvZhhm.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerTwo() {
                        @Override
                        public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str1, String str2) {
                            dialog.dismiss();
                            tvZhhm.setText(str1 + "," + str2);
                        }
                    });
                } else {
                    FootDialog.initFootDialogOne(this, tvZhhm.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
                        @Override
                        public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
                            dialog.dismiss();
                            tvZhhm.setText(str);
                        }
                    });
                }
                break;
            case R.id.btn_sure:
                //确定添加
                try {
                    mMemberPresenter.getFootBuyEntityAdd(mHyUserDetailEntity.getBasicinfo().getMid(),
                            tvZhnx.getText().toString(),
                            tvZhhm.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.ll_del:
                //点击删除
                SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "确定删除该条记录吗？", this, dialog -> {
                    dialog.dismiss();
                    try {
                        mMemberPresenter.getFootBuyEntityDel(mHyUserDetailEntity.getBasicinfo().getMid(),
                                mFootBuyEntity.getZid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
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
                    //散号足环
                    tvZhnx.setText("散号足环");
                    CommonUitls.showToast(FootBuyAddActivity.this, "散号足环");
                    break;
                case 2:
                    //连号足环
                    tvZhnx.setText("连号足环");
                    CommonUitls.showToast(FootBuyAddActivity.this, "连号足环");
                    break;
                case 3:
                    //其他足环(散号)
                    tvZhnx.setText("其它足环（散号）");
                    CommonUitls.showToast(FootBuyAddActivity.this, "其它足环（散号）");
                    break;
                case 4:
                    //其他足环(连号)
                    tvZhnx.setText("其它足环（连号）");
                    CommonUitls.showToast(FootBuyAddActivity.this, "其它足环（连号）");
                    break;
            }
        }
    };
}
