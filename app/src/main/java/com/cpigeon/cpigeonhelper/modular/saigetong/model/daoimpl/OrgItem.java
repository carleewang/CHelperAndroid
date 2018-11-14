package com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.adapter2.SGTHomeListAdapter3;


/**
 * Created by Administrator on 2018/1/24.
 */

public class OrgItem extends AbstractExpandableItem<RaceItem> implements MultiItemEntity {

    SGTHomeListEntity orgInfo;

    public OrgItem(SGTHomeListEntity orgInfo) {
        this.orgInfo = orgInfo;
    }

    @Override
    public int getItemType() {
        return SGTHomeListAdapter3.TYPE_ORG;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public SGTHomeListEntity getOrgInfo() {
        return orgInfo;
    }
}