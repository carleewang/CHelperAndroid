package com.cpigeon.cpigeonhelper.modular.geyuntong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.modular.authorise.view.activity.AuthoriseHomeActivity;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.activity.FlyingAreaActivity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTService;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.GYTHomePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.presenter.XGTPresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTHomeView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.XGTView;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OpenGytActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.RenewalUpgradeActivity;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 鸽运通主页
 * Created by Administrator on 2017/9/18.
 */

public class GYTHomeActivity extends ToolbarBaseActivity implements GYTHomeView, XGTView {

    @BindView(R.id.img_vip)
    ImageView imgVip;//vip等级
    @BindView(R.id.imgbtn_gyt)
    ImageButton imgbtn1;//鸽运通
    @BindView(R.id.img_sifangdi)
    ImageButton imgbtn2;//司放地
    @BindView(R.id.img_xufei)
    ImageButton imgbtn3;//续费
    @BindView(R.id.img_shouquan)
    ImageButton imgbtn4;//授权
    @BindView(R.id.imgbtn_ll4)
    LinearLayout imgbtnLl4;
    @BindView(R.id.tv_center_miles)
    TextView tvCenterMiles;//已监控总里程
    @BindView(R.id.tv_total_length)
    TextView tvTotalLength;//总时长
    @BindView(R.id.tv_total_number)
    TextView tvTotalNumber;//监控次数
    @BindView(R.id.tv_open_time)
    TextView tvOpenTime;//开通时间
    @BindView(R.id.tv_come_due_time)
    TextView tvComeDueTime;//到期时间
    @BindView(R.id.tv_gb)
    TextView tvGb;//鸽币

    private GYTHomePresenter presenter;//鸽运通主页控制层
    private XGTPresenter mXGTPresenter;

    private String serviceType;//当前服务类型{鸽运通，训鸽通（geyuntong，xungetong）}

    private boolean isOpenXGT = false;//是否开通训鸽通
    private Handler handler;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gyt_homes;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    private String TAG = "XGTPresenter";

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTopLeftButton(R.drawable.ic_back, GYTHomeActivity.this::finish);

        serviceType = RealmUtils.getServiceType();
        presenter = new GYTHomePresenter(this);//初始化控制层

