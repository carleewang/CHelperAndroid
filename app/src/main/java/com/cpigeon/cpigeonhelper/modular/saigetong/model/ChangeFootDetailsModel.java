package com.cpigeon.cpigeonhelper.modular.saigetong.model;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.entity.ChangeFootPhotoDetailsEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class ChangeFootDetailsModel {
    public static Observable<ApiResponse<ChangeFootPhotoDetailsEntity>> getFootInfo(String footId, String tagId){
        return GXYHttpUtil.<ApiResponse<ChangeFootPhotoDetailsEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<ChangeFootPhotoDetailsEntity>>(){}.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_get_change_photo_info)
                .addBody("id", footId)
                .addBody("tag", tagId)
                .request();
    }
}
