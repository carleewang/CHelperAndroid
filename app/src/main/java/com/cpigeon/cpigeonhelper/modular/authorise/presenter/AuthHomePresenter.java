package com.cpigeon.cpigeonhelper.modular.authorise.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AuthHomeEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.daoimpl.AuthHomeImpl;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AuthHomeView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/21.
 */

public class AuthHomePresenter extends BasePresenter<AuthHomeView,AuthHomeImpl>{


    public AuthHomePresenter(AuthHomeView mView) {
        super(mView);
    }

    @Override
    protected AuthHomeImpl initDao() {
        return new AuthHomeImpl();
    }


    /**
     * 获取授权列表数据
     */
    public void  getAuthHomeData(){

        mDao.downAuthHomeData(AssociationData.getUserToken(),
                AssociationData.getUserId());

//        Log.d(TAG, "getAuthHomeData: uid---->"+AssociationData.getUserId());
//        Log.d(TAG, "getAuthHomeData: userToken---->"+AssociationData.getUserToken());
        mDao.getServerData = new IBaseDao.GetServerData<List<AuthHomeEntity>>() {
            @Override
            public void getdata(ApiResponse<List<AuthHomeEntity>> authHomeEntityApiResponse) {
//                Log.d(TAG, "getdata: 返回错误码"+authHomeEntityApiResponse.getErrorCode());

                switch (authHomeEntityApiResponse.getErrorCode()){
                    case 0://返回成功
                        if(authHomeEntityApiResponse.getData().size()>0){//有数据返回
                           mView.authHomeListData(authHomeEntityApiResponse.getData());
                        }else {//没有数据返回
                            mView.authHomeListDataNO();
                        }
                        break;
                    case 1000://用户 ID 校验失败
//                        Log.d(TAG, "getdata: 用户 ID 校验失败");
                        mView.getErrorNews("用户 ID 校验失败");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.authHomeListDataNO();//没有数据返回
                mView.getThrowable(throwable);
            }
        };



    }


    /**
     * 取消比赛授权
     */
    public  void  getRemoveGYTRaceAuth(int rId){
        long timestamp = System.currentTimeMillis() / 1000;//时间戳

        Map<String ,Object> params = new HashMap<>();
        params.put("uid",AssociationData.getUserId());
        params.put("rid",rId);

        mDao.submitRemoveGYTRaceAuth(AssociationData.getUserToken(),params,timestamp, CommonUitls.getApiSign(timestamp, params));

        mDao.getServerRemoveData = new IBaseDao.GetServerData<String>() {
            @Override
            public void getdata(ApiResponse<String> stringApiResponse) {
                switch (stringApiResponse.getErrorCode()){
                    case 0://请求成功
//                        Log.d(TAG, "getdata: 请求成功");
                        mView.cancelSucceed();//取消授权成功后回调
                        break;
                    case 1000://用户 ID 校验失败
//                        Log.d(TAG, "getdata: 用户 ID 校验失败");
                        mView.getErrorNews("用户 ID 校验失败");
                        break;
                    case 20000://授权失败
//                        Log.d(TAG, "getdata: 授权失败");
                        mView.getErrorNews("授权失败");
                        break;

                    case 20004://比赛信息未找到
//                        Log.d(TAG, "getdata: 比赛信息未找到");
                        mView.getErrorNews("比赛信息未找到");
                        break;

                    case 20005://权限不足，无法取消授权(比赛不是由当前用户创建，因此无法取消授权)
//                        Log.d(TAG, "getdata: 权限不足，无法取消授权(比赛不是由当前用户创建，因此无法取消授权)");
                        mView.getErrorNews("权限不足，无法取消授权(比赛不是由当前用户创建，因此无法取消授权)");
                        break;

                    case 20006://赛事已开始监控，无法取消此赛事的授权
                        Log.d(TAG, "getdata: 赛事已开始监控，无法取消此赛事的授权");
                        mView.getErrorNews("赛事已开始监控，无法取消此赛事的授权");
                        break;

                    default://其他错误
//                        Log.d(TAG, "getdata: ");
                        mView.getErrorNews("返回失败，错误码："+stringApiResponse.getErrorCode());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };


    }
}
