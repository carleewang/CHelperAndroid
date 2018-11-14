package com.cpigeon.cpigeonhelper.modular.menu.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.dao.IBullentinDao;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/20.
 */

public class BullentinImpl implements IBullentinDao{

    public GetServerData<List<BulletinEntity>> getServerData;

    public void downServierBullentinData(String userToken){
        RetrofitHelper.getApi()
                .getAnnouncementTop(userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listApiResponse -> {
                            getServerData.getdata(listApiResponse);
                        }

                        , throwable -> {
                            getServerData.getThrowable(throwable);//抛出异常
                        });
    }

}
