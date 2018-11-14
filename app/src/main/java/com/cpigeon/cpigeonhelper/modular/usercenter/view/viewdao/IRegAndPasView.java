package com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao;


import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface IRegAndPasView extends IView {

    void validationSucceed();//验证成功回调

    void validationSucceed(ApiResponse<CheckCode> dataApiResponse);//获取验证码验证成功回调
}
