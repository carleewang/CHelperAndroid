package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
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

/**
 * Created by Administrator on 2017/12/22.
 */

public interface GpSmsView extends IView {


    //入棚短信
    void getGpdxSetData_RP(ApiResponse<GpRpdxSetEntity> dataApiResponse, String msg);//获取公棚入棚短信设置数据

    void subGpdxSetData_RP(ApiResponse<Object> dataApiResponse, String msg);//提交入棚短信设置信息


    //训赛短信
    void getSmsListData_XS(ApiResponse<List<XsListEntity>> listApiResponse, String msg, Throwable throwable);

    void getSmsDetail_XS(ApiResponse<XsSmsDetailEntity> detailApiResponse, String msg);//获取训赛项目详细

    void getSmsSet_XS(ApiResponse<XsSmsSetEntity> dataApiResponse, String msg);//获取训赛短信设置信息

    void subSmsSet_XS(ApiResponse<Object> dataApiResponse, String msg);//提交训赛短信设置信息


    //上笼短信
    void getSmsListData_SL(ApiResponse<List<SlListEntity>> listApiResponse, String msg, Throwable throwable);//获取上笼信息列表

    void getSmsSetData_SL(ApiResponse<SlSmsSetEntity> dataApiResponse, String msg);//获取上笼短信设置信息

    void getSmsSubSetData_SL(ApiResponse<Object> dataApiResponse, String msg);//提交上笼短信设置信息

    void getSmsSubCeShiData_SL(ApiResponse<Object> dataApiResponse, String msg);//提交发送短信通道测试短信


    //插组指定  列表
    void getDesignatedList_gp(ApiResponse<List<DesignatedListEntity>> listApiResponse, String msg, Throwable throwable);

    //插组指定 详情  舍弃
    void getDesignatedDetails_gp(ApiResponse<DesignatedSetEntity> listApiResponse, String msg, Throwable throwable);

    //插组指定 设置  舍弃
    void getSetDesignated_gp(ApiResponse<Object> dataApiResponse, String msg,Throwable throwable);


    //插组指定 详情
    void getGetChaZuList(ApiResponse<List<GetChaZuListEntity>> listApiResponse, String msg, Throwable throwable);

    //获取公棚插组每组设置详细
    void getGP_GetChaZu(ApiResponse<GP_GetChaZuEntity> listApiResponse, String msg, Throwable throwable);

    //设置 公棚插组每组设置
    void setGP_SetChaZu(ApiResponse<Object> listApiResponse, String msg, Throwable throwable);

    void setGP_GetChaZu(ApiResponse<GetJiangJinXianShiBiLiEntity> listApiResponse, String msg, Throwable throwable);
}
