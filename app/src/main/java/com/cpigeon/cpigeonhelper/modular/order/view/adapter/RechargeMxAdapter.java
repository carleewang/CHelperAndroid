package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.modular.order.presenter.OrderPresenter;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.BalanceReChargeActivity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 充值明细适配器
 * Created by Administrator on 2017/12/21.
 */

public class RechargeMxAdapter extends BaseQuickAdapter<RechargeMxEntity, BaseViewHolder> {
    private OrderPresenter mPresenter;//控制层

    public RechargeMxAdapter(List<RechargeMxEntity> data, OrderPresenter mPresenter) {
        super(R.layout.item_new_order_details, data);
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeMxEntity item) {
        //总tiem
        LinearLayout it_list_z = helper.getView(R.id.it_list_z);

        //订单内容
        TextView it_order_content = helper.getView(R.id.it_order_content);
        it_order_content.setText("中鸽助手余额充值");

        //下单时间
        TextView it_order_time = helper.getView(R.id.it_order_time);
        it_order_time.setText(item.getTime());

        //订单编号
        TextView it_order_number = helper.getView(R.id.it_order_number);//订单编号
        it_order_number.setText(item.getNumber());

        //设置支付金额
        TextView tv_play_money = helper.getView(R.id.it_order_money);

        tv_play_money.setText(String.valueOf(item.getPrice()) + " 元/" + item.getPayway());

        //支付状态
        TextView tv_play = helper.getView(R.id.it_order_play);//已完成， 已过期
        TextView tv_ddfk = helper.getView(R.id.tv_ddfk);//等待付款
        LinearLayout it_order_zt_ll = helper.getView(R.id.it_order_zt_ll);//已完成， 已过期 父布局

        //删除订单
        it_list_z.setOnClickListener(view -> {
            CommonUitls.showSweetDialog(mContext, "您确定要删除该订单吗？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    mPresenter.subDelOrderMxItem(String.valueOf(item.getId()));
                }
            });
        });

        switch (item.getStatusname()) {
            case "充值完成":
                //设置支付金额
                tv_ddfk.setVisibility(View.GONE);
                it_order_zt_ll.setVisibility(View.VISIBLE);
                tv_play.setText("已完成");
                break;
            case "已过期":
                tv_ddfk.setVisibility(View.GONE);
                it_order_zt_ll.setVisibility(View.VISIBLE);
                tv_play.setText("已过期");
                break;
            case "待充值":
                it_order_zt_ll.setVisibility(View.GONE);
                tv_ddfk.setVisibility(View.VISIBLE);//显示前往支付
                it_list_z.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUitls.showSweetDialog(mContext, "是否继续支付", sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();

                            //跳转余额充值页面
                            Intent intent = new Intent(mContext, BalanceReChargeActivity.class);
                            intent.putExtra("tag", 2);//标记（不用创建订单）
                            intent.putExtra("item", item);//订单详情
                            mContext.startActivity(intent);
                        });
                    }
                });
                break;
        }
    }
}
