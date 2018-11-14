package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.EmpowerPhotoUserEntity;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.message.base.MyBaseQuickAdapter;
import com.cpigeon.cpigeonhelper.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class CancelPhotoPermissionAdapter extends MyBaseQuickAdapter<EmpowerPhotoUserEntity, BaseViewHolder> {

    private OnCancelAuthListener listener;

    public CancelPhotoPermissionAdapter() {
        super(R.layout.item_addauth_search_succeed, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, EmpowerPhotoUserEntity item) {
        holder.setImageResource(R.id.imgbtn, R.mipmap.ic_cancle_empower_photo);

        holder.setHeadImageView(R.id.add_it_img_userheads, item.touxiang);
        holder.setText(R.id.add_auth_tv_userName, item.username);
        holder.setText(R.id.add_auth_tv_phone, item.shouji);
        holder.getView(R.id.imgbtn).setOnClickListener(v -> {
            listener.cancel(holder.getAdapterPosition());
        });
    }

    public void setOnCancelAuthListener(OnCancelAuthListener listener) {
        this.listener = listener;
    }

    public interface  OnCancelAuthListener{
        void cancel(int point);
    }

    @Override
    protected String getEmptyViewText() {
        return "没有授权信息";
    }
}
