package com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.adapter;

import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.model.bean.GetChaZuListEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.gp.servergp.view.actvity.DesignatedSetActivity;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2018/4/28.
 */

public class DesignatedDetailAdapter extends BaseQuickAdapter<GetChaZuListEntity, BaseViewHolder> {

    private SweetAlertDialog mSweetAlertDialog;
    private EditText mEt;
    private String tid;
    private int serviceType;

    public DesignatedDetailAdapter(List<GetChaZuListEntity> data, String tid, int serviceType) {
        super(R.layout.item_designated_set2, data);
        this.tid = tid;
        this.serviceType = serviceType;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetChaZuListEntity item) {

        helper.setText(R.id.tv_title, item.getCz() + "组");

        TextView tv_com2 = helper.getView(R.id.tv_com2);
        TextView tv_com1 = helper.getView(R.id.tv_com1);

        tv_com1.setText("");
        tv_com2.setText("");

        if ((item.getCsf() == null || Integer.valueOf(item.getCsf()) == 0) && item.getBm() == null) {
            tv_com1.setText("");
            tv_com2.setText("未设置");
            tv_com2.setTextColor(mContext.getResources().getColor(R.color.color_b3b3b3));

        } else if ((item.getCsf() == null || Integer.valueOf(item.getCsf()) == 0) && item.getBm() != null) {

            tv_com1.setText(item.getBm());
            tv_com2.setText("");
            tv_com2.setTextColor(mContext.getResources().getColor(R.color.black));
        } else if (Integer.valueOf(item.getCsf()) != 0) {
            tv_com1.setText(item.getCsf() + "元");
            tv_com2.setText(item.getGz());
            tv_com2.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        LinearLayout ll_itemZ = helper.getView(R.id.ll_itemZ);
        ll_itemZ.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DesignatedSetActivity.class);
            intent.putExtra("tid", tid);
            intent.putExtra("zb", item.getCz());
            intent.putExtra("serviceType", serviceType);
            mContext.startActivity(intent);
        });
    }
}
