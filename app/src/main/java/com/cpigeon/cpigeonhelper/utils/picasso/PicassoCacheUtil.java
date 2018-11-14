package com.cpigeon.cpigeonhelper.utils.picasso;

import android.content.Context;
import android.os.Looper;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2018/2/6.
 */

public class PicassoCacheUtil {

    /**
     * 清除图片磁盘缓存
     */
    public  static void clearImageDiskCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

//                        Picasso.with(context).shutdown();  // clear bitmap cache in memory
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
