package com.cpigeon.cpigeonhelper.message.ui.order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity;
import com.cpigeon.cpigeonhelper.entity.OrderInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseMVPFragment;
import com.cpigeon.cpigeonhelper.message.event.WXPayResultEvent;
import com.cpigeon.cpigeonhelper.message.ui.BaseWebViewActivity;
import com.cpigeon.cpigeonhelper.message.ui.home.GXTUserInfoEvent;
import com.cpigeon.cpigeonhelper.message.ui.order.OderInfoViewHolder;
import com.cpigeon.cpigeonhelper.message.ui.order.adpter.OrderPayAdapter;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.presenter.PayOrderPre;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.NeedInvoice;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter2;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.InvoiceListActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderViewImpl;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.PlaySMSvalidationActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.RxUtils;
import com.cpigeon.cpigeonhelper.utils.SendWX;
import com.cpigeon.cpigeonhelper.utils.StringUtil;
import com.cpigeon.cpigeonhelper.utils.service.EventBusService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class OrderPayFragment extends BaseMVPFragment<PayOrderPre> {

    RecyclerView recyclerView;
    OrderPayAdapter adapter;
    OderInfoViewHolder holder;
    TextView tvPay;
    EditText edPassword;

    String type;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_order_pay_layout;
//        return R.layout.fragment_recyclerview_layout;
    }

    @Override
    protected PayOrderPre initPresenter() {
        return new PayOrderPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        type = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);

        mOrderPresenter = new OrderPresenter(new OrderViewImpl() {
            @Override
            public void getDatas(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mThrowable != null) {
                        OrderPayFragment.this.error(mThrowable.getLocalizedMessage());
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
                        OrderPayFragment.this.error(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        initView();
    }

    protected void initView() {
        setTitle("订单支付");
        initHeadView();
        bindHeadData();
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderPayAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setNewData(Lists.newArrayList("余额支付", "微信支付"));

//        adapter.addHeaderView(initHeadView());

        adapter.setOnItemClickListener((adapter1, view, position) -> {

            if (checkBox.isChecked()) {
                if (position == 0) {
                    mPresenter.getUserBalance(userBalanceEntity -> {
                        showPayDialog(userBalanceEntity.money, mPresenter.orderInfoEntity.price);
                        bindUi(RxUtils.textChanges(edPassword), mPresenter.setPassword());
                    });

                } else {
                    showLoading("正在创建订单");
                    mPresenter.getWXOrder(weiXinPayEntity -> {
                        hideLoading();
                        SendWX sendWX = new SendWX(getActivity());
                        sendWX.payWeiXin(weiXinPayEntity.getPayReq());
                    });
                }
            } else {
                error("请同意中鸽网支付协议");
            }

        });
    }

//    private View initHeadView() {
//        View head = LayoutInflater.from(getContext()).inflate(R.layout.item_order_info_head_layout, recyclerView, false);
//        holder = new OderInfoViewHolder(head);
//        holder.visibleBottom();
//
//        holder.tvProtocol.setOnClickListener(v -> {
//
//            IntentBuilder.Builder(getActivity(), BaseWebViewActivity.class)
//                    .putExtra(IntentBuilder.KEY_DATA, ApiConstants.BASE_URL + "/APP/Protocol?type=pay")
//                    .putExtra(IntentBuilder.KEY_TITLE, "中鸽网支付协议")
//                    .startActivity();
//
//
//        });
//        return holder.itemView;
//    }

    public TextView orderId;
    public TextView orderContent;
    public TextView orderTime;
    public TextView orderPrice;
    public LinearLayout llBottom;
    public TextView tvPlayCountdown;//支付倒计时
    //    public TextView orderPayText;
    public LinearLayout ll_wyfp;
    public CheckBox cb_invoice;

    public CheckBox checkBox;
    public TextView tvProtocol;

    private void initHeadView() {

        orderId = findViewById(R.id.tv_order_number_content);
        orderContent = findViewById(R.id.tv_order_name_content);
        orderTime = findViewById(R.id.tv_order_time_content);
        orderPrice = findViewById(R.id.tv_order_price_content);
        llBottom = findViewById(R.id.ll1);
//        orderPayText = findViewById(R.id.text1);

        checkBox = findViewById(R.id.cb_order_protocol);
        tvProtocol = findViewById(R.id.tv_order_protocol);

        tvPlayCountdown = findViewById(R.id.tv_play_countdown);

//        llBottom.setVisibility(View.VISIBLE);
//        orderPayText.setVisibility(View.VISIBLE);
        tvProtocol.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(EXTRA_URL, ApiConstants.BASE_URL + ApiConstants.PLAY_TYPE);
            startActivity(intent);
        });

        //我要发票
        ll_wyfp = findViewById(R.id.ll_wyfp);
        cb_invoice = findViewById(R.id.cb_invoice);
        ll_wyfp.setOnClickListener(view -> {
            if (cb_invoice.isChecked()) {
                mOrderPresenter.setUser_Invoice_Bind(oid, mNeedInvoice.getId(), false);
            } else {
                intent = new Intent(getActivity(), InvoiceListActivity.class);
//                intent = new Intent(this, InvoiceSetActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void bindHeadData() {
//        holder.bindData(mPresenter.orderInfoEntity);
        OrderInfoEntity entity = mPresenter.orderInfoEntity;
        orderId.setText(entity.number);
        orderContent.setText(entity.item);
        orderTime.setText(entity.time);
        orderPrice.setText(entity.price + "元" + "   (微信手续费" + StringUtil.twoPoint(Double.parseDouble(entity.price) * 0.01) + "元)");


        //订单发票显示初始化
        if (Integer.valueOf(entity.fpid) > 0) {
            cb_invoice.setChecked(true);
            this.mNeedInvoice = new NeedInvoice.Builder().id(entity.fpid).isNeedInvoice(true).build();
        } else {
            cb_invoice.setChecked(false);
            this.mNeedInvoice = new NeedInvoice.Builder().id("-1").isNeedInvoice(false).build();
        }

        oid = Integer.parseInt(entity.id);
        thread = OrderPresenter2.startCountdown(thread, getActivity(), DateUtils.DataToMs(entity.time), tvPlayCountdown);
    }

    protected void showPayDialog(String balance, String price) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = View.inflate(getContext(), R.layout.dialogfragment_pay, null);

        ImageView colse = findViewById(view, R.id.iv_pay_close);
        TextView title = findViewById(view, R.id.tv_yue_prompt);
        edPassword = findViewById(view, R.id.et_paypwd);
        tvPay = findViewById(view, R.id.tv_pay);
        TextView meanPassword = findViewById(view, R.id.tv_mean_for_paypwd);
        TextView forgotPassword = findViewById(view, R.id.tv_forget_paypwd);

        bindUi(RxUtils.textChanges(edPassword), mPresenter.setPassword());

        title.setText(getString(R.string.format_pay_account_balance_tips_2, balance, price));

        colse.setOnClickListener(v -> {
            dialog.dismiss();
        });

        tvPay.setOnClickListener(v -> {
            dialog.dismiss();
            payByBalance();
        });

        meanPassword.setOnClickListener(v -> {
            String url = ApiConstants.BASE_URL + "/APP/Help?type=help&id=172";

            IntentBuilder.Builder(getActivity(), BaseWebViewActivity.class)
                    .putExtra(IntentBuilder.KEY_DATA, url)
                    .putExtra(IntentBuilder.KEY_TITLE, "中鸽网支付协议")
                    .startActivity();
        });

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PlaySMSvalidationActivity.class));
        });


        dialog.setContentView(view);
        dialog.show();
    }

    private void payByBalance() {
        mPresenter.payOrderByBalance(r -> {
            if (r.status) {
//                DialogUtils.createDialog(getContext(), r.msg, sweetAlertDialog -> {
//                    sweetAlertDialog.dismiss();
//                    finish();
//                });

                EventBus.getDefault().post(new GXTUserInfoEvent());

                CommonUitls.showSweetDialog2(getActivity(), r.msg, dialog -> {
                    dialog.dismiss();
                    finish();
                });
            } else {
                error(r.msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WXPayResultEvent event) {
        if (WXPayResultEvent.CODE_OK == event.code) {
//            DialogUtils.createDialogWithLeft(getContext(), "支付成功", sweetAlertDialog -> {
//                sweetAlertDialog.dismiss();
//                finish();
//            });


            CommonUitls.showSweetDialog2(getActivity(), "支付成功", dialog -> {
                dialog.dismiss();
                finish();
            });
        } else if (WXPayResultEvent.CODE_ERROR == event.code) {
//            ToastUtil.showLongToast(getContext(), "支付失败");

            CommonUitls.showSweetDialog1(getActivity(), "支付失败", dialog -> {
                dialog.dismiss();
            });

        } else {
//            ToastUtil.showLongToast(getContext(), "取消支付");

            CommonUitls.showSweetDialog1(getActivity(), "取消支付", dialog -> {
                dialog.dismiss();
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (thread != null && thread.isAlive()) {//停止线程
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }

    /**
     * 开始倒计时
     * @param orderTime 下单时间
     */
    private Thread thread;
    private NeedInvoice mNeedInvoice;
    private Intent intent;
    private OrderPresenter mOrderPresenter;
    private int oid = -1;

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(NeedInvoice mNeedInvoice) {
        this.mNeedInvoice = mNeedInvoice;
        mOrderPresenter.setUser_Invoice_Bind(oid, mNeedInvoice.getId(), mNeedInvoice.isNeedInvoice());
    }
}