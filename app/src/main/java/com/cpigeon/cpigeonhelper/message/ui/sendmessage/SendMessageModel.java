package com.cpigeon.cpigeonhelper.message.ui.sendmessage;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/28.
 */

public class SendMessageModel {
    public static Observable<ApiResponse> sendMessage(int userId, String groupIds, String content, String number) {
        return GXYHttpUtil.<ApiResponse>build()
                .setToJsonType(new TypeToken<ApiResponse>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_send_message)
                .addBody("fzids", groupIds)
                .addBody("dxnr", content)
                .addBody("sjhm", number)
                .request();
    }
}
