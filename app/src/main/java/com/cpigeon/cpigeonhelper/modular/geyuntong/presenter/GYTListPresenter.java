package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.util.Log;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl.GYTListImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.GYTListView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/9/27.
 */

public class GYTListPresenter extends BasePresenter<GYTListView, GYTListImpl> {

    private Map<String, Object> postParams = new HashMap<>();//存放参数
    private long timestamp;//时间搓

    public GYTListPresenter(GYTListView mView) {
        super(mView);
    }

    @Override
    protected GYTListImpl initDao() {
        return new GYTListImpl();
    }


    /**
     * 获取鸽运通比赛列表
     * key  查询关键字【目前可查询：比赛名称、司放地】
     * ps  页大小【一页记录条数，默认值 10】
     * pi  页码【小于 0 时获取全部，默认值-1】
     * isauth);//是否仅查询被授权的比赛【true:获取被授权的比赛信息，false:获取自己创建的比赛信息】【默认都获取】
     * status);//比赛状态筛选【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
     */
    public void getGYTRaceList(int ps, int pi, String key, String isauth, String status) {
        Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("uid", AssociationData.getUserId());//用户ID

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            urlParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            urlParams.put("type", "geren");//组织类型 个人
        }

        urlParams.put("key", key);//查询关键字【目前可查询：比赛名称、司放地】
        urlParams.put("ps", ps);//页大小【一页记录条数，默认值 10】
        urlParams.put("pi", pi);//页码【小于 0 时获取全部，默认值-1】
        urlParams.put("isauth", isauth);//是否仅查询被授权的比赛【true:获取被授权的比赛信息，false:获取自己创建的比赛信息】【默认都获取】
        urlParams.put("status", status);//比赛状态筛选【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】
//        urlParams.put("status", 2);//比赛状态筛选【0：未开始监控的；1：正在监控中：2：监控结束】【默认无筛选】

