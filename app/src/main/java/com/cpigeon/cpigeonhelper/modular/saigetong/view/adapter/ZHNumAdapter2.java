package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTImgEntity;
import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ZHNumAdapter2 extends BaseQuickAdapter<SGTImgEntity, BaseViewHolder> {

    public ZHNumAdapter2(List<SGTImgEntity> data) {
        super(R.layout.item_zh_num2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SGTImgEntity item) {

        ImageView rePhotoTag = helper.getView(R.id.change_tag);

        helper.setImageBitmap(R.id.time_line_top, null);
        helper.setImageBitmap(R.id.time_line_btm, null);

        if(item.getBupai() == 1){
            rePhotoTag.setVisibility(View.VISIBLE);
        }else {
            rePhotoTag.setVisibility(View.GONE);
        }

        if (helper.getAdapterPosition() != 1) {
            helper.setImageResource(R.id.time_line_top, R.mipmap.time_zhou_btm);
        }

        if (helper.getAdapterPosition() != mData.size()) {
            helper.setImageResource(R.id.time_line_btm, R.mipmap.time_zhou_top);
        }

        Glide.with(mContext)
                .load(TextUtils.isEmpty(item.getSlturl()) ? "1" : item.getSlturl())
                .centerCrop()
                .into((ImageView) helper.getView(R.id.image));


        helper.setText(R.id.tv_day, DateTool.format(DateUtils.strToDateLong(item.getSj()).getTime(), DateTool.FORMAT_DD));
        helper.setText(R.id.tv_year, DateTool.format(DateUtils.strToDateLong(item.getSj()).getTime(), DateTool.FORMAT_YYYY_MM2));
        helper.setText(R.id.image_type, item.getTag());

        if (StringValid.isStringValid(item.getUpdatefootinfo())) {
            helper.setText(R.id.remark, item.getUpdatefootinfo());
            helper.getView(R.id.remark).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.remark).setVisibility(View.GONE);
        }

    }
}
