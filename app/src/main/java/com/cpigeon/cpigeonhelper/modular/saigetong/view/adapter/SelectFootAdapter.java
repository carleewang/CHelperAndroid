package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GeZhuFootEntity;

import java.util.List;

/**
 * 选择鸽主足环适配器
 * Created by Administrator on 2018/1/15.
 */

public class SelectFootAdapter extends BaseQuickAdapter<GeZhuFootEntity.FootlistBean, BaseViewHolder> {


    public SelectFootAdapter(List<GeZhuFootEntity.FootlistBean> data) {
        super(R.layout.item_sgt_foot_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GeZhuFootEntity.FootlistBean item) {

        helper.setText(R.id.tv_center, item.getFoot());
        CheckBox checkBox = helper.getView(R.id.btn_cb);

        if (mData.get(helper.getPosition()).getClickTag() == 1) {//当前状态未选中
            checkBox.setChecked(false);
        } else {//当前状态选中
            checkBox.setChecked(true);
        }

        LinearLayout llz = helper.getView(R.id.ll_z);

        llz.setOnClickListener(view -> {
            if (mData.get(helper.getPosition()).getClickTag() == 1) {//当前状态未选中
                checkBox.setChecked(false);
                mData.get(helper.getPosition()).setClickTag(2);//设置选中
            } else {//当前状态选中
                checkBox.setChecked(true);
                mData.get(helper.getPosition()).setClickTag(1);//设置未选中
            }
            SelectFootAdapter.this.notifyDataSetChanged();
        });
    }
}
