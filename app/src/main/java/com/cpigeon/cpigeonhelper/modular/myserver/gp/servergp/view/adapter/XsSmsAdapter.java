package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.XsSmsSetActivity;

import java.util.List;

/**
 * 训赛短信适配器
 * Created by Administrator on 2017/12/25.
 */

public class XsSmsAdapter extends BaseQuickAdapter<XsListEntity, BaseViewHolder> {
    private Intent intent;

    public XsSmsAdapter(List<XsListEntity> data) {
        super(R.layout.item_list1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, XsListEntity item) {

        helper.setText(R.id.it_tv1, item.getXmmc());//设置名称
        helper.setText(R.id.it_tv2, item.getSfsj());//设置时间

        helper.getView(R.id.item1_z_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到上笼短信设置页面
                intent = new Intent(mContext, XsSmsSetActivity.class);
                intent.putExtra("XsListEntity", item);
                mContext.startActivity(intent);


//                //跳转到上笼短信详情页面
//                intent = new Intent(mContext, XsSmsDetailActivity.class);
//                intent.putExtra("XsListEntity", item);//当前点击item数据
//                mContext.startActivity(intent);
            }
        });
    }
}
