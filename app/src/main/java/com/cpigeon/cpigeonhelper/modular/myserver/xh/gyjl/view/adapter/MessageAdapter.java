package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.GyjlMessageEntity;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.presenter.GyjlPresenter;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.dialog.InputCommentDialog;
import com.cpigeon.cpigeonhelper.utils.dialog.SweetAlertDialogUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MessageAdapter extends BaseQuickAdapter<GyjlMessageEntity, BaseViewHolder> {
    private GyjlPresenter mGyjlPresenter;
    private CircleImageView mCircleImageView;


    public MessageAdapter(List<GyjlMessageEntity> data, GyjlPresenter mGyjlPresenter) {
        super(R.layout.item_gyjl_message, data);
        this.mGyjlPresenter = mGyjlPresenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, GyjlMessageEntity item) {

        helper.getView(R.id.ll_hfZ).setVisibility(View.GONE);

        mCircleImageView = helper.getView(R.id.img_user_head);
        //头像
        Glide.with(mContext).load(item.getToux()).into(mCircleImageView);

        //昵称
        helper.setText(R.id.tv_nick, item.getNichen());

        //留言内容
        helper.setText(R.id.tv_content, item.getLynr());

        //留言时间
        helper.setText(R.id.tv_time, item.getLysj());

        helper.getView(R.id.btn_delZ).setOnClickListener(view -> {
            SweetAlertDialogUtil.showSweetDialog((Activity) mContext, "您确定是要删除该条留言吗？", dialog -> {
                dialog.dismiss();
                mGyjlPresenter.delLyPl_XH(item.getLyid(), "ly");
            });
        });

        helper.getView(R.id.btn_hfZ).setOnClickListener(view -> {
            InputCommentDialog inputCommentDialog = new InputCommentDialog();
            inputCommentDialog.setHint(String.valueOf("回复："));
            inputCommentDialog.setPushClickListener(editText -> {
                inputCommentDialog.dismiss();
                if (editText.getText().toString().equals("")) {
                    CommonUitls.showToast(mContext, "回复不能为空");
                    return;
                }
                mGyjlPresenter.translateLyPl_XH(item.getLyid(), "ly", editText.getText().toString());
            });
            inputCommentDialog.show(((Activity) mContext).getFragmentManager(), "11");
        });

        if (item.getHflist() != null && item.getHflist().size() != 0) {
            helper.getView(R.id.ll_hfZ).setVisibility(View.VISIBLE);
            RecyclerView item_recyclerView = helper.getView(R.id.item_recyclerView);
            MessageItemAdapter mAdapter = new MessageItemAdapter(item.getHflist(), mGyjlPresenter, item.getLyid());
            item_recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    SweetAlertDialogUtil.showSweetDialog((Activity) mContext, "您确定是要删除该条回复吗？", dialog -> {
                        dialog.dismiss();
                        mGyjlPresenter.delHF_LyPl_XH(item.getLyid(), item.getHflist().get(position).getHfid());
                    });
                }
            });
            item_recyclerView.setFocusableInTouchMode(false);
            item_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            helper.getView(R.id.ll_hfZ).setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        super.setOnItemChildClickListener(listener);
    }
}
