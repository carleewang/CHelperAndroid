package com.cpigeon.cpigeonhelper.utils.picker;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.DateUtils;

import java.util.Date;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by Administrator on 2018/6/20.
 */

public class PickerAdmin2 {


    //最近3-4年 年月日 倒序
    public static void showPicker(Activity mContent, int datePosition, DatePicker.OnYearMonthDayPickListener mOnYearMonthDayPickListener) {
        //获取当前年份
        int year = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY));

        DatePicker mDatePicker = new DatePicker(mContent);
        mDatePicker.setRangeStart(year, Integer.parseInt(DateUtils.dateToStrM(new Date())), Integer.parseInt(DateUtils.dateToStrD(new Date())));
        mDatePicker.setRangeEnd(year - 3, 12, 31);
        mDatePicker.setOnDatePickListener(mOnYearMonthDayPickListener);
        mDatePicker.show();
    }


    //最近50年 年月日 倒序
    public static void showPicker2(Activity mContent, int datePosition, DatePicker.OnYearMonthDayPickListener mOnYearMonthDayPickListener) {
        //获取当前年份
        int year = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY));

        DatePicker mDatePicker = new DatePicker(mContent);
        mDatePicker.setRangeStart(year, Integer.parseInt(DateUtils.dateToStrM(new Date())), Integer.parseInt(DateUtils.dateToStrD(new Date())));
        mDatePicker.setRangeEnd(year - 80, 12, 31);
        mDatePicker.setOnDatePickListener(mOnYearMonthDayPickListener);
        mDatePicker.show();
    }


    //最近10年 年月日 倒序
    public static void showPicker3(Activity mContent, int datePosition, DatePicker.OnYearMonthDayPickListener mOnYearMonthDayPickListener) {
        //获取当前年份
        int year = Integer.parseInt(DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY));

        DatePicker mDatePicker = new DatePicker(mContent);
        mDatePicker.setRangeStart(year, Integer.parseInt(DateUtils.dateToStrM(new Date())), Integer.parseInt(DateUtils.dateToStrD(new Date())));
        mDatePicker.setRangeEnd(year - 10, 12, 31);
        mDatePicker.setOnDatePickListener(mOnYearMonthDayPickListener);
        mDatePicker.show();
    }
}
