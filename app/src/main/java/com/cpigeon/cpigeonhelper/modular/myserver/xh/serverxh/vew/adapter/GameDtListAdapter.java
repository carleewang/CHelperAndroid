package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity.DetailsGameGcActivity;

import java.util.List;

/**
 * 协会动态列表adapter
 * Created by Administrator on 2017/12/12.
 */
public class GameDtListAdapter extends BaseQuickAdapter<DtListEntity, BaseViewHolder> {

    private Intent intent;

    public GameDtListAdapter(List<DtListEntity> data) {
        super(R.layout.item_list1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DtListEntity item) {
        helper.setText(R.id.it_tv1, item.getTitle());//标题
        helper.setText(R.id.it_tv2, item.getFbsj());//时间

        helper.getView(R.id.item1_z_ll).setOnClickListener(view -> {
            intent = new Intent(mContext, DetailsGameGcActivity.class);
            intent.putExtra("dtid", item.getDtid());
            mContext.startActivity(intent);
        });
    }
}
