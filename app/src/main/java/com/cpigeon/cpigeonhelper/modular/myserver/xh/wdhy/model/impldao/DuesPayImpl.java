package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.impldao;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/11.
 */

public class DuesPayImpl implements IBaseDao {

    public GetServerData<Object> getServiceHyData;
    public GetServerData<HFInfoEntity> getHFInfoEntityData;

    //添加处罚记录
    public void setXHHYGL_SetHuiFeiService(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXHHYGL_SetHuiFei(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getServiceHyData.getdata(listApiResponse);
                }, throwable -> {
                    getServiceHyData.getThrowable(throwable);
                });
    }


    //获取年度会费
    public void setXHHYGL_GetHuiFeiService(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXHHYGL_GetHuiFei(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getHFInfoEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getHFInfoEntityData.getThrowable(throwable);
                });
    }
}
