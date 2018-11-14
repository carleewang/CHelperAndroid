package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;

/**
 * Created by Administrator on 2018/3/14.
 */

public class GYTHomeViewImpl implements GYTHomeView {
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
    public void gytStatisticalData(GYTStatisticalEntity data) {

    }

    @Override
    public void getServiceData(ApiResponse<GYTHomeEntity> dataApiResponse, String msg) {

    }

    @Override
    public void getStatisticalData(ApiResponse<GYTStatisticalEntity> dataApiRespons, String msg) {

    }
}
