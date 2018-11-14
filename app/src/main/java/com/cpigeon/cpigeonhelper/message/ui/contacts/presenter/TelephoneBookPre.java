package com.cpigeon.cpigeonhelper.message.ui.contacts.presenter;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.message.ui.contacts.ContactsModel;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class TelephoneBookPre extends BasePresenter {

    int userId;

    public String groupName;

    public TelephoneBookPre(IView mView) {
        super(mView);
        userId = AssociationData.getUserId();
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getContactsGroups(Consumer<List<ContactsGroupEntity>> consumer){
        submitRequestThrowError(ContactsModel.getContactsGroups(userId).map(r -> {
            if(r.isOk()){
                if(r.isHaveDate()){
                    return r.data;
                }else {
                    return Lists.newArrayList();
                }
            }else {
                throw new HttpErrorException(r);
            }
        }),consumer);
    }

    public void addContactsGroups(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.ContactsGroupsAdd(userId, groupName).map(r ->{
                return  r;
        }),consumer);
    }

    public Consumer<String> setGroupName(){
        return s -> {
            groupName = s;
        };
    }

}
