package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GP_GetChaZuEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetChaZuListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetJiangJinXianShiBiLiEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GpRpdxSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsSetEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/22.
 */

public class GpSmsImpl implements IBaseDao {

    //============================================公棚服务（入棚短信）===============================================
    public GetServerData<GpRpdxSetEntity> getGpdxSetData;//获取公棚入棚短信设置数据
    public GetServerData<Object> subGpdxSetData;//提交入棚短信设置信息

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
                .getRuPengDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGpdxSetData.getdata(listApiResponse);
                }, throwable -> {
                    getGpdxSetData.getThrowable(throwable);
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
    public void subBsdxSettingData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setRuPengDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    subGpdxSetData.getdata(listApiResponse);
                }, throwable -> {
                    subGpdxSetData.getThrowable(throwable);
                });
    }


    //============================================公棚服务（训赛短信）===============================================
    public GetServerNewData<List<XsListEntity>> getXsListData;//获取公棚训赛项目列表
    public GetServerData<XsSmsDetailEntity> getXsSmsDetailEntity;//获取公棚训赛项目列表
    public GetServerData<XsSmsSetEntity> getXsSmsSetEntity;//获取训赛短信设置信息
    public GetServerData<Object> getSubXsSmsSet;//提交训赛短信设置信息  (设置公棚插组指定)

    /**
     * 获取公棚训赛项目列表
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subGetXsSmsListData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXunSaiList_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getXsListData.getdata(listApiResponse, null);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getXsListData.getdata(null, throwable);
                });
    }


//================================================插组指定(公棚)============================================================

    public GetServerData<List<DesignatedListEntity>> getDesignatedList_gp;//获取公棚插组指定列表
    public GetServerData<DesignatedSetEntity> getDesignatedDetails_gp;//获取公棚插组指定列表
    public GetServerData<List<GetChaZuListEntity>> getGetChaZuList;//获取公棚插组指定列表
    public GetServerData<GP_GetChaZuEntity> getGP_GetChaZuEntity;//获取公棚插组指定列表
    public GetServerData<Object> getGP_SetChaZu;//获取公棚插组指定列表
    public GetServerData<GetJiangJinXianShiBiLiEntity> getJiangJinXianShiBiLi;//获取公棚插组指定列表

    /**
     * 获取公棚插组指定列表
     */
    public void subDesignatedList_gp(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDesignatedList_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDesignatedList_gp.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getDesignatedList_gp.getThrowable(throwable);
                });
    }


    /**
     * 获取公棚插组指定详情   舍弃
     */
    public void subDesignatedDetails_gp(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDesignatedDetails_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDesignatedDetails_gp.getdata(listApiResponse);
                }, throwable -> {
                    getDesignatedDetails_gp.getThrowable(throwable);
                });
    }


    /**
     * 获取公棚插组指定详情
     */
    public void subGP_GetChaZuList(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGP_GetChaZuList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGetChaZuList.getdata(listApiResponse);
                }, throwable -> {
                    getGetChaZuList.getThrowable(throwable);
                });
    }


    /**
     * 获取公棚插组指定详情
     */
    public void subGP_GetChaZu(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getGP_GetChaZu(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGP_GetChaZuEntity.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGP_GetChaZuEntity.getThrowable(throwable);
                });
    }

    /**
     * 获取公棚插组指定详情
     */
    public void subXH_GetChaZu(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXH_GetChaZu(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGP_GetChaZuEntity.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGP_GetChaZuEntity.getThrowable(throwable);
                });
    }

    /**
     * 设置 公棚插组指定
     */
    public void subGP_SetChaZu(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setGP_SetChaZu(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGP_SetChaZu.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGP_SetChaZu.getThrowable(throwable);
                });
    }


    /**
     * 设置 协会插组指定
     */
    public void subXH_SetChaZu(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setXH_SetChaZu(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGP_SetChaZu.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGP_SetChaZu.getThrowable(throwable);
                });
    }

    /**
     * 设置 公棚插组指定
     */
    public void subGP_SetJiangJinXianShiBiLi(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setGP_SetJiangJinXianShiBiLi(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGP_SetChaZu.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGP_SetChaZu.getThrowable(throwable);
                });
    }


    /**
     * 设置 公棚插组指定
     */
    public void subXH_SetJiangJinXianShiBiLi(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setXH_SetJiangJinXianShiBiLi(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGP_SetChaZu.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGP_SetChaZu.getThrowable(throwable);
                });
    }

    /**
     * 设置 公棚插组指定
     */
    public void subGP_GetJiangJinXianShiBiLi(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setGP_GetJiangJinXianShiBiLi(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getJiangJinXianShiBiLi.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getJiangJinXianShiBiLi.getThrowable(throwable);
                });
    }


    /**
     * 协会 获取奖金比例
     */
    public void subXH_GetJiangJinXianShiBiLi(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setXH_GetJiangJinXianShiBiLi(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getJiangJinXianShiBiLi.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getJiangJinXianShiBiLi.getThrowable(throwable);
                });
    }


    /**
     * 设置公棚插组指定  舍弃
     */
    public void subSetDesignated_gp(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setDesignated_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getSubXsSmsSet.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getSubXsSmsSet.getThrowable(throwable);

                });
    }

