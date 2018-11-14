package com.cpigeon.cpigeonhelper.modular.geyuntong.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.MonitorData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.daoimpl.MonitorImpl;
import com.cpigeon.cpigeonhelper.modular.geyuntong.view.viewdao.MonitorView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鸽车监控的控制层
 * Created by Administrator on 2017/10/11.
 */

public class GYTMonitorPresenter extends BasePresenter<MonitorView, MonitorImpl> {

    private Map<String, Object> postParams = new HashMap<>();//存放参数
    private long timestamp;//时间搓

    public GYTMonitorPresenter(MonitorView mView) {
        super(mView);
    }

    @Override
    protected MonitorImpl initDao() {
        return new MonitorImpl();
    }

    /**
     * .获取发布的视频照片
     *
     * @param ft 文件类型【image video】
     */
    public void getImgOrVideo(String ft) {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());// 鸽运通赛事 ID
        postParams.put("ft", ft);//文件类型【image video】

        mDao.getMonitorImgOrVideo(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServerData = new IBaseDao.GetServerData<List<ImgOrVideoEntity>>() {
            @Override
            public void getdata(ApiResponse<List<ImgOrVideoEntity>> listApiResponse) {

                mView.imgOrVideoData(listApiResponse, listApiResponse.getMsg(), null);

//                switch (listApiResponse.getErrorCode()) {
//                    case 0:
//                        mView.imgOrVideoData(listApiResponse.getData());
//                        break;
//                    default:
//                        mView.getErrorNews("获取数据失败：" + listApiResponse.getMsg());
//                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.imgOrVideoData(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };

    }


    /**
     * 开始监控
     */
    public void startMonitor() {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());// 鸽运通赛事 ID

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserAccountTypeStrings());//组织类型 【xiehui gongpeng】
            Log.d("kqbs", "getdata: " + AssociationData.getUserAccountTypeStrings());
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }

