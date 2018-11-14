package com.cpigeon.cpigeonhelper.utils.time_picker;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;

import com.bigkoo.pickerview.listener.CustomListener;
import com.cpigeon.cpigeonhelper.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/3/31.
 */

public class MyBuilder {
    private int layoutRes = R.layout.pickerview_time;
    private CustomListener customListener;
    private Context context;
    private MyTimePicker.OnTimeSelectListener timeSelectListener;
    private boolean[] type = new boolean[]{true, true, true, false, false, false};//显示类型 默认全部显示
    private int gravity = Gravity.CENTER;//内容显示位置 默认居中

    private String Str_Submit;//确定按钮文字
    private String Str_Cancel;//取消按钮文字
    private String Str_Title;//标题文字

    private int Color_Submit;//确定按钮颜色
    private int Color_Cancel;//取消按钮颜色
    private int Color_Title;//标题颜色

    private int Color_Background_Wheel;//滚轮背景颜色
    private int Color_Background_Title;//标题背景颜色

    private int Size_Submit_Cancel = 17;//确定取消按钮大小
    private int Size_Title = 18;//标题字体大小
    private int Size_Content = 18;//内容字体大小
    private Calendar date;//当前选中时间
    private Calendar startDate;//开始时间
    private Calendar endDate;//终止时间
    private int startYear;//开始年份
    private int endYear;//结尾年份

    private boolean cyclic = false;//是否循环
    private boolean cancelable = true;//是否能取消

    private boolean isCenterLabel = true;//是否只显示中间的label
    private boolean isLunarCalendar = false;//是否显示农历
    public ViewGroup decorView;//显示pickerview的根View,默认是activity的根view

    private int textColorOut; //分割线以外的文字颜色
    private int textColorCenter; //分割线之间的文字颜色
    private int dividerColor; //分割线的颜色
    private int backgroundId; //显示时的外部背景色颜色,默认是灰色
    private MyWheelView.DividerType dividerType;//分隔线类型
    // 条目间距倍数 默认1.6
    private float lineSpacingMultiplier = 1.6F;

    private boolean isDialog;//是否是对话框模式

    private String label_year, label_month, label_day, label_hours, label_mins, label_seconds;//单位
    private int xoffset_year, xoffset_month, xoffset_day, xoffset_hours, xoffset_mins, xoffset_seconds;//单位

    //Required
    public MyBuilder(Context context, MyTimePicker.OnTimeSelectListener listener) {
        this.context = context;
        this.timeSelectListener = listener;
    }

    //Option
    public MyBuilder setType(boolean[] type) {
        this.type = type;
        return this;
    }

    public MyBuilder gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public MyBuilder setSubmitText(String Str_Submit) {
        this.Str_Submit = Str_Submit;
        return this;
    }

    public MyBuilder isDialog(boolean isDialog) {
        this.isDialog = isDialog;
        return this;
    }

    public MyBuilder setCancelText(String Str_Cancel) {
        this.Str_Cancel = Str_Cancel;
        return this;
    }

    public MyBuilder setTitleText(String Str_Title) {
        this.Str_Title = Str_Title;
        return this;
    }

    public MyBuilder setSubmitColor(int Color_Submit) {
        this.Color_Submit = Color_Submit;
        return this;
    }

    public MyBuilder setCancelColor(int Color_Cancel) {
        this.Color_Cancel = Color_Cancel;
        return this;
    }

    /**
     * 必须是viewgroup
     * 设置要将pickerview显示到的容器id
     *
     * @param decorView
     * @return
     */
    public MyBuilder setDecorView(ViewGroup decorView) {
        this.decorView = decorView;
        return this;
    }

    public MyBuilder setBgColor(int Color_Background_Wheel) {
        this.Color_Background_Wheel = Color_Background_Wheel;
        return this;
    }

    public MyBuilder setTitleBgColor(int Color_Background_Title) {
        this.Color_Background_Title = Color_Background_Title;
        return this;
    }

