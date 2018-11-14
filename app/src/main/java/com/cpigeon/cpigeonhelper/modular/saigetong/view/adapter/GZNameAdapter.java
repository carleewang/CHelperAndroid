package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.activity.SGImgDetailsActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */
public class GZNameAdapter extends BaseQuickAdapter<GZImgEntity, BaseViewHolder> {

    public GZNameAdapter(List<GZImgEntity> data) {
        super(R.layout.item_fg_photo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GZImgEntity item) {
        //设置图片
        Glide.with(mContext)
                .load(TextUtils.isEmpty(item.getImage()) ? "1" : item.getImage())
                .placeholder(R.mipmap.default_geyuntong)
                .error(R.mipmap.default_geyuntong)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.it_photo_img));

//        helper.setText(R.id.it_gz_tag, item.getImglist().get(0).getTag());
        helper.setText(R.id.it_photo_tv, item.getTitle());
        helper.getView(R.id.it_photo_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SGImgDetailsActivity.class);
                intent.putExtra("GZImgEntity", item);
                mContext.startActivity(intent);
            }
        });

    }
}
