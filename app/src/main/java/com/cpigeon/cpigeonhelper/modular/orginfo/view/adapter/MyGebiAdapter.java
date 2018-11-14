package com.cpigeon.cpigeonhelper.modular.orginfo.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class MyGebiAdapter extends BaseQuickAdapter<GbListEntity, BaseViewHolder> {

    public MyGebiAdapter(List<GbListEntity> data) {
        super(R.layout.item_gebi_mx, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GbListEntity item) {
        helper.setText(R.id.it_mx_cen, item.getItem());//内容
        helper.setText(R.id.it_mx_time, item.getDatetime());//时间
        if (item.getGebi() > 0) {
            helper.setText(R.id.it_mx_num, String.valueOf("+" + item.getGebi()));//鸽币数量
        } else {
            helper.setText(R.id.it_mx_num, String.valueOf("" + item.getGebi()));//鸽币数量
        }
    }
}