        mXGTPresenter = new XGTPresenter(this);//训鸽通控制层
        handler = new Handler();
        initData();
    }

    private void initData() {
        if (serviceType.equals("geyuntong")) {
            setTitle("鸽运通");
            //获取鸽运通服务数据
            presenter.gytServerDatas(AssociationData.getUserAccountTypeStrings(), serviceType);
            imgbtn3.setImageResource(R.mipmap.xufei_text2x);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.getGYTHomeData("1");//获取鸽运通统计数据数据
                }
            }, 300);

        } else if (serviceType.equals("xungetong")) {
            if (this.isDestroyed()) {
                return;
            }

            setTitle("训鸽通");
            imgbtn1.setImageResource(R.mipmap.xunget);
            imgbtn2.setImageResource(R.mipmap.xunfangd);
//            imgbtn3.setImageResource(R.mipmap.shenqingsy);
            imgbtnLl4.setVisibility(View.GONE);

            presenter.gytServerDatas("geren", serviceType);//获取训鸽通服务数据

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mXGTPresenter.getXGTInfo();//获取训鸽通信息
                }
            }, 300);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.getGYTHomeData("xungetong");//获取训鸽通统计数据数据
                }
            }, 600);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @OnClick({R.id.imgbtn_gyt, R.id.img_sifangdi, R.id.img_xufei, R.id.img_shouquan})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {
            return;
        }

        switch (view.getId()) {
            case R.id.imgbtn_gyt:
                if (serviceType.equals("geyuntong")) {
                    //点击鸽运通
                    startActivity(new Intent(GYTHomeActivity.this, GYTListActivity.class));
                } else if (serviceType.equals("xungetong")) {
                    //点击训鸽通
                    if (isOpenXGT) {
                        startActivity(new Intent(GYTHomeActivity.this, GYTListActivity.class));
                    } else {
                        CommonUitls.showToast(this, "请先开通训鸽通服务");
                    }
                }
                break;

            case R.id.img_sifangdi:
                if (serviceType.equals("geyuntong")) {
                    //点击司放地
                    if (AssociationData.getUserAccountTypeString().equals("组织信息")) {
                        CommonUitls.showToast(this, "您还不是公棚（协会）用户，无法进行司放地操作");
                        return;
                    }
                    startActivity(new Intent(GYTHomeActivity.this, FlyingAreaActivity.class));
                } else if (serviceType.equals("xungetong")) {
                    //点击训放地
                    if (isOpenXGT) {
                        startActivity(new Intent(GYTHomeActivity.this, FlyingAreaActivity.class));
                    } else {
                        CommonUitls.showToast(this, "请先开通训鸽通服务");
                    }
                }

                break;
            case R.id.img_xufei:
                if (serviceType.equals("geyuntong")) {
                    //点击续费
                    startXufei();
                } else if (serviceType.equals("xungetong")) {
                    //点击开通服务
//                    startActivity(new Intent(GYTHomeActivity.this, ApplyProbationActivity.class));//申请试用
//                    XGTPresenter.initXTGView(myXGTEntity, this, imgbtn3);
                }

                break;
            case R.id.img_shouquan://点击授权
                if (AssociationData.getUserAccountTypeString().equals("组织信息")) {
                    CommonUitls.showSweetDialog1(GYTHomeActivity.this, "暂未开通鸽运通服务，或您还不是公棚（协会）用户", dialog -> {
                        dialog.dismiss();
                    });
                    return;
                }
                startActivity(new Intent(GYTHomeActivity.this, AuthoriseHomeActivity.class));
                break;
        }
    }

    /**
     * 续费
     */
    private void startXufei() {
        if (AssociationData.getUserAccountTypeString().equals("组织信息")) {
            CommonUitls.showSweetDialog1(GYTHomeActivity.this, "暂未开通鸽运通服务，或您还不是公棚（协会）用户", dialog -> {
                dialog.dismiss();
            });
            return;
        }

        if (RealmUtils.getInstance().existGYTInfo()) {
            GYTService gytService = RealmUtils.getInstance().queryGTYInfo().last();
            if (gytService.getGrade().equals("-1")) {
                if (gytService.getUsefulTime() != null) {
                    //开通用户
                    //跳转到续费，充值 鸽运通服务页面
                    Intent intent = new Intent(new Intent(GYTHomeActivity.this, RenewalUpgradeActivity.class));
                    intent.putExtra("vipGrade", vipGrade);
                    intent.putExtra("endTime", tvComeDueTime.getText().toString());
                    startActivity(intent);
                } else {
                    //跳转到开通鸽运通服务页面
                    startActivity(new Intent(GYTHomeActivity.this, OpenGytActivity.class));
                }
            } else {
                //开通用户
                //跳转到续费，充值 鸽运通服务页面
                Intent intent = new Intent(new Intent(GYTHomeActivity.this, RenewalUpgradeActivity.class));
                intent.putExtra("vipGrade", vipGrade);
                intent.putExtra("endTime", tvComeDueTime.getText().toString());
                startActivity(intent);
            }
        }
    }

    private int vipGrade = -1;//1：普通用户   2：vip用户 3：svip用户

    /**
     * 获取鸽运通统计数据成功
     *
     * @param data
     */
    @Override
    public void gytStatisticalData(GYTStatisticalEntity data) {

    }

    /**
     * 获取服务数据回调
     *
     * @param dataApiResponse
     * @param msg
     */
    @Override
    public void getServiceData(ApiResponse<GYTHomeEntity> dataApiResponse, String msg) {
        try {
            if (this.isDestroyed()) {//页面销毁
                return;
            }

            if (serviceType.equals("xungetong")) {
                if (dataApiResponse.getData() != null) {
                    serviceDataInit(dataApiResponse.getData());
                } else {
                    //没有训鸽通服务数据
                    Log.d(TAG, "getServiceData: 没有训鸽通服务数据");
                }
            } else if (serviceType.equals("geyuntong")) {
                serviceDataInit(dataApiResponse.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取统计数据回调
     *
     * @param dataApiRespons
     * @param msg
     */
    @Override
    public void getStatisticalData(ApiResponse<GYTStatisticalEntity> dataApiRespons, String msg) {
        try {
            if (dataApiRespons.getErrorCode() == 0) {
                if (serviceType.equals("xungetong")) {
                    if (dataApiRespons.getData() == null) {
                        //没有开通训鸽通

                    } else {
                        statisticalDataInit(dataApiRespons.getData());
                    }

                } else if (serviceType.equals("geyuntong")) {
                    if (dataApiRespons.getData() == null) {
                        //没有开通鸽运通

                    } else {
                        statisticalDataInit(dataApiRespons.getData());
                    }
                }
            } else if (dataApiRespons.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
                errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, this, dialog -> {
                    dialog.dismiss();
                    //跳转到登录页
                    AppManager.getAppManager().startLogin(MyApplication.getContext());
                    RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计数据设置
     */
    private void statisticalDataInit(GYTStatisticalEntity data) {

        tvTotalLength.setText(DateUtils.formatSeconds(data.getTotalSeconds()));//监控总时长
        tvTotalNumber.setText(data.getMonitorRaceCount() + "次");//监控次数

        if (data.getTotalMileage() > 1000) {
            tvCenterMiles.setText("已监控里程：" + new DecimalFormat("#.000").format(data.getTotalMileage() / 1000) + "公里");//已监控总里程
        } else if (data.getTotalMileage() == 0) {
            tvCenterMiles.setText("已监控里程：0公里");
        } else {
            tvCenterMiles.setText("已监控里程：0" + new DecimalFormat("#.000").format(data.getTotalMileage() / 1000) + "公里");//已监控总里程
        }
    }

    /**
     * 服务数据初始化
     */
    private void serviceDataInit(GYTHomeEntity data) {

        if (data == null) {
            Log.d(TAG, "serviceDataInit: data==null");
            return;
        }

        if (data.getOpenTime() != null) {
            tvOpenTime.setText(data.getOpenTime());//开通时间
        }

        if (data.getExpireTime() != null) {
            tvComeDueTime.setText(data.getExpireTime());//到期时间
        }

        Log.d(TAG, "getGrade: " + data.getGrade());
        //获取用户等级
        if (serviceType.equals("xungetong")) {
            if (!data.getScores().isEmpty()) {
                tvGb.setText(data.getScores());
            }

            imgVip.setImageResource(R.mipmap.grade_gebi);
        } else {
            switch (data.getGrade()) {
                case "-1":
                    if (data.getUsefulTime() == null) {
                        //未开通鸽运通
                        vipGrade = -1;
                        if (!data.getScores().isEmpty()) {
                            tvGb.setText(data.getScores());
                        }

                        imgVip.setImageResource(R.mipmap.grade_gebi);
                        imgbtn3.setImageResource(R.mipmap.kaitongfw);
                        imgbtn2.setOnClickListener(view -> {
                            CommonUitls.showSweetDialog1(GYTHomeActivity.this, "暂未开通鸽运通服务，或您还不是公棚（协会）用户", dialog -> {
                                dialog.dismiss();
                            });
                        });

                        imgbtn4.setOnClickListener(view -> {
                            CommonUitls.showSweetDialog1(GYTHomeActivity.this, "暂未开通鸽运通服务，或您还不是公棚（协会）用户", dialog -> {
                                dialog.dismiss();
                            });
                        });
                    }
                    break;
                case "vip"://vip用户
                    Log.d(TAG, "getGrade: " + data.getGrade());
                    vipGrade = 2;
                    imgVip.setImageResource(R.mipmap.grade_vip2x);
                    break;
                case "svip"://svip用户
                    Log.d(TAG, "getGrade: " + data.getGrade());
                    vipGrade = 3;
                    imgVip.setImageResource(R.mipmap.grade_svip2x);
                    break;
                default:
                    //普通用户
                    vipGrade = 1;
                    imgVip.setImageResource(R.mipmap.grade_common);
            }
        }
    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    /**
     * 开通训鸽通信息
     *
     * @param myXGTEntity
     * @param msg
     */
    private XGTEntity myXGTEntity;

    @Override
    public void isXGTInfo(ApiResponse<XGTEntity> apiResponse, String msg) {
        try {
            if (apiResponse.getErrorCode() == 0) {
                if (apiResponse.getData() != null && apiResponse.getData().getStatuscode() == 0) {
                    isOpenXGT = true;
                }
                this.myXGTEntity = apiResponse.getData();
                XGTPresenter.initXTGView(myXGTEntity, this, imgbtn3, 1);//初始化训鸽通续费，申请开通
            } else if (apiResponse.getErrorCode() == 10010) {
                imgbtn3.setImageResource(R.mipmap.shenqingsy);
                imgbtn3.setOnClickListener(view -> CommonUitls.showSweetDialog(this, msg));
            } else {
                XGTPresenter.startShiyong(myXGTEntity, this, imgbtn3, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isUploadIdCardInfo(ApiResponse apiResponse) {

    }

    @Override
    public void getXTGOpenAndRenewInfo(List<XGTOpenAndRenewEntity> openInfo, List<XGTOpenAndRenewEntity> renewInfo, String msg) {

    }
}
