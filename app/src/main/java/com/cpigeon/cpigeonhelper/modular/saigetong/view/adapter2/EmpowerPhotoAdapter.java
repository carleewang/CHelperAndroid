package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.message.base.MyBaseQuickAdapter;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.EmpowerPre;
import com.cpigeon.cpigeonhelper.utils.Lists;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class EmpowerPhotoAdapter extends MyBaseQuickAdapter<AddAuthEntity, BaseViewHolder>{

    private addEmpowerClickListener addEmpowerClickListener;

    public EmpowerPhotoAdapter(EmpowerPre empowerPre) {
        super(R.layout.item_addauth_search_succeed, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, AddAuthEntity item) {

        holder.setImageResource(R.id.imgbtn, R.mipmap.ic_empower_photo);

        holder.setHeadImageView(R.id.add_it_img_userheads, item.getAuthUserInfo().getHeadimgUrl());
        holder.setText(R.id.add_auth_tv_userName, item.getAuthUserInfo().getName());
        holder.setText(R.id.add_auth_tv_phone, item.getAuthUserInfo().getPhone());
        holder.getView(R.id.imgbtn).setOnClickListener(v -> {
            addEmpowerClickListener.empower(item.getAuthUserInfo().getPhone());
        });
    }

    public interface addEmpowerClickListener{
        void empower(String id);
    }

    public void setAddEmpowerClickListener(EmpowerPhotoAdapter.addEmpowerClickListener addEmpowerClickListener) {
        this.addEmpowerClickListener = addEmpowerClickListener;
    }
}
