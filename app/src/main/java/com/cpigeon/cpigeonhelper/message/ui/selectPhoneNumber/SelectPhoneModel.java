package com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber;



import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class SelectPhoneModel {
    static Observable<ApiResponse> getCommons(int userId, int GroupId, String phones){
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_input_number_of_phone)
                .addBody("fzid", String.valueOf(GroupId))
                .addBody("c", phones)
                .request();
    }
}
