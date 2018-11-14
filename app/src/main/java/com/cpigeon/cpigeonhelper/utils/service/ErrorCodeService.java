package com.cpigeon.cpigeonhelper.utils.service;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 服务器返回错误码 ErrorCode
 * Created by Administrator on 2018/5/21.
 */

public class ErrorCodeService {

    public static final int LOG_SERVICE = 90102;//您的账号已在别的设备上登录，如果非本人操作请退出后重新登录并修改密码!



    /**
     * 对于同一个 dialog 只弹出一次  不能点返回键　　只有确定
     */
    public static SweetAlertDialog showDialog(SweetAlertDialog dialogZ, String errThr, Activity mContext, SweetAlertDialog.OnSweetClickListener confirmListener) {

        try {

            if (!errThr.equals("")) {
                if (dialogZ == null) {
                    return SweetAlertDialogUtil.showSweetDialogNo(dialogZ, mContext, errThr, confirmListener);
                } else if (dialogZ != null && dialogZ.isShowing()) {
                    dialogZ.dismiss();
                    return SweetAlertDialogUtil.showSweetDialogNo(dialogZ, mContext, errThr, confirmListener);
                } else {
                    return SweetAlertDialogUtil.showSweetDialogNo(dialogZ, mContext, errThr, confirmListener);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

}
