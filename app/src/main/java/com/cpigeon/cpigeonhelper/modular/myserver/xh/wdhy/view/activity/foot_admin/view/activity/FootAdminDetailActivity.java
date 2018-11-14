package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay.SmallVideoHelper;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.HyglListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity.AgentTakePlaceActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.playvideo.VideoPlayActivity;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog2;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.video.RecordedFootShootActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FootAdminDetailActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_foot)
    TextView tvFoot;
    @BindView(R.id.tv_dove_lord_name)
    TextView tvDoveLordName;//鸽主姓名
    @BindView(R.id.tv_status_sell)
    TextView tv_status_sell;//出售状态
    @BindView(R.id.tv_status_paying)
    TextView tv_status_paying;//缴费状态
    @BindView(R.id.tv_foot_money)
    TextView tv_foot_money;//足环价格
    @BindView(R.id.tv_agent_take_place)
    TextView tv_agent_take_place;//代售点

    @BindView(R.id.ll_sgyl)
    LinearLayout ll_sgyl;//拍摄


    @BindView(R.id.llz_sgyl)
    LinearLayout llz_sgyl;//上笼验鸽
    @BindView(R.id.ll_status_paying)
    LinearLayout ll_status_paying;//交费状态
    @BindView(R.id.ll_agent_take_place)
    LinearLayout ll_agent_take_place;//代售点


    @BindView(R.id.fl_sgyl)
    FrameLayout fl_sgyl;
    @BindView(R.id.img_slyg)
    ImageView img_slyg;//上笼验鸽
    @BindView(R.id.rl_sgyl)
    RelativeLayout rl_sgyl;
    @BindView(R.id.list_item_btn)
    ImageView list_item_btn;//
    @BindView(R.id.list_item_btn1)
    ImageView list_item_btn1;//

    @BindView(R.id.view_status_paying)
    View view_status_paying;//
    @BindView(R.id.view_agent_take_place)
    View view_agent_take_place;//

    private FootAdminPresenter mFootAdminPresenter;
    private FootAdminListDataEntity.FootlistBean mFootAdminListEntity;

    private FootAdminEntity mFootAdminEntity = null;//足环详细信息

    private String dsdId = "-1";
    private String jf = "0";

    private String type = "普通足环";//普通足环  特比环
    private String gzid = "";//购买者 鸽主id
    private String footId = "";//足环id

    private SmallVideoHelper smallVideoHelper;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_foot_admin_detail;
    }

    @Override
    protected void setStatusBar() {

        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, FootAdminDetailActivity.this::finish);

        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        if (type.equals("普通足环")) {
            llz_sgyl.setVisibility(View.GONE);
            ll_status_paying.setVisibility(View.GONE);
            ll_agent_take_place.setVisibility(View.GONE);

            view_status_paying.setVisibility(View.GONE);
            view_agent_take_place.setVisibility(View.GONE);

            setTitle("足环详情");

        } else if (type.equals("特比环")) {
            setTitle("特足环详情");
        }

        mFootAdminListEntity = (FootAdminListDataEntity.FootlistBean) getIntent().getSerializableExtra("data");

        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {

            @Override
            public void getFootAdminDetailsData(ApiResponse<FootAdminEntity> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (mThrowable != null) {
                        FootAdminDetailActivity.this.getThrowable(mThrowable);
                        return;
                    }
                    if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminDetailActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }

                    FootAdminDetailActivity.this.mFootAdminEntity = listApiResponse.getData();

                    gzid = listApiResponse.getData().getBuyuid();
                    footId = listApiResponse.getData().getId();

                    tvFoot.setText(listApiResponse.getData().getFoot());//足环号码
                    tvDoveLordName.setText(listApiResponse.getData().getBuyname());//鸽主姓名

                    if (type.equals("特比环")) {
                        try {
                            if (tvDoveLordName.getText().toString().isEmpty()) {
                                llz_sgyl.setVisibility(View.INVISIBLE);
                            } else {
                                llz_sgyl.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            llz_sgyl.setVisibility(View.INVISIBLE);
                        }
                    }


                    if (listApiResponse.getData().getCszt().equals("1")) {
                        tv_status_sell.setText("已出售");//出售状态
                    } else {
                        tv_status_sell.setText("未出售");//出售状态
                    }

                    if (listApiResponse.getData().getJfzt().equals("1")) {
                        jf = "1";
                        tv_status_paying.setText("已交费");//缴费状态
                    } else {
                        jf = "0";
                        tv_status_paying.setText("未交费");//缴费状态
                    }

                    dsdId = listApiResponse.getData().getDsd();

                    tv_foot_money.setText(listApiResponse.getData().getZhjg());//足环价格
                    tv_agent_take_place.setText(listApiResponse.getData().getDsdname());//代售点

                    //上笼验鸽
                    if (listApiResponse.getData().getSlyg() != null) {

                        if (listApiResponse.getData().getSlyg().getFiletype().equals("图片")) {

                            if (!listApiResponse.getData().getSlyg().getFileurl().isEmpty()) {

                                noShootHint();

                                img_slyg.setVisibility(View.VISIBLE);
                                Glide.with(FootAdminDetailActivity.this)
                                        .load(listApiResponse.getData().getSlyg().getFileurl())
                                        .into(img_slyg);

                                img_slyg.setOnClickListener(view -> {
                                    LocalMedia localMedia = new LocalMedia();
                                    localMedia.setPath(listApiResponse.getData().getSlyg().getFileurl());

                                    //图片预览展示
                                    PictureSelector.create(FootAdminDetailActivity.this).externalPicturePreview(0, Lists.newArrayList(localMedia));
                                });

                            }

                        } else if (listApiResponse.getData().getSlyg().getFiletype().equals("视频")) {

                            if (!listApiResponse.getData().getSlyg().getFileurl().isEmpty()) {
                                noShootHint();

                                rl_sgyl.setVisibility(View.VISIBLE);

                                Glide.with(FootAdminDetailActivity.this).load(listApiResponse.getData().getSlyg().getFilepic()).into(list_item_btn1);//缩略图

                                rl_sgyl.setOnClickListener(view -> {
                                    VideoPlayActivity.startActivity(FootAdminDetailActivity.this, rl_sgyl, listApiResponse.getData().getSlyg().getFileurl());
                                });
                            }
                        }
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getFootAdminResultsData(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mThrowable != null) {
                        FootAdminDetailActivity.this.getThrowable(mThrowable);
                        return;
                    }

                    try {
                        if (listApiResponse.getErrorCode() == 0) {
                            EventBus.getDefault().post(EventBusService.FOOT_ADMIN_REFRESH);

                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminDetailActivity.this, dialog -> {
                                dialog.dismiss();
                                AppManager.getAppManager().killActivity(mWeakReference);
                            });//弹出提示
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminDetailActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getFootAdminResultsData2(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (mThrowable != null) {
                        FootAdminDetailActivity.this.getThrowable(mThrowable);
                        return;
                    }
                    try {
                        if (listApiResponse.getErrorCode() == 0) {
                            mFootAdminPresenter.getXHHYGL_ZHGL_GetDetail(footId);
                            EventBus.getDefault().post(EventBusService.FOOT_ADMIN_REFRESH);
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminDetailActivity.this, dialog -> {
                                dialog.dismiss();
                            });//弹出提示
                        } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, FootAdminDetailActivity.this, dialog -> {
                                dialog.dismiss();
                                //跳转到登录页
                                AppManager.getAppManager().startLogin(MyApplication.getContext());
                                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (mFootAdminListEntity != null) {
            //获取详情信息
            footId = mFootAdminListEntity.getId();
            mFootAdminPresenter.getXHHYGL_ZHGL_GetDetail(footId);
        }
    }

    //已存在验鸽图片 或视频，不能再继续上传
    private void noShootHint() {
        ll_sgyl.setOnClickListener(view -> {
            FootAdminDetailActivity.this.getErrorNews("已存在上笼验鸽，不能再次上传！");
        });
    }

    private Intent intent;

    @OnClick({R.id.ll_foot, R.id.ll_dove_lord_name, R.id.btn_del, R.id.ll_sgyl, R.id.ll_status_sell,
            R.id.ll_status_paying, R.id.ll_agent_take_place})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_foot:
                //getXHHYGL_ZHGL_GetEdit
                break;
            case R.id.ll_dove_lord_name:
                //鸽主姓名
                intent = new Intent(this, HyglListActivity.class);
                intent.putExtra("type", "select");
                startActivity(intent);
//                MyMemberDialogUtil.initInputDialog1(FootAdminDetailActivity.this, "", "输入鸽主姓名", "请填写鸽主姓名!", InputType.TYPE_CLASS_TEXT,
//                        new MyMemberDialogUtil.DialogClickListener() {
//                            @Override
//                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
//                                dialog.dismiss();
//                                if (etStr.isEmpty() || etStr.length() == 0) {
//                                    tvDoveLordName.setText("");
//                                    tv_status_sell.setText("未出售");
//                                    return;
//                                } else {
//                                    tvDoveLordName.setText(etStr);
//                                    tv_status_sell.setText("已出售");
//                                }
//
//                                mFootAdminPresenter.getXHHYGL_ZHGL_GetEdit(mFootAdminListEntity.getId(), "特比环", tvDoveLordName.getText().toString(), "0", jf, dsdId);
//                            }
//                        });

                break;

            case R.id.ll_status_sell:
                //出售状态

                break;

            case R.id.ll_status_paying:
                //缴费状态
                if (tvDoveLordName.getText().toString().isEmpty()) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "请先选择鸽主姓名", FootAdminDetailActivity.this, dialog -> {
                        dialog.dismiss();
                    });//弹出提示
                } else {
                    new SaActionSheetDialog(FootAdminDetailActivity.this)
                            .builder()
                            .addSheetItem("已交费", OnSheetItemClickListenerState1)
                            .addSheetItem("未交费", OnSheetItemClickListenerState1)
                            .show();
                }
                break;
            case R.id.ll_agent_take_place:
                //代售点
                Intent intent = new Intent(FootAdminDetailActivity.this, AgentTakePlaceActivity.class);
                intent.putExtra("type", "select");
                startActivity(intent);

                break;
            case R.id.btn_del:
                errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "确定删除该足环吗？", FootAdminDetailActivity.this, dialog -> {
                    dialog.dismiss();
                    if (mFootAdminListEntity != null) {
                        mFootAdminPresenter.getXHHYGL_ZHGL_GetDel(footId);
                    }
                });//弹出提示
                break;
            case R.id.ll_sgyl:
                shootHintDialog(this);
                break;
        }
    }

    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState1 = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            switch (which) {
                case 1:
                    //已交费
                    jf = "1";
                    tv_status_paying.setText("已交费");
                    break;
                case 2:
                    //未交费
                    jf = "0";
                    tv_status_paying.setText("未交费");
                    break;
            }
            mFootAdminPresenter.getXHHYGL_ZHGL_GetEdit(footId, type, tvDoveLordName.getText().toString(), gzid, jf, dsdId);
        }
    };

    //点击拍照  拍摄视频
    private CustomAlertDialog2 shootHintDialog(Activity mContext) {

        try {
            CustomAlertDialog2 dialog = new CustomAlertDialog2(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_shoot_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView shoot_photo = dialogLayout.findViewById(R.id.shoot_photo);
            ImageView shoot_video = dialogLayout.findViewById(R.id.shoot_video);

            LinearLayout ll_z = dialogLayout.findViewById(R.id.ll_z);
            ll_z.setOnClickListener(view -> {
                dialog.dismiss();
            });

            //拍摄图片
            shoot_photo.setOnClickListener(view -> {
                dialog.dismiss();

                if (mFootAdminEntity != null) {
                    Intent intent = new Intent(mContext, RecordedFootShootActivity.class);
                    intent.putExtra("type", "photo");
                    intent.putExtra("data", mFootAdminEntity);
                    mContext.startActivity(intent);
                } else {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "暂无足环数据", FootAdminDetailActivity.this, mDialog -> {
                        mDialog.dismiss();
                    });
                }
            });

            //拍摄视频
            shoot_video.setOnClickListener(view -> {
                dialog.dismiss();
                if (mFootAdminEntity != null) {
                    Intent intent = new Intent(mContext, RecordedFootShootActivity.class);
                    intent.putExtra("type", "video");
                    intent.putExtra("data", mFootAdminEntity);
                    mContext.startActivity(intent);
                } else {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "暂无足环数据", FootAdminDetailActivity.this, mDialog -> {
                        mDialog.dismiss();
                    });
                }
            });

            dialog.setContentView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
//            toggleInput(mContext);
            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(AgentTakePlaceListEntity item) {
        //选择代售点返回
        dsdId = item.getId();
        tv_agent_take_place.setText(item.getDsd());
        mFootAdminPresenter.getXHHYGL_ZHGL_GetEdit(footId, type, tvDoveLordName.getText().toString(), gzid, jf, dsdId);
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(HyglHomeListEntity.DatalistBean item) {
        //选择鸽主姓名返回
        tvDoveLordName.setText(item.getName());
        gzid = item.getId();
        mFootAdminPresenter.getXHHYGL_ZHGL_GetEdit(footId, type, tvDoveLordName.getText().toString(), gzid, jf, dsdId);
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.FOOT_ADMIN_UPLOAD)) {
            mFootAdminPresenter.getXHHYGL_ZHGL_GetDetail(footId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
