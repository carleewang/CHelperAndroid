package com.cpigeon.cpigeonhelper.message.adapter;

import android.widget.TextView;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseMultiSelectAdapter;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.entity.CommonEntity;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.cpigeon.cpigeonhelper.utils.DialogUtils;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class CommonMessageAdapter extends BaseMultiSelectAdapter<CommonEntity, BaseViewHolder> {

    private OnCheckboxClickListener listener;

    private OnEditClickListener onEditClickListener;


    public CommonMessageAdapter() {
        super(R.layout.item_common_message_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, CommonEntity item) {
        super.convert(holder,item);

        TextView content = holder.findViewById(R.id.content);

        content.setText(item.dxnr);
        content.setOnClickListener(v -> {
            DialogUtils.createDialog(mContext, "详细内容"
                    , item.dxnr, "确定");
        });

        holder.findViewById(R.id.checkbox).setOnClickListener(v -> {
            listener.OnClick(holder.getAdapterPosition());
        });

        holder.findViewById(R.id.btnEdit).setOnClickListener(v -> {
            onEditClickListener.onClick(holder.getAdapterPosition());
        });

    }

    public void closeSwipe(int position){
        SwipeMenuLayout swipeLayout = (SwipeMenuLayout) this.getViewByPosition(position, R.id.swipeLayout);
        swipeLayout.smoothClose();
    }

    public interface OnCheckboxClickListener{
        void OnClick(int position);
    }

    public interface OnEditClickListener{
        void onClick(int position);
    }

    public void setOnCheckboxClickListener(OnCheckboxClickListener listener){
        this.listener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }



    @Override
    public void setNewData(List<CommonEntity> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            CommonTool.setEmptyView(this,"常用语为空");
        }
    }
}
