package com.cpigeon.cpigeonhelper.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class DialogUtils {

    public static void createDialog(Context context, String title, String content, String left) {
        if (context == null) return ;

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText(title)
                .setContentText(content)
                .setConfirmText(left).show();
    }

    public static void createDialog(Context context, String title, String content, String right,
                                    SweetAlertDialog.OnSweetClickListener rightListener) {
        if (context == null) return ;
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText(title)
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText(right).show();
    }

    public static void createDialog(Context context, String content,
                                    SweetAlertDialog.OnSweetClickListener rightListener) {
        if (context == null) return ;

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setTitleText("提示")
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText("确定").show();
    }

    public static void createDialogWithLeft(Context context, String content,
                                            SweetAlertDialog.OnSweetClickListener rightListener) {

        if (context == null) return ;
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCanceledOnTouchOutside(false);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText("提示")
                .setCancelText("取消")
                .setCancelClickListener(sweetAlertDialog -> {
                    dialogPrompt.dismissWithAnimation();
                })
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText("确定").show();
    }

    public static void createDialogWithLeft(Context context, String content,
                                            SweetAlertDialog.OnSweetClickListener leftListener,
                                            SweetAlertDialog.OnSweetClickListener rightListener) {
        if (context == null) return ;
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCanceledOnTouchOutside(false);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText("提示")
                .setCancelText("取消")
                .setCancelClickListener(leftListener)
                .setConfirmClickListener(rightListener)
                .setContentText(content)
                .setConfirmText("确定").show();
    }

    public static SweetAlertDialog createErrorDialog(Context context, String error) {
        if (context == null) return null;

        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialogPrompt.setTitleText("失败");
        dialogPrompt.setContentText(error);
        dialogPrompt.show();
        return dialogPrompt;
    }

    public static void createDialog(Context context, String title, String content
            , @Nullable String left, String right, @Nullable SweetAlertDialog.OnSweetClickListener leftListener, @Nullable SweetAlertDialog.OnSweetClickListener rightListener) {
        if (context == null) return ;
        SweetAlertDialog dialogPrompt;
        dialogPrompt = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialogPrompt.setCancelable(false);
        dialogPrompt.setTitleText(title);
        if (left != null) {
            dialogPrompt.setCancelText(left);
        }
        dialogPrompt.setCancelClickListener(leftListener);
        dialogPrompt.setConfirmClickListener(rightListener);
        dialogPrompt.setContentText(content);
        dialogPrompt.setConfirmText(right);
        dialogPrompt.show();

    }

    public static void createHintDialog(Context context, String content) {
        if (context == null) return ;
        createDialog(context, "提示", content
                , null, "确定", null, null);

    }
}
