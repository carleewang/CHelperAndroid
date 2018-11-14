package com.cpigeon.cpigeonhelper.modular.saigetong.presenter;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.EmpowerPhotoUserEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.EmpowerPhotoModel;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class EmpowerPre extends BasePresenter {

    public String phone;
    public String toUserPhone;

    public EmpowerPre(Activity activity) {
        super(activity);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void searchPhoto(Consumer<List<AddAuthEntity>> consumer) {

        if (!StringValid.phoneNumberValid(phone)) {
            error("请输入完整的手机号码");
        }

        submitRequestThrowError(EmpowerPhotoModel.searchUserByPhone(phone).map(r -> {
            if (r.status) {
                if (r.data != null) {
                    return r.data;
                } else {
                    return Lists.newArrayList();
                }
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void empowerPhoto(Consumer<String> consumer) {
        submitRequestThrowError(EmpowerPhotoModel.addEmpower(toUserPhone).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void cleanData() {
        phone = "";
        toUserPhone = "";
    }
}
