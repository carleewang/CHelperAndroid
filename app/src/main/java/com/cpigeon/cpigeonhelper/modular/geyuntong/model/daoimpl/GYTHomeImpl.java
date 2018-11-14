package com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.dao.IGYTHomeDao;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/20.
 */

public class GYTHomeImpl implements IGYTHomeDao {
    public GetServerData<GYTHomeEntity> getServerData;
    public GetServerData<GYTStatisticalEntity> getServerStatisticalData;
    private String TAG = "print";

    /**
     * 获取我的鸽运通信息
     * @param token  通行验证
     * @param params 参数
     */
    @Override
    public void dowdGYTData(String token, Map<String, Object> params) {
        RetrofitHelper.getApi()
                .getGYTInfo(token,//通行验证
                        params)//参数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    getServerData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getServerData.getThrowable(throwable);//抛出异常
                });
    }

    /**
     * 获取鸽运通用户的统计信息
     *
     * @param token  通行验证
     * @param params 参数
     */
    @Override
    public void downGYTStatisticalData(String token, Map<String, Object> params) {
        RetrofitHelper.getApi()
                .getGYTStatisticalData(token,//通行验证
                        params)//参数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    getServerStatisticalData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getServerStatisticalData.getThrowable(throwable);//抛出异常
                });
    }
}
