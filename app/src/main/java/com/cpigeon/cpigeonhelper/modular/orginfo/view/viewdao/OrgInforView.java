package com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;

/**
 * Created by Administrator on 2017/9/14.
 */

public interface OrgInforView extends IView {
    void validationSucceed(OrgInfo data);//验证成功回调

    void validationSucceed();//验证成功回调

    //查看申请状态（二级域名，修改组织名称）
    void checkStateFor();//申请中

    void checkStateNameNo();//没有组织名称申请

    void checkStateDomainNo();//没有申请


    void getUserTypeSuccend(ApiResponse<UserType> userTypeApiResponse, String msg,Throwable  mThrowable);//获取用户类型
}
