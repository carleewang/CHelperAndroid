package com.cpigeon.cpigeonhelper.utils.time_picker;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;
import com.cpigeon.cpigeonhelper.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/31.
 */

public class MyTimePicker extends BasePickerView implements View.OnClickListener {
    private int layoutRes;
    private CustomListener customListener;

    MyWheelTime wheelTime; //自定义控件
    private Button btnSubmit, btnCancel; //确定、取消按钮
    private TextView tvTitle;//标题
    private OnTimeSelectListener timeSelectListener;//回调接口
    private int gravity = Gravity.CENTER;//内容显示位置 默认居中
    private boolean[] type;// 显示类型

    private String Str_Submit;//确定按钮字符串
    private String Str_Cancel;//取消按钮字符串
    private String Str_Title;//标题字符串

    private int Color_Submit;//确定按钮颜色
    private int Color_Cancel;//取消按钮颜色
    private int Color_Title;//标题颜色

    private int Color_Background_Wheel;//滚轮背景颜色
    private int Color_Background_Title;//标题背景颜色

    private int Size_Submit_Cancel;//确定取消按钮大小
    private int Size_Title;//标题字体大小
    private int Size_Content = 10;//内容字体大小

    private Calendar date;//当前选中时间
    private Calendar startDate;//开始时间
    private Calendar endDate;//终止时间
    private int startYear;//开始年份
    private int endYear;//结尾年份

    private boolean cyclic;//是否循环
    private boolean cancelable;//是否能取消
    private boolean isCenterLabel;//是否只显示中间的label
    private boolean isLunarCalendar;//是否显示农历

    private int textColorOut; //分割线以外的文字颜色
    private int textColorCenter; //分割线之间的文字颜色
    private int dividerColor; //分割线的颜色
    private int backgroundId; //显示时的外部背景色颜色,默认是灰色

    // 条目间距倍数 默认1.6
    private float lineSpacingMultiplier = 1.6F;
    private boolean isDialog;//是否是对话框模式
    private String label_year, label_month, label_day, label_hours, label_mins, label_seconds;
    private int xoffset_year, xoffset_month, xoffset_day, xoffset_hours, xoffset_mins, xoffset_seconds;
    private MyWheelView.DividerType dividerType;//分隔线类型

    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    //构造方法
    public MyTimePicker(MyBuilder builder) {
        super(builder.getContext());
        this.timeSelectListener = builder.getTimeSelectListener();
        this.gravity = builder.getGravity();
        this.type = builder.getType();
        this.Str_Submit = builder.getStr_Submit();
        this.Str_Cancel = builder.getStr_Cancel();
        this.Str_Title = builder.getStr_Title();
        this.Color_Submit = builder.getColor_Submit();
        this.Color_Cancel = builder.getColor_Cancel();
        this.Color_Title = builder.getColor_Title();
        this.Color_Background_Wheel = builder.getColor_Background_Wheel();
        this.Color_Background_Title = builder.getColor_Background_Title();
        this.Size_Submit_Cancel = builder.getSize_Submit_Cancel();
        this.Size_Title = builder.getSize_Title();
        this.Size_Content = builder.getSize_Content();
        this.startYear = builder.getStartYear();
        this.endYear = builder.getEndYear();
        this.startDate = builder.getStartDate();
        this.endDate = builder.getEndDate();
        this.date = builder.getDate();
        this.cyclic = builder.isCyclic();
        this.isCenterLabel = builder.isCenterLabel();
        this.isLunarCalendar = builder.isLunarCalendar();
        this.cancelable = builder.isCancelable();
        this.label_year = builder.getLabel_year();
        this.label_month = builder.getLabel_month();
        this.label_day = builder.getLabel_day();
        this.label_hours = builder.getLabel_hours();
        this.label_mins = builder.getLabel_mins();
        this.label_seconds = builder.getLabel_seconds();
        this.xoffset_year = builder.getXoffset_year();
        this.xoffset_month = builder.getXoffset_month();
        this.xoffset_day = builder.getXoffset_day();
        this.xoffset_hours = builder.getXoffset_hours();
        this.xoffset_mins = builder.getXoffset_mins();
        this.xoffset_seconds = builder.getXoffset_seconds();
        this.textColorCenter = builder.getTextColorCenter();
        this.textColorOut = builder.getTextColorOut();
        this.dividerColor = builder.getDividerColor();
        this.customListener = builder.getCustomListener();
        this.layoutRes = builder.getLayoutRes();
        this.lineSpacingMultiplier = builder.getLineSpacingMultiplier();
        this.isDialog = builder.isDialog();
        this.dividerType = builder.getDividerType();
        this.backgroundId = builder.getBackgroundId();
        this.decorView = builder.decorView;
        initView(builder.getContext());
    }



