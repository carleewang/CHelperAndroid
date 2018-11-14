package com.cpigeon.cpigeonhelper.modular.menu.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.menu.model.dao.ISettingDao;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/13.
 */

public class SettingImpl implements ISettingDao {

    public GetServerData<String> getServerData;

    /**
     * 发送退出登录请求
     */
    @Override
    public void loginOut(String userToken) {
        RetrofitHelper.getApi()
                .logout(userToken)
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


    /**
     * 修改登录密码
     */
    @Override
    public void alterUserPas() {

    }

    /**
     * 修改支付密码
     */
    @Override
    public void alterPlayPas() {

    }

}
