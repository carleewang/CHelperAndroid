package com.cpigeon.cpigeonhelper.message.ui.contacts;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.ContactsEntity;
import com.cpigeon.cpigeonhelper.entity.ContactsGroupEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/24.
 */

public class ContactsModel {

    public static Observable<ApiResponse<List<ContactsGroupEntity>>> getContactsGroups(int userId) {
        return GXYHttpUtil.<ApiResponse<List<ContactsGroupEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<ContactsGroupEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_contact_group)
                .request();
    }

    public static Observable<ApiResponse> ContactsGroupsAdd(int userId, String groupName) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_contact_group_add)
                .addBody("fzmc", groupName)
                .request();
    }

    public static Observable<ApiResponse> ContactsAdd(int userId, String groupId,
                                                      String phoneNumber, String name, String remarks) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_contact_add)
                .addBody("fzid", groupId)
                .addBody("sjhm", phoneNumber)
                .addBody("xingming", name)
                .addBody("beizhu", remarks)
                .request();
    }

    public static Observable<ApiResponse<List<ContactsEntity>>> ContactsInGroup(int userId, int groupId, int page, String search) {
        return GXYHttpUtil.<ApiResponse<List<ContactsEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<ContactsEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_contacts_in_group)
                .addBody("fzid", String.valueOf(groupId))
                .addBody("p", String.valueOf(page))
                .addBody("s", search)
                .request();
    }

    public static Observable<ApiResponse<ContactsEntity>> NumberOfContactsInGroup(int userId, String groupIds) {
        return GXYHttpUtil.<ApiResponse<ContactsEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<ContactsEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_contacts_in_group)
                .addBody("fzid", groupIds)
                .addBody("type","s")
                .request();
    }

    public static Observable<ApiResponse> ModifyContactGroupName(int userId, int groupIds, String groupName) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_modify_contacts_group_name)
                .addBody("fzid", String.valueOf(groupIds))
                .addBody("fzmc",groupName)
                .request();
    }

    public static Observable<ApiResponse> deleteContactGroup(int userId, int groupIds) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_delete_contacts_group)
                .addBody("fzid", String.valueOf(groupIds))
                .request();
    }

    public static Observable<ApiResponse> deleteContactInGroup(int userId, String contactIds) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_delete_contacts_in_group)
                .addBody("ids", contactIds)
                .request();
    }


}
