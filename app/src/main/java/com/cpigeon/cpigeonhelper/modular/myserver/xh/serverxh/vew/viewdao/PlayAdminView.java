package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.BsdxSettingEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public interface PlayAdminView extends IView {

    void getPlayAdminData(ApiResponse<List<PlayListEntity>> listApiResponse, String msg, Throwable mThrowable);//获取比赛列表

    void getBsdxData(ApiResponse<BsdxSettingEntity> dxsetApiResponse, String msg);//获取比赛短信设置数据

    void subBsdxData(ApiResponse<Object> dxsetApiResponse, String msg);//获取比赛短信设置数据(删除比赛申请结果)
}