        mDao.downGYTRaceList(AssociationData.getUserToken(), urlParams);
        mDao.getServerData = new IBaseDao.GetServerData<List<GeYunTong>>() {
            @Override
            public void getdata(ApiResponse<List<GeYunTong>> listApiResponse) {
                mView.getGYTRaceList(listApiResponse, listApiResponse.getMsg(), null);
                Log.d("ddddcs", "getdata: " + listApiResponse.toJsonString());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getGYTRaceList(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 批量删除鸽运通比赛列表
     */
    public void deleteGYTRaces(String rids) {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }

        postParams.put("rids", rids);//：比赛 ID 列表【例：1,23,4】

        //开始请求数据
        mDao.requestDeleteGYTRaces(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getDelServerData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> delApiResponse) {
                Log.d("editGyt", "getdata: 1   "+rids);
                Log.d("editGyt", "getdata: " + delApiResponse.toJsonString());
//                Log.d(TAG, "getdata: 数据请求返回错误码---》" + delApiResponse.getErrorCode());
                switch (delApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.getReturnMsg("批量删除成功");
                        break;
                    default://其他错误
//                        android.util.Log.d(TAG, "getdata: 删除失败--》" + delApiResponse.getMsg());
                        mView.getErrorNews(delApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("editGyt", "getdata: 异常 "+rids+"-->"+throwable.getLocalizedMessage());
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 删除鸽运通比赛
     */
    public void deleteGYTRace(String rid) {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }

        postParams.put("rid", rid);//：比赛 ID


        //开始请求数据
        mDao.requestDeleteGYTRace(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getDelServerData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> delApiResponse) {
//                Log.d(TAG, "getdata: 数据请求返回错误码---》" + delApiResponse.getErrorCode());
                switch (delApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.getReturnMsg("删除成功");
                        break;
                    default://其他错误
//                        android.util.Log.d(TAG, "getdata: 删除失败--》" + delApiResponse.getMsg());
                        mView.getErrorNews(delApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 添加比赛
     *
     * @param rname 比赛名称
     * @param farea 司放地
     * @param lo    经度
     * @param la    纬度
     * @param faid  常用司放地 ID
     * @param show  是否公开 1：公开  0：不公开
     */
    public void addGytPlay(EditText rname, String farea, EditText lo, EditText la, String faid, int show, SweetAlertDialog mLoadDataDialog) {

        if (rname.getText().toString().isEmpty()) {
            mView.getErrorNews("输入监控名称不能为空");
            return;
        }

        if (mLoadDataDialog.isShowing()) mLoadDataDialog.dismiss();
        mLoadDataDialog.show();

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户 ID

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserAccountTypeStrings());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
            postParams.put("show", show);// 比赛是否公开 1：公开  0：不公开
        }

        postParams.put("rname", rname.getText().toString());//比赛名称
        postParams.put("farea", farea);//司放地
        postParams.put("faid", 0);//司放地
        if (!lo.getText().toString().isEmpty()) {
            postParams.put("lo", Double.valueOf(lo.getText().toString()));//经度
        }
        if (!la.getText().toString().isEmpty()) {
            postParams.put("la", Double.valueOf(la.getText().toString()));//纬度
        }

        mDao.submitAddGytPlay(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.serverData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> addApiResponse) {
//                Log.d(TAG, "getdata: 添加比赛请求返回错误码---》" + addApiResponse.getErrorCode());
                switch (addApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.addPlaySuccess();//添加比赛成功回调
                        break;
                    default://其他错误
                        mView.getErrorNews("添加监控失败:" + addApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 修改编辑比赛
     *
     * @param rname 比赛名称
     * @param farea 司放地
     * @param lo    经度
     * @param la    纬度
     * @param rid   比赛 ID
     */
    public void updataGytPlay(EditText rname, String farea, EditText lo, EditText la, String rid) {

        if (rname.getText().toString().isEmpty()) {
            mView.getErrorNews("输入监控名称不能为空");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户 ID

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }

        postParams.put("rname", rname.getText().toString());//比赛名称
        postParams.put("farea", farea);//司放地
        postParams.put("faid", 0);//司放地
        postParams.put("rid", rid);//比赛id
        if (!lo.getText().toString().isEmpty()) {
            postParams.put("lo", CommonUitls.GPS2AjLocation(Double.valueOf(lo.getText().toString())));//经度
        }
        if (!la.getText().toString().isEmpty()) {
            postParams.put("la", CommonUitls.GPS2AjLocation(Double.valueOf(la.getText().toString())));//纬度
        }
//        params.put("faid", faid);//常用司放地 ID

        mDao.submitUpdateGYTRace(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.serverData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> addApiResponse) {
//                Log.d(TAG, "getdata: 修改司放地请求返回错误码---》" + addApiResponse.getErrorCode());
                switch (addApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.addPlaySuccess();//添加比赛成功回调
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
     * 17.鸽运通比赛授权确认
     *
     * @param rid 鸽运通比赛 ID
     * @param ar  接收或拒绝授权【0：拒绝；1：接受】
     */
    public void getGYTAuthConfirm(int rid, int ar) {


        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户 ID
        postParams.put("rid", rid);//鸽运通比赛 ID
        postParams.put("ar", ar);//接收或拒绝授权【0：拒绝；1：接受】

        mDao.getGYTAuthConfirm(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.serverData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> addApiResponse) {
                Log.d(TAG, "获取授权比赛确认返回错误码---》" + addApiResponse.getErrorCode());
                switch (addApiResponse.getErrorCode()) {
                    case 0://成功
                        Log.d("GYTListAdapter", "getdata: 接受比赛授权");
                        mView.addPlaySuccess();//接受比赛成功回调
                        break;
                    default://其他错误
                        Log.d("GYTListAdapter", "getdata: 接受比赛授权错误");
                        mView.getErrorNews("获取授权监控确认失败:" + addApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("GYTListAdapter", "getdata: 接受比赛授权抛出异常");
                mView.getThrowable(throwable);
            }
        };

    }
}
