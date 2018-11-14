package com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.presenter;

import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.BsdxSettingEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.daoimpl.PlayAdminImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.vew.viewdao.PlayAdminView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.List;

/**
 * 比赛管理控制层
 * Created by Administrator on 2017/12/18.
 */

public class PlayAdminPrensenter extends BasePresenter<PlayAdminView, PlayAdminImpl> {

    public PlayAdminPrensenter(PlayAdminView mView) {
        super(mView);
    }

    @Override
    protected PlayAdminImpl initDao() {
        return new PlayAdminImpl();
    }

    /**
     * 获取比赛管理列表
     */
    public void getPlayList() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getPlayAdminData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getPlayData = new IBaseDao.GetServerData<List<PlayListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<PlayListEntity>> listApiResponse) {
                mView.getPlayAdminData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {

                mView.getPlayAdminData(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取协会比赛短信设置
     */
    public void getBsdxSetting(int bsid) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("bsid", bsid);//赛事id

        mDao.getBsdxSettingData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getBsdxSettingData = new IBaseDao.GetServerData<BsdxSettingEntity>() {
            @Override
            public void getdata(ApiResponse<BsdxSettingEntity> dxsetApiResponse) {
                mView.getBsdxData(dxsetApiResponse, dxsetApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 提交删除比赛申请
     */
    public void subDelPlayApply(int bsid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("bsid", bsid);//赛事id

        mDao.subDelPlayApplyData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.sbuBsdxSetting = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dxSubApiResponse) {

                mView.subBsdxData(dxSubApiResponse, dxSubApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 提交协会比赛短信设置
     */
    public void subSmsSetting(int bsid, EditText xhjc, BsdxSettingEntity mBsdxSettingEntity) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("bsid", bsid);//赛事id


        if (mBsdxSettingEntity.isBsxm()) {
            postParams.put("bsxm", 1);//项目
        } else {
            postParams.put("bsxm", 0);//项目
        }

        if (mBsdxSettingEntity.isGzfs()) {
            postParams.put("gzfs", 1);//分速
        } else {
            postParams.put("gzfs", 0);//分速
        }

        if (mBsdxSettingEntity.isGcsj()) {
            postParams.put("gcsj", 1);//时间
        } else {
            postParams.put("gcsj", 0);//时间
        }


        if (mBsdxSettingEntity.isKqzt()) {
            postParams.put("sffs", 1);//短信
        } else {
            postParams.put("sffs", 0);//短信
            postParams.put("bsxm", 0);//项目
            postParams.put("gzfs", 0);//分速
            postParams.put("gcsj", 0);//时间
        }
        postParams.put("xhjc", xhjc.getText().toString());//协会简称

        mDao.subBsdxSettingData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.sbuBsdxSetting = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dxSubApiResponse) {
                mView.subBsdxData(dxSubApiResponse, dxSubApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

}
