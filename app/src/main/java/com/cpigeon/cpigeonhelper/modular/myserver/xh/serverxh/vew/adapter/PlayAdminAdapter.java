package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity.DetailsPlayActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.actvity.PlaySmsSetActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PlayAdminAdapter extends BaseQuickAdapter<PlayListEntity, BaseViewHolder> {

    private Intent intent;

    private int isBsglOrDxsz = -1; // 1：比赛管理   2：短信设置

    public PlayAdminAdapter(List<PlayListEntity> data, int isBsglOrDxsz) {
        super(R.layout.item_list2, data);
        this.isBsglOrDxsz = isBsglOrDxsz;
    }

    @Override
    protected void convert(BaseViewHolder helper, PlayListEntity item) {
        helper.setText(R.id.it_tv1, item.getXmmc());//设置内容

        helper.getView(R.id.item2_z_ll).setOnClickListener(view -> {
            if (isBsglOrDxsz == 1) {
                intent = new Intent(mContext, DetailsPlayActivity.class);
                intent.putExtra("PlayListEntity", item);
                mContext.startActivity(intent);
            } else if (isBsglOrDxsz == 2) {
                intent = new Intent(mContext, PlaySmsSetActivity.class);
                intent.putExtra("PlayListEntity", item);
                mContext.startActivity(intent);
            }
        });
    }
}
