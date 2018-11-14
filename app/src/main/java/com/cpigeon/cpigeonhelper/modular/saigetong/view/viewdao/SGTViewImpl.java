package com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
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

/**
 * Created by Administrator on 2018/2/26.
 */

public class SGTViewImpl implements SGTView {
    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }

    @Override
    public void getSGTHomeData(ApiResponse<List<SGTHomeListEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getSGTSearchData(ApiResponse<List<FootSSEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getSearchFootInfoData(ApiResponse<SearchFootEntity> listApiResponse, String msg, Throwable mThrowable) {

    }


    @Override
    public void getFootImg(List<SGTImgEntity> imgDatas, String str) {

    }

    @Override
    public void getFootInfo(SGTFootInfoEntity infoData, String str) {

    }

    @Override
    public void getGZImgEntity(List<GZImgEntity> gzImgData, String str) {

    }

    @Override
    public void uploadResults(ApiResponse<Object> dateApiResponse, String msg) {

    }

    @Override
    public void getTagData(List<TagEntitiy> tagDatas) {

    }

    @Override
    public void getGeZhuFootData(ApiResponse<GeZhuFootEntity> listApiResponse, String msg) {

    }

    @Override
    public void getUserInfo_SGT(ApiResponse<SGTUserInfo> dataApiResponse, String msg) {

    }

    @Override
    public void getSetRpTimeResults(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void getSetGPKrysResults(ApiResponse<Object> dataApiResponse, String msg) {

    }
}
