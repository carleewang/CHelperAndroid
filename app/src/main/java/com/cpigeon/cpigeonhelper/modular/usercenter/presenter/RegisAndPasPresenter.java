package com.cpigeon.cpigeonhelper.modular.usercenter.presenter;

import android.util.Log;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.CheckCode;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.dao.IRegisDao;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.daoimpl.RegisImpl;
import com.cpigeon.cpigeonhelper.modular.usercenter.view.viewdao.IRegAndPasView;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.EncryptionTool;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册和忘记密码共有的控制层
 * Created by Administrator on 2017/9/7.
 */

public class RegisAndPasPresenter extends BasePresenter<IRegAndPasView, RegisImpl> {
    private Map<String, Object> postParams = new HashMap<>();
    ;//用户信息相关

    public RegisAndPasPresenter(IRegAndPasView mView) {
        super(mView);
    }

    private long timestamp;

    @Override
    protected RegisImpl initDao() {
        return new RegisImpl();
    }


    /**
     * 获取验证码
     * userPhone:输入手机号
     * t:发送类型【1：注册验证码，2：找回密码验证码，3：支付密码设置验证码】
     */
    public void getValidation(EditText userPhone, int type) {
        String phone = userPhone.getText().toString();
        if (phone.isEmpty()) {
            mView.getErrorNews("输入手机号不能为空");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清空之前的数据
        postParams.put("p", phone);//p:手机号码
        postParams.put("t", type);//t:发送类型【1：注册验证码，2：找回密码验证码，3：支付密码设置验证码】

        mDao.sendVerification(postParams, timestamp);//发送验证

        mDao.registeredData = new IBaseDao.GetServerData<CheckCode>() {
            @Override
            public void getdata(ApiResponse<CheckCode> dataApiResponse) {

                switch (dataApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.validationSucceed(dataApiResponse);
                        break;
                    case 1003://手机号码已被使用；
                        mView.getErrorNews("手机号码已被使用");
                        break;
                    case 1004://手机号码格式错误
                        mView.getErrorNews("手机号码格式错误");
                        break;
                    case 1005://手机验证码发送达到每日上限：同一个手机号 2 次
                        mView.getErrorNews("手机验证码发送达到每日上限：同一个手机号2次");
                        break;
                    case 1006://其他错误
                        mView.getErrorNews("其他错误");
                        break;
                    case 1007://手机验证码类型错误
                        mView.getErrorNews("手机验证码类型错误");
                        break;
                    case 1008://手机号码未绑定账号
                        mView.getErrorNews("手机号码未绑定账号");
                        break;
                    case 1009://5 分钟内不能重复获取验证码
                        mView.getErrorNews("5分钟内不能重复获取验证码");
                        break;
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };

//        mDao.sendVerifi = new IRegisDao.SendVerifi() {
//            @Override
//            public void getThrowable(Throwable throwable) {
//                mView.getThrowable(throwable);
//            }
//
//            @Override
//            public void getVerifiInformation(int errorCode) {
//                Log.d(TAG, "errorCode: " + errorCode);
//                switch (errorCode) {
//                    case 0://成功
//                        mView.getErrorNews("成功");
//                        Log.d(TAG, "成功: ");
//                        mView.validationSucceed();
//                        break;
//                    case 1003://手机号码已被使用；
//                        mView.getErrorNews("手机号码已被使用");
//                        Log.d(TAG, "手机号码已被使用: ");
//                        break;
//                    case 1004://手机号码格式错误
//                        mView.getErrorNews("手机号码格式错误");
//                        Log.d(TAG, "手机号码格式错误: ");
//                        break;
//                    case 1005://手机验证码发送达到每日上限：同一个手机号 2 次
//                        mView.getErrorNews("手机验证码发送达到每日上限：同一个手机号2次");
//                        Log.d(TAG, "手机验证码发送达到每日上限: ");
//                        break;
//                    case 1006://其他错误
//                        mView.getErrorNews("其他错误");
//                        Log.d(TAG, "其他错误: ");
//                        break;
//                    case 1007://手机验证码类型错误
//                        mView.getErrorNews("手机验证码类型错误");
//                        Log.d(TAG, "手机验证码类型错误: ");
//                        break;
//                    case 1008://手机号码未绑定账号
//                        mView.getErrorNews("手机号码未绑定账号");
//                        Log.d(TAG, "手机号码未绑定账号: ");
//                        break;
//                    case 1009://5 分钟内不能重复获取验证码
//                        mView.getErrorNews("5分钟内不能重复获取验证码");
//                        Log.d(TAG, "5分钟内不能重复获取验证码: ");
//                        break;
//                }
//            }
//        };
    }


    /**
     * 获取用户输入
     *
     * @param userPhone    输入用户名
     * @param validation   验证码
     * @param oldPas       旧的密码
     * @param userPas      密码  （newType=2  新的密码
     * @param userAgainPas 再次输入密码 （newType=2  再次输入新的密码）
     * @param type         验证码  1：注册验证码，2：找回密码验证码
     */
    public void userRegis(String userPhone, String validation, String userPas, String userAgainPas, String oldPas, int type) {

        if (type == 3) {//设置密码，并且还是重置密码
            if (userPas.isEmpty() || userAgainPas.isEmpty() || oldPas.isEmpty()) {
                mView.getErrorNews("输入密码不能为空");
                return;
            }
            if (!userPas.equals(userAgainPas)) {
                mView.getErrorNews("两次输入新的密码不一样");
                return;
            }

        } else {//注册，忘记密码，
            if (userPas.isEmpty() || userAgainPas.isEmpty()) {
                mView.getErrorNews("输入密码不能为空");
                return;
            }
            if (!userPas.equals(userAgainPas)) {
                mView.getErrorNews("两次输入密码不一样");
                return;
            }
        }


        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清空之前的数据
        postParams.put("t", userPhone);//t:手机号码
        postParams.put("y", validation);//y:手机验证码
        postParams.put("p", EncryptionTool.encryptAES(userAgainPas));//p:密码

        switch (type) {
            case 1://注册
                mDao.startRegistration(postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));//开始注册
                mDao.sendVerifi = new IRegisDao.SendVerifi() {
                    @Override
                    public void getThrowable(Throwable throwable) {
                        mView.getThrowable(throwable);
                    }

                    @Override
                    public void getVerifiInformation(int errorCode) {
                        switch (errorCode) {
                            case 0://成功
                                mView.getErrorNews("成功");
                                mView.validationSucceed();
                                break;
                            case 1000://手机号码已被使用；
                                mView.getErrorNews("手机号码、验证码或密码为空");
                                break;
                            case 1002://手机号码格式错误
                                mView.getErrorNews("重复注册");
                                break;
                            case 1003://手机验证码发送达到每日上限：同一个手机号 2 次
                                mView.getErrorNews("手机验证码错误或已失效");
                                break;
                            case 1006://其他错误
                                mView.getErrorNews("其他错误");
                                break;
                        }
                    }
                };
                break;

            case 2://找回密码
                mDao.requestForgetPas(postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
                mDao.sendVerifi = new IRegisDao.SendVerifi() {
                    @Override
                    public void getThrowable(Throwable throwable) {
                        mView.getThrowable(throwable);
                    }

                    @Override
                    public void getVerifiInformation(int errorCode) {
                        switch (errorCode) {
                            case 0://成功
                                mView.getErrorNews("成功");
                                mView.validationSucceed();
                                break;
                            case 1000://手机号码、验证码、密码为空
                                mView.getErrorNews("手机号码、验证码或密码为空");
                                break;
                            case 1002://手机号码格式错误
                                mView.getErrorNews("不是已绑定手机号码");
                                break;
                            case 1003://手机验证码发送达到每日上限：同一个手机号 2 次
                                mView.getErrorNews("手机验证码错误或失效");
                                break;
                        }
                    }
                };


                break;
        }
    }


    /**
     * 用户注册
     *
     * @param userPhone    电话号码
     * @param validation   验证码
     * @param userPas      输入密码
     * @param userAgainPas 再次输入密码
     */
    public void userRegistration(String userPhone, String validation, String userPas, String userAgainPas,
                                 EditText etYqm) {

        if (userPas.isEmpty() || userAgainPas.isEmpty()) {
            mView.getErrorNews("输入密码不能为空");
            return;
        }

        if (!userPas.equals(userAgainPas)) {
            mView.getErrorNews("两次输入密码不一样");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清空之前的数据
        postParams.put("t", userPhone);//t:手机号码
        postParams.put("y", validation);//y:手机验证码
        postParams.put("p", EncryptionTool.encryptAES(userAgainPas));//p:密码
        postParams.put("yqm", etYqm.getText().toString());//yqm:邀请码

        mDao.startRegistration(postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));//开始注册
        mDao.registeredData = new IBaseDao.GetServerData<CheckCode>() {
            @Override
            public void getdata(ApiResponse<CheckCode> checkCodeApiResponse) {
                Log.d(TAG, "getdata: cod" + checkCodeApiResponse.getErrorCode() + "   msg-->" + checkCodeApiResponse.getMsg());
                switch (checkCodeApiResponse.getErrorCode()) {
                    case 0://成功
                        mView.validationSucceed();
                        break;
                    case 1000://手机号码已被使用；
                        mView.getErrorNews("手机号码、验证码或密码为空");
                        break;
                    case 1002://手机号码格式错误
                        mView.getErrorNews("重复注册");
                        break;
                    case 1003://手机验证码发送达到每日上限：同一个手机号 2 次
                        mView.getErrorNews("手机验证码错误或已失效");
                        break;
                    case 1006://其他错误
                        mView.getErrorNews("其他错误");
                        break;
                    default:
                        mView.getErrorNews(checkCodeApiResponse.getMsg());
                }
            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }


    /**
     * 忘记密码
     *
     * @param userPhone    电话号码
     * @param validation   验证码
     * @param userPas      输入密码
     * @param userAgainPas 再次输入密码
     */
    public void forgetPas(String userPhone, String validation, String userPas, String userAgainPas) {


        if (userPas.isEmpty() || userAgainPas.isEmpty()) {
            mView.getErrorNews("输入密码不能为空");
            return;
        }
        if (!userPas.equals(userAgainPas)) {
            mView.getErrorNews("两次输入密码不一样");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清空之前的数据
        postParams.put("t", userPhone);//t:手机号码
        postParams.put("y", validation);//y:手机验证码
        postParams.put("p", EncryptionTool.encryptAES(userAgainPas));//p:密码


        mDao.requestForgetPas(postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.sendVerifi = new IRegisDao.SendVerifi() {
            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }

            @Override
            public void getVerifiInformation(int errorCode) {
                switch (errorCode) {
                    case 0://成功
                        mView.validationSucceed();
                        break;
                    case 1000://手机号码、验证码、密码为空
                        mView.getErrorNews("手机号码、验证码或密码为空");
                        break;
                    case 1002://手机号码格式错误
                        mView.getErrorNews("不是已绑定手机号码");
                        break;
                    case 1003://手机验证码发送达到每日上限：同一个手机号 2 次
                        mView.getErrorNews("手机验证码错误或失效");
                        break;
                }
            }
        };
    }

    /**
     * 重置密码(修改密码)
     *
     * @param userId       用户id
     * @param oldPas       旧密码
     * @param userPas      新密码
     * @param userAgainPas 重新设置新密码
     */
    public void resettingPas(String userToken, String userId, String oldPas, String userPas, String userAgainPas) {

        if (oldPas.isEmpty() || userPas.isEmpty() || userAgainPas.isEmpty()) {
            mView.getErrorNews("输入密码不能为空");
            return;
        }

        if (!userPas.equals(userAgainPas)) {
            mView.getErrorNews("两次输入密码不一样");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清空之前的数据
        postParams.put("u", userId);//用户 ID
        postParams.put("op", EncryptionTool.encryptAES(oldPas));//旧密码【AES 加密】
        postParams.put("np", EncryptionTool.encryptAES(userAgainPas));//新密码【AES 加密】

        mDao.requestResetPas(userToken, postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));
        mDao.sendVerifi = new IRegisDao.SendVerifi() {
            @Override
            public void getVerifiInformation(int errorCode) {
                switch (errorCode) {
                    case 0://成功
                        mView.validationSucceed();
                        break;
                    case 10000://用户 ID 不一致
                        mView.getErrorNews("用户 ID 不一致");
                        break;
                    case 20000://新密码位数少于 6 位
                        mView.getErrorNews("新密码位数少于 6 位");
                        break;
                    case 20001://原密码错误
                        mView.getErrorNews("原密码错误");
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
     * 11.修改支付密码
     *
     * @param p      密码
     * @param pAgain 再次输入密码
     * @param y      验证码
     * @param t      手机号码
     */
    public void setUserPayPwd(String p, String pAgain, String y, String t) {

        if (p.length() != 6 || pAgain.length() != 6) {
            mView.getErrorNews("支付密码为6位数字");
            return;
        }

        if (!p.equals(pAgain)) {
            mView.getErrorNews("两次输入密码不一致，请重新输入");
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        postParams.clear();//清空之前的数据
        postParams.put("u", AssociationData.getUserId());//用户 ID
        postParams.put("p", EncryptionTool.encryptAES(p));//密码【AES 加密】
        postParams.put("y", y);//验证码【通过验证码接口获取】
        postParams.put("t", t);//手机号码


        mDao.requestSetUserPayPwd(AssociationData.getUserToken(), postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams));

        mDao.getUserPlayPasDatas = new IBaseDao.GetServerData<Object>() {
            @Override
            public void getdata(ApiResponse<Object> tApiResponse) {

                switch (tApiResponse.getErrorCode()) {
                    case 0:
                        mView.getErrorNews("设置成功");
                        mView.validationSucceed();
                        break;
                    default:
                        mView.getErrorNews("设置失败：" + tApiResponse.getMsg());

                }

            }

            @Override
            public void getThrowable(Throwable throwable) {
                mView.getThrowable(throwable);
            }
        };
    }

}
