package com.cpigeon.cpigeonhelper.utils.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;

/**
 * Created by Administrator on 2018/1/24.
 */

public class DialogUtil {


    public static AlertDialog initXiaohlDialog(Context mContext, String titleStr, String mrStr, int inputType, DialogClickListener mDialogClickListener) {

        try {
            TextView title = new TextView(mContext);
            title.setText(titleStr);
            title.setPadding(10, 38, 10, 10);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(mContext.getResources().getColor(R.color.black));
            title.setTextSize(18);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            AlertDialog dialog = builder.create();

            dialog.setCustomTitle(title);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_xiaohl_dialog, null);
            Button dialogDetermine = (Button) dialogLayout.findViewById(R.id.dialog_determine);
            Button dialog_cel = (Button) dialogLayout.findViewById(R.id.dialog_cel);
            EditText et_shuru = (EditText) dialogLayout.findViewById(R.id.et_shuru);
            et_shuru.setText(mrStr);
            et_shuru.setInputType(inputType);

            dialogDetermine.setOnClickListener(view -> {
                mDialogClickListener.onDialogClickListener(view, null, dialog, et_shuru.getText().toString());
            });

            dialog_cel.setOnClickListener(view -> {
                mDialogClickListener.onDialogClickListener(null, view, dialog, et_shuru.getText().toString());
            });

            dialog.setView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);

            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface DialogClickListener {
        void onDialogClickListener(View viewSure, View viewCel, AlertDialog dialog, String etStr);
    }
}
