package com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao;

import android.widget.ImageView;

import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;

/**
 * Created by Administrator on 2017/9/6.
 */

public interface ILoginView  extends IView{

   void loginSucceed(UserBean bean);//登录成功回调

   void getErrorNews(String str);//获取错误消息

   void getThrowable(Throwable throwable);//抛出异常

   void setUserImg(String urlUserImg );//设置用户头像

   void isNewEquipmentLogin(int type);//在新的设备上登录回调 type :1 没有登录，2已在别的设备登录


}
