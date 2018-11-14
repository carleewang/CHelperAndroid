package com.cpigeon.cpigeonhelper.modular.menu.model.dao;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface ISettingDao extends IBaseDao{

    void  loginOut(String userToken);//退出登录

    void  alterUserPas();//修改用户密码

    void  alterPlayPas();//修改支付密码

}
