package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;

/**
 * Created by Administrator on 2018/4/11.
 */

public class DuesPayViewImpl implements IView {


    //获取年度会费结果
    public void getHFInfoResults(ApiResponse<HFInfoEntity> listApiResponse, String msg, Throwable mThrowable) {

    }

    //获取请求结果
    public void getServiceResults(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

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
