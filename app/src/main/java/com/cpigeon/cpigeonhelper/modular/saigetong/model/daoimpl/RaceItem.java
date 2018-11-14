package com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.SGTHomeListAdapter3;

/**
 * Created by Administrator on 2018/1/24.
 */

public class RaceItem extends AbstractExpandableItem<SGTHomeListEntity> implements MultiItemEntity {

    public SGTHomeListEntity.DataBean getRace() {
        return race;
    }

    SGTHomeListEntity.DataBean race;

    public RaceItem(SGTHomeListEntity.DataBean race) {
        this.race = race;
    }

    @Override
    public int getItemType() {
        return SGTHomeListAdapter3.TYPE_RACE;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
