package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter;

import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildMemberEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.IssueFoot;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PieChartFootEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.impldao.FootAdminImpl;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/6/15.
 */

public class FootAdminPresenter extends BasePresenter<FootAdminViewImpl, FootAdminImpl> {


    public FootAdminPresenter(FootAdminViewImpl mView) {
        super(mView);
    }

    @Override
    protected FootAdminImpl initDao() {
        return new FootAdminImpl();
    }

    //1、获取协会会员列表
    public void getXHHYGL_ZHGL_GetList(String y, String t, String s, int pi, int ps, String j, String f) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        postParams.put("y", y);//年份
        postParams.put("j", j);//是否缴费：0为未缴费，1为已交费，2为全部
        postParams.put("s", s);//是否出售：0为未售出，1为已出售  2全部
        postParams.put("t", t);//每页显示条数（默认显示20条）
        postParams.put("pi", pi);//每页显示条数（默认显示20条）
        postParams.put("ps", ps);//每页显示条数（默认显示20条）
        postParams.put("f", f);//搜索

        mDao.getXHHYGL_ZHGL_GetData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getFootAdminListEntityData = new IBaseDao.GetServerData<FootAdminListDataEntity>() {
            @Override
            public void getdata(ApiResponse<FootAdminListDataEntity> listApiResponse) {
                mView.getFootAdminList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootAdminList(null, null, throwable);
            }
        };
    }

    //获取足环详细
    public void getXHHYGL_ZHGL_GetDetail(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//索引ID

        mDao.getXHHYGL_ZHGL_GetDetail(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getFootAdminData = new IBaseDao.GetServerData<FootAdminEntity>() {
            @Override
            public void getdata(ApiResponse<FootAdminEntity> footAdminEntityApiResponse) {
                mView.getFootAdminDetailsData(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };

    }

    //足环修改
    public void getXHHYGL_ZHGL_GetEdit(String id, String lx, String gzxm, String gzid, String jf, String dsd) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//索引ID
        postParams.put("lx", lx);//索引ID
        postParams.put("gzxm", gzxm);//鸽主姓名
        postParams.put("gzid", gzid);//鸽主ID（如果鸽主是协会会员，否则为0）
        postParams.put("jf", jf);//是否交费：0为未交费|1为已交费
        postParams.put("dsd", dsd);//代售点索引ID

        mDao.getXHHYGL_ZHGL_GetEdit(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getFootAdminResultsData2(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootAdminResultsData2(null, null, throwable);
            }
        };

    }


    //删除足环
    public void getXHHYGL_ZHGL_GetDel(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//索引ID

        mDao.getXHHYGL_ZHGL_GetDel(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getFootAdminResultsData(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootAdminResultsData(null, null, throwable);
            }
        };
    }

    //足环录入
    public void getXHHYGL_ZHGL_Add(String lx, int t, String f, String f1, String f2) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("lx", lx);//用户id
        postParams.put("t", t);//类型：t=1为散号|t=2为连号
        postParams.put("f", f);//散号足环
        postParams.put("f1", f1);//连号足环开始足环号码
        postParams.put("f2", f2);//连号足环结束足环号码

        mDao.getXHHYGL_ZHGL_Add(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getFootAdminResultsData(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootAdminResultsData(null, null, throwable);
            }
        };
    }

    //获取上级鸽会发行足环
    public void getXHHYGL_ZHGL_GetFoot() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getXHHYGL_ZHGL_GetFoot(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getIssueFootData = new IBaseDao.GetServerData<IssueFoot>() {
            @Override
            public void getdata(ApiResponse<IssueFoot> footAdminEntityApiResponse) {
                mView.getIssueFootsData(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //导入上级鸽会发行足环
    public void getXHHYGL_ZHGL_ImportFoot(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//索引ID，传递给确定导入接口

        mDao.getXHHYGL_ZHGL_ImportFoot(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_ImportFootResult(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //特比环图标统计
    public void getXHHYGL_ZHGL_GetTotal(String y) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);//年份

        mDao.getXHHYGL_ZHGL_GetTotal(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getPieChartFootData = new IBaseDao.GetServerData<PieChartFootEntity>() {
            @Override
            public void getdata(ApiResponse<PieChartFootEntity> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_GetTotal(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //获取足环单价
    public void getXHHYGL_ZHGL_GetFootPrice(String y, String t) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);//年份
        postParams.put("t", t);//类型：t=0为普通足环，t=1为特比环

        mDao.getXHHYGL_ZHGL_GetFootPrice(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getFootPriceResultsData = new IBaseDao.GetServerData<FootPriceEntity>() {
            @Override
            public void getdata(ApiResponse<FootPriceEntity> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_GetFootPrice(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //设置足环单价
    public void getXHHYGL_ZHGL_SetFootPrice(String y, String t, String p) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);//年份
        postParams.put("t", t);//类型：t=0为普通足环，t=1为特比环
        postParams.put("p", p);//价格

        mDao.getXHHYGL_ZHGL_SetFootPrice(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_SetFootPrice(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //代售点 x修改
    public void getAgentTakePlace_Modify(String id, String d, String lxr, String tel, String dz) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//索引ID
        postParams.put("d", d);//代售点名称
        postParams.put("lxr", lxr);//联系人姓名
        postParams.put("tel", tel);//联系电话
        postParams.put("dz", dz);//联系地址

        mDao.getAgentTakePlace_Ser_Modify(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getAgentTakePlaceModify(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //代售点 添加
    public void getAgentTakePlace_Add(String d, String lxr, String tel, String dz) {


        if (d.isEmpty()){
            mView.getErrorNews("请填写代收点名称");
            return;
        }

        if (lxr.isEmpty()){
            mView.getErrorNews("请填写联系人姓名");
            return;
        }

        if (tel.isEmpty()){
            mView.getErrorNews("请填写联系电话");
            return;
        }

        if (dz.isEmpty()){
            mView.getErrorNews("请填写联系地址");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("d", d);//代售点名称
        postParams.put("lxr", lxr);//联系人姓名
        postParams.put("tel", tel);//联系电话
        postParams.put("dz", dz);//联系地址

        mDao.getAgentTakePlace_Ser_Add(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_SetFootPrice(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //代售点 删除
    public void getAgentTakePlace_Del(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);//索引id

        mDao.getAgentTakePlace_Ser_Del(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_SetFootPrice(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //代售点 列表
    public void getAgentTakePlace_list(String y) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getAgentTakePlace_Ser_list(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getAgentTakePlaceListData = new IBaseDao.GetServerData<List<AgentTakePlaceListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<AgentTakePlaceListEntity>> footAdminEntityApiResponse) {
                mView.getAgentTakePlaceList(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getAgentTakePlaceList(null, null, throwable);
            }
        };
    }

    //代售点 详情
    public void getAgentTakePlace_details(String id) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);

        mDao.getAgentTakePlace_Ser_details(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getAgentTakePlaceData = new IBaseDao.GetServerData<AgentTakePlaceListEntity>() {
            @Override
            public void getdata(ApiResponse<AgentTakePlaceListEntity> footAdminEntityApiResponse) {
                mView.getAgentTakePlaceDetails(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getAgentTakePlaceDetails(null, null, throwable);
            }
        };
    }

    //代售点 特比环验鸽
    public void getXHHYGL_ZHGL_TBHYG(String id, String ft, String fileURL) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("id", id);
        postParams.put("ft", ft);

        RequestBody requestFileZm = null;
        if (ft.equals("image")) {
            // 创建 RequestBody，用于封装构建RequestBody
            requestFileZm = RequestBody.create(MediaType.parse("image/jpeg"), new File(fileURL));

        } else if (ft.equals("video")) {
            requestFileZm = RequestBody.create(MediaType.parse("video/mp4"), new File(fileURL));
        }

        MultipartBody.Part bodyTx = MultipartBody.Part.createFormData("file", fileURL, requestFileZm);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))//用户 ID
                .addFormDataPart("id", id)//足环索引ID
                .addFormDataPart("ft", ft)//类型
                .addPart(bodyTx);

        RequestBody requestBody = builder.build();

        mDao.getXHHYGL_ZHGL_TBHYGData(AssociationData.getUserToken(), requestBody, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                Log.d(TAG, "getdata: " + dataApiResponse.toJsonString());
                mView.getFootAdminResultsData(dataApiResponse, dataApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //下级协会  获取足环列表
    public void getXHHYGL_SJGH_GetFootList(String y, int pi, int ps,String s) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);
        postParams.put("pi", pi);
        postParams.put("ps", ps);
        postParams.put("s", s);

        mDao.getXHHYGL_SJGH_GetFootListData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getXHHYGL_SJGH_GetFootListData = new IBaseDao.GetServerData<ChildFoodAdminListEntity>() {
            @Override
            public void getdata(ApiResponse<ChildFoodAdminListEntity> footAdminEntityApiResponse) {
                mView.getXHHYGL_SJGH_GetFootList(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getXHHYGL_SJGH_GetFootList(null, null, throwable);
            }
        };
    }

    //下级协会 足环录入
    public void getXHHYGL_SJGH_AddFoot(String lx, String xhuid, String xhid, String xhmc, String f1, String f2) {

        if (f1.isEmpty()||f2.isEmpty()){
            mView.getErrorNews("请输入足环号码");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("lx", lx);//类型：普通足环|特比环
        postParams.put("xhuid", xhuid);//协会用户ID
        postParams.put("xhid", xhid);//协会ID
        postParams.put("xhmc", xhmc);//协会名称
        postParams.put("f1", f1);//连号足环开始足环号码
        postParams.put("f2", f2);//连号足环结束足环号码

        mDao.getXHHYGL_SJGH_AddFoot(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getFootAdminResultsData(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootAdminResultsData(null, null, throwable);
            }
        };
    }

    //下级协会  获取协会会员列表
    public void getAssociationList() {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id

        mDao.getMemberListData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getMemberListData = new IBaseDao.GetServerData<List<ChildMemberEntity>>() {
            @Override
            public void getdata(ApiResponse<List<ChildMemberEntity>> listApiResponse) {
                mView.getChildMemberList(listApiResponse, listApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getChildMemberList(null, null, throwable);
            }
        };
    }


    //下级协会  获取足环单价
    public void getXHHYGL_ZHGL_GetFootPrice_Child(String y, String t) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);//年份
        postParams.put("t", t);//类型：t=0为普通足环，t=1为特比环，t=2为发行普通足环，t=3为发行特比环

        mDao.getXHHYGL_ZHGL_GetFootPrice_Child(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getFootPriceResultsData = new IBaseDao.GetServerData<FootPriceEntity>() {
            @Override
            public void getdata(ApiResponse<FootPriceEntity> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_GetFootPrice(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //下级协会  设置足环单价
    public void getXHHYGL_ZHGL_SetFootPrice_Child(String y, String t, String p) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("y", y);//年份
        postParams.put("t", t);//类型：t=0为普通足环，t=1为特比环
        postParams.put("p", p);//价格

        mDao.getXHHYGL_ZHGL_SetFootPrice_Child(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getXHHYGL_ZHGL_SetFootPrice(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //下级协会 足环修改
    public void getXHHYGL_SJGH_EditFoot(String id, String lx, String xhuid, String xhid, String xhmc, String f1, String f2) {
        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清除集合中之前的数据
        postParams.put("uid", AssociationData.getUserId());//用户id
        postParams.put("lx", lx);//类型：普通足环|特比环
        postParams.put("id", id);//类型：普通足环|特比环
        postParams.put("xhuid", xhuid);//协会用户ID
        postParams.put("xhid", xhid);//协会ID
        postParams.put("xhmc", xhmc);//协会名称
        postParams.put("f1", f1);//连号足环开始足环号码
        postParams.put("f2", f2);//连号足环结束足环号码

        mDao.getXHHYGL_SJGH_EditFoot(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getResultsData = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> footAdminEntityApiResponse) {
                mView.getFootAdminResultsData(footAdminEntityApiResponse, footAdminEntityApiResponse.getMsg(), null);
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootAdminResultsData(null, null, throwable);
            }
        };
    }

}
