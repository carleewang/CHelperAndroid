package com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GeZhuFootEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTFootInfoEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTUserInfo;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SearchFootEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/1.
 */
public class SGTImpl implements IBaseDao {

    private String TAG = getClass().getSimpleName();

    public GetServerData<List<SGTHomeListEntity>> getSGTHomeListEntity;//获取赛格通主页数据列表
    public GetServerData<List<FootSSEntity>> getFootSSEntity;//获取赛鸽通的数据
    public GetServerData<List<SGTImgEntity>> getSGTImgEntity;//获取足环照片
    public GetServerData<List<GZImgEntity>> getGZImgEntity;//获取鸽主的鸽子照片
    public GetServerData<GeZhuFootEntity> getGeZhuFootData;//获取鸽主下的足环列表
    public GetServerData<SGTFootInfoEntity> getSGTFootInfoEntity;//获取足环详情
    public GetServerData<Object> getUploadFootImg;//上传足环照片(设置比赛入棚时间，设置可容羽数 ， 申请试用回调)
    public GetServerData<List<TagEntitiy>> getTagEntitiy;//获取赛格通标签
    public GetServerData<SGTUserInfo> getInfo_SGT;//获取赛鸽通用户信息
    public GetServerData<SearchFootEntity> getSearchFootEntity;//搜索足环号码

    /**
     * 获取赛鸽通的数据
     */
    public void getSGTGeZuData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getSGTGeZhu(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getSGTHomeListEntity.getdata(getSGTGeZuResponse);
//                    submitServerData.getdata(orgInfoApiResponse);//获取数据
                }, throwable -> {
//                    submitServerData.getThrowable(throwable);//抛出异常
                    getSGTHomeListEntity.getThrowable(throwable);
                });
    }


    /**
     * 获取赛鸽通的数据
     */
    public void getSGTFootSSData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getSGTFootSS(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getFootSSEntity.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getFootSSEntity.getThrowable(throwable);
                });
    }


    /**
     * 获取足环照片
     */
    public void getZHImgData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getSGTImageFoot(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getSGTImgEntity.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getSGTImgEntity.getThrowable(throwable);
                });
    }

    /**
     * 获取足环详情
     */
    public void getSGTFootInfoData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getSGTFootInfo(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getSGTFootInfoEntity.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getSGTFootInfoEntity.getThrowable(throwable);
                });
    }

    /**
     * 获取鸽主的鸽子照片
     */
    public void getSGTImagesGeZhuData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getSGTImagesGeZhu(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getGZImgEntity.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getGZImgEntity.getThrowable(throwable);
                });
    }


    /**
     * 开始提交上传足环图片
     */
    public void uploadFootImg(String userToken, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .uploadSGTFootImage(userToken,
                        requestBody,
                        timestamp,
                        sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    getUploadFootImg.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    getUploadFootImg.getThrowable(throwable);
                });
    }

    /**
     * 开始提交上传足环图片(覆盖)
     */
    public void uploadFootImg_fg(String userToken, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .uploadSGTFootImage_fg(userToken,
                        requestBody,
                        timestamp,
                        sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    getUploadFootImg.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    getUploadFootImg.getThrowable(throwable);
                });
    }


    /**
     * 开始提交上传鸽主足环图片
     */
    public void uploadGZFootImg(String userToken, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .uploadSGTGeZhuImage(userToken,
                        requestBody,
                        timestamp,
                        sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    getUploadFootImg.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    getUploadFootImg.getThrowable(throwable);
                });
    }


    /**
     * 获取赛格通标签
     */
    public void getSTGTag(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getSGTTAG(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    getTagEntitiy.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    getTagEntitiy.getThrowable(throwable);
                });
    }


    /**
     * 获取鸽主下的足环列表
     */
    public void getFootListData_SGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getFootList_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getGeZhuFootData.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getGeZhuFootData.getThrowable(throwable);
                });
    }


    /**
     * 获取赛鸽通用户信息
     */
    public void getUserInfo_SGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUserInfo_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    Log.d("SGTPresenter", "getUserInfo_SGT: 数据返回" + getSGTGeZuResponse.getErrorCode() + "   msg-->" + getSGTGeZuResponse.getMsg());
                    getInfo_SGT.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getInfo_SGT.getThrowable(throwable);
                });
    }

    /**
     * 设置公棚可容羽数
     */
    public void settingKrys_SGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .settingKrys_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getUploadFootImg.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getUploadFootImg.getThrowable(throwable);
                });
    }

    /**
     * 设置公棚可容羽数
     */
    public void getRpTime_SGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getRpTime_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getUploadFootImg.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getUploadFootImg.getThrowable(throwable);
                });
    }


    /**
     * 修改足环号码
     */
    public void editFoot_SGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .editFootInfo_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getUploadFootImg.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getUploadFootImg.getThrowable(throwable);
                });
    }


    /**
     * 申请试用  公棚赛鸽
     */
    public void applyTrial_SGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .openService_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getUploadFootImg.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getUploadFootImg.getThrowable(throwable);
                });
    }

    /**
     * 搜索足环号码
     * 说明：根据选择的标签，搜索足环号码。
     */
    public void getSearchFootInfoService(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .searchFootInfo_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    getSearchFootEntity.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getSearchFootEntity.getThrowable(throwable);
                });
    }


//    重新补拍足环搜索
    public void getSearchBuPaiFootInfoService(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .searchBuPaiFootInfo_sgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSGTGeZuResponse -> {
                    Log.d("cwbpsj", "getSearchBuPaiFootInfoService: "+getSGTGeZuResponse.toJsonString());
                    getSearchFootEntity.getdata(getSGTGeZuResponse);
//                    getSearchFootEntity.getdata(getSGTGeZuResponse);
                }, throwable -> {
                    getSearchFootEntity.getThrowable(throwable);
//                    getSearchFootEntity.getThrowable(throwable);
                });
    }
}
