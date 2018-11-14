package com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.viewmodel;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.entity.ShootInfoEntity;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class ShootViewModel {

    //我的鸽币
//    public MutableLiveData<ShootInfoEntity> mShootInfo = new MutableLiveData<>();
    public ShootInfoEntity mShootInfoEntity = new ShootInfoEntity.Builder().build();
    private boolean isOneTag = true;//是否第一次请求

    protected Map<String, Object> postParams = new HashMap<>();//存放参数
    protected long timestamp;//时间搓

    //  设置用户头像和鸽舍名称
    public void getTXGP_SetTouXiangGeSheMingChengData(Consumer<ApiResponse<Object>> onNext) {

        if (StringUtil.isStringValid(mShootInfoEntity.getImgurl())) {
            if (mShootInfoEntity.getImgurl().contains("http:")) {
                mShootInfoEntity.setImgurl("");
            }
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("sszz", mShootInfoEntity.getSszz());//赛事组织


        MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户id
                .addFormDataPart("sszz", mShootInfoEntity.getSszz());//赛事组织


        if (StringUtil.isStringValid(mShootInfoEntity.getImgurl())) {
            MultipartBody.Part body = MultipartBody.Part.createFormData("imgfile",
                    mShootInfoEntity.getImgurl(),
                    RequestBody.create(MediaType.parse("image/jpeg"), new File(mShootInfoEntity.getImgurl())));
            mBuilder.addPart(body);
        }

        RequestBody requestBody = mBuilder.build();

        RetrofitHelper.getApi()
                .getTXGP_SetTouXiangGeSheMingCheng(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> {
                    Log.d("sdfasdf", "getTXGP_SetTouXiangGeSheMingChengData: " + throwable.getLocalizedMessage());
                });

    }


    // 获取用户头像和鸽舍名称
    public void getTXGP_GetTouXiangGeSheMingChengData(Consumer<ApiResponse<ShootInfoEntity>> onNext) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        RetrofitHelper.getApi()
                .getTXGP_GetTouXiangGeSheMingCheng(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> {
                    Log.d("sdfasdf", "getTXGP_SetTouXiangGeSheMingChengData: " + throwable.getLocalizedMessage());
                });
    }

}