        mDao.getStartMonitor(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getStartMonitorData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> startApiResponse) {
                Log.d("kqbs", "getdata: " + startApiResponse.toJsonString());
                mView.openMonitorResults(startApiResponse, startApiResponse.getMsg());
//                switch (startApiResponse.getErrorCode()) {
//                    case 0:
//                        mView.succeed();//开启比赛成功后回调
//                        break;
//                    default:
//                        mView.fail();//开启比赛失败后回调
//                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("kqbs", "getdata: 异常");
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 结束监控
     */
    public void stopMonitor() {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());// 鸽运通赛事 ID

        if (RealmUtils.getServiceType().equals("geyuntong")) {
            postParams.put("type", AssociationData.getUserType());//组织类型 【xiehui gongpeng】
        } else if (RealmUtils.getServiceType().equals("xungetong")) {
            postParams.put("type", "geren");//组织类型 个人
        }


        mDao.getStopMonitor(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getStartMonitorData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> startApiResponse) {
//                Log.d(TAG, "获取---" + "结束监控" + "错误码：--》" + startApiResponse.getErrorCode());
                switch (startApiResponse.getErrorCode()) {
                    case 0:
//                        Log.d(TAG, "获取结束监控的数据成功: " + startApiResponse.getMsg());
                        mView.succeed();//成功后回调
                        break;
                    default:
                        mView.getErrorNews("获取结束监控的数据失败：" + startApiResponse.getMsg());
                        mView.fail();//失败后回调
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取监控定位数据报表
     */
    public void getLocationInfo() {

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());// 鸽运通赛事 ID
        postParams.put("hw", "y");// 是否包含天气信息【默认不包含，需要时请填 y】

        mDao.getLocationInfoData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getLocationInfoDatas = new IBaseDao.GetServerData<List<LocationInfoEntity>>() {
            @Override
            public void getdata(ApiResponse<List<LocationInfoEntity>> listApiResponse) {
//                Log.d(TAG, "获取---" + "位置信息" + "错误码：--》" + listApiResponse.getErrorCode());
                switch (listApiResponse.getErrorCode()) {
                    case 0:
//                        Log.d(TAG, "获取位置信息的数据成功: " + listApiResponse.getMsg() + "   定位点数——》" + listApiResponse.getData().size());
                        mView.locationInfoDatas(listApiResponse.getData());
//                        mView.succeed();//成功后回调
                        break;
                    default:
                        mView.getErrorNews("获取位置信息的数据失败：" + listApiResponse.getMsg());
//                        mView.fail();//失败后回调
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 51.获取鸽运通定位信息 GetGTYRaceLocation中鸽网
     */
    public void getGTYRaceLocation(int lid) {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());// 鸽运通赛事 ID
        postParams.put("lid", lid);// 定位 ID【用于增量获取定位信息】

        mDao.getGTYRaceLocation(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getRaceLocation = new IBaseDao.GetServerData<List<RaceLocationEntity>>() {
            @Override
            public void getdata(ApiResponse<List<RaceLocationEntity>> listApiResponse) {
//                Log.d("hqdwsj", "getdata: "+listApiResponse.toJsonString());
                Log.d("hqdwsj", "getdata: 结果");
                switch (listApiResponse.getErrorCode()) {
                    case 0:
                        if (listApiResponse.getData().size() > 0) {
                            mView.getRaceLocation(listApiResponse.getData());
                        } else {

                        }
                        break;
                    default:
                        mView.getErrorNews("获取鸽运通定位信息失败：" + listApiResponse.getErrorCode() + "  " + listApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //结束监控  dialog 显示信息
    public void getKjLc() {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", MonitorData.getMonitorId());//监控赛事ID
        mDao.getGTYKjLc(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getKjLcEntity = new IBaseDao.GetServerData<KjLcEntity>() {
            @Override
            public void getdata(ApiResponse<KjLcEntity> dataApiResponse) {

                if (dataApiResponse != null && dataApiResponse.getErrorCode() == 0 && dataApiResponse.getData() != null) {
                    if (MonitorData.getMonitorStateCode() == 2) {//监控结束，显示回放

                        try {
                            if (Double.valueOf(dataApiResponse.getData().getSfdjd()) == 0 || Double.valueOf(dataApiResponse.getData().getSfdwd()) == 0) {
                                SetDialogData.sfdzb = "司放地坐标：用户未设置";
                            } else {
                                SetDialogData.sfdzb = "司放地坐标：" + GPSFormatUtils.strToDMs(dataApiResponse.getData().getSfdjd()) + "E/" + GPSFormatUtils.strToDMs(dataApiResponse.getData().getSfdwd()) + "N";
                            }
                        } catch (Exception e) {
                            SetDialogData.sfdzb = "司放地坐标：用户未设置";
                        }

                        try {
                            if (dataApiResponse.getData().getTianqi() != null && dataApiResponse.getData().getTianqi().length() > 0) {
                                SetDialogData.sfdtq = "司放地天气：" + dataApiResponse.getData().getTianqi();
                            } else {
                                SetDialogData.sfdtq = "司放地天气：暂无数据";
                            }

                        } catch (Exception e) {
                            SetDialogData.sfdtq = "司放地天气：暂无数据";
                        }

                        try {
                            if (dataApiResponse.getData().getSfd() != null && dataApiResponse.getData().getSfd().length() > 0) {
                                SetDialogData.dqzb = "司放地点：" + dataApiResponse.getData().getSfd();
                            } else {
                                SetDialogData.dqzb = "司放地点：暂无数据";
                            }
                        } catch (Exception e) {
                            SetDialogData.dqzb = "司放地点：暂无数据";
                        }

                        //获取空距里程成功
                        try {
                            SetDialogData.kj = MonitorDialogPresenter.setUllage(Double.valueOf(dataApiResponse.getData().getKongju()));
                        } catch (Exception e) {
                            SetDialogData.kj = "空距：0 公里";
                        }

                        try {
                            SetDialogData.lc = MonitorDialogPresenter.setLc(Double.valueOf(dataApiResponse.getData().getLicheng()));
                        } catch (Exception e) {
                            SetDialogData.lc = "里程：0 公里";

                        }

                    }
                }

                mView.getKjLcData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getKjLcData(null, null, throwable);
            }
        };
    }

    //监控未结束 获取空距，里程
    public void getKjLcInfo(double sfdjd, double sfdwd, double jd, double wd) {


        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", MonitorData.getMonitorId());//监控赛事ID

        if (sfdjd != 0 || sfdwd != 0) {
            postParams.put("sfdjd", sfdjd);//司放地经度
            postParams.put("sfdwd", sfdwd);//司放地纬度
        }

        if (jd != 0 || wd != 0) {
            postParams.put("jd", jd);//起点经度
            postParams.put("wd", wd);//起点纬度
        }

        mDao.getGTYKjLcInfo(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getKjLcInfoEntity = new IBaseDao.GetServerData<KjLcInfoEntity>() {
            @Override
            public void getdata(ApiResponse<KjLcInfoEntity> dataApiResponse) {
                Log.d("xiaohlss", "getdatass: id-->" + MonitorData.getMonitorId() + "   uid-->" + AssociationData.getUserId());
                Log.d("xiaohlss", "getdatass: sfdjd-->" + sfdjd + "   sfdwd-->" + sfdwd);
                Log.d("xiaohlss", "getdatass: jd-->" + jd + "   wd-->" + wd);
                Log.d("xiaohlss", "getdatass: UserToken-->" + AssociationData.getUserToken() + "   ApiSign-->" + CommonUitls.getApiSign(timestamp, postParams));

                Log.d("xiaohlss", "getdata: " + dataApiResponse.toJsonString());
                mView.getKjLcInfoData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("xiaohlss", "getdatass: id-->" + MonitorData.getMonitorId() + "   uid-->" + AssociationData.getUserId());
                Log.d("xiaohlss", "getdatass: sfdjd-->" + sfdjd + "   sfdwd-->" + sfdwd);
                Log.d("xiaohlss", "getdatass: jd-->" + jd + "   wd-->" + wd);
                Log.d("xiaohlss", "getdatass: UserToken-->" + AssociationData.getUserToken() + "   ApiSign-->" + CommonUitls.getApiSign(timestamp, postParams));
                Log.d("xiaohlss", "getdatass: 异常" + throwable.getLocalizedMessage());
                mView.getKjLcInfoData(null, null, throwable);
            }
        };
    }

    //比赛监控授权检查
    public void gytRaceChk() {
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());//监控赛事ID

        mDao.gytRaceChk(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.gytRaceChkService = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d("qxjc", "getdata: " + dataApiResponse.toJsonString());
                mView.gytRaceChkData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("qxjc", "getdata: " + throwable.getLocalizedMessage());
                mView.gytRaceChkData(null, null, throwable);
            }
        };
    }

    //离线上传
    public void gytOfflineUpload(String datas) {

        Log.d("asdfsa", "writeToServer: 11");

        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("rid", MonitorData.getMonitorId());//监控赛事ID
        postParams.put("datas", datas);//离线缓存的数据
        Log.d("asdfsa", "writeToServer: 7");
        mDao.gytOfflineUploadService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.gytOfflineUploadData = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d("asdfsa", "writeToServer: 8" + dataApiResponse.toJsonString());
                mView.gytOfflineUploadData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("asdfsa", "writeToServer: 9-->" + throwable.getLocalizedMessage());
                mView.gytOfflineUploadData(null, null, throwable);
            }
        };

    }

}
