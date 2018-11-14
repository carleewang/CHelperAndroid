package com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.entity.ChangeFootPhotoDetailsEntity;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.saigetong.presenter.ChangeFootDetailsPre;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.video.RecordedSGTActivity;

import java.util.ArrayList;
import java.util.List;

import static com.cpigeon.cpigeonhelper.utils.PermissonUtil.cameraIsCanUse;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class ZHNumChangePhotoAdapter extends BaseQuickAdapter<ChangeFootPhotoDetailsEntity, BaseViewHolder> {

    private SaActionSheetDialog dialogFoot;
    ChangeFootDetailsPre mPre;

    public ZHNumChangePhotoAdapter(List<ChangeFootPhotoDetailsEntity> data, Activity activity) {
        super(R.layout.item_zh_num2, data);

        dialogFoot = new SaActionSheetDialog(activity).builder();

        SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener2 = new SaActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                if (!cameraIsCanUse()) {
                    return;
                }

                Intent intent = new Intent(mContext, RecordedSGTActivity.class);
                intent.putExtra("tagStr", mPre.tagString);
                intent.putExtra("tagid", Integer.valueOf(mPre.tagId));

                if (which == 1) {
                    intent.putExtra("IMG_NUM_TAG", 2);//左脚
                } else {
                    intent.putExtra("IMG_NUM_TAG", 3);//右脚
                }

                intent.putParcelableArrayListExtra("listdetail", (ArrayList<? extends Parcelable>) mPre.getFootSSEntity());
                intent.putExtra("reason", mPre.errorReason);

                mContext.startActivity(intent);
            }

        };
        dialogFoot.addSheetItem("足环在左脚", onSheetItemClickListener2);
        dialogFoot.addSheetItem("足环在右脚", onSheetItemClickListener2);


        /**
         * 足环左右脚
         */

    }

    @Override
    protected void convert(BaseViewHolder helper, ChangeFootPhotoDetailsEntity item) {
        try {
            if (helper.getAdapterPosition() != 1) {
                helper.setImageResource(R.id.time_line_top, R.mipmap.time_zhou_btm);
            }
            if (helper.getAdapterPosition() != mData.size()) {
                helper.setImageResource(R.id.time_line_btm, R.mipmap.time_zhou_top);
            }

            TextView rePhoto = helper.getView(R.id.retaken_photo);
            ImageView rePhotoTag = helper.getView(R.id.change_tag);
            TextView remark = helper.getView(R.id.remark);
            //没有错误补拍
            if (helper.getAdapterPosition() == 1) {
                Glide.with(mContext)
                        .load(item.slturl)
    //                .resize(mContext.getResources().getDimensionPixelSize(R.dimen.gyt_monitor_img_wh), mContext.getResources().getDimensionPixelSize(R.dimen.gyt_monitor_img_wh))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));
                if(!StringValid.isStringValid(item.bpimgurl)){
                    rePhoto.setVisibility(View.VISIBLE);
                }else {
                    rePhoto.setVisibility(View.GONE);
                }
                rePhotoTag.setVisibility(View.GONE);
                rePhoto.setOnClickListener(v -> {
                    dialogFoot.show();
                });

                if (StringValid.isStringValid(item.updatefootinfo)) {
                    remark.setVisibility(View.VISIBLE);
                    remark.setText(item.updatefootinfo);
                }else {
                    remark.setVisibility(View.GONE);
                }

                helper.setText(R.id.tv_day, DateTool.format(DateUtils.str2DateLong(item.scsj).getTime(), DateTool.FORMAT_DD));
                helper.setText(R.id.tv_year, DateTool.format(DateUtils.str2DateLong(item.scsj).getTime(), DateTool.FORMAT_YYYY_MM2));

            } else {
                rePhoto.setVisibility(View.GONE);
                rePhotoTag.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(item.bpslturl)
    //                .resize(mContext.getResources().getDimensionPixelSize(R.dimen.gyt_monitor_img_wh), mContext.getResources().getDimensionPixelSize(R.dimen.gyt_monitor_img_wh))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));

                if (StringValid.isStringValid(item.bpyuanyin)) {
                    remark.setVisibility(View.VISIBLE);
                    remark.setText(item.bpyuanyin);
                }else {
                    remark.setVisibility(View.GONE);
                }
                helper.setText(R.id.tv_day, DateTool.format(DateUtils.strToDateLong(item.bpimgtime).getTime(), DateTool.FORMAT_DD));
                helper.setText(R.id.tv_year, DateTool.format(DateUtils.strToDateLong(item.bpimgtime).getTime(), DateTool.FORMAT_YYYY_MM2));
            }


            helper.setText(R.id.image_type, item.tagname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setmPre(ChangeFootDetailsPre mPre) {
        this.mPre = mPre;
    }
}
