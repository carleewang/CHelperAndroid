package com.cpigeon.cpigeonhelper.modular.home.model.daoimpl;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.DiZhenEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.dao.IHomeDao;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/5.
 */

public class HomeImpl implements IHomeDao {

    //    private List<HomeAd> homeAds = new ArrayList<>();//数据保存
    public GetDownData getdata;//接口回调，用于数据传输

    public GetServerData<List<GeYunTong>> getServerData;//获取数据

    public GetServerDatas<DiZhenEntity> getDiZhenInfo;//地震信息
    public GetServerDatas<DiZhenEntity> getCiBaoInfo;//磁暴信息
    public GetServerDatas<DiZhenEntity> getDiZhenCiBaoInfo;//磁暴信息

    /**
     * 开始下载数据
     */
    @Override
    public void downBannerData() {
        RetrofitHelper.getApi()
                .getAllAd()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            if (listApiResponse.isStatus()) {

//                                for (HomeAd ad : listApiResponse.getData()) {
//                                    homeAds.add(new HomeAd(ad.getAdImageUrl()));
//                                }
//                                Log.d("print", "downData: 数据下载完成");
                                //数据下载，接口回调
                                getdata.getdata(listApiResponse.getData());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                getdata.getThrowable(throwable);
                            }
                        }

                );
    }


    /**
     * 下载鸽运通比赛列表
     */
    @Override
    public void downGYTRaceList(String userToken, Map<String, Object> urlParams) {
        RetrofitHelper.getApi()
                .getGeYunTongRaceList(AssociationData.getUserToken(), urlParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getServerData.getdata(listApiResponse);
                        }
                        , throwable -> {
                            getServerData.getThrowable(throwable);
                        });
    }


    /**
     * 获取地震信息
     */
    public void getDizhen() {
        RetrofitHelper.getApi()
                .getGTYRaceLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getDiZhenInfo.getdata(listApiResponse);
                        }
                        , throwable -> {
                            getDiZhenInfo.getThrowable(throwable);
                        });
    }


    /**
     * 获取磁暴信息
     */
    public void getCiBao() {
        RetrofitHelper.getApi()
                .getTaiYangCiBao()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getCiBaoInfo.getdata(listApiResponse);
                        }
                        , throwable -> {
                            getCiBaoInfo.getThrowable(throwable);
                        });
    }


    /**
     * 获取地震磁暴信息
     */
    public void getDiZhenCiBao() {
        RetrofitHelper.getApi()
                .getDiZhenCiBao()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getDiZhenCiBaoInfo.getdata(listApiResponse);
                        }
                        , throwable -> {
                            getDiZhenCiBaoInfo.getThrowable(throwable);
                        });
    }


    public GetServerData<AppInfoEntity> getAppInfoData;

    //APP使用信息设置
    public void setAppInfoData(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getSetAppInfo(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
//                    Log.d("GyjlImpl", "getServiceLY_XH: "+listApiResponse.toJsonString());
                    getAppInfoData.getdata(listApiResponse);
                }, throwable -> {
                    getAppInfoData.getThrowable(throwable);
                });
    }

}
