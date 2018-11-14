package com.cpigeon.cpigeonhelper.modular.saigetong.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GeZhuFootEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.LocationEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTFootInfoEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTUserInfo;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SearchFootEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.daoimpl.SGTImpl;
import com.cpigeon.cpigeonhelper.modular.saigetong.view.viewdao.SGTView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 赛格通控制层
 * Created by Administrator on 2017/12/1.
 */
public class SGTPresenter extends BasePresenter<SGTView, SGTImpl> {

    private Map<String, Object> postParams = new HashMap<>();
    private long timestamp;

    public static int uids = -1;
    public static boolean isAuth = false;

    public SGTPresenter(SGTView mView) {
        super(mView);
    }

    @Override
    protected SGTImpl initDao() {
        return new SGTImpl();
    }

    /**
     * 获取赛格通主页数据
     * p  页数
     */
    public void getSGTGeZu(int p) {

        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据

        postParams.put("uid", String.valueOf(uids));
        postParams.put("p", p);
        mDao.getSGTGeZuData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getSGTHomeListEntity = new IBaseDao.GetServerData<List<SGTHomeListEntity>>() {
            @Override
            public void getdata(ApiResponse<List<SGTHomeListEntity>> listApiResponse) {

                Log.d("gpsgsjqu", "getdata 1: " + AssociationData.getUserId());
                Log.d("gpsgsjqu", "getdata 1: " + uids);
                Log.d("gpsgsjqu", "getdata 1: " + listApiResponse.toJsonString());
//
//                if (listApiResponse.getData() != null) {
//                    Log.d(TAG, "获取赛格通主页数据: msg--》" + listApiResponse.getMsg() + "   照片数量---》" + listApiResponse.getData().size() + "   错误码：" + listApiResponse.getErrorCode());
//                } else {
//                    Log.d(TAG, "获取赛格通主页数据: msg--》" + listApiResponse.getMsg() + "   错误码：" + listApiResponse.getErrorCode());
//                }

                Log.d("sggp", "getdata: " + listApiResponse.toJsonString());

                mView.getSGTHomeData(listApiResponse, listApiResponse.getMsg(), null);
//                switch (listApiResponse.getErrorCode()) {
//                    case 0:
//
//                        break;
//                    default:
//                        mView.getErrorNews("获取数据失败：" + listApiResponse.getMsg());
//                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                Log.d("sggp", "getdata异常: " + throwable.getLocalizedMessage());
                mView.getSGTHomeData(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取赛格通主页数据
     * p  页数
     */
    public void getGetFootSS(int p, String searchStr) {

//        if (searchStr.isEmpty()) {
////            mView.getErrorNews("输入内容不能为空，请重新输入");
//            return;
//        }
        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("p", p);
        postParams.put("s", searchStr);//搜索足环号码（支持模糊搜索）
        mDao.getSGTFootSSData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getFootSSEntity = new IBaseDao.GetServerData<List<FootSSEntity>>() {
            @Override
            public void getdata(ApiResponse<List<FootSSEntity>> listApiResponse) {

                Log.d("xiaohlss", "getdata: " + listApiResponse.toJsonString());
                mView.getSGTSearchData(listApiResponse, listApiResponse.getMsg(), null);

//                switch (listApiResponse.getErrorCode()) {
//                    case 0:
//                        mView.getSGTSearchData(listApiResponse.getData(), "");
//                        break;
//                    default:
//                        mView.getErrorNews(listApiResponse.getMsg());
//                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getSGTSearchData(null, null, throwable);
//                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 获取足环照片
     */
    public void getFootImg(String f, Context context) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据

        postParams.put("uid", String.valueOf(uids));
//        postParams.put("f", f);//足环号码
        postParams.put("id", f);//足环记录索引ID

        mDao.getZHImgData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getSGTImgEntity = new IBaseDao.GetServerData<List<SGTImgEntity>>() {
            @Override
            public void getdata(ApiResponse<List<SGTImgEntity>> listApiResponse) {
                Log.d("gpsgsjqu", "getdata: " + AssociationData.getUserId());
                Log.d("gpsgsjqu", "getdata: " + uids);
                Log.d("gpsgsjqu", "getdata: " + listApiResponse.toJsonString());
                mView.getFootImg(listApiResponse.getData(), "");
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getFootImg(null, throwable.getLocalizedMessage());
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取足环详情
     */
    public void getFootInfo(String id) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("id", id);//足环索引ID

        mDao.getSGTFootInfoData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getSGTFootInfoEntity = new IBaseDao.GetServerData<SGTFootInfoEntity>() {
            @Override
            public void getdata(ApiResponse<SGTFootInfoEntity> sgtFootInfoEntityApiResponse) {
                Log.d(TAG, "获取足环详情: " + sgtFootInfoEntityApiResponse.getErrorCode() + "   msg-->" + sgtFootInfoEntityApiResponse.getMsg());
                switch (sgtFootInfoEntityApiResponse.getErrorCode()) {
                    case 0:
                        mView.getFootInfo(sgtFootInfoEntityApiResponse.getData(), "");
                        break;
                    default:
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 获取鸽主的鸽子照片
     */
    public void getSGTImagesGeZhu(String c) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("c", c);//足环索引ID
        mDao.getSGTImagesGeZhuData(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getGZImgEntity = new IBaseDao.GetServerData<List<GZImgEntity>>() {
            @Override
            public void getdata(ApiResponse<List<GZImgEntity>> listApiResponse) {
                Log.d("SGTHomeListAdapter", "获取鸽主信息: " + listApiResponse.getErrorCode() + "   msg---》" + listApiResponse.getMsg());
                switch (listApiResponse.getErrorCode()) {
                    case 0:
                        mView.getGZImgEntity(listApiResponse.getData(), "");
                        break;
                    default:
                        mView.getGZImgEntity(null, listApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getGZImgEntity(null, throwable.getLocalizedMessage());
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 上传足环照片
     *
     * @param tagid
     * @param dwData
     * @param filePath
     * @param tag      1  第一次上传， 2 继续上传覆盖
     */
    public void uploadFootImg(String id, int tagid, LocationEntity dwData, String filePath, int tag, @Nullable String reason) {

        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
//        postParams.put("uid", String.valueOf(21819));

        postParams.put("id", id);//
        postParams.put("tagid", tagid);//标签ID
        postParams.put("lo", dwData.getLo());//经度
        postParams.put("la", dwData.getLa());//纬度
        postParams.put("we", dwData.getWe());//天气
        postParams.put("t", dwData.getT());//温度
        postParams.put("wd", dwData.getWd());//风向

        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath));

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filePath, requestFile);


        MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);


        mBuilder.addFormDataPart("id", id);//SGT_UploadFootImage接口，把参数foot改为id，参数为足环号码索引ID
        mBuilder.addFormDataPart("uid", String.valueOf(uids))//用户 ID
                .addFormDataPart("tagid", String.valueOf(tagid))//标签 ID
                .addFormDataPart("lo", dwData.getLo())//经度
                .addFormDataPart("la", dwData.getLa())//维度
                .addFormDataPart("we", dwData.getWe())//天气名称
                .addFormDataPart("t", dwData.getT())//温度
                .addFormDataPart("wd", dwData.getWd())//风向
                .addFormDataPart("bp", StringValid.isStringValid(reason) ? "y" : "")//是否补拍
                .addFormDataPart("bpyy", StringValid.isStringValid(reason) ? reason : "")//补拍原因
                .addPart(body);//图片、视频【文件表单】


        RequestBody requestBody = mBuilder.build();

        if (tag == 1) {
            //第一次上传
            mDao.uploadFootImg(AssociationData.getUserToken()//通行验证
                    , requestBody//请求体
                    , timestamp//时间戳
                    , CommonUitls.getApiSign(timestamp, postParams));//签名
        } else if (tag == 2) {
            //覆盖上传图片
            mDao.uploadFootImg_fg(AssociationData.getUserToken()//通行验证
                    , requestBody//请求体
                    , timestamp//时间戳
                    , CommonUitls.getApiSign(timestamp, postParams));//签名
        }


        mDao.getUploadFootImg = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dateApiResponse) {
                mView.uploadResults(dateApiResponse, dateApiResponse.getMsg());//上传结果回调
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    //上传鸽主足环照片
    public void uploadGZFootImg(String ids, int tagid, LocationEntity dwData, String filePath, String strSelectFoot) {


        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
//        postParams.put("uid", String.valueOf(21819));
        postParams.put("ids", ids);//足环号码
        postParams.put("tagid", tagid);//标签ID
        postParams.put("zhhm", strSelectFoot);//zhhm：足环号码，多个足环号码使用英文逗号分隔。示例：2018-22-1234567,2018-22-22222

        postParams.put("lo", dwData.getLo());//经度
        postParams.put("la", dwData.getLa());//纬度
        postParams.put("we", dwData.getWe());//天气
        postParams.put("t", dwData.getT());//温度
        postParams.put("wd", dwData.getWd());//风向


        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath));

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filePath, requestFile);


        MultipartBody.Builder mBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(uids))//用户 ID
                .addFormDataPart("ids", ids)//足环号码
                .addFormDataPart("tagid", String.valueOf(tagid))//标签 ID
                .addFormDataPart("lo", dwData.getLo())//经度
                .addFormDataPart("la", dwData.getLa())//纬度
                .addFormDataPart("we", dwData.getWe())//天气名称
                .addFormDataPart("t", dwData.getT())//温度
                .addFormDataPart("wd", dwData.getWd())//风向
                .addPart(body);//图片、视频【文件表单】

        if (strSelectFoot != null) {
            mBuilder.addFormDataPart("zhhm", strSelectFoot);//zhhm：足环号码，多个足环号码使用英文逗号分隔。示例：2018-22-1234567,2018-22-22222
        }

        RequestBody requestBody = mBuilder.build();

        mDao.uploadGZFootImg(AssociationData.getUserToken()//通行验证
                , requestBody//请求体
                , timestamp//时间戳
                , CommonUitls.getApiSign(timestamp, postParams));//签名

        mDao.getUploadFootImg = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dateApiResponse) {
                mView.uploadResults(dateApiResponse, dateApiResponse.getMsg());//上传结果回调
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //获取赛鸽通标签
    public void getSGTTag(String id) {

        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("id", id);//足环信息ID

        mDao.getSTGTag(AssociationData.getUserToken()//通行验证
                , postParams//请求体
                , timestamp//时间戳
                , CommonUitls.getApiSign(timestamp, postParams));//签名
        mDao.getTagEntitiy = new IBaseDao.GetServerData<List<TagEntitiy>>() {
            @Override
            public void getdata(ApiResponse<List<TagEntitiy>> listApiResponse) {
                switch (listApiResponse.getErrorCode()) {
                    case 0://请求成功
                        if (listApiResponse.getData().size() > 0) {
                            mView.getTagData(listApiResponse.getData());
                        }
                        break;
                    default:
                        mView.getErrorNews("获取标签失败：" + listApiResponse.getErrorCode() + "  " + listApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    //获取鸽主下的足环列表
    public void getFootList_SGT(int id, String s, int tagid) {

        if (id == -1) {
            mView.getGeZhuFootData(null, "当前鸽主不存在");
            return;
        }

        if (tagid == -1) {
            mView.getGeZhuFootData(null, "当前鸽主不存在");
            return;
        }


        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("id", id);//足环信息ID
        postParams.put("tagid", tagid);//足环信息ID
        postParams.put("s", s);//搜索足环号码

        mDao.getFootListData_SGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getGeZhuFootData = new IBaseDao.GetServerData<GeZhuFootEntity>() {
            @Override
            public void getdata(ApiResponse<GeZhuFootEntity> listApiResponse) {
                mView.getGeZhuFootData(listApiResponse, listApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 获取赛鸽通用户信息
     */
    public void getUserInfo_SGT() {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));

        mDao.getUserInfo_SGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.getInfo_SGT = new IBaseDao.GetServerData<SGTUserInfo>() {

            @Override
            public void getdata(ApiResponse<SGTUserInfo> dataApiResponse) {
                mView.getUserInfo_SGT(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 设置公棚可容羽数
     */
    public void setGPKrys(String krys) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("krys", krys);//可容羽数，数字。

        mDao.settingKrys_SGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getUploadFootImg = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> sgtUserInfoApiResponse) {
                mView.getSetGPKrysResults(sgtUserInfoApiResponse, sgtUserInfoApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 赛鸽通设置本届比赛入棚时间
     */
    public void setRpTime(String rpsj) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("rpsj", rpsj);//入棚时间，格式：2018-01-01

        mDao.getRpTime_SGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getUploadFootImg = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getSetRpTimeResults(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 修改足环号码
     */
    public void editSGTFoot(String id, String f) {
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("id", id);//足环索引ID
        postParams.put("f", f);//足环号码

        mDao.editFoot_SGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getUploadFootImg = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getSetRpTimeResults(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 申请试用 公棚赛鸽
     */
    public void applyTrial_SGT(String time, String krys) {

        if (time == null) {
            mView.getErrorNews("请设置入棚开始时间");
            return;
        }

        if (krys == null || krys.length() == 0) {
            mView.getErrorNews("请设置可容羽数");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(AssociationData.getUserId()));
        postParams.put("sj", time);//本届比赛入棚开始时间，年月日。格式：2018-01-01
        postParams.put("rl", krys);//公棚容量羽数

        mDao.applyTrial_SGT(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getUploadFootImg = new IBaseDao.GetServerData<Object>() {

            @Override
            public void getdata(ApiResponse<Object> dataApiResponse) {
                mView.getSetRpTimeResults(dataApiResponse, dataApiResponse.getMsg());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

    /**
     * 搜索足环号码
     * 说明：根据选择的标签，搜索足环号码。
     */
    public static ApiResponse<SearchFootEntity> searchFootInfoData;

    public static boolean footTag = true;//第一次  没有选择左右足环

    public void getSearchFootInfo(String foot, int tag) {
        searchFootInfoData = null;
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("foot", foot);
        postParams.put("tag", tag);
        mDao.getSearchFootInfoService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getSearchFootEntity = new IBaseDao.GetServerData<SearchFootEntity>() {
            @Override
            public void getdata(ApiResponse<SearchFootEntity> dataApiResponse) {
                mView.getSearchFootInfoData(dataApiResponse, dataApiResponse.getMsg(), null);
                SGTPresenter.searchFootInfoData = dataApiResponse;
                Log.d("gpsgser", "getdata: " + dataApiResponse.toJsonString());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getSearchFootInfoData(null, null, throwable);
            }
        };
    }

    //    重新补拍足环搜索
    public void getSearchBuPaiFootInfo(String foot) {
        searchFootInfoData = null;
        timestamp = System.currentTimeMillis() / 1000;//时间戳
        postParams.clear();//清空之前集合中保存的数据
        postParams.put("uid", String.valueOf(uids));
        postParams.put("foot", foot);

        mDao.getSearchBuPaiFootInfoService(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getSearchFootEntity = new IBaseDao.GetServerData<SearchFootEntity>() {
            @Override
            public void getdata(ApiResponse<SearchFootEntity> dataApiResponse) {
                mView.getSearchFootInfoData(dataApiResponse, dataApiResponse.getMsg(), null);
                SGTPresenter.searchFootInfoData = dataApiResponse;
                Log.d("gpsgser", "getdata: " + dataApiResponse.toJsonString());
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getSearchFootInfoData(null, null, throwable);
            }
        };
    }
}
