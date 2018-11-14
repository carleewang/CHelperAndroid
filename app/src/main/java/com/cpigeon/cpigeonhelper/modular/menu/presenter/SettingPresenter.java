package com.cpigeon.cpigeonhelper.modular.menu.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.menu.model.daoimpl.SettingImpl;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.SettingView;

/**
 * 设置页控制层
 * Created by Administrator on 2017/9/13.
 */

public class SettingPresenter extends BasePresenter<SettingView, SettingImpl> {


    public SettingPresenter(SettingView mView) {
        super(mView);
    }

    @Override
    protected SettingImpl initDao() {
        return new SettingImpl();
    }


    /**
     * 退出登录
     */
    public void startOutLogin() {

        mDao.loginOut(AssociationData.getUserToken());
        mDao.getServerData = new IBaseDao.GetServerData<String>() {
            @Override
            public void getdata(ApiResponse<String> stringApiResponse) {
                switch (stringApiResponse.getErrorCode()) {
                    case 0://退出成功
                        mView.succeed();
                        break;
                    case 10001://用户不存在
                        mView.getErrorNews("用户不存在");
                        break;
                    case 20000://退出失败
                        mView.getErrorNews("退出失败");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }



}
