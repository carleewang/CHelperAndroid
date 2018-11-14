package com.cpigeon.cpigeonhelper.commonstandard.view.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.StatusBarUtil;

import butterknife.BindView;

import static com.cpigeon.cpigeonhelper.commonstandard.view.activity.WebViewActivity.EXTRA_URL;

/**
 * 纯链接网页展示
 * Created by Administrator on 2017/11/7.
 */

public class WebViewNewActivity extends ToolbarBaseActivity {


    @BindView(R.id.new_web_view)
    WebView webView;

    @Override
    protected void swipeBack() {

    }

    @Override
    protected int getContentView() {
        return R.layout.avtivity_webview_new;
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_theme), 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
//        setTitle("中鸽助手");
        setTopLeftButton(R.drawable.ic_back, WebViewNewActivity.this::finish);
        String web_url = getIntent().getStringExtra(EXTRA_URL);
        //实例化WebView对象
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        webView.loadUrl(web_url);

        //设置title
        if (getIntent().getStringExtra("web_title") != null) {
            setTitle(getIntent().getStringExtra("web_title"));
        } else {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    setTitle(view.getTitle());
                }
            });
        }

//        //设置Web视图
//        setContentView(webView);
    }

//    @Override
//    //设置回退
//    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
//        return false;
//    }


}
