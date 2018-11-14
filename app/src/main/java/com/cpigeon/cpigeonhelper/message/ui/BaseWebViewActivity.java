package com.cpigeon.cpigeonhelper.message.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.base.BaseActivity;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class BaseWebViewActivity<Pre extends BasePresenter> extends BaseActivity<Pre> {

    WebView webView;
    String url;
    String title;
    ProgressBar progressBar;

    TextView tvTitle;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view_layout;
    }

    @Override
    public Pre initPresenter() {
        return null;
    }


    @Override
    public void initView(Bundle savedInstanceState) {

        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);

        url = getIntent().getStringExtra(IntentBuilder.KEY_DATA);

        title = getIntent().getStringExtra(IntentBuilder.KEY_TITLE);

        tvTitle = (TextView) findViewById(R.id.toolbar_title);

        if(StringValid.isStringValid(title)){
            tvTitle.setText(title);
        }


        webView = (WebView) findViewById(R.id.web_view);
        progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                CrashReport.setJavascriptMonitor(view, true);
                if (progressBar != null) {
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        if (View.GONE == progressBar.getVisibility()) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        progressBar.setProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }


            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                //如果不需要其他对点击链接事件的处理返回true，否则返回false
                return true;
            }
        });
        //webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        Map<String, String> mHeaderMap = new HashMap<>();
        mHeaderMap.put("auth", AssociationData.getUserToken());
        webView.loadUrl(url, mHeaderMap);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }

    @Override
    public void getErrorNews(String str) {

    }

    @Override
    public void getThrowable(Throwable throwable) {

    }
}
