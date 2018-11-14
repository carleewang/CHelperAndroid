package com.cpigeon.cpigeonhelper.modular.order.view.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;

import java.util.List;

/**
 * 续费鸽运通适配器
 * Created by Administrator on 2017/10/31.
 */

public class RenewalAdapter extends BaseQuickAdapter<PackageInfo, BaseViewHolder> {

    private String hint = "";

    public void setHint(String hint) {
        this.hint = hint;
        this.notifyDataSetChanged();
    }

    public RenewalAdapter(List<PackageInfo> data) {
        super(R.layout.item_meal_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PackageInfo item) {

        Intent intent = new Intent(mContext, OrderPlayActivity.class);
        intent.putExtra("tag", 1);//开通鸽运通跳转订单标记
        intent.putExtra("sid", item.getId());//鸽运通服务 ID

        //套餐等级
        TextView it_meal_grade = helper.getView(R.id.it_meal_grade);
        it_meal_grade.setText(item.getPackageName());

        //设置套餐内容
        helper.setText(R.id.it_meal_content, item.getBrief());
        Log.d("RenewalAdapter", "convert: " + item.getBrief());

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

        TextView it_meal_start_order = helper.getView(R.id.it_meal_start_order);
        it_meal_start_order.setText("立即续费");

        //提示信息  it_meal_hint
        TextView it_meal_hint = helper.getView(R.id.it_meal_hint);
        it_meal_hint.setText(hint);

        switch (helper.getPosition()) {
            case 0://单次 monitoring  danci  promptly  buy

                //设置套餐价格
                if (item.getBrief().indexOf("单场") != -1) {
                    it_meal_start_order.setText("立即购买");
                    intent.putExtra("type", "once");//订单类型 标记开通、升级、续费 单次购买【open，upgrade，renewal，once】
                } else {
                    intent.putExtra("type", "renewal");//订单类型 标记开通、升级、续费【open，upgrade，renewal】
                }

                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_danci));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.danci));
                break;
            case 1://普通
                intent.putExtra("type", "renewal");//订单类型 标记开通、升级、续费【open，upgrade，renewal】
                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_pt));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.putong));
                break;
            case 2://vip
                intent.putExtra("type", "renewal");//订单类型 标记开通、升级、续费【open，upgrade，renewal】
                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_vip));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.vip));
                break;
            case 3://svip
                intent.putExtra("type", "renewal");//订单类型 标记开通、升级、续费【open，upgrade，renewal】
                it_meal_start_order.setBackground(mContext.getResources().getDrawable(R.drawable.selector_meal_btn_svip));
                helper.getView(R.id.it_meal_ll).setBackground(mContext.getResources().getDrawable(R.mipmap.svip));
                break;
        }

        helper.getView(R.id.it_meal_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(intent);
            }
        });
    }
}
