package com.cpigeon.cpigeonhelper.modular.orginfo.model.daoimpl;

import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.AlterEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.dao.IOrgInforDao;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/14.
 */

public class OrgInforImpl implements IOrgInforDao {

    public GetServerData<OrgInfo> getServerData;//获取服务器数据
    public GetServerData<OrgInfo> submitServerData;//向服务器提交用户数据
    public GetServerData<String> submitApplyforNameData;//向服务器提交用户数据
    public GetServerData<AlterEntity> alterData;//向服务器提交用户数据
    public GetServerData<UserType> userType;//向服务器提交用户数据

    /**
     * @param userToken
     * @param userId
     */
    public void downOrginforData(String userToken, int userId) {
        RetrofitHelper.getApi()
                .getOrgInfo(userToken, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orgInfoApiResponse -> {
                    getServerData.getdata(orgInfoApiResponse);//获取数据
                }, throwable -> {
                    getServerData.getThrowable(throwable);//抛出异常
                });
    }

    /**
     * 修改用户信息
     */
    public void submitUserInfor(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .setOrgInfo(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orgInfoApiResponse -> {
                    submitServerData.getdata(orgInfoApiResponse);//获取数据
                }, throwable -> {
                    submitServerData.getThrowable(throwable);//抛出异常
                });
    }


    /**
     * 开始提交修改协会名称
     */
    public void submitApplyforName(String userToken, RequestBody requestBody, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .submitOrgNameApply(userToken,
                        requestBody,
                        timestamp,
                        sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    submitApplyforNameData.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    submitApplyforNameData.getThrowable(throwable);
                });

    }


    /**
     * 开始提交修改二级域名
     */
    public void submitDomain(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .submitOrgSubDomainApply(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectApiResponse -> {
                    //获取数据
                    submitApplyforNameData.getdata(objectApiResponse);
                }, throwable -> {
                    //抛出异常
                    submitApplyforNameData.getThrowable(throwable);
                });
    }


    /**
     * 获取组织名称，二级域名申请状态
     */
    public void requestState(String token, Map<String, Object> params, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getOrgApplyStatus(token, params, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alterEntityApiResponse -> {
                    //获取数据
                    alterData.getdata(alterEntityApiResponse);
                }, throwable -> {
                    //抛出异常
                    alterData.getThrowable(throwable);
                });
    }

    /**
     * 获取用户类型
     */
    public void requestGetUserType(String userToken, Map<String, Object> postParams, long timestamp, String sign) {
        RetrofitHelper.getApi()
                .getUserType(userToken, postParams, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userTypeApiResponse -> {
                    //获取数据
                    userType.getdata(userTypeApiResponse);
                }, throwable -> {
                    //抛出异常
                    userType.getThrowable(throwable);
                });
    }
}
