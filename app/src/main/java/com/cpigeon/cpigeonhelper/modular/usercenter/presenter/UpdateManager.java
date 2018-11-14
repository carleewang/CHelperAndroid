package com.cpigeon.cpigeonhelper.modular.usercenter.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UpdateInfo;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonTool;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.CpigeonConfig;
import com.cpigeon.cpigeonhelper.utils.SharedPreferencesTool;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-10-20.
 */

public class UpdateManager {
    private static final String DOWNLOAD_STATE_NO = "no";
    private static final String DOWNLOAD_STATE_READY = "ready";
    private static final String DOWNLOAD_STATE_ERROR = "error";
    private static final String DOWNLOAD_STATE_CANCEL = "cancel";
    private static final String DOWNLOAD_STATE_FINISHED = "finished";

    private OnInstallAppListener onInstallAppListener;
    private OnCheckUpdateInfoListener onCheckUpdateInfoListener;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    private Map<String, Object> postParams = new HashMap<>();

    public UpdateManager(@NonNull Context context) {
        mContext = context;
    }

    public void setOnCheckUpdateInfoListener(OnCheckUpdateInfoListener onCheckUpdateInfoListener) {
        this.onCheckUpdateInfoListener = onCheckUpdateInfoListener;
    }

    public UpdateManager setOnInstallAppListener(UpdateManager.OnInstallAppListener onInstallAppListener) {
        this.onInstallAppListener = onInstallAppListener;
        return this;
    }

