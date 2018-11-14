package com.cpigeon.cpigeonhelper.modular.lineweather.view.viewdeo;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.message.CHAPIHttpUtil;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.UllageToolEntity;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.GetGongPengListEntity;
import com.cpigeon.cpigeonhelper.modular.lineweather.model.bean.GetSiFangDiEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AssociationListEntity;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;




/**
 * Created by Administrator on 2018/5/9.
 */

public class ILineWeatherView implements IBaseDao {


    //获取公棚坐标信息
    public static Observable<ApiResponse<List<GetGongPengListEntity>>> getTool_GetGongPengInfoData(String str) {
        return CHAPIHttpUtil.<ApiResponse<List<GetGongPengListEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<GetGongPengListEntity>>>() {
                }.getType())
                .url(R.string.api_tool_get_gong_peng_info)
                .setType(HttpUtil.TYPE_POST)
                .addBody("s", str)
                .request();
    }

    //获取协会信息
    public static Observable<ApiResponse<List<AssociationListEntity>>> getAssociationInfoData(String str,String p,String c) {
        return CHAPIHttpUtil.<ApiResponse<List<AssociationListEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<AssociationListEntity>>>() {
                }.getType())
                .url(R.string.api_XH_GetUsersList)
                .setType(HttpUtil.TYPE_POST)
                .addBody("skey", str)
                .addBody("c", c)
                .addBody("p", p)
                .request();
    }


    //获取公棚坐标信息
    public static Observable<ApiResponse<List<GetSiFangDiEntity>>> getTool_GetSiFangDiData(String str) {
        return CHAPIHttpUtil.<ApiResponse<List<GetSiFangDiEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<GetSiFangDiEntity>>>() {
                }.getType())
                .url(R.string.api_tool_get_sfd_info)
                .setType(HttpUtil.TYPE_POST)
                .addBody("s", str)
                .request();
    }

    //获取公棚坐标信息
    public static Observable<ApiResponse<UllageToolEntity>> getKongju(Map<String, String> body) {
        return CHAPIHttpUtil.<ApiResponse<UllageToolEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<UllageToolEntity>>() {
                }.getType())
                .url(R.string.api_get_kongju)
                .setType(HttpUtil.TYPE_POST)
                .addList(body)
                .request();
    }

}
