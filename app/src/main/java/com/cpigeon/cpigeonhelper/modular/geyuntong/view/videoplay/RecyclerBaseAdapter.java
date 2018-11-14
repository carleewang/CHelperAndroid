package com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nelson on 15/11/9.
 */
public class RecyclerBaseAdapter extends RecyclerView.Adapter {
    
    private List<ImgOrVideoEntity> itemDataList = null;

    private Context context = null;

    private SmallVideoHelper smallVideoHelper;

    private ImageView imageView;

    private SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder;

    public RecyclerBaseAdapter(Context context, List<ImgOrVideoEntity> itemDataList) {
        this.itemDataList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_voideo2, parent, false);

        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        final RecyclerView.ViewHolder holder = new RecyclerItemViewHolder(context, v, imageView);
        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            RecyclerItemViewHolder recyclerItemViewHolder = (RecyclerItemViewHolder) holder;
            recyclerItemViewHolder.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);
            recyclerItemViewHolder.setRecyclerBaseAdapter(this);
            recyclerItemViewHolder.onBind(position, itemDataList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void addData(List<ImgOrVideoEntity> data) {
        itemDataList = data;
        notifyDataSetChanged();
    }

    public SmallVideoHelper getVideoHelper() {
        return smallVideoHelper;
    }

    public void setVideoHelper(SmallVideoHelper smallVideoHelper, SmallVideoHelper.GSYSmallVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }


    public List<ImgOrVideoEntity> getData() {
        return itemDataList;
    }
}
