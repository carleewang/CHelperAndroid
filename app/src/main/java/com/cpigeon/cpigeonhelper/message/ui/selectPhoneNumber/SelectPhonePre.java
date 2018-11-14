package com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber;

import android.app.Activity;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class SelectPhonePre extends BasePresenter {

    int userId;
    int groupId;
    public String phoneString;

    public SelectPhonePre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
        groupId = activity.getIntent().getIntExtra(IntentBuilder.KEY_DATA,0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void putTelephone(Consumer<ApiResponse> consumer){
        submitRequestThrowError(SelectPhoneModel.getCommons(userId,groupId,phoneString).map(r -> {
            if(r.status){
                return r;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

}
