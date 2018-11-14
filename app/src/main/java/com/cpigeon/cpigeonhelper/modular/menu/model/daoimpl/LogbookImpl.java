package com.cpigeon.cpigeonhelper.modular.menu.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.dao.ILogbookDao;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/13.
 */
public class LogbookImpl implements ILogbookDao {

    public GetServerData<List<LogbookEntity>> getServerData;

    /**
     * 获取操作日志数据
     */
    public void downLogboosData(String userToken, Map<String, Object> urlParams) {
        RetrofitHelper.getApi()
                .getUserOperateLogs(userToken, urlParams)
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
