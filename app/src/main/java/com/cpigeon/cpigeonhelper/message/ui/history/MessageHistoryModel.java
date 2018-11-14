package com.cpigeon.cpigeonhelper.message.ui.history;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.MessageEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class MessageHistoryModel {

    public static Observable<ApiResponse<List<MessageEntity>>> MessageHistory(int userId, int date) {
        return GXYHttpUtil.<ApiResponse<List<MessageEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<MessageEntity>>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_message_history_list)
                .addBody("y", String.valueOf(date))
                .request();
    }

}
