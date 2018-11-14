package com.cpigeon.cpigeonhelper.message.base;


import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.DefaultItemAnimator;

import com.cpigeon.cpigeonhelper.utils.CommonTool;

import java.util.List;


/**
 * Created by Zhu TingYu on 2018/1/11.
 */

public abstract class MyBaseQuickAdapter<T, K extends BaseViewHolder> extends com.chad.library.adapter.base.BaseQuickAdapter<T, K> {
    public MyBaseQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);

    }


    public void setLoadMore(boolean isEnd) {
        if (isEnd) {
            this.loadMoreEnd();
        } else this.loadMoreComplete();
    }

    @Override
    public void setNewData(List<T> data) {
        if (getRecyclerView() != null) {
            getRecyclerView().getRecycledViewPool().clear();
            notifyDataSetChanged();
            ((DefaultItemAnimator) getRecyclerView().getItemAnimator()).setSupportsChangeAnimations(false);
        }

        if (data.isEmpty()) {
            if (!getEmptyViewText().isEmpty()) {
                setEmptyView();
            }
        }
        super.setNewData(data);
    }

    public void setEmptyView() {
        if (getEmptyViewImage() == 0) {
            CommonTool.setEmptyView(this, getEmptyViewText());
        }
//        else {
////            CommonTool.setEmptyView(this, getEmptyViewImage(), getEmptyViewText());
//        }
    }

    protected String getEmptyViewText() {
        return "";
    }


    protected
    @DrawableRes
    int getEmptyViewImage() {
        return 0;
    }

    protected int getColor(@ColorRes int resId) {
        return mContext.getResources().getColor(resId);
    }

    protected float getDimension(@DimenRes int resId) {
        return mContext.getResources().getDimension(resId);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) mContext;
    }

}

