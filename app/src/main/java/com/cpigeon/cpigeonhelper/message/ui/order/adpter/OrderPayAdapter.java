package com.cpigeon.cpigeonhelper.message.ui.order.adpter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.Lists;


import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class OrderPayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private List<Integer> icons;

    public OrderPayAdapter() {
        super(R.layout.item_line_text_with_icon_layout, Lists.newArrayList());
        icons = Lists.newArrayList(
                R.mipmap.money,
                R.mipmap.wxpay
        );
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {

        if (holder.getPosition() + 1 == icons.size()) {
            holder.getView(R.id.view_fgx).setVisibility(View.GONE);
        }

        holder.setImageResource(R.id.icon, icons.get(holder.getAdapterPosition()));
        holder.setText(R.id.title, item);
    }
}