    public MyBuilder setTitleColor(int Color_Title) {
        this.Color_Title = Color_Title;
        return this;
    }

    public MyBuilder setSubCalSize(int Size_Submit_Cancel) {
        this.Size_Submit_Cancel = Size_Submit_Cancel;
        return this;
    }

    public MyBuilder setTitleSize(int Size_Title) {
        this.Size_Title = Size_Title;
        return this;
    }

    public MyBuilder setContentSize(int Size_Content) {
        this.Size_Content = Size_Content;
        return this;
    }

    /**
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     *
     * @param date
     * @return
     */
    public MyBuilder setDate(Calendar date) {
        this.date = date;
        return this;
    }

    public MyBuilder setLayoutRes(int res, CustomListener customListener) {
        this.layoutRes = res;
        this.customListener = customListener;
        return this;
    }

    public MyBuilder setRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
        return this;
    }

    /**
     * 设置起始时间
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     *
     * @return
     */

    public MyBuilder setRangDate(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }


    /**
     * 设置间距倍数,但是只能在1.2-2.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public MyBuilder setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public MyBuilder setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public MyBuilder setDividerType(MyWheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        return this;
    }

    /**
     * //显示时的外部背景色颜色,默认是灰色
     *
     * @param backgroundId
     */

    public MyBuilder setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
        return this;
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public MyBuilder setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        return this;
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public MyBuilder setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        return this;
    }

    public MyBuilder isCyclic(boolean cyclic) {
        this.cyclic = cyclic;
        return this;
    }

    public MyBuilder setOutSideCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public MyBuilder setLunarCalendar(boolean lunarCalendar) {
        isLunarCalendar = lunarCalendar;
        return this;
    }

    public MyBuilder setLabel(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        this.label_year = label_year;
        this.label_month = label_month;
        this.label_day = label_day;
        this.label_hours = label_hours;
        this.label_mins = label_mins;
        this.label_seconds = label_seconds;
        return this;
    }

    /**
     * 设置X轴倾斜角度[ -90 , 90°]
     * @param xoffset_year 年
     * @param xoffset_month 月
     * @param xoffset_day 日
     * @param xoffset_hours 时
     * @param xoffset_mins 分
     * @param xoffset_seconds 秒
     * @return
     */
    public MyBuilder setTextXOffset(int xoffset_year, int xoffset_month, int xoffset_day, int xoffset_hours, int xoffset_mins, int xoffset_seconds){
        this.xoffset_year = xoffset_year;
        this.xoffset_month = xoffset_month;
        this.xoffset_day = xoffset_day;
        this.xoffset_hours = xoffset_hours;
        this.xoffset_mins = xoffset_mins;
        this.xoffset_seconds = xoffset_seconds;
        return this;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public CustomListener getCustomListener() {
        return customListener;
    }

    public void setCustomListener(CustomListener customListener) {
        this.customListener = customListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MyTimePicker.OnTimeSelectListener getTimeSelectListener() {
        return timeSelectListener;
    }

    public void setTimeSelectListener(MyTimePicker.OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public boolean[] getType() {
        return type;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public String getStr_Submit() {
        return Str_Submit;
    }

    public void setStr_Submit(String str_Submit) {
        Str_Submit = str_Submit;
    }

    public String getStr_Cancel() {
        return Str_Cancel;
    }

    public void setStr_Cancel(String str_Cancel) {
        Str_Cancel = str_Cancel;
    }

    public String getStr_Title() {
        return Str_Title;
    }

    public void setStr_Title(String str_Title) {
        Str_Title = str_Title;
    }

    public int getColor_Submit() {
        return Color_Submit;
    }

    public void setColor_Submit(int color_Submit) {
        Color_Submit = color_Submit;
    }

    public int getColor_Cancel() {
        return Color_Cancel;
    }

    public void setColor_Cancel(int color_Cancel) {
        Color_Cancel = color_Cancel;
    }

    public int getColor_Title() {
        return Color_Title;
    }

    public void setColor_Title(int color_Title) {
        Color_Title = color_Title;
    }

    public int getColor_Background_Wheel() {
        return Color_Background_Wheel;
    }

    public void setColor_Background_Wheel(int color_Background_Wheel) {
        Color_Background_Wheel = color_Background_Wheel;
    }

    public int getColor_Background_Title() {
        return Color_Background_Title;
    }

    public void setColor_Background_Title(int color_Background_Title) {
        Color_Background_Title = color_Background_Title;
    }

    public int getSize_Submit_Cancel() {
        return Size_Submit_Cancel;
    }

    public void setSize_Submit_Cancel(int size_Submit_Cancel) {
        Size_Submit_Cancel = size_Submit_Cancel;
    }

    public int getSize_Title() {
        return Size_Title;
    }

    public void setSize_Title(int size_Title) {
        Size_Title = size_Title;
    }

    public int getSize_Content() {
        return Size_Content;
    }

    public void setSize_Content(int size_Content) {
        Size_Content = size_Content;
    }

    public Calendar getDate() {
        return date;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isCenterLabel() {
        return isCenterLabel;
    }

    public void setCenterLabel(boolean centerLabel) {
        isCenterLabel = centerLabel;
    }

    public boolean isLunarCalendar() {
        return isLunarCalendar;
    }

    public ViewGroup getDecorView() {
        return decorView;
    }

    public int getTextColorOut() {
        return textColorOut;
    }

    public int getTextColorCenter() {
        return textColorCenter;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public MyWheelView.DividerType getDividerType() {
        return dividerType;
    }

    public float getLineSpacingMultiplier() {
        return lineSpacingMultiplier;
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }

    public String getLabel_year() {
        return label_year;
    }

    public void setLabel_year(String label_year) {
        this.label_year = label_year;
    }

    public String getLabel_month() {
        return label_month;
    }

    public void setLabel_month(String label_month) {
        this.label_month = label_month;
    }

    public String getLabel_day() {
        return label_day;
    }

    public void setLabel_day(String label_day) {
        this.label_day = label_day;
    }

    public String getLabel_hours() {
        return label_hours;
    }

    public void setLabel_hours(String label_hours) {
        this.label_hours = label_hours;
    }

    public String getLabel_mins() {
        return label_mins;
    }

    public void setLabel_mins(String label_mins) {
        this.label_mins = label_mins;
    }

    public String getLabel_seconds() {
        return label_seconds;
    }

    public void setLabel_seconds(String label_seconds) {
        this.label_seconds = label_seconds;
    }

    public int getXoffset_year() {
        return xoffset_year;
    }

    public void setXoffset_year(int xoffset_year) {
        this.xoffset_year = xoffset_year;
    }

    public int getXoffset_month() {
        return xoffset_month;
    }

    public void setXoffset_month(int xoffset_month) {
        this.xoffset_month = xoffset_month;
    }

    public int getXoffset_day() {
        return xoffset_day;
    }

    public void setXoffset_day(int xoffset_day) {
        this.xoffset_day = xoffset_day;
    }

    public int getXoffset_hours() {
        return xoffset_hours;
    }

    public void setXoffset_hours(int xoffset_hours) {
        this.xoffset_hours = xoffset_hours;
    }

    public int getXoffset_mins() {
        return xoffset_mins;
    }

    public void setXoffset_mins(int xoffset_mins) {
        this.xoffset_mins = xoffset_mins;
    }

    public int getXoffset_seconds() {
        return xoffset_seconds;
    }

    public void setXoffset_seconds(int xoffset_seconds) {
        this.xoffset_seconds = xoffset_seconds;
    }

    public MyBuilder isCenterLabel(boolean isCenterLabel) {
        this.isCenterLabel = isCenterLabel;
        return this;
    }


    public MyTimePicker build() {
        return new MyTimePicker(this);
    }
}