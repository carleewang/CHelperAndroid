package com.cpigeon.cpigeonhelper.modular.home.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class IHomeViewImpl implements IHomeView {
    @Override
    public void getHomeAdData(List<HomeAd> homeAds) {

    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }

    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {

    }

    @Override
    public void diZhenInfo(String diZhenStr) {

    }

    @Override
    public void ciBaoInfo(String ciBaoStr) {

    }

    @Override
    public void diZhenCiBaoInfo(String ciBaoStr) {

    }

    @Override
    public void appInfoDataReturn(ApiResponse<AppInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

    }
}
