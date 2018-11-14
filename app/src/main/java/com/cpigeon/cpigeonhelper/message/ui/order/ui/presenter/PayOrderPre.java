package com.cpigeon.cpigeonhelper.message.ui.order.ui.presenter;

import android.app.Activity;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.OrderInfoEntity;
import com.cpigeon.cpigeonhelper.entity.UserBalanceEntity;
import com.cpigeon.cpigeonhelper.entity.WeiXinPayEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.OrderModel;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.utils.ToastUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/12/8.
 */

public class PayOrderPre extends BasePresenter {

    String password;
    int userId;
    public OrderInfoEntity orderInfoEntity;

    public PayOrderPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
        orderInfoEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void payOrderByBalance(Consumer<ApiResponse> consumer) {
        if (!StringValid.isStringValid(password) || password.length() < 6) {
            ToastUtil.showShortToast(getActivity(), "请输入密码（6-12位）");
            return;
        }
        submitRequestThrowError(OrderModel.payOrderByBalance(userId, orderInfoEntity.id, password), consumer);
    }

    public void getUserBalance(Consumer<UserBalanceEntity> consumer) {
        submitRequestThrowError(OrderModel.getUserBalance(userId).map(r -> {
            if (r.isHaveDate()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void getWXOrder(Consumer<WeiXinPayEntity> consumer) {
        submitRequestThrowError(OrderModel.greatWXOrder(userId, orderInfoEntity.id).map(r -> {
            if (r.isHaveDate()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public Consumer<String> setPassword() {
        return s -> {
            password = s;
        };
    }

}
