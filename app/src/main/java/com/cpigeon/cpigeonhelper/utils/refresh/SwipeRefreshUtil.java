package com.cpigeon.cpigeonhelper.utils.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.ui.CustomEmptyView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 刷新控件
 * Created by Administrator on 2018/2/25.
 */

public class SwipeRefreshUtil {


    /**
     * 刷新布局中显示错误信息
     * 使用  CommonUitls.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
     */
    public static void swipeRefreshLayoutCustom(SwipeRefreshLayout mSwipeRefreshLayout, CustomEmptyView mCustomEmptyView, RecyclerView mRecyclerView, String errorText) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mCustomEmptyView.setEmptyImage(R.mipmap.face);
            mCustomEmptyView.setEmptyText(errorText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 刷新布局中显示错误信息
     * 使用  CommonUitls.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
     */
    public static void swipeRefreshLayoutCustom(SwipeRefreshLayout mSwipeRefreshLayout, CustomEmptyView mCustomEmptyView, RecyclerView mRecyclerView, int errorText) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mCustomEmptyView.getEmptyText().setVisibility(View.VISIBLE);
            mCustomEmptyView.getRetryBtn().setVisibility(View.GONE);
            mCustomEmptyView.setEmptyImage(R.mipmap.face);
            mCustomEmptyView.setEmptyText(errorText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新布局中显示错误信息 （点击可以刷新）
     * 使用  CommonUitls.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, msg);
     */
    public static void swipeRefreshLayoutCustom2(SwipeRefreshLayout mSwipeRefreshLayout, CustomEmptyView mCustomEmptyView, RecyclerView mRecyclerView, Throwable mThrowable, String errorText, View.OnClickListener mClick) {
        try {
            if (mThrowable instanceof SocketTimeoutException || mThrowable instanceof ConnectException) {
                //网络异常
                mSwipeRefreshLayout.setRefreshing(false);
                mCustomEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mCustomEmptyView.getEmptyText().setVisibility(View.GONE);
                mCustomEmptyView.getRetryBtn().setVisibility(View.VISIBLE);
                mCustomEmptyView.setEmptyImage(R.mipmap.network_retry);
                mCustomEmptyView.getRetryBtn().setOnClickListener(mClick);
            } else {
                //其他异常
                SwipeRefreshUtil.swipeRefreshLayoutCustom(mSwipeRefreshLayout, mCustomEmptyView, mRecyclerView, errorText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新布局显示正常视图 （点击刷新恢复原视图）
     */
    public static void showNormal(CustomEmptyView mCustomEmptyView, RecyclerView mRecyclerView) {
        try {
            mCustomEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mCustomEmptyView.getEmptyText().setVisibility(View.VISIBLE);
            mCustomEmptyView.getRetryBtn().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
