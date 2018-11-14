package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity.DetailsGameGcActivity;

import java.util.List;

/**
 * 赛事规程列表adapter
 * Created by Administrator on 2017/12/12.
 */
public class GameGcListAdapter extends BaseQuickAdapter<GcListEntity, BaseViewHolder> {

    private Intent intent;

    public GameGcListAdapter(List<GcListEntity> data) {
        super(R.layout.item_list1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GcListEntity item) {
        helper.setText(R.id.it_tv1, item.getTitle());//标题
        helper.setText(R.id.it_tv2, item.getFbsj());//时间

        helper.getView(R.id.item1_z_ll).setOnClickListener(view -> {
            intent = new Intent(mContext, DetailsGameGcActivity.class);
            intent.putExtra("gcid", item.getGcid());
            mContext.startActivity(intent);
        });
    }
}
