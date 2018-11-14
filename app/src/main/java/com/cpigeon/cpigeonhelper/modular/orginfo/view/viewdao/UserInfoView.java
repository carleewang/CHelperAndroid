package com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public interface UserInfoView extends IView {

    void getMyInfoData(ApiResponse<MyInfoEntity> myInfoApiResponse, String msg);//获取我的信息

    void getGbmxData(ApiResponse<List<GbListEntity>> myInfoApiResponse, String msg, Throwable mThrowable);//获取鸽币明细列表

    void getShareCodeData(ApiResponse<ShareCodeEntity> myInfoApiResponse, String msg);//获取分享分享码
}
