package com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.dao.IMonitorDao;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/11.
 */

public class MonitorImpl implements IMonitorDao {

    public GetServerData<List<ImgOrVideoEntity>> getServerData;//获取监控图片，视频数据
    public GetServerData<List<LocationInfoEntity>> getLocationInfoDatas;//获取监控定位数据报表
    public GetServerData<List<RaceLocationEntity>> getRaceLocation;//.获取鸽运通定位信息
    public GetServerData<Object> getStartMonitorData;//开始监控
    public GetServerData<KjLcEntity> getKjLcEntity;//开始监控
    public GetServerData<KjLcInfoEntity> getKjLcInfoEntity;//开始监控
    public GetServerData<Object> gytRaceChkService;//开始监控
    public GetServerData<Object> gytOfflineUploadData;//离线上传


    /**
     * 获取监控图片，视频
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getMonitorImgOrVideo(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGYTRaceImageOrVideo(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    getServerData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    getServerData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 开始监控
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getStartMonitor(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGYTStartMonitor(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    getStartMonitorData.getdata(startApiResponse);
                }, throwable -> {

                    getStartMonitorData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 结束监控
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getStopMonitor(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGYTStopMonitor(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    getStartMonitorData.getdata(startApiResponse);
                }, throwable -> {

                    getStartMonitorData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 获取监控定位数据报表
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getLocationInfoData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGYTLocationInfoReports(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResponse -> {
                    Log.d(TAG, "数据报表--1》" + apiResponse.toString());

                    getLocationInfoDatas.getdata(apiResponse);
                }, throwable -> {
                    Log.d(TAG, "数据报表--2》" + throwable.getLocalizedMessage());
                    getLocationInfoDatas.getThrowable(throwable);//抛出异常
                });
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ApiConstants.BASE_URL)
//
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiService service = retrofit.create(ApiService.class);
//
//        service.getGYTLocationInfoReports(token,//通行验证
//                params,//参数
//                timestamp,// 时间戳
//                sign).enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                Log.d(TAG, "数据报表---1》: " + response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//
//                Log.d(TAG, "数据报表---2》: " + t.getLocalizedMessage());
//            }
//        });
    }


    /**
     * 51.获取鸽运通定位信息 GetGTYRaceLocation
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getGTYRaceLocation(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGTYRaceLocation(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    getRaceLocation.getdata(startApiResponse);
                }, throwable -> {

                    getRaceLocation.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 结束监控  dialog 显示信息
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getGTYKjLc(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getPlaybackInfo(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    getKjLcEntity.getdata(startApiResponse);
                }, throwable -> {
                    getKjLcEntity.getThrowable(throwable);//抛出异常
                });
    }

    /**
     * 监控未结束 获取空距，里程
     */
    public void getGTYKjLcInfo(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getMonitorInfo(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    getKjLcInfoEntity.getdata(startApiResponse);
                }, throwable -> {
                    getKjLcInfoEntity.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 监控未结束 比赛监控授权检查
     */
    public void gytRaceChk(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGYTRaceCHK(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    gytRaceChkService.getdata(startApiResponse);
                }, throwable -> {
                    gytRaceChkService.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 监控未结束 离线上传
     */
    public void gytOfflineUploadService(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGYTOfflineUpload(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(startApiResponse -> {
                    gytOfflineUploadData.getdata(startApiResponse);
                }, throwable -> {
                    gytOfflineUploadData.getThrowable(throwable);//抛出异常
                });
    }
}
