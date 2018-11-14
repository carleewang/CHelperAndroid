package com.cpigeon.cpigeonhelper.utils;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AddAuthEntity;
import com.cpigeon.cpigeonhelper.modular.authorise.model.bean.AuthHomeEntity;
import com.cpigeon.cpigeonhelper.modular.flyarea.model.bean.FlyingAreaEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTHomeEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTStatisticalEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTong;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ImgOrVideoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.KjLcInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.LocationInfoEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.RaceLocationEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.TagEntitiy;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.XGTOpenAndRenewEntity;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.DeviceBean;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.AppInfoEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.DiZhenEntity;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.HomeAd;
import com.cpigeon.cpigeonhelper.modular.home.model.bean.UllageToolEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.LogbookEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.DesignatedSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GP_GetChaZuEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetChaZuListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetJiangJinXianShiBiLiEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GpRpdxSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.SlSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.XsSmsSetEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlMessageEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlReviewEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.BsdxSettingEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.DtListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcItemEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.GcListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.serverxh.model.bean.PlayListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.AgentTakePlaceListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildMemberEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootAdminListDataEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootBuyEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HFInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HYGLInfoEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HistoryLeagueEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserIdInfo;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyglHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.IssueFoot;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PenalizeRecordEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PieChartFootEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearPayCostEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.YearSelectionEntitiy;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.InvoiceEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.Order;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.OrderList;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PackageInfo;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayRequest;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.PayWxRequest;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.RechargeMxEntity;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.AlterEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.GbListEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.MyInfoEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.ShareCodeEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GZImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.GeZhuFootEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTFootInfoEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTHomeListEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTImgEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTUserInfo;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SearchFootEntity;
import com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.entity.ShootInfoEntity;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.LogInfoBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UpdateInfo;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/9/5.
 */

public interface ApiService {

    // 主页数据
    @GET("GAPI/V1/GetAd")
    Observable<ApiResponse<List<HomeAd>>> getAllAd();

    // 用户信息接口
    @FormUrlEncoded
    @POST("GAPI/V1/Login")
    Observable<ApiResponse<UserBean>> login(@FieldMap Map<String, Object> params,
                                            @Query("timestamp") long timestamp,
                                            @Query("sign") String sign);

    //    @POST("CPAPI/V1/version")
//    Observable<List<UpdateInfo>> version(@FieldMap Map<String, Object> params,
//    Observable<ResponseBody> version(@FieldMap Map<String, Object> params,
//                                                      @Query("timestamp") long timestamp,
//                                                      @Query("sign") String sign);
    @POST("CPAPI/V1/version")
    Observable<List<UpdateInfo>> version(@Query("id") String u);

    //获取头像
    @GET("GAPI/V1/GetUserHeadImg")
    Observable<ApiResponse<String>> getUserHeadImg(@Query("u") String u);


    //发送验证码
    @FormUrlEncoded
    @POST("GAPI/V1/SendVerifyCode")
    Observable<ApiResponse<CheckCode>> sendVerifyCode(@FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);

    //注册
    @FormUrlEncoded
    @POST("GAPI/V1/Regist")
    Observable<ApiResponse<CheckCode>> userRegist(@FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);

    //找回登录密码
    @FormUrlEncoded
    @POST("GAPI/V1/FindPwd")
    Observable<ApiResponse<CheckCode>> findPwd(@FieldMap Map<String, Object> params,
                                               @Query("timestamp") long timestamp,
                                               @Query("sign") String sign);

    //余额充值
    @POST("GAPI/V1/CreateRechargeOrder")
    Observable<ApiResponse<Order>> createRechargeOrder(@Header("auth") String token,
                                                       @Body RequestBody body,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);

    //微信支付（余额支付）
    @POST("GAPI/V1/GetWXPrePayOrderForRecharge")
    Observable<ApiResponse<PayRequest>> getWXPrePayOrderForRecharge(@Header("auth") String token,
                                                                    @Body RequestBody body,
                                                                    @Query("timestamp") long timestamp,
                                                                    @Query("sign") String sign);

    //单点登录
    @FormUrlEncoded
    @POST("GAPI/V1/SingleLoginCheck")
    Observable<ApiResponse<DeviceBean>> singleLoginCheck(@Header("auth") String token,
                                                         @FieldMap Map<String, Object> params,
                                                         @Query("timestamp") long timestamp,
                                                         @Query("sign") String sign);

    //退出登录
    @POST("GAPI/V1/Logout")
    Observable<ApiResponse<String>> logout(@Header("auth") String token);

    //修改密码
    @FormUrlEncoded
    @POST("GAPI/V1/SetUserPwd")
    Observable<ApiResponse<Object>> changePassword(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);


    //登录时候判断
    // 获取用户的最新登录信息
    @FormUrlEncoded
    @POST("GAPI/V1/GetLoginInfo")
    Observable<ApiResponse<LogInfoBean>> getGetLoginInfo1(@FieldMap Map<String, Object> params,
                                                          @Query("sign") String sign,
                                                          @Query("timestamp") long timestamp);

    //闪图时候看用户是否在新设备登录
    // 获取用户的最新登录信息
    @FormUrlEncoded
    @POST("GAPI/V1/GetLoginInfo")
    Observable<ApiResponse<DeviceBean>> getDeviceInfo(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);


    //获取鸽运通比赛列表
    @GET("CHAPI/V1/GetGYTRaceList")
    Observable<ApiResponse<List<GeYunTong>>> getGeYunTongRaceList(@Header("auth") String token,
                                                                  @QueryMap Map<String, Object> urlParams);

//    //获取鸽运通比赛列表(测试用，返回json字符串)
//    @GET("CHAPI/V1/GetGYTRaceList")
//    Observable<Object> getGeYunTongRaceList1(@Header("auth") String token,
//                                             @QueryMap Map<String, Object> urlParams);

    //获取日志信息
    @GET("GAPI/V1/GetUserOperateLogs")
    Observable<ApiResponse<List<LogbookEntity>>> getUserOperateLogs(@Header("auth") String token,
                                                                    @QueryMap Map<String, Object> urlParams);


//    //获取网站公告信息
//    @GET("CHAPI/V1/GetAnnouncementTop")
//    Observable<ApiResponse<List<BulletinEntity>>> getAnnouncementTop(@Header("auth") String token);

    //获取网站公告信息
    @GET("CHAPI/V1/GetAnnouncementList")
    Observable<ApiResponse<List<BulletinEntity>>> getAnnouncementTop(@Header("auth") String token);

    //获取用户关联的组织信息
    @GET("CHAPI/V1/GetOrgInfo")
    Observable<ApiResponse<OrgInfo>> getOrgInfo(@Header("auth") String token,
                                                @Query("uid") int uid);

    //设置用户关联的组织信息
    @FormUrlEncoded
    @POST("CHAPI/V1/SetOrgInfo")
    Observable<ApiResponse<OrgInfo>> setOrgInfo(@Header("auth") String token,
                                                @FieldMap Map<String, Object> params,
                                                @Query("timestamp") long timestamp,
                                                @Query("sign") String sign);


    //提交组织二级域名修改申请
    @FormUrlEncoded
    @POST("CHAPI/V1/SubmitOrgSubDomainApply")
    Observable<ApiResponse<String>> submitOrgSubDomainApply(@Header("auth") String token,
                                                            @FieldMap Map<String, Object> params,
                                                            @Query("timestamp") long timestamp,
                                                            @Query("sign") String sign);


