package com.cpigeon.cpigeonhelper.modular.menu.view.viewdao;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.message.CHAPIHttpUtil;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.GebiEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.SignRuleEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.UserSignInfoEntity;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/5/26.
 */

public class ISignView {

    //获取签到信息
    public static Observable<ApiResponse<UserSignInfoEntity>> getUserSignInfo() {
        return CHAPIHttpUtil.<ApiResponse<UserSignInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UserSignInfoEntity>>() {
                }.getType())
                .url(R.string.api_get_user_sign_info)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }


    //领取礼包
    public static Observable<ApiResponse<GebiEntity>> getGiftBagInfo(String g) {
        return CHAPIHttpUtil.<ApiResponse<GebiEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<GebiEntity>>() {
                }.getType())
                .url(R.string.api_get_gift_bag)
                .addBody("g",g)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }


    //签到
    public static Observable<ApiResponse<GebiEntity>> getUserSignInInfo() {
        return CHAPIHttpUtil.<ApiResponse<GebiEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<GebiEntity>>() {
                }.getType())
                .url(R.string.api_get_user_sign_in)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }

    //签到规则
    public static Observable<ApiResponse<List<SignRuleEntity>>> getSignGuiZeInfo() {
        return CHAPIHttpUtil.<ApiResponse<List<SignRuleEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<SignRuleEntity>>>() {
                }.getType())
                .url(R.string.api_get_sign_gui_ze)
                .setType(HttpUtil.TYPE_POST)
                .request();
    }

}
