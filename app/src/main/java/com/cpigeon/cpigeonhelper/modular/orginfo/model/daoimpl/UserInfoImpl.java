package com.cpigeon.cpigeonhelper.modular.orginfo.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户信息
 * Created by Administrator on 2017/12/19.
 */

public class UserInfoImpl implements IBaseDao {

    public GetServerData<MyInfoEntity> myInfoData;
    public GetServerData<List<GbListEntity>> getGbListDada;//鸽币明细
    public GetServerData<ShareCodeEntity> getShareCodeEntity;//获取创建验证码


    /**
     * 获取我的信息
     *
     * @param userToken
     * @param postParams
     * @param timestamp
     * @param sign
     */
    public void getMyInfoData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getMyInfo(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMyInfoResponse -> {
//                    String str = getMyInfoResponse.string();
//                    Log.d("mythr", "getMyInfoData1: " + str);
                    myInfoData.getdata(getMyInfoResponse);
                }, throwable -> {
                    Log.d(TAG, "getMyInfoData2: " + throwable.getLocalizedMessage());
                    myInfoData.getThrowable(throwable);
                });
    }


    /**
     * 获取鸽币明细
     *
     * @param userToken
     * @param postParams
     * @param timestamp
     * @param sign
     */
    public void getGbMxData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getMyScoresList(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMyInfoResponse -> {
                    getGbListDada.getdata(getMyInfoResponse);
                }, throwable -> {
                    getGbListDada.getThrowable(throwable);
                });
    }


    /**
     * 分享，创建验证码
     *
     * @param userToken
     * @param postParams
     * @param timestamp
     * @param sign
     */
    public void getCreateYaoQingMa(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .createYaoQingMa(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMyInfoResponse -> {
                    getShareCodeEntity.getdata(getMyInfoResponse);
                }, throwable -> {
                    getShareCodeEntity.getThrowable(throwable);
                });
    }


    /**
     * 分享图片视频获取鸽币
     *
     * @param userToken
     * @param postParams
     * @param timestamp
     * @param sign
     */
    public void subShareImgVideo(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getAddScoreByShare(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataResponse -> {
                    Log.d("subShareImgVideo", "subShareImgVideo: " + dataResponse.string().toString());
                }, throwable -> {
                    getShareCodeEntity.getThrowable(throwable);
                });
    }
}
