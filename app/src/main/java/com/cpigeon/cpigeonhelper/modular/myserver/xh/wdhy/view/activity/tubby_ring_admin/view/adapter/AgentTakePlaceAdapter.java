package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.adapter;

import android.app.Activity;
import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.tubby_ring_admin.view.activity.AddAgentTakePlaceActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2018/6/22.
 */

public class AgentTakePlaceAdapter extends BaseQuickAdapter<AgentTakePlaceListEntity, BaseViewHolder> {

    private String type;

    public AgentTakePlaceAdapter(List<AgentTakePlaceListEntity> data, String type) {
        super(R.layout.item_list6, data);
        this.type = type;//类型  select 选择
    }

    @Override
    protected void convert(BaseViewHolder helper, AgentTakePlaceListEntity item) {
        helper.setText(R.id.it_tv1, item.getLxr());
        helper.setText(R.id.it_tv2, item.getDiz());

        helper.getView(R.id.item1_z_ll).setOnClickListener(view -> {

            if (type.equals("select")) {
                //选择返回
                EventBus.getDefault().post(item);
                ((Activity) mContext).finish();

            } else {
                Intent intent = new Intent(mContext, AddAgentTakePlaceActivity.class);
                intent.putExtra("type", "modify");
                intent.putExtra("data", item);
                mContext.startActivity(intent);
            }

        });
    }
}
