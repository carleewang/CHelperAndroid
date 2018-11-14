package com.cpigeon.cpigeonhelper.modular.authorise.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.dao.IAddAuthDao;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/21.
 */

public class AddAuthImpl implements IAddAuthDao {

    public GetServerData<List<AddAuthEntity>> getServerAddAuthData;//请求用户信息回调



    public GetServerData<Object> getSMSData;//请求比赛授权信息回调

    /**
     * 开始请求通过手机号搜索用户
     * @param token
     * @param phone
     */
    public void requestAddAuthData(String token,String phone){
        RetrofitHelper.getApi()
                .queryUserByPhone(token,//通行验证
                        phone )//用户输入电话号码
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addAuthEntityApiResponse -> {
                    Log.d(TAG, "downAuthHomeData: ");
                    getServerAddAuthData.getdata(addAuthEntityApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getServerAddAuthData.getThrowable(throwable);//抛出异常
                });

    }




    /**
     * 短信通知用户
     */
    public void requestSMSCall(String  userToken,Map<String, Object> params,long timestamp,String sign){
        RetrofitHelper.getApi()
                .SendMSG(userToken,//通行验证
                        params ,//参数
                        timestamp,//时间戳
                        sign)//签名检查
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addAuthEntityApiResponse -> {
                    Log.d(TAG, "downAuthHomeData: ");
                    getSMSData.getdata(addAuthEntityApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getSMSData.getThrowable(throwable);//抛出异常
                });

    }
}
