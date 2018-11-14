package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter;

import android.util.Log;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardNInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.entity.IdCardPInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AddRegisterEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootBuyEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HYGLInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserIdInfo;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PenalizeRecordEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearPayCostEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearSelectionEntitiy;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.impldao.MenberImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.MenberViewImpl;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 我的会员P层
 * Created by Administrator on 2018/3/24.
 */

public class MemberPresenter extends BasePresenter<MenberViewImpl, MenberImpl> {


    public MemberPresenter(MenberViewImpl mView) {
        super(mView);
    }

    @Override
    protected MenberImpl initDao() {
        return new MenberImpl();
    }

    public String getHintString(TextView et, String str) {
        return et.getText().equals(str) ? "" : et.getText().toString();
    }


    //信鸽协会会员管理系统信息
    public void getXHHYGL_UserInfo() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id


        mDao.XHHYGL_UserInfoService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHYGLInfoEntityData = new IBaseDao.GetServerData<HYGLInfoEntity>() {
            @Override
            public void getdata(ApiResponse<HYGLInfoEntity> listApiResponse) {
                mView.getHyglInfoData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getHyglInfoData(null, null, throwable);
            }
        };
    }

    //1、获取协会会员列表
    public void getDataHyList_XH(int pi, int ps, String s, String zt, String c,String xhuid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）
        postParams.put("s", s);//搜索会员姓名
        postParams.put("c", c);//地区
        postParams.put("xhuid", xhuid);//地区
        postParams.put("zt", zt);//会员状态：在册|禁赛|除名（默认显示所有状态的会员）
        mDao.getServiceHyList_XH(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyList_XHData = new IBaseDao.GetServerData<HyglHomeListEntity>() {
            @Override
            public void getdata(ApiResponse<HyglHomeListEntity> listApiResponse) {
                mView.getHyListData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getHyListData(null, null, throwable);
            }
        };
    }

    //1、获取协会会员列表
    public void getDataHyList_XH2(int pi, int ps, String s, String zt, String c,String xhuid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）
        postParams.put("s", s);//搜索会员姓名

        if (c.equals("全部地区")){
            postParams.put("c", "");//地区
        }else {
            postParams.put("c", c);//地区
        }

        postParams.put("xhuid", xhuid);//地区
        postParams.put("zt", zt);//会员状态：在册|禁赛|除名（默认显示所有状态的会员）
        mDao.getServiceHyList_XH2(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyList_XHData = new IBaseDao.GetServerData<HyglHomeListEntity>() {
            @Override
            public void getdata(ApiResponse<HyglHomeListEntity> listApiResponse) {
                mView.getHyListData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getHyListData(null, null, throwable);
            }
        };
    }

    //2、获取信鸽协会会员详细
    public void getXHHYGL_GetUserDetail(String mid,String xhuid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }

        postParams.put("mid", mid);//信鸽协会会员索引ID
        mDao.getUserDetailService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyDetail = new IBaseDao.GetServerData<HyUserDetailEntity>() {
            @Override
            public void getdata(ApiResponse<HyUserDetailEntity> dataApiResponse) {
                mView.getHyUserDetail(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getHyUserDetail(null, null, throwable);
            }
        };
    }


    //2、获取协会会员身份证信息
    public void getXHHYGL_GetUserIdInfo(String mid,String xhuid, String type) {
        Log.d(TAG, "getXHHYGL_GetUserIdInfo: 获取协会会员身份证信息");
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        postParams.put("mid", mid);//信鸽协会会员索引ID

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }

        mDao.getUserIdInfoService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getHyUserIdInfoData = new IBaseDao.GetServerData<HyUserIdInfo>() {
            @Override
            public void getdata(ApiResponse<HyUserIdInfo> dataApiResponse) {
                Log.d(TAG, "getXHHYGL_GetUserIdInfo: 结果" + dataApiResponse.toJsonString());
                mView.getHyUserIdInfo(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d(TAG, "getXHHYGL_GetUserIdInfo: 异常");
                mView.getHyUserIdInfo(null, null, throwable);
            }
        };
    }


    //添加协会会员
    public void getXHHYGL_JBXX_Add(AddRegisterEntity mAddRegisterEntity, IdCardPInfoEntity idCardInfoEntity,
                                   IdCardNInfoEntity idCardNInfoEntity, TextView tvHyxm, TextView tvSfzhm, File touxiang) {

//        if (idCardInfoEntity == null || idCardNInfoEntity == null) {
//            mView.getErrorNews("身份证不能为空");
//            return;
//        }
//
//
//        if (!tvHyxm.getText().toString().equals(idCardInfoEntity.name)) {
//            mView.getErrorNews("会员姓名与拍摄身份证姓名不符");
//            return;
//        }
//
//        if (!tvSfzhm.getText().toString().equals(idCardInfoEntity.id)) {
//            mView.getErrorNews("身份证号码与拍摄身份证身份证号码不符");
//            return;
//        }


        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()));//用户 ID

        builder.addFormDataPart("xm", tvHyxm.getText().toString());//身份证信息：姓名
        builder.addFormDataPart("haoma", tvSfzhm.getText().toString());//身份证信息：身份证号码

        //身份证正面
        if (idCardInfoEntity != null) {
            postParams.put("haoma", idCardInfoEntity.id);//身份证号码
            // 创建 RequestBody，用于封装构建RequestBody (身份证正面)
            RequestBody requestFileZm = RequestBody.create(MediaType.parse("image/jpeg"), new File(idCardInfoEntity.frontimage));
            MultipartBody.Part bodyZm = MultipartBody.Part.createFormData("sfzzm", idCardInfoEntity.frontimage, requestFileZm);
            builder.addPart(bodyZm);//身份证正面

            builder.addFormDataPart("xb", idCardInfoEntity.sex);//身份证信息：性别
            builder.addFormDataPart("minzu", idCardInfoEntity.nation);//身份证信息：民族
            builder.addFormDataPart("zhuzhi", idCardInfoEntity.address);//身份证信息：住址
        }


        //身份证正面
        if (idCardNInfoEntity != null) {
            // 创建 RequestBody，用于封装构建RequestBody (身份证背面)
            RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), new File(idCardNInfoEntity.backimage));
            MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("sfzbm", idCardNInfoEntity.backimage, requestFileFm);
            builder.addFormDataPart("qianfajiguan", idCardNInfoEntity.authority);//身份证信息：签发机关

            builder.addPart(bodyFm);//身份证反面
        }

        if (touxiang != null) {
            RequestBody requestFileTx = RequestBody.create(MediaType.parse("image/jpeg"), touxiang);
            MultipartBody.Part bodyTx = MultipartBody.Part.createFormData("touxiang", touxiang.getPath(), requestFileTx);
            builder.addPart(bodyTx);//头像
        }

        if (mAddRegisterEntity != null) {

            builder.addFormDataPart("sjhm", mAddRegisterEntity.getSjhm());//手机号码
            builder.addFormDataPart("ph", mAddRegisterEntity.getPh());//棚号
            builder.addFormDataPart("gsmc", mAddRegisterEntity.getGsmc());//鸽舍名称
            builder.addFormDataPart("gsdz", mAddRegisterEntity.getGsdz());//鸽舍地址
            builder.addFormDataPart("gsjd", mAddRegisterEntity.getGsjd());//鸽舍经度
            builder.addFormDataPart("gswd", mAddRegisterEntity.getGswd());//鸽舍纬度
            builder.addFormDataPart("csbh", mAddRegisterEntity.getCsbh());//参赛卡号
            builder.addFormDataPart("rhsj", mAddRegisterEntity.getRhsj());//入会时间
        }

        RequestBody requestBody = builder.build();

        Log.d("MenberImpls", "getServiceLY_XH: 3");
        mDao.getAddHyService(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d("MenberImpls", "getServiceLY_XH: 7");
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("MenberImpls", "getServiceLY_XH: 5");
                mView.getServiceResults(null, null, throwable);
            }
        };
    }

    //6、获取历次赛绩
    public void getXHHYGL_SJ_GetList(String mid, int pi, int ps,String xhuid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）

        mDao.getXHHYGL_SJ_GetListService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXHHYGL_SJData = new IBaseDao.GetServerData<List<HistoryLeagueEntity>>() {
            @Override
            public void getdata(ApiResponse<List<HistoryLeagueEntity>> listApiResponse) {
                mView.getXHHYGL_SJ_GetList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getXHHYGL_SJ_GetList(null, null, throwable);
            }
        };
    }

    //6、导入历次赛绩
    public void getXHHYGL_SJ_GetImport(String mid) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID

        mDao.getXHHYGL_SJ_Import(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getXHHYGL_SJData = new IBaseDao.GetServerData<List<HistoryLeagueEntity>>() {
            @Override
            public void getdata(ApiResponse<List<HistoryLeagueEntity>> listApiResponse) {
                mView.getXHHYGL_SJ_GetImport(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getXHHYGL_SJ_GetImport(null, null, throwable);
            }
        };
    }


    //添加历次赛绩
    public void getXHHYGL_SJ_Add(String mid, String zhhm, String xmmc, String bsmc, String bsrq, String bsgm) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("zhhm", zhhm);//足环号
        postParams.put("xmmc", xmmc);//项目名称
        postParams.put("bsmc", bsmc);//比赛名次，数字类型
        postParams.put("bsrq", bsrq);//比赛日期，日期类型
        postParams.put("bsgm", bsgm);//比赛规模，数字类型

        mDao.getXHHYGL_SJ_Add(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }

    //删除历次赛绩
    public void getXHHYGL_SJ_Del(String mid, String rid) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("rid", rid);//信鸽协会会员索引ID

        mDao.getXHHYGL_SJ_Del(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }


    //编辑历次赛绩
    public void getXHHYGL_SJ_Edit(String mid, String rid, String zhhm, String xmmc, String bsmc, String bsrq, String bsgm) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("rid", rid);//协会赛绩索引ID
        postParams.put("zhhm", zhhm);//足环号
        postParams.put("xmmc", xmmc);//项目名称
        postParams.put("bsmc", bsmc);//比赛名次，数字类型
        postParams.put("bsrq", bsrq);//比赛日期，日期类型
        postParams.put("bsgm", bsgm);//比赛规模，数字类型

        mDao.getXHHYGL_SJ_Edit(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }


    //获取年度评选
    public void getXHHYGL_NDPX_GetList(String mid, int pi, int ps,String xhuid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）

        mDao.getXHHYGL_NDPX_GetListService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getYearSelectionData = new IBaseDao.GetServerData<List<YearSelectionEntitiy>>() {
            @Override
            public void getdata(ApiResponse<List<YearSelectionEntitiy>> listApiResponse) {
                mView.getXHHYGL_NDPX_GetListData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getXHHYGL_NDPX_GetListData(null, null, throwable);
            }
        };
    }

    //添加年度评选
    public void getXHHYGL_NDPX_GetAdd(String mid, String y, String jg, String py) {


        if (y.isEmpty() || y.length() == 0) {
            mView.getErrorNews("请选择年份");
            return;
        }


        if (jg.isEmpty() || jg.length() == 0) {
            mView.getErrorNews("请选择评选结果");
            return;
        }


        if (py.isEmpty() || py.length() == 0) {
            mView.getErrorNews("请输入评语");
            return;
        }


        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("y", y);//评选年份
        postParams.put("jg", jg);//评选结果：最佳鸽棚|最佳裁判|最佳会员|最佳司放员
        postParams.put("py", py);//评语

        mDao.getXHHYGL_NDPX_GetAddService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }


    //编辑年度评选
    public void getXHHYGL_NDPX_GetEdit(String mid, String pid, String jg, String py) {


        if (jg.isEmpty() || jg.length() == 0) {
            mView.getErrorNews("请选择评选结果");
            return;
        }


        if (py.isEmpty() || py.length() == 0) {
            mView.getErrorNews("请输入评语");
            return;
        }


        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pid", pid);//索引ID
        postParams.put("jg", jg);//评选结果：最佳鸽棚|最佳裁判|最佳会员|最佳司放员
        postParams.put("py", py);//评语

        mDao.getXHHYGL_NDPX_GetEditService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }

    //获取年度缴费
    public void getYearPayCostList(String mid, int pi, int ps,String xhuid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）

        mDao.getYearPayCostService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getYearPayCostData = new IBaseDao.GetServerData<List<YearPayCostEntity>>() {
            @Override
            public void getdata(ApiResponse<List<YearPayCostEntity>> listApiResponse) {
                mView.getYearPayCost_ListData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getYearPayCost_ListData(null, null, throwable);
            }
        };
    }


    //添加年度缴费
    public void getYearPayCostAdd(String mid, String y, String zt) {


        if (y.isEmpty() || y.length() == 0) {
            mView.getErrorNews("请选择年份");
            return;
        }
        if (zt.isEmpty() || zt.length() == 0) {
            mView.getErrorNews("请选择状态");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("y", y);//年份
        postParams.put("zt", zt);//状态：已交费|未交费

        mDao.getYearPayCostAddService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d(TAG, "getdata: " + zt);
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }


    //修改年度缴费
    public void getYearPayCostEdit(String mid, String jid, String zt) {

        if (zt.isEmpty() || zt.length() == 0) {
            mView.getErrorNews("请选择状态");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("jid", jid);//索引ID
        postParams.put("zt", zt);//状态：已交费|未交费

        mDao.getYearPayCostEditService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getServiceHyData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d(TAG, "getdata: " + zt);
                mView.getServiceResults(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getServiceResults(null, null, throwable);
            }
        };
    }

    //获取足环购买列表
    public void getFootBuyEntityList(String mid, int pi, int ps,String xhuid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）

        mDao.getXHHYGL_ZHGM_GetListService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getFootBuyData = new IBaseDao.GetServerData<List<FootBuyEntity>>() {
            @Override
            public void getdata(ApiResponse<List<FootBuyEntity>> listApiResponse) {
                mView.getFootBuy_ListData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootBuy_ListData(null, null, throwable);
            }
        };
    }

    //添加足环购买
    public void getFootBuyEntityAdd(String mid, String t, String f) {

        if (t.isEmpty()) {
            mView.getErrorNews("请选择类型！");
            return;
        }

        if (f.isEmpty()) {
            mView.getErrorNews("请输入足环号码！");
            return;
        }

        String f1 = "", f2 = "";

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("t", t);//类型：散号足环|连号足环|其它足环（散号）|其它足环（连号）

        Log.d(TAG, "getFootBuyEntityAdd: 1--->" + t);
        Log.d(TAG, "getFootBuyEntityAdd: 2--->" + f);
        if (t.indexOf("散号") != -1) {

            if (f.indexOf(",") == -1) {
                postParams.put("f", f);//散号足环号码，没有为空
            } else {
                mView.getErrorNews("输入足环号码为连号足环，但选择的类型为散号");
                return;
            }
        } else {
            if (f.indexOf(",") == -1) {
                mView.getErrorNews("输入足环号码为散号足环，但选择的类型为连号");
                return;
            } else {

                String[] strs = f.split(",");
                f1 = strs[0];
                f2 = strs[1];

                postParams.put("f1", f1);//连号足环起始足环号码，没有为空
                postParams.put("f2", f2);//连号足环结束足环号码，没有为空
            }
        }

        mDao.getXHHYGL_ZHGM_GetAddService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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


    //编辑足环购买
    public void getFootBuyEntityEdit(String mid, String zid, String t, String f) {

        if (t.isEmpty()) {
            mView.getErrorNews("请选择类型！");
            return;
        }

        if (f.isEmpty()) {
            mView.getErrorNews("请输入足环号码！");
            return;
        }

        String f1 = "", f2 = "";

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("zid", zid);//信鸽协会会员索引ID
        postParams.put("t", t);//类型：散号足环|连号足环|其它足环（散号）|其它足环（连号）

        if (t.indexOf("散号") != -1) {

            if (f.indexOf(",") == -1) {
                postParams.put("f", f);//散号足环号码，没有为空
            } else {
                mView.getErrorNews("输入足环号码为连号足环，但选择的类型为散号");
                return;
            }
        } else {
            if (f.indexOf(",") == -1) {
                mView.getErrorNews("输入足环号码为散号足环，但选择的类型为连号");
                return;
            } else {

                String[] strs = f.split(",");
                f1 = strs[0];
                f2 = strs[1];

                postParams.put("f1", f1);//连号足环起始足环号码，没有为空
                postParams.put("f2", f2);//连号足环结束足环号码，没有为空
            }
        }

        mDao.getXHHYGL_ZHGM_GetEditService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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

    //删除足环购买
    public void getFootBuyEntityDel(String mid, String zid) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("zid", zid);//信鸽协会会员索引ID

        mDao.getXHHYGL_ZHGM_GetDelService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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

    //获取处罚记录列表
    public void getPenalizeRecordList(String mid, int pi, int ps,String xhuid, String type) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据

        if (type.equals("look")){
            postParams.put("uid", xhuid);//用户id
        }else {
            postParams.put("uid", AssociationData.getUserId());//用户id
        }
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pi", pi);//页码
        postParams.put("ps", ps);//每页显示条数（默认显示20条）

        mDao.getXHHYGL_CFJL_GetListService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getPenalizeRecordData = new IBaseDao.GetServerData<List<PenalizeRecordEntity>>() {
            @Override
            public void getdata(ApiResponse<List<PenalizeRecordEntity>> listApiResponse) {
                mView.getPenalizeRecord_ListData(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getPenalizeRecord_ListData(null, null, throwable);
            }
        };
    }


    //添加处罚记录
    public void getPenalizeRecordAdd(String mid, String sj, String jg, String yy) {

        if (sj.isEmpty() || sj.length() == 0) {
            mView.getErrorNews("请选择处罚时间");
            return;
        }
        if (jg.isEmpty() || jg.length() == 0) {
            mView.getErrorNews("请选择处罚结果");
            return;
        }

        if (yy.isEmpty() || yy.length() == 0) {
            mView.getErrorNews("请填写处罚原因");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("sj", sj);//处罚时间。格式：2018-03-03
        postParams.put("jg", jg);//处罚结果：警告|严重警告|记过|禁赛一年|开除会籍
        postParams.put("yy", yy);//处罚原因

        mDao.getXHHYGL_CFJL_GetAddService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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

    //编辑处罚记录
    public void getPenalizeRecordEdit(String mid, String id, String sj, String jg, String yy, String cxsj, String cxyy) {

        if (sj.isEmpty() || sj.length() == 0) {
            mView.getErrorNews("请选择处罚时间");
            return;
        }
        if (jg.isEmpty() || jg.length() == 0) {
            mView.getErrorNews("请选择处罚结果");
            return;
        }

        if (yy.isEmpty() || yy.length() == 0) {
            mView.getErrorNews("请填写处罚原因");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("id", id);//索引ID
        postParams.put("sj", sj);//处罚时间。格式：2018-03-03
        postParams.put("jg", jg);//处罚结果：警告|严重警告|记过|禁赛一年|开除会籍
        postParams.put("yy", yy);//处罚原因
        postParams.put("cxsj", cxsj);//撤销时间
        postParams.put("cxyy", cxyy);//撤销原因

        mDao.getXHHYGL_CFJL_GetEditService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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


    /**
     * 训鸽通申请试用
     */
    public void uploadIdCardHyInfo(String mid, IdCardPInfoEntity idCardInfoEntity, IdCardNInfoEntity idCardNInfoEntity) {

        if (idCardInfoEntity == null) {
            mView.getErrorNews("请拍摄身份证正面");
            return;
        }

        if (idCardNInfoEntity == null) {
            mView.getErrorNews("请拍摄身份证背面");
            return;
        }


        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//协会会员索引id
        postParams.put("xingming", idCardInfoEntity.name);//身份证信息：姓名
        postParams.put("xingbie", idCardInfoEntity.sex);//身份证信息：性别
        postParams.put("minzu", idCardInfoEntity.nation);//身份证信息：民族
        postParams.put("zhuzhi", idCardInfoEntity.address);//身份证信息：住址
        postParams.put("haoma", idCardInfoEntity.id);//身份证信息：身份证号码
        postParams.put("qianfajiguan", idCardNInfoEntity.authority);//身份证信息：签发机关
        postParams.put("youxiaoqi", idCardNInfoEntity.valid_date);//身份证信息：有效期


        // 创建 RequestBody，用于封装构建RequestBody (身份证正面)
        RequestBody requestFileZm = RequestBody.create(MediaType.parse("image/jpeg"), new File(idCardInfoEntity.frontimage));
        MultipartBody.Part bodyZm = MultipartBody.Part.createFormData("sfzzm", idCardInfoEntity.frontimage, requestFileZm);

        // 创建 RequestBody，用于封装构建RequestBody (身份证背面)
        RequestBody requestFileFm = RequestBody.create(MediaType.parse("image/jpeg"), new File(idCardNInfoEntity.backimage));
        MultipartBody.Part bodyFm = MultipartBody.Part.createFormData("sfzbm", idCardNInfoEntity.backimage, requestFileFm);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("mid", mid)//协会会员索引id
                .addFormDataPart("xingming", idCardInfoEntity.name)//身份证信息：姓名
                .addFormDataPart("xingbie", idCardInfoEntity.sex)//身份证信息：性别
                .addFormDataPart("minzu", idCardInfoEntity.nation)//身份证信息：民族
                .addFormDataPart("zhuzhi", idCardInfoEntity.address)//身份证信息：住址
                .addFormDataPart("haoma", idCardInfoEntity.id)//身份证信息：身份证号码
                .addFormDataPart("qianfajiguan", idCardNInfoEntity.authority)//身份证信息：签发机关
                .addFormDataPart("youxiaoqi", idCardNInfoEntity.valid_date)//身份证信息：有效期
                .addPart(bodyZm)//身份证正面
                .addPart(bodyFm);//身份证反面面

        RequestBody requestBody = builder.build();

        mDao.uploadIdCardHyInfoService(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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


    //修改鸽舍资料
    public void setXHHYGL_GSZL_Edit(String mid, String pn, String gsmc, String gsdz, String gsjdwd, String zcsj, String csbh) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("pn", pn);//棚号
        postParams.put("gsmc", gsmc);//鸽舍名称，可为空
        postParams.put("gsdz", gsdz);//鸽舍地址，可为空


        gsjdwd = gsjdwd.replace("东经：", "");
        gsjdwd = gsjdwd.replace("北纬：", "");


        if (gsjdwd.length() == 0) {
            postParams.put("gsjd", "");//鸽舍经度
            postParams.put("gswd", "");//鸽舍纬度
        } else {
            postParams.put("gsjd", gsjdwd.split(" ")[0]);//鸽舍经度
            postParams.put("gswd", gsjdwd.split(" ")[1]);//鸽舍纬度
        }

        postParams.put("zcsj", zcsj);//注册入会时间
        postParams.put("csbh", csbh);//参赛编号

        mDao.getXHHYGL_GSZL_EditService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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


    //3、修改基本信息
    public void setXHHYGL_JBXX_Edit(String mid, String xm, String sex, String sr, String zt, String sjhm) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID
        postParams.put("xm", xm);//会员姓名
        postParams.put("sex", sex);//会员性别
        postParams.put("sr", sr);//会员出生年月
        postParams.put("wh", "");//会员文化程度
        postParams.put("zy", "");//会员职业
        postParams.put("sjhm", sjhm);//会员手机号码
        postParams.put("zt", zt);//会员状态：在册|禁赛|除名

        mDao.getXHHYGL_JBXX_Edit(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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

    //3、修改会员头像
    public void setUpdateUserFaceImage(String mid, String uface) {

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("mid", mid);//信鸽协会会员索引ID


        // 创建 RequestBody，用于封装构建RequestBody (头像)
        RequestBody requestFileZm = RequestBody.create(MediaType.parse("image/jpeg"), new File(uface));
        MultipartBody.Part bodyTx = MultipartBody.Part.createFormData("uface", uface, requestFileZm);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("mid", mid)//协会会员索引id
                .addPart(bodyTx);//身份证反面面

        RequestBody requestBody = builder.build();

        mDao.updateUserFaceImageService(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
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

}
