package com.cpigeon.cpigeonhelper.modular.menu.view.viewdao;

import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public interface BulletinView  extends IView{
    void getBulletData(List<BulletinEntity> list);
}