    //提交修改组织申请
    @POST("CHAPI/V1/SubmitOrgNameApply")
    Observable<ApiResponse<String>> submitOrgNameApply(@Header("auth") String token,
                                                       @Body RequestBody body,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);


    //获取用户类型
    @FormUrlEncoded
    @POST("CHAPI/V1/GetUserType")
    Observable<ApiResponse<UserType>> getUserType(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //获取协会（公棚）相关申请状态（包括协会名称，二级域名）
    @FormUrlEncoded
    @POST("CHAPI/V1/GetOrgApplyStatus")
    Observable<ApiResponse<AlterEntity>> getOrgApplyStatus(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //获取鸽运通信息
    @POST("CHAPI/V1/GetGYTInfo")
    Observable<ApiResponse<GYTHomeEntity>> getGYTInfo(@Header("auth") String token,
                                                      @QueryMap Map<String, Object> params);


    //获取鸽运通用户的统计信息
    @POST("CHAPI/V1/GetGYTStatisticalData")
    Observable<ApiResponse<GYTStatisticalEntity>> getGYTStatisticalData(@Header("auth") String token,
                                                                        @QueryMap Map<String, Object> params);


    //14.获取授权的用户列表
    @POST("CHAPI/V1/GetGYTAuthUsers")
    Observable<ApiResponse<List<AuthHomeEntity>>> getGYTAuthUsers(@Header("auth") String token,
                                                                  @Query("uid") int uid);


    //16.鸽运通比赛取消授权
    @FormUrlEncoded
    @POST("CHAPI/V1/RemoveGYTRaceAuth")
    Observable<ApiResponse<String>> removeGYTRaceAuth(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);


    //15.鸽运通比赛授权
    @FormUrlEncoded
    @POST("CHAPI/V1/GYTRaceAuth")
    Observable<ApiResponse<String>> gytRaceAuth(@Header("auth") String token,
                                                @FieldMap Map<String, Object> params,
                                                @Query("timestamp") long timestamp,
                                                @Query("sign") String sign);

    //13.通过用户手机号码获取用户信息
    @POST("CHAPI/V1/QueryUserByPhone")
    Observable<ApiResponse<List<AddAuthEntity>>> queryUserByPhone(@Header("auth") String token,
                                                                  @Query("p") String phone);


//    //18.获取需要接受授权的鸽运通比赛列表（舍弃未用到）
//    @FormUrlEncoded
//    @POST("CHAPI/V1/GetGYTAuthRaceList")
//    Observable<ApiResponse<List<GYTAuthRaceListEntity>>> getGYTAuthRaceList(@Header("auth") String token,
//                                                                            @FieldMap Map<String, Object> params);
//    //18.获取需要接受授权的鸽运通比赛列表
//    @POST("CHAPI/V1/GetGYTAuthRaceList")
//    Observable<String> getGYTAuthRaceList1(@Header("auth") String token,
//                                           @QueryMap Map<String, Object> params);


    //短信通知用户下载app
    @FormUrlEncoded
    @POST("CHAPI/V1/SendMSG")
    Observable<ApiResponse<Object>> SendMSG(@Header("auth") String token,
                                            @FieldMap Map<String, Object> params,
                                            @Query("timestamp") long timestamp,
                                            @Query("sign") String sign);


    //短信通知用户下载app
    @FormUrlEncoded
    @POST("CHAPI/V1/GetFlyingAreas")
    Observable<ApiResponse<List<FlyingAreaEntity>>> getFlyingAreas(@Header("auth") String token,
                                                                   @FieldMap Map<String, Object> params,
                                                                   @Query("timestamp") long timestamp,
                                                                   @Query("sign") String sign);

    //24.创建司放地
    @FormUrlEncoded
    @POST("CHAPI/V1/CreateFlyingArea")
    Observable<ApiResponse<Object>> createFlyingArea(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);

    //25.修改司放地
    @FormUrlEncoded
    @POST("CHAPI/V1/ModifyFlyingArea")
    Observable<ApiResponse<Object>> modifyFlyingArea(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);

    //27.删除司放地
    @FormUrlEncoded
    @POST("CHAPI/V1/DeleteFlyingArea")
    Observable<ApiResponse<Object>> deleteFlyingArea(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);

    //20.添加鸽运通比赛
    @FormUrlEncoded
    @POST("CHAPI/V1/CreateGYTRace")
    Observable<ApiResponse<Object>> createGYTRace(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //23.批量删除鸽运通比赛
    @FormUrlEncoded
    @POST("CHAPI/V1/DeleteGYTRaces")
    Observable<ApiResponse<Object>> deleteGYTRaces(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);

    //22.删除鸽运通比赛
    @FormUrlEncoded
    @POST("CHAPI/V1/DeleteGYTRace")
    Observable<ApiResponse<Object>> deleteGYTRace(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //21.修改鸽运通比赛
    @FormUrlEncoded
    @POST("CHAPI/V1/UpdateGYTRace")
    Observable<ApiResponse<Object>> updateGYTRace(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //17.鸽运通比赛授权确认
    @FormUrlEncoded
    @POST("CHAPI/V1/GYTAuthConfirm")
    Observable<ApiResponse<Object>> gytAuthConfirm(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);


    //32.获取发布的视频照片
    @FormUrlEncoded
    @POST("CHAPI/V1/GetGYTRaceImageOrVideo")
    Observable<ApiResponse<List<ImgOrVideoEntity>>> getGYTRaceImageOrVideo(@Header("auth") String token,
                                                                           @FieldMap Map<String, Object> params,
                                                                           @Query("timestamp") long timestamp,
                                                                           @Query("sign") String sign);

    //28.开始鸽运通监控
    @FormUrlEncoded
    @POST("CHAPI/V1/StartRaceMonitor")
    Observable<ApiResponse<Object>> getGYTStartMonitor(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);


    //29.结束鸽运通监控
    @FormUrlEncoded
    @POST("CHAPI/V1/StopRaceMonitor")
    Observable<ApiResponse<Object>> getGYTStopMonitor(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);

    //30.获取监控定位数据报表
    @FormUrlEncoded
    @POST("CHAPI/V1/GetGYTLocationInfoReports")
    Observable<ApiResponse<List<LocationInfoEntity>>> getGYTLocationInfoReports(@Header("auth") String token,
                                                                                @FieldMap Map<String, Object> params,
                                                                                @Query("timestamp") long timestamp,
                                                                                @Query("sign") String sign);


//    //30.获取监控定位数据报表
//    @FormUrlEncoded
//    @POST("CHAPI/V1/GetGYTLocationInfoReports")
//     Call<Object> getGYTLocationInfoReports(@Header("auth") String token,
//                                            @FieldMap Map<String, Object> params,
//                                            @Query("timestamp") long timestamp,
//                                            @Query("sign") String sign);


    //31.照片视频上传
    @POST("CHAPI/V1/UploadGYTRaceImageOrVideo")
    Observable<ApiResponse<Object>> uploadGYTRaceImageOrVideo(@Header("auth") String token,
                                                              @Body RequestBody body,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);

    //28.获取标签
    @POST("GAPI/V1/GetTAG")
    Observable<ApiResponse<List<TagEntitiy>>> getTAG(@Query("type") String type);

    //获取开通鸽运通数据   /CPAPI/V1/GetServicesInfo
    @FormUrlEncoded
//    @POST("GAPI/V1/GetServicesInfo")
    @POST("CPAPI/V1/GetServicesInfo")
    Observable<Object> getServicesInfo(@Header("auth") String token,
                                       @FieldMap Map<String, Object> params,
                                       @Query("timestamp") long timestamp,
                                       @Query("sign") String sign);


    //9.创建鸽运通订单
    @FormUrlEncoded
    @POST("CHAPI/V1/CreateGYTOrder")
    Observable<ApiResponse<Order>> createGYTOrder(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //设置用户支付密码
    @FormUrlEncoded
    @POST("GAPI/V1/SetUserPayPwd")
    Observable<ApiResponse<Object>> setUserPayPwd(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);

    //24.订单支付-余额支付
    @FormUrlEncoded
    @POST("GAPI/V1/OrderPayByBalance")
    Observable<ApiResponse<Object>> orderPayByBalance(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);


    //22.订单支付-创建微信预支付订单
    @FormUrlEncoded
    @POST("GAPI/V1/GetWXPrePayOrder")
    Observable<ApiResponse<PayWxRequest>> getWXPrePayOrder(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);

//    //17.获取我的鸽运通订单列表
//    @FormUrlEncoded
//    @POST("GAPI/V1/GetMyOrderList")
//    Observable<ApiResponse<List<OrderList>>> getMyOrderList(@Header("auth") String token,
//                                                            @FieldMap Map<String, Object> params,
//                                                            @Query("timestamp") long timestamp,
//                                                            @Query("sign") String sign);


//    //17.获取我的鸽运通订单列表
//    @FormUrlEncoded
//    @POST("CHAPI/V1/MyOrderList")
//    Observable<ApiResponse<List<OrderList>>> getMyOrderList(@Header("auth") String token,
//                                                            @FieldMap Map<String, Object> params,
//                                                            @Query("timestamp") long timestamp,
//                                                            @Query("sign") String sign);

    //17.获取我的鸽运通订单列表
    @FormUrlEncoded
    @POST("CHAPI/V1/MyOrderList")
//Observable<ResponseBody> getMyOrderList(@Header("auth") String token,
    Observable<ApiResponse<List<OrderList>>> getMyOrderList(@Header("auth") String token,
                                                            @FieldMap Map<String, Object> params,
                                                            @Query("timestamp") long timestamp,
                                                            @Query("sign") String sign);

    //获取服务信息
    @FormUrlEncoded
    @POST("GAPI/V1/GetServicePackageInfo")
    Observable<ApiResponse<List<PackageInfo>>> getServicePackageInfo(@Header("auth") String token,
                                                                     @FieldMap Map<String, Object> params,
                                                                     @Query("timestamp") long timestamp,
                                                                     @Query("sign") String sign);

    //11.获取鸽运通用户续费套餐信息
    @GET("CHAPI/V1/GetGytRenewalServicePackageInfo")
    Observable<ApiResponse<List<PackageInfo>>> getGytRenewalServicePackageInfo(@Header("auth") String token,
                                                                               @QueryMap Map<String, Object> urlParams);

    //10.获取鸽运通用户升级套餐信息
    @GET("CHAPI/V1/GetGytUpgradeServicePackageInfo")
    Observable<ApiResponse<List<PackageInfo>>> getGytUpgradeServicePackageInfo(@Header("auth") String token,
                                                                               @QueryMap Map<String, Object> urlParams);


    //51.获取鸽运通定位信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GetGTYRaceLocation")
    Observable<ApiResponse<List<RaceLocationEntity>>> getGTYRaceLocation(@Header("auth") String token,
                                                                         @FieldMap Map<String, Object> params,
                                                                         @Query("timestamp") long timestamp,
                                                                         @Query("sign") String sign);

    //结束监控  dialog 显示信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GetPlaybackInfo")
    Observable<ApiResponse<KjLcEntity>> getPlaybackInfo(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //监控未结束 获取空距，里程
    @FormUrlEncoded
    @POST("CHAPI/V1/GetMonitorInfo")
    Observable<ApiResponse<KjLcInfoEntity>> getMonitorInfo(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //比赛监控权限检查
    @FormUrlEncoded
    @POST("CHAPI/V1/GYTRaceCHK")
    Observable<ApiResponse<Object>> getGYTRaceCHK(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //离线上传
    @FormUrlEncoded
    @POST("CHAPI/V1/GYT_UpdateCacheData")
    Observable<ApiResponse<Object>> getGYTOfflineUpload(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);

    //获取地震信息
    @POST("Other/GetDiZhen")
    Observable<DiZhenEntity> getGTYRaceLocation();


    //获取地震信息
    @POST("Other/GetTaiYangCiBao")
    Observable<DiZhenEntity> getTaiYangCiBao();

    //获取地震和磁暴信息
    @POST("/Other/GetDiZhenCiBao")
    Observable<DiZhenEntity> getDiZhenCiBao();

    //获取赛格通主页数据   获取鸽主入棚记录
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetGeZhu")
    Observable<ApiResponse<List<SGTHomeListEntity>>> getSGTGeZhu(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //获取模糊搜索赛格通足环信息
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetFootSS")
    Observable<ApiResponse<List<FootSSEntity>>> getSGTFootSS(@Header("auth") String token,
                                                             @FieldMap Map<String, Object> params,
                                                             @Query("timestamp") long timestamp,
                                                             @Query("sign") String sign);


    //获取获取足环照片
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetImagesFoot")
    Observable<ApiResponse<List<SGTImgEntity>>> getSGTImageFoot(@Header("auth") String token,
                                                                @FieldMap Map<String, Object> params,
                                                                @Query("timestamp") long timestamp,
                                                                @Query("sign") String sign);


    //获取鸽主的鸽子照片
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetImagesGeZhu")
    Observable<ApiResponse<List<GZImgEntity>>> getSGTImagesGeZhu(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);

//    //获取获取足环照片
//    @GET("CHAPI/V1/SGT_GetImageFoot")
//    Observable<ApiResponse<List<SGTImgEntity>>> getSGTImageFoot(@Header("auth") String token,
//                                                                @QueryMap Map<String, Object> params,
//                                                                @Query("timestamp") long timestamp,
//                                                                @Query("sign") String sign);

    //获取足环详情
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetFootInfo")
    Observable<ApiResponse<SGTFootInfoEntity>> getSGTFootInfo(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);

    //上传足环照片
    @POST("CHAPI/V1/SGT_UploadFootImage")
    Observable<ApiResponse<Object>> uploadSGTFootImage(@Header("auth") String token,
                                                       @Body RequestBody body,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);

    //上传足环照片（覆盖）
    @POST("CHAPI/V1/SGT_UploadCoverFootImage")
    Observable<ApiResponse<Object>> uploadSGTFootImage_fg(@Header("auth") String token,
                                                          @Body RequestBody body,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);

    //上传足环照片
    @POST("CHAPI/V1/SGT_UploadGeZhuImage ")
    Observable<ApiResponse<Object>> uploadSGTGeZhuImage(@Header("auth") String token,
                                                        @Body RequestBody body,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);

    //28.获取标签
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetTAG")
    Observable<ApiResponse<List<TagEntitiy>>> getSGTTAG(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //获取鸽主下的足环列表
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetFootList")
    Observable<ApiResponse<GeZhuFootEntity>> getFootList_sgt(@Header("auth") String token,
                                                             @FieldMap Map<String, Object> params,
                                                             @Query("timestamp") long timestamp,
                                                             @Query("sign") String sign);


    //获取赛鸽通用户信息
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_GetUserInfo")
    Observable<ApiResponse<SGTUserInfo>> getUserInfo_sgt(@Header("auth") String token,
                                                         @FieldMap Map<String, Object> params,
                                                         @Query("timestamp") long timestamp,
                                                         @Query("sign") String sign);

    //设置公棚可容羽数
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_SetKeRongYuShu")
    Observable<ApiResponse<Object>> settingKrys_sgt(@Header("auth") String token,
                                                    @FieldMap Map<String, Object> params,
                                                    @Query("timestamp") long timestamp,
                                                    @Query("sign") String sign);

    //赛鸽通设置本届比赛入棚时间
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_SetRuPengShiJian")
    Observable<ApiResponse<Object>> getRpTime_sgt(@Header("auth") String token,
                                                  @FieldMap Map<String, Object> params,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //修改足环号码
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_EditFootInfo")
    Observable<ApiResponse<Object>> editFootInfo_sgt(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //申请试用  公棚赛鸽
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_OpenService")
    Observable<ApiResponse<Object>> openService_sgt(@Header("auth") String token,
                                                    @FieldMap Map<String, Object> params,
                                                    @Query("timestamp") long timestamp,
                                                    @Query("sign") String sign);


    //搜索足环号码  公棚赛鸽
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_SearchFootInfo")
    Observable<ApiResponse<SearchFootEntity>> searchFootInfo_sgt(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //重新补拍足环搜索  公棚赛鸽
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_SearchBuPaiFootInfo")
    Observable<ApiResponse<SearchFootEntity>> searchBuPaiFootInfo_sgt(@Header("auth") String token,
                                                                      @FieldMap Map<String, Object> params,
                                                                      @Query("timestamp") long timestamp,
                                                                      @Query("sign") String sign);

    //上传身份证信息（申请试用）
    @POST("CHAPI/V1/OpenXGT")
    Observable<ApiResponse<Object>> openXGT(@Header("auth") String token,
                                            @Body RequestBody body,
                                            @Query("timestamp") long timestamp,
                                            @Query("sign") String sign);


    //获取训鸽通信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GetXGTInfo")
    Observable<ApiResponse<XGTEntity>> getXGTInfos(@Header("auth") String token,
//    Observable<Object> getXGTInfos(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);

    //训鸽通开通
    @FormUrlEncoded
    @POST("CHAPI/V1/GetXgtOpenServicePackageInfo")
    Observable<ApiResponse<List<XGTOpenAndRenewEntity>>> getXGTOpenInfos(@Header("auth") String token,
                                                                         @FieldMap Map<String, Object> params,
                                                                         @Query("timestamp") long timestamp,
                                                                         @Query("sign") String sign);


    //训鸽通续费
    @FormUrlEncoded
    @POST("CHAPI/V1/GetXgtRenewalServicePackageInfo")
    Observable<ApiResponse<List<XGTOpenAndRenewEntity>>> getXGTrenewInfos(@Header("auth") String token,
                                                                          @FieldMap Map<String, Object> params,
                                                                          @Query("timestamp") long timestamp,
                                                                          @Query("sign") String sign);

//    //训鸽通开通
//    @POST("CHAPI/V1/GetXgtOpenServicePackageInfo")
//    Observable<ApiResponse<List<XGTOpenAndRenewEntity>>> getXGTOpenInfos();
//
//    //训鸽通续费
//    @POST("CHAPI/V1/GetXgtRenewalServicePackageInfo")
//    Observable<ApiResponse<List<XGTOpenAndRenewEntity>>> getXGTRenewInfos();

    //1获取训鸽通订单
    @FormUrlEncoded
    @POST("CHAPI/V1/CreateXGTOrder")
    Observable<ApiResponse<Order>> getCreateXGTOrder(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //1获取 开通赛鸽通 订单
    @FormUrlEncoded
    @POST("CHAPI/V1/SGT_CreateOrder")
    Observable<ApiResponse<Order>> getCreateSGTOrder(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //创建订单
    @FormUrlEncoded
    @POST("CHAPI/V1/CreateServiceOrder")
    Observable<ApiResponse<Order>> getCreateServiceOrder(@Header("auth") String token,
                                                         @FieldMap Map<String, Object> params,
                                                         @Query("timestamp") long timestamp,
                                                         @Query("sign") String sign);

    //获取服务信息   到期时间
    @FormUrlEncoded
    @POST("CHAPI/V1/GetServesInfo")
    Observable<ApiResponse<ServesInfoEntity>> getServesInfo(@Header("auth") String token,
//    Observable<ResponseBody> getServesInfo(@Header("auth") String token,
                                                            @FieldMap Map<String, Object> params,
                                                            @Query("timestamp") long timestamp,
                                                            @Query("sign") String sign);


    //24.订单支付-训鸽通鸽币支付
    @FormUrlEncoded
//    @POST("CPAPI/V1/OrderPayScoreByXGT")
    @POST("CHAPI/V1/OrderPayScoreByXGT")
    Observable<ApiResponse<Object>> getOrderPayByScore_xgt(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);

    //24.订单支付-鸽运通鸽币支付
    @FormUrlEncoded
    @POST("CHAPI/V1/OrderPayScoreByGYT")
    Observable<ApiResponse<Object>> getOrderPayByScore_gyt(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);

    //删除订单
    @FormUrlEncoded
    @POST("CHAPI/V1/DeleteOrder")
    Observable<ApiResponse<Object>> subDeleteOrder(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);

    //18.获取我的充值记录列表 (充值明细)
    @FormUrlEncoded
    @POST("GAPI/V1/GetMyRechargeList")
    Observable<ApiResponse<List<RechargeMxEntity>>> getMyRechargeList(@Header("auth") String token,
//    Observable<ResponseBody> getMyRechargeList(@Header("auth") String token,
                                                                      @FieldMap Map<String, Object> params,
                                                                      @Query("timestamp") long timestamp,
                                                                      @Query("sign") String sign);


    //删除明细订单
    @FormUrlEncoded
    @POST("CHAPI/V1/DeleteChongZhi")
    Observable<ApiResponse<Object>> getDeleteChongZhi(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);

    //=======================================协会服务============================================================

    //获取协会规程信息列表 赛事规程
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetGuiChengList")
    Observable<ApiResponse<List<GcListEntity>>> getGuiChengList_xh(@Header("auth") String token,
                                                                   @FieldMap Map<String, Object> params,
                                                                   @Query("timestamp") long timestamp,
                                                                   @Query("sign") String sign);


    //获取协会规程信息详细 赛事规程
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetGuiChengDetail")
    Observable<ApiResponse<GcItemEntity>> getGuiChengDetail_xh(@Header("auth") String token,
//    Observable<ApiResponse<Object>> getGuiChengDetail_xh(@Header("auth") String token,
                                                               @FieldMap Map<String, Object> params,
                                                               @Query("timestamp") long timestamp,
                                                               @Query("sign") String sign);


    //添加协会赛事规程 赛事规程
    @POST("CHAPI/V1/XH_GuiChengAdd")
    Observable<ApiResponse<Object>> guiChengAdd_xh(@Header("auth") String token,
                                                   @Body RequestBody body,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);


    //修改协会赛事规程 赛事规程
    @POST("CHAPI/V1/XH_GuiChengEdit")
    Observable<ApiResponse<Object>> guiChengEdit_xh(@Header("auth") String token,
                                                    @Body RequestBody body,
                                                    @Query("timestamp") long timestamp,
                                                    @Query("sign") String sign);


    //获取协会动态信息列表 赛事规程
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetDongTaiList")
    Observable<ApiResponse<List<DtListEntity>>> getDongTaiList_xh(@Header("auth") String token,
                                                                  @FieldMap Map<String, Object> params,
                                                                  @Query("timestamp") long timestamp,
                                                                  @Query("sign") String sign);


    //获取协会动态信息详细 协会动态
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetDongTaiDetail")
    Observable<ApiResponse<DtItemEntity>> getDongTaiDetail_XH(@Header("auth") String token,
//    Observable<ApiResponse<Object>> getGuiChengDetail_xh(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);

    //添加协会动态 协会动态
    @POST("CHAPI/V1/XH_DongTaiAdd")
    Observable<ApiResponse<Object>> dongTaiAdd_XH(@Header("auth") String token,
                                                  @Body RequestBody body,
                                                  @Query("timestamp") long timestamp,
                                                  @Query("sign") String sign);


    //修改协会动态 协会动态
    @POST("CHAPI/V1/XH_DongTaiEdit")
    Observable<ApiResponse<Object>> editDongTaiEdit_XH(@Header("auth") String token,
                                                       @Body RequestBody body,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);


    //删除协会动态 协会动态
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_DongTaiDelete")
    Observable<ApiResponse<Object>> delDongTaiEdit_XH(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);


    //删除赛事规程  赛事规程
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GuiChengDelete")
    Observable<ApiResponse<Object>> delGcEdit_XH(@Header("auth") String token,
                                                 @FieldMap Map<String, Object> params,
                                                 @Query("timestamp") long timestamp,
                                                 @Query("sign") String sign);

    @POST("CHAPI/V1/GXT_XGQMXX")
    Observable<ApiResponse> modifySign(
            @Header("auth") String token,
            @Query("sign") String sign,
            @Query("timestamp") long timestamp,
            @Body RequestBody requestBody);

    @POST("CHAPI/V1/GXT_XGGRXX")
    Observable<ApiResponse> modifyPersonInfo(
            @Header("auth") String token,
            @Query("sign") String sign,
            @Query("timestamp") long timestamp,
            @Body RequestBody requestBody);

    @POST("CHAPI/V1/GXT_TJGRXX")
    Observable<ApiResponse> uploadPersonInfo(
            @Header("auth") String token,
            @Query("sign") String sign,
            @Query("timestamp") long timestamp,
            @Body RequestBody requestBody);

    //添加协会动态 协会动态
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetRaceList")
    Observable<ApiResponse<List<PlayListEntity>>> getRaceList_XH(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //获取协会比赛短信设置
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetBiSaiDuanXin")
    Observable<ApiResponse<BsdxSettingEntity>> getBiSaiDuanXin_xh(@Header("auth") String token,
                                                                  @FieldMap Map<String, Object> params,
                                                                  @Query("timestamp") long timestamp,
                                                                  @Query("sign") String sign);

    //提交删除比赛申请
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_RaceDeleteSQ")
    Observable<ApiResponse<Object>> subRaceDeleteSQ_xh(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);

    //提交协会比赛短信设置
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_SetBiSaiDuanXin")
    Observable<ApiResponse<Object>> setBiSaiDuanXin_xh(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);


    //获取用户信息
    @FormUrlEncoded
    @POST("CHAPI/V1/MyInfo")
    Observable<ApiResponse<MyInfoEntity>> getMyInfo(@Header("auth") String token,
//  Observable<ResponseBody> getMyInfo(@Header("auth") String token,
                                                    @FieldMap Map<String, Object> params,
                                                    @Query("timestamp") long timestamp,
                                                    @Query("sign") String sign);


    //获取鸽币信息列表
    @FormUrlEncoded
    @POST("CHAPI/V1/MyScores")
    Observable<ApiResponse<List<GbListEntity>>> getMyScoresList(@Header("auth") String token,
                                                                @FieldMap Map<String, Object> params,
                                                                @Query("timestamp") long timestamp,
                                                                @Query("sign") String sign);

    //============================================公棚服务（入棚短信）===============================================
    //公棚 入棚短信 获取入棚短信设置信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetRuPengDuanXin")
    Observable<ApiResponse<GpRpdxSetEntity>> getRuPengDuanXin_gp(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //公棚 入棚短信 提交入棚短信设置信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetRuPengDuanXin")
    Observable<ApiResponse<Object>> setRuPengDuanXin_gp(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);

    //============================================公棚服务（训赛短信）===============================================
    //公棚  训赛短信  获取公棚训赛项目列表
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetXunSaiList")
    Observable<ApiResponse<List<XsListEntity>>> getXunSaiList_gp(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //公棚  训赛短信  获取训赛项目详细
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetXunSaiDetail")
    Observable<ApiResponse<XsSmsDetailEntity>> getXunSaiDetail_gp(@Header("auth") String token,
                                                                  @FieldMap Map<String, Object> params,
                                                                  @Query("timestamp") long timestamp,
                                                                  @Query("sign") String sign);


    //公棚  训赛短信  获取训赛短信设置信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetXunSaiDuanXin")
    Observable<ApiResponse<XsSmsSetEntity>> getXunSaiDuanXin_gp(@Header("auth") String token,
                                                                @FieldMap Map<String, Object> params,
                                                                @Query("timestamp") long timestamp,
                                                                @Query("sign") String sign);


    //公棚  训赛短信  提交训赛短信设置信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetXunSaiDuanXin")
    Observable<ApiResponse<Object>> setXunSaiDuanXin_gp(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //协会  插组指定  协会获取插组奖金比例
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetJiangJinXianShiBiLi")
    Observable<ApiResponse<GetJiangJinXianShiBiLiEntity>> setXH_GetJiangJinXianShiBiLi(@Header("auth") String token,
                                                                                       @FieldMap Map<String, Object> params,
                                                                                       @Query("timestamp") long timestamp,
                                                                                       @Query("sign") String sign);

    //协会  插组指定  协会插组奖金显示比例设置
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_SetJiangJinXianShiBiLi")
    Observable<ApiResponse<Object>> setXH_SetJiangJinXianShiBiLi(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //协会  插组指定  获取协会插组每组设置详细
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetChaZu")
    Observable<ApiResponse<GP_GetChaZuEntity>> getXH_GetChaZu(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);


    //协会  插组指定  设置协会插组每组设置
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_SetChaZu")
    Observable<ApiResponse<Object>> setXH_SetChaZu(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);

    //============================================公棚服务（插组指定）===============================================

    //公棚  插组指定  获取公棚插组指定列表
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetBiSaiList")
    Observable<ApiResponse<List<DesignatedListEntity>>> getDesignatedList_gp(@Header("auth") String token,
                                                                             @FieldMap Map<String, Object> params,
                                                                             @Query("timestamp") long timestamp,
                                                                             @Query("sign") String sign);

    //公棚  插组指定  获取公棚插组指定详情  舍弃
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetBiSaiChaZu")
    Observable<ApiResponse<DesignatedSetEntity>> getDesignatedDetails_gp(@Header("auth") String token,
                                                                         @FieldMap Map<String, Object> params,
                                                                         @Query("timestamp") long timestamp,
                                                                         @Query("sign") String sign);

    //公棚  插组指定  设置公棚插组指定详情  舍弃
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetBiSaiChaZu")
    Observable<ApiResponse<Object>> setDesignated_gp(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //公棚  插组指定  获取公棚插组指定详情
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetChaZuList")
    Observable<ApiResponse<List<GetChaZuListEntity>>> getGP_GetChaZuList(@Header("auth") String token,
                                                                         @FieldMap Map<String, Object> params,
                                                                         @Query("timestamp") long timestamp,
                                                                         @Query("sign") String sign);

    //公棚  插组指定  获取公棚插组每组设置详细
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetChaZu")
    Observable<ApiResponse<GP_GetChaZuEntity>> getGP_GetChaZu(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);


    //公棚  插组指定  设置公棚插组每组设置
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetChaZu")
    Observable<ApiResponse<Object>> setGP_SetChaZu(@Header("auth") String token,
                                                   @FieldMap Map<String, Object> params,
                                                   @Query("timestamp") long timestamp,
                                                   @Query("sign") String sign);


    //公棚  插组指定  公棚插组奖金显示比例设置
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetJiangJinXianShiBiLi")
    Observable<ApiResponse<Object>> setGP_SetJiangJinXianShiBiLi(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //公棚  插组指定  公棚插组奖金显示比例设置
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetJiangJinXianShiBiLi")
    Observable<ApiResponse<GetJiangJinXianShiBiLiEntity>> setGP_GetJiangJinXianShiBiLi(@Header("auth") String token,
                                                                                       @FieldMap Map<String, Object> params,
                                                                                       @Query("timestamp") long timestamp,
                                                                                       @Query("sign") String sign);


    //============================================公棚服务（上笼短信）===============================================
    //公棚  上笼短信  获取上笼信息列表
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetShangLongList")
    Observable<ApiResponse<List<SlListEntity>>> getShangLongList_gp(@Header("auth") String token,
                                                                    @FieldMap Map<String, Object> params,
                                                                    @Query("timestamp") long timestamp,
                                                                    @Query("sign") String sign);

    //公棚  上笼短信  获取上笼短信设置信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_GetShangLongDuanXin")
    Observable<ApiResponse<SlSmsSetEntity>> getShangLongDuanXin_gp(@Header("auth") String token,
                                                                   @FieldMap Map<String, Object> params,
                                                                   @Query("timestamp") long timestamp,
                                                                   @Query("sign") String sign);


    //公棚  上笼短信  提交上笼短信设置信息
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetShangLongDuanXin")
    Observable<ApiResponse<Object>> setShangLongDuanXin_gp(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //公棚  上笼短信  提交发送短信通道测试短信
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_CeShiDuanXin")
    Observable<ApiResponse<Object>> subCeShiDuanXin_gp(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);

    //分享 创建邀请码
    @FormUrlEncoded
    @POST("CHAPI/V1/CreateYaoQingMa")
    Observable<ApiResponse<ShareCodeEntity>> createYaoQingMa(@Header("auth") String token,
                                                             @FieldMap Map<String, Object> params,
                                                             @Query("timestamp") long timestamp,
                                                             @Query("sign") String sign);


    //分享 分享图片视频获取鸽币
    @FormUrlEncoded
    @POST("CHAPI/V1/AddScoreByShare")
    Observable<ResponseBody> getAddScoreByShare(@Header("auth") String token,
                                                @FieldMap Map<String, Object> params,
                                                @Query("timestamp") long timestamp,
                                                @Query("sign") String sign);


    //空距计算
    @FormUrlEncoded
    @POST("CHAPI/V1/GetKongJu")
    Observable<ApiResponse<UllageToolEntity>> getKongju(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //============================================协会服务（鸽友交流  留言）===============================================

    //获取留言列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetLY")
    Observable<ApiResponse<List<GyjlMessageEntity>>> getXH_GetLY(@Header("auth") String token,
                                                                 @FieldMap Map<String, Object> params,
                                                                 @Query("timestamp") long timestamp,
                                                                 @Query("sign") String sign);


    //    获取评论列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetPL")
    Observable<ApiResponse<List<GyjlReviewEntity>>> getXH_GetPL(@Header("auth") String token,
                                                                @FieldMap Map<String, Object> params,
                                                                @Query("timestamp") long timestamp,
                                                                @Query("sign") String sign);


    //    删除留言评论
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_SCLYPL")
    Observable<ApiResponse<Object>> getDelXH_SCLYPL(@Header("auth") String token,
                                                    @FieldMap Map<String, Object> params,
                                                    @Query("timestamp") long timestamp,
                                                    @Query("sign") String sign);


    //    删除回复
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_SCHF")
    Observable<ApiResponse<Object>> getDelHF_XH_SCLYPL(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);

    //回复留言评论
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_HFLY")
    Observable<ApiResponse<Object>> translateLYPL_XH(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //============================================协会服务（插组指定）===============================================

    //协会  插组指定  获取协会插组指定列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetBiSaiList")
    Observable<ApiResponse<List<DesignatedListEntity>>> getDesignatedList_xh(@Header("auth") String token,
                                                                             @FieldMap Map<String, Object> params,
                                                                             @Query("timestamp") long timestamp,
                                                                             @Query("sign") String sign);

    //协会  插组指定  获取协会插组指定详情
    @FormUrlEncoded
    @POST("CHAPI/V1/XH_GetChaZuList")
    Observable<ApiResponse<List<GetChaZuListEntity>>> getDesignatedDetails_xh(@Header("auth") String token,
                                                                              @FieldMap Map<String, Object> params,
                                                                              @Query("timestamp") long timestamp,
                                                                              @Query("sign") String sign);

    //协会  插组指定  设置协会插组指定详情
    @FormUrlEncoded
    @POST("CHAPI/V1/GP_SetXHBiSaiChaZu")
    Observable<ApiResponse<Object>> setDesignated_xh(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);

    //============================================协会服务（我的会员）===============================================

    //信鸽协会会员管理系统信息
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_UserInfo")
    Observable<ApiResponse<HYGLInfoEntity>> getXHHYGL_UserInfo(@Header("auth") String token,
                                                               @FieldMap Map<String, Object> params,
                                                               @Query("timestamp") long timestamp,
                                                               @Query("sign") String sign);


    //协会会员列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_GetUserList")
    Observable<ApiResponse<HyglHomeListEntity>> getXHHYGL_GetUserList(@Header("auth") String token,
                                                                      @FieldMap Map<String, Object> params,
                                                                      @Query("timestamp") long timestamp,
                                                                      @Query("sign") String sign);

    //协会会员列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_GetAreaUserList")
    Observable<ApiResponse<HyglHomeListEntity>> getXHHYGL_GetUserList2(@Header("auth") String token,
                                                                       @FieldMap Map<String, Object> params,
                                                                       @Query("timestamp") long timestamp,
                                                                       @Query("sign") String sign);

    //协会会员列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_GetUserDetail")
    Observable<ApiResponse<HyUserDetailEntity>> getXHHYGL_GetUserDetail(@Header("auth") String token,
//    Observable<ResponseBody> getXHHYGL_GetUserDetail(@Header("auth") String token,
                                                                        @FieldMap Map<String, Object> params,
                                                                        @Query("timestamp") long timestamp,
                                                                        @Query("sign") String sign);


    //获取协会会员身份证信息
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_GetSFZXX")
    Observable<ApiResponse<HyUserIdInfo>> getXHHYGL_GetUserIdInfo(@Header("auth") String token,
                                                                  @FieldMap Map<String, Object> params,
                                                                  @Query("timestamp") long timestamp,
                                                                  @Query("sign") String sign);

    //添加协会会员
    @POST("CHAPI/V1/XHHYGL_JBXX_Add")
    Observable<ApiResponse<Object>> getXHHYGL_JBXX_Add(@Header("auth") String token,
                                                       @Body RequestBody body,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);


    //添加历次赛绩
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJ_Add")
    Observable<ApiResponse<Object>> getXHHYGL_SJ_Add(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //删除历次赛绩
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJ_Delete")
    Observable<ApiResponse<Object>> getXHHYGL_SJ_Del(@Header("auth") String token,
                                                     @FieldMap Map<String, Object> params,
                                                     @Query("timestamp") long timestamp,
                                                     @Query("sign") String sign);


    //导入历次赛绩
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJ_Import")
    Observable<ApiResponse<List<HistoryLeagueEntity>>> getXHHYGL_SJ_Import(@Header("auth") String token,
//    Observable<ResponseBody> getXHHYGL_SJ_Import(@Header("auth") String token,
                                                                           @FieldMap Map<String, Object> params,
                                                                           @Query("timestamp") long timestamp,
                                                                           @Query("sign") String sign);

    //编辑历次赛绩
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJ_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_SJ_Edit(@Header("auth") String token,
                                                      @FieldMap Map<String, Object> params,
                                                      @Query("timestamp") long timestamp,
                                                      @Query("sign") String sign);


    //获取历次赛绩
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJ_GetList")
    Observable<ApiResponse<List<HistoryLeagueEntity>>> getXHHYGL_SJ_GetList(@Header("auth") String token,
                                                                            @FieldMap Map<String, Object> params,
                                                                            @Query("timestamp") long timestamp,
                                                                            @Query("sign") String sign);

    //获取年度评选列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_NDPX_GetList")
    Observable<ApiResponse<List<YearSelectionEntitiy>>> getXHHYGL_NDPX_GetList(@Header("auth") String token,
                                                                               @FieldMap Map<String, Object> params,
                                                                               @Query("timestamp") long timestamp,
                                                                               @Query("sign") String sign);


    //添加年度评选
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_NDPX_Add")
    Observable<ApiResponse<Object>> getXHHYGL_NDPX_GetAdd(@Header("auth") String token,
                                                          @FieldMap Map<String, Object> params,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);

    //添加年度评选
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_NDPX_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_NDPX_GetEdit(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);

    //获取处罚记录列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_CFJL_GetList")
    Observable<ApiResponse<List<PenalizeRecordEntity>>> getXHHYGL_CFJL_GetList(@Header("auth") String token,
                                                                               @FieldMap Map<String, Object> params,
                                                                               @Query("timestamp") long timestamp,
                                                                               @Query("sign") String sign);


    //添加处罚记录
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_CFJL_Add")
    Observable<ApiResponse<Object>> getXHHYGL_CFJL_GetAdd(@Header("auth") String token,
                                                          @FieldMap Map<String, Object> params,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);

    //编辑处罚记录
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_CFJL_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_CFJL_GetEdit(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //获取足环购买记录列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGM_GetList")
    Observable<ApiResponse<List<FootBuyEntity>>> getXHHYGL_ZHGM_GetList(@Header("auth") String token,
                                                                        @FieldMap Map<String, Object> params,
                                                                        @Query("timestamp") long timestamp,
                                                                        @Query("sign") String sign);

    //添加足环购买记录
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGM_Add")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGM_GetAdd(@Header("auth") String token,
                                                          @FieldMap Map<String, Object> params,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);


    //编辑足环购买记录
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGM_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGM_GetEdit(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //删除足环购买记录
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGM_Delete")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGM_GetDel(@Header("auth") String token,
                                                          @FieldMap Map<String, Object> params,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);

    //获取年度缴费列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_NDJF_GetList")
    Observable<ApiResponse<List<YearPayCostEntity>>> getYearPayCost_GetList(@Header("auth") String token,
                                                                            @FieldMap Map<String, Object> params,
                                                                            @Query("timestamp") long timestamp,
                                                                            @Query("sign") String sign);

    //添加年度缴费
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_NDJF_Add")
    Observable<ApiResponse<Object>> getXHHYGL_NDJF_Add(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);


    //添加年度缴费
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_NDJF_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_NDJF_Edit(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);

    //上传协会会员身份证信息
    @POST("CHAPI/V1/XHHYGL_SFZXX")
    Observable<ApiResponse<Object>> getXHHYGL_SFZXX(@Header("auth") String token,
                                                    @Body RequestBody body,
                                                    @Query("timestamp") long timestamp,
                                                    @Query("sign") String sign);

    //设置年度会费
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SetHuiFei")
    Observable<ApiResponse<Object>> getXHHYGL_SetHuiFei(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //设置年度会费
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_GetHuiFei")
    Observable<ApiResponse<HFInfoEntity>> getXHHYGL_GetHuiFei(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);


    //4、修改鸽舍资料
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_GSZL_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_GSZL_Edit(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);

    //4、3、修改基本信息
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_JBXX_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_JBXX_Edit(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //上传头像
    @POST("CHAPI/V1/UpdateUserFaceImage")
    Observable<ApiResponse<Object>> getUpdateUserFaceImage(@Header("auth") String token,
                                                           @Body RequestBody body,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //APP使用信息设置
    @FormUrlEncoded
    @POST("CHAPI/V1/SetAppInfo")
    Observable<ApiResponse<AppInfoEntity>> getSetAppInfo(@Header("auth") String token,
                                                         @FieldMap Map<String, Object> params,
                                                         @Query("timestamp") long timestamp,
                                                         @Query("sign") String sign);


    //订单发票添加
    @FormUrlEncoded
    @POST("CHAPI/V1/User_Invoice_Set")
    Observable<ApiResponse<Object>> getUser_Invoice_Set(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //订单发票  列表
    @FormUrlEncoded
    @POST("CHAPI/V1/User_Invoice_List")
    Observable<ApiResponse<List<InvoiceEntity>>> getUser_Invoice_GetList(@Header("auth") String token,
                                                                         @FieldMap Map<String, Object> params,
                                                                         @Query("timestamp") long timestamp,
                                                                         @Query("sign") String sign);


    //订单发票  详情
    @FormUrlEncoded
    @POST("CHAPI/V1/User_Invoice_Get")
    Observable<ApiResponse<InvoiceEntity>> getUser_Invoice_detail(@Header("auth") String token,
                                                                  @FieldMap Map<String, Object> params,
                                                                  @Query("timestamp") long timestamp,
                                                                  @Query("sign") String sign);


    //订单发票  删除
    @FormUrlEncoded
    @POST("CHAPI/V1/User_Invoice_Delete")
    Observable<ApiResponse<Object>> getUser_Invoice_del(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);


    //订单发票  删除
    @FormUrlEncoded
    @POST("CHAPI/V1/User_Invoice_Bind")
    Observable<ApiResponse<Object>> getUser_Invoice_Bind(@Header("auth") String token,
                                                         @FieldMap Map<String, Object> params,
                                                         @Query("timestamp") long timestamp,
                                                         @Query("sign") String sign);


    //足环管理  获取足环列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_GetList")
    Observable<ApiResponse<FootAdminListDataEntity>> getXHHYGL_ZHGL_GetList(@Header("auth") String token,
                                                                            @FieldMap Map<String, Object> params,
                                                                            @Query("timestamp") long timestamp,
                                                                            @Query("sign") String sign);

    //足环管理  获取详细
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_GetDetail")
    Observable<ApiResponse<FootAdminEntity>> getXHHYGL_ZHGL_GetDetail(@Header("auth") String token,
                                                                      @FieldMap Map<String, Object> params,
                                                                      @Query("timestamp") long timestamp,
                                                                      @Query("sign") String sign);

    //足环管理  足环修改
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_Edit")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_Edit(@Header("auth") String token,
                                                        @FieldMap Map<String, Object> params,
                                                        @Query("timestamp") long timestamp,
                                                        @Query("sign") String sign);

    //足环管理  删除足环
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_Delete")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_Delete(@Header("auth") String token,
                                                          @FieldMap Map<String, Object> params,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);

    //足环管理  足环录入
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_Add")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_Add(@Header("auth") String token,
                                                       @FieldMap Map<String, Object> params,
                                                       @Query("timestamp") long timestamp,
                                                       @Query("sign") String sign);

    //足环管理  获取上级鸽会发行足环
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_GetFoot")
    Observable<ApiResponse<IssueFoot>> getXHHYGL_ZHGL_GetFoot(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);


    //足环管理  导入上级鸽会发行足环
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_ImportFoot")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_ImportFoot(@Header("auth") String token,
                                                              @FieldMap Map<String, Object> params,
                                                              @Query("timestamp") long timestamp,
                                                              @Query("sign") String sign);

    //足环管理  特比环图标统计
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_GetTotal")
    Observable<ApiResponse<PieChartFootEntity>> getXHHYGL_ZHGL_GetTotal(@Header("auth") String token,
                                                                        @FieldMap Map<String, Object> params,
                                                                        @Query("timestamp") long timestamp,
                                                                        @Query("sign") String sign);


    //足环管理  设置单价
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_SetFootPrice")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_SetFootPrice(@Header("auth") String token,
                                                                @FieldMap Map<String, Object> params,
                                                                @Query("timestamp") long timestamp,
                                                                @Query("sign") String sign);

    //足环管理  获取单价
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_GetFootPrice")
    Observable<ApiResponse<FootPriceEntity>> getXHHYGL_ZHGL_GetFootPrice(@Header("auth") String token,
                                                                         @FieldMap Map<String, Object> params,
                                                                         @Query("timestamp") long timestamp,
                                                                         @Query("sign") String sign);


    //特比环代售点  添加
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_DSDAdd")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_DSDAdd(@Header("auth") String token,
                                                          @FieldMap Map<String, Object> params,
                                                          @Query("timestamp") long timestamp,
                                                          @Query("sign") String sign);


    //特比环代售点价 修改
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_DSDEdit")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_DSDEdit(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //删除特比环代售点
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_DSDDelete")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_DSDDelete(@Header("auth") String token,
                                                             @FieldMap Map<String, Object> params,
                                                             @Query("timestamp") long timestamp,
                                                             @Query("sign") String sign);


    //特比环代售点 列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_DSDList")
    Observable<ApiResponse<List<AgentTakePlaceListEntity>>> getXHHYGL_ZHGL_DSDList(@Header("auth") String token,
                                                                                   @FieldMap Map<String, Object> params,
                                                                                   @Query("timestamp") long timestamp,
                                                                                   @Query("sign") String sign);

    //特比环代售点 详细
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_DSDDetail")
    Observable<ApiResponse<AgentTakePlaceListEntity>> getXHHYGL_ZHGL_DSDDetail(@Header("auth") String token,
                                                                               @FieldMap Map<String, Object> params,
                                                                               @Query("timestamp") long timestamp,
                                                                               @Query("sign") String sign);


    //上传上笼验鸽
    @POST("CHAPI/V1/XHHYGL_ZHGL_TBHYG")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_TBHYG(@Header("auth") String token,
                                                         @Body RequestBody body,
                                                         @Query("timestamp") long timestamp,
                                                         @Query("sign") String sign);


    //下级协会 获取足环列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJGH_GetFootList")
    Observable<ApiResponse<ChildFoodAdminListEntity>> getXHHYGL_SJGH_GetFootListData(@Header("auth") String token,
                                                                                     @FieldMap Map<String, Object> params,
                                                                                     @Query("timestamp") long timestamp,
                                                                                     @Query("sign") String sign);


    //下级协会  获取会员列表
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJGH_GetFootList")
    Observable<ApiResponse<List<ChildMemberEntity>>> getMemberListData(@Header("auth") String token,
                                                                       @FieldMap Map<String, Object> params,
                                                                       @Query("timestamp") long timestamp,
                                                                       @Query("sign") String sign);


    //下级协会  足环管理 足环录入
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJGH_AddFoot")
    Observable<ApiResponse<Object>> getXHHYGL_SJGH_AddFoot(@Header("auth") String token,
                                                           @FieldMap Map<String, Object> params,
                                                           @Query("timestamp") long timestamp,
                                                           @Query("sign") String sign);


    //下级协会  足环管理 足环修改
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_SJGH_EditFoot")
    Observable<ApiResponse<Object>> getXHHYGL_SJGH_EditFoot(@Header("auth") String token,
                                                            @FieldMap Map<String, Object> params,
                                                            @Query("timestamp") long timestamp,
                                                            @Query("sign") String sign);

    //下级协会  足环管理  设置单价
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_SetFootPrice")
    Observable<ApiResponse<Object>> getXHHYGL_ZHGL_SetFootPrice_Child(@Header("auth") String token,
                                                                      @FieldMap Map<String, Object> params,
                                                                      @Query("timestamp") long timestamp,
                                                                      @Query("sign") String sign);

    //下级协会  足环管理  获取单价
    @FormUrlEncoded
    @POST("CHAPI/V1/XHHYGL_ZHGL_GetFootPrice")
    Observable<ApiResponse<FootPriceEntity>> getXHHYGL_ZHGL_GetFootPrice_Child(@Header("auth") String token,
                                                                               @FieldMap Map<String, Object> params,
                                                                               @Query("timestamp") long timestamp,
                                                                               @Query("sign") String sign);


    //设置用户头像和鸽舍名称
    @POST("CHAPI/V1/SetSuiPai")
    Observable<ApiResponse<Object>> getTXGP_SetTouXiangGeSheMingCheng(@Header("auth") String token,
                                                                               @Body RequestBody body,
                                                                               @Query("timestamp") long timestamp,
                                                                               @Query("sign") String sign);

    //获取用户头像和鸽舍名称
    @FormUrlEncoded
    @POST("CHAPI/V1/GetSuiPai")
    Observable<ApiResponse<ShootInfoEntity>> getTXGP_GetTouXiangGeSheMingCheng(@Header("auth") String token,
                                                                               @FieldMap Map<String, Object> params,
                                                                               @Query("timestamp") long timestamp,
                                                                               @Query("sign") String sign);

}
