package com.cpigeon.cpigeonhelper.modular.home.view.frigment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.BaseFragment;
import com.cpigeon.cpigeonhelper.modular.home.view.activity.UllageToolActivity;
import com.cpigeon.cpigeonhelper.modular.menu.presenter.SettingPresenter;
import com.cpigeon.cpigeonhelper.modular.menu.view.viewdao.SettingView;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UpdateInfo;
import com.cpigeon.cpigeonhelper.modular.usercenter.presenter.UpdateManager;
import com.cpigeon.cpigeonhelper.service.SingleLoginService;
import com.cpigeon.cpigeonhelper.utils.AppManager;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.CpigeonConfig;
import com.cpigeon.cpigeonhelper.utils.FileTool;
import com.cpigeon.cpigeonhelper.utils.SharedPreferencesTool;
import com.cpigeon.cpigeonhelper.utils.butterknife.AntiShake;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.cpigeon.cpigeonhelper.utils.glide.GlideCacheUtil;
import com.cpigeon.cpigeonhelper.utils.video.VideoStorageUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页设置Fragment
 * Created by Administrator on 2017/11/23.
 */

public class SettingFragment extends BaseFragment implements SettingView {

    @BindView(R.id.ll_appraise)
    RelativeLayout llAppraise;
    @BindView(R.id.ll_edition)
    RelativeLayout llEdition;
    @BindView(R.id.out_login)
    Button outLogin;
    @BindView(R.id.app_edition)
    TextView appEdition;//app版本号
    @BindView(R.id.tv_remove_cache)
    TextView tv_remove_cache;//清除缓存

    private SettingPresenter presenter;//设置页控制层

    private File mFile;
    private long cacheSize;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void finishCreateView(Bundle state) {

        checkNewVersion(1);//版本更新检查

        appEdition.setText("v " + CommonUitls.getVersionName(getActivity()));//设置当前app的版本号

        presenter = new SettingPresenter(this);//初始化控制层

        mFile = VideoStorageUtils.getIndividualCacheDirectory(getActivity());
        try {
            cacheSize = GlideCacheUtil.getInstance().getFolderSize(mFile) + GlideCacheUtil.getInstance().getFolderSize(new File(getActivity().getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
            tv_remove_cache.setText(String.valueOf(GlideCacheUtil.getFormatSize(cacheSize)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            cacheSize = GlideCacheUtil.getInstance().getFolderSize(mFile) + GlideCacheUtil.getInstance().getFolderSize(new File(getActivity().getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
            tv_remove_cache.setText(String.valueOf(GlideCacheUtil.getFormatSize(cacheSize)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkLogin() {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        return false;
    }

    @Override
    public boolean showTips(String tip, TipType tipType, int tag) {
        return false;
    }

    @OnClick({R.id.ll_appraise, R.id.ll_edition,
            R.id.out_login, R.id.ll_ullage_tool, R.id.ll_remove_cache})
    public void onViewClicked(View view) {
        if (AntiShake.getInstance().check()) {//防止点击过快
            return;
        }
        switch (view.getId()) {

            case R.id.ll_remove_cache:
                //清除缓存
                CommonUitls.showSweetDialog(getActivity(), "是否清除缓存", dialog -> {
                    dialog.dismissWithAnimation();
                    removeCache();
                });
                break;
            case R.id.ll_appraise://应用评价
                try {
                    String mAddress = "market://details?id=" + getActivity().getPackageName();
                    Intent marketIntent = new Intent("android.intent.action.VIEW");
                    marketIntent.setData(Uri.parse(mAddress));
                    startActivity(marketIntent);
                } catch (Exception e) {
                    CommonUitls.showSweetDialog1(getActivity(), "未找到应用商城", dialog -> {
                        dialog.dismiss();
                    });
                }
                break;
            case R.id.ll_edition://当前版本
                checkNewVersion(2);
                break;
            case R.id.out_login://退出登录
                SweetAlertDialogUtil.showDialog3(errSweetAlertDialog, "是否确定退出登录", getActivity(), dialog -> {
                    presenter.startOutLogin();
                    dialog.dismiss();
                });
                break;
            case R.id.ll_ullage_tool://空距计算
                startActivity(new Intent(getActivity(), UllageToolActivity.class));
                break;
        }
    }

    /**
     * 清除缓存
     */
    private void removeCache() {
        GlideCacheUtil.clearImageDiskCache(getActivity());//清除Glide图片加载缓存
        GlideCacheUtil.deleteFolderFile(mFile.getPath(), false);//清除视频播放缓存
        tv_remove_cache.setText(String.valueOf(0 + "KB"));
    }

    @Override
    public void succeed() {
        SharedPreferencesTool.deleteFile(getActivity(), SharedPreferencesTool.SP_FILE_GUIDE);//清除赛线天气引导数据

        SingleLoginService.stopService();
        removeCache();
        CommonUitls.showToast(getActivity(), "退出登录成功");
        getActivity().finish();
        //跳转到登录页
        AppManager.getAppManager().startLogin(getApplicationContext());
    }

    /**
     * 检查新版本
     */
    private UpdateManager mUpdateManager;

    private void checkNewVersion(int tag) {
        //更新检查
        mUpdateManager = new UpdateManager(getActivity());
        mUpdateManager.setOnInstallAppListener(new UpdateManager.OnInstallAppListener() {
            @Override
            public void onInstallApp() {
                Log.d("xiaohl", "onNotFoundUpdate:  mEntryInstall = true");
            }
        });
        mUpdateManager.setOnCheckUpdateInfoListener(new UpdateManager.OnCheckUpdateInfoListener() {
            @Override
            public void onGetUpdateInfoStart() {
                Log.d("xiaohl", "onNotFoundUpdate: 检查更新中...");
            }

            @Override
            public boolean onGetUpdateInfoEnd(List<UpdateInfo> updateInfos) {
                return false;
            }

            @Override
            public void onNotFoundUpdate() {
                try {
                    if (tag == 2) {
                        //点击弹出
                        CommonUitls.showSweetDialog(getActivity(), "当前已是最新版本！！！");
                    }

                    String path = CpigeonConfig.UPDATE_SAVE_FOLDER + MyApplication.getContext().getPackageName() + ".apk";
                    FileTool.deleteDirectory(path, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHasUpdate(UpdateInfo updateInfo) {

            }

            @Override
            public void onDownloadStart() {

            }
        });

        mUpdateManager.checkUpdate();
    }
}
