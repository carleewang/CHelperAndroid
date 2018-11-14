package com.cpigeon.cpigeonhelper.message.ui.order.ui;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.GXTMessagePrice;
import com.cpigeon.cpigeonhelper.entity.MessageOrderEntity;
import com.cpigeon.cpigeonhelper.entity.OrderInfoEntity;
import com.cpigeon.cpigeonhelper.entity.UserBalanceEntity;
import com.cpigeon.cpigeonhelper.entity.WeiXinPayEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.EncryptionTool;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class OrderModel {
    public static Observable<ApiResponse<OrderInfoEntity>> greatServiceOrder(int userId) {
        return GXYHttpUtil.<ApiResponse<OrderInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<OrderInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_open_GXT_order)
                .addBody("ly", "中鸽助手APP")
                .addBody("uc", "android")
                .request();
    }

    // hl 创建短信充值订单
    public static Observable<ApiResponse<OrderInfoEntity>> createGXTMessageOrder(int userId, int count, double money) {
        return GXYHttpUtil.<ApiResponse<OrderInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<OrderInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_create_GXT_message_order)
                .addBody("ly", "中鸽助手APP")
                .addBody("uc", "android")
                .addBody("czts", String.valueOf(count))
                .addBody("money", String.valueOf(money))
                .request();
    }

    public static Observable<ApiResponse> payOrderByBalance(int userId, String orderId, String password) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .setHeadUrl(R.string.api_public_head_url)
                .url(R.string.api_pay_order_by_balance)
                .addBody("oid", orderId)
                .addBody("p", EncryptionTool.encryptAES(password))
                .request();
    }

    public static Observable<ApiResponse<UserBalanceEntity>> getUserBalance(int userId) {
        return GXYHttpUtil.<ApiResponse<UserBalanceEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UserBalanceEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_user_balance_info)
                .request();
    }

    public static Observable<ApiResponse<WeiXinPayEntity>> greatWXOrder(int userId, String orderId) {
        return GXYHttpUtil.<ApiResponse<WeiXinPayEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<WeiXinPayEntity>>() {
                }.getType())
                .setHeadUrl(R.string.api_public_head_url)
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_wx_order)
                .addBody("oid", orderId)
                .addBody("app", "cpigeonhelper")
                .request();
    }

    public static Observable<ApiResponse<GXTMessagePrice>> getMessagePrice() {
        return GXYHttpUtil.<ApiResponse<GXTMessagePrice>>build()
                .setToJsonType(new TypeToken<ApiResponse<GXTMessagePrice>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_GXT_message_price)
                .request();
    }


    public static Observable<ApiResponse<List<MessageOrderEntity>>> getMessageOrderHistory(int userId, String startTime, String endTime) {
        return GXYHttpUtil.<ApiResponse<List<MessageOrderEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MessageOrderEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_GXT_message_order_history)
                .addBody("u", String.valueOf(userId))
                .addBody("t1", startTime)
                .addBody("t2", endTime)
                .request();
    }
}
