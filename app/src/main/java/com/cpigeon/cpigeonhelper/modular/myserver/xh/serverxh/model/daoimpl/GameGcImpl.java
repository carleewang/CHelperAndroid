package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/12.
 */

public class GameGcImpl implements IBaseDao {

    public GetServerData<List<GcListEntity>> getGcListEntityData;//获取数据
    public GetServerData<Object> getAddSSGC;//获取添加赛事规程结果
    public GetServerData<Object> getDelXhdt;//获取删除赛事规程结果
    public GetServerData<GcItemEntity> getGcItemEntityData;//获取添加赛事规程结果

    /**
     * 获取协会规程（赛事规程）信息列表
     */
    public void getGuiChengList_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGuiChengList_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGcListEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getGcListEntityData.getThrowable(throwable);
                });
    }


    /**
     * 添加赛事规程
     */
    public void addSSGC_xh(String token, RequestBody body, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .guiChengAdd_xh(token,//通行验证
                        body,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getAddSSGC.getdata(listApiResponse);
                }, throwable -> {
                    getAddSSGC.getThrowable(throwable);
                });
    }

    /**
     * 赛事规程详情
     */
    public void getGuiChengItemInfo_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGuiChengDetail_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGcItemEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getGcItemEntityData.getThrowable(throwable);
                });
    }

    /**
     * 修改赛事规程
     */
    public void editSSGC_xh(String token, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .guiChengEdit_xh(token,//通行验证
                        requestBody,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getAddSSGC.getdata(listApiResponse);
                }, throwable -> {
                    getAddSSGC.getThrowable(throwable);
                });
    }

    /**
     * 删除赛事规程
     */
    public void delSsgc_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .delGcEdit_XH(token,//通行验证
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
