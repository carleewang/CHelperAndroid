package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootBuyEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.FootBuyAddActivity;

import java.util.List;

/**
 * 足环购买
 * Created by Administrator on 2018/3/30.
 */

public class FootBuyAdapter extends BaseQuickAdapter<FootBuyEntity, BaseViewHolder> {

    private Intent intent;
    private HyUserDetailEntity mHyUserDetailEntity;
    private String type = "myself";//look :上级协会查看   myself：自己查看


    public FootBuyAdapter(List<FootBuyEntity> data, HyUserDetailEntity mHyUserDetailEntity,String type) {
        super(R.layout.item_foot_buy_layout, data);
        this.mHyUserDetailEntity = mHyUserDetailEntity;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, FootBuyEntity item) {

        helper.setText(R.id.tv_title, item.getZtype());
        helper.setText(R.id.tv_time, item.getZgmsj());//


        switch (item.getZtype()) {

            case "散号足环":
                helper.setText(R.id.tv_con, item.getZfoot());
                break;
            case "连号足环":
                helper.setText(R.id.tv_con, item.getZfoot1() + "-----" + item.getZfoot2());
                break;
            case "其它足环（连号）":
                helper.setText(R.id.tv_con, item.getZfoot1() + "-----" + item.getZfoot2());
                break;
            case "其它足环（散号）":
                helper.setText(R.id.tv_con, item.getZfoot());
                break;
        }

        if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
            helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
                intent = new Intent(mContext, FootBuyAddActivity.class);
                intent.putExtra("mFootBuyEntity", item);
                intent.putExtra("data", mHyUserDetailEntity);
                mContext.startActivity(intent);
            });
        }

        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }
    }
}
