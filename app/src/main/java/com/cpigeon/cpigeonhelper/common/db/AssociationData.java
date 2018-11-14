package com.cpigeon.cpigeonhelper.common.db;

import android.os.Build;
import android.text.TextUtils;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;

import io.realm.RealmResults;

/**
 * 中鸽助手数据管理类
 * Created by Administrator on 2017/5/20.
 */

public class AssociationData {

    public static final String VER = String.valueOf(CommonUitls.getVersionCode(MyApplication.getInstance()));
    public static final String DEV_ID = CommonUitls.getCombinedDeviceID(MyApplication.getInstance());
    public static final String DEV = Build.MODEL;

    public static RealmResults<UserBean> info = RealmUtils.getInstance().queryUserInfo();


    public static UserBean getUserBean() {
        for (UserBean userBean : info) {
            if (userBean != null) {
                return userBean;
            }
        }
        return null;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean checkIsLogin() {

        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getToken())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取当前用户的uid
     *
     * @return
     */
    public static int getUserId() {
        try {
            for (UserBean userBean : info) {
                if (userBean.getUid() != 0) {
                    return userBean.getUid();
                }
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    /**
     * 获取当前用户的昵称
     *
     * @return
     */
    public static String getUserName() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getNickname())) {
                return userBean.getNickname();
            }
        }
        return getName();
    }


    /**
     * 获取组织信息
     *
     * @return
     */
    public static String getUserAccountType() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getAccountType())) {
                return userBean.getAccountType();
            }
        }
        return "无组织信息";
    }

    /**
     * 获取组织信息
     *
     * @return
     */
    public static String getUserAccountTypeString() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getAccountType())) {
//                Log.d("HomeFragment", "getUserAccountTypeString: ");
                switch (userBean.getAccountType()) {
                    case "cphelper.xiehui":
                        return "协会信息";
                    case "cphelper.gongpeng":
                        return "公棚信息";

//                    case "cphelper.none":
//
//                        return "普通用户";
//
//                    case "cphelper.authuser":
//
//                        return "授权用户";
                }
            }
        }
        return "组织信息";
    }


    /**
     * 获取组织信息  //1：公棚 2：协会  3：个人  4:授权用户
     *
     * @return
     */
    public static int getUserAccountTypeInt() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getAccountType())) {
                switch (userBean.getAccountType()) {

                    case "cphelper.gongpeng":
                        return 1;
                    case "cphelper.xiehui":
                        return 2;
                    case "cphelper.geren":
                        return 3;
                    case "cphelper.authuser":
                        return 4;
                    case "cphelper.none":
                        return 5;
                }
            }
        }
        return 5;
    }


    /**
     * 获取组织类型  //1：公棚 2：协会  3：个人  4:授权用户
     *
     * @return
     */
    public static String getUserAccountTypeStrings() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getAccountType())) {
                switch (userBean.getAccountType()) {
                    case "cphelper.gongpeng":
                        return "gongpeng";

                    case "cphelper.xiehui":
                        return "xiehui";

                    case "cphelper.geren":
                        return "geren";

                    case "cphelper.authuser":
                        return "authuser";

                    case "cphelper.none":
                        return "none";
                }
            }
        }
        return "none";
    }

    /**
     * 获取名字
     *
     * @return
     */
    public static String getName() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getUsername())) {
                return userBean.getUsername();
            }
        }
        return "请设置您的名字!";
    }


    /**
     * 获取当前用户的token
     */
    public static String getUserToken() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getToken())) {
                return userBean.getToken();
            }
        }
        return null;
    }

    /**
     * 获取用户输入的密码（AES加密过后）
     */
    public static String getUserPassword() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getPassword())) {
                return userBean.getPassword();
            }
        }
        return null;
    }

    /**
     * 获取用户的签名
     *
     * @return
     */
    public static String getUserSign() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getSign())) {
                return userBean.getSign();
            }
        }
        return "这家伙很懒，啥都没写";
    }

    /**
     * 获取用户头像的url
     *
     * @return
     */
    public static String getUserImgUrl() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getHeadimgurl())) {
                return userBean.getHeadimgurl();
            }
        }
        return "www";
    }


    /**
     * 获取用户登录类型
     *
     * @return
     */
    public static String getUserType() {

        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getAccountType())) {
                switch (userBean.getAccountType()) {
                    case "cphelper.gongpeng":
                        return "gongpeng";
                    case "cphelper.xiehui":
                        return "xiehui";
                    case "cphelper.geren":
                        return "geren";
                    case "cphelper.authuser":
                        return "authuser";
                    case "cphelper.none":
                        return "none";
                }
            }
        }
        return "none";
    }

    /**
     * 获取用户类型
     *
     * @return
     */
    public static String getUserAType() {
        for (UserBean userBean : info) {
            if (!TextUtils.isEmpty(userBean.getAtype())) {
                return userBean.getAtype();
            }
        }
        return "admin";
    }

}
