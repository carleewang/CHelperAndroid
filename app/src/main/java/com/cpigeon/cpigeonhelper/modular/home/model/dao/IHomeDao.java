package com.cpigeon.cpigeonhelper.modular.home.model.dao;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/5.
 */

public interface IHomeDao extends IBaseDao{


    /**
     * 接口回调
     */
    interface GetDownData{
        void getdata(List<HomeAd> homeAds);//获取数据
        void getThrowable(Throwable throwable);//抛出异常
    }


    //数据下载头部轮播数据
    void downBannerData();

    //获取鸽运通比赛列表
    void  downGYTRaceList(String userToken,Map<String, Object> urlParams);
}
