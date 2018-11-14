package com.cpigeon.cpigeonhelper.modular.saigetong.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SGTUserInfo;
import com.cpigeon.cpigeonhelper.ui.SaActionSheetDialog;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/19.
 */

public class SGTPresenter2 {

    public static void initChart(Context mContext, PieChart pieChart, String cenStr, int colorId, float scale1, float scale2) {

        pieChart.clear();

        // 设置饼图是否接收点击事件，默认为true
        pieChart.setTouchEnabled(true);
        //设置饼图是否使用百分比
        pieChart.setUsePercentValues(true);
        //设置饼图右下角的文字描述
        pieChart.setDescription("");
//        //设置饼图右下角的文字大小
//        pieChart.setDescriptionTextSize(16);

        //是否显示圆盘中间文字，默认显示
        pieChart.setDrawCenterText(true);
        //设置圆盘中间文字
        pieChart.setCenterText(cenStr);
        //设置圆盘中间文字的大小
        pieChart.setCenterTextSize(12);
        //设置圆盘中间文字的颜色
        pieChart.setCenterTextColor(mContext.getResources().getColor(R.color.color_262626));
        //设置圆盘中间文字的字体
        pieChart.setCenterTextTypeface(Typeface.DEFAULT);

        //设置中间圆盘的颜色
        pieChart.setHoleColor(Color.WHITE);
        //设置中间圆盘的半径,值为所占饼图的百分比
        pieChart.setHoleRadius(86);

//        //设置中间透明圈的半径,值为所占饼图的百分比
        pieChart.setTransparentCircleRadius(0);
//
        //是否显示饼图中间空白区域，默认显示
        pieChart.setDrawHoleEnabled(true);
//        //设置圆盘是否转动，默认转动
//        pieChart.setRotationEnabled(true);
//        //设置初始旋转角度
//        pieChart.setLegendAngle(0);
        pieChart.setRotationEnabled(false);
        //设置比例图
        Legend mLegend = pieChart.getLegend();
        mLegend.setEnabled(false);//是否启动图例
        //设置比例图显示在饼图的哪个位置
//        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        //设置比例图的形状，默认是方形,可为方形、圆形、线性
//        mLegend.setForm(Legend.LegendForm.CIRCLE);
//        mLegend.setXEntrySpace(7f);
//        mLegend.setYEntrySpace(5f);


        //设置X轴动画
        pieChart.animateX(1800);
//        //设置y轴动画
//        pieChart.animateY(1800);
//        //设置xy轴一起的动画
//        pieChart.animateXY(1800, 1800);

        //绑定数据
        bindData(mContext, 2, pieChart, colorId, scale1, scale2);
//
//        // 设置一个选中区域监听
//        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                Toast.makeText(SGTInfoActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
    }

    /**
     * @param count 分成几部分
     */
    public static void bindData(Context mContext, int count, PieChart pieChart, int colorId, float scale1, float scale2) {
        /**
         * nameList用来表示每个饼块上的文字内容
         * 如：部分一，部分二，部分三
         */
        ArrayList<String> nameList = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            nameList.add("部分" + (i + 1));
        }
        /**
         * valueList将一个饼形图分成三部分，各个区域的百分比的值
         * Entry构造函数中
         * 第一个值代表所占比例，
         * 第二个值代表区域位置
         * （可以有第三个参数，表示携带的数据object）这里没用到
         */
        ArrayList<PieEntry> valueList = new ArrayList<PieEntry>();
        valueList.add(new PieEntry(scale1, 0));
        valueList.add(new PieEntry(scale2, 1));

        //显示在比例图上
//        PieDataSet dataSet = new PieDataSet(valueList, "不同颜色代表的含义");
        PieDataSet dataSet = new PieDataSet(valueList, "");
        //设置个饼状图之间的距离
//        dataSet.setSliceSpace(3f);
        // 部分区域被选中时多出的长度
        dataSet.setSelectionShift(0f);

        // 设置饼图各个区域颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(mContext.getResources().getColor(R.color.color_sgt_gray));
        colors.add(colorId);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        //设置百分比  20%   false  显示 , true  不显示
        data.setDrawValues(true);
        //设置以百分比显示
        data.setValueFormatter(new PercentFormatter());
        //区域文字的大小
        data.setValueTextSize(11f);
        //设置区域文字的颜色
        data.setValueTextColor(Color.WHITE);
        //设置区域文字的字体
        data.setValueTypeface(Typeface.DEFAULT);

