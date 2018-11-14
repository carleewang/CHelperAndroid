package com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.model.bean.HflistBean;
import com.cpigeon.cpigeonhelper.modular.myserver.xh.gyjl.presenter.GyjlPresenter;

import java.util.List;

/**
 * 留言回复适配器
 * Created by Administrator on 2018/3/23.
 */

public class MessageItemAdapter extends BaseQuickAdapter<HflistBean, BaseViewHolder> {

    private GyjlPresenter mGyjlPresenter;

    private String id;

    public MessageItemAdapter(List<HflistBean> data, GyjlPresenter mGyjlPresenter, String id) {
        super(R.layout.item_item_gyly_hf, data);
        this.mGyjlPresenter = mGyjlPresenter;
        this.id = id;
    }

    @Override
    protected void convert(BaseViewHolder helper, HflistBean item) {

        //回复内容
        helper.setText(R.id.tv_item_item_nr, item.getHfcontent());

//        helper.getView(R.id.ll_item_item_hfz).setOnClickListener(view -> {
//
//        });
    }
}
