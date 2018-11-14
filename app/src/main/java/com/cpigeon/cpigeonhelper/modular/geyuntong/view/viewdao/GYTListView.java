package com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;

import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */

public interface GYTListView extends IView {

    //获取鸽运通列表数据
    void getGYTRaceList(ApiResponse<List<GeYunTong>> listApiResponse, String msg, Throwable mThrowable);

    void getGYTRaceLists(List<GeYunTong> geYunTongDatas);//获取鸽运通列表

    void getReturnMsg(String msg);//返回信息

//    void  isOpenGYT(boolean  isOpen);//是否开通鸽运通服务

    void addPlaySuccess();//添加(修改)鸽运通比赛成功后回调
}
