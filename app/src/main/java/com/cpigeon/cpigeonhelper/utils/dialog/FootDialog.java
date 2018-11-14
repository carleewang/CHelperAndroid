package com.cpigeon.cpigeonhelper.utils.dialog;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.DateTool;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.Lists;

import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by Administrator on 2018/4/10.
 */

public class FootDialog {


    //两个足环
    public static CustomAlertDialog initFootDialogTwo(Activity mContext, String hintCen, int inputType, DialogClickListenerTwo mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_foot_select_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            EditText ll1_et1 = (EditText) dialogLayout.findViewById(R.id.ll1_et1);
            EditText ll1_et2 = (EditText) dialogLayout.findViewById(R.id.ll1_et2);
            EditText ll1_et3 = (EditText) dialogLayout.findViewById(R.id.ll1_et3);

            EditText ll2_et1 = (EditText) dialogLayout.findViewById(R.id.ll2_et1);
            EditText ll2_et2 = (EditText) dialogLayout.findViewById(R.id.ll2_et2);
            EditText ll2_et3 = (EditText) dialogLayout.findViewById(R.id.ll2_et3);

            //初始化提示
            if (!hintCen.isEmpty()) {
                String[] towStr = hintCen.split(",");
                if (towStr.length == 2) {

                    String[] oneStr1 = towStr[0].split("-");
                    if (oneStr1.length == 3) {
                        ll1_et1.setText(oneStr1[0]);
                        ll1_et2.setText(oneStr1[1]);
                        ll1_et3.setText(oneStr1[2]);
                    }

                    String[] oneStr2 = towStr[1].split("-");
                    if (oneStr2.length == 3) {
                        ll2_et1.setText(oneStr2[0]);
                        ll2_et2.setText(oneStr2[1]);
                        ll2_et3.setText(oneStr2[2]);
                    }
                }
            }

            TextView dialog_determine = (TextView) dialogLayout.findViewById(R.id.dialog_determine);

            ll1_et1.setOnClickListener(view -> {
                showPicker(mContext, ll1_et1);
            });

            ll2_et1.setOnClickListener(view -> {
                showPicker(mContext, ll2_et1);
            });

            dialog_determine.setOnClickListener(view -> {
                if (ll1_et1.getText().toString().isEmpty() || ll1_et2.getText().toString().isEmpty() || ll1_et3.getText().toString().isEmpty()
                        || ll2_et1.getText().toString().isEmpty() || ll2_et2.getText().toString().isEmpty() || ll2_et3.getText().toString().isEmpty()) {
                    CommonUitls.showToast(mContext, "请输入正确的足环号码！");
                    dialog.dismiss();
                    return;
                }

                mDialogClickListener.onDialogClickListener(dialog_determine, dialog,
                        ll1_et1.getText().toString() + "-" + ll1_et2.getText().toString() + "-" + ll1_et3.getText().toString(),
                        ll2_et1.getText().toString() + "-" + ll2_et2.getText().toString() + "-" + ll2_et3.getText().toString());

            });

            dialog.setContentView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
//            toggleInput(mContext);
            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void showPicker(Activity context, EditText et) {
        OptionPicker picker = new OptionPicker(context, getDates());
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setSelectedIndex(datePosition);
        picker.setCycleDisable(true);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                datePosition = index;
                et.setText(item);
            }
        });
        picker.show();
    }

    private static int datePosition = 0;

    private static List<String> getDates() {
        List<String> dateList = Lists.newArrayList();
        String year = DateTool.format(System.currentTimeMillis(), DateTool.FORMAT_YYYY);

        int yearInt = Integer.parseInt(year);
        for (int i = yearInt; i >= yearInt - 5; i--) {
            dateList.add(String.valueOf(i));
        }
        return dateList;
    }


    //单个足环
    public static CustomAlertDialog initFootDialogOne(Activity mContext, String hintCen, int inputType, DialogClickListenerOne mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_foot_select_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout ll_z2 = (LinearLayout) dialogLayout.findViewById(R.id.ll_z2);

            ll_z2.setVisibility(View.GONE);

            EditText ll1_et1 = (EditText) dialogLayout.findViewById(R.id.ll1_et1);
            EditText ll1_et2 = (EditText) dialogLayout.findViewById(R.id.ll1_et2);
            EditText ll1_et3 = (EditText) dialogLayout.findViewById(R.id.ll1_et3);


            if (!hintCen.isEmpty()) {
                String[] strDefault = hintCen.split("-");
                if (strDefault.length == 3) {
                    ll1_et1.setText(strDefault[0]);
                    ll1_et2.setText(strDefault[1]);
                    ll1_et3.setText(strDefault[2]);
                }
            }

            TextView dialog_determine = (TextView) dialogLayout.findViewById(R.id.dialog_determine);

            ll1_et1.setOnClickListener(view -> {
                showPicker(mContext, ll1_et1);
            });

            dialog_determine.setOnClickListener(view -> {
                if (ll1_et1.getText().toString().isEmpty() || ll1_et2.getText().toString().isEmpty() || ll1_et3.getText().toString().isEmpty()) {
                    CommonUitls.showToast(mContext, "请输入正确的足环号码！");
                    return;
                }

                mDialogClickListener.onDialogClickListener(dialog_determine, dialog,
                        ll1_et1.getText().toString() + "-" + ll1_et2.getText().toString() + "-" + ll1_et3.getText().toString());

            });

            dialog.setContentView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    //单个足环 前半段只能是当年
    public static CustomAlertDialog initFootDialogOne1(Activity mContext, String hintCen, int inputType, DialogClickListenerOne mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_foot_select_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout ll_z2 = (LinearLayout) dialogLayout.findViewById(R.id.ll_z2);

            ll_z2.setVisibility(View.GONE);

            EditText ll1_et1 = (EditText) dialogLayout.findViewById(R.id.ll1_et1);
            EditText ll1_et2 = (EditText) dialogLayout.findViewById(R.id.ll1_et2);
            EditText ll1_et3 = (EditText) dialogLayout.findViewById(R.id.ll1_et3);


            ll1_et1.setRawInputType(Configuration.KEYBOARD_QWERTY);
            ll1_et2.setRawInputType(Configuration.KEYBOARD_QWERTY);
            ll1_et3.setRawInputType(Configuration.KEYBOARD_QWERTY);


            if (!hintCen.isEmpty()) {
                String[] strDefault = hintCen.split("-");
                if (strDefault.length == 3) {
                    ll1_et1.setText(strDefault[0]);
                    ll1_et2.setText(strDefault[1]);
                    ll1_et3.setText(strDefault[2]);
                }
            }

            TextView dialog_determine = (TextView) dialogLayout.findViewById(R.id.dialog_determine);


            ll1_et1.setText(DateUtils.getStringDateY());

            dialog_determine.setOnClickListener(view -> {
                if (ll1_et1.getText().toString().isEmpty() || ll1_et2.getText().toString().isEmpty() || ll1_et3.getText().toString().isEmpty()) {
                    CommonUitls.showToast(mContext, "请输入正确的足环号码！");
                    return;
                }

                mDialogClickListener.onDialogClickListener(dialog_determine, dialog,
                        ll1_et1.getText().toString() + "-" + ll1_et2.getText().toString() + "-" + ll1_et3.getText().toString());

            });

            dialog.setContentView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface DialogClickListenerTwo {
        void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str1, String str2);
    }

    public interface DialogClickListenerOne {
        void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String str);
    }
}
