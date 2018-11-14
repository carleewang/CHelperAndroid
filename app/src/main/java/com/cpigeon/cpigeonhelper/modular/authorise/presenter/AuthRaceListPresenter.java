package com.cpigeon.cpigeonhelper.modular.authorise.presenter;

import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.authorise.model.daoimpl.AuthRaceListImpl;
import com.cpigeon.cpigeonhelper.modular.authorise.view.viewdao.AuthRaceListView;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索授权比赛列表展示控制层
 * Created by Administrator on 2017/9/22.
 */
public class AuthRaceListPresenter extends BasePresenter<AuthRaceListView, AuthRaceListImpl> {
    private Map<String, Object> params = new HashMap<>();//请求参数保存
    private long timestamp;//时间戳

    public AuthRaceListPresenter(AuthRaceListView mView) {
        super(mView);
    }

    @Override
    protected AuthRaceListImpl initDao() {
        return new AuthRaceListImpl();
    }


    /**
     * 获取授权比赛列表
     *
     * @param queryKeywords
     * @param pi
     * @param ps
     */
    public void getAuthRaceListData(EditText queryKeywords, int pi, int ps) {

        int pis = pi;
        if (queryKeywords.getText().toString().isEmpty()) {//输入为空默认显示全部数据
            pis = -1;
        }

        params.clear();//清除之前的数据
        params.put("uid", AssociationData.getUserId());//用户 ID
        params.put("key", queryKeywords.getText().toString());//查询关键字【目前可查询：比赛名称、司放地】
        params.put("type", AssociationData.getUserAccountTypeStrings());//组织类型 xiehui gongpeng
        params.put("pi", pis);//页码【小于 0 时获取全部，默认值-1】
        params.put("ps", ps);//页大小【一页记录条数，默认值 10】
        params.put("isauth", false);//是否仅查询被授权的比赛【true:获取被授权的比赛信息，false:获取自己创建的比赛信息】【默认都获取】
        params.put("status", 0);//比赛状态筛选【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】


        mDao.requestAuthRaceList(AssociationData.getUserToken(),//用户通行验证
                params);//参数
        mDao.listGetServerData = new IBaseDao.GetServerData<List<GeYunTong>>() {
            @Override
            public void getdata(ApiResponse<List<GeYunTong>> listApiResponse) {

                switch (listApiResponse.getErrorCode()) {
                    case 0://请求成功
                        if (listApiResponse.getData().isEmpty()) {//请求成功没有数据
//                            Log.d(TAG, "getdata: 请求成功没有数据");
                            mView.getRaceListDataIsNo();
                        } else {//请求成功有数据
                            mView.getRaceListData(listApiResponse.getData());
//                            Log.d(TAG, "getdata: 请求成功有数据");
                        }
                        break;
                    case 1000://用户 ID 校验失败
                        mView.getErrorNews("用户 ID 校验失败");
                        break;
                    default://其他状态错误
                        mView.getErrorNews("数据加载失败，错误码：" + listApiResponse.getErrorCode());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * .鸽运通比赛授权(接口不一样 暂时舍弃)
     */
    public void rquestGYTRaceAuth(int auid, int rid, int c) {

        //时间戳
        timestamp = System.currentTimeMillis() / 1000;

        params.clear();//清除集合中之前保存的数据
        params.put("uid", AssociationData.getUserId());
        params.put("type", AssociationData.getUserType());
        params.put("auid", auid);
        params.put("rid", rid);
        params.put("c", c);


        mDao.requestGYTRaceAuth(AssociationData.getUserToken(), params, timestamp, CommonUitls.getApiSign(timestamp, params));
        mDao.getGYTRaceAuthData = new IBaseDao.GetServerData<String>() {
            @Override
            public void getdata(ApiResponse<String> stringApiResponse) {


                mView.addAuthData(stringApiResponse, stringApiResponse.getMsg(), null);
//                switch (stringApiResponse.getErrorCode()) {
//                    case 0://授权成功
//                        mView.getErrorNews("授权成功");
//                        break;
//                    default://授权失败
//                        mView.getErrorNews("授权失败：" + stringApiResponse.getMsg());
//                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
//                mView.getThrowable(throwable);
                mView.addAuthData(null, null, throwable);
            }
        };
    }
}
