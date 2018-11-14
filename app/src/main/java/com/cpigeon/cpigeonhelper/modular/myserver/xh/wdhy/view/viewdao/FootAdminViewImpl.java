package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildMemberEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.IssueFoot;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PieChartFootEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */

public class FootAdminViewImpl implements IView {
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


    //获取足环管理列表
    public void getFootAdminList(ApiResponse<FootAdminListDataEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    //获取足环管理详情
    public void getFootAdminDetailsData(ApiResponse<FootAdminEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    //获取足环管理 请求结果回调
    public void getFootAdminResultsData(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }

    //获取足环管理 请求结果回调
    public void getFootAdminResultsData2(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }

    // 导入上级鸽会发行足环
    public void getXHHYGL_ZHGL_ImportFootResult(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }
    //获取发行足环数据
    public void getIssueFootsData(ApiResponse<IssueFoot> listApiResponse, String msg, Throwable mThrowable) {

    }


    // 特比环图标统计
    public void getXHHYGL_ZHGL_GetTotal(ApiResponse<PieChartFootEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    // 获取足环单价
    public void getXHHYGL_ZHGL_GetFootPrice(ApiResponse<FootPriceEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    // 设置足环单价
    public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }

    //代售点 列表
    public void getAgentTakePlaceList(ApiResponse<List<AgentTakePlaceListEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    //代售点 详情
    public void getAgentTakePlaceDetails(ApiResponse<AgentTakePlaceListEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    //代售点 修改
    public void getAgentTakePlaceModify(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }

    //下级协会 足环管理 列表
    public void getXHHYGL_SJGH_GetFootList(ApiResponse<ChildFoodAdminListEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    //下级协会 会员信息 列表
    public void getChildMemberList(ApiResponse<List<ChildMemberEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }
}
