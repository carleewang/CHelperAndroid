package com.cpigeon.cpigeonhelper.modular.home.view.frigment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.child_association.ChildFoodAdminListActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.child_association.ChildMemberListActivity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.UserType;

/**
 * Created by Administrator on 2017/1/7.
 * 支付密码输入Fragment
 */

public class ChildAssociationDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.layout_child_association_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.AnimBottomDialog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2 / 5;
        window.setAttributes(lp);
        initView(dialog);

        return dialog;
    }

    private void initView(Dialog dialog) {

        LinearLayout ll_btn1 = dialog.findViewById(R.id.ll_btn1);
        LinearLayout ll_btn2 = dialog.findViewById(R.id.ll_btn2);
        LinearLayout ll_btn3 = dialog.findViewById(R.id.ll_btn3);

        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);

        ll_btn1.setOnClickListener(view -> {
            this.dismiss();
            //会员信息
            Intent intent = new Intent(getActivity(), ChildMemberListActivity.class);
            intent.putExtra("region",userTypeApiResponse.getData().getShenggehui());
            startActivity(intent);
        });


        ll_btn2.setOnClickListener(view -> {
            this.dismiss();
            //足环管理
            Intent intent = new Intent(getActivity(), ChildFoodAdminListActivity.class);
            intent.putExtra("region",userTypeApiResponse.getData().getShenggehui());
            startActivity(intent);

        });

        ll_btn3.setOnClickListener(view -> {
            this.dismiss();
        });

        tv_cancel.setOnClickListener(view -> {
            this.dismiss();
        });

    }

    private ApiResponse<UserType> userTypeApiResponse;
    public void  setInitData(ApiResponse<UserType> userTypeApiResponse){
        this.userTypeApiResponse = userTypeApiResponse;
    }

}
