package com.findyou.findyoueverywhere.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.findyou.findyoueverywhere.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class WebViewFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.webview_loading)
    ProgressBar webview_loading;

    public int getLayoutId(){
        return R.layout.fragment_webview;
    }

    public void initView(){
        Bundle bundle = this.getActivity().getIntent().getExtras();
        String default_title = bundle.getString("title");
        String url = bundle.getString("url");

        if(!TextUtils.isEmpty(default_title)) {
            setTitle(default_title);
        }

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if(TextUtils.isEmpty(default_title)) {
                    setTitle(view.getTitle());
                }
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);

        webView.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v) { // 禁用长按出现“复制”按钮
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(TextUtils.isEmpty(default_title)) {
                    setTitle(title);
                }
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(webview_loading != null) {
                    webview_loading.setProgress(newProgress);
                    if (newProgress != 100) {
                        webview_loading.setVisibility(View.VISIBLE);
                    } else {
                        webview_loading.setVisibility(View.GONE);
                    }
                }
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !(webView.canGoBack())) {
            if(getActivity() != null) {
                getActivity().finish();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack(); // 返回前一个页面
            return false;
        }
        return false;
    }
}
