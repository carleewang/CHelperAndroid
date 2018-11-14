package com.cpigeon.cpigeonhelper.message.ui.order.ui.presenter;

import android.app.Activity;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.MessageOrderEntity;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.OrderModel;
import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/12/14.
 */

public class RechargeHistoryPre extends BasePresenter {

    int userId;
    public String startTime;
    public String endTime;

    public RechargeHistoryPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getHistory(Consumer<List<MessageOrderEntity>> consumer){
        submitRequestThrowError(OrderModel.getMessageOrderHistory(userId, startTime, endTime).map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else {
                    return Lists.newArrayList();
                }
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public boolean timeValid(){
        long sTime = DateTool.strToDate(startTime).getTime();
        long eTime =  DateTool.strToDate(endTime).getTime();

        return sTime <= eTime;
    }

}
