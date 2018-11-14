package com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.message.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.HyUserDetailEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.model.bean.PenalizeRecordEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.wdhy.presenter.MemberPresenter;
import com.cpigeon.cpigeonhelper.ui.mydialog.CustomAlertDialog;
import com.cpigeon.cpigeonhelper.utils.DateUtils;
import com.cpigeon.cpigeonhelper.utils.dialog.MyMemberDialogUtil;

import java.util.List;

/**
 * 处罚记录
 * Created by Administrator on 2018/3/30.
 */

public class PenalizeRecordAdapter extends BaseQuickAdapter<PenalizeRecordEntity, BaseViewHolder> {

    private Intent intent;
    private HyUserDetailEntity mHyUserDetailEntity;
    private MemberPresenter mMemberPresenter;
    private String type = "myself";//look :上级协会查看   myself：自己查看

    public PenalizeRecordAdapter(List<PenalizeRecordEntity> data, HyUserDetailEntity mHyUserDetailEntity, MemberPresenter mMemberPresenter,String type) {
        super(R.layout.item_penalize_record_layout, data);
        this.mHyUserDetailEntity = mHyUserDetailEntity;
        this.mMemberPresenter = mMemberPresenter;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PenalizeRecordEntity item) {

        helper.setText(R.id.tv_title, item.getCfresult());//处罚结果


        if (item.getCfcxyy().length() != 0) {
            helper.getView(R.id.img_ycx).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_time, item.getCfcxsj());//时间
            helper.setText(R.id.tv_con, item.getCfcxyy());//处罚原因
        } else {
            helper.setText(R.id.tv_time, item.getCfsj());//时间
            helper.setText(R.id.tv_con, item.getCfreson());//处罚原因

            if (type.equals("myself") && !mHyUserDetailEntity.getZhuangtai().equals("除名")) {
                helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
                    //处罚原因
                    MyMemberDialogUtil.initInputDialog((Activity) mContext, "", "请输入撤销原因", "请填写1-100个字符！", InputType.TYPE_CLASS_TEXT,
                            new MyMemberDialogUtil.DialogClickListener() {
                                @Override
                                public void onDialogClickListener(View viewSure, CustomAlertDialog dialog, String etStr) {
                                    dialog.dismiss();
                                    if (etStr.isEmpty() || etStr.length() == 0) return;

                                    mMemberPresenter.getPenalizeRecordEdit(mHyUserDetailEntity.getBasicinfo().getMid(),
                                            item.getCid(), item.getCfsj(), item.getCfresult(), item.getCfreson(),
                                            DateUtils.getStringDateShort(), etStr);
                                }
                            });
                });
            }
        }


        if (helper.getPosition() == getData().size() ) {
            helper.getView(R.id.ll_divline).setVisibility(View.GONE);
        }


//        helper.getView(R.id.ll_itemZ).setOnClickListener(view -> {
//            intent = new Intent(mContext, PenalizeRecordAddActivity.class);
//            intent.putExtra("mPenalizeRecordEntity", item);
//            intent.putExtra("data", mHyUserDetailEntity);
//            mContext.startActivity(intent);
//        });
    }
}
