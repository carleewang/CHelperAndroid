package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GeZhuFootEntity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

/**
 * 选择鸽主足环适配器
 * Created by Administrator on 2018/1/15.
 */

public class SelectFootAdapter2 extends BaseQuickAdapter<GeZhuFootEntity.FootlistBean, BaseViewHolder> {

    public static String strTag;//标签tag

    public SelectFootAdapter2(List<GeZhuFootEntity.FootlistBean> data, String strTag) {
        super(R.layout.item_sgt_foot_select, data);
        this.strTag = strTag;
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


            SelectFootAdapter2.this.notifyDataSetChanged();

            clickNum = 0;
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getClickTag() == 2) {//当前状态 选中
                    clickNum = clickNum + 1;
                }
            }

            Log.d("xiaohl", "convert: " + clickNum + "   datasize-->" + mData.size() + "    ");


            if (SelectFootAdapter2.strTag.equals("入棚拍照")) {
                if (clickNum == 4) {
                    checkBox.setChecked(true);
                    mData.get(helper.getPosition()).setClickTag(1);//设置未选中

                    CommonUitls.showSweetDialog(mContext, "最多选择三个足环");
                    return;
                }
            } else {
                if (clickNum == 2) {
                    checkBox.setChecked(true);
                    mData.get(helper.getPosition()).setClickTag(1);//设置未选中

                    CommonUitls.showSweetDialog(mContext, "最多选择一个足环");
                    return;
                }
            }


            SelectFootAdapter2.this.notifyDataSetChanged();
        });
    }

    private int clickNum = 0;//总共选中的足环数量
}
