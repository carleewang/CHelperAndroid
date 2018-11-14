package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.OrgInforPresenter;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.daima.MyTimePickerViewYMD;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.picker.PickerAdmin2;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * 个人信息
 * Created by Administrator on 2018/3/29.
 */

public class HyPersonalInfoActivity extends ToolbarBaseActivity {
    @BindView(R.id.tv_hyzp)
    ImageView tvHyzp;//会员照片
    @BindView(R.id.tv_hyxm)
    TextView tvHyxm;//会员姓名
    @BindView(R.id.tv_hyzt)
    TextView tvHyzt;//会员状态
    @BindView(R.id.tv_sjhm)
    TextView tvSjhm;//手机号码
    @BindView(R.id.tv_hyxb)
    TextView tvHyxb;//会员性别
    @BindView(R.id.tv_hrsr)
    TextView tvHrsr;//会员生日
    @BindView(R.id.tv_zcph)
    TextView tvZcph;//注册棚号
    @BindView(R.id.tv_gsmc)
    TextView tvGsmc;//鸽舍名称
    @BindView(R.id.tv_gszb)
    TextView tvGszb;//鸽舍坐标
    @BindView(R.id.tv_gsdz)
    TextView tvGsdz;//鸽舍地址
    @BindView(R.id.tv_zcrhsj)
    TextView tvZcrhsj;//注册入会时间
    @BindView(R.id.tv_csbh)
    TextView tvCsbh;//参赛编号


    private HyUserDetailEntity mHyUserDetailEntity;//
    private MemberPresenter mMemberPresenter;



    @BindView(R.id.ll_hyzp)
    LinearLayout ll_hyzp;//
    @BindView(R.id.ll_hyxm)
    LinearLayout ll_hyxm;//
    @BindView(R.id.ll_hyzt)
    LinearLayout ll_hyzt;//
    @BindView(R.id.ll_sjhm)
    LinearLayout ll_sjhm;//
    @BindView(R.id.ll_hyxb)
    LinearLayout ll_hyxb;//
    @BindView(R.id.ll_hysr)
    LinearLayout ll_hysr;//
    @BindView(R.id.ll_zcph)
    LinearLayout ll_zcph;//
    @BindView(R.id.ll_gsmc)
    LinearLayout ll_gsmc;//
    @BindView(R.id.ll_gszb)
    LinearLayout ll_gszb;//
    @BindView(R.id.ll_gsdz)
    LinearLayout ll_gsdz;//
    @BindView(R.id.ll_zcrhsj)
    LinearLayout ll_zcrhsj;//
    @BindView(R.id.ll_csbh)
    LinearLayout ll_csbh;//

    @BindView(R.id.arrow_hyzp)
    AppCompatImageView arrow_hyzp;//
    @BindView(R.id.arrow_hyxm)
    AppCompatImageView arrow_hyxm;//
    @BindView(R.id.arrow_hyzt)
    AppCompatImageView arrow_hyzt;//
    @BindView(R.id.arrow_sjhm)
    AppCompatImageView arrow_sjhm;//
    @BindView(R.id.arrow_hyxb)
    AppCompatImageView arrow_hyxb;//
    @BindView(R.id.arrow_hysr)
    AppCompatImageView arrow_hysr;//
    @BindView(R.id.arrow_zcph)
    AppCompatImageView arrow_zcph;//
    @BindView(R.id.arrow_gsmc)
    AppCompatImageView arrow_gsmc;//
    @BindView(R.id.arrow_gszb)
    AppCompatImageView arrow_gszb;//
    @BindView(R.id.arrow_gsdz)
    AppCompatImageView arrow_gsdz;//
    @BindView(R.id.arrow_zcrhsj)
    AppCompatImageView arrow_zcrhsj;//
    @BindView(R.id.arrow_csbh)
    AppCompatImageView arrow_csbh;//

    private String type = "myself";//look :上级协会查看   myself：自己查看

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hy_personal_info;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        setTopLeftButton(R.drawable.ic_back, HyPersonalInfoActivity.this::finish);
        setTitle("个人信息");

