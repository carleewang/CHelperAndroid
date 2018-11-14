package com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao;

import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AuthHomeEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21.
 */

public interface AuthHomeView extends IView{

    void authHomeListData(List<AuthHomeEntity> listData);//有数据返回
    void authHomeListDataNO();//没有数据返回

    void cancelSucceed();//取消授权成功
}
