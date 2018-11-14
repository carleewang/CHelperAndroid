package com.cpigeon.cpigeonhelper.modular.usercenter.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.dao.IRegisDao;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/7.
 */

public class RegisImpl implements IRegisDao {

    public SendVerifi sendVerifi;
    private String TAG = "RegisAndPasPresenter";

    public GetServerData<Object> getUserPlayPasDatas;
    public GetServerData<CheckCode> registeredData;//注册

    /**
     * 发送验证
     */
    @Override
    public void sendVerification(Map<String, Object> postParams, long timestamp) {

        RetrofitHelper.getApi()
                .sendVerifyCode(postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkCodeApiResponse -> {
                    if (checkCodeApiResponse.getData() != null) {
                        Log.d(TAG, "数据返回: " + checkCodeApiResponse.getData().getCode());
                    }
                    Log.d(TAG, "sendVerification: cod-->" + checkCodeApiResponse.getErrorCode() + "    msg-->" + checkCodeApiResponse.getMsg());
                    registeredData.getdata(checkCodeApiResponse);
//                    sendVerifi.getVerifiInformation(checkCodeApiResponse.getErrorCode());
                }, throwable -> {
                    registeredData.getThrowable(throwable);
//                    sendVerifi.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 请求开始注册
     *
     * @param postParams 用户信息
     * @param timestamp  时间戳
     */
    public void startRegistration(Map<String, Object> postParams, long timestamp, String sign) {//开始注册

        RetrofitHelper.getApi()
                .userRegist(postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkCodeApiResponse -> {
                    Log.d(TAG, "数据返回: ");
                    registeredData.getdata(checkCodeApiResponse);
                }, throwable -> {
                    registeredData.getThrowable(throwable);//抛出异常
                });
    }

    /**
     * 请求忘记密码
     */
    public void requestForgetPas(Map<String, Object> postParams, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .findPwd(postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkCodeApiResponse -> {
                    Log.d(TAG, "数据返回: ");
                    sendVerifi.getVerifiInformation(checkCodeApiResponse.getErrorCode());

                }, throwable -> {
                    sendVerifi.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 请求重置密码
     */
    public void requestResetPas(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .changePassword(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkCodeApiResponse -> {
                    Log.d(TAG, "数据返回: ");
                    sendVerifi.getVerifiInformation(checkCodeApiResponse.getErrorCode());

                }, throwable -> {
                    sendVerifi.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 请求修改支付密码
     *
     * @param userToken
     * @param postParams
     * @param timestamp
     * @param sign
     */
    public void requestSetUserPayPwd(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setUserPayPwd(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkCodeApiResponse -> {
                    Log.d(TAG, "数据返回: ");

                    getUserPlayPasDatas.getdata(checkCodeApiResponse);
                }, throwable -> {
                    getUserPlayPasDatas.getThrowable(throwable);
                });
    }
}
