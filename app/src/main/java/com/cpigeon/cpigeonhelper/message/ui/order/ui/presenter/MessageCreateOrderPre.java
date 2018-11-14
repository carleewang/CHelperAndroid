package com.cpigeon.cpigeonhelper.message.ui.order.ui.presenter;

import android.app.Activity;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.GXTMessagePrice;
import com.cpigeon.cpigeonhelper.entity.OrderInfoEntity;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.OrderModel;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class MessageCreateOrderPre extends BasePresenter {

    public final static int SID_MESSAGE = 23;
    int userId;
    public int messageCount;
    public double price;

    public MessageCreateOrderPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getGXTMessagePrice(Consumer<ApiResponse<GXTMessagePrice>> consumer) {
        submitRequestThrowError(OrderModel.getMessagePrice(), consumer);
    }

    public void createGXTMessageOrder(Consumer<ApiResponse<OrderInfoEntity>> consumer) {
        submitRequestThrowError(OrderModel.createGXTMessageOrder(userId, messageCount, price), consumer);
    }

    public Consumer<String> setMessageCount(Consumer<Integer> consumer) {
        return s -> {
            if (StringValid.isStringValid(s)) {
                messageCount = Integer.valueOf(s);
            } else {
                messageCount = 0;
            }
            Observable.<Integer>create(observableEmitter -> {
                observableEmitter.onNext(messageCount);
            }).subscribe(consumer);
        };
    }
}
