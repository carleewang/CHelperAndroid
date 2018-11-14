package com.cpigeon.cpigeonhelper.modular.flyarea.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.presenter.FlyingPresenter;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import java.util.List;

/**
 * 参考司放地的适配器
 * Created by Administrator on 2017/6/14.
 */

public class SystemFlyingAreaAdapter extends BaseQuickAdapter<FlyingAreaEntity, BaseViewHolder> {
    private FlyingPresenter presenter;//控制层

    public SystemFlyingAreaAdapter(List<FlyingAreaEntity> data, FlyingPresenter presenter) {
        super(R.layout.item_systemflyingarea, data);
        this.presenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, FlyingAreaEntity item) {
        helper.setText(R.id.tv_system_flyingarea_place, item.getArea());//设置区域名称
        helper.setText(R.id.tv_system_flyingarea_longitude, GPSFormatUtils.strToDMs(item.getLatitude() + "") + "E/" + GPSFormatUtils.strToDMs(item.getLongitude() + "") + "N");//设置经纬度

        helper.getView(R.id.imgbtn_add_myflyingarea).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                presenter.addFlyingArea(item.getAlias(),
                        item.getArea(),
                        item.getLongitude(),
                        item.getLatitude());
            }
        });
    }
}
