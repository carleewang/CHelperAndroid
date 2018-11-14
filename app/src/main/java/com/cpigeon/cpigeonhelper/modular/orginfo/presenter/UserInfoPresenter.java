package com.cpigeon.cpigeonhelper.modular.orginfo.presenter;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.daoimpl.UserInfoImpl;
import com.cpigeon.cpigeonhelper.modular.orginfo.view.viewdao.UserInfoView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

/**
 * 用户信息控制层
 * Created by Administrator on 2017/12/19.
 */
public class UserInfoPresenter extends BasePresenter<UserInfoView, UserInfoImpl> {


    public UserInfoPresenter(UserInfoView mView) {
        super(mView);
    }

    @Override
    protected UserInfoImpl initDao() {
        return new UserInfoImpl();
    }


    /**
     * 获取用户信息（我的余额，我的订单）
     */
    public void getMyInfo() {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));

        mDao.getMyInfoData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.myInfoData = new IBaseDao.GetServerData<MyInfoEntity>() {
            @Override
            public void getdata(ApiResponse<MyInfoEntity> myInfoEntityApiResponse) {
                mView.getMyInfoData(myInfoEntityApiResponse, myInfoEntityApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取鸽币明细
     * CHAPI/V1/MyScores
     */
    public void getGbDataMx() {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));

        mDao.getGbMxData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getGbListDada = new IBaseDao.GetServerData<List<GbListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<GbListEntity>> gbListApiResponse) {
                mView.getGbmxData(gbListApiResponse, gbListApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getGbmxData(null, null, throwable);
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 分享创建分享码
     */
    public void getShareCode() {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));

        mDao.getCreateYaoQingMa(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getShareCodeEntity = new IBaseDao.GetServerData<ShareCodeEntity>() {
            @Override
            public void getdata(ApiResponse<ShareCodeEntity> dataApiResponse) {
                mView.getShareCodeData(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 分享图片视频获取鸽币
     */
    public void getShareImgVideo(String t) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));
        postParams.put("t", t);//tp=图片；sp=视频

        mDao.subShareImgVideo(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

    }


}
