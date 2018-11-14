package com.cpigeon.cpigeonhelper.modular.order.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.Order;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayWxRequest;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/25.
 */

public class OrderViewImpl implements OrderView {

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
    public void setMealGytOrderList(ApiResponse<List<PackageInfo>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getOrderData(Order order) {

    }

    @Override
    public void playSucceed() {

    }

    @Override
    public void playFail(String msg) {

    }

    @Override
    public void playGbSucceed() {

    }

    @Override
    public void playGbFail(String msg) {

    }

    @Override
    public void playWXPreSucceed(PayWxRequest payWxRequest) {

    }

    @Override
    public void playWXPreFail() {

    }

    @Override
    public void getPlayListDatas(ApiResponse<List<OrderList>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getRechargeMxDatas(ApiResponse<List<RechargeMxEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }


    @Override
    public void getGetServesInfoData(ApiResponse<ServesInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getInvoiceList(ApiResponse<List<InvoiceEntity>> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getInvoiceData(ApiResponse<InvoiceEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    @Override
    public void getDatas(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

    }
}
