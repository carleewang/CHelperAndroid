package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.foot_admin.view.activity.FootAdminDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FootAdminHomeListAdapter extends BaseQuickAdapter<FootAdminListDataEntity.FootlistBean, BaseViewHolder> {


    private String type;

    public FootAdminHomeListAdapter( List<FootAdminListDataEntity.FootlistBean> data,String type) {
        super(R.layout.item_list5, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, FootAdminListDataEntity.FootlistBean item) {
        try {
            helper.setText(R.id.it_tv1,"");
            helper.setText(R.id.it_tv2,"");

            helper.setText(R.id.it_tv1,item.getFoot());
            helper.setText(R.id.it_tv2,item.getName());

            helper.getView(R.id.item1_z_ll).setOnClickListener(view->{
                Intent intent = new Intent(mContext, FootAdminDetailActivity.class);
                intent.putExtra("data",item);
                intent.putExtra("type",type);
                mContext.startActivity(intent);
            });


            if (helper.getPosition() == mData.size()) {
                helper.getView(R.id.view_div_line).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
