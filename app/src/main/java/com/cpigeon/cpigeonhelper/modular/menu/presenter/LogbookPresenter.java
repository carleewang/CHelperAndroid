package com.cpigeon.cpigeonhelper.modular.menu.presenter;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.daoimpl.LogbookImpl;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.LogbookView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志的控制层
 * Created by Administrator on 2017/9/13.
 */

public class LogbookPresenter extends BasePresenter<LogbookView, LogbookImpl> {


    public LogbookPresenter(LogbookView mView) {
        super(mView);
    }


    @Override
    protected LogbookImpl initDao() {
        return new LogbookImpl();
    }


    /**
     * 处理数据
     */
    public void getLogboodData(int ps, int pi) {

        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("uid", String.valueOf(AssociationData.getUserId()));//用户id
        urlParams.put("type", "cpigeonhelper");//操作日志类型，默认cpigeonhelper
        urlParams.put("orgtype", AssociationData.getUserType());//组织类型
        urlParams.put("ps", String.valueOf(ps));//页大小【一页记录条数，默认值 10】
        urlParams.put("pi", String.valueOf(pi));//页码【小于 0 时获取全部，默认值-1
        mDao.downLogboosData(AssociationData.getUserToken(), urlParams);

        mDao.getServerData = new IBaseDao.GetServerData<List<LogbookEntity>>() {
            @Override
            public void getdata(ApiResponse<List<LogbookEntity>> listApiResponse) {

                mView.getDatas(listApiResponse, listApiResponse.getMsg(), null);

//                switch (listApiResponse.getErrorCode()) {
//                    case 0://成功
//                        mView.getDatas(listApiResponse.getData(),listApiResponse.getMsg());
//                        break;
//                    default:
//                        mView.getErrorNews(listApiResponse.getMsg());
//                        break;
//                }

            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDatas(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }
}











































