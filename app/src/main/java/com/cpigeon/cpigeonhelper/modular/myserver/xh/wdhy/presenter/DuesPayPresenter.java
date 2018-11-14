package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.impldao.DuesPayImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.DuesPayViewImpl;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

/**
 * 会费缴纳控制层
 * Created by Administrator on 2018/4/11.
 */

public class DuesPayPresenter extends BasePresenter<DuesPayViewImpl, DuesPayImpl> {
    public DuesPayPresenter(DuesPayViewImpl mView) {
        super(mView);
    }

    @Override
    protected DuesPayImpl initDao() {
        return new DuesPayImpl();
    }

    //设置年度会费
    public void setXHHYGL_SetHuiFei(String y, String hf) {

        if (y.isEmpty() || y.length() == 0) {
            mView.getErrorNews("请选择设置会费的年份");
            return;
        }
        if (hf.isEmpty() || hf.length() == 0) {
            mView.getErrorNews("请输入会费的金额");
            return;
        }


        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        postParams.put("y", y);//年份
        postParams.put("hf", hf);//会费金额

        mDao.setXHHYGL_SetHuiFeiService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }


    //获取年度会费
    public void setXHHYGL_SetHuiFei(String y) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);//年份

        mDao.setXHHYGL_GetHuiFeiService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getHFInfoEntityData = new IBaseDao.GetServerData<HFInfoEntity>() {
            @Override
            public void getdata(ApiResponse<HFInfoEntity> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());
                mView.getHFInfoResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getHFInfoResults(null, null, throwable);
            }
        };
    }
}
