package com.cpigeon.cpigeonhelper.message.adapter;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.MessageEntity;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.message.base.MyBaseQuickAdapter;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.cpigeon.cpigeonhelper.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class MessageHistoryAdapter extends MyBaseQuickAdapter<MessageEntity, BaseViewHolder> {

    public MessageHistoryAdapter() {
        super(R.layout.item_message_history_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, MessageEntity item) {
        holder.setText(R.id.number, mContext.getString(R.string.string_text_message_addressee_number, String.valueOf(item.fscount)));
        holder.setText(R.id.date,item.fssj);
        holder.setText(R.id.content, item.dxnr);
    }

    @Override
    public void setNewData(List<MessageEntity> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            CommonTool.setEmptyView(this,"历史记录为空");
        }
    }
}
