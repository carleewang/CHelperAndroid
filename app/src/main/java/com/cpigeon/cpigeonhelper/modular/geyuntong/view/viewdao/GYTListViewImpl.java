package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;

/**
 * Created by Administrator on 2018/2/25.
 */

public class GYTListViewImpl implements GYTListView {
    @Override
    public void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getGYTRaceLists(List<GeYunTong> geYunTongDatas) {

    }

    @Override
    public void getReturnMsg(String msg) {

    }

    @Override
    public void addPlaySuccess() {

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
