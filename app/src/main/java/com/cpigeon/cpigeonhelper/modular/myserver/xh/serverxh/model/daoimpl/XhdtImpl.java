package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/14.
 */

public class XhdtImpl implements IBaseDao {

    public GetServerData<List<DtListEntity>> getTcListEntityData;//获取数据
    public GetServerData<DtItemEntity> getDtItemEntityData;//获取添加赛事规程结果
    public GetServerData<Object> getAddXhdt;//获取添加赛事规程结果
    public GetServerData<Object> getDelXhdt;//删除添加赛事规程结果


    /**
     * 获取协会动态信息列表
     */
    public void getDongtaiList_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDongTaiList_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getTcListEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getTcListEntityData.getThrowable(throwable);
                });
    }


    /**
     * 协会动态详情
     */
    public void getXhdtItemInfo_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDongTaiDetail_XH(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDtItemEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getDtItemEntityData.getThrowable(throwable);
                });
    }


    /**
     * 添加协会动态
     */
    public void addXhdt_xh(String token, RequestBody body, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .dongTaiAdd_XH(token,//通行验证
                        body,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getAddXhdt.getdata(listApiResponse);
                }, throwable -> {
                    getAddXhdt.getThrowable(throwable);
                });
    }


    /**
     * 修改协会动态
     */
    public void editXhdt_xh(String token, RequestBody body, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .editDongTaiEdit_XH(token,//通行验证
                        body,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getAddXhdt.getdata(listApiResponse);
                }, throwable -> {
                    getAddXhdt.getThrowable(throwable);
                });
    }

    /**
     * 删除协会动态
     */
    public void delXhdt_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .delDongTaiEdit_XH(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDelXhdt.getdata(listApiResponse);
                }, throwable -> {
                    getDelXhdt.getThrowable(throwable);
                });
    }


}
