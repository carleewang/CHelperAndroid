package com.cpigeon.cpigeonhelper.modular.usercenter.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.cpigeon.cpigeonhelper.BuildConfig;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.LogInfoBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.dao.ILoginDao;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.daoimpl.IoginImpl;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.ILoginView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.EncryptionTool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */

public class LoginPresenter extends BasePresenter<ILoginView, IoginImpl> {

    IoginImpl iogin = new IoginImpl();
    private String TAG = "print";
    private Map<String, Object> postParams = new HashMap<>();

    private long timestamp;
    private String str;

    @Override
    protected IoginImpl initDao() {
        return new IoginImpl();
    }

    public LoginPresenter(ILoginView mView) {
        super(mView);
    }


    /**
     * 开始登录，保存用户信息
     *
     * @param userName 用户名字
     * @param userPas  用户密码
     */
    public void userLogin(EditText userName, EditText userPas) {

        if (userName.getText().toString().isEmpty()) {
            mView.getErrorNews("输入用户名不能为空");
            return;
        }

        if (userPas.getText().toString().isEmpty()) {
            mView.getErrorNews("输入密码不能为空");
            return;
        }

        //当前时间戳，签名验证使用
        timestamp = System.currentTimeMillis() / 1000;

        postParams.clear();
        postParams.put("u", userName.getText().toString());//用户名或手机号码
        postParams.put("p", EncryptionTool.encryptAES(userPas.getText().toString()));//密码(AES 加密)
        postParams.put("t", "1");//APP 类型【1：安卓；2：IPHONE】
        postParams.put("lt", "cpmanhel");//登录类型【cpmanhel:中鸽助手登录，默认为中鸽网登录】
        postParams.put("devid", AssociationData.DEV_ID);//设备 ID【设备唯一编号，通过各种渠道获取并计算，最后 MD5 加密】
        postParams.put("dev", AssociationData.DEV);//设备信息【例如：BLN-AL10)】

        postParams.put("ver", AssociationData.VER);//APP 版本代码
        postParams.put("appid", BuildConfig.APPLICATION_ID);//应用标识 【Android 为 applicationId；IOS 为 BundleId】

        iogin.loginStart(postParams, timestamp);//开始登录
        iogin.getdata = new ILoginDao.GetLoginDownData() {
            @Override
            public void getdata(ApiResponse<UserBean> userBeanApiResponse) {

//                Log.d(TAG, "登录时的错误码为: " + userBeanApiResponse.getErrorCode());
//                Log.d(TAG, "用户id: " + userBeanApiResponse.getData().getUid());
                switch (userBeanApiResponse.getErrorCode()) {

                    case 0://登录成功
                        if (RealmUtils.getInstance().existUserInfo()) {//判断是否存在用户数据
                            RealmUtils.getInstance().deleteUserInfo();//删除用户数据
                        }
                        UserBean bean = new UserBean();
                        bean.setSign(TextUtils.isEmpty(userBeanApiResponse.getData().getSign())
                                ? "这家伙很懒，什么都没有留下" : userBeanApiResponse.getData().getSign());//存放用户签名
                        bean.setHeadimgurl(userBeanApiResponse.getData().getHeadimgurl());
                        bean.setNickname(TextUtils.isEmpty(userBeanApiResponse.getData().getNickname())
                                ? userBeanApiResponse.getData().getUsername()
                                : userBeanApiResponse.getData().getNickname());
                        bean.setPassword(EncryptionTool.encryptAES(userPas.getText().toString().trim()));
                        bean.setSltoken(userBeanApiResponse.getData().getSltoken());
                        bean.setUsername(userBeanApiResponse.getData().getUsername());
                        bean.setToken(userBeanApiResponse.getData().getToken());
                        bean.setUid(userBeanApiResponse.getData().getUid());
                        bean.setAccountType(userBeanApiResponse.getData().getAccountType());
//                        bean.setAtype("admin");
//                        bean.setType("xiehui");
                        bean.setAtype(userBeanApiResponse.getData().getAtype());
                        bean.setType(userBeanApiResponse.getData().getType());

//                        Log.d("LoginPresenter", "setAtype: " + userBeanApiResponse.getData().getAtype());
//                        Log.d("LoginPresenter", "setType: " + userBeanApiResponse.getData().getType());
//                        Log.d("LoginPresenter", "setAccountType: " + userBeanApiResponse.getData().getAccountType());

                        RealmUtils.getInstance().deleteUserAllInfo();//删除所有数据
                        RealmUtils.getInstance().insertUserInfo(bean);//添加用户数据

                        mView.loginSucceed(bean);//登录成功

                        break;
                    case 1000://用户名或密码为空
                        mView.getErrorNews("用户名或密码为空");
                        break;
                    case 1001://用户名或密码错误
                        mView.getErrorNews("用户名或密码错误");
                        break;
                    case 1002://登录失败（用户名或密码错误或者没有登录权限）
                        mView.getErrorNews("登录失败（用户名或密码错误或者没有登录权限）");
                        break;
                    case 1003://登录失败,没有登录权限
                        mView.getErrorNews("登录失败,没有登录权限");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };

    }


    /**
     * 输入时候设置用户头像
     *
     * @param etUsername
     * @param userImg
     * @param context
     */
    public void setUserImg(EditText etUsername, ImageView userImg, Context context) {
        String strUserName = etUsername.getText().toString();
        iogin.getUserImg(strUserName);
        iogin.getImgUrl = new ILoginDao.GetUserImgUrl() {
            @Override
            public void getUrlData(String userImgUrl) {
                if (userImgUrl != null) {
                    mView.setUserImg(userImgUrl);
                }
            }
        };
    }


    /**
     * 获取用户的最新登录信息
     */
    public void singleLoginCheck(EditText userName, EditText userPas) {

        if (userName.getText().toString().isEmpty()) {
            mView.getErrorNews("输入用户名不能为空");
            return;
        }

        if (userPas.getText().toString().isEmpty()) {
            mView.getErrorNews("输入密码不能为空");
            return;
        }


        timestamp = System.currentTimeMillis() / 1000;//当前时间戳，签名验证使用
        postParams.clear();

        postParams.put("u", userName.getText().toString());
        postParams.put("p", EncryptionTool.encryptAES(userPas.getText().toString()));
        postParams.put("appid", BuildConfig.APPLICATION_ID);//应用标识【Android 为 applicationId；IOS 为 BundleId】

        iogin.isLogin(postParams, CommonUitls.getApiSign(timestamp, postParams), timestamp);
        iogin.serverData = new IBaseDao.GetServerData<LogInfoBean>() {
            @Override
            public void getdata(ApiResponse<LogInfoBean> logInfoBeanApiResponse) {

                switch (logInfoBeanApiResponse.getErrorCode()) {
                    case 0:
                        if (!AssociationData.DEV_ID.equals(logInfoBeanApiResponse.getData().getDeviceid())) {
                            //在别的设备上已经登录
                            mView.isNewEquipmentLogin(2);
                        } else {
                            //没有登录
                            mView.isNewEquipmentLogin(1);
                        }

                        break;
                    case 90101://用户名或密码错误
                        mView.getErrorNews("用户名或密码错误");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };

    }
}
