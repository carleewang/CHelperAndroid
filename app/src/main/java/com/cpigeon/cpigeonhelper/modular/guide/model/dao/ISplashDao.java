package com.cpigeon.cpigeonhelper.modular.guide.model.dao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.DeviceBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/9.
 */

public interface ISplashDao  extends IBaseDao {

    interface GetLoginData{
        void getdata(ApiResponse<DeviceBean> userBeanApiResponse);//获取数据

        void getThrowable(Throwable throwable);//抛出异常
    }


    /**
     * 看用户是否已经登录过
     * @param userToken  用户登录token
     * @param params 用户资料
     * @param timestamp  时间戳
     * @param apiSign  签名
     */
    void  isLoginData(String userToken, Map<String, Object> params, long timestamp, String apiSign );


}
