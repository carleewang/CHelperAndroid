package com.cpigeon.cpigeonhelper.ui;


import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.cpigeon.cpigeonhelper.R;

/**
 * 自定义加载界面
 * Created by BlingBling on 2016/10/15.
 */

public final class CustomLoadMoreView2 extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_load_more2;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override public int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }

}
