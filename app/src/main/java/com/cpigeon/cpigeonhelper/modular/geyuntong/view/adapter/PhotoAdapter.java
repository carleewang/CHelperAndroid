package com.cpigeon.cpigeonhelper.modular.geyuntong.view.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.ViewControlShare;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.umeng.socialize.media.UMImage;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */

public class PhotoAdapter extends BaseQuickAdapter<ImgOrVideoEntity, BaseViewHolder> {
    private ShareDialogFragment dialogFragment;

    public PhotoAdapter(List<ImgOrVideoEntity> data) {
        super(R.layout.item_fg_photo, data);
        dialogFragment = new ShareDialogFragment();
    }

    @Override
    protected void convert(BaseViewHolder helper, ImgOrVideoEntity item) {

        //设置图片
        Glide.with(mContext)
                .load(TextUtils.isEmpty(item.getThumburl()) ? "1" : item.getThumburl())
                .placeholder(R.mipmap.default_geyuntong)
                .error(R.mipmap.default_geyuntong)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.it_photo_img));

        //设置标签
        helper.setText(R.id.it_photo_tv, item.getTag());
        LinearLayout linearLayout = helper.getView(R.id.it_photo_ll);

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                UMImage image = new UMImage(mContext, item.getUrl());//网络图片

                //设置缩略图
                UMImage thumb = new UMImage(mContext, item.getThumburl());
                image.setThumb(thumb);

                if (dialogFragment != null) {
                    dialogFragment.setShareContent(item.getUrl());
                    dialogFragment.setShareListener(ViewControlShare.getShareResultsDown(mContext, dialogFragment, "tp"));
                    dialogFragment.setShareType(2);
                    dialogFragment.show(((Activity) mContext).getFragmentManager(), "share");
                }
                return true;
            }
        });
    }
}
