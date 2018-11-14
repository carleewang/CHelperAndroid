package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.HyInfoActivity;
import com.cpigeon.cpigeonhelper.utils.glide.GlideRoundTransform;

import java.util.List;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ChildMemberAdapter extends BaseQuickAdapter<HyglHomeListEntity.DatalistBean, BaseViewHolder> {

    private ImageView headImg;//头像

    public ChildMemberAdapter(List<HyglHomeListEntity.DatalistBean> data) {
        super(R.layout.item_child_member, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HyglHomeListEntity.DatalistBean item) {


        helper.setText(R.id.tv_hyxm, "");
        helper.setText(R.id.tv_zcph, "棚号：暂无");
        helper.setText(R.id.tv_gsmc, "");
        helper.setText(R.id.tv_hyxm, item.getName());
        helper.setText(R.id.tv_zcph, "棚号：" + item.getPn());
        helper.setText(R.id.tv_gsmc, "");

        //设置头像
        headImg = helper.getView(R.id.img_hygl_user_tx);
        helper.setText(R.id.tv_status, item.getStatus());
        TextView tvStatus = helper.getView(R.id.tv_status);
        switch (item.getStatus()) {
            case "在册":
                Glide.with(mContext).load(item.getTouxiang())
                        .transform(new GlideRoundTransform(mContext, 3))
                        .into(headImg);
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_00aa0c));
                break;
            case "禁赛":
                Glide.with(mContext).load(item.getTouxiang())
                        .transform(new GlideRoundTransform(mContext, 3))
                        .into(headImg);
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_ff0909));
                break;
            case "除名":
                Glide.with(mContext).load(item.getTouxiang())
                        .bitmapTransform(new GlideRoundTransform(mContext, 3), new GrayscaleTransformation(mContext))
//                        .transform()
                        .into(headImg);

                tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_444444));
                break;
        }

        helper.setText(R.id.tv_hyxm, item.getName());
        helper.setText(R.id.tv_zcph, "棚号：" + item.getPn());
        helper.setText(R.id.tv_gsmc, item.getXhmc());

        helper.getView(R.id.item_z).setOnClickListener(view -> {
            Intent intent = new Intent(mContext, HyInfoActivity.class);
            intent.putExtra("type", "look");
            intent.putExtra("databean", item);
            mContext.startActivity(intent);
        });
    }
}
