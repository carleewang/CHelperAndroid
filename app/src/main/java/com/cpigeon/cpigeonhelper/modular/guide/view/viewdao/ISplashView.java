package com.cpigeon.cpigeonhelper.modular.guide.view.viewdao;

import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;

/**
 * Created by Administrator on 2017/9/9.
 */

public interface ISplashView extends IView{

    void  isLastLogin(int  type);//上次是否登录过账号，1  登录过，2  未登录过账号

}
