package com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/25.
 */

public class FlyingViewImpl implements FlyingView {


    @Override
    public void addFlyingFlyingData(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getFlyingData(ApiResponse<List<FlyingAreaEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getFlyingDataNull(String msg) {

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
}
