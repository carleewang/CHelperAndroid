package com.cpigeon.cpigeonhelper.modular.saigetong.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTUserInfo;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.SGTPresenter2;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTView;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTViewImpl;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.daima.PickerChooseUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.utils.service.OrderService;
import com.github.mikephil.charting.charts.PieChart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 赛鸽通信息页
 * Created by Administrator on 2018/1/18.
 */

public class SGTInfoActivity extends ToolbarBaseActivity {

    @BindView(R.id.tv_rp_start_time)
    TextView tvRpStartTime;
    @BindView(R.id.pie_chart1)
    PieChart pieChart1;
    @BindView(R.id.pie_chart3)
    PieChart pieChart3;
    @BindView(R.id.pie_chart4)
    PieChart pieChart4;
    @BindView(R.id.pie_chart5)
    PieChart pieChart5;

    @BindView(R.id.sgt_tv1)
    TextView sgtTv1;
    @BindView(R.id.sgt_tv2)
    TextView sgtTv2;
    @BindView(R.id.sgt_tv3)
    TextView sgtTv3;
    @BindView(R.id.sgt_tv4)
    TextView sgtTv4;
    @BindView(R.id.sgt_tv5)
    TextView sgtTv5;

    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;
    @BindView(R.id.tv_content3)
    TextView tvContent3;
    @BindView(R.id.tv_content4)
    TextView tvContent4;
    @BindView(R.id.tv_content5)
    TextView tvContent5;

    @BindView(R.id.tv_krys)
    TextView tvKrys;//可容羽数

    private SGTPresenter mSGTPresenter;//控制层

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sgt_info;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("公棚赛鸽");

        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        setTopLeftButton(R.drawable.ic_back, this::finish);

