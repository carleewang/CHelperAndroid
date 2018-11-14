package com.cpigeon.cpigeonhelper.message.ui.userAgreement;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;

import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class UserAgreementPre extends BasePresenter {

    int userId;
    boolean agreementState;

    public UserAgreementPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
        agreementState = activity.getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void setUserAgreement(Consumer<ApiResponse> consumer){
        submitRequestThrowError(UserAgreementModel.setUserAgreement(userId).map(r -> {
            return r;
        }),consumer);
    }

}
