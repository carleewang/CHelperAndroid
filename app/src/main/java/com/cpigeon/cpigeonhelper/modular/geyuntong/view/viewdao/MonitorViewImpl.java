package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MonitorViewImpl implements MonitorView{
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
    public void imgOrVideoData(ApiResponse<List<ImgOrVideoEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void succeed() {

    }

    @Override
    public void openMonitorResults(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void fail() {

    }

    @Override
    public void locationInfoDatas(List<LocationInfoEntity> datas) {

    }

    @Override
    public void getRaceLocation(List<RaceLocationEntity> raceLocationData) {

    }

    //鸽运通监控获取空距里程
    @Override
    public void getKjLcData(ApiResponse<KjLcEntity> dataApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getKjLcInfoData(ApiResponse<KjLcInfoEntity> dataApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void gytRaceChkData(ApiResponse<Object> dataApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void gytOfflineUploadData(ApiResponse<Object> dataApiResponse, String msg, Throwable mThrowable) {

    }
}
