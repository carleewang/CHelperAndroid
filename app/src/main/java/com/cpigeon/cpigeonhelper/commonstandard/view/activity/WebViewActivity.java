package com.cpigeon.cpigeonhelper.commonstandard.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.ui.ObservableWebView;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 网页activity
 * Created by Administrator on 2017/7/19.
 */

public class WebViewActivity extends ToolbarBaseActivity implements ObservableWebView.OnScrollChangedListener {

    String url, titles;
    private static final Map<String, Integer> URL_POSITION_CACHES = new HashMap<>();
    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TITLE = "extra_title";
    boolean overrideTitleEnabled = true;
    int positionHolder;

    @BindView(R.id.web_view)
    WebView webview;
    //    ObservableWebView webview;
    @BindView(R.id.progressbar)
    NumberProgressBar progressbar;

    @Override
    protected void swipeBack() {
//        Slidr.attach(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    private Map<String, String> mHeaderMap;

    @Override
    protected void initViews(Bundle savedInstanceState) {
//        setTitle("帮助");//默认title
        setTopLeftButton(R.drawable.ic_back, this::finish);

        url = getIntent().getStringExtra(EXTRA_URL);
        titles = getIntent().getStringExtra(EXTRA_TITLE);

        mHeaderMap = new HashMap<>();
        mHeaderMap.put("u", AssociationData.getUserToken());
        //声明WebSettings子类
        WebSettings settings = webview.getSettings();
        //与Javascript交互
        settings.setJavaScriptEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        ////设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setAppCacheEnabled(true);//设置应用程序缓存
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置布局算法

        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//这句话必加，不然缩放不起作用
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);

        settings.setDomStorageEnabled(true);
        webview.setWebChromeClient(new ChromeClient());
//        webview.setOnScrollChangedListener(this);

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webview.setOnKeyListener(onKeyListener);

        webview.setWebViewClient(new WebViewClient() {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url, mHeaderMap);
//                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (titles != null) {
                    WebViewActivity.this.setTitle(titles);
                    Log.d(TAG, "onPageFinished: 1");
                } else {
                    Log.d(TAG, "onPageFinished: 2");
                    WebViewActivity.this.setTitle(view.getTitle());
                }
            }
        });
        webview.loadUrl(url, mHeaderMap);
//        webview.loadUrl(url);
    }

    //网页点击返回监听
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  //表示按返回键
                    webview.goBack();   //后退

                    //webview.goForward();//前进
                    return true;    //已处理
                }
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        final String url = webview.getUrl();
        int bottom = (int) Math.floor(webview.getContentHeight() * webview.getScale() * 0.8f);
        if (positionHolder >= bottom) {
            URL_POSITION_CACHES.remove(url);
        } else {
            URL_POSITION_CACHES.put(url, positionHolder);
        }
        super.onDestroy();
        if (webview != null) webview.destroy();
    }

    @Override
    public void onScrollChanged(WebView v, int x, int y, int oldX, int oldY) {
        positionHolder = y;
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressbar == null) return;
            progressbar.setProgress(newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(View.INVISIBLE);
            } else {
                progressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            if (overrideTitleEnabled) {
//                if (title.isEmpty()) {
//                    setTitle(view.getTitle());
//                } else {
//                    setTitle(titles);
//                }
//            }
        }
    }


}
