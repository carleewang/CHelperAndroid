package com.cpigeon.cpigeonhelper.modular.flyarea.presenter;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.daoimpl.FlyingImpl;
import com.cpigeon.cpigeonhelper.modular.flyarea.view.viewdao.FlyingView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/25.
 */

public class FlyingPresenter extends BasePresenter<FlyingView, FlyingImpl> {

    private Map<String, Object> postParams = new HashMap<>();//存放参数
    private long timestamp;//时间搓

    public FlyingPresenter(FlyingView mView) {
        super(mView);
    }

    @Override
    protected FlyingImpl initDao() {
        return new FlyingImpl();
    }


    /**
     * 获取我的(参考)司放地列表
     * type 获取类型 【user ：用户列表 refer：参考司放地列表】【默认值 user】
     * key  查询关键字
     * pi  页码【小于 0 时获取全部，默认值-1】
     * ps 页大小【一页记录条数，默认值 10】
     */
    public void getFlyingAreasData(String type, String key, int pi, int ps) {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据

        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("type", type);//获取类型 【user ：用户列表 refer：参考司放地列表】【默认值 user】
        postParams.put("key", key);//查询关键字
        postParams.put("pi", pi);//页码【小于 0 时获取全部，默认值-1】
        postParams.put("ps", ps);//页大小【一页记录条数，默认值 10】

        //开始请求数据
        mDao.requestFlyingAres(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        //请求返回结果
        mDao.flyingAreasData = new IBaseDao.GetServerData<List<FlyingAreaEntity>>() {
            @Override
            public void getdata(ApiResponse<List<FlyingAreaEntity>> listApiResponse) {
                mView.getFlyingData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFlyingData(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 添加司放地
     *
     * @param alias//司放地别名
     * @param area//司放地
     * @param lo//经度【安捷格式】
     * @param la//纬度【安捷格式】
     */
    public void addFlyingArea(String alias, String area, double lo, double la) {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据

        postParams.put("uid", AssociationData.getUserId());//用户id

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }

        postParams.put("alias", alias);//司放地别名
        postParams.put("area", area);//司放地
        postParams.put("lo", lo);//经度【安捷格式】
        postParams.put("la", la);//纬度【安捷格式】

        //开始提交添加司放地的申请
        mDao.submitAddFlyingArea(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.addFlyingAreasData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> addApiResponse) {
                mView.addFlyingFlyingData(addApiResponse, addApiResponse.getMsg(), null);
                switch (addApiResponse.getErrorCode()) {
                    case 0://成功
                        if (RealmUtils.getServiceType().equals("geyuntong")) {
                            //鸽运通
                            mView.getErrorNews("添加司放地成功");
                            mView.getFlyingDataNull("添加司放地成功");
                        } else if (RealmUtils.getServiceType().equals("xungetong")) {
                            //训鸽通
                            mView.getErrorNews("添加训放地成功");
                            mView.getFlyingDataNull("添加训放地成功");
                        }

                        break;
                    default://其他错误
                        mView.getErrorNews("添加失败:" + addApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.addFlyingFlyingData(null, null, throwable);
                mView.getThrowable(throwable);
            }
        };

    }

    /**
     * 修改司放地
     */
    public void modifyFlyingArea(int faid, String alias, String area, String lo, String la) {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合之前的数据

        postParams.put("uid", AssociationData.getUserId());//用户id

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }

        postParams.put("faid", faid);//需要修改的司放地坐标
        postParams.put("alias", alias);//司放地别名(备注)
        postParams.put("area", area);//司放地
        postParams.put("lo", lo);//经度【安捷格式】
        postParams.put("la", la);//维度【安捷格式】


        mDao.submitAmendFlyingArea(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.addFlyingAreasData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> addApiResponse) {
//                Log.d(TAG, "getdata: 修改司放地请求返回错误码---》" + addApiResponse.getErrorCode());
                switch (addApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.getFlyingDataNull("修改成功");
                        break;
                    default://其他错误
                        mView.getErrorNews("修改失败:" + addApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }

        };
    }


    /**
     * 删除司放地
     */
    public void delFlyingArea(int faid) {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("faid", faid);//司放地 ID

        mDao.delAmendFlyingArea(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.addFlyingAreasData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> addApiResponse) {
//                Log.d(TAG, "getdata: 删除司放地请求返回错误码---》" + addApiResponse.getErrorCode());
                switch (addApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.getFlyingDataNull("删除成功");
                        break;
                    default://其他错误
                        mView.getErrorNews("删除失败:" + addApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }
}
