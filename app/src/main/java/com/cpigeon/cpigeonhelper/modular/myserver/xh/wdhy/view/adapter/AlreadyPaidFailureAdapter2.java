package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;

import java.util.List;

/**
 * 未缴费适配器
 * Created by Administrator on 2018/4/12.
 */

public class AlreadyPaidFailureAdapter2 extends BaseQuickAdapter<HFInfoEntity.WjflistBean, BaseViewHolder> {

    public AlreadyPaidFailureAdapter2(List<HFInfoEntity.WjflistBean> data) {
        super(R.layout.item_already_paid_failure, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HFInfoEntity.WjflistBean item) {
        helper.setText(R.id.tv1, item.getHyxm());
        helper.setText(R.id.tv2, item.getJfmoney() + "元");


        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }
    }
}
