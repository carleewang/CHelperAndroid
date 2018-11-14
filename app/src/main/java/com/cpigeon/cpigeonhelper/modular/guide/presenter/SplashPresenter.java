package com.cpigeon.cpigeonhelper.modular.guide.presenter;

import com.cpigeon.cpigeonhelper.BuildConfig;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.DeviceBean;
import com.cpigeon.cpigeonhelper.modular.guide.model.daoimpl.SplashImpl;
import com.cpigeon.cpigeonhelper.modular.guide.view.viewdao.ISplashView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */

public class SplashPresenter extends BasePresenter<ISplashView, SplashImpl> {

    private String TAG = "print";

    public SplashPresenter(ISplashView mView) {
        super(mView);
    }

    @Override
    protected SplashImpl initDao() {
        return new SplashImpl();
    }


    /**
     * 查看当前用户在该设备是否登录过
     */
    public void isLogin() {
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, Object> postParams = new HashMap<>();
        postParams.put("uid", AssociationData.getUserId());
        postParams.put("u", AssociationData.getUserName());
        postParams.put("appid", BuildConfig.APPLICATION_ID);

        if (RealmUtils.getInstance().existUserLoginEntity()) {
            postParams.put("p", String.valueOf(AssociationData.getUserPassword()));//密码
        }

        //查看是否已经登录过
        mDao.isLoginData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        //登录返回数据
        mDao.getServerData = new IBaseDao.GetServerData<DeviceBean>() {
            @Override
            public void getdata(ApiResponse<DeviceBean> deviceBeanApiResponse) {

                switch (deviceBeanApiResponse.getErrorCode()) {

                    case 0://返回成功
                        if (deviceBeanApiResponse.getData().getDeviceid().equals(AssociationData.DEV_ID)) {
                            //该设备之前已经登录过账号
                            mView.isLastLogin(1);
                        } else {
                            //之前没有登录过设备
                            mView.isLastLogin(2);
                        }
                        break;

                    default:
                        mView.isLastLogin(2);
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);//抛出异常
            }
        };
    }
}
