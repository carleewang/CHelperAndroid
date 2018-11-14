package com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */

public class EditGytListAdapter extends BaseQuickAdapter<GeYunTong, BaseViewHolder> {

    public EditGytListAdapter(List<GeYunTong> data) {
        super(R.layout.item_edit_gyt_list, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, GeYunTong item) {

        if (mData.get(helper.getPosition()).getTag() == 2) {//当前状态未选中
            ((ImageView) helper.getView(R.id.it_gyt_r_img)).setImageResource(R.mipmap.yuan_ok);
        } else {//当前状态选中
            ((ImageView) helper.getView(R.id.it_gyt_r_img)).setImageResource(R.mipmap.yuan);
        }

        ((LinearLayout) helper.getView(R.id.item_btn_ll)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("print", "选中按钮--->" + helper.getPosition());

                if (mData.get(helper.getPosition()).getTag() == 1) {//当前状态未选中
                    ((ImageView) helper.getView(R.id.it_gyt_r_img)).setImageResource(R.mipmap.yuan_ok);
                    mData.get(helper.getPosition()).setTag(2);//设置选中
                } else {//当前状态选中
                    ((ImageView) helper.getView(R.id.it_gyt_r_img)).setImageResource(R.mipmap.yuan);
                    mData.get(helper.getPosition()).setTag(1);//设置未选中
                }
                EditGytListAdapter.this.notifyDataSetChanged();
            }
        });

        helper.setText(R.id.tv_geyuntong_name, item.getRaceName());//竞赛名称
        if (item.getFlyingArea().isEmpty()) {
            if (RealmUtils.getServiceType().equals("geyuntong")) {
                helper.setText(R.id.tv_geyuntong_place, "暂无司放地点");//飞行区域
            } else {
                helper.setText(R.id.tv_geyuntong_place, "暂无训放地点");//飞行区域
            }
        } else {
            helper.setText(R.id.tv_geyuntong_place, item.getFlyingArea());//飞行区域
        }

        helper.setText(R.id.tv_geyuntong_time, item.getCreateTime());//创建时间
        helper.setText(R.id.tv_geyuntong_status, item.getState());//状态
        Glide.with(mContext)
                .load(TextUtils.isEmpty(item.getRaceImage()) ? "1" : item.getRaceImage())
                .placeholder(R.mipmap.default_geyuntong)
                .centerCrop()
                .error(R.mipmap.default_geyuntong)
                .into((ImageView) helper.getView(R.id.iv_geyuntong_img));
    }

}
