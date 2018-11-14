package com.cpigeon.cpigeonhelper.modular.menu.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class LogbookViewImpl implements LogbookView {
    @Override
    public void getDatas(ApiResponse<List<LogbookEntity>> datas, String msg, Throwable mThrowable) {

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
