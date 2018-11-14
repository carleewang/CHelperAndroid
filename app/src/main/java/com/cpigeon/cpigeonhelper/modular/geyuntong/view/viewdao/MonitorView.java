package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */

public interface MonitorView extends IView {
    void imgOrVideoData(ApiResponse<List<ImgOrVideoEntity>> listApiResponse, String msg, Throwable mThrowable);

    void succeed();//(结束比赛成功)

    void openMonitorResults(ApiResponse<Object> dataApiResponse, String msg);//开启监控结果

    void fail();//（结束比赛失败）

    void locationInfoDatas(List<LocationInfoEntity> datas);//获取监控定位数据报表

    void getRaceLocation(List<RaceLocationEntity> raceLocationData);//获取鸽运通定位信息

    void getKjLcData(ApiResponse<KjLcEntity> dataApiResponse, String msg, Throwable mThrowable);//结束监控  dialog 显示信息

    void getKjLcInfoData(ApiResponse<KjLcInfoEntity> dataApiResponse, String msg, Throwable mThrowable);//监控未结束 获取空距，里程

    void gytRaceChkData(ApiResponse<Object> dataApiResponse, String msg, Throwable mThrowable);//比赛监控授权检查

    void gytOfflineUploadData(ApiResponse<Object> dataApiResponse, String msg, Throwable mThrowable);//离线上传
}
