package com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.presenter.ViewControlShare;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.fragment.ShareDialogFragment;
import com.cpigeon.cpigeonhelper.playvideo.VideoPlayActivity;

/**
 * Created by GUO on 2015/12/3.
 */
public class RecyclerItemViewHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context = null;

    FrameLayout listItemContainer;
    ImageView listItemBtn;
    TextView it_video_lable;//设置标签
    TextView it_video_time;//设置时间
    TextView it_video_water;//设置天气 (天气  温度   风向)
    TextView it_video_lola;//设置经纬度
    ImageButton imgBtn_share;

    ImageView imageView;

    private SmallVideoHelper smallVideoHelper;
    private SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;

    private ShareDialogFragment dialogFragment;//分享显示视图

    public RecyclerItemViewHolder(Context context, View v, ImageView imageView) {
        super(v);
        try {
            this.context = context;
            listItemContainer = (FrameLayout) v.findViewById(R.id.list_item_container);
            listItemBtn = (ImageView) v.findViewById(R.id.list_item_btn);
            it_video_lable = (TextView) v.findViewById(R.id.it_video_lable);
            it_video_time = (TextView) v.findViewById(R.id.it_video_time);
            it_video_water = (TextView) v.findViewById(R.id.it_video_water);
            it_video_lola = (TextView) v.findViewById(R.id.it_video_lola);
            imgBtn_share = (ImageButton) v.findViewById(R.id.imgBtn_share);
            this.imageView = imageView;
            dialogFragment = new ShareDialogFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBind(final int position, final ImgOrVideoEntity item) {


        try {
            Glide.with(context).load(item.getThumburl()).into(imageView);

            smallVideoHelper.addVideoPlayer(position, imageView, TAG, listItemContainer, listItemBtn);

            it_video_lable.setText(item.getTag());//设置标签
            it_video_time.setText(item.getTime()); //设置时间
            it_video_water.setText(item.getWeartherName() + " " + item.getTemperature() + "° " + item.getWindDir()); //设置天气
            it_video_lola.setText("坐标：" + item.getLongitude() + "/" + item.getLatitude());//设置经纬度

            imgBtn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (dialogFragment != null) {
                            dialogFragment.setShareContent(ApiConstants.VIDEO_SHARE + item.getFid());
                            dialogFragment.setShareListener(ViewControlShare.getShareResultsDown(context, dialogFragment, "sp"));
                            dialogFragment.setShareTitle(item.getTag());
                            dialogFragment.setShareContentString(MonitorData.getMonitorRaceName());
                            dialogFragment.setShareType(1);
                            dialogFragment.show(((Activity) context).getFragmentManager(), "share");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            listItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VideoPlayActivity.startActivity((Activity) context, v, item.getUrl());

//                    try {
//                        getRecyclerBaseAdapter().notifyDataSetChanged();
//                        //listVideoUtil.setLoop(true);
//                        smallVideoHelper.setPlayPositionAndTag(position, TAG);
//                        gsySmallVideoHelperBuilder.setVideoTitle(item.getTag()).setUrl(item.getUrl());
//                        smallVideoHelper.startPlay();
//                    } catch (Exception e) {
//
//                    }
                }
            });
        } catch (Exception e) {

        }
    }


    public void setVideoHelper(SmallVideoHelper smallVideoHelper, SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }
}





