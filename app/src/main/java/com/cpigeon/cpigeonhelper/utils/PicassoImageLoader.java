package com.cpigeon.cpigeonhelper.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.youth.banner.loader.ImageLoader;

/**
 * Picasso图片加载器
 * Created by Administrator on 2017/5/17.
 */

public class PicassoImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load( ((HomeAd) path).getAdImageUrl())
//                .placeholder(R.mipmap.logos)
//                .error(R.mipmap.logos)
//                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }
}
