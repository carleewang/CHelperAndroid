package com.cpigeon.cpigeonhelper.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;


/**
 * Created by hcc on 16/8/7 21:18
 * 100332338@qq.com
 * <p/>
 * 自定义EmptyView
 */
public class CustomEmptyView extends FrameLayout {

    private ImageView mEmptyImg;

    private TextView mEmptyText, retryBtn;


    public CustomEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }


    public CustomEmptyView(Context context) {

        this(context, null);
    }


    public CustomEmptyView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }


    public void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, this);
        mEmptyImg = (ImageView) view.findViewById(R.id.empty_img);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
        retryBtn = (TextView) view.findViewById(R.id.btn_empty);//点击重试按钮
    }


    public void setEmptyImage(int imgRes) {
        mEmptyImg.setImageResource(imgRes);
    }


    public void setEmptyText(String text) {
        mEmptyText.setText(text);
    }

    public void setEmptyText(int text) {
        mEmptyText.setText(text);
    }

    public void setEmptyTextVisibility(int visibility) {
        mEmptyText.setVisibility(visibility);
    }

    public TextView getEmptyText() {//点击重试按钮
        return mEmptyText;
    }

    public TextView getRetryBtn() {//点击重试按钮
        return retryBtn;
    }

}