        mHyUserDetailEntity = (HyUserDetailEntity) getIntent().getSerializableExtra("data");

        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mMemberPresenter = new MemberPresenter(new MenberViewImpl() {

            @Override
            public void getHyUserDetail(ApiResponse<HyUserDetailEntity> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyPersonalInfoActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                        return;
                    }

                    //获取协会会员身份证信息
                    mHyUserDetailEntity = listApiResponse.getData();
                    initViewData(mHyUserDetailEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HyPersonalInfoActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
                try {
                    if (listApiResponse.getErrorCode() == 0) {
                        //发布事件（刷新数据）
                        EventBus.getDefault().post(EventBusService.HYGL_LIST_REFRESH);
                    } else if (listApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                        errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyPersonalInfoActivity.this, dialog -> {
                            dialog.dismiss();
                            //跳转到登录页
                            AppManager.getAppManager().startLogin(MyApplication.getContext());
                            RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                        });
                    }

                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, HyPersonalInfoActivity.this, dialog -> {
                        dialog.dismiss();
                    });//弹出提示

                    switch (listApiResponse.getErrorCode()) {
                        case 0:
                            againRequest();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HyPersonalInfoActivity.this.getThrowable(mThrowable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getErrorNews(String str) {
                super.getErrorNews(str);
                try {
                    HyPersonalInfoActivity.this.getErrorNews(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        againRequest();
    }

    //初始化控件数据
    private void initViewData(HyUserDetailEntity mHyUserDetailEntity) {
        if (mHyUserDetailEntity == null) return;


        //设置姓名
        try {
            tvHyxm.setText(mHyUserDetailEntity.getBasicinfo().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //状态
        try {
            tvHyzt.setText(mHyUserDetailEntity.getZhuangtai());
            //  ll_hyzp, ll_hyxm,ll_hyzt,ll_sjhm, ll_hyxb, ll_hysr,
            // ll_zcph, ll_gsmc, ll_gszb,ll_gsdz, ll_zcrhsj,ll_csbh})
            if (mHyUserDetailEntity.getZhuangtai().equals("除名")){
                //设置照片
                Glide.with(this).load(mHyUserDetailEntity.getBasicinfo().getTouxiang())
                        .bitmapTransform(new GrayscaleTransformation(mContext))
                        .into(tvHyzp);

                ll_hyzp.setClickable(false);
                ll_hyxm.setClickable(false);
                ll_sjhm.setClickable(false);
                ll_hyxb.setClickable(false);
                ll_hysr.setClickable(false);
                ll_zcph.setClickable(false);
                ll_gsmc.setClickable(false);
                ll_gszb.setClickable(false);
                ll_gsdz.setClickable(false);
                ll_zcrhsj.setClickable(false);
                ll_csbh.setClickable(false);

                arrow_hyzp.setVisibility(View.GONE);
                arrow_hyxm.setVisibility(View.GONE);
                arrow_sjhm.setVisibility(View.GONE);
                arrow_hyxb.setVisibility(View.GONE);
                arrow_hysr.setVisibility(View.GONE);
                arrow_zcph.setVisibility(View.GONE);
                arrow_gsmc.setVisibility(View.GONE);
                arrow_gsdz.setVisibility(View.GONE);
                arrow_zcrhsj.setVisibility(View.GONE);
                arrow_csbh.setVisibility(View.GONE);
                arrow_gszb.setVisibility(View.GONE);
            }else {
                //设置照片
                Glide.with(this).load(mHyUserDetailEntity.getBasicinfo().getTouxiang()).into(tvHyzp);

                ll_hyzp.setClickable(true);
                ll_hyxm.setClickable(true);
                ll_sjhm.setClickable(true);
                ll_hyxb.setClickable(true);
                ll_hysr.setClickable(true);
                ll_zcph.setClickable(true);
                ll_gsmc.setClickable(true);
                ll_gszb.setClickable(true);
                ll_gsdz.setClickable(true);
                ll_zcrhsj.setClickable(true);
                ll_csbh.setClickable(true);

                arrow_hyzp.setVisibility(View.VISIBLE);
                arrow_hyxm.setVisibility(View.VISIBLE);
                arrow_hyzt.setVisibility(View.VISIBLE);
                arrow_sjhm.setVisibility(View.VISIBLE);
                arrow_hyxb.setVisibility(View.VISIBLE);
                arrow_hysr.setVisibility(View.VISIBLE);
                arrow_zcph.setVisibility(View.VISIBLE);
                arrow_gsmc.setVisibility(View.VISIBLE);
                arrow_gsdz.setVisibility(View.VISIBLE);
                arrow_zcrhsj.setVisibility(View.VISIBLE);
                arrow_csbh.setVisibility(View.VISIBLE);
                arrow_gszb.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (type.equals("look") ) {

            if (mHyUserDetailEntity.getZhuangtai().equals("除名")){
                //设置照片
                Glide.with(this).load(mHyUserDetailEntity.getBasicinfo().getTouxiang())
                        .bitmapTransform(new GrayscaleTransformation(mContext))
                        .into(tvHyzp);
            }else {
                //设置照片
                Glide.with(this).load(mHyUserDetailEntity.getBasicinfo().getTouxiang()).into(tvHyzp);
            }

            ll_hyzp.setClickable(false);
            ll_hyxm.setClickable(false);
            ll_sjhm.setClickable(false);
            ll_hyxb.setClickable(false);
            ll_hysr.setClickable(false);
            ll_zcph.setClickable(false);
            ll_gsmc.setClickable(false);
            ll_gszb.setClickable(false);
            ll_hyzt.setClickable(false);
            ll_gsdz.setClickable(false);
            ll_zcrhsj.setClickable(false);
            ll_csbh.setClickable(false);

            arrow_hyzp.setVisibility(View.GONE);
            arrow_hyxm.setVisibility(View.GONE);
            arrow_sjhm.setVisibility(View.GONE);
            arrow_hyxb.setVisibility(View.GONE);
            arrow_hysr.setVisibility(View.GONE);
            arrow_zcph.setVisibility(View.GONE);
            arrow_hyzt.setVisibility(View.GONE);
            arrow_gsmc.setVisibility(View.GONE);
            arrow_gsdz.setVisibility(View.GONE);
            arrow_zcrhsj.setVisibility(View.GONE);
            arrow_csbh.setVisibility(View.GONE);
            arrow_gszb.setVisibility(View.GONE);
        }

        //手机号码
        try {
            tvSjhm.setText(mHyUserDetailEntity.getBasicinfo().getHandphone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //会员性别
        try {
            if (!mHyUserDetailEntity.getBasicinfo().getSex().isEmpty()){
                tvHyxb.setText(mHyUserDetailEntity.getBasicinfo().getSex());
            }else {
                tvHyxb.setText("男");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //会员生日
        try {
            tvHrsr.setText(mHyUserDetailEntity.getBasicinfo().getBirthday());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //棚号
        try {
            tvZcph.setText(mHyUserDetailEntity.getGesheinfo().getPn());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //鸽舍名称
        try {
            tvGsmc.setText(mHyUserDetailEntity.getGesheinfo().getGeshename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //鸽舍坐标
        try {
            if (mHyUserDetailEntity.getGesheinfo().getGeshelo().length() == 0 || mHyUserDetailEntity.getGesheinfo().getGeshela().length() == 0) {
                tvGszb.setText("");
            } else {
                tvGszb.setText(String.valueOf("东经：" + mHyUserDetailEntity.getGesheinfo().getGeshelo() + " 北纬：" + mHyUserDetailEntity.getGesheinfo().getGeshela()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvGszb.setText("");
        }
        //鸽舍地址
        try {
            tvGsdz.setText(mHyUserDetailEntity.getGesheinfo().getGesheaddr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //鸽舍时间
        try {
            tvZcrhsj.setText(mHyUserDetailEntity.getGesheinfo().getZhucetime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvCsbh.setText(mHyUserDetailEntity.getGesheinfo().getCsbh());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void againRequest() {
        mMemberPresenter.getXHHYGL_GetUserDetail(mHyUserDetailEntity.getBasicinfo().getMid(),mHyUserDetailEntity.getBasicinfo().getXhuid(),type);
    }


    @OnClick({R.id.ll_hyzp, R.id.ll_hyxm, R.id.ll_hyzt, R.id.ll_sjhm, R.id.ll_hyxb, R.id.ll_hysr, R.id.ll_zcph, R.id.ll_gsmc, R.id.ll_gszb, R.id.ll_gsdz, R.id.ll_zcrhsj, R.id.ll_csbh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_hyzp://会员照片
                new SaActionSheetDialog(HyPersonalInfoActivity.this)
                        .builder()
                        .addSheetItem("相册选取", OnSheetItemClickListener)
                        .addSheetItem("拍一张", OnSheetItemClickListener)
                        .show();
                break;
            case R.id.ll_hyxm://会员姓名
                MyMemberDialogUtil.initInputDialog(this, tvHyxm.getText().toString(), "请输入会员姓名", "请如实按照身份证进行填写!", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
//                                tvHyxm.setText(etStr);
                                mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), etStr, tvHyxb.getText().toString(),
                                        tvHrsr.getText().toString(), tvHyzt.getText().toString(), tvSjhm.getText().toString());
                            }
                        });
                break;
            case R.id.ll_hyzt://会员状态

                new SaActionSheetDialog(HyPersonalInfoActivity.this)
                        .builder()
                        .addSheetItem("在册", OnSheetItemClickListenerState)
                        .addSheetItem("禁赛", OnSheetItemClickListenerState)
                        .addSheetItem("除名", OnSheetItemClickListenerState)
                        .show();
                break;
            case R.id.ll_sjhm://手机号码
                MyMemberDialogUtil.initInputDialog(this, tvSjhm.getText().toString(), "请输入会员手机号码", "请填写正确的手机号码！", InputType.TYPE_CLASS_NUMBER,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;
//                                tvSjhm.setText(etStr);
                                mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), tvHyxb.getText().toString(),
                                        tvHrsr.getText().toString(), tvHyzt.getText().toString(), etStr);

                            }
                        });
                break;
            case R.id.ll_hyxb://会员性别
                new SaActionSheetDialog(HyPersonalInfoActivity.this)
                        .builder()
                        .addSheetItem("男", OnSheetItemClickListenerSex)
                        .addSheetItem("女", OnSheetItemClickListenerSex)
                        .show();
                break;
            case R.id.ll_hysr://会员生日
//                showTimePickerChooseYMDSr(this);

                PickerAdmin2.showPicker2(this, 0, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), tvHyxb.getText().toString(),
                                year + "-" + month + "-"+day, tvHyzt.getText().toString(), tvSjhm.getText().toString());

                    }
                });

//                PickerChooseUtil.showTimePickerChooseYMD(this, tvHrsr);
                break;
            case R.id.ll_zcph://注册棚号
                MyMemberDialogUtil.initInputDialog(this, tvZcph.getText().toString(), "请输入注册棚号", "请填写正确的棚号！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;

                                mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                        etStr, tvGsmc.getText().toString(), tvGsdz.getText().toString(),
                                        tvGszb.getText().toString(), tvZcrhsj.getText().toString(), tvCsbh.getText().toString());
                            }
                        });
                break;
            case R.id.ll_gsmc://鸽舍名称
                MyMemberDialogUtil.initInputDialog(this, tvGsmc.getText().toString(), "请输入鸽舍名称", "请填写正确的鸽舍名称！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;

                                mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                        tvZcph.getText().toString(), etStr, tvGsdz.getText().toString(),
                                        tvGszb.getText().toString(), tvZcrhsj.getText().toString(), tvCsbh.getText().toString());
                            }
                        });
                break;
            case R.id.ll_gszb://鸽舍坐标
                MyMemberDialogUtil.initInputDialogLola(this, tvGszb.getText().toString(), "请输入鸽舍坐标", "请如实填写坐标！", InputType.TYPE_CLASS_TEXT,
                        errSweetAlertDialog,
                        new MyMemberDialogUtil.DialogClickListenerLoLa() {
                            @Override
                            public void onDialogClickListenerLoLa(View viewSure, CustomAlertDialog dialog, String lo, String la) {
                                dialog.dismiss();
                                if (lo.isEmpty() || lo.length() == 0) return;
                                if (la.isEmpty() || la.length() == 0) return;

                                mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                        tvZcph.getText().toString(), tvGsmc.getText().toString(), tvGsdz.getText().toString(),
                                        lo + " " + la, tvZcrhsj.getText().toString(), tvCsbh.getText().toString());
                            }
                        });
                break;
            case R.id.ll_gsdz://鸽舍地址
                MyMemberDialogUtil.initInputDialog(this, tvGsdz.getText().toString(), "请输入鸽舍地址", "请填写正确的鸽舍地址！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;

                                mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                        tvZcph.getText().toString(), tvGsmc.getText().toString(), etStr,
                                        tvGszb.getText().toString(), tvZcrhsj.getText().toString(), tvCsbh.getText().toString());
                            }
                        });
                break;
            case R.id.ll_zcrhsj://注册入会时间

                PickerAdmin2.showPicker2(this, 0, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                tvZcph.getText().toString(), tvGsmc.getText().toString(), tvGsdz.getText().toString(),
                                tvGszb.getText().toString(), year + "-" + month + "-"+day, tvCsbh.getText().toString());
                    }
                });

//                showTimePickerChooseYMD(this);
                break;
            case R.id.ll_csbh:
                //参赛编号

                MyMemberDialogUtil.initInputDialog(this, tvCsbh.getText().toString(), "请输入鸽舍地址", "请填写正确的鸽舍地址！", InputType.TYPE_CLASS_TEXT,
                        new MyMemberDialogUtil.DialogClickListener() {
                            @Override
                            public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                dialog.dismiss();
                                if (etStr.isEmpty() || etStr.length() == 0) return;

                                mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                        tvZcph.getText().toString(), tvGsmc.getText().toString(), tvGsdz.getText().toString(),
                                        tvGszb.getText().toString(), tvZcrhsj.getText().toString(), etStr);
                            }
                        });

                break;
        }
    }

    /**
     * @param mActivity 年月日
     */
    private void showTimePickerChooseYMD(Activity mActivity) {
        //时间选择器
        MyTimePickerViewYMD pvTime = new MyTimePickerViewYMD.Builder(mActivity, new MyTimePickerViewYMD.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                mMemberPresenter.setXHHYGL_GSZL_Edit(mHyUserDetailEntity.getBasicinfo().getMid(),
                        tvZcph.getText().toString(), tvGsmc.getText().toString(), tvGsdz.getText().toString(),
                        tvGszb.getText().toString(), DateUtils.dateToStrYMD(date), tvCsbh.getText().toString());
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    /**
     * @param mActivity 年月日
     */
    private void showTimePickerChooseYMDSr(Activity mActivity) {
        //时间选择器
        MyTimePickerViewYMD pvTime = new MyTimePickerViewYMD.Builder(mActivity, new MyTimePickerViewYMD.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), tvHyxb.getText().toString(),
                        DateUtils.dateToStrYMD(date), tvHyzt.getText().toString(), tvSjhm.getText().toString());
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    private List<LocalMedia> list = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofImage();//设置选择的模式
    private int maxSelectNum = 1;// 最大图片选择数量
    private int compressMode = PictureConfig.LUBAN_COMPRESS_MODE;//选择压缩模式
    private OrgInforPresenter mOrgInforPresenter;//控制层
    private final int albumRequestCode = 0x0021;//相册,相册返回码
    private File compressimg = new File("");//图片路径

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case albumRequestCode:
                // 图片选择结果回调
                try {
                    list = PictureSelector.obtainMultipleResult(data);
                    compressimg = new File(list.get(0).getCompressPath());
                    mMemberPresenter.setUpdateUserFaceImage(mHyUserDetailEntity.getBasicinfo().getMid(), list.get(0).getCompressPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 弹出选择相册还是照相机
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListener = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //跳转到相册
                    mOrgInforPresenter.jumpAlbumWH(HyPersonalInfoActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
                case 2:
                    //跳转到相机
                    mOrgInforPresenter.jumpCameraWH(HyPersonalInfoActivity.this, chooseMode, maxSelectNum, compressMode, albumRequestCode);
                    break;
            }
        }
    };

    /**
     * 弹出选择姓名
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerSex = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //男
//                    tvHyxb.setText("男");
                    mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), "男",
                            tvHrsr.getText().toString(), tvHyzt.getText().toString(), tvSjhm.getText().toString());
                    break;
                case 2:
                    //女
//                    tvHyxb.setText("女");
                    mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), "女",
                            tvHrsr.getText().toString(), tvHyzt.getText().toString(), tvSjhm.getText().toString());
                    break;
            }
        }
    };

    /**
     * 弹出选择状态
     */
    private SaActionSheetDialog.OnSheetItemClickListener OnSheetItemClickListenerState = new SaActionSheetDialog.OnSheetItemClickListener() {
        @Override
        public void onClick(int which) {
            Logger.e(which + "");
            switch (which) {
                case 1:
                    //在册
                    mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), tvHyxb.getText().toString(),
                            tvHrsr.getText().toString(), "在册", tvSjhm.getText().toString());
                    break;
                case 2:
                    //禁赛
                    mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), tvHyxb.getText().toString(),
                            tvHrsr.getText().toString(), "禁赛", tvSjhm.getText().toString());
                    break;
                case 3:
                    //除名
                    mMemberPresenter.setXHHYGL_JBXX_Edit(mHyUserDetailEntity.getBasicinfo().getMid(), tvHyxm.getText().toString(), tvHyxb.getText().toString(),
                            tvHrsr.getText().toString(), "除名", tvSjhm.getText().toString());
                    break;
            }
        }
    };
}
