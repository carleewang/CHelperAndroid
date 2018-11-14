package com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;

/**
 * Created by Administrator on 2018/3/14.
 */

public class OrgInforViewImpl implements OrgInforView {
    @Override
    public void validationSucceed(OrgInfo data) {

    }

    @Override
    public void validationSucceed() {

    }

    @Override
    public void checkStateFor() {

    }

    @Override
    public void checkStateNameNo() {

    }

    @Override
    public void checkStateDomainNo() {

    }

    @Override
    public void getUserTypeSuccend(ApiResponse<UserType> userTypeApiResponse, String msg, Throwable mThrowable) {

    }


    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}
