package com.cpigeon.cpigeonhelper.modular.menu.presenter;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.GebiEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.SignRuleEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.UserSignInfoEntity;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.ISignView;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/26.
 */

public class SignPresenter extends BasePresenter {

    public SignPresenter(IView mView) {
        super(mView);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    //获取签到信息
    public void getUserSignInfoData(Consumer<UserSignInfoEntity> consumer) {
        submitRequestThrowError(ISignView.getUserSignInfo().map(r -> {
            if (r.isOk()) {
                if (r.status) {
                    return r.data;
                } else {
                    return new UserSignInfoEntity();
                }
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }


    //领取礼包
    public void getGiftBagData(String g, Consumer<ApiResponse<GebiEntity>> consumer) {
        submitRequestThrowError(ISignView.getGiftBagInfo(g).map(r -> {
            return r;
        }), consumer);
    }

    //签到
    public void getUserSignInData(Consumer<GebiEntity> consumer) {
        submitRequestThrowError(ISignView.getUserSignInInfo().map(r -> {

            if (r.status) {
                return r.getData();
            } else {
                throw new HttpErrorException(r);
            }

        }), consumer);
    }

    //签到规则
    public void getSignGuiZeData(Consumer<List<SignRuleEntity>> consumer) {
        submitRequestThrowError(ISignView.getSignGuiZeInfo().map(r -> {

            if (r.status) {
                return r.data;
            } else {
                throw new HttpErrorException(r);
            }
        }), consumer);
    }
}
