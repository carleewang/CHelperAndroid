package com.cpigeon.cpigeonhelper.modular.usercenter.model.daoimpl;


import android.support.annotation.NonNull;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.LogInfoBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.dao.ILoginDao;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/6.
 */

public class IoginImpl implements ILoginDao {


    public GetLoginDownData getdata;//接口回调，用于数据传输
    public GetUserImgUrl getImgUrl;//获取头像

    public GetSingleLoginCheck singleLoginCheck;//单点登录检查接口回调

    public GetServerData<LogInfoBean>  serverData;//服务器返回数据

    private String TAG = "print";

    /**
     * 开始登录
     *
     * @param postParams
     * @param timestamp
     */
    @Override
    public void loginStart(Map<String, Object> postParams, long timestamp) {

        RetrofitHelper.getApi()
                .login(postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams))//请求服务器后段url
                .subscribeOn(Schedulers.io())//订阅（调度程序）
                .observeOn(AndroidSchedulers.mainThread())//观察（调度程序.主线程）
                .subscribe(userBeanApiResponse -> {
                    //登录完成回调
                    getdata.getdata(userBeanApiResponse);
                }, throwable -> {
                    getdata.getThrowable(throwable);//抛出异常
                });
    }

    /**
     * 输入用户名显示头像
     */
    @Override
    public void getUserImg(String strUserName) {
        RetrofitHelper.getApi()
                .getUserHeadImg(strUserName.trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<String>>() {
                    @Override
                    public void accept(@NonNull ApiResponse<String> stringApiResponse) throws Exception {
                        getImgUrl.getUrlData(stringApiResponse.getData());
                        Log.d(TAG, "accept: 设置图片了");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: 抛出异常了");
                    }
                });
    }


    /**
     * 获取登录信息的情况
     *
     * @return userToken:用户
     * params：不要参数
     * sign：签名
     * timestamp：时间戳
     */
    @Override
    public void isLogin(Map<String, Object> params, String sign, long timestamp) {
        RetrofitHelper.getApi()
                .getGetLoginInfo1(params, sign, timestamp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {

//                    ApiResponse<LogInfoBean> obje = objectApiResponse;

                    serverData.getdata(objectApiResponse);

//                    if (objectApiResponse.getErrorCode()==0) {
//                        singleLoginCheck.getdata(objectApiResponse.getData().getDeviceid());
//                        Log.d(TAG, "isLogin: 单点登录检查成功，开始回调");
//                    } else {
//                        Log.d(TAG, "isLogin: 单点登录返回设备ID 为空");
//                    }


                }, throwable -> {
                    Log.d(TAG, "isLogin: 单点登录检查失败，开始回调");
//                    singleLoginCheck.getThrowable(throwable);//抛出异常

                    serverData.getThrowable(throwable);
                });
    }
}
