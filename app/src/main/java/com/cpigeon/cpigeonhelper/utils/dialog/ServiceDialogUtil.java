package com.cpigeon.cpigeonhelper.utils.dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.CreateMessageOrderFragment;
import com.cpigeon.cpigeonhelper.modular.order.model.bean.ServesInfoEntity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OpenXGTServiceActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.OrderPlayActivity;
import com.cpigeon.cpigeonhelper.modular.order.view.activity.RenewalUpgradeActivity;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.service.OrderService;

/**
 * 服务到期时间
 * Created by Administrator on 2018/3/30.
 */

public class ServiceDialogUtil {

    public static CustomAlertDialog initServiceTimelDialog(Activity mContext, ServesInfoEntity mServesInfoEntity) {
        try {
            CustomAlertDialog dialog = new CustomAlertDialog(mContext);

            LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_service_time_dialog, null);


            //鸽运通
            LinearLayout ll_gyt = (LinearLayout) dialogLayout.findViewById(R.id.ll_gyt);
            TextView tv_gyt = (TextView) dialogLayout.findViewById(R.id.tv_gyt);

            //训鸽通
            LinearLayout ll_xgt = (LinearLayout) dialogLayout.findViewById(R.id.ll_xgt);
            TextView tv_xgt = (TextView) dialogLayout.findViewById(R.id.tv_xgt);


            //点击确定
            TextView tv_sure = (TextView) dialogLayout.findViewById(R.id.tv_sure);
            tv_sure.setOnClickListener(view -> {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            });

            //网站信息平台
            LinearLayout ll_wzxxpt = (LinearLayout) dialogLayout.findViewById(R.id.ll_wzxxpt);
            TextView tv_wzxxpt = (TextView) dialogLayout.findViewById(R.id.tv_wzxxpt);


            //比赛直播平台
            LinearLayout ll_bszbpt = (LinearLayout) dialogLayout.findViewById(R.id.ll_bszbpt);
            TextView tv_bszbpt = (TextView) dialogLayout.findViewById(R.id.tv_bszbpt);

            //鸽信通
            LinearLayout ll_gxt = (LinearLayout) dialogLayout.findViewById(R.id.ll_gxt);
            TextView tv_gxt = (TextView) dialogLayout.findViewById(R.id.tv_gxt);

            if (Integer.valueOf(mServesInfoEntity.getGxtsyts()) == 0) {
                ll_gxt.setVisibility(View.VISIBLE);
                tv_gxt.setText("剩余：0条");
                ll_gxt.setOnClickListener(view -> {
                    IntentBuilder.Builder().startParentActivity(mContext, CreateMessageOrderFragment.class);
                });
            }


            //公棚
            if (AssociationData.getUserAccountTypeInt() == 1) {
/*                //公棚网站
                if (DateUtils.isDifference(mServesInfoEntity.getGpwzdqsj(), 30)) {
                    ll_wzxxpt.setVisibility(View.VISIBLE);
                    tv_wzxxpt.setText(String.valueOf(mServesInfoEntity.getGpwzdqsj() + "到期"));
                    ll_wzxxpt.setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, OrderPlayActivity.class);
                        intent.putExtra("sid", 28);
                        intent.putExtra("tag", 4);
                        mContext.startActivity(intent);
                    });
                }*/

                //公棚赛鸽
                LinearLayout ll_gpsg = (LinearLayout) dialogLayout.findViewById(R.id.ll_gpsg);
                TextView tv_gpsg = (TextView) dialogLayout.findViewById(R.id.tv_gpsg);
                if (DateUtils.isDifference(mServesInfoEntity.getGpsgdqsj(), 30)) {
                    ll_gpsg.setVisibility(View.VISIBLE);
                    tv_gpsg.setText(String.valueOf(mServesInfoEntity.getGpsgdqsj() + "到期"));
                    ll_gpsg.setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, OrderPlayActivity.class);
                        intent.putExtra("sid", OrderService.GPSG_XF_SID);
                        intent.putExtra("tag", 5);
                        mContext.startActivity(intent);
                    });
                }
            }

            //协会
            if (AssociationData.getUserAccountTypeInt() == 2) {
             /*   //协会网站
                if (DateUtils.isDifference(mServesInfoEntity.getXhwzdqsj(), 30)) {
                    ll_wzxxpt.setVisibility(View.VISIBLE);
                    tv_wzxxpt.setText(String.valueOf(mServesInfoEntity.getXhwzdqsj() + "到期"));
                    ll_wzxxpt.setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, OrderPlayActivity.class);
                        intent.putExtra("sid", 28);
                        intent.putExtra("tag", 4);
                        mContext.startActivity(intent);
                    });
                }*/

                //我的会员
                LinearLayout ll_wdhy = (LinearLayout) dialogLayout.findViewById(R.id.ll_wdhy);
                TextView tv_wdhy = (TextView) dialogLayout.findViewById(R.id.tv_wdhy);
                if (DateUtils.isDifference(mServesInfoEntity.getXhhydqsj(), 30)) {
                    ll_wdhy.setVisibility(View.VISIBLE);
                    tv_wdhy.setText(String.valueOf(mServesInfoEntity.getXhhydqsj() + "到期"));
                    ll_wdhy.setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, OrderPlayActivity.class);
                        intent.putExtra("sid", OrderService.WDHY_XF_SID);
                        intent.putExtra("tag", 5);
                        mContext.startActivity(intent);
                    });
                }

            }

            //协会 公棚
            if (AssociationData.getUserAccountTypeInt() == 1 || AssociationData.getUserAccountTypeInt() == 2) {
                //鸽运通
                if (DateUtils.isDifference(mServesInfoEntity.getGytdqsj(), 30)) {
                    ll_gyt.setVisibility(View.VISIBLE);
                    tv_gyt.setText(String.valueOf(mServesInfoEntity.getGytdqsj() + "到期"));
                    ll_gyt.setOnClickListener(view -> {
                        mContext.startActivity(new Intent(new Intent(mContext, RenewalUpgradeActivity.class)));
                    });
                }

                //直播平台
                if (DateUtils.isDifference(mServesInfoEntity.getZbptdqsj(), 30)) {
                    ll_bszbpt.setVisibility(View.VISIBLE);
                    tv_bszbpt.setText(String.valueOf(mServesInfoEntity.getZbptdqsj() + "到期"));
                    ll_bszbpt.setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, OrderPlayActivity.class);
                        intent.putExtra("sid", OrderService.BSZBPT_XF_SID);
                        intent.putExtra("tag", 5);
                        mContext.startActivity(intent);
                    });
                }
            }

            //协会   个人
            if (AssociationData.getUserAccountTypeInt() == 2 || AssociationData.getUserAccountTypeInt() == 5) {
                //训鸽通
                if (DateUtils.isDifference(mServesInfoEntity.getXgtdqsj(), 30)) {
                    ll_xgt.setVisibility(View.VISIBLE);
                    tv_xgt.setText(String.valueOf(mServesInfoEntity.getXgtdqsj() + "到期"));
                    ll_xgt.setOnClickListener(view -> {
                        mContext.startActivity(new Intent(mContext, OpenXGTServiceActivity.class));
                    });
                }
            }

            dialog.setContentView(dialogLayout);

            //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
            dialog.setCanceledOnTouchOutside(true);

            if (isVisibility(ll_gyt) || isVisibility(ll_xgt) || isVisibility(ll_wzxxpt) ||
                    isVisibility(ll_bszbpt) || isVisibility(ll_gxt)) {
                dialog.show();
            }
            return dialog;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //控件是否显示
    private static boolean isVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public interface DialogClickListener {
        void onDialogClickListener(View viewSure, View viewCel, CustomAlertDialog dialog, String etStr);
    }
}