    private void initView(Context context) {
        setDialogOutSideCancelable(cancelable);
        initViews(backgroundId);
        init();
        initEvents();
        if (customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.my_pickerview_time, contentContainer);

            //顶部标题
            tvTitle = (TextView) findViewById(R.id.tvTitle);

            //确定和取消按钮
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            btnCancel = (Button) findViewById(R.id.btnCancel);

            btnSubmit.setTag(TAG_SUBMIT);
            btnCancel.setTag(TAG_CANCEL);

            btnSubmit.setOnClickListener(this);
            btnCancel.setOnClickListener(this);

            //设置文字
            btnSubmit.setText(TextUtils.isEmpty(Str_Submit) ? context.getResources().getString(R.string.pickerview_submit) : Str_Submit);
            btnCancel.setText(TextUtils.isEmpty(Str_Cancel) ? context.getResources().getString(R.string.pickerview_cancel) : Str_Cancel);
            tvTitle.setText(TextUtils.isEmpty(Str_Title) ? "" : Str_Title);//默认为空

            //设置文字颜色
            btnSubmit.setTextColor(Color_Submit == 0 ? pickerview_timebtn_nor : Color_Submit);
            btnCancel.setTextColor(Color_Cancel == 0 ? pickerview_timebtn_nor : Color_Cancel);
            tvTitle.setTextColor(Color_Title == 0 ? pickerview_topbar_title : Color_Title);

            //设置文字大小
            btnSubmit.setTextSize(Size_Submit_Cancel);
            btnCancel.setTextSize(Size_Submit_Cancel);
            tvTitle.setTextSize(Size_Title);
            RelativeLayout rv_top_bar = (RelativeLayout) findViewById(R.id.rv_topbar);
            rv_top_bar.setBackgroundColor(Color_Background_Title == 0 ? pickerview_bg_topbar : Color_Background_Title);
        } else {
            customListener.customLayout(LayoutInflater.from(context).inflate(layoutRes, contentContainer));
        }
        // 时间转轮 自定义控件
        LinearLayout timePickerView = (LinearLayout) findViewById(R.id.timepicker);

        timePickerView.setBackgroundColor(Color_Background_Wheel == 0 ? bgColor_default : Color_Background_Wheel);

        wheelTime = new MyWheelTime(timePickerView, type, gravity, Size_Content);
        wheelTime.setLunarCalendar(isLunarCalendar);

        if (startYear != 0 && endYear != 0 && startYear <= endYear) {
            setRange();
        }

        if (startDate != null && endDate != null) {
            if (startDate.getTimeInMillis() <= endDate.getTimeInMillis()) {

                setRangDate();
            }
        } else if (startDate != null && endDate == null) {
            setRangDate();
        } else if (startDate == null && endDate != null) {
            setRangDate();
        }

        setTime();
        wheelTime.setLabels(label_year, label_month, label_day, label_hours, label_mins, label_seconds);
        wheelTime.setTextXOffset(xoffset_year, xoffset_month, xoffset_day, xoffset_hours, xoffset_mins, xoffset_seconds);

        setOutSideCancelable(cancelable);
        wheelTime.setCyclic(cyclic);
        wheelTime.setDividerColor(dividerColor);
        wheelTime.setDividerType(dividerType);
        wheelTime.setLineSpacingMultiplier(lineSpacingMultiplier);
        wheelTime.setTextColorOut(textColorOut);
        wheelTime.setTextColorCenter(textColorCenter);
        wheelTime.isCenterLabel(isCenterLabel);
    }

    /**
     * 设置默认时间
     */
    public void setDate(Calendar date) {
        this.date = date;
        setTime();
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRange() {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);

    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRangDate() {
        wheelTime.setRangDate(startDate, endDate);
        //如果设置了时间范围
        if (startDate != null && endDate != null) {
            //判断一下默认时间是否设置了，或者是否在起始终止时间范围内
            if (date == null || date.getTimeInMillis() < startDate.getTimeInMillis()
                    || date.getTimeInMillis() > endDate.getTimeInMillis()) {
                date = startDate;
            }
        } else if (startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            date = startDate;
        } else if (endDate != null) {
            date = endDate;
        }
    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private void setTime() {
        int year, month, day, hours, minute, seconds;

        Calendar calendar = Calendar.getInstance();

        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DAY_OF_MONTH);
            hours = date.get(Calendar.HOUR_OF_DAY);
            minute = date.get(Calendar.MINUTE);
            seconds = date.get(Calendar.SECOND);
        }

        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_SUBMIT)) {
            returnData();
        }
        dismiss();
    }

    public void returnData() {
        if (timeSelectListener != null) {
            try {
                Date date = WheelTime.dateFormat.parse(wheelTime.getTime());

                timeSelectListener.onTimeSelect(date, clickView);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public void setLunarCalendar(boolean lunar) {
        try {
            int year, month, day, hours, minute, seconds;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(WheelTime.dateFormat.parse(wheelTime.getTime()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);

            wheelTime.setLunarCalendar(lunar);
            wheelTime.setLabels(label_year, label_month, label_day, label_hours, label_mins, label_seconds);
            wheelTime.setPicker(year, month, day, hours, minute, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isLunarCalendar() {
        return wheelTime.isLunarCalendar();
    }


    public interface OnTimeSelectListener {
        void onTimeSelect(Date date, View v);
    }

    @Override
    public boolean isDialog() {
        return isDialog;
    }
}
