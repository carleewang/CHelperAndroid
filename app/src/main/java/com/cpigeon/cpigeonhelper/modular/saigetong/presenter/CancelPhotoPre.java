package com.cpigeon.cpigeonhelper.modular.saigetong.presenter;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.EmpowerPhotoUserEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.EmpowerPhotoModel;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class CancelPhotoPre extends BasePresenter {

    public String cancelUserId;

    public CancelPhotoPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getEmpowerUserList(Consumer<List<EmpowerPhotoUserEntity>> consumer){
        submitRequestThrowError(EmpowerPhotoModel.getEmpowerList().map(r -> {
            if(r.isOk()){
                if(r.status){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void cancelEmpower(Consumer<Boolean> consumer){
        submitRequestThrowError(EmpowerPhotoModel.cancelEmpower(cancelUserId).map(r -> {
            if(r.status){
                return true;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    public void cancelAllEmpower(Consumer<Boolean> consumer){
        submitRequestThrowError(EmpowerPhotoModel.cancelAllEmpower().map(r -> {
            if(r.status){
                return true;
            }else throw new HttpErrorException(r);
        }),consumer);
    }
}
