package com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9.
 */

public class AuthRaceListViewImpl implements AuthRaceListView {
    @Override
    public void getRaceListDataIsNo() {

    }

    @Override
    public void getRaceListData(List<GeYunTong> data) {

    }

    @Override
    public void addAuthData(ApiResponse<String> dataApiResponse, String msg, Throwable mThrowable) {

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
