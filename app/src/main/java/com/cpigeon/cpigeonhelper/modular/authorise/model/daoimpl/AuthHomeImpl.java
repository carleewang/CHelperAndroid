package com.cpigeon.cpigeonhelper.modular.authorise.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AuthHomeEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.dao.IAuthHomeDao;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/21.
 */

public class AuthHomeImpl implements IAuthHomeDao {

    public GetServerData<List<AuthHomeEntity>> getServerData;
    public GetServerData<String> getServerRemoveData;


    /**
     *
     * @param userToken 用户通行验证
     * @param userID 用户ID
     */
    public void  downAuthHomeData(String userToken,int userID){
        RetrofitHelper.getApi()
                .getGYTAuthUsers(userToken,//通行验证
                        userID)//参数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    Log.d(TAG, "downAuthHomeData: ");
                    getServerData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getServerData.getThrowable(throwable);//抛出异常
                });
    }

    /**
     * 提交取消授权比赛
     */
    public void  submitRemoveGYTRaceAuth(String token, Map<String, Object> params,long timestamp,String sign){
        RetrofitHelper.getApi()
                .removeGYTRaceAuth(token,//通行验证
                        params,//参数
                        timestamp,
                        sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    Log.d(TAG, "downAuthHomeData: ");
                    getServerRemoveData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getServerRemoveData.getThrowable(throwable);//抛出异常
                });
    }
}
