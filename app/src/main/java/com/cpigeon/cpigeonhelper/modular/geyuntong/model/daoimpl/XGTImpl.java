package com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 训鸽通Model
 * Created by Administrator on 2017/12/7.
 */
public class XGTImpl implements IBaseDao {

    public GetServerData getServerData;
    public GetServerData<XGTEntity> getXGTEntity;

    public GetServerData<List<XGTOpenAndRenewEntity>> getXGTOpenAndRenewEntity;

    /**
     * 上传身份证资料
     *
     * @param token       通行验证
     * @param requestBody 参数
     * @param timestamp   时间戳
     * @param sign        签名
     */
    public void uploadIdCardInfo(String token, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .openXGT(token,//通行验证
                        requestBody,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadIdCardInfoData -> {
                    if (uploadIdCardInfoData != null) {
                        getServerData.getdata(uploadIdCardInfoData);
                    }
                }, throwable -> {
                    if (throwable != null) {
                        getServerData.getThrowable(throwable);
                    }
                });
    }

    //获取训鸽通信息
    public void getXGTInfoData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXGTInfos(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadIdCardInfoData -> {
                    getXGTEntity.getdata(uploadIdCardInfoData);
                }, throwable -> {
                    getXGTEntity.getThrowable(throwable);
                });
    }

    //获取训鸽通开通信息
    public void getXGTInfoOpenData(String token, Map<String, Object> params, long timestamp, String sign) {
//    public void getXGTInfoOpenData() {
        RetrofitHelper.getApi()
                .getXGTOpenInfos(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
//                .getXGTOpenInfos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadIdCardInfoData -> {
                    getXGTOpenAndRenewEntity.getdata(uploadIdCardInfoData);
                }, throwable -> {
                    getXGTOpenAndRenewEntity.getThrowable(throwable);
                });
    }


    //获取训鸽通续费信息
    public void getXGTInfoRenewData(String token, Map<String, Object> params, long timestamp, String sign) {
//    public void getXGTInfoRenewData() {
        RetrofitHelper.getApi()
                .getXGTrenewInfos(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
//                .getXGTRenewInfos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadIdCardInfoData -> {
                    getXGTOpenAndRenewEntity.getdata(uploadIdCardInfoData);
                }, throwable -> {
                    getXGTOpenAndRenewEntity.getThrowable(throwable);
                });
    }
}
