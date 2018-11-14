package com.cpigeon.cpigeonhelper.message.adapter;



import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseMultiSelectAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.utils.Lists;


/**
 * Created by Zhu TingYu on 2017/11/21.
 */

public class ContactsListAdapter extends BaseMultiSelectAdapter<ContactsGroupEntity,BaseViewHolder> {

    String type;

    public ContactsListAdapter() {
        super(R.layout.item_contacts_list_layout, Lists.newArrayList());
    }

    public ContactsListAdapter(String type) {
        super(R.layout.item_contacts_list_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ContactsGroupEntity item) {
        super.convert(holder, item);

        holder.setText(R.id.title,item.fzmc != null ? item.fzmc : "12312312");
        holder.setText(R.id.number,mContext.getString(R.string.string_text_contacts_number
                , String.valueOf(item.fzcount)));


    }

    public void setType(String type) {
        this.type = type;
    }
}
