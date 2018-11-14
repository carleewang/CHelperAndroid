package com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.activity.EditFlyingActivity;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 我的司放地的适配器,我的训放地
 * Created by Administrator on 2017/6/14.
 */

public class FlyingAreaAdapter extends BaseQuickAdapter<FlyingAreaEntity, BaseViewHolder> {

    private Intent intent;
    private String bearingActivity;
    private LinearLayout linearLayout;

    public FlyingAreaAdapter(List<FlyingAreaEntity> data, String bearingActivity) {
        super(R.layout.item_myflyingarea, data);
        this.bearingActivity = bearingActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, FlyingAreaEntity item) {
        helper.setText(R.id.it_fly_location_name, item.getArea());//司放地名称
        helper.setText(R.id.it_tv_location_address, item.getAlias());//详细地址
        helper.setText(R.id.it_fly_faid, "序号：" + item.getFaid());//序号
        helper.setText(R.id.it_tv_lat_and_long, GPSFormatUtils.strToDMs(item.getLatitude() + "") + "E/" + GPSFormatUtils.strToDMs(item.getLongitude() + "") + "N" );//经纬度

        linearLayout = helper.getView(R.id.ll_fly_item);
        if (bearingActivity.equals("SelectFlyingActivity")) {//如果当前页面为（选择司放地）  item 为可点击
            linearLayout.setClickable(true);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("print", "onClick: 点击item---");
                    //发布事件
                    EventBus.getDefault().post(item);
                }
            });
        }

        helper.getView(R.id.it_ll_edit_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到编辑司放地页面
                intent = new Intent(mContext, EditFlyingActivity.class);//跳转意图
                intent.putExtra("flyingAreaEntity", item);
                mContext.startActivity(intent);//开始跳转
            }
        });
    }
}
