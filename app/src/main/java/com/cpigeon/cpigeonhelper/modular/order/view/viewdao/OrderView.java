package com.cpigeon.cpigeonhelper.modular.order.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.Order;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayWxRequest;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */
public interface OrderView extends IView {

    void setMealGytOrderList(ApiResponse<List<PackageInfo>> listApiResponse, String msg, Throwable mThrowable);//鸽运通套餐列表数据

    void getOrderData(Order order);

    void playSucceed();//余额订单支付成功

    void playFail(String msg);//余额订单支付失败

    void playGbSucceed();//余额订单支付成功（删除订单成功）

    void playGbFail(String msg);//余额订单支付失败 （删除订单失败）

    void playWXPreSucceed(PayWxRequest payWxRequest);//微信预支付订单创建成功

    void playWXPreFail();//微信预支付订单创建成功

    void getPlayListDatas(ApiResponse<List<OrderList>> listApiResponse, String msg, Throwable mThrowable);//我的订单列表

    void getRechargeMxDatas(ApiResponse<List<RechargeMxEntity>> listApiResponse, String msg, Throwable mThrowable);//充值明细数据


    void getGetServesInfoData(ApiResponse<ServesInfoEntity> listApiResponse, String msg, Throwable mThrowable);//我的订单列表


    void getInvoiceList(ApiResponse<List<InvoiceEntity>> listApiResponse, String msg, Throwable mThrowable);//发票列表

    void getInvoiceData(ApiResponse<InvoiceEntity> listApiResponse, String msg, Throwable mThrowable);//发票详情


    void getDatas(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable);//获取数据结果
}
