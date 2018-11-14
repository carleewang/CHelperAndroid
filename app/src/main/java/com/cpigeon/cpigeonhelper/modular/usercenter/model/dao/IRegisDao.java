package com.cpigeon.cpigeonhelper.modular.usercenter.model.dao;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;

import java.util.Map;

/**
 * 注册和忘记密码共有的mode层
 * Created by Administrator on 2017/9/7.
 */

public interface IRegisDao extends IBaseDao {

    /**
     * 发送完验证回调接口
     */
    interface SendVerifi{
        void getThrowable(Throwable throwable);//抛出异常

        void  getVerifiInformation(int errorCode);//返回验证后信息
    }

    void  sendVerification(Map<String, Object> postParams, long timestamp);//发送验证
}
