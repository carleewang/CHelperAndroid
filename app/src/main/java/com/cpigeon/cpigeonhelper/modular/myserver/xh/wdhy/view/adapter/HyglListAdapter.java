package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.HyInfoActivity;
import com.cpigeon.cpigeonhelper.utils.glide.GlideRoundTransform;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;


/**
 * Created by Administrator on 2018/3/23.
 */

public class HyglListAdapter extends BaseQuickAdapter<HyglHomeListEntity.DatalistBean, BaseViewHolder> {

    private Intent intent;
    private ImageView headImg;//头像
    private String type = "";// select  选择  ordinary 普通列表（会员管理）

    public HyglListAdapter(List<HyglHomeListEntity.DatalistBean> data, String type) {
        super(R.layout.item_hygl_home, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, HyglHomeListEntity.DatalistBean item) {

        //设置头像
        headImg = helper.getView(R.id.img_hygl_user_tx);

        if (item.getGeshemc().length() != 0) {
            helper.setText(R.id.tv_gsmc, String.valueOf("[" + item.getGeshemc() + "]"));
        } else {
            helper.setText(R.id.tv_gsmc, String.valueOf(""));
        }

        //设置时间
        helper.setText(R.id.tv_time, item.getShijian());

        //名称
        helper.setText(R.id.tv_name, item.getName());

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

        //设置经纬度
        try {
            if (item.getGeshejd().length() == 0) {
                helper.setText(R.id.tv_lola, String.valueOf("东经：暂无  北纬：暂无"));
            } else {
                helper.setText(R.id.tv_lola, String.valueOf("东经：" + item.getGeshejd() + "  北纬：" + item.getGeshewd()));
            }
        } catch (Exception e) {
            helper.setText(R.id.tv_lola, String.valueOf("东经：暂无  北纬：暂无"));
        }

        if (type.equals("select")) {
            //选择会员  足环管理特比环详情选择鸽主姓名
            helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
                EventBus.getDefault().post(item);
                ((Activity) mContext).finish();
            });

        } else if (type.equals("ordinary")) {
            //会员管理  会员详情
            helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
                //跳转会员信息页面
                intent = new Intent(mContext, HyInfoActivity.class);
                intent.putExtra("type","myself");
                intent.putExtra("databean", item);
                mContext.startActivity(intent);
            });
        }

        helper.getView(R.id.ll_divline).setVisibility(View.VISIBLE);
        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }
    }
}
