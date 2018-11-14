package com.cpigeon.cpigeonhelper.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Created by chenshuai on 2017/4/12.
 */

public class ToastUtil {
    private static Toast toast = null;

    public static void showToast(Context context, int retId, int duration) {
        try {
            if (toast == null) {
                toast = Toast.makeText(context.getApplicationContext(), retId, duration);
            } else {
                toast.setText(retId);
                toast.setDuration(duration);
            }
            toast.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void showToast(Context context, String hint, int duration) {
        try {
            if (toast == null) {
                toast = Toast.makeText(context.getApplicationContext(), hint, duration);
            } else {
                toast.setText(hint);
                toast.setDuration(duration);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShortToast(Context context, String hint) {
        try {
            if (toast == null) {
                toast = Toast.makeText(context.getApplicationContext(), hint, Toast.LENGTH_SHORT);
            } else {
                toast.setText(hint);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLongToast(Context context, String hint) {
        try {
            if (toast == null) {
                toast = Toast.makeText(context.getApplicationContext(), hint, Toast.LENGTH_LONG);
            } else {
                toast.setText(hint);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
