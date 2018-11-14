package com.cpigeon.cpigeonhelper.modular.order.model.daoimpl;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.Order;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayWxRequest;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.dao.IOrderDao;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/26.
 */

public class OrderImpl implements IOrderDao {

    public GetServerData<List<PackageInfo>> getSetMealGytInfoDatas;
    public GetServerData<List<InvoiceEntity>> getInvoiceListDatas;
    public GetServerData<InvoiceEntity> getInvoiceDatas;
    public GetServerData<Object> getDatas;
    public GetServerData<Order> getCreateGYTOrders;
    public GetServerData<ServesInfoEntity> getServesInfoEntity;
    public GetServerData<Object> getOrderPayByBalanceDatas;
    public GetServerData<PayWxRequest> getPayWxRequest;

    public GetServerData<List<OrderList>> getOrderList;//订单列表
    public GetServerData<List<RechargeMxEntity>> getRechargeMxData;//充值明细列表

    /**
     * 请求开通鸽运通页面数据列表
     */
    public void getOpenGytInfoData(String userToken, Map<String, Object> postParams, long timestamp, String sign) {

        RetrofitHelper.getApi()
                .getServicePackageInfo(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            Log.d(TAG, "获取开通鸽运通数据: " + listApiResponse.toString());
                            getSetMealGytInfoDatas.getdata(listApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: " + throwable.getLocalizedMessage());
                            getSetMealGytInfoDatas.getThrowable(throwable);//抛出异常
                        });
    }


    /**
     * 创建鸽运通订单
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getCreateGYTOrder(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .createGYTOrder(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getCreateGYTOrders.getdata(listApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getCreateGYTOrders.getThrowable(throwable);
                        });
    }

    /**
     * 24.订单支付-余额支付
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getOrderPayByBalance(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .orderPayByBalance(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getOrderPayByBalanceDatas.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getOrderPayByBalanceDatas.getThrowable(throwable);
                        });
    }


    /**
     * 22.订单支付-创建微信预支付订单
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getWXPrePayOrder(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getWXPrePayOrder(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getPayWxRequest.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getPayWxRequest.getThrowable(throwable);
                        });
    }

    /**
     * 17.获取我的订单列表
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getMyOrderList(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getMyOrderList(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
//                            Log.d(TAG, "getMyOrderList: " + listApiResponse.string().toString());
                            getOrderList.getdata(listApiResponse);
                        }
                        , throwable -> {
//                            Log.d(TAG, "开通鸽运通列表数据异常: " + throwable.getLocalizedMessage());
                            getOrderList.getThrowable(throwable);
                        });
    }


    /**
     * 获取续费套餐信息
     *
     * @param token
     * @param urlParams
     */
    public void getRenewalInfoData(String token, Map<String, Object> urlParams) {
        RetrofitHelper.getApi().
                getGytRenewalServicePackageInfo(token, urlParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getSetMealGytInfoDatas.getdata(listApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "续费套餐数据异常: " + throwable.getLocalizedMessage());
                            getSetMealGytInfoDatas.getThrowable(throwable);
                        });
    }


    /**
     * 获取升级套餐信息
     *
     * @param token
     * @param urlParams
     */
    public void getUpgradeInfoData(String token, Map<String, Object> urlParams) {
        RetrofitHelper.getApi().
                getGytUpgradeServicePackageInfo(token, urlParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getSetMealGytInfoDatas.getdata(listApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getSetMealGytInfoDatas.getThrowable(throwable);
                        });
    }


    /**
     * 获取开通训鸽通订单
     */
    public void getXGTOrderInfo(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getCreateXGTOrder(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listApiResponse -> {
                            getCreateGYTOrders.getdata(listApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getCreateGYTOrders.getThrowable(throwable);
                        });
    }


    /**
     * 24.订单支付-训鸽通鸽币支付
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getOrderPayGb_XGT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getOrderPayByScore_xgt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getOrderPayByBalanceDatas.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getOrderPayByBalanceDatas.getThrowable(throwable);
                        });
    }


    /**
     * 24.订单支付-鸽运通鸽币支付
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getOrderPayGb_GYT(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getOrderPayByScore_gyt(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getOrderPayByBalanceDatas.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getOrderPayByBalanceDatas.getThrowable(throwable);
                        });
    }

    /**
     * 删除订单
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void subDelOrderItem(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .subDeleteOrder(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getOrderPayByBalanceDatas.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getOrderPayByBalanceDatas.getThrowable(throwable);
                        });
    }


    /**
     * 充值明细
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void getRechargeMx(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getMyRechargeList(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
//                            Log.d(TAG, "getRechargeMx: " + objApiResponse.string().toString());
                            getRechargeMxData.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getRechargeMxData.getThrowable(throwable);
                        });
    }


    /**
     * 删除订单
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void subDelOrderMxItem(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getDeleteChongZhi(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getOrderPayByBalanceDatas.getdata(objApiResponse);
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getOrderPayByBalanceDatas.getThrowable(throwable);
                        });
    }


    /**
     * 提交创建赛鸽通订单
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void subCreateSGTOrder(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getCreateSGTOrder(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getCreateGYTOrders.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getCreateGYTOrders.getThrowable(throwable);
                        });
    }


    /**
     * 提交创建赛鸽通订单
     *
     * @param userToken  通行验证
     * @param postParams 参数
     * @param timestamp  时间搓
     * @param sign       签名
     */
    public void subCreateServiceOrder(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getCreateServiceOrder(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getCreateGYTOrders.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            Log.d(TAG, "开通鸽运通列表数据异常: ");
                            getCreateGYTOrders.getThrowable(throwable);
                        });
    }

    //获取服务信息   到期时间
    public void getServesInfoService(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getServesInfo(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
//                            Log.d("fwdqsj", "getServesInfoService: "+objApiResponse.string().toString());
                            getServesInfoEntity.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            getServesInfoEntity.getThrowable(throwable);
                        });
    }

    //订单发票添加
    public void getUser_Invoice_Set(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUser_Invoice_Set(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
//                            Log.d("fwdqsj", "getServesInfoService: "+objApiResponse.string().toString());
                            getOrderPayByBalanceDatas.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            getOrderPayByBalanceDatas.getThrowable(throwable);
                        });
    }

    //订单发票列表
    public void getUser_Invoice_GetList(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUser_Invoice_GetList(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getInvoiceListDatas.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            getInvoiceListDatas.getThrowable(throwable);
                        });
    }

    //订单发票详情
    public void getUser_Invoice_detail(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUser_Invoice_detail(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getInvoiceDatas.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            getInvoiceDatas.getThrowable(throwable);
                        });
    }

    //订单发票删除
    public void getUser_Invoice_del(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUser_Invoice_del(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getDatas.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            getDatas.getThrowable(throwable);
                        });
    }

    //发票信息绑定订单
    public void getUser_Invoice_Bind(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUser_Invoice_Bind(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objApiResponse -> {
                            getDatas.getdata(objApiResponse);//获取开通赛鸽通订单
                        }
                        , throwable -> {
                            getDatas.getThrowable(throwable);
                        });
    }
}
