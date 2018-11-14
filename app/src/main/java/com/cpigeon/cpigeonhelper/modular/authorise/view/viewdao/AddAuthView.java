package com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21.
 */

public interface AddAuthView extends IView {

    void getAddAuthDatas(List<AddAuthEntity> data);

    void getDataIsNull();


}
