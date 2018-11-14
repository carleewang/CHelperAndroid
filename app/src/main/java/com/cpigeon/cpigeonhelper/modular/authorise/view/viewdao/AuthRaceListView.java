package com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public interface AuthRaceListView extends IView{

    void getRaceListDataIsNo();//请求成功，没有数据
    void getRaceListData(List<GeYunTong> data);//请求成功，有数据

    void addAuthData(ApiResponse<String> dataApiResponse, String msg, Throwable mThrowable);//添加授权结果
}
