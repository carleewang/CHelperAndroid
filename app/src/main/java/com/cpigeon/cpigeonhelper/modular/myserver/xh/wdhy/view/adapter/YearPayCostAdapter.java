package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearPayCostEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.YearPayCostAddActivity;

import java.util.List;

/**
 * 年度交费
 * Created by Administrator on 2018/4/4.
 */

public class YearPayCostAdapter extends BaseQuickAdapter<YearPayCostEntity, BaseViewHolder> {

    private Intent intent;
    private HyUserDetailEntity mHyUserDetailEntity;
    private String type = "myself";//look :上级协会查看   myself：自己查看

    public YearPayCostAdapter(List<YearPayCostEntity> data, HyUserDetailEntity mHyUserDetailEntity,String type) {
        super(R.layout.item_year_pay_cost, data);
        this.mHyUserDetailEntity = mHyUserDetailEntity;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, YearPayCostEntity item) {

        helper.setText(R.id.tv_title, item.getJyear());
        helper.setText(R.id.tv_money, item.getMoney() + "元");

        ImageView img_ycx = helper.getView(R.id.img_ycx);
        img_ycx.setVisibility(View.VISIBLE);
        switch (item.getJstate()) {
            case "已交费":
                img_ycx.setImageResource(R.mipmap.hyjf_yjf);
                break;
            case "未交费":
                img_ycx.setImageResource(R.mipmap.hyjf_wjf);
                break;
        }

        if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
            helper.getView(R.id.item_z).setOnClickListener(view -> {
                //点击item
                intent = new Intent(mContext, YearPayCostAddActivity.class);
                intent.putExtra("edit", item);
                intent.putExtra("data", mHyUserDetailEntity);
                mContext.startActivity(intent);
            });
        }


        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }
    }
}
