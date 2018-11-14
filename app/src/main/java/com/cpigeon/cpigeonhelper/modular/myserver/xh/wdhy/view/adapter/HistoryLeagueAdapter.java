package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.hygl.HistoryLeagueAddActivity;

import java.util.List;

/**
 * 历次赛绩
 * Created by Administrator on 2018/4/4.
 */

public class HistoryLeagueAdapter extends BaseQuickAdapter<HistoryLeagueEntity, BaseViewHolder> {
    private HyUserDetailEntity mHyUserDetailEntity;
    private Intent intent;

    private String type = "myself";//look :上级协会查看   myself：自己查看

    public HistoryLeagueAdapter(List<HistoryLeagueEntity> data, HyUserDetailEntity mHyUserDetailEntity,String type) {
        super(R.layout.item_history_league, data);
        this.mHyUserDetailEntity = mHyUserDetailEntity;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryLeagueEntity item) {

        helper.setText(R.id.tv_mc, item.getBsmc());//比赛名次

        if (item.getZhhm().isEmpty() || item.getZhhm().length() == 0) {
            helper.setText(R.id.tv_zuhm, "暂无足环号码");//足环号码
        } else {
            helper.setText(R.id.tv_zuhm, item.getZhhm());//足环号码
        }


        helper.setText(R.id.tv_cs_time, item.getBsrq() + "参赛");

        helper.setText(R.id.tv_xmmc_bsgm, item.getXmmc() + "(" + item.getBsgm() + ")羽");


        if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
            helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
                intent = new Intent(mContext, HistoryLeagueAddActivity.class);
                intent.putExtra("mHistoryLeagueEntity", item);
                intent.putExtra("data", mHyUserDetailEntity);
                mContext.startActivity(intent);
            });
        }

        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }
    }
}
