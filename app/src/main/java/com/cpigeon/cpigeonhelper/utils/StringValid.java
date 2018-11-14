package com.cpigeon.cpigeonhelper.utils;

import android.text.TextUtils;

/**
 * Created by Zhu TingYu on 2017/11/20.
 */

public class StringValid {

    public static boolean isStringValid(String string) {
        return string != null && !string.isEmpty() && string.length() > 0;
    }

    public static boolean phoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11) {
            try {
                Long phone = Long.valueOf(phoneNumber);
                if (phone >= 0) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return isValid;
    }
}
