package com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.dao.IGYTListDao;


import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/27.
 */

public class GYTListImpl implements IGYTListDao {

    public GetServerData<List<GeYunTong>> getServerData;//获取数据
    public GetServerData<Object> getDelServerData;//获取数据

    /**
     * 下载鸽运通比赛列表
     */
    @Override
    public void downGYTRaceList(String userToken, Map<String, Object> urlParams) {
        RetrofitHelper.getApi()
                .getGeYunTongRaceList(AssociationData.getUserToken(), urlParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getServerData.getdata(listApiResponse);
                        }
                        , throwable -> {
                            getServerData.getThrowable(throwable);
                        });
    }

    /**
     * 批量删除鸽运通比赛列表
     */
    public void requestDeleteGYTRaces(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .deleteGYTRaces(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    getDelServerData.getdata(deviceBeanApiResponse);//返回服务器数据
                }, throwable -> {
                    getDelServerData.getThrowable(throwable);//抛出异常
                });
    }
    
    /**
     * 删除鸽运通比赛
     */
    public void requestDeleteGYTRace(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .deleteGYTRace(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    getDelServerData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getDelServerData.getThrowable(throwable);//抛出异常
                });
    }


    public GetServerData<Object> serverData;//


    /**
     * 提交添加比赛数据
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void submitAddGytPlay(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .createGYTRace(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    serverData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    serverData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 提交修改比赛
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void submitUpdateGYTRace(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .updateGYTRace(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    serverData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    serverData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 17.鸽运通比赛授权确认
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void getGYTAuthConfirm(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .gytAuthConfirm(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    serverData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    serverData.getThrowable(throwable);//抛出异常
                });
    }
}
