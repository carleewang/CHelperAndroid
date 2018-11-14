package com.cpigeon.cpigeonhelper.modular.home.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.utils.glide.GlideRoundTransform;
import com.zhouwei.mzbanner.holder.MZViewHolder;

/**
 * Created by Administrator on 2018/1/20.
 */

public class BannerViewHolder implements MZViewHolder<HomeAd> {
    private ImageView mImageView;
    private Context mContext;

    public BannerViewHolder(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null);
        mImageView = (ImageView) view.findViewById(R.id.it_banner_imgview);
        return view;
    }

    @Override
    public void onBind(Context context, int position, HomeAd data) {
        // 数据绑定
        Glide.with(mContext).load(data.getAdImageUrl()).transform(new GlideRoundTransform(context, 5)).into(mImageView);
    }
}
