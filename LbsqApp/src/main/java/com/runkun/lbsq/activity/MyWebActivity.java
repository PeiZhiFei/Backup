package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import feifei.project.util.ConfigUtil;

public class MyWebActivity extends BaseAcitivity
{
    protected WebView mWebView;
    protected ProgressBar progressBar;
    protected WebSettings webSettings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);
        tint();
//        initActionbar();
//        setTitles("产品");

        progressBar = (ProgressBar) findViewById(R.id.progress);
        mWebView = (WebView) findViewById(R.id.webview);
        webSettings = mWebView.getSettings();

        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.requestFocus();
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                mWebView.loadUrl(url);
                return true;
            }
        });
//        mWebView.loadUrl(getIntent().getStringExtra("url"));
        mWebView.loadUrl("file:///android_asset/ads/index.html");

        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                progressBar.setProgress(newProgress);
                if (newProgress == 100)
                {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.addJavascriptInterface(new PayJavaScriptInterface(), "pay");
    }

    public class PayJavaScriptInterface
    {
        PayJavaScriptInterface()
        {
        }

        @android.webkit.JavascriptInterface
        public String clickOnAndroid(String[] js)
        {
            if (Tools.isEmpty(ConfigUtil.readString (activity, MyConstant.KEY_MEMBERID, "")))
            {
                startActivity(new Intent(activity,LoginActivity.class));
                AnimUtil.animToSlide(activity);
            } else
            {
                Intent intent = new Intent(activity, BuyNowActivity.class);
                intent.putExtra("storeId", "56");
                intent.putExtra("storeName", "特色专卖店");
                intent.putExtra("unit", "克");
                intent.putExtra("step", "1");
                intent.putExtra("min", "1");
                intent.putExtra("max", "99");
                intent.putExtra("quantity", "1");

                intent.putExtra("goodId", js[1]);
                intent.putExtra("by", "430");
                intent.putExtra("goodName", js[0] + "-" + getGoodsName(js[1]));
                intent.putExtra("goodPrice", js[2]);
                startActivity(intent);
            }

//            Tools.dialog(activity, "你要购买" + js[0] + ",\n" + "口味是" + js[1] + ",一共是" + js[2] + "元", true, null);
            return "成功添加到购物车";
        }
    }

    public String getGoodsName(String s)
    {
        String[] array = new String[]{"微辣", "中辣", "麻辣"};
        return array[Integer.valueOf(s) - 1];
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack())
        {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // @Override
    // public void onBackPressed() {
    // if (mWebView.canGoBack()) {
    // mWebView.goBack();
    // } else {
    // super.onBackPressed();
    // }
    // }

}
