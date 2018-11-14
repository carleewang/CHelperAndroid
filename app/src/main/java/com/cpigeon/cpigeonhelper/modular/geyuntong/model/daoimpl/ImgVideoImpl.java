package com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.dao.IImgVideoDao;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 上传图片，视频 接口实现层
 * Created by Administrator on 2017/10/18.
 */

public class ImgVideoImpl implements IImgVideoDao {


    public GetServerData<Object> getServerData;//服务器传过来的数据
    public GetServerData<List<TagEntitiy>> getServerTag;//获取服务器标签数据

    /**
     * 开始提交上传图片，视频
     */
    public void uploadImgVideo(String userToken, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .uploadGYTRaceImageOrVideo(userToken,
                        requestBody,
                        timestamp,
                        sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    getServerData.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    getServerData.getThrowable(throwable);
                });
    }

    /**
     * 获取标签数据
     */
    public void getServerTag(String type) {
        RetrofitHelper.getApi()
                .getTAG(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    getServerTag.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    getServerTag.getThrowable(throwable);
                });
    }

}
