package com.cpigeon.cpigeonhelper.modular.menu.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.daoimpl.BullentinImpl;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.BulletinView;

import java.util.List;

/**
 * 公告通知
 * Created by Administrator on 2017/9/20.
 */

public class BulletinPresenter extends BasePresenter<BulletinView, BullentinImpl> {

    public BulletinPresenter(BulletinView mView) {
        super(mView);
    }

    @Override
    protected BullentinImpl initDao() {
        return new BullentinImpl();
    }


    public void getBullentinData() {
        mDao.downServierBullentinData(AssociationData.getUserToken());
        mDao.getServerData = new IBaseDao.GetServerData<List<BulletinEntity>>() {

            @Override
            public void getdata(ApiResponse<List<BulletinEntity>> listApiResponse) {
                switch (listApiResponse.getErrorCode()) {
                    case 0:
                        mView.getBulletData(listApiResponse.getData());
                        break;
                    default:
                        mView.getErrorNews("获取内容失败，错误码");

                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }
}
