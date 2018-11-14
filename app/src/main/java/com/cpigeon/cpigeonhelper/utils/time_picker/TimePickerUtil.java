package com.cpigeon.cpigeonhelper.utils.time_picker;

import android.app.Activity;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/19.
 */

public class TimePickerUtil {


    /**
     * @param mActivity
     * @param textView
     * @param showType  必须为六位
     */
    public static void showTimePickerChoose(Activity mActivity, TextView textView, boolean[] showType,MyTimePicker.OnTimeSelectListener onTimeSelectListener) {
        //时间选择器
        MyBuilder pvTime = new MyBuilder(mActivity, onTimeSelectListener);

        pvTime.setType(showType);
        pvTime.setStartYear(Integer.valueOf(DateUtils.getStringDateY()) - 10);
        pvTime.setEndYear(Integer.valueOf(DateUtils.getStringDateY()));

        MyTimePicker mMyTimePicker = pvTime.build();
        mMyTimePicker.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        mMyTimePicker.show();
    }
}
