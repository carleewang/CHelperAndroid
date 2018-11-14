package com.cpigeon.cpigeonhelper.message.ui.home;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.UserGXTEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class UserGXTModel {
    public static Observable<ApiResponse<UserGXTEntity>> getUserInfo(int userId){
        return GXYHttpUtil.<ApiResponse<UserGXTEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UserGXTEntity>>(){}.getType())
                .url(R.string.api_user_info)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }
}
