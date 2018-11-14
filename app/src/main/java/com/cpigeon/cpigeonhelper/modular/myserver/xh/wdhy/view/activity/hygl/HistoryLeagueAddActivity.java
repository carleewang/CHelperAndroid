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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.daima.PickerChooseUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.FootDialog;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 历史赛绩 添加
 * Created by Administrator on 2018/3/29.
 */

public class HistoryLeagueAddActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_bsrq)
    TextView tvBsrq;
    @BindView(R.id.tv_zhhm)
    TextView tvZhhm;//足环号码
    @BindView(R.id.tv_bsgm)
    TextView tvBsgm;//比赛规模
    @BindView(R.id.tv_bsmc)
    TextView tvBsmc;//比赛名次
    @BindView(R.id.tv_xmmc)
    TextView tvXmmc;//项目名称


    @BindView(R.id.btn_sure)
    Button btn_sure;//确定按钮
    @BindView(R.id.ll_del)
    LinearLayout ll_del;

    private MemberPresenter mMemberPresenter;
    private HyUserDetailEntity mHyUserDetailEntity;
    private HistoryLeagueEntity mHistoryLeagueEntity;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_history_league_add;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, HistoryLeagueAddActivity.this::finish);

        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");
        mHistoryLeagueEntity = (HistoryLeagueEntity) getIntent().getSerializableExtra("mHistoryLeagueEntity");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        if (mHistoryLeagueEntity != null) {
            setTitle("编辑历次赛绩");
            btn_sure.setVisibility(View.GONE);
            ll_del.setVisibility(View.VISIBLE);

            tvBsrq.setText(mHistoryLeagueEntity.getBsrq());
            tvZhhm.setText(mHistoryLeagueEntity.getZhhm());
            tvBsgm.setText(mHistoryLeagueEntity.getBsgm());
            tvBsmc.setText(mHistoryLeagueEntity.getBsmc());
            tvXmmc.setText(mHistoryLeagueEntity.getXmmc());

            setTopRightButton("保存", () -> {
                try {
                    mMemberPresenter.getXHHYGL_SJ_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                            mHistoryLeagueEntity.getRid(),
                            tvZhhm.getText().toString(), tvXmmc.getText().toString(),
                            tvBsmc.getText().toString(), tvBsrq.getText().toString(), tvBsgm.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } else {
            setTitle("添加历次赛绩");
        }

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {
            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.HISTORY_LEAGUE_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HistoryLeagueAddActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HistoryLeagueAddActivity.this, dialog -> {
                        dialog.dismiss();
                        if (listApiResponse.getErrorCode() == 0) {
                            AppManager.getAppManager().killActivity(mWeakReference);
                        }
                    });//弹出提示

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @OnClick({R.id.ll_bsrq, R.id.ll_zhhm, R.id.ll_bsgm, R.id.ll_bsmc, R.id.ll_xmmc, R.id.btn_sure, R.id.ll_del})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }

        switch (view.getId()) {
            case R.id.ll_bsrq:
                //比赛日期
                PickerChooseUtil.showTimePickerChooseYMD(this, tvBsrq);
                break;
            case R.id.ll_zhhm:
                //足环号码
                FootDialog.initFootDialogOne(this, tvZhhm.getText().toString(), InputType.TYPE_CLASS_TEXT, new FootDialog.DialogClickListenerOne() {
                    @Override
                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str) {
                        dialog.dismiss();
                        tvZhhm.setText(str);
                    }
                });
                break;
            case R.id.ll_bsgm:
                //比赛规模
                MyMemberDialogUtil.initInputDialog(this, tvBsgm.getText().toString(), "请输入比赛规模", "请输入数量", InputType.TYPE_CLASS_NUMBER,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvBsgm.setText(etStr);
                            }
                        });
                break;
            case R.id.ll_bsmc:
                //比赛名次
                MyMemberDialogUtil.initInputDialog(this, tvBsmc.getText().toString(), "请输入比赛名次", "请输入数字", InputType.TYPE_CLASS_NUMBER,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvBsmc.setText(etStr);
                            }
                        });
                break;
            case R.id.ll_xmmc:
                //项目名称
                MyMemberDialogUtil.initInputDialog(this, tvXmmc.getText().toString(), "请输入项目名称", "请填写1-20个字符!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvXmmc.setText(etStr);
                            }
                        });
                break;
            case R.id.btn_sure:
                //确定添加
                try {
                    mMemberPresenter.getXHHYGL_SJ_Add(mHyUserDetailEntity.getBasicinfo().getMid(), tvZhhm.getText().toString(), tvXmmc.getText().toString(),
                            tvBsmc.getText().toString(), tvBsrq.getText().toString(), tvBsgm.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.ll_del:
                SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "确定删除该条赛绩吗？", this, dialog -> {
                    dialog.dismiss();
                    try {
                        mMemberPresenter.getXHHYGL_SJ_Del(mHyUserDetailEntity.getBasicinfo().getMid(),
                                mHistoryLeagueEntity.getRid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                break;
        }
    }
}
