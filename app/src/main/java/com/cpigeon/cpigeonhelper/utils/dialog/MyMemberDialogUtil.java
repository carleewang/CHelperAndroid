package com.cpigeon.cpigeonhelper.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.home.presenter.UlageToolPresenter;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.GPSFormatUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2018/3/28.
 */

public class MyMemberDialogUtil {

    //我的会员 输入框  输入内容不能为空
    public static CustomAlertDialog initInputDialog(Activity mContext, String mrStr, String strTitle, String hintCen, int inputType, DialogClickListener mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_my_member_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView tv_title = (TextView) dialogLayout.findViewById(R.id.tv_title);//标题
            tv_title.setText(strTitle);

            TextView tv_hint_cen = (TextView) dialogLayout.findViewById(R.id.tv_hint_cen);//提示内容
            tv_hint_cen.setText(hintCen);
            EditText et_shuru = (EditText) dialogLayout.findViewById(R.id.et_shuru);//输入框
            et_shuru.setText(mrStr);
            et_shuru.setInputType(inputType);
            et_shuru.setSelection(et_shuru.getText().toString().length());//光标移动到最后的位置
            TextView dialogDetermine = (TextView) dialogLayout.findViewById(R.id.dialog_determine);
            dialogDetermine.setOnClickListener(view -> {
                if (et_shuru.getText().toString().isEmpty()) {
                    dialog.dismiss();
                    return;
                }
                mDialogClickListener.onDialogClickListener(view, dialog, et_shuru.getText().toString());
            });

//            dialog.setView(dialogLayout);

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


    //我的会员 输入框  输入内容可以为空
    public static CustomAlertDialog initInputDialog1(Activity mContext, String mrStr, String strTitle, String hintCen, int inputType, DialogClickListener mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_my_member_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView tv_title = (TextView) dialogLayout.findViewById(R.id.tv_title);//标题
            tv_title.setText(strTitle);

            TextView tv_hint_cen = (TextView) dialogLayout.findViewById(R.id.tv_hint_cen);//提示内容
            tv_hint_cen.setText(hintCen);
            EditText et_shuru = (EditText) dialogLayout.findViewById(R.id.et_shuru);//输入框
            et_shuru.setText(mrStr);
            et_shuru.setInputType(inputType);
            et_shuru.setSelection(et_shuru.getText().toString().length());//光标移动到最后的位置
            TextView dialogDetermine = (TextView) dialogLayout.findViewById(R.id.dialog_determine);
            dialogDetermine.setOnClickListener(view -> {

                mDialogClickListener.onDialogClickListener(view, dialog, et_shuru.getText().toString());
            });

//            dialog.setView(dialogLayout);

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

    public interface DialogClickListener {
        void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr);
    }


    //我的会员 经度纬度输入框
    public static CustomAlertDialog initInputDialogLola(Activity mContext, String mrStr, String strTitle, String hintCen, int inputType, SweetAlertDialog errSweetAlertDialog,
                                                        DialogClickListenerLoLa mDialogClickListener) {

        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_lola_input_dialog, null);
            dialogLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            EditText lo1 = (EditText) dialogLayout.findViewById(R.id.et_lo1);
            EditText lo2 = (EditText) dialogLayout.findViewById(R.id.et_lo2);
            EditText lo3 = (EditText) dialogLayout.findViewById(R.id.et_lo3);

            EditText la1 = (EditText) dialogLayout.findViewById(R.id.et_la1);
            EditText la2 = (EditText) dialogLayout.findViewById(R.id.et_la2);
            EditText la3 = (EditText) dialogLayout.findViewById(R.id.et_la3);


            lo1.addTextChangedListener(UlageToolPresenter.setLoLaSListener(mContext, lo1, 1, lo2));
            lo2.addTextChangedListener(UlageToolPresenter.setLoLaSListener(mContext, lo2, 3, lo3));
            lo3.addTextChangedListener(UlageToolPresenter.setLoLaSListener(mContext, lo3, 4, la1));

            la1.addTextChangedListener(UlageToolPresenter.setLoLaSListener(mContext, la1, 2, la2));
            la2.addTextChangedListener(UlageToolPresenter.setLoLaSListener(mContext, la2, 3, la3));
            la3.addTextChangedListener(UlageToolPresenter.setLoLaSListener(mContext, la3, 4, la3));


            try {
                if (!mrStr.isEmpty()) {

                    String[] towStr = mrStr.split(" ");
                    if (towStr.length == 2) {
                        //获取经度
                        String oneStr1 = towStr[0].replace("东经：", "");

                        lo1.setText(GPSFormatUtils.getStrToD(oneStr1));//设置度
                        lo2.setText(GPSFormatUtils.getStrToM(oneStr1));//设置分
                        lo3.setText(GPSFormatUtils.getStrToS(oneStr1));//设置秒

                        //获取纬度
                        String oneStr2 = towStr[1].replace("北纬：", "");

                        la1.setText(GPSFormatUtils.getStrToD(oneStr2));//设置度
                        la2.setText(GPSFormatUtils.getStrToM(oneStr2));//设置分
                        la3.setText(GPSFormatUtils.getStrToS(oneStr2));//设置秒
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView dialogDetermine = (TextView) dialogLayout.findViewById(R.id.dialog_determine);
            dialogDetermine.setOnClickListener(view -> {

                if (lo1.getText().toString().isEmpty() || lo2.getText().toString().isEmpty() || lo3.getText().toString().isEmpty() ||
                        la1.getText().toString().isEmpty() || la2.getText().toString().isEmpty() || la3.getText().toString().isEmpty()) {
                    SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "请将坐标填写完整", mContext);
                    return;
                }

                if (lo2.getText().toString().length() == 1) {
                    lo2.setText(String.valueOf("0" + lo2.getText().toString()));
                }

                if (la2.getText().toString().length() == 1) {
                    la2.setText(String.valueOf("0" + la2.getText().toString()));
                }

                mDialogClickListener.onDialogClickListenerLoLa(view, dialog,
                        lo1.getText().toString() + "." + lo2.getText().toString() + lo3.getText().toString().replace(".", ""),
                        la1.getText().toString() + "." + la2.getText().toString() + la3.getText().toString().replace(".", ""));
            });

//            dialog.setView(dialogLayout);

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

    public interface DialogClickListenerLoLa {
        void onDialogClickListenerLoLa(View viewSure, CustomAlertDialog dialog, String lo, String la);
    }

    public static void toggleInput(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
