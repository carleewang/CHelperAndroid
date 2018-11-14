package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
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
 * Created by Administrator on 2018/2/25.
 */

public class GpSmsViewImpl implements GpSmsView {
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
    public void getGpdxSetData_RP(ApiResponse<GpRpdxSetEntity> dataApiResponse, String msg) {

    }

    @Override
    public void subGpdxSetData_RP(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void getSmsListData_XS(ApiResponse<List<XsListEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getSmsDetail_XS(ApiResponse<XsSmsDetailEntity> detailApiResponse, String msg) {

    }

    @Override
    public void getSmsSet_XS(ApiResponse<XsSmsSetEntity> dataApiResponse, String msg) {

    }

    @Override
    public void subSmsSet_XS(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void getSmsListData_SL(ApiResponse<List<SlListEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getSmsSetData_SL(ApiResponse<SlSmsSetEntity> dataApiResponse, String msg) {

    }

    @Override
    public void getSmsSubSetData_SL(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void getSmsSubCeShiData_SL(ApiResponse<Object> dataApiResponse, String msg) {

    }

    @Override
    public void getDesignatedList_gp(ApiResponse<List<DesignatedListEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getDesignatedDetails_gp(ApiResponse<DesignatedSetEntity> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getSetDesignated_gp(ApiResponse<Object> dataApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getGetChaZuList(ApiResponse<List<GetChaZuListEntity>> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void getGP_GetChaZu(ApiResponse<GP_GetChaZuEntity> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void setGP_SetChaZu(ApiResponse<Object> listApiResponse, String msg, Throwable throwable) {

    }

    @Override
    public void setGP_GetChaZu(ApiResponse<GetJiangJinXianShiBiLiEntity> listApiResponse, String msg, Throwable throwable) {

    }
}
