package com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter;

import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.SelectPlaceEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.SelectPlaceSelect;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 选择地点（地图）页面数据展示适配器
 * Created by Administrator on 2017/9/26.
 */

public class SelectPlaceAdapter extends BaseQuickAdapter<SelectPlaceEntity, BaseViewHolder> {

    public LinearLayout linearLayoutClick;
    private int position = -1;
    private int colorTag = -1;

    public GetPosition getPosition;

    public interface GetPosition {
        void getClickPosition(int position);
    }


    public SelectPlaceAdapter(List<SelectPlaceEntity> data) {
        super(R.layout.item_select_place, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectPlaceEntity item) {

        LinearLayout linearLayout = helper.getView(R.id.item_se_pl_ll);

        helper.setText(R.id.item_current, item.getLocation());//设置位置
        helper.setText(R.id.item_current2, item.getSnippet());//设置位置
        helper.setText(R.id.item_current_lo_la, "经度：" + GPSFormatUtils.loLaToDMS(item.getLongitude()) + "N" + "  纬度：" + GPSFormatUtils.loLaToDMS(item.getLatitude()) + "E");//设置经纬度


        //根据tag设置item颜色的值
        if (item.getTag() == 1) {
            linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else if (item.getTag() == 2) {
            linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_item_se_pl_bg));
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new SelectPlaceSelect(item.getLongitude(), item.getLatitude(),item.getLocation()));

                getPosition.getClickPosition(helper.getPosition());
                for (int i = 0; i < mData.size(); i++) {//设置所有的tag为1，位选中状态
                    mData.get(i).setTag(1);
                }

                mData.get(helper.getPosition()).setTag(2);//设置选中状态的tag为2，
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_item_se_pl_bg));//设置选中item颜色的值

                SelectPlaceAdapter.this.notifyDataSetChanged();


            }
        });

    }
}
