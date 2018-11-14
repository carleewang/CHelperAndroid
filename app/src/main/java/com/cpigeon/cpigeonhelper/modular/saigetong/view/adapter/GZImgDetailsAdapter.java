package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;

import java.util.List;

/**
 * 鸽主图片详情   舍弃
 * Created by Administrator on 2017/12/5.
 */
public class GZImgDetailsAdapter extends BaseQuickAdapter<GZImgEntity.ImglistBean, BaseViewHolder> {

    public GZImgDetailsAdapter(List<GZImgEntity.ImglistBean> data) {
        super(R.layout.item_fg_photo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GZImgEntity.ImglistBean item) {
        //设置图片
        Glide.with(mContext)
                .load(TextUtils.isEmpty(item.getSlturl()) ? "1" : item.getSlturl())
                .placeholder(R.mipmap.default_geyuntong)
                .error(R.mipmap.default_geyuntong)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.it_photo_img));
        helper.setText(R.id.it_photo_tv, item.getTag());
    }
}
