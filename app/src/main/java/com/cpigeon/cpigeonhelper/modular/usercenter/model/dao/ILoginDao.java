package com.cpigeon.cpigeonhelper.modular.usercenter.model.dao;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface ILoginDao extends IBaseDao {
    /**
     * 点击登录
     */
    interface GetLoginDownData {
        void getdata(ApiResponse<UserBean> userBeanApiResponse);//获取数据

        void getThrowable(Throwable throwable);//抛出异常
    }


    /**
     * 单点登录检查接口
     */

    interface GetSingleLoginCheck{
        void getdata(String deviceid);//获取数据（服务器放回设备id）

        void getThrowable(Throwable throwable);//抛出异常
    }

    /**
     * 获取头像
     */
    interface GetUserImgUrl{
        void getUrlData(String userImgUrl);//获取用户头像url
    }


    void loginStart(Map<String, Object> postParams, long timestamp);//开始登录


    void getUserImg(String strUserName);//输入用户名显示头像

    void  isLogin( Map<String, Object> params,  String sign,long  timestam);//是否已经登录
}
