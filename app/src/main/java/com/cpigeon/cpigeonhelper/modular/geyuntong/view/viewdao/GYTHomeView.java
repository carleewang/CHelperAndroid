package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;

/**
 * Created by Administrator on 2017/9/20.
 */

public interface GYTHomeView extends IView {

    //    void gytHomeData(GYTHomeEntity data);
    void gytStatisticalData(GYTStatisticalEntity data);


    void getServiceData(ApiResponse<GYTHomeEntity> dataApiResponse, String msg);//获取服务数据

    void getStatisticalData(ApiResponse<GYTStatisticalEntity> dataApiRespons, String msg);//获取统计数据
}
