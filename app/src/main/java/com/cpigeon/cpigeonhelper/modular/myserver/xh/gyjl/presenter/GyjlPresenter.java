package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlMessageEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlReviewEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.impldao.GyjlImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.viewdao.GyjlViewImpl;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class GyjlPresenter extends BasePresenter<GyjlViewImpl, GyjlImpl> {

    public GyjlPresenter(GyjlViewImpl mView) {
        super(mView);
    }

    @Override
    protected GyjlImpl initDao() {
        return new GyjlImpl();
    }


    //获取用户给协会的留言
    public void getLY_XH() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        Log.d(TAG, "getdata:1 ");
        mDao.getServiceLY_XH(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceLY_XHData = new IBaseDao.GetServerData<List<GyjlMessageEntity>>() {
            @Override
            public void getdata(ApiResponse<List<GyjlMessageEntity>> listApiResponse) {
                Log.d(TAG, "getdata: " + listApiResponse.toJsonString());
                mView.getLyList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getLyList(null, null, throwable);
            }
        };
    }

    //获取信息评论
    public void getPL_XH() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        Log.d(TAG, "getdata:1 ");
        mDao.getServicePL_XH(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServicePL_XHData = new IBaseDao.GetServerData<List<GyjlReviewEntity>>() {
            @Override
            public void getdata(ApiResponse<List<GyjlReviewEntity>> listApiResponse) {
                Log.d(TAG, "getdata: " + listApiResponse.toJsonString());
                mView.getPLList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getPLList(null, null, throwable);
            }
        };
    }


    //删除留言或评论
    public void delLyPl_XH(String cid, String t) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("cid", cid);//留言或评论索引ID
        postParams.put("t", t);//删除留言；t=pl：删除评论。

        mDao.delServiceLyPL_XH(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getDelLyPlData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                Log.d(TAG, "getdata: " + listApiResponse.toJsonString());
                mView.getDelPLResults(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDelPLResults(null, null, throwable);
            }
        };
    }


    //删除回复
    public void delHF_LyPl_XH(String cid, String hfid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("cid", cid);//留言或评论索引ID
        postParams.put("hfid", hfid);//回复ID

        mDao.delServiceHF_LyPL_XH(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getDelLyPlData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                Log.d(TAG, "getdata: " + listApiResponse.toJsonString());
                mView.getDelPLResults(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getDelPLResults(null, null, throwable);
            }
        };
    }


    //回复留言或评论
    public void translateLyPl_XH(String cid, String t, String nr) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("cid", cid);//留言或评论索引ID
        postParams.put("t", t);//删除留言；t=pl：删除评论。
        postParams.put("nr", nr);//回复内容，不超过300个字符。

        mDao.translateServiceLyPL_XH(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getDelLyPlData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> listApiResponse) {
                Log.d(TAG, "getdata: " + listApiResponse.toJsonString());
                mView.getTraPLResults(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getTraPLResults(null, null, throwable);
            }
        };
    }
}
