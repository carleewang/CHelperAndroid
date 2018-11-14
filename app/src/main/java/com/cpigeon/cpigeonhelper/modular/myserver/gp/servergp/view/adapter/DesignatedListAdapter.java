package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class DesignatedListAdapter extends BaseQuickAdapter<DesignatedListEntity, BaseViewHolder> {


    public DesignatedListAdapter(List<DesignatedListEntity> data) {
        super(R.layout.item_list4, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DesignatedListEntity item) {

        if (helper.getPosition() == 0) {
            helper.getView(R.id.ll_dividerline).setVisibility(View.GONE);
        }

        helper.setText(R.id.it_tv1, item.getXmmc());
        helper.setText(R.id.it_tv2, item.getSfsj());
    }
}
