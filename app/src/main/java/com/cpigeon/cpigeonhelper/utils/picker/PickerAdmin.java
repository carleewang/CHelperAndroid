package com.cpigeon.cpigeonhelper.utils.picker;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.Lists;

import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by Administrator on 2018/6/20.
 */

public class PickerAdmin {

    //获取最近几年  年份
    public static void showPicker(Activity mContent, int datePosition, OptionPicker.OnOptionPickListener mOnOptionPickListener) {
        OptionPicker picker = new OptionPicker(mContent, getDates());
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(datePosition);
        picker.setCycleDisable(true);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(mOnOptionPickListener);
        picker.show();
    }

    public static List<String> getDates() {
        List<String> dateList = Lists.newArrayList();
        int year = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY));

        for (int i = year; i >= year - 3; i--) {
            dateList.add(String.valueOf(i));
        }

        return dateList;
    }

}
