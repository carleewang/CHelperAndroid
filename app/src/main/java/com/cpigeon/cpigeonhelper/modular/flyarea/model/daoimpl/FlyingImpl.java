package com.cpigeon.cpigeonhelper.modular.flyarea.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.dao.IFlyingDao;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/25.
 */

public class FlyingImpl implements IFlyingDao {
    public GetServerData<List<FlyingAreaEntity>> flyingAreasData;//我的司放地列表数据
    public GetServerData<Object> addFlyingAreasData;//添加司放地(删除，修改司放地接口)


    /**
     * 获取我的(参考)司放地列表
     *
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void requestFlyingAres(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getFlyingAreas(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    flyingAreasData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    flyingAreasData.getThrowable(throwable);//抛出异常
                });
    }



    /**
     * 添加司放地提交
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
   public  void submitAddFlyingArea(String token, Map<String, Object> params, long timestamp, String sign){
       RetrofitHelper.getApi()
               .createFlyingArea(token,//通行验证
                       params,//参数
                       timestamp,// 时间戳
                       sign)// 签名

               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(deviceBeanApiResponse -> {
                   android.util.Log.d(TAG, "dowdGYTData: ");
                   addFlyingAreasData.getdata(deviceBeanApiResponse);//返回服务器数据

               }, throwable -> {
                   Log.d(TAG, "isLogin: 开始检测4");
                   addFlyingAreasData.getThrowable(throwable);//抛出异常
               });
   }


    /**
     * 修改司放地
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void  submitAmendFlyingArea(String token, Map<String, Object> params, long timestamp, String sign){
        RetrofitHelper.getApi()
                .modifyFlyingArea(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    addFlyingAreasData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    addFlyingAreasData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 删除司放地
     * @param token     通行验证
     * @param params    参数
     * @param timestamp 时间戳
     * @param sign      签名
     */
    public void  delAmendFlyingArea(String token, Map<String, Object> params, long timestamp, String sign){
        RetrofitHelper.getApi()
                .deleteFlyingArea(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceBeanApiResponse -> {
                    android.util.Log.d(TAG, "dowdGYTData: ");
                    addFlyingAreasData.getdata(deviceBeanApiResponse);//返回服务器数据

                }, throwable -> {
                    Log.d(TAG, "isLogin: 开始检测4");
                    addFlyingAreasData.getThrowable(throwable);//抛出异常
                });
    }
}
