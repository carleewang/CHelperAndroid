package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.impldao;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlMessageEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlReviewEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/22.
 */

public class GyjlImpl implements IBaseDao {

    public GetServerData<List<GyjlMessageEntity>> getServiceLY_XHData;
    public GetServerData<List<GyjlReviewEntity>> getServicePL_XHData;
    public GetServerData<Object> getDelLyPlData;


    //获取用户给协会的留言
    public void getServiceLY_XH(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXH_GetLY(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
//                    Log.d("GyjlImpl", "getServiceLY_XH: "+listApiResponse.toJsonString());
                    getServiceLY_XHData.getdata(listApiResponse);
                }, throwable -> {
                    getServiceLY_XHData.getThrowable(throwable);
                });
    }


    //获取用户给协会的评论
    public void getServicePL_XH(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXH_GetPL(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
//                    Log.d("GyjlImpl", "getServiceLY_XH: " + listApiResponse.toJsonString());
                    getServicePL_XHData.getdata(listApiResponse);
                }, throwable -> {
                    getServicePL_XHData.getThrowable(throwable);
                });
    }


    //删除留言评论
    public void delServiceLyPL_XH(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getDelXH_SCLYPL(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDelLyPlData.getdata(listApiResponse);
                }, throwable -> {
                    getDelLyPlData.getThrowable(throwable);
                });
    }


    //删除回复
    public void delServiceHF_LyPL_XH(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getDelHF_XH_SCLYPL(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDelLyPlData.getdata(listApiResponse);
                }, throwable -> {
                    getDelLyPlData.getThrowable(throwable);
                });
    }


    //恢复留言评论
    public void translateServiceLyPL_XH(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .translateLYPL_XH(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDelLyPlData.getdata(listApiResponse);
                }, throwable -> {
                    getDelLyPlData.getThrowable(throwable);
                });
    }

}
