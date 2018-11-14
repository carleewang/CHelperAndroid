package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.impldao;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootBuyEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HYGLInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserIdInfo;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PenalizeRecordEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearPayCostEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearSelectionEntitiy;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/3/24.
 */

public class MenberImpl implements IBaseDao {

    public GetServerData<HyglHomeListEntity> getServiceHyList_XHData;
    public GetServerData<HYGLInfoEntity> getServiceHYGLInfoEntityData;
    public GetServerData<HyUserDetailEntity> getServiceHyDetail;
    public GetServerData<HyUserIdInfo> getHyUserIdInfoData;
    public GetServerData<Object> getServiceHyData;
    public GetServerData<List<HistoryLeagueEntity>> getXHHYGL_SJData;
    public GetServerData<List<YearSelectionEntitiy>> getYearSelectionData;//年度评选
    public GetServerData<List<PenalizeRecordEntity>> getPenalizeRecordData;//年度评选
    public GetServerData<List<FootBuyEntity>> getFootBuyData;//足环购买
    public GetServerData<List<YearPayCostEntity>> getYearPayCostData;//年度缴费


    //信鸽协会会员管理系统信息
    public void XHHYGL_UserInfoService(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_UserInfo(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH: " + listApiResponse.toJsonString());
                    getServiceHYGLInfoEntityData.getdata(listApiResponse);
                }, throwable -> {
                    getServiceHYGLInfoEntityData.getThrowable(throwable);
                });
    }


    //获取协会会员列表
    public void getServiceHyList_XH(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_GetUserList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH: " + listApiResponse.toJsonString());
                    getServiceHyList_XHData.getdata(listApiResponse);
                }, throwable -> {
                    getServiceHyList_XHData.getThrowable(throwable);
                });
    }

    //获取协会会员列表
    public void getServiceHyList_XH2(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_GetUserList2(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH: " + listApiResponse.toJsonString());
                    getServiceHyList_XHData.getdata(listApiResponse);
                }, throwable -> {
                    getServiceHyList_XHData.getThrowable(throwable);
                });
    }

    //获取协会会员列表
    public void getUserDetailService(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_GetUserDetail(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("xiehuihuiyuan", "getUserDetailService: " + listApiResponse.toJsonString());
                    getServiceHyDetail.getdata(listApiResponse);
                }, throwable -> {
                    getServiceHyDetail.getThrowable(throwable);
                });
    }


    //获取协会会员身份证信息
    public void getUserIdInfoService(String token, Map<String, Object> params, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getXHHYGL_GetUserIdInfo(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getHyUserIdInfoData.getdata(listApiResponse);
                }, throwable -> {
                    getHyUserIdInfoData.getThrowable(throwable);
                });
    }

    //添加协会会员
    public void getAddHyService(String token, RequestBody body, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_JBXX_Add(token,//通行验证
                        body,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    Log.d("MenberImpls", "getServiceLY_XH: 2");
                    Log.d("MenberImpls", "getServiceLY_XH: " + listApiResponse.toJsonString());
                    getServiceHyData.getdata(listApiResponse);
                }, throwable -> {
                    Log.d("MenberImpls", "getServiceLY_XH: 6" + throwable.getLocalizedMessage());
                    getServiceHyData.getThrowable(throwable);
                });
    }


    //添加历次赛绩
    public void getXHHYGL_SJ_Add(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_SJ_Add(token,//通行验证
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


    //删除历次赛绩
    public void getXHHYGL_SJ_Del(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_SJ_Del(token,//通行验证
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


    //编辑历次赛绩
    public void getXHHYGL_SJ_Edit(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_SJ_Edit(token,//通行验证
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


    //历次赛绩列表
    public void getXHHYGL_SJ_GetListService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_SJ_GetList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getXHHYGL_SJData.getdata(listApiResponse);
                }, throwable -> {
                    getXHHYGL_SJData.getThrowable(throwable);
                });
    }


    //导入历次赛绩
    public void getXHHYGL_SJ_Import(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_SJ_Import(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
//                    Log.d(TAG, "getXHHYGL_SJ_Import: " + listApiResponse.string().toString());
                    Log.d(TAG, "getXHHYGL_SJ_Import: " + listApiResponse.toJsonString());
                    getXHHYGL_SJData.getdata(listApiResponse);
                }, throwable -> {
                    Log.d(TAG, "getXHHYGL_SJ_Import: 异常");
                    getXHHYGL_SJData.getThrowable(throwable);
                });
    }


    //获取年度评选列表
    public void getXHHYGL_NDPX_GetListService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_NDPX_GetList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getYearSelectionData.getdata(listApiResponse);
                }, throwable -> {
                    getYearSelectionData.getThrowable(throwable);
                });
    }

    //添加年度评选
    public void getXHHYGL_NDPX_GetAddService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_NDPX_GetAdd(token,//通行验证
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

    //编辑年度评选
    public void getXHHYGL_NDPX_GetEditService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_NDPX_GetEdit(token,//通行验证
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


    //获取处罚记录列表
    public void getXHHYGL_CFJL_GetListService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_CFJL_GetList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getPenalizeRecordData.getdata(listApiResponse);
                }, throwable -> {
                    getPenalizeRecordData.getThrowable(throwable);
                });
    }


    //添加处罚记录
    public void getXHHYGL_CFJL_GetAddService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_CFJL_GetAdd(token,//通行验证
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

    //编辑处罚记录
    public void getXHHYGL_CFJL_GetEditService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_CFJL_GetEdit(token,//通行验证
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

    //获取足环购买记录列表
    public void getXHHYGL_ZHGM_GetListService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_ZHGM_GetList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getFootBuyData.getdata(listApiResponse);
                }, throwable -> {
                    getFootBuyData.getThrowable(throwable);
                });
    }


    //添加足环购买记录
    public void getXHHYGL_ZHGM_GetAddService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_ZHGM_GetAdd(token,//通行验证
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


    //编辑足环购买记录
    public void getXHHYGL_ZHGM_GetEditService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_ZHGM_GetEdit(token,//通行验证
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


    //删除足环购买记录
    public void getXHHYGL_ZHGM_GetDelService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_ZHGM_GetDel(token,//通行验证
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

    //获取年度缴费列表
    public void getYearPayCostService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getYearPayCost_GetList(token,//通行验证
                        params,//参数
                        timestamp,// 时间戳
                        sign)// 签名
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                    getYearPayCostData.getdata(listApiResponse);
                }, throwable -> {
                    getYearPayCostData.getThrowable(throwable);
                });
    }

    //添加年度缴费
    public void getYearPayCostAddService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_NDJF_Add(token,//通行验证
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

    //编辑年度缴费
    public void getYearPayCostEditService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_NDJF_Edit(token,//通行验证
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


    //上传协会会员身份证信息
    public void uploadIdCardHyInfoService(String token, RequestBody body, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_SFZXX(token,//通行验证
                        body,//参数
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


    //修改鸽舍资料
    public void getXHHYGL_GSZL_EditService(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_GSZL_Edit(token,//通行验证
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

    //修改基本信息
    public void getXHHYGL_JBXX_Edit(String token, Map<String, Object> params, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getXHHYGL_JBXX_Edit(token,//通行验证
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

    //上传头像
    public void updateUserFaceImageService(String token, RequestBody body, long timestamp, String sign) {
        Log.d("MenberImpls", "getServiceLY_XH: 1");
        RetrofitHelper.getApi()
                .getUpdateUserFaceImage(token,//通行验证
                        body,//参数
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

}
