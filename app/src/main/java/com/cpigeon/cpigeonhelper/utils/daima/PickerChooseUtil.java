package com.cpigeon.cpigeonhelper.utils.daima;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.time_picker.MyBuilder;
import com.cpigeon.cpigeonhelper.utils.time_picker.MyTimePicker;

import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by Administrator on 2018/1/23.
 */

public class PickerChooseUtil {


    /**
     * @param mActivity
     * @param textView
     */
    public static void showTimePicker(Activity mActivity, TextView textView, DatePicker.OnYearMonthDayPickListener pickListener) {
        int rangeLabelY = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY));
        int rangeLabelM = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_MM));
        int rangeLabelD = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_DD));

        int rangeEndY = rangeLabelY + 1;
        int rangeEndM = 12;
        int rangeEndD = 31;

        final DatePicker picker = new DatePicker(mActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(mActivity, 10));
        picker.setRangeStart(rangeLabelY - 2, 1, 1);
        picker.setSelectedItem(rangeLabelY, rangeLabelM, rangeLabelD);
//        picker.setLabel(rangeLabelY,rangeLabelM,rangeLabelD);
        picker.setRangeEnd(rangeEndY, rangeEndM, rangeEndD);
        picker.setResetWhileWheel(false);
        picker.setTopLineColor(mActivity.getResources().getColor(R.color.colorPrimary));
        picker.setLabelTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        picker.setDividerColor(mActivity.getResources().getColor(R.color.colorPrimary));
        picker.setOnDatePickListener(pickListener);
        picker.show();
    }

    /**
     * @param mActivity 时分秒
     * @param textView
     */
    public static void showTimePickerChooseSMF(Activity mActivity, TextView textView) {
        //时间选择器
        MyTimePickerView pvTime = new MyTimePickerView.Builder(mActivity, new MyTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                textView.setText(DateUtils.msToHsm(date));
//                textView.setText(DateUtils.msToHsm(date.getTime()));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    /**
     * @param mActivity 年月日
     * @param textView
     */
    public static void showTimePickerChooseYMD(Activity mActivity, TextView textView) {
        //时间选择器
        MyTimePickerViewYMD pvTime = new MyTimePickerViewYMD.Builder(mActivity, new MyTimePickerViewYMD.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                textView.setText(DateUtils.dateToStrYMD(date));
//                textView.setText(DateUtils.msToHsm(date.getTime()));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    /**
     * @param mActivity
     * @param textView
     * @param showType  必须为六位
     */
    public static void showTimePickerChoose(Activity mActivity, TextView textView, boolean[] showType) {
        //时间选择器
        MyBuilder pvTime = new MyBuilder(mActivity, new MyTimePicker.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                //获取年月日  时分秒
                if (showType[0] && showType[1] && showType[2] && showType[3] && showType[4] && showType[5]) {
                    textView.setText(DateUtils.dateToStrYMSHMS(date));
                }


                //获取年月日
                if (showType[0] && showType[1] && showType[2] && !showType[3] && !showType[4] && !showType[5]) {
                    textView.setText(DateUtils.dateToStrYMD(date));
                }

                //获取时分秒
                if (!showType[0] && !showType[1] && !showType[2] && showType[3] && showType[4] && showType[5]) {
                    textView.setText(DateUtils.dateToStrHMS(date));
                }

                //获取年
                if (showType[0] && !showType[1] && !showType[2] && !showType[3] && !showType[4] && !showType[5]) {

                    Log.d("ndjf", "onTimeSelect1: " + DateUtils.dateToStrY(date));
                    Log.d("ndjf", "onTimeSelect2: " + DateUtils.dateToStrYMSHMS(date));
                    Log.d("ndjf", "onTimeSelect3: " + date);
                    textView.setText(DateUtils.dateToStrY(date));
                }
            }
        });

        pvTime.setType(showType);
        MyTimePicker mMyTimePicker = pvTime.build();
        mMyTimePicker.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        mMyTimePicker.show();
    }
}
