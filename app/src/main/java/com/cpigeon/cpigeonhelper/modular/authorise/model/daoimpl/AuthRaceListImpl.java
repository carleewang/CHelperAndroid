package com.cpigeon.cpigeonhelper.modular.authorise.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.authorise.model.dao.IAuthRaceListDao;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/22.
 */

public class AuthRaceListImpl implements IAuthRaceListDao {


    public GetServerData<List<GeYunTong>> listGetServerData;
    public GetServerData<String> getGYTRaceAuthData;//请求比赛授权信息回调
    /**
     * 获取鸽运通可以授权的比赛列表数据
     *
     * @param token
     * @param params
     */
    public void requestAuthRaceList(String token, Map<String, Object> params) {
        RetrofitHelper.getApi()
                .getGeYunTongRaceList(token,//通行验证
                        params)//参数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    Log.d(TAG, "downAuthHomeData: -----" + deviceBeanApiResponse.getData().size());
                    listGetServerData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4" + throwable.getLocalizedMessage());
                    listGetServerData.getThrowable(throwable);//抛出异常
                });

    }




    /**
     * .鸽运通比赛授权(接口不一样 暂时舍弃)
     *
     * 请求鸽运通比赛授权信息
     * token,//通行验证
     * params, //用户输入电话号码
     * timestamp,//时间戳
     * sign)//签名
     */
    public  void requestGYTRaceAuth(String token,Map<String, Object> params,long timestamp,String sign){
        RetrofitHelper.getApi()
                .gytRaceAuth(token,//通行验证
                        params, //用户输入电话号码
                        timestamp,//时间戳
                        sign)//签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addAuthEntityApiResponse -> {
                    Log.d(TAG, "downAuthHomeData: ");
                    getGYTRaceAuthData.getdata(addAuthEntityApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    getGYTRaceAuthData.getThrowable(throwable);//抛出异常
                });
    }

}
