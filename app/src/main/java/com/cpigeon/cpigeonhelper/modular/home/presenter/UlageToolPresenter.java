package com.cpigeon.cpigeonhelper.modular.home.presenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.cpigeon.cpigeonhelper.utils.CommonUitls;

/**
 * Created by Administrator on 2018/1/23.
 */

public class UlageToolPresenter {

    /**
     * 设置经度纬度  度数监听
     * tag: 1 经度度数
     * tag: 2 纬度度数
     * tag: 3 分数
     * tag: 4 秒数
     */
    public static TextWatcher setLoLaSListener(Context mContext, EditText mEtLoLaD, int tag, EditText nextEditText) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) return;
                if (s.toString().equals(".")) {
                    mEtLoLaD.setText("0.");
                    try {
                        mEtLoLaD.setSelection(mEtLoLaD.getText().toString().length());//光标移动到最后的位置
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                double loD = Double.valueOf(s.toString());
                switch (tag) {
                    case 1:
                        if (loD >= 180) {
                            CommonUitls.showSweetDialog(mContext, "经度不能超过180");
                            mEtLoLaD.setText(s.toString().substring(0, s.toString().length() - 1));
                            mEtLoLaD.setSelection(mEtLoLaD.getText().toString().length());//光标移动到最后的位置
                        }
                        break;
                    case 2:
                        if (loD >= 90) {
                            CommonUitls.showSweetDialog(mContext, "纬度不能超过90");
                            mEtLoLaD.setText(s.toString().substring(0, s.toString().length() - 1));
                            mEtLoLaD.setSelection(mEtLoLaD.getText().toString().length());//光标移动到最后的位置
                        }
                        break;
                    case 3:
                        if (loD >= 60) {
                            CommonUitls.showSweetDialog(mContext, "分数不能超过60");
                            mEtLoLaD.setText(s.toString().substring(0, s.toString().length() - 1));
                            mEtLoLaD.setSelection(mEtLoLaD.getText().toString().length());//光标移动到最后的位置
                        }
                        break;
                    case 4:
                        if (s.toString().length() == 3 && s.toString().indexOf(".") == -1) {
                            //自动
                            String zh = s.toString().substring(s.toString().length() - 1, s.toString().length());//最后一个字符
                            mEtLoLaD.setText(s.toString().substring(0, s.toString().length() - 1) + "." + zh);
                            mEtLoLaD.setSelection(mEtLoLaD.getText().toString().length());//光标移动到最后的位置
                        }

                        if (Double.valueOf(mEtLoLaD.getText().toString()) >= 60) {
                            CommonUitls.showSweetDialog(mContext, "秒数不能超过60");
                            mEtLoLaD.setText(s.toString().substring(0, s.toString().length() - 1));
                            mEtLoLaD.setSelection(mEtLoLaD.getText().toString().length());//光标移动到最后的位置
                            return;
                        }

                        break;
                }

                //跳转到下一行
                switch (tag) {
                    case 1:
                        if (mEtLoLaD.getText().length() == 3) {
                            Log.d("xiaohls", "afterTextChanged: 111");
                            nextEditText.requestFocus();
                        }
                        break;
                    case 2:
                        if (mEtLoLaD.getText().length() == 2) {
                            Log.d("xiaohls", "afterTextChanged: 222");
                            nextEditText.requestFocus();
                        }
                        break;
                    case 3:
                        if (mEtLoLaD.getText().length() == 2) {
                            Log.d("xiaohls", "afterTextChanged: 333");
                            nextEditText.requestFocus();
                        }
                        break;
                    case 4:
                        if (mEtLoLaD.getText().length() == 5) {
                            Log.d("xiaohls", "afterTextChanged: 444");
                            if (mEtLoLaD != nextEditText) {
                                nextEditText.requestFocus();
                            }
                        }
                        break;
                }
            }
        };
    }

    //经纬度字符串拼接
    public static double strPj(EditText et1, EditText et2, EditText et3) {

        String str1 = String.valueOf(et1.getText().toString() + "." + et2.getText().toString());

        if (et3.getText().toString().indexOf(".") != -1) {
            //包含小数点
            str1 = String.valueOf(str1 + et3.getText().toString().replace(".", ""));

        } else {
            //不包含小数点
            str1 = String.valueOf(str1 + et3.getText().toString());
        }

        return Double.valueOf(str1);
    }

}
