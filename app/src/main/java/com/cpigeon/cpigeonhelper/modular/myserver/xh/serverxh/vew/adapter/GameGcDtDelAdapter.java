package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.adapter;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DelGcDtEntity;

import java.util.List;

/**
 * 赛事规程列表adapter
 * Created by Administrator on 2017/12/12.
 */
public class GameGcDtDelAdapter extends BaseQuickAdapter<DelGcDtEntity, BaseViewHolder> {



    public GameGcDtDelAdapter(List<DelGcDtEntity> data) {
        super(R.layout.item_list3, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DelGcDtEntity item) {
        helper.setText(R.id.it_tv1, item.getTitle());//标题
        helper.setText(R.id.it_tv2, item.getTime());//时间
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
            GameGcDtDelAdapter.this.notifyDataSetChanged();
        });
    }
}