//================================================插组指定(协会)============================================================

    /**
     * 获取公棚插组指定列表
     */
    public void subDesignatedList_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDesignatedList_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getDesignatedList_gp.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getDesignatedList_gp.getThrowable(throwable);
                });
    }


    /**
     * 获取公棚插组指定详情
     */
    public void subDesignatedDetails_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDesignatedDetails_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getGetChaZuList.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getGetChaZuList.getThrowable(throwable);
                });
    }


    /**
     * 设置公棚插组指定
     */
    public void subSetDesignated_xh(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setDesignated_xh(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getSubXsSmsSet.getdata(listApiResponse);
                    Log.d("xxxxiao", "subGetXsSmsListData: " + listApiResponse.toJsonString());
                }, throwable -> {
//                    Log.d("xxxxiao", "subGetXsSmsListData: " + throwable.getLocalizedMessage());
                    getSubXsSmsSet.getThrowable(throwable);

                });
    }


    /**
     * 获取训赛项目详细
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subGetXsSmsDetailData(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXunSaiDetail_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getXsSmsDetailEntity.getdata(listApiResponse);
                }, throwable -> {
                    getXsSmsDetailEntity.getThrowable(throwable);
                });
    }

    /**
     * 获取训赛短信设置信息
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void getSmsSetData_XS(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getXunSaiDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getXsSmsSetEntity.getdata(listApiResponse);
                }, throwable -> {
                    getXsSmsSetEntity.getThrowable(throwable);
                });
    }

    /**
     * 提交训赛短信设置信息
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subSmsSetData_XS(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setXunSaiDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getSubXsSmsSet.getdata(listApiResponse);
                }, throwable -> {
                    getSubXsSmsSet.getThrowable(throwable);
                });
    }

    //============================================公棚服务（上笼短信）===============================================
    public GetServerData<List<SlListEntity>> getListData_SL;//获取上笼信息列表
    public GetServerData<SlSmsSetEntity> getSmsSetData_SL;//获取上笼短信设置信息
    public GetServerData<Object> subSmsSetData_SL;//提交上笼短信设置信息
    public GetServerData<Object> ceShiSmsSetData_SL;//提交发送短信通道测试短信

    /**
     * 获取上笼信息列表
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void getSmsListData_SL(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getShangLongList_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getListData_SL.getdata(listApiResponse);
                }, throwable -> {
                    getListData_SL.getThrowable(throwable);
                });
    }

    /**
     * 获取上笼短信设置信息
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void getSmsSetData_SL(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getShangLongDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getSmsSetData_SL.getdata(listApiResponse);
                }, throwable -> {
                    getSmsSetData_SL.getThrowable(throwable);
                });
    }

    /**
     * 提交上笼短信设置信息
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subSmsSetData_SL(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setShangLongDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    subSmsSetData_SL.getdata(listApiResponse);
                }, throwable -> {
                    subSmsSetData_SL.getThrowable(throwable);
                });
    }

    /**
     * 提交发送短信通道测试短信
     *
     * @param token
     * @param params
     * @param timestamp
     * @param sign
     */
    public void subSmsCeShiData_SL(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .subCeShiDuanXin_gp(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    ceShiSmsSetData_SL.getdata(listApiResponse);
                }, throwable -> {
                    ceShiSmsSetData_SL.getThrowable(throwable);
                });
    }

}
