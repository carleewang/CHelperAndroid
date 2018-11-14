package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;

import java.util.List;

/**
 * 开通鸽运通页面适配器
 * Created by Administrator on 2017/10/27.
 */

public class OpenGytOrderAdapter extends BaseQuickAdapter<PackageInfo, BaseViewHolder> {


    public OpenGytOrderAdapter(List<PackageInfo> data) {
        super(R.layout.item_meal_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PackageInfo item) {

        TextView it_meal_start_order = helper.getView(R.id.it_meal_start_order);
        it_meal_start_order.setText("立即开通");

        switch (helper.getPosition()) {
            case 0://普通
                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_pt));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.putong));
                break;
            case 1://vip
                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_vip));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.vip));
                break;
            case 2://svip
                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_svip));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.svip));
                break;
        }

        helper.getView(R.id.it_meal_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderPlayActivity.class);
                intent.putExtra("tag", 1);//开通鸽运通跳转订单标记
                intent.putExtra("type", "open");//订单类型 标记开通、升级、续费【open，upgrade，renewal】
                intent.putExtra("sid", item.getId());//鸽运通服务 ID
                mContext.startActivity(intent);
            }
        });

        //设置套餐等级
        helper.setText(R.id.it_meal_grade, item.getPackageName());

        //设置套餐内容
        helper.setText(R.id.it_meal_content, item.getBrief());

        //设置套餐价格
        TextView it_meal_cost = helper.getView(R.id.it_meal_cost);

        if (Double.valueOf(item.getScores()) > 0) {
            if (item.getUnitname().length() > 0) {
                it_meal_cost.setText(String.valueOf(item.getPrice()) + " 元(" + item.getScores() + "鸽币)" + "/" + item.getUnitname());
            } else {
                it_meal_cost.setText(String.valueOf(item.getPrice()) + " 元(" + item.getScores() + "鸽币)");
            }
        } else {
            if (item.getUnitname().length() > 0) {
                it_meal_cost.setText(String.valueOf(item.getPrice()) + " 元/" + item.getUnitname());
            } else {
                it_meal_cost.setText(String.valueOf(item.getPrice()) + " 元");
            }
        }
    }
}