    /**
     * 检查App更新(从服务器拉取数据)
     */
    public void checkUpdate() {
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onGetUpdateInfoStart();

        postParams.clear();

        postParams.put("id", "com.cpigeon.cpigeonhelper");

        RetrofitHelper.getApi()
                .version("com.cpigeon.cpigeonhelper")//请求服务器后段url
                .subscribeOn(Schedulers.io())//订阅（调度程序）
                .observeOn(AndroidSchedulers.mainThread())//观察（调度程序.主线程）
                .subscribe(data -> {
                    Log.d("xiazaidizhi", "checkUpdate: " + data.get(0).getUrl());
                    if (onCheckUpdateInfoListener != null) {
                        new Handler(Looper.getMainLooper())
                                .postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     if (!onCheckUpdateInfoListener.onGetUpdateInfoEnd(data))
                                                         checkUpdate(data);
                                                 }
                                             }
                                        , 500);
                    } else {
                        checkUpdate(data);
                    }

                }, throwable -> {
                    if (onCheckUpdateInfoListener != null)
                        onCheckUpdateInfoListener.onGetUpdateInfoEnd(null);
                });
    }

    /**
     * 检查App更新
     */
    private void checkUpdate(List<UpdateInfo> updateInfos) {
        if (updateInfos == null || updateInfos.size() == 0) return;
        for (UpdateInfo updateInfo : updateInfos) {
            if (mContext.getPackageName() != null && mContext.getPackageName().equals(updateInfo.getPackageName())) {
                if (updateInfo.getVerCode() > CommonTool.getVersionCode(mContext)) {
                    updateReady(updateInfo);
                    return;
                }
            }
        }
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onNotFoundUpdate();
    }

    /**
     * 下载准备
     */
    private LinearLayout mDialogLayout;
    private Button btn_cancel;//右上角取消
    private TextView tv_xbbh;//新版本号
    private TextView update_tv1;//当前版本
    private TextView update_tv2;//更新时间
    private TextView update_tv3;//更新内容
    private Button btn_ljgx;//点击更新

    private AlertDialog updateAlertDialog;

    private void updateReady(final UpdateInfo updateInfo) {

        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onHasUpdate(updateInfo);
        final String _url = updateInfo.getUrl();

        mDialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_update_dialog, null);
        btn_cancel = (Button) mDialogLayout.findViewById(R.id.btn_cancel);
        tv_xbbh = (TextView) mDialogLayout.findViewById(R.id.tv_xbbh);
        update_tv1 = (TextView) mDialogLayout.findViewById(R.id.update_tv1);
        update_tv2 = (TextView) mDialogLayout.findViewById(R.id.update_tv2);
        update_tv3 = (TextView) mDialogLayout.findViewById(R.id.update_tv3);
        update_tv3 = (TextView) mDialogLayout.findViewById(R.id.update_tv3);
        btn_ljgx = (Button) mDialogLayout.findViewById(R.id.btn_ljgx);

        tv_xbbh.setText(updateInfo.getVerName());
        update_tv1.setText("当前版本:" + CommonTool.getVersionName(mContext));
        update_tv2.setText("更新时间：" + updateInfo.getUpdateTime());
        update_tv3.setText(updateInfo.getUpdateExplain());

        //取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (updateInfo.isForce()) {
                    AppManager.getAppManager().killAllActivity();
                }
            }
        });

        //确定
        btn_ljgx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = CpigeonConfig.UPDATE_SAVE_FOLDER + mContext.getPackageName() + ".apk";
                //判断当前是否已经下载了
                if (SharedPreferencesTool.Get(mContext, "download", UpdateManager.DOWNLOAD_STATE_NO, SharedPreferencesTool.SP_FILE_APPUPDATE).equals(UpdateManager.DOWNLOAD_STATE_FINISHED)) {
                    //获取APK信息
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                    //判断是否是最新版本的APK文件
                    if (info != null && info.versionCode == updateInfo.getVerCode() && info.applicationInfo.packageName.equals(updateInfo.getPackageName())) {
                        install(path);
                        return;
                    }
                }

                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_READY, SharedPreferencesTool.SP_FILE_APPUPDATE);
                DownLoad(_url, path);
            }
        });

        //Logger.i("下载");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        updateAlertDialog = builder.create();
        updateAlertDialog.setView(mDialogLayout);
        updateAlertDialog.show();
    }

    /**
     * 下载安装包
     *
     * @param url  下载路径
     * @param path 保存路径
     */
    private void DownLoad(String url, final String path) {
        Logger.i(path);
        if (onCheckUpdateInfoListener != null)
            onCheckUpdateInfoListener.onDownloadStart();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("准备下载");
        mProgressDialog.setCancelable(false);// 设置点击空白处也不能关闭该对话框
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置采用圆形进度条
        mProgressDialog.setMax(0);
        mProgressDialog.setIndeterminate(false);// 设置显示明确的进度
        mProgressDialog.setProgressNumberFormat("%1dK/%2dK");
        mProgressDialog.show();
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(path);

        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                mProgressDialog.setMessage("开始下载");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                BigDecimal b = new BigDecimal((float) current / (float) total);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                if (mProgressDialog.getMax() == 100)
                    mProgressDialog.setMax((int) (total / 1024));
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) (f1 * mProgressDialog.getMax()));
            }

            @Override
            public void onSuccess(File result) {
                mProgressDialog.setMessage("下载完成");
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_FINISHED, SharedPreferencesTool.SP_FILE_APPUPDATE);
                install(path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                CommonUitls.showSweetDialog(MyApplication.getContext(), "下载失败");

                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_ERROR, SharedPreferencesTool.SP_FILE_APPUPDATE);
                closeAndExit();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                SharedPreferencesTool.Save(mContext, "download", UpdateManager.DOWNLOAD_STATE_CANCEL, SharedPreferencesTool.SP_FILE_APPUPDATE);
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private void install(String path) {
//        CommonTool.installApp(mContext, path);
        CommonTool.openAPKFile(mContext, path);
        if (onInstallAppListener != null)
            onInstallAppListener.onInstallApp();
    }

    private void closeAndExit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(startMain);
        System.exit(0);
    }

    public interface OnCheckUpdateInfoListener {
        /**
         * 开始获取更新信息
         */
        void onGetUpdateInfoStart();

        /**
         * 获取更新信息完成
         *
         * @param updateInfos
         * @return
         */
        boolean onGetUpdateInfoEnd(List<UpdateInfo> updateInfos);

        /**
         * 没有更新
         */
        void onNotFoundUpdate();

        /**
         * 有更新
         *
         * @param updateInfo
         */
        void onHasUpdate(UpdateInfo updateInfo);

        /**
         * 开始下载更新包
         */
        void onDownloadStart();
    }

    public interface OnInstallAppListener {
        void onInstallApp();
    }

}