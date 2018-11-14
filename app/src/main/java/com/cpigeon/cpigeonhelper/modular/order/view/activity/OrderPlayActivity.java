package com.cpigeon.cpigeonhelper.modular.order.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.ToolbarBaseActivity;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.NeedInvoice;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.Order;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayWxRequest;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter2;
import com.cpigeon.cpigeonhelper.modular.order.view.fragment.PayPwdInputFragment;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.UserInfoPresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.ui.PayFragment;
import com.cpigeon.cpigeonhelper.ui.PayPwdView;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.OrderUtil;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ErrorCodeService;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;
import com.cpigeon.cpigeonhelper.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 订单支付页面
 * Created by Administrator on 2017/10/27.
 */

public class OrderPlayActivity extends ToolbarBaseActivity implements PayPwdView.InputCallBack, UserInfoView {

    @BindView(R.id.tv_play_order_number)
    TextView tvPlayOrderNumber;//订单编号
    @BindView(R.id.tv_play_order_content)
    TextView tvPlayOrderContent;//订单内容
    @BindView(R.id.tv_place_order_time)
    TextView tvPlaceOrderTime;//下单时间
    @BindView(R.id.tv_play_money)
    TextView tvPlayMoney;//支付金额
    @BindView(R.id.tv_play_countdown)
    TextView playCountdown;//支付剩余时间
    @BindView(R.id.cb_order_protocol)
    CheckBox cbConsentPro;//同意支付按钮
    @BindView(R.id.tv_order_protocol)
    TextView tvProtocol;//支付协议
    @BindView(R.id.balance_play_ll)
    LinearLayout balancePlayLl;//余额支付
    @BindView(R.id.wx_play_ll)
    LinearLayout wxPlayLl;//微信支付
    @BindView(R.id.gb_play_ll)
    LinearLayout gbPlayLl;//鸽币兑换

    @BindView(R.id.tv_wx_sxf)
    TextView tvSXF;//微信手续费

    @BindView(R.id.ll_wyfp)
    LinearLayout ll_wyfp;//我要发票
    @BindView(R.id.cb_invoice)
    CheckBox cb_invoice;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static int playResultsTag = -1;// 1  支付成功   -1 未成功

    private int sid;//鸽运通服务 ID
    private String type;//订单类型 标记开通、升级、续费【open，upgrade，renewal】

    private OrderPresenter presenter;//控制层
    private PayFragment fragment;//输入交易密码弹出窗口
    private String xgtplay;//当前为训鸽通续费

    private UserInfoPresenter mUserInfoPresenter;//获取用户余额，鸽币
    private String userMoney, userGebi;//用户余额，用户鸽币


    private int playTag = -1;//支付类型标志， 3  余额支付  4 鸽币支付

    private CommonUitls.OnWxPayListener onWxPayListenerWeakReference;//微信回调监听

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_play;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
        playResultsTag = -1;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("订单支付");
        setTopLeftButton(R.drawable.ic_back, OrderPlayActivity.this::finish);
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        presenter = new OrderPresenter(new OrderViewImpl() {
            /**
             * 获取订单成功回调
             */
            @Override
            public void getOrderData(Order order) {
                tvPlayOrderNumber.setText(order.getNumber());//订单编号
                tvPlayOrderContent.setText(order.getItem());//订单内容
                tvPlaceOrderTime.setText(order.getTime());//下单时间
                if (order.getScores() > 0) {
                    playMoney = String.valueOf(order.getPrice());
                    playIntegration = String.valueOf(order.getScores());
                    tvSXF.setText("" + OrderUtil.getHandlingCharge(order.getPrice(), 0.01));//手续费
                    tvPlayMoney.setText(String.valueOf(order.getPrice()) + "元（" + order.getScores() + "鸽币）");//支付金额
                    gbPlayLl.setVisibility(View.VISIBLE);//存在鸽币支付内容，显示鸽币支付
                } else {
                    playMoney = String.valueOf(order.getPrice());
                    tvSXF.setText("" + OrderUtil.getHandlingCharge(order.getPrice(), 0.01));//手续费
                    tvPlayMoney.setText(String.valueOf(order.getPrice()) + "元");//支付金额
                }
                oid = order.getId();
                //开始倒计时
                thread = OrderPresenter2.startCountdown(thread, OrderPlayActivity.this, DateUtils.DataToMs(order.getTime()), playCountdown);

                try {
                    //订单发票显示初始化
                    if (Integer.valueOf(order.getFpid()) > 0) {
                        cb_invoice.setChecked(true);
                        OrderPlayActivity.this.mNeedInvoice = new NeedInvoice.Builder().id(order.getFpid()).isNeedInvoice(true).build();
                    } else {
                        cb_invoice.setChecked(false);
                        OrderPlayActivity.this.mNeedInvoice = new NeedInvoice.Builder().id("-1").isNeedInvoice(false).build();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

//                startCountdown(DateUtils.DataToMs(order.getTime()));
            }

            /**
             * 余额支付失败
             */
            @Override
            public void playFail(String msg) {
                showDialog("余额支付失败:" + msg);
            }

            /**
             * 鸽币支付成功
             */
            @Override
            public void playGbSucceed() {
                playResultsTag = 1;
                //发布事件（订单列表刷新数据）
                EventBus.getDefault().post(EventBusService.ORDER_REFRESH);
                showDialog("鸽币支付成功");
            }

            /**
             * 鸽币支付失败
             */
            @Override
            public void playGbFail(String msg) {
                showDialog("鸽币支付失败:" + msg);
            }

            //微信预支付订单创建成功
            @Override
            public void playWXPreSucceed(PayWxRequest payWxRequest) {
                entryWXPay(payWxRequest.getWxPayReq());//开始微信支付
            }

            /**
             * 余额订单支付成功
             */
            @Override
            public void playSucceed() {
                playResultsTag = 1;
                //发布事件（订单列表刷新数据）
                EventBus.getDefault().post(EventBusService.ORDER_REFRESH);
                showDialog("余额支付成功");
            }

            //错误提示
            @Override
            public void getErrorNews(String str) {
                OrderPlayActivity.this.getErrorNews(str);
            }

            //异常信息提示
            @Override
            public void getThrowable(Throwable throwable) {
                OrderPlayActivity.this.getThrowable(throwable);
            }

            @Override
            public void getDatas(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mThrowable != null) {
                        OrderPlayActivity.this.getThrowable(mThrowable);
                        return;
                    }

                    if (listApiResponse.getErrorCode() == 0) {
                        EventBus.getDefault().post(EventBusService.ORDER_REFRESH);
                        if (cb_invoice.isChecked()) {
                            cb_invoice.setChecked(false);
                        } else {
                            cb_invoice.setChecked(true);
                        }
                    } else {
                        OrderPlayActivity.this.getErrorNews(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });//初始化控制层

        xgtplay = getIntent().getStringExtra("xgtplay");

        //鸽运通支付
        switch (getIntent().getIntExtra("tag", -1)) {
            case 3: //训鸽通开通
                if (xgtplay != null) {
                    gbPlayLl.setVisibility(View.VISIBLE);//鸽币兑换设置为显示
                    presenter.getXGTOrder(35, "renewal");//open（开通）|renewal（续费）  sid=35
                }
                break;
            case 2://订单详情，继续支付
                continuePlay();    //继续支付
                break;
            case 1://创建鸽运通订单
                sid = getIntent().getIntExtra("sid", -1);//鸽运通服务(订单) ID
                type = getIntent().getStringExtra("type");//订单类型 标记开通、升级、续费【open，upgrade，renewal】
                if (sid != -1 && !type.isEmpty()) {
                    presenter.createGYTOrder(sid, type);
                }
                break;
            case 4://赛鸽通开通  创建订单
                presenter.createSGTOrder(getIntent().getIntExtra("sid", -1));
                break;

            case 5://创建统一订单
                presenter.getCreateServiceOrder(getIntent().getIntExtra("sid", -1));
                break;
            default:
        }

        mUserInfoPresenter = new UserInfoPresenter(this);//初始化控制层
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUserInfoPresenter.getMyInfo();//获取我的信息
            }
        }, 300);


        if (mWxApi == null) {//初始化微信api
            mWxApi = WXAPIFactory.createWXAPI(mContext, null);
            mWxApi.registerApp(WXPayEntryActivity.APP_ID);
        }

        onWxPayListenerWeakReference = CommonUitls.onWxPayListenerWeakReference(this, 2);//微信支付回调监听
        CommonUitls.getInstance().addOnWxPayListener(onWxPayListenerWeakReference);
    }

    //继续支付
    private void continuePlay() {
        OrderList item = (OrderList) getIntent().getSerializableExtra("item");
        if (item != null) {
            tvPlayOrderNumber.setText(item.getDdbh());//订单编号
            tvPlayOrderContent.setText(item.getDdnr());//订单内容
            tvPlaceOrderTime.setText(item.getDdsj());//下单时间

            if (item.getDdgb() > 0) {
                //订单有鸽币
                playMoney = String.valueOf(item.getDdje());//保存支付现金
                playIntegration = String.valueOf(item.getDdgb());//保存支付鸽币

                gbPlayLl.setVisibility(View.VISIBLE);
                tvSXF.setText("" + OrderUtil.getHandlingCharge(item.getDdje(), 0.01));//手续费
                tvPlayMoney.setText(String.valueOf(item.getDdje()) + " 元/" + item.getDdgb() + "鸽币");
            } else {
                //订单只有金额
                tvSXF.setText("" + OrderUtil.getHandlingCharge(item.getDdje(), 0.01));//手续费
                playMoney = String.valueOf(item.getDdje());//保存支付现金
                tvPlayMoney.setText(String.valueOf(item.getDdje()) + " 元");
            }
            oid = item.getDdid();//支付id

            //订单发票显示初始化
            if (Integer.valueOf(item.getDdfp()) > 0) {
                cb_invoice.setChecked(true);
                this.mNeedInvoice = new NeedInvoice.Builder().id(item.getDdfp()).isNeedInvoice(true).build();
            } else {
                cb_invoice.setChecked(false);
                this.mNeedInvoice = new NeedInvoice.Builder().id("-1").isNeedInvoice(false).build();
            }

            //开始倒计时
            thread = OrderPresenter2.startCountdown(thread, OrderPlayActivity.this, DateUtils.DataToMs(item.getDdsj()), playCountdown);
//            startCountdown(DateUtils.DataToMs(item.getDdsj()));
        }
    }

    private long timePoor;
    private int oid = -1;
    private String playMoney = "-1", playIntegration = "-1";//支付金额，鸽币

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册

        mWxApi = null;
        CommonUitls.getInstance().removeOnWxPayListener(onWxPayListenerWeakReference);

        if (thread != null && thread.isAlive()) {//停止线程
            thread.interrupt();
            thread = null;
        }
    }

    private Thread thread;

    @OnClick({R.id.tv_order_protocol, R.id.balance_play_ll, R.id.wx_play_ll, R.id.gb_play_ll, R.id.ll_wyfp})
    public void onViewClicked(View view) {

        if (AntiShake.getInstance().check()) {//防止点击过快
            return;
        }
        switch (view.getId()) {
            case R.id.tv_order_protocol://阅读协议
                Intent intent = new Intent(OrderPlayActivity.this, WebViewActivity.class);
                intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.PLAY_TYPE);
                startActivity(intent);
                break;
            case R.id.balance_play_ll://余额支付
                playTag = 3;//余额支付
                loadPayFragment(playMoney + "元");//多位密码弹出框
                break;
            case R.id.wx_play_ll://微信支付
                if (!cbConsentPro.isChecked()) {
                    CommonUitls.showSweetDialog(this, "请阅读并同意《中鸽网支付协议》");
                    return;
                }
                presenter.playWXPreOrder(String.valueOf(oid));//创建微信预支付订单
                break;
            case R.id.gb_play_ll://鸽币兑换
                playTag = 4;//鸽币兑换
                loadPayFragment(playIntegration + "鸽币");//多位密码弹出框
                break;

            case R.id.ll_wyfp:
                if (cb_invoice.isChecked()) {
                    presenter.setUser_Invoice_Bind(oid, mNeedInvoice.getId(), false);
                } else {
                    intent = new Intent(this, InvoiceListActivity.class);
//                intent = new Intent(this, InvoiceSetActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }

    /**
     * 多位数密码弹出提示框Fragment
     */
    private PayPwdInputFragment payFragment;

    private void loadPayFragment(String playStr) {
        if (!cbConsentPro.isChecked()) {
            CommonUitls.showSweetDialog(this, "请阅读并同意《中鸽网支付协议》");
            return;
        }

        payFragment = new PayPwdInputFragment();
        payFragment.setOnPayListener(new PayPwdInputFragment.OnPayListener() {
            @Override
            public void onPay(Dialog dialog, String payPwd) {
                switch (playTag) {
                    case 3:
                        //余额支付
                        if (Double.valueOf(playMoney) > Double.valueOf(userMoney)) {
                            payFragment.dismiss();
                            CommonUitls.showSweetDialog(OrderPlayActivity.this, "当前余额不足，无法支付");
                            return;
                        }
                        presenter.orderPayByBalance(String.valueOf(oid), payPwd);
                        break;
                    case 4:
                        //鸽币支付
                        if (Double.valueOf(playIntegration) > Double.valueOf(userGebi)) {
                            payFragment.dismiss();
                            CommonUitls.showSweetDialog(OrderPlayActivity.this, "当前鸽币不足，无法支付");
                            return;
                        }

                        if (playIntegration != null) {
                            if (xgtplay != null) {
                                //训鸽通鸽币支付
                                presenter.orderPayGb_XGT(String.valueOf(oid), payPwd);
                            } else {
                                //鸽运通鸽币支付
                                presenter.orderPayGb_GYT(String.valueOf(oid), payPwd);
                            }
                        }
                        break;
                }
            }
        });

        payFragment.setOkText("支付");
        //设置信息提示（余额鸽币提示）
        switch (playTag) {
            case 3:
                //余额支付
                payFragment.setPromptInfo("支付金额:" + playStr + ",当前余额:" + userMoney + "元");
                break;
            case 4:
                //鸽币支付
                payFragment.setPromptInfo("支付鸽币:" + playStr + ",当前鸽币:" + userGebi + "鸽币");
                break;
        }

        payFragment.show(getFragmentManager(), "payFragment");
    }

    /**
     * 6位输入输入密码（输入完最后一位回调）
     */
    @Override
    public void onInputFinish(String result) {
        fragment.dismiss();
        switch (playTag) {
            case 3:
                //余额支付
                presenter.orderPayByBalance(String.valueOf(oid), result);
                break;
            case 4:
                //鸽币支付
                if (playIntegration != null) {
                    if (xgtplay != null) {
                        //训鸽通鸽币支付
                        presenter.orderPayGb_XGT(String.valueOf(oid), result);
                    } else {
                        //鸽运通鸽币支付
                        presenter.orderPayGb_GYT(String.valueOf(oid), result);
                    }
                }
                break;
        }
    }

    /**
     * 弹出提示
     */
    private void showDialog(String dialogMsg) {
        if (payFragment != null) {
            payFragment.dismiss();
        }

        if (dialogMsg.indexOf("成功") != -1) {
            SweetAlertDialog mSweetAlertDialog = CommonUitls.showSweetDialog1(this, dialogMsg, dialog -> {
                dialog.dismiss();
                AppManager.getAppManager().killActivity(mWeakReference);
            });
            mSweetAlertDialog.setCancelable(false);
        } else {
            CommonUitls.showSweetDialog(this, dialogMsg);
        }
    }

    private IWXAPI mWxApi;

    private void entryWXPay(PayReq payReq) {

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
     * 获取我的余额，我的鸽币回调
     */
    @Override
    public void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg) {
        if (myInfoApiResponse.getErrorCode() == 0) {
            userMoney = myInfoApiResponse.getData().getMoney();
            userGebi = myInfoApiResponse.getData().getGebi();
        } else if (myInfoApiResponse.getErrorCode() == ErrorCodeService.LOG_SERVICE) {
            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, msg, OrderPlayActivity.this, dialog -> {
                dialog.dismiss();
                //跳转到登录页
                AppManager.getAppManager().startLogin(MyApplication.getContext());
                RealmUtils.getInstance().deleteUserAllInfo();//删除所有用户资料
            });
        }
    }

    @Override
    public void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg) {

    }

    private NeedInvoice mNeedInvoice;

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(NeedInvoice mNeedInvoice) {
        this.mNeedInvoice = mNeedInvoice;
        presenter.setUser_Invoice_Bind(oid, mNeedInvoice.getId(), mNeedInvoice.isNeedInvoice());
    }

}
