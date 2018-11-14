package com.cpigeon.cpigeonhelper.message.ui.contacts.presenter;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.message.ui.contacts.ContactsModel;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class ContactsInfoPre extends BasePresenter {

    int userId;

    public int groupId = 0;

    String name;
    String phoneNumber;
    String remarks;



    public ContactsInfoPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();

    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void addContacts(Consumer<ApiResponse> consumer){

        if(!StringValid.isStringValid(name)){
//            ToastUtil.showLongToast(getActivity(),"姓名不能为空");

            CommonUitls.showSweetDialog1(getActivity(), "姓名不能为空", dialog -> {
                dialog.dismiss();
            });

            return;
        }

        if(!StringValid.phoneNumberValid(phoneNumber)){
            CommonUitls.showSweetDialog1(getActivity(), "手机号码无效", dialog -> {
                dialog.dismiss();
            });

//            ToastUtil.showLongToast(getActivity(),"手机号码无效");
            return;
        }

        if(groupId == 0){
            CommonUitls.showSweetDialog1(getActivity(), "请选择分组", dialog -> {
                dialog.dismiss();
            });
//            ToastUtil.showLongToast(getActivity(),"请选择分组");
            return;
        }

        submitRequestThrowError(ContactsModel.ContactsAdd(userId, String.valueOf(groupId),phoneNumber,name,remarks).map(r -> {
            return r;
        }),consumer);
    }


    public Consumer<String> setName(){
        return s -> {
          name = s;
        };
    }

    public Consumer<String> setPhoneNumer(){
        return s -> {
            phoneNumber = s;
        };
    }

    public Consumer<String> setRemarks(){
        return s -> {
            remarks = s;
        };
    }

}
