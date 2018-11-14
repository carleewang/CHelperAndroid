package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlMessageEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlReviewEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class GyjlViewImpl implements IView {


    public void getLyList(ApiResponse<List<GyjlMessageEntity>> listApiResponse, String msg, Throwable mThrowable) {
    }


    public void getPLList(ApiResponse<List<GyjlReviewEntity>> listApiResponse, String msg, Throwable mThrowable) {
    }

    public void getDelPLResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
    }

    public void getTraPLResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {
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
