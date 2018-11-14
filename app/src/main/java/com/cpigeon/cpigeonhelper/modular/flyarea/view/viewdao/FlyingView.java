package com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface FlyingView extends IView {

    void addFlyingFlyingData(ApiResponse<Object> listApiResponse, String msg, Throwable throwable);

    void getFlyingData(ApiResponse<List<FlyingAreaEntity>> listApiResponse, String msg, Throwable throwable);//有数据成功后返回

    void getFlyingDataNull(String msg);//没有数据返回(修改，删除司放地成功后回调)
}
