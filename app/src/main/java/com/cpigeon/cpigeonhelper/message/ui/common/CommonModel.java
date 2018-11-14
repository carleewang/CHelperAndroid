package com.cpigeon.cpigeonhelper.message.ui.common;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.CommonEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class CommonModel {

    static Observable<ApiResponse<List<CommonEntity>>> getCommons(int userId){
        return GXYHttpUtil.<ApiResponse<List<CommonEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<CommonEntity>>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_common_massage_list)
                .request();
    }

    public static Observable<ApiResponse> addCommonMessage(int userId, String content){

        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .url(R.string.api_common_massage_add)
                .setType(HttpUtil.TYPE_POST)
                .addBody("dxnr", content)
                .request();

    }

    static Observable<ApiResponse> modifyCommonMessage(int userId, String messageId , String content){

        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .url(R.string.api_common_massage_modify)
                .setType(HttpUtil.TYPE_POST)
                .addBody("id", messageId)
                .addBody("dxnr", content)
                .request();

    }

    static Observable<ApiResponse> deleteCommonMessage(int userId, String messageIds){

        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .url(R.string.api_common_massage_delete)
                .setType(HttpUtil.TYPE_POST)
                .addBody("ids", messageIds)
                .request();

    }


}