        setTopRightButton("续费", new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(SGTInfoActivity.this, OrderPlayActivity.class);
                intent.putExtra("sid", OrderService.GPSG_XF_SID);
                intent.putExtra("tag", 4);
                startActivity(intent);
            }
        });

        mSGTPresenter = new SGTPresenter(mSGTView);
        mSGTPresenter.getUserInfo_SGT();

        dialog = SGTPresenter2.initSGTKrysDialog(SGTInfoActivity.this, mSGTPresenter);
    }

    private AlertDialog dialog;

    @OnClick({R.id.ll_start_time, R.id.ll_wddd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_start_time://开始时间
                PickerChooseUtil.showTimePicker(SGTInfoActivity.this, tvRpStartTime, new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        time = year + "-" + month + "-" + day;
                        mSGTPresenter.setRpTime(time);
                    }
                });
                break;
            case R.id.ll_wddd://可容羽数

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                } else {
                    dialog.show();
                }
                break;
        }
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(String strRefresh) {
        if (strRefresh.equals(EventBusService.ORDER_REFRESH)) {
            mSGTPresenter.getUserInfo_SGT();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    private String time;
    private int tag = -1;// -1 刚进入  1  已在当前页面

    //数据回调
    private SGTView mSGTView = new SGTViewImpl() {
        //获取赛鸽通用户信息结果
        @Override
        public void getUserInfo_SGT(ApiResponse<SGTUserInfo> dataApiResponse, String msg) {
            try {

                if (dataApiResponse.getErrorCode() == 0) {

                    //数据获取成功
                    sgtTv1.setText(String.valueOf(dataApiResponse.getData().getPiccountrp().getCount()));//入棚拍照张数
                    tvContent1.setText(String.valueOf(dataApiResponse.getData().getPiccountrp().getTag()));//入棚拍照张数
                    sgtTv2.setText(String.valueOf(dataApiResponse.getData().getPiccountrc().getCount()));//日常拍照张数
                    tvContent2.setText(String.valueOf(dataApiResponse.getData().getPiccountrc().getTag()));//日常拍照张数
                    sgtTv3.setText(String.valueOf(dataApiResponse.getData().getPiccountsf().getCount()));//收费拍照张数
                    tvContent3.setText(String.valueOf(dataApiResponse.getData().getPiccountsf().getTag()));//收费拍照张数

                    sgtTv4.setText(String.valueOf(dataApiResponse.getData().getPiccountbs().getCount()));//比赛拍照张数
                    tvContent4.setText(String.valueOf(dataApiResponse.getData().getPiccountbs().getTag()));//比赛拍照张数

                    sgtTv5.setText(String.valueOf(dataApiResponse.getData().getPiccounthj().getCount()));//获奖荣誉张数
                    tvContent5.setText(String.valueOf(dataApiResponse.getData().getPiccounthj().getTag()));//获奖荣誉张数

                    tvKrys.setText(String.valueOf(dataApiResponse.getData().getGpkrys() + "羽"));//可容羽数

                    SGTInfoActivity.this.setTitle(dataApiResponse.getData().getGpmc());   //公棚名称

                    tvRpStartTime.setText(String.valueOf(dataApiResponse.getData().getBjrpkssj()));//本届入棚开始时间

                    SGTPresenter2.initChart1(SGTInfoActivity.this, pieChart1, dataApiResponse.getData());

                    //使用空间
                    double sykj1 = (double) Double.valueOf(dataApiResponse.getData().getKjcountyy()) / (double) Double.valueOf(dataApiResponse.getData().getKjcountrl());
                    double sykj = 100 * sykj1;

                    SGTPresenter2.initChart(SGTInfoActivity.this, pieChart3, "已使用\n" + String.valueOf(dataApiResponse.getData().getKjcountyy()) + "M",
                            getResources().getColor(R.color.color_sgt_red), (float) (100 - sykj), (float) sykj);


                    //总共的时间
                    float zTime = DateUtils.daysBetween(dataApiResponse.getData().getKtsj(), dataApiResponse.getData().getDqsj());
                    //已使用的时间
                    float yTime = DateUtils.daysBetween(dataApiResponse.getData().getKtsj(), DateUtils.getStringDateShort());

                    float blTime = 0;
                    if (zTime != 0) {
                        blTime = 100 * (yTime / zTime);
                    }

                    SGTPresenter2.initChart(SGTInfoActivity.this, pieChart5, String.valueOf(dataApiResponse.getData().getDqsj()),
                            getResources().getColor(R.color.color_sgt_cyan), 100 - blTime, blTime);

                    //赛鸽入棚
                    double sgrp = 100 * ((double) Double.valueOf(dataApiResponse.getData().getGzcount()) / (double) Double.valueOf(dataApiResponse.getData().getGpkrys()));

                    SGTPresenter2.initChart(SGTInfoActivity.this, pieChart4, "已入棚\n" + String.valueOf(dataApiResponse.getData().getGzcount()),
                            getResources().getColor(R.color.color_sgt_blue), (float) (100 - sgrp), (float) sgrp);

                    if (tag == -1) {
                        tag = 1;
                        //赛鸽总数，小于入棚容量，设置可容羽数
                        if (Double.valueOf(dataApiResponse.getData().getGzcount()) > Double.valueOf(dataApiResponse.getData().getGpkrys())) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            } else {
                                dialog.show();
                            }
                            return;
                        }
                    }
                } else if (dataApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                    errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, SGTInfoActivity.this, dialog -> {
                        dialog.dismiss();
                        //跳转到登录页
                        AppManager.getAppManager().startLogin(MyApplication.getContext());
                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                    });
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getSetRpTimeResults(ApiResponse<Object> dataApiResponse, String msg) {

            try {
                if (dataApiResponse.getErrorCode() == 0) {
                    mSGTPresenter.getUserInfo_SGT();
                    CommonUitls.showSweetDialog(SGTInfoActivity.this, msg);
                } else {
                    CommonUitls.showSweetDialog(SGTInfoActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void getSetGPKrysResults(ApiResponse<Object> dataApiResponse, String msg) {
            try {
                if (dataApiResponse.getErrorCode() == 0) {
                    mSGTPresenter.getUserInfo_SGT();
                    CommonUitls.showSweetDialog(SGTInfoActivity.this, msg);
                } else {
                    CommonUitls.showSweetDialog(SGTInfoActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
