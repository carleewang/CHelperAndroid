package com.cpigeon.cpigeonhelper.modular.order.presenter;

import android.util.Log;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.Order;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayWxRequest;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.daoimpl.OrderImpl;
import com.cpigeon.cpigeonhelper.modular.order.view.viewdao.OrderView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.EncryptionTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开通，续费，升级页面控制层
 * Created by Administrator on 2017/10/26.
 */

public class OrderPresenter extends BasePresenter<OrderView, OrderImpl> {

    private Map<String, Object> postParams = new HashMap<>();//存放参数
    private long timestamp;//时间搓

    public OrderPresenter(OrderView mView) {
        super(mView);
    }

    @Override
    protected OrderImpl initDao() {
        return new OrderImpl();
    }

    /**
     * 开通鸽运通
     */
    public void openGeYunTong(String key) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("key", key);//服务名称

        mDao.getOpenGytInfoData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getSetMealGytInfoDatas = new IBaseDao.GetServerData<List<PackageInfo>>() {
            @Override
            public void getdata(ApiResponse<List<PackageInfo>> listApiResponse) {
                mView.setMealGytOrderList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.setMealGytOrderList(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 创建鸽运通订单
     */
    public void createGYTOrder(int sid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("sid", sid);//鸽运通服务 ID
        postParams.put("type", type);//订单类型 标记开通、升级、续费【open，upgrade，renewal】
        postParams.put("ly", "中鸽助手APP");//来源
        postParams.put("uc", "android");//用户端

        mDao.getCreateGYTOrder(AssociationData.getUserToken(),
                postParams,
                timestamp,
                CommonUitls.getApiSign(timestamp, postParams));

        mDao.getCreateGYTOrders = new IBaseDao.GetServerData<Order>() {
            @Override
            public void getdata(ApiResponse<Order> orderApiResponse) {

                switch (orderApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.getOrderData(orderApiResponse.getData());
                        break;
                    default:
                        mView.getErrorNews("获取订单失败：" + orderApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 24.订单支付-余额支付
     *
     * @param oid
     * @param p
     */
    public void orderPayByBalance(String oid, String p) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("oid", oid);//鸽运通服务(订单) ID
        postParams.put("p", EncryptionTool.encryptAES(p));//支付密码

        mDao.getOrderPayByBalance(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getOrderPayByBalanceDatas = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> objApiResponse) {
                switch (objApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.playSucceed();//余额支付成功后回调
                        break;
                    default:
                        mView.playFail(objApiResponse.getMsg());//余额支付失败回调
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }

        };
    }

    /**
     * 22.订单支付-创建微信预支付订单
     *
     * @param oid 订单 ID
     */
    public void playWXPreOrder(String oid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("oid", oid);//鸽运通服务(订单) ID
        postParams.put("app", "cpigeonhelper");//鸽运通服务(订单) ID

        mDao.getWXPrePayOrder(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getPayWxRequest = new IBaseDao.GetServerData<PayWxRequest>() {
            @Override
            public void getdata(ApiResponse<PayWxRequest> payWxRequestApiResponse) {

                switch (payWxRequestApiResponse.getErrorCode()) {
                    case 0://创建微信预支付订单成功
                        mView.playWXPreSucceed(payWxRequestApiResponse.getData());
                        break;

                    default://创建微信预支付订单失败
                        mView.playWXPreFail();
                        mView.getErrorNews("获取微信预支付订单失败：" + payWxRequestApiResponse.getErrorCode() + "   " + payWxRequestApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * @param ps  页大小，即一页多少条数据【默认 20】
     * @param pi  页码，即第几页【默认 1】
     * @param key 查询关键字【可模糊查询订单项目、订单号】
     */
    public void getOrderData(int ps, int pi, String key) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("ps", ps);// 页大小，即一页多少条数据【默认 20】
        postParams.put("pi", pi);//页码，即第几页【默认 1】

        if (!key.isEmpty()) {
            postParams.put("key", key);//查询关键字【可模糊查询订单项目、订单号】
        }


        mDao.getMyOrderList(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getOrderList = new IBaseDao.GetServerData<List<OrderList>>() {
            @Override
            public void getdata(ApiResponse<List<OrderList>> listApiResponse) {
                mView.getPlayListDatas(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getPlayListDatas(null, null, throwable);
            }
        };
    }


    /**
     * 获取续费套餐信息
     */
    public void getRenewalInfo() {
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getRenewalInfoData(AssociationData.getUserToken(), postParams);
        mDao.getSetMealGytInfoDatas = new IBaseDao.GetServerData<List<PackageInfo>>() {
            @Override
            public void getdata(ApiResponse<List<PackageInfo>> listApiResponse) {
                mView.setMealGytOrderList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.setMealGytOrderList(null, null, throwable);
            }
        };

    }

    /**
     * 获取升级套餐信息
     */
    public void getUpgradelInfo() {
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getUpgradeInfoData(AssociationData.getUserToken(), postParams);
        mDao.getSetMealGytInfoDatas = new IBaseDao.GetServerData<List<PackageInfo>>() {
            @Override
            public void getdata(ApiResponse<List<PackageInfo>> listApiResponse) {
                mView.setMealGytOrderList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.setMealGytOrderList(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取训鸽通订单
     */
    public void getXGTOrder(int sid, String type) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("sid", sid);//鸽运通服务 ID
        postParams.put("type", type);//订单类型 标记开通、升级、续费【open，upgrade，renewal】
        postParams.put("ly", "中鸽助手APP");//来源
        postParams.put("uc", "android");//用户端

        mDao.getXGTOrderInfo(AssociationData.getUserToken(),
                postParams,
                timestamp,
                CommonUitls.getApiSign(timestamp, postParams));

        mDao.getCreateGYTOrders = new IBaseDao.GetServerData<Order>() {
            @Override
            public void getdata(ApiResponse<Order> orderApiResponse) {

                switch (orderApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.getOrderData(orderApiResponse.getData());
                        break;
                    default:
                        mView.getErrorNews("获取订单失败：" + orderApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 24.订单支付-训鸽通鸽币支付
     *
     * @param oid
     * @param p   支付密码
     */
    public void orderPayGb_XGT(String oid, String p) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("oid", oid);//鸽运通服务(订单) ID
        postParams.put("p", EncryptionTool.encryptAES(p));//支付密码

        mDao.getOrderPayGb_XGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getOrderPayByBalanceDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> objApiResponse) {

                switch (objApiResponse.getErrorCode()) {
                    case 0:
                        mView.playGbSucceed();//鸽币支付成功
                        break;
                    default:
                        mView.playGbFail(objApiResponse.getMsg());//鸽币支付失败回调
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 24.订单支付-鸽运通鸽币支付
     *
     * @param oid
     * @param p   支付密码
     */
    public void orderPayGb_GYT(String oid, String p) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("oid", oid);//鸽运通服务(订单) ID
        postParams.put("p", EncryptionTool.encryptAES(p));//支付密码

        mDao.getOrderPayGb_GYT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getOrderPayByBalanceDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> objApiResponse) {

                switch (objApiResponse.getErrorCode()) {
                    case 0:
                        mView.playGbSucceed();//鸽币支付成功
                        break;
                    default:
                        mView.playGbFail(objApiResponse.getMsg());//鸽币支付失败回调
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 删除订单
     */
    public void subDelOrderItem(String isdStr) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("ids", isdStr);//要删除的订单ID字符串，超过1个使用英文逗号分隔。示例：1,2,3
        mDao.subDelOrderItem(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getOrderPayByBalanceDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> objApiResponse) {
                switch (objApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.playGbSucceed();//余额支付成功后回调
                        break;
                    default:
                        mView.playGbFail("删除订单失败：" + objApiResponse.getMsg());//删除订单失败
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 充值明细
     */
    public void getRechargeMx() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getRechargeMx(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getRechargeMxData = new IBaseDao.GetServerData<List<RechargeMxEntity>>() {
            @Override
            public void getdata(ApiResponse<List<RechargeMxEntity>> listApiResponse) {
                mView.getRechargeMxDatas(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getRechargeMxDatas(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 删除明细订单
     */
    public void subDelOrderMxItem(String isdStr) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("ids", isdStr);//要删除的订单ID字符串，超过1个使用英文逗号分隔。示例：1,2,3
        mDao.subDelOrderMxItem(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getOrderPayByBalanceDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> objApiResponse) {
                Log.d(TAG, "getdata: " + "删除订单回调：" + objApiResponse.getErrorCode() + "  msg-->" + objApiResponse.getMsg());
                switch (objApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.playGbSucceed();//余额支付成功后回调
                        break;
                    default:
                        mView.playGbFail("删除订单失败：" + objApiResponse.getMsg());//删除订单失败
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 赛鸽通开通  创建订单
     */
    public void createSGTOrder(int sid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        postParams.put("ly", "中鸽助手APP");//来源
        postParams.put("uc", "android");//用户端


        if (sid != -1) {
            postParams.put("sid", sid);//续费
        }

        mDao.subCreateSGTOrder(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getCreateGYTOrders = new IBaseDao.GetServerData<Order>() {
            @Override
            public void getdata(ApiResponse<Order> orderApiResponse) {
                switch (orderApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.getOrderData(orderApiResponse.getData());
                        break;
                    default:
                        mView.getErrorNews("获取订单失败：" + orderApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 创建订单
     */
    public void getCreateServiceOrder(int sid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("ly", "中鸽助手APP");//来源
        postParams.put("uc", "android");//用户端

        if (sid != -1) {
            postParams.put("sid", sid);//续费
        }

        mDao.subCreateServiceOrder(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getCreateGYTOrders = new IBaseDao.GetServerData<Order>() {
            @Override
            public void getdata(ApiResponse<Order> orderApiResponse) {
                switch (orderApiResponse.getErrorCode()) {
                    case 0://获取订单列表成功
                        mView.getOrderData(orderApiResponse.getData());
                        break;
                    default:
                        mView.getErrorNews("获取订单失败：" + orderApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //获取服务信息  到期时间
    public void getServesInfo() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        mDao.getServesInfoService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getServesInfoEntity = new IBaseDao.GetServerData<ServesInfoEntity>() {
            @Override
            public void getdata(ApiResponse<ServesInfoEntity> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());
                mView.getGetServesInfoData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getGetServesInfoData(null, null, throwable);
            }
        };
    }

    //首次设置发票信息
    public void getUser_Invoice_Set(String id, EditText dw, EditText sh, String lx,
                                    EditText lxr, EditText dh, EditText dz,
                                    EditText yx,String p,String c,String a) {

        String shs = sh.getText().toString();

        if (shs.isEmpty()) {
            mView.getErrorNews("输入的税号不能为空");
            return;
        }

        if (shs.length() == 15 || sh.length() == 18 || sh.length() == 20) {
            timestamp = System.currentTimeMillis() / 1000;
            postParams.clear();//清除集合中之前的数据
            postParams.put("uid", AssociationData.getUserId());//用户id
            postParams.put("id", id);//用户id
            postParams.put("dw", dw.getText().toString());//单位名称
            postParams.put("sh", sh.getText().toString());//税号
            postParams.put("lx", lx);//类型：纸质发票|电子发票
            postParams.put("lxr", lxr.getText().toString());//收件人
            postParams.put("dh", dh.getText().toString());//收件人电话
            postParams.put("dz", dz.getText().toString());//收件人地址

            postParams.put("yx", yx.getText().toString());//收件人邮箱

            postParams.put("p",p);//收件人所在省
            postParams.put("c", c);//收件人所在市
            postParams.put("a", a);//收件人所在区县

            mDao.getUser_Invoice_Set(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

            mDao.getOrderPayByBalanceDatas = new IBaseDao.GetServerData<Object>() {
                @Override
                public void getdata(ApiResponse<Object> dataApiResponse) {
                    mView.getDatas(dataApiResponse, dataApiResponse.getMsg(), null);
                }

                @Override
                public void getThrowable(Throwable throwable) {
                    mView.getThrowable(throwable);
                }
            };
        }else {
            mView.getErrorNews("当前税号不正确,请填写正确的税号");
        }
    }


    //发票信息  列表
    public void getUser_Invoice_GetList() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getUser_Invoice_GetList(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getInvoiceListDatas = new IBaseDao.GetServerData<List<InvoiceEntity>>() {
            @Override
            public void getdata(ApiResponse<List<InvoiceEntity>> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());

                mView.getInvoiceList(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getInvoiceList(null, null, throwable);
            }
        };
    }

    //发票信息  详情
    public void getUser_Invoice_Get_detail(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//发票设置信息ID

        mDao.getUser_Invoice_detail(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getInvoiceDatas = new IBaseDao.GetServerData<InvoiceEntity>() {
            @Override
            public void getdata(ApiResponse<InvoiceEntity> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());

                mView.getInvoiceData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getInvoiceData(null, null, throwable);
            }
        };
    }

    //发票信息  详情
    public void getUser_Invoice_Get_del(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//发票设置信息ID

        mDao.getUser_Invoice_del(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());

                mView.getDatas(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDatas(null, null, throwable);
            }
        };
    }


    //发票信息绑定订单
    public void setUser_Invoice_Bind(int oid, String id, boolean bind) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("oid", oid);//订单索引ID
        postParams.put("id", id);//发票信息索引ID

        if (bind) {
            postParams.put("bind", "y");//bind=y绑定，否则不绑定
        } else {
            postParams.put("bind", "");//bind=y绑定，否则不绑定
        }

        mDao.getUser_Invoice_Bind(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());

                mView.getDatas(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDatas(null, null, throwable);
            }
        };

    }
}
