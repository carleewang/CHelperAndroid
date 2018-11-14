package com.cpigeon.cpigeonhelper.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 权限
 * Created by Administrator on 2017/11/24.
 */

public class PermissonUtil {
    /**
     * 权限检查
     */
    public static void getAppDetailSettingIntent1(Context mContext) {

        try {
            List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
            permissonItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "电话簿", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
            permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_NETWORK_STATE, "网络信息", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, "相机", R.drawable.permission_ic_camera));
            permissonItems.add(new PermissionItem(Manifest.permission.READ_CONTACTS, "联系人", R.drawable.permission_ic_contacts));
            permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "地理位置", R.drawable.permission_ic_location));
            permissonItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));

            HiPermission.create(mContext)
                    .title("权限设置")
                    .permissions(permissonItems)
                        .filterColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, mContext.getTheme()))
                    .msg("当前应用需要一些权限，请打开")
                    .style(R.style.PermissionBlueStyle)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            AppManager.getAppManager().AppExit(mContext);
                        }

                        @Override
                        public void onFinish() {
                            // checkDevice();
                        }

                        @Override
                        public void onDeny(String s, int i) {
                            Logger.e("onDeny" + s + "第" + i + "个");
                        }

                        @Override
                        public void onGuarantee(String s, int i) {
                            Logger.e("onGuarantee" + s + "第" + i + "个");
                        }
                    });
        } catch (Resources.NotFoundException e) {
            SweetAlertDialogUtil.showDialog(null,e.getLocalizedMessage(), (Activity) mContext);
        }
    }

    /**
     * 权限检查
     */
    public static void getAppDetailSettingIntent(Activity mContext) {

        if (Build.VERSION.SDK_INT >= 23) {

            List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
            permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储空间", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "设备信息", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "地理位置", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "相册", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, "相机", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_phone));
            permissonItems.add(new PermissionItem(Manifest.permission.READ_CONTACTS, "联系人", R.drawable.permission_ic_contacts));

            HiPermission.create(mContext)
                    .title("权限设置")
                    .permissions(permissonItems)
                    .filterColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, mContext.getTheme()))
                    .msg("当前应用需要一些权限，请允许，否则本应用的一些功能将无法使用")
                    .style(R.style.PermissionBlueStyle)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            AppManager.getAppManager().killAllActivity();
                        }

                        @Override
                        public void onFinish() {
//                        checkDevice();
                        }

                        @Override
                        public void onDeny(String s, int i) {
                            Logger.e("onDeny" + s + "第" + i + "个");
                        }

                        @Override
                        public void onGuarantee(String s, int i) {
                            Logger.e("onGuarantee" + s + "第" + i + "个");
                        }
                    });
        } else {
            // 检查权限
            cameraIsCanUse();//开启相机权限
        }
    }

    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }
}
