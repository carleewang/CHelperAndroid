package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.OrderInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.OrderPayFragment;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 订单列表适配器
 * Created by Administrator on 2017/10/30.
 */
public class OrderNewDetailsAdapter extends BaseQuickAdapter<OrderList, BaseViewHolder> {

    private OrderPresenter mPresenter;//控制层
    private Intent intent;

    public OrderNewDetailsAdapter(List<OrderList> data, OrderPresenter mPresenter) {
        super(R.layout.item_new_order_details, data);
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderList item) {

        //总tiem
        LinearLayout it_list_z = helper.getView(R.id.it_list_z);

        //订单内容
        TextView it_order_content = helper.getView(R.id.it_order_content);
        it_order_content.setText(item.getDdnr());

        //下单时间
        TextView it_order_time = helper.getView(R.id.it_order_time);
        it_order_time.setText(item.getDdsj());

        //订单编号
        TextView it_order_number = helper.getView(R.id.it_order_number);//订单编号
        it_order_number.setText(item.getDdbh());

        //支付状态
        TextView tv_play = helper.getView(R.id.it_order_play);//已完成， 已过期
        tv_play.setTextColor(mContext.getResources().getColor(R.color.color_999999));
        TextView tv_ddfk = helper.getView(R.id.tv_ddfk);//等待付款
        ImageButton it_imgbtn_del = helper.getView(R.id.it_imgbtn_del);//删除订单
        LinearLayout it_order_zt_ll = helper.getView(R.id.it_order_zt_ll);//已完成， 已过期 父布局

        //设置支付金额
        TextView tv_play_money = helper.getView(R.id.it_order_money);
        String palyStr = "";

        //有金额，没鸽币
        if (item.getDdje() > 0 && item.getDdgb() == 0) {
            palyStr = String.valueOf(item.getDdje()) + " 元";
        }

        //有鸽币，没金额
        if (item.getDdje() == 0 && item.getDdgb() > 0) {
            palyStr = item.getDdgb() + "鸽币";
        }

        //有鸽币，有金额
        if (item.getDdje() > 0 && item.getDdgb() > 0) {
            palyStr = String.valueOf(item.getDdje()) + " 元/" + item.getDdgb() + "鸽币";
        }

        if (!item.getDdtype().isEmpty()) {
            palyStr += ("(" + item.getDdtype() + ")");
        }

        tv_play_money.setText(palyStr);

        //删除订单
        it_list_z.setOnClickListener(view -> {
            CommonUitls.showSweetDialog(mContext, "您确定要删除该订单吗？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    mPresenter.subDelOrderItem(String.valueOf(item.getDdid()));
                }
            });
        });

        switch (item.getDdzt()) {
            case 1:
                //设置支付金额
                tv_ddfk.setVisibility(View.GONE);
                it_order_zt_ll.setVisibility(View.VISIBLE);
                tv_play.setText("交易完成");
                tv_play.setTextColor(mContext.getResources().getColor(R.color.color_cpigeonapp_theme));
                break;
            case 2:
                tv_ddfk.setVisibility(View.GONE);
                it_order_zt_ll.setVisibility(View.VISIBLE);
                tv_play.setText("交易过期");
                break;
            case 0:
                it_order_zt_ll.setVisibility(View.GONE);
                tv_ddfk.setVisibility(View.VISIBLE);//显示前往支付
                it_list_z.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!item.getDdtype().isEmpty() && item.getDdtype().indexOf("中鸽助手") != -1 && item.getDdly().equals("android")) {
                            SweetAlertDialogUtil.showDialog3(null, "是否继续支付", (Activity) mContext, sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                if (item.getDdnr().indexOf(mContext.getString(R.string.str_sms)) != -1) {

                                    OrderInfoEntity mOrderInfoEntity = new OrderInfoEntity();
                                    mOrderInfoEntity.id = item.getDdid() + "";
                                    mOrderInfoEntity.time = item.getDdsj() + "";
                                    mOrderInfoEntity.number = item.getDdbh() + "";
                                    mOrderInfoEntity.item = item.getDdnr() + "";
                                    mOrderInfoEntity.price = item.getDdje() + "";
                                    mOrderInfoEntity.scores = item.getDdgb() + "";
                                    mOrderInfoEntity.fpid = item.getDdfp();

                                    IntentBuilder.Builder()
                                            .putExtra(IntentBuilder.KEY_DATA, mOrderInfoEntity)
                                            .startParentActivity((Activity) mContext, OrderPayFragment.class);
                                } else {
                                    //跳转支付页面 hl
                                    intent = new Intent(mContext, OrderPlayActivity.class);
                                    if (item.getDdnr().indexOf("训鸽通") != -1) {
                                        intent.putExtra("xgtplay", "xgtplay");
                                    }
                                    intent.putExtra("tag", 2);//标记（不用创建订单）
                                    intent.putExtra("item", item);//订单详情
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {
                            String strHint = "";
                            if (item.getDdtype().indexOf("中鸽网") != -1) {
                                strHint = "中鸽网订单请在中鸽网App上支付";
                            } else if (item.getDdtype().indexOf("天下鸽谱") != -1) {
                                strHint = "天下鸽谱订单请在天下鸽谱App上支付";
                            } else if (item.getDdtype().indexOf("网页") != -1) {
                                strHint = "网页订单请在网页上支付";
                            } else if (item.getDdtype().indexOf("中鸽助手") != -1 && item.getDdly().equals("ios")) {
                                strHint = "请在中鸽助手ios版本上支付";
                            } else {
                                strHint = "未知的订单类型";
                            }

                            SweetAlertDialogUtil.showSweetDialog(null, mContext, strHint, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                        }
                    }
                });
                break;
        }
    }
}
