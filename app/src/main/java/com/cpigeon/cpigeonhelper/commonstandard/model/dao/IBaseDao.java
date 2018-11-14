package com.cpigeon.cpigeonhelper.commonstandard.model.dao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;

/**
 * 基类BaseDao,MVP的M
 * Created by Administrator on 2017/4/6.
 */

public interface IBaseDao {
    public String TAG = "print";

    interface OnCompleteListener<T> {
        void onSuccess(T data);

        void onFail(String msg);
    }

    /**
     * 获取服务器接口
     *
     * @param <T> 实体类
     */
    interface GetServerData<T> {
        void getdata(ApiResponse<T> tApiResponse);//获取数据

        void getThrowable(Throwable throwable);//抛出异常
    }

    /**
     * 获取服务器接口
     *
     * @param <T> 实体类
     */
    interface GetServerNewData<T> {
        void getdata(ApiResponse<T> tApiResponse, Throwable throwable);//获取数据
    }


    interface GetServerDatas<T> {
        void getdata(T tApiResponse);//获取数据

        void getThrowable(Throwable throwable);//抛出异常
    }

}
