package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.NeedInvoice;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.InvoiceSetActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 发票列表
 * Created by Administrator on 2018/6/13.
 */

public class InvoiceAdapter extends BaseQuickAdapter<InvoiceEntity, BaseViewHolder> {

    private Intent intent;

    public InvoiceAdapter(List<InvoiceEntity> data) {
        super(R.layout.item_invoice_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvoiceEntity item) {
        helper.setTextView(R.id.tv_head, item.getDwmc());
        helper.setTextView(R.id.tv_type, item.getLx());
        helper.setTextView(R.id.tv_ein, item.getSh());

        helper.getView(R.id.ll_edit).setOnClickListener(view -> {
            intent = new Intent(mContext, InvoiceSetActivity.class);
            intent.putExtra("data", item);
            mContext.startActivity(intent);
        });

        helper.getView(R.id.item_z).setOnClickListener(view -> {
            EventBus.getDefault().post(new NeedInvoice.Builder().isNeedInvoice(true).id(item.getId()).build());
            ((Activity) mContext).finish();
        });

        try {
            if (helper.getAdapterPosition() == mData.size() - 1) {
                helper.getView(R.id.view_div_line).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.view_div_line).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
