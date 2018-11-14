package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.ChildFoodAdminListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.FootPriceEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.FootAdminPresenter;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.activity.child_association.ChildFootEntryActivity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.viewdao.FootAdminViewImpl;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ChildFoodAdminAdapter extends BaseQuickAdapter<ChildFoodAdminListEntity.FootlistBean, BaseViewHolder> {


    private FootAdminPresenter mFootAdminPresenter;
    private String y;
    private String clidkTypt = "-1";
    private double price;
    private SweetAlertDialog errSweetAlertDialog;
    private Intent intent;
    private ChildFoodAdminListEntity.FootlistBean item;

    public ChildFoodAdminAdapter(List<ChildFoodAdminListEntity.FootlistBean> data, SweetAlertDialog errSweetAlertDialog) {
        super(R.layout.item_child_food_admin, data);
        this.errSweetAlertDialog = errSweetAlertDialog;
        mFootAdminPresenter = new FootAdminPresenter(new FootAdminViewImpl() {

            @Override
            public void getXHHYGL_ZHGL_GetFootPrice(ApiResponse<FootPriceEntity> listApiResponse, String msg, Throwable mThrowable) {

                if (listApiResponse.getErrorCode() == 0) {
                    price = Double.valueOf(listApiResponse.getData().getPrice());
                    if (price == 0) {
                        ChildFoodAdminAdapter.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(ChildFoodAdminAdapter.this.errSweetAlertDialog, "请先设置足环单价，然后才能录入足环", (Activity) mContext, dialog -> {
                            dialog.dismiss();
                            setPrice(price, clidkTypt);
                        });//弹出提示
                    } else {
                        intent = new Intent(mContext, ChildFootEntryActivity.class);
                        intent.putExtra("type", "edit");
                        intent.putExtra("price", String.valueOf(price));
                        intent.putExtra("data", item);
                        mContext.startActivity(intent);
                    }

                } else {
                    ChildFoodAdminAdapter.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(ChildFoodAdminAdapter.this.errSweetAlertDialog, "获取数据失败，请稍后再试!", (Activity) mContext, dialog -> {
                        dialog.dismiss();
                        setPrice(price, clidkTypt);
                    });//弹出提示
                }
            }

            @Override
            public void getXHHYGL_ZHGL_SetFootPrice(ApiResponse<Object> listApiResponse, String msg, Throwable mThrowable) {

                try {
                    if (mThrowable != null) {
                        ChildFoodAdminAdapter.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog3(ChildFoodAdminAdapter.this.errSweetAlertDialog, "设置足环价格失败，请稍后再试", (Activity) mContext, dialog -> {
                            dialog.dismiss();

                        });//弹出提示
                        return;
                    }

                    ChildFoodAdminAdapter.this.errSweetAlertDialog = SweetAlertDialogUtil.showDialog(ChildFoodAdminAdapter.this.errSweetAlertDialog, msg, (Activity) mContext, dialog -> {
                        dialog.dismiss();

                    });//弹出提示

                    clidkTypt = "-1";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        y = DateUtils.getStringDateY();
    }

    @Override
    protected void convert(BaseViewHolder helper, ChildFoodAdminListEntity.FootlistBean item) {

        try {

            String[] foots = item.getFoot2().split("-");
            if (foots.length==3) {
                helper.setText(R.id.tv_com1, String.valueOf(item.getFoot1() + "  ──  " + foots[2]));
            }else {
                helper.setText(R.id.tv_com1, String.valueOf(item.getFoot1() + "  ──  " + item.getFoot2()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            helper.setText(R.id.tv_com2, item.getXhmc());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            helper.setText(R.id.tv_com3, String.valueOf("(" + item.getFootnum() + "枚)"));
        } catch (Exception e) {
            helper.setText(R.id.tv_com3, String.valueOf("(0枚)"));
        }

        helper.getView(R.id.ll_z).setOnClickListener(view -> {

            if (item.getType().equals("普通足环")) {
                clidkTypt = "普通足环";
                mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice_Child(y, "2");
            } else {
                clidkTypt = "特比环";
                mFootAdminPresenter.getXHHYGL_ZHGL_GetFootPrice_Child(y, "3");
            }
            this.item = item;
        });
    }


    //设置单价
    private void setPrice(double price, String footType) {
        String hintStr = "";
        if (footType.equals("普通足环")) {
            hintStr = "请输入普通足环单价";
            showPriceDialog(hintStr, footType, price);
        } else if (footType.equals("特比环")) {
            hintStr = "请输入特比环单价";
            showPriceDialog(hintStr, footType, price);
        }
    }

    //弹出设置单价的dialog
    private void showPriceDialog(String hintStr, String footType, double price) {
        MyMemberDialogUtil.initInputDialog1((Activity) mContext, String.valueOf(price), hintStr, "请填写数字即可!", InputType.TYPE_CLASS_TEXT,
                new MyMemberDialogUtil.DialogClickListener() {
                    @Override
                    public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                        dialog.dismiss();
                        if (etStr.isEmpty() || etStr.length() == 0) {

                            errSweetAlertDialog = SweetAlertDialogUtil.showDialog(errSweetAlertDialog, "输入价格不能为空", (Activity) mContext, dialogs -> {
                                dialogs.dismiss();
                            });//弹出提示
                            return;
                        }

                        if (footType.equals("普通足环")) {
                            mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice_Child(y, "2", etStr);
                        } else if (footType.equals("特比环")) {
                            mFootAdminPresenter.getXHHYGL_ZHGL_SetFootPrice_Child(y, "3", etStr);
                        }
                    }
                });
    }

}
