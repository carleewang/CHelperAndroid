package com.cpigeon.cpigeonhelper.modular.saigetong.model;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.EmpowerPhotoUserEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class EmpowerPhotoModel {
    public static Observable<ApiResponse<List<AddAuthEntity>>> searchUserByPhone(String phone){
        return GXYHttpUtil.<ApiResponse<List<AddAuthEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<AddAuthEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_search_user_by_phone)
                .addQueryString("p", phone)
                .request();
    }

    public static Observable<ApiResponse> addEmpower(String toUserId){
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_add_phone_empower)
                .addQueryString("sjhm", toUserId)
                .request();
    }

    public static Observable<ApiResponse<List<EmpowerPhotoUserEntity>>> getEmpowerList(){
        return GXYHttpUtil.<ApiResponse<List<EmpowerPhotoUserEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<EmpowerPhotoUserEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_phone_empower_list)
                .request();
    }

    public static Observable<ApiResponse> cancelEmpower(String userId){
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_cancel_empower_photo)
                .addBody("auid", userId)
                .request();
    }

    public static Observable<ApiResponse> cancelAllEmpower(){
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_cancel_all_empower_photo)
                .request();
    }


}
