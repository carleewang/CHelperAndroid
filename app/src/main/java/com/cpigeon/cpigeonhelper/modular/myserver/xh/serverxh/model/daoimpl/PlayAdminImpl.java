package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.BsdxSettingEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PlayAdminImpl implements IBaseDao {

    public GetServerData<List<PlayListEntity>> getPlayData;//获取比赛管理列表
    public GetServerData<BsdxSettingEntity> getBsdxSettingData;//获取比赛管理设置数据
    public GetServerData<Object> sbuBsdxSetting;//获取比赛管理设置结果

    /**
     * 获取协会比赛列表数据
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void getPlayAdminData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getRaceList_XH(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getPlayData.getdata(listApiResponse);
                }, throwable -> {
                    getPlayData.getThrowable(throwable);
                });
    }


    /**
     * 获取协会比赛短信设置
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void getBsdxSettingData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getBiSaiDuanXin_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getBsdxSettingData.getdata(listApiResponse);
                }, throwable -> {
                    getBsdxSettingData.getThrowable(throwable);
                });
    }


    /**
     * 提交删除比赛申请
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subDelPlayApplyData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .subRaceDeleteSQ_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    sbuBsdxSetting.getdata(listApiResponse);
                }, throwable -> {
                    sbuBsdxSetting.getThrowable(throwable);
                });
    }


    /**
     * 提交协会比赛短信设置
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subBsdxSettingData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setBiSaiDuanXin_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    sbuBsdxSetting.getdata(listApiResponse);
                }, throwable -> {
                    sbuBsdxSetting.getThrowable(throwable);
                });
    }


}
