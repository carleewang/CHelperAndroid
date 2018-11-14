package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootBuyEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HYGLInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserIdInfo;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PenalizeRecordEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearPayCostEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearSelectionEntitiy;

import java.util.List;

/**
 * Created by Administrator on 2018/3/24.
 */

public class MenberViewImpl implements IView {


    //2、获取处罚记录列表
    public void getPenalizeRecord_ListData(ApiResponse<List<PenalizeRecordEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    //2、获取足环购买列表
    public void getFootBuy_ListData(ApiResponse<List<FootBuyEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }


    //2、获取年度缴费
    public void getYearPayCost_ListData(ApiResponse<List<YearPayCostEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    //2、获取年度评选
    public void getXHHYGL_NDPX_GetListData(ApiResponse<List<YearSelectionEntitiy>> listApiResponse, String msg, Throwable mThrowable) {

    }

    //2、获取历次赛绩
    public void getXHHYGL_SJ_GetList(ApiResponse<List<HistoryLeagueEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    //2、导入历次赛绩
    public void getXHHYGL_SJ_GetImport(ApiResponse<List<HistoryLeagueEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }


    //2、添加会员
    public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }

    //2、获取信鸽协会会员详细
    public void getHyUserDetail(ApiResponse<HyUserDetailEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    //获取协会会员身份证信息
    public void getHyUserIdInfo(ApiResponse<HyUserIdInfo> listApiResponse, String msg, Throwable mThrowable) {

    }


    //获取会员列表
    public void getHyListData(ApiResponse<HyglHomeListEntity> listApiResponse, String msg, Throwable mThrowable) {
    }


    //信鸽协会会员管理系统信息
    public void getHyglInfoData(ApiResponse<HYGLInfoEntity> listApiResponse, String msg, Throwable mThrowable) {
    }

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
}
