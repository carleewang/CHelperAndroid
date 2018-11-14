package com.cpigeon.cpigeonhelper.modular.menu.view.adapter;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public class BullentinAdapter extends BaseQuickAdapter<BulletinEntity, BaseViewHolder> {


    private WebView mWebView;
    private WebSettings mWebSettings;

    public BullentinAdapter(List<BulletinEntity> data) {
        super(R.layout.item_bulletin, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BulletinEntity item) {
        helper.setText(R.id.it_tv_bl_title, item.getTitle());//设置标题
        helper.setText(R.id.it_tv_bl_time, DateUtils.getYMD(item.getTime()));//设置时间
//        helper.setText(R.id.it_tv_bl_content, item.getContent());//设置内容

        mWebView = ((WebView) helper.getView(R.id.it_tv_bl_content));
        mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setTextSize(WebSettings.TextSize.NORMAL);

//        mWebView.setWebViewClient(null);

        //  加载、并显示HTML代码
        mWebView.loadDataWithBaseURL(null, item.getContent(), "text/html", "utf-8", null);
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
    }
}
