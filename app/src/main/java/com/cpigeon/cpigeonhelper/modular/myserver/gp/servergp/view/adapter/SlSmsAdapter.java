package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.SlSmsSetActivity;
import com.cpigeon.cpigeonhelper.utils.DateUtils;

import java.util.List;

/**
 * 上笼短信适配器
 * Created by Administrator on 2017/12/25.
 */
public class SlSmsAdapter extends BaseQuickAdapter<SlListEntity, BaseViewHolder> {
    private Intent intent;

    public SlSmsAdapter(List<SlListEntity> data) {
        super(R.layout.item_list1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SlListEntity item) {
        helper.setText(R.id.it_tv1, item.getXmmc());//标//项目名称
        helper.setText(R.id.it_tv2, DateUtils.getYMD(item.getSlsj()));//上笼时间

        helper.getView(R.id.item1_z_ll).setOnClickListener(view -> {
            //跳转到上笼短信设置页面
            intent = new Intent(mContext, SlSmsSetActivity.class);
            intent.putExtra("tid", String.valueOf(item.getTid()));//索引id
            mContext.startActivity(intent);
        });

    }
}
