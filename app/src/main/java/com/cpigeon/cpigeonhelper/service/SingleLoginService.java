package com.cpigeon.cpigeonhelper.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.db.RealmUtils;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.DeviceBean;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.EncryptionTool;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.service.ServiceUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 单点登录的Servcie
 * Created by Administrator on 2017/7/17.
 */
public class SingleLoginService extends Service {
    private static String token;
    private static String pas = "";
    private static String uid = "";
    public static final String ACTION_CHECK_SINGLE_LOGIN = "com.cpigeon.cpigeonhelper.service.SingleLoginService";
    private static final String TAG = "SingleLoginService";

    private static Activity mContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHECK_SINGLE_LOGIN.equals(action)) {
                checkLogin();
            }
        }
        return START_STICKY_COMPATIBILITY;
    }

    public static void start(Activity context) {
        SingleLoginService.mContext = context;
        token = AssociationData.getUserToken();
        uid = String.valueOf(AssociationData.getUserId());
        if (RealmUtils.getInstance().existUserLoginEntity()) {
            pas = EncryptionTool.MD5_16(EncryptionTool.decryptAES(String.valueOf(AssociationData.getUserPassword())));
        }


        Intent intent = new Intent(context, SingleLoginService.class);
        intent.setAction(ACTION_CHECK_SINGLE_LOGIN);
        context.startService(intent);
    }

    public static void stopService() {
        Intent intent = new Intent(SingleLoginService.mContext, SingleLoginService.class);
        intent.setAction(ACTION_CHECK_SINGLE_LOGIN);
        SingleLoginService.mContext.stopService(intent);
    }

    protected Map<String, Object> postParams = new HashMap<>();//存放参数
    protected long timestamp;//时间搓

    private void checkLogin() {
        if (token == null) {
            return;
        }


        postParams.clear();//清除集合中之前的数据
        timestamp = System.currentTimeMillis() / 1000;//时间戳

        postParams.put("p", pas);//密码
        postParams.put("uid", uid);//用户id

        RetrofitHelper.getApi()
                .singleLoginCheck(token, postParams, timestamp, CommonUitls.getApiSign(timestamp, postParams))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<DeviceBean>>() {
                    @Override
                    public void accept(@NonNull ApiResponse<DeviceBean> deviceBeanApiResponse) throws Exception {
                        if (deviceBeanApiResponse.getErrorCode() == 20000 || deviceBeanApiResponse.getErrorCode() == 20005) {
                            Log.d("SingleLoginService", "accept:     您已下线");
                            showNormalDialog(deviceBeanApiResponse.getMsg());
                        } else {
                            mThread = new Thread(mRunnable);
                            if (!mThread.isAlive()) {
                                mThread.start();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.d("SingleLoginService", "accept: 异常" + throwable.getLocalizedMessage());
                        mThread = new Thread(mRunnable);
                        if (!mThread.isAlive()) {
                            mThread.start();
                        }

                        //抛出网络异常相关
                        if (throwable instanceof SocketTimeoutException) {
                            CommonUitls.showToast(getApplication().getApplicationContext(), "网路有点不稳定，请检查网速");
                        } else if (throwable instanceof ConnectException) {
                            CommonUitls.showToast(getApplication().getApplicationContext(), "未能连接到服务器，请检查连接");
                        } else if (throwable instanceof RuntimeException) {
                            CommonUitls.showToast(getApplication().getApplicationContext(), "发生了不可预期的问题");
                        }
                    }
                });
    }

    private Thread mThread = null;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.e("SingleLoginService", "run: 1");
                Thread.sleep(15 * 1000);
            } catch (InterruptedException e) {
                Log.e("SingleLoginService", "run: 异常-->" + e.getLocalizedMessage());

                if (!ServiceUtil.isServiceAlive(mContext, SingleLoginService.this.getClass().getCanonicalName())) {
                    return;
                }
            }

            checkLogin();
        }
    };

    //踢下线 弹出提示框
    private void showNormalDialog(String msg) {
        if (mThread != null && mThread.isAlive()) {//如果现在正在执行
            mThread.interrupt();//线程结束
        }
        Log.d("SingleLoginService", "showNormalDialog: 11s");
        SweetAlertDialogUtil.loginOfflineHint(msg);//登录下线提示
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SingleLoginService", "onDestroy: ");
        if (mThread != null && mThread.isAlive()) {//如果现在正在执行
            mThread.interrupt();//线程结束
            mThread = null;
            mRunnable = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
