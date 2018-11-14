package com.cpigeon.cpigeonhelper.message.ui.contacts.presenter;

import android.app.Activity;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.ContactsEntity;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.adapter.ContactsInfoAdapter;
import com.cpigeon.cpigeonhelper.message.ui.contacts.ContactsModel;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class ContactsListPre extends BasePresenter {
    int userId;
    int groupId;
    public int page = 1;
    String searchString;
    public ContactsGroupEntity contactsGroupEntity;
    public String groupName;
    String contactsIds;

    public ContactsListPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
        contactsGroupEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        groupId = contactsGroupEntity.fzid;
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getContactsInGroup(Consumer<List<ContactsEntity>> consumer){

        submitRequestThrowError(ContactsModel.ContactsInGroup(userId,groupId,page,searchString).map(r -> {
            if(r.isOk()){
                if(r.isHaveDate()){
                    return r.data;
                }else return Lists.newArrayList();
            }else throw new HttpErrorException(r);
        }),consumer);

    }

    public void modifyGroupName(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.ModifyContactGroupName(userId, groupId, groupName).map(apiResponse -> {
            return apiResponse;
        }),consumer);
    }

    public void deleteGroup(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.deleteContactGroup(userId, groupId),consumer);
    }

    public void deleteContacts(Consumer<ApiResponse> consumer){
        submitRequestThrowError(ContactsModel.deleteContactInGroup(userId, contactsIds),consumer);
    }



    public Consumer<String> setSearchString(){
        return s -> {
            searchString = s;
        };
    }

    public Consumer<String> setGroupName(){
        return s -> {
            groupName = s;
        };
    }

    public void setSelectIds(ContactsInfoAdapter adapter){
        List<String> id = Lists.newArrayList();
        List<Integer> positions  = adapter.getSelectedPotion();
        for (Integer position : positions) {
            id.add(String.valueOf(adapter.getItem(position).id));
        }
        contactsIds = Lists.appendStringByList(id);
    }

}
