package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.impldao;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildMemberEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.IssueFoot;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PieChartFootEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/6/15.
 */

public class FootAdminImpl implements IBaseDao {

    public GetServerData<FootAdminListDataEntity> getFootAdminListEntityData;
    public GetServerData<FootAdminEntity> getFootAdminData;
    public GetServerData<Object> getResultsData;
    public GetServerData<FootPriceEntity> getFootPriceResultsData;
    public GetServerData<IssueFoot> getIssueFootData;
    public GetServerData<PieChartFootEntity> getPieChartFootData;
    public GetServerData<List<AgentTakePlaceListEntity>> getAgentTakePlaceListData;
    public GetServerData<ChildFoodAdminListEntity> getXHHYGL_SJGH_GetFootListData;
    public GetServerData<List<ChildMemberEntity>> getMemberListData;
    public GetServerData<AgentTakePlaceListEntity> getAgentTakePlaceData;

    //获取协会会员列表
    public void getXHHYGL_ZHGL_GetData(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_GetList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH: " + listApiResponse.toJsonString());
                    getFootAdminListEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getFootAdminListEntityData.getThrowable(throwable);
                });
    }

    //获取足环详细
    public void getXHHYGL_ZHGL_GetDetail(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_GetDetail(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getFootAdminData.getdata(listApiResponse);
                }, throwable -> {
                    getFootAdminData.getThrowable(throwable);
                });
    }

    //足环修改
    public void getXHHYGL_ZHGL_GetEdit(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_Edit(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }
    //足环删除
    public void getXHHYGL_ZHGL_GetDel(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_Delete(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }

    //足环录入
    public void getXHHYGL_ZHGL_Add(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_Add(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }

    //获取上级鸽会发行足环
    public void getXHHYGL_ZHGL_GetFoot(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_GetFoot(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getIssueFootData.getdata(listApiResponse);
                }, throwable -> {
                    getIssueFootData.getThrowable(throwable);
                });
    }


    //足环录入
    public void getXHHYGL_ZHGL_ImportFoot(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_ImportFoot(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }

    //特比环图标统计
    public void getXHHYGL_ZHGL_GetTotal(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_GetTotal(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getPieChartFootData.getdata(listApiResponse);
                }, throwable -> {
                    getPieChartFootData.getThrowable(throwable);
                });
    }

    //获取足环单价
    public void getXHHYGL_ZHGL_GetFootPrice(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_GetFootPrice(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getFootPriceResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getFootPriceResultsData.getThrowable(throwable);
                });
    }

    //设置足环单价
    public void getXHHYGL_ZHGL_SetFootPrice(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_SetFootPrice(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }


    //特比环代售点  添加
    public void getAgentTakePlace_Ser_Add(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_DSDAdd(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }


    //特比环代售点价 修改
    public void getAgentTakePlace_Ser_Modify(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_DSDEdit(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }


    //删除特比环代售点
    public void getAgentTakePlace_Ser_Del(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_DSDDelete(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }


    //特比环代售点 列表
    public void getAgentTakePlace_Ser_list(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_DSDList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getAgentTakePlaceListData.getdata(listApiResponse);
                }, throwable -> {
                    getAgentTakePlaceListData.getThrowable(throwable);
                });
    }

    //特比环代售点 详情
    public void getAgentTakePlace_Ser_details(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_DSDDetail(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getAgentTakePlaceData.getdata(listApiResponse);
                }, throwable -> {
                    getAgentTakePlaceData.getThrowable(throwable);
                });
    }


    //特比环 验鸽
    public void getXHHYGL_ZHGL_TBHYGData(String token, RequestBody body, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_TBHYG(token,//通行验证
                        body,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }



    //下级协会 获取足环列表
    public void getXHHYGL_SJGH_GetFootListData(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_SJGH_GetFootListData(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getXHHYGL_SJGH_GetFootListData.getdata(listApiResponse);
                }, throwable -> {
                    getXHHYGL_SJGH_GetFootListData.getThrowable(throwable);
                });
    }


    //下级协会  获取协会列表
    public void getMemberListData(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getMemberListData(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getMemberListData.getdata(listApiResponse);
                }, throwable -> {
                    getMemberListData.getThrowable(throwable);
                });
    }

    //下级协会  足环管理 足环录入
    public void getXHHYGL_SJGH_AddFoot(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_SJGH_AddFoot(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getResultsData.getdata(listApiResponse);
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }

    //下级协会  足环管理 足环修改
    public void getXHHYGL_SJGH_EditFoot(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_SJGH_EditFoot(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }



    //下级协会  获取足环单价
    public void getXHHYGL_ZHGL_GetFootPrice_Child(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_GetFootPrice_Child(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getFootPriceResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getFootPriceResultsData.getThrowable(throwable);
                });
    }

    //下级协会  设置足环单价
    public void getXHHYGL_ZHGL_SetFootPrice_Child(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_ZHGL_SetFootPrice_Child(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH222222: " + listApiResponse.toJsonString());
                    getResultsData.getdata(listApiResponse);
                }, throwable -> {
                    getResultsData.getThrowable(throwable);
                });
    }
}
