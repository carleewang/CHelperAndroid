package com.cpigeon.cpigeonhelper.modular.home.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;

import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */
public interface IHomeView extends IView {

    void getHomeAdData(List<HomeAd> homeAds);//获取数据

    void getThrowable(Throwable throwable);//抛出异常

    void getGYTRaceLists(List<GeYunTong> geYunTongDatas);//获取鸽运通列表


    void diZhenInfo(String diZhenStr);//获取地震内容

    void ciBaoInfo(String ciBaoStr);//获取内容

    void diZhenCiBaoInfo(String ciBaoStr);//获取内容

    void appInfoDataReturn(ApiResponse<AppInfoEntity> listApiResponse, String msg, Throwable mThrowable);
}
