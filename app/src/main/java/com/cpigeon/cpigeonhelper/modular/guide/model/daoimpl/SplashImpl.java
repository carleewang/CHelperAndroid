package com.cpigeon.cpigeonhelper.modular.guide.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.DeviceBean;
import com.cpigeon.cpigeonhelper.modular.guide.model.dao.ISplashDao;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cpigeon.cpigeonhelper.common.db.AssociationData.DEV_ID;

/**
 * Created by Administrator on 2017/9/9.
 */

public class SplashImpl implements ISplashDao{

    public GetServerData<DeviceBean> getServerData;
    private String  TAG  = "print";


    /**
     * 查看是否已经登录过
     * @param userToken  用户登录token
     * @param params 用户资料
     * @param timestamp  时间戳
     * @param apiSign  签名
     */
    @Override
    public  void  isLoginData( String userToken,Map<String, Object> params,long timestamp,String apiSign ){
        Log.d(TAG, "isLogin: 开始检测2");
        RetrofitHelper.getApi()
                .getDeviceInfo(userToken,
                        params, timestamp, apiSign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    Log.d(TAG, "isLogin: 开始检测3");
                    getServerData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getServerData.getThrowable(throwable);//抛出异常
                });
    }
}
