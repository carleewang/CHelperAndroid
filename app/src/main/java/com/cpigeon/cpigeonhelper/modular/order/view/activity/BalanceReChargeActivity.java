package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.utils.CashierInputFilter;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.OrderUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;


/**
 * 支付界面
 * 微信
 * <p>
 * Created by Administrator on 2017/7/7.
 */

public class BalanceReChargeActivity extends ToolbarBaseActivity {
    @BindView(R.id.et_money_income_number)
    EditText etMoneyIncomeNumber;
    @BindView(R.id.tv_pay_way_tips)
    TextView tvPayWayTips;
    @BindView(R.id.rl_wxpay)
    RelativeLayout rlWxpay;
    @BindView(R.id.cb_order_protocol_income)
    CheckBox cbOrderProtocolIncome;
    @BindView(R.id.tv_order_protocol_income)
    TextView tvOrderProtocolIncome;
    @BindView(R.id.btn_ok_income)
    Button btnOkIncome;
    private int currPayway = -1;
    private IWXAPI mWxApi = null;
    private PayReq payReq;

    private CommonUitls.OnWxPayListener onWxPayListenerWeakReference; //微信支付添加回调监听
    private RechargeMxEntity item;//已有订单

    @Override
    protected void swipeBack() {
//        Slidr.attach(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_balance_recharge;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("余额充值");
        setTopLeftButton(R.drawable.ic_back, this::finish);
        if (mWxApi == null) {
            mWxApi = WXAPIFactory.createWXAPI(mContext, null);
            mWxApi.registerApp(WXPayEntryActivity.APP_ID);
        }

        //微信支付添加回调监听
        onWxPayListenerWeakReference = CommonUitls.onWxPayListenerWeakReference(this, 1);
        CommonUitls.getInstance().addOnWxPayListener(onWxPayListenerWeakReference);

        chosePayWayMoney(1);
        etMoneyIncomeNumber.setFilters(new InputFilter[]{new CashierInputFilter()});
        etMoneyIncomeNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chosePayWayMoney(currPayway);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //已有订单
        item = (RechargeMxEntity) getIntent().getSerializableExtra("item");
        if (item != null) {
            etMoneyIncomeNumber.setText("" + item.getPrice());
        }
    }

    private void chosePayWayMoney(int type) {
        if (currPayway != type)
            currPayway = type;
        showTip();
    }

    private void showTip() {
        double fee;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        try {
            fee = Double.valueOf(etMoneyIncomeNumber.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fee = 0;
        }
        if (fee == 0) {
            tvPayWayTips.setVisibility(View.INVISIBLE);
        } else if (currPayway == 1) {
            SpannableStringBuilder builder = new SpannableStringBuilder(String.format("需要收取1%%的手续费，实际支付%.2f元", OrderUtil.getTotalFee(fee, 0.01)));
            builder.setSpan(redSpan, 15,
                    15 + String.format("%.2f", OrderUtil.getTotalFee(fee, 0.01)).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvPayWayTips.setText(builder);
            tvPayWayTips.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_order_protocol_income, R.id.btn_ok_income})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_order_protocol_income:
                //阅读协议
                Intent intent = new Intent(BalanceReChargeActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.PLAY_TYPE);
                startActivity(intent);
                break;
            case R.id.btn_ok_income://hl  确定支付按钮
                if (TextUtils.isEmpty(etMoneyIncomeNumber.getText().toString().trim())) {
                    CommonUitls.showToast(this, "充值金额必须大于0.01");
                    return;
                }
                if (!cbOrderProtocolIncome.isChecked()) {//hl 是否选中同意协议
                    CommonUitls.showToast(this, "请阅读中鸽网支付协议");
                } else {
                    if (item == null) {
                        zjPlay();//需要创建订单
                    } else {
                        zdPlay(item.getId());
                    }

                }
                break;
        }
    }

    long timestamp = System.currentTimeMillis() / 1000;//hl 获取当前系统时间

    /**
     * 直接充值
     */
    private void zjPlay() {
        RequestBody mRequestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))
                .addFormDataPart("m", etMoneyIncomeNumber.getText().toString().trim())
                .build();
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));
        postParams.put("m", etMoneyIncomeNumber.getText().toString().trim());
        RetrofitHelper.getApi()
                .createRechargeOrder(AssociationData.getUserToken(), mRequestBody, timestamp,
                        CommonUitls.getApiSign(timestamp, postParams))
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderApiResponse -> {
                    if (orderApiResponse.getErrorCode() == 0) {
                        Log.d("print", "onViewClicked: 创建充值订单完成，请求服务器进行微信预支付订单的获取");
                        zdPlay(orderApiResponse.getData().getId());
                    }
                }, throwable -> {
                    //抛出网络异常相关
                    CommonUitls.exceptionHandling(this, throwable);
                });
    }

    /**
     * 账单充值
     */
    private void zdPlay(int id) {
        Log.d("print", "onViewClicked: 创建充值订单完成，请求服务器进行微信预支付订单的获取");
        //创建充值订单完成，请求服务器进行微信预支付订单的获取
        RequestBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))
                .addFormDataPart("did", String.valueOf(id))
                .addFormDataPart("t", "android")
                .addFormDataPart("app", "cpigeonhelper")
                .build();

        Map<String, Object> pstParams = new HashMap<>();
        pstParams.put("uid", String.valueOf(AssociationData.getUserId()));
        pstParams.put("did", String.valueOf(id));
        pstParams.put("t", "android");
        pstParams.put("app", "cpigeonhelper");

        //请求服务器进行微信预支付订单的获取
        RetrofitHelper.getApi()
                .getWXPrePayOrderForRecharge(AssociationData.getUserToken(), mBody,
                        timestamp, CommonUitls.getApiSign(timestamp, pstParams))
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(payRequestApiResponse -> {
                    if (payRequestApiResponse.getErrorCode() == 0) {
                        Log.d("print", "onViewClicked: 请求服务器进行微信预支付订单的获取");
                        if (mWxApi.isWXAppInstalled()) {
                            if (payReq != null) {//hl  支付申请
                                entryWXPay(payReq);
                                Log.d("print", "onViewClicked: payReq!=null");
                                return;
                            } else
                                Log.d("print", "onViewClicked: payReq==null     " + payRequestApiResponse.getData());
                            mWxApi.sendReq(payRequestApiResponse.getData().getWxPayReq());

                        } else {
                            Log.d("print", "onViewClicked: 支付失败");
                        }
                    } else {
                        Log.d("print", "onViewClicked: 请求失败");
                    }
                }, throwable -> {
                    //抛出网络异常相关
                    CommonUitls.exceptionHandling(this, throwable);
                });
    }

    private void entryWXPay(PayReq payReq) {
        this.payReq = payReq;
        if (mWxApi != null) {
            boolean result = mWxApi.sendReq(payReq);
            if (!result) {
                CommonUitls.showToast(this, "支付失败");
            } else {
                Logger.d("发起微信支付");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWxApi = null;
        payReq = null;
        CommonUitls.getInstance().removeOnWxPayListener(onWxPayListenerWeakReference);
    }

}
