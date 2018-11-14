package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTService;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl.GYTHomeImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTHomeView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */

public class GYTHomePresenter extends BasePresenter<GYTHomeView, GYTHomeImpl> {

    private Map<String, Object> postParams = new HashMap<>();

    public GYTHomePresenter(GYTHomeView mView) {
        super(mView);
    }

    @Override
    protected GYTHomeImpl initDao() {
        return new GYTHomeImpl();
    }

    /**
     * 获取鸽运通主页的数据
     */
    public void getGYTHomeData(String serviceType) {
        postParams.clear();//清除之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户 ID【管理员、授权用户均可】

        if (serviceType.equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        } else {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        }

        //获取鸽运通主页统计数据
        mDao.downGYTStatisticalData(AssociationData.getUserToken()
                , postParams);
        mDao.getServerStatisticalData = new IBaseDao.GetServerData<GYTStatisticalEntity>() {
            @Override
            public void getdata(ApiResponse<GYTStatisticalEntity> dataApiResponse) {
                mView.getStatisticalData(dataApiResponse, dataApiResponse.getMsg());
                switch (dataApiResponse.getErrorCode()) {
                    case 0:
                        mView.gytStatisticalData(dataApiResponse.getData());
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取鸽运通服务数据  （type==geren  获取训鸽通服务数据）
     */
    public void gytServerDatas(String type, String serviceType) {

        postParams.clear();//清除之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户 ID【管理员、授权用户均可】
        postParams.put("type", type);//组织类型 【xiehui gongpeng  geren】

        if (serviceType == null) {
            return;
        }
        //获取鸽运通服务数据
        mDao.dowdGYTData(AssociationData.getUserToken(),
                postParams);
        mDao.getServerData = new IBaseDao.GetServerData<GYTHomeEntity>() {
            @Override
            public void getdata(ApiResponse<GYTHomeEntity> dataApiResponse) {
                mView.getServiceData(dataApiResponse, dataApiResponse.getMsg());
                switch (dataApiResponse.getErrorCode()) {
                    case 0://
                        GYTService gytService = new GYTService();
                        if (dataApiResponse.getData() == null) {
                            gytService.setIsClosed(true);//是否已关闭
                            gytService.setScores(dataApiResponse.getData().getScores());//鸽币
                            RealmUtils.getInstance().insertGYTService(gytService);//添加鸽运通服务
                        } else {
                            if (RealmUtils.getInstance().existGYTInfo()) {//是否存在鸽运通服务
                                RealmUtils.getInstance().deleteGYTInfo();//删除鸽运通服务
                            }

                            gytService.setGrade(dataApiResponse.getData().getGrade());//用户等级
                            gytService.setAuthNumber(dataApiResponse.getData().getAuthNumber());//授权次数
                            gytService.setOpenTime(dataApiResponse.getData().getOpenTime());//开通时间
                            gytService.setReason(dataApiResponse.getData().getReason());//关闭原因
                            gytService.setIsClosed(dataApiResponse.getData().isIsClosed());//是否已关闭
                            gytService.setUsefulTime(dataApiResponse.getData().getUsefulTime());//使用时间
                            gytService.setIsExpired(dataApiResponse.getData().isIsExpired());//是否到期
                            gytService.setExpireTime(dataApiResponse.getData().getExpireTime());//到期时间
                            gytService.setScores(dataApiResponse.getData().getScores());//鸽币
                            RealmUtils.getInstance().insertGYTService(gytService);//添加鸽运通服务
                        }
                        break;
                    default:
//                        mView.getErrorNews(dataApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

}
