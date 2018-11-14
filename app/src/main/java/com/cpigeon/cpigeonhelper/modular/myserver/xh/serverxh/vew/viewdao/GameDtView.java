package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;

import java.util.List;

/**
 * 协会动态view
 * Created by Administrator on 2017/12/14.
 */
public interface GameDtView extends IView {

    void getDtList(List<DtListEntity> datas, String msg);//获取协会动态列表

    void addResults(ApiResponse<Object> listApiResponse, String msg);//添加协会动态结果（修改协会动态结果）

    void delResults_dt(ApiResponse<Object> listApiResponse, String msg);//删除协会动态结果 删除赛事规程结果

    void getDtItmeInfo(ApiResponse<DtItemEntity> listApiResponse, String msg);//获取单条协会动态详情回调
}
