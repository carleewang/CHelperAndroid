package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdaoimpl;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.GameGcView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

public class GameGcViewImpl implements GameGcView {
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
    public void getGCList(List<GcListEntity> datas, String msg) {

    }

    @Override
    public void addResults(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void delResults_gc(ApiResponse<Object> listApiResponse, String msg) {

    }

    @Override
    public void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg) {

    }
}
