package com.cpigeon.cpigeonhelper.modular.menu.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;

import java.util.List;

/**
 * 用户操作日志的adapter
 * Created by Administrator on 2017/7/10.
 */

public class LogbookAdapter extends BaseQuickAdapter<LogbookEntity, BaseViewHolder> {
    public LogbookAdapter(List<LogbookEntity> data) {
        super(R.layout.item_logbook, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogbookEntity item) {
        helper.setText(R.id.tv_operator_ip, "IP：" + item.getIp());
        helper.setText(R.id.tv_operator_time, item.getTime());
        helper.setText(R.id.tv_operator_operatorlog, "操作内容：" + item.getContent());
        if (item.getUserInfo()!=null){
            helper.setText(R.id.tv_operator_userid, "用户ID：" + item.getUserInfo().getName());
        }
    }
}
