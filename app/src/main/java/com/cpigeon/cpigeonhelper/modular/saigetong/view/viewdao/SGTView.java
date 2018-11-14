package com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
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
 * Created by Administrator on 2017/12/1.
 */

public interface SGTView extends IView {

    void getSGTHomeData(ApiResponse<List<SGTHomeListEntity>> listApiResponse, String msg, Throwable mThrowable);//赛格通主页列表数据

    void getSGTSearchData(ApiResponse<List<FootSSEntity>> listApiResponse, String msg, Throwable mThrowable);//赛格通主页列表数据

    void getSearchFootInfoData(ApiResponse<SearchFootEntity> listApiResponse, String msg, Throwable mThrowable);//搜索足环号码

//    void getSGTSearchData(List<FootSSEntity> data, String msg);//赛格通足环搜索页

    void getFootImg(List<SGTImgEntity> imgDatas, String str);//获取足环照片

    void getFootInfo(SGTFootInfoEntity infoData, String str);//获取足环详情

    void getGZImgEntity(List<GZImgEntity> gzImgData, String str);//获取鸽主图片

    void uploadResults(ApiResponse<Object> dateApiResponse, String msg);//上传图片结果

    void getTagData(List<TagEntitiy> tagDatas);//获取标签数据

    void getGeZhuFootData(ApiResponse<GeZhuFootEntity> listApiResponse, String msg);//获取鸽主下的足环列表

    void getUserInfo_SGT(ApiResponse<SGTUserInfo> dataApiResponse, String msg);//获取赛鸽通用户信息

    void getSetRpTimeResults(ApiResponse<Object> dataApiResponse, String msg);//设置本届比赛入棚时间(修改足环号码)

    void getSetGPKrysResults(ApiResponse<Object> dataApiResponse, String msg);//设置公棚可容羽数
}
