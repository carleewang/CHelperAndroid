package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdaoimpl;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.BsdxSettingEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.PlayAdminView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

public class PlayAdminViewImpl implements PlayAdminView {
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
    public void getPlayAdminData(ApiResponse<List<PlayListEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getBsdxData(ApiResponse<BsdxSettingEntity> dxsetApiResponse, String msg) {

    }

    @Override
    public void subBsdxData(ApiResponse<Object> dxsetApiResponse, String msg) {

    }
}
