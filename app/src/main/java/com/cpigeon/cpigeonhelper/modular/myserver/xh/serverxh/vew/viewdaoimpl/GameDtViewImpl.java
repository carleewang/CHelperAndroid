package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdaoimpl;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameDtView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

public class GameDtViewImpl implements GameDtView {
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
    public void getDtList(List<DtListEntity> datas, String msg) {

    }

    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_dt(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg) {

    }
}
