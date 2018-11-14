package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;

import java.util.List;

/**
 * 已缴费适配器
 * Created by Administrator on 2018/4/12.
 */

public class AlreadyPaidFailureAdapter1 extends BaseQuickAdapter<HFInfoEntity.YjflistBean, BaseViewHolder> {
    public AlreadyPaidFailureAdapter1(List<HFInfoEntity.YjflistBean> data) {
        super(R.layout.item_already_paid_failure, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HFInfoEntity.YjflistBean item) {

        helper.setText(R.id.tv1, item.getHyxm());
        helper.setText(R.id.tv2, item.getJfmoney() + "元");


        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }
    }
}
