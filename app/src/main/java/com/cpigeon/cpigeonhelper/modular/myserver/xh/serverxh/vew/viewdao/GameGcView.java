package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;

import java.util.List;

/**
 * 赛事规程view
 * Created by Administrator on 2017/12/12.
 */

public interface GameGcView extends IView {

    void getGCList(List<GcListEntity> datas, String msg);//获取赛事规程列表

    void addResults(ApiResponse<Object> listApiResponse, String msg);//添加赛事规程结果（修改赛事规程结果）

    void delResults_gc(ApiResponse<Object> listApiResponse, String msg);//删除赛事规程结果

    void getItmeInfo(ApiResponse<GcItemEntity> listApiResponse, String msg);//获取单条赛事规程详情回调


}