        pieChart.setData(data);
//        pieChart.setdata

        //设置是否显示区域文字内容
//        pieChart.setDrawSliceText(pieChart.isDrawSliceTextEnabled());
        //设置是否显示区域百分比的值
        for (IDataSet<?> set : pieChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }


    public static void initChart1(Context mContext, PieChart pieChart, SGTUserInfo mSGTUserInfo) {

        pieChart.clear();

        // 设置饼图是否接收点击事件，默认为true
        pieChart.setTouchEnabled(true);
        //设置饼图是否使用百分比
        pieChart.setUsePercentValues(true);
        //设置饼图右下角的文字描述
        pieChart.setDescription("");
//        //设置饼图右下角的文字大小
//        pieChart.setDescriptionTextSize(16);

//        //是否显示圆盘中间文字，默认显示
//        pieChart.setDrawCenterText(true);
//        //设置圆盘中间文字
//        pieChart.setCenterText("我在中间");
//        //设置圆盘中间文字的大小
//        pieChart.setCenterTextSize(20);
//        //设置圆盘中间文字的颜色
//        pieChart.setCenterTextColor(Color.WHITE);
//        //设置圆盘中间文字的字体
//        pieChart.setCenterTextTypeface(Typeface.DEFAULT);
//
//        //设置中间圆盘的颜色
//        pieChart.setHoleColor(Color.GREEN);
//        //设置中间圆盘的半径,值为所占饼图的百分比
//        pieChart.setHoleRadius(20);
//
//        //设置中间透明圈的半径,值为所占饼图的百分比
//        pieChart.setTransparentCircleRadius(40);
//
        //是否显示饼图中间空白区域，默认显示
        pieChart.setDrawHoleEnabled(false);
//        //设置圆盘是否转动，默认转动
//        pieChart.setRotationEnabled(true);
//        //设置初始旋转角度
//        pieChart.setLegendAngle(0);
        pieChart.setRotationEnabled(false);
//        //设置比例图
        Legend mLegend = pieChart.getLegend();
        mLegend.setEnabled(false);//是否显示比例尺
//        //设置比例图显示在饼图的哪个位置
//        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        //设置比例图的形状，默认是方形,可为方形、圆形、线性
//        mLegend.setForm(Legend.LegendForm.CIRCLE);
//        mLegend.setXEntrySpace(7f);
//        mLegend.setYEntrySpace(5f);

        //设置X轴动画
        pieChart.animateX(1800);
//        //设置y轴动画
//        pieChart.animateY(1800);
//        //设置xy轴一起的动画
//        pieChart.animateXY(1800, 1800);

        //绑定数据
        bindData1(mContext, 4, pieChart, mSGTUserInfo);

        // 设置一个选中区域监听
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e.getData() != null) {
//                    CommonUitls.showSweetDialog(mContext, "" + e.toString() + "  -->" + e.getData().toString());
                    CommonUitls.showSweetDialog(mContext, e.getData().toString() + "\n" + CommonUitls.strTwo(e.getY()) + "%");
                } else {
                    CommonUitls.showSweetDialog(mContext, "" + e.toString() + "  -->");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private static String TAG = "xiaohl";

    /**
     * @param count 分成几部分
     */
    public static void bindData1(Context mContext, int count, PieChart pieChart, SGTUserInfo mSGTUserInfo) {
        /**
         * nameList用来表示每个饼块上的文字内容
         * 如：部分一，部分二，部分三
         */
        ArrayList<String> nameList = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            nameList.add("部分" + (i + 1));
        }

        /**
         * valueList将一个饼形图分成三部分，各个区域的百分比的值
         * Entry构造函数中
         * 第一个值代表所占比例，
         * 第二个值代表区域位置
         * （可以有第三个参数，表示携带的数据object）这里没用到
         */

        int num1 = Integer.valueOf(mSGTUserInfo.getPiccountrp().getCount());
        int num2 = Integer.valueOf(mSGTUserInfo.getPiccountrc().getCount());
        int num3 = Integer.valueOf(mSGTUserInfo.getPiccountsf().getCount());
        int num4 = Integer.valueOf(mSGTUserInfo.getPiccountbs().getCount());
        int num5 = Integer.valueOf(mSGTUserInfo.getPiccounthj().getCount());

        float dataSize = num1 + num2 + num3 + num4 + num5;

//        Log.d(TAG, "bindData1: " + dataSize + "   " + num1 + "   " + num2 + "   " + num3 + "   " + num4);
//        Log.d(TAG, "bindData2: " + 100 * (num1 / dataSize) + "   " + 100 * (num2 / dataSize) + "   " + 100 * (num3 / dataSize) + "   " + 100 * (num4 / dataSize));

        ArrayList<PieEntry> valueList = new ArrayList<PieEntry>();

        if (dataSize != 0) {
            valueList.add(new PieEntry(100 * (num1 / dataSize), "", mSGTUserInfo.getPiccountrp().getTag()));
            valueList.add(new PieEntry(100 * (num2 / dataSize), "", mSGTUserInfo.getPiccountrc().getTag()));
            valueList.add(new PieEntry(100 * (num3 / dataSize), "", mSGTUserInfo.getPiccountsf().getTag()));
            valueList.add(new PieEntry(100 * (num4 / dataSize), "", mSGTUserInfo.getPiccountbs().getTag()));
            valueList.add(new PieEntry(100 * (num5 / dataSize), "", mSGTUserInfo.getPiccounthj().getTag()));
        } else {
            valueList.add(new PieEntry(100, "", "暂无数据"));
        }


        //显示在比例图上
//        PieDataSet dataSet = new PieDataSet(valueList, "不同颜色代表的含义");
        PieDataSet dataSet = new PieDataSet(valueList, "");
        //设置个饼状图之间的距离
//        dataSet.setSliceSpace(12f);
        // 部分区域被选中时多出的长度
        dataSet.setSelectionShift(5f);

        // 设置饼图各个区域颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(mContext.getResources().getColor(R.color.color_sgt_gray));
        colors.add(mContext.getResources().getColor(R.color.color_sgt_red));
        colors.add(mContext.getResources().getColor(R.color.color_sgt_cyan));
        colors.add(mContext.getResources().getColor(R.color.color_sgt_blue));
        colors.add(mContext.getResources().getColor(R.color.color_sgt_purple));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        //设置百分比  20%   false  显示 , true  不显示
        data.setDrawValues(true);
        //设置以百分比显示
        data.setValueFormatter(new PercentFormatter());
        //区域文字的大小
        data.setValueTextSize(11f);
        //设置区域文字的颜色
        data.setValueTextColor(Color.WHITE);
        //设置区域文字的字体
        data.setValueTypeface(Typeface.DEFAULT);

        pieChart.setData(data);
//        pieChart.setdata

        //设置是否显示区域文字内容
//        pieChart.setDrawSliceText(pieChart.isDrawSliceTextEnabled());
        //设置是否显示区域百分比的值
        for (IDataSet<?> set : pieChart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    /**
     * 可容羽数
     *
     * @param mContext
     */
    public static AlertDialog initSGTKrysDialog(Context mContext, SGTPresenter mSGTPresenter) {

        TextView title = new TextView(mContext);
        title.setText("设置可容羽数");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(mContext.getResources().getColor(R.color.black));
        title.setTextSize(18);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog dialog = builder.create();

        dialog.setCustomTitle(title);

        LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_sgt_krys_dialog, null);
        Button dialogDetermine = (Button) dialogLayout.findViewById(R.id.dialog_determine);
        EditText etKrys = (EditText) dialogLayout.findViewById(R.id.et_krys);

        dialogDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mSGTPresenter.setGPKrys(etKrys.getText().toString());
            }
        });

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setView(dialogLayout);

        //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }


    public static SaActionSheetDialog selectFootDialog(Context myContext, SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener2) {
        SaActionSheetDialog dialogFoot = new SaActionSheetDialog(myContext).builder();
        dialogFoot.addSheetItem("足环在左脚", onSheetItemClickListener2);
        dialogFoot.addSheetItem("足环在右脚", onSheetItemClickListener2);

        return dialogFoot;
    }


    public static SaActionSheetDialog selectFootDialog2(SaActionSheetDialog dialogFoot, Context myContext, SaActionSheetDialog.OnSheetItemClickListener onSheetItemClickListener2) {
        dialogFoot = new SaActionSheetDialog(myContext).builder();
        dialogFoot.addSheetItem("足环在左脚", onSheetItemClickListener2);
        dialogFoot.addSheetItem("足环在右脚", onSheetItemClickListener2);

        return dialogFoot;
    }


}
