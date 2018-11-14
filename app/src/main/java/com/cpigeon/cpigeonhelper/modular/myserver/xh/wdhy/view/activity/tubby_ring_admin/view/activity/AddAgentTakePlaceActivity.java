package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity;

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
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 代售点 添加
 * Created by Administrator on 2018/6/15.
 */

public class AddAgentTakePlaceActivity extends ToolbarBaseActivity {


    @BindView(R.id.tv_dsdmc)
    TextView tvDsdmc;
    @BindView(R.id.tv_dsdxm)
    TextView tvDsdxm;
    @BindView(R.id.tv_dsddh)
    TextView tvDsddh;
    @BindView(R.id.tv_dsddz)
    TextView tvDsddz;
    @BindView(R.id.ll_dsdmc)
    LinearLayout llDsdmc;
    @BindView(R.id.ll_dsdxm)
    LinearLayout llDsdxm;
    @BindView(R.id.ll_dsddh)
    LinearLayout llDsddh;
    @BindView(R.id.ll_dsddz)
    LinearLayout llDsddz;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @BindView(R.id.btn_del)
    LinearLayout btnDel;

    private FootAdminPresenter mFootAdminPresenter;

    private String type = "-1";
    private AgentTakePlaceListEntity item;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_agent_take_place;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, AddAgentTakePlaceActivity.this::finish);
        setTitle("添加代售点");
        type = getIntent().getStringExtra("type");
        item = (AgentTakePlaceListEntity) getIntent().getSerializableExtra("data");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {
            @Override
            public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                //添加  删除代售点结果回调
                if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                if (listApiResponse.getErrorCode() == 0) {
                    EventBus.getDefault().post(EventBusService.AGENT_TAKE_PLACE_REFRESH);
                    AddAgentTakePlaceActivity.this.getErrorNews(msg);

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, AddAgentTakePlaceActivity.this, dialog -> {
                        dialog.dismiss();
                        AppManager.getAppManager().killActivity(mWeakReference);
                    });//弹出提示

                } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, AddAgentTakePlaceActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                } else {
                    AddAgentTakePlaceActivity.this.getErrorNews(msg);
                }
            }

            @Override
            public void getAgentTakePlaceModify(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                //代售点修改结果回调
                if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                if (listApiResponse.getErrorCode() == 0) {
                    EventBus.getDefault().post(EventBusService.AGENT_TAKE_PLACE_REFRESH);
                    try {
                        mFootAdminPresenter.getAgentTakePlace_details(item.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, AddAgentTakePlaceActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                } else {
                    AddAgentTakePlaceActivity.this.getErrorNews(msg);
                }
            }

            @Override
            public void getAgentTakePlaceDetails(ApiResponse<AgentTakePlaceListEntity> listApiResponse, String msg, Throwable mThrowable) {
                //获取代售点详情
                if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();

                if (listApiResponse.getErrorCode() == 0) {
                    tvDsdmc.setText(listApiResponse.getData().getDsd());
                    tvDsdxm.setText(listApiResponse.getData().getLxr());
                    tvDsddh.setText(listApiResponse.getData().getTel());
                    tvDsddz.setText(listApiResponse.getData().getDiz());
                } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, AddAgentTakePlaceActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                } else {
                    AddAgentTakePlaceActivity.this.getErrorNews(msg);
                }
            }

            @Override
            public void getErrorNews(String str) {
                AddAgentTakePlaceActivity.this.getErrorNews(str);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                AddAgentTakePlaceActivity.this.getThrowable(throwable);
            }
        });

        if (type.equals("modify")) {
            btnSure.setVisibility(View.GONE);
            btnDel.setVisibility(View.VISIBLE);
            if (item != null) {
                tvDsdmc.setText(item.getDsd());
                tvDsdxm.setText(item.getLxr());
                tvDsddh.setText(item.getTel());
                tvDsddz.setText(item.getDiz());
            }
        } else {
            btnDel.setVisibility(View.GONE);
            btnSure.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.ll_dsdmc, R.id.ll_dsdxm, R.id.ll_dsddh, R.id.ll_dsddz, R.id.btn_sure, R.id.btn_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_dsdmc:

                MyMemberDialogUtil.initInputDialog(AddAgentTakePlaceActivity.this, tvDsdmc.getText().toString(), "请输入代售点名称", "请如实进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvDsdmc.setText(etStr);
                                modiftAgentTakePlace();
                            }
                        });


                break;
            case R.id.ll_dsdxm:
                MyMemberDialogUtil.initInputDialog(AddAgentTakePlaceActivity.this, tvDsdxm.getText().toString(), "请输入联系人姓名", "请如实进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvDsdxm.setText(etStr);
                                modiftAgentTakePlace();
                            }
                        });

                break;
            case R.id.ll_dsddh:
                MyMemberDialogUtil.initInputDialog(AddAgentTakePlaceActivity.this, tvDsddh.getText().toString(), "请输入联系人电话", "请如实进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvDsddh.setText(etStr);
                                modiftAgentTakePlace();
                            }
                        });

                break;
            case R.id.ll_dsddz:

                MyMemberDialogUtil.initInputDialog(AddAgentTakePlaceActivity.this, tvDsddz.getText().toString(), "请输入代售点地址", "请如实进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
                                tvDsddz.setText(etStr);
                                modiftAgentTakePlace();
                            }
                        });
                break;
            case R.id.btn_sure:
                mLoadDataDialog.show();
                mFootAdminPresenter.getAgentTakePlace_Add(tvDsdmc.getText().toString(),
                        tvDsdxm.getText().toString(),
                        tvDsddh.getText().toString(),
                        tvDsddz.getText().toString()
                );
                break;

            case R.id.btn_del:

                if (item != null) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "确定要删除该条代售点吗", this, dialog -> {
                        dialog.dismiss();
                        mLoadDataDialog.show();
                        mFootAdminPresenter.getAgentTakePlace_Del(item.getId());
                    });//弹出提示

                }

                break;
        }
    }

    //修改代售点
    private void modiftAgentTakePlace() {
        if (type.equals("modify")) {
            if (item != null) {
                mFootAdminPresenter.getAgentTakePlace_Modify(item.getId(), tvDsdmc.getText().toString(),
                        tvDsdxm.getText().toString(), tvDsddh.getText().toString(), tvDsddz.getText().toString());
            }
        }
    }

}
