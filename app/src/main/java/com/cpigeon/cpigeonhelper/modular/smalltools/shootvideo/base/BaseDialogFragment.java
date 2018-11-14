package com.cpigeon.cpigeonhelper.modular.smalltools.shootvideo.base;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.ScreenTool;
import com.yanzhenjie.loading.LoadingView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/8/6.
 */

public abstract class BaseDialogFragment extends DialogFragment {


    protected LoadingView progressView;
    ViewGroup mRootView;

    public BaseDialogFragment(){}
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);g
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        if(getLayoutView() == null){
            dialog.setContentView(getLayoutRes());
        }else {
            dialog.setContentView(getLayoutView());
        }
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.drawable.shape_bg_corner_3);
        //window.setWindowAnimations(R.style.AnimBottomDialog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
//        lp.width = (ScreenTool.getScreenWidth(dialog.getContext()) / 4) * 3;

        lp.width =  WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        initView(dialog);


        initProgressLayout(dialog);

        return dialog;
    }

    @LayoutRes
    protected abstract int  getLayoutRes();

    protected View getLayoutView(){
        return null;
    }

    protected abstract void initView(Dialog dialog);

    protected void initLayout(Window window, WindowManager.LayoutParams lp){

    }

    public void show(FragmentManager manager){
        show(manager, "dialog");
    }

    public void hide(){
       this.dismiss();
    }


    private void initProgressLayout(Dialog dialog) {
        mRootView = (ViewGroup)dialog.getWindow().getDecorView();
        if (progressView == null) {
            progressView = new LoadingView(dialog.getContext());
        }
        mRootView.addView(progressView);
    }

}
