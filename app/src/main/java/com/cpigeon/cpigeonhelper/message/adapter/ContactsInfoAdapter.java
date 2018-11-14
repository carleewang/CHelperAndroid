package com.cpigeon.cpigeonhelper.message.adapter;



import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseMultiSelectAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.entity.ContactsEntity;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.cpigeon.cpigeonhelper.utils.Lists;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class ContactsInfoAdapter extends BaseMultiSelectAdapter<ContactsEntity,BaseViewHolder> {

    public ContactsInfoAdapter() {
        super(R.layout.item_contacts_list_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ContactsEntity item) {
        super.convert(holder, item);
        holder.setText(R.id.title, item.xingming);
        holder.setText(R.id.number,item.sjhm);
    }

    public void setLoadMore(boolean isEnd) {
            if (isEnd) this.loadMoreEnd();
            else
                this.loadMoreComplete();
    }

    @Override
    public void setNewData(List<ContactsEntity> data) {
        getRecyclerView().getRecycledViewPool().clear();
        notifyDataSetChanged();
        if(data.isEmpty()){
            CommonTool.setEmptyView(this, "暂时没有联系人");
        }
        super.setNewData(data);
    }
}
