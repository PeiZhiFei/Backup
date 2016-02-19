package com.hitoosoft.hrssapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.view.refresh.RefreshBase;
import com.hitoosoft.hrssapp.view.refresh.RefreshBase.OnRefreshListener;
import com.hitoosoft.hrssapp.view.refresh.RefreshWebView;

@SuppressLint("SetJavaScriptEnabled")
public class WebFragment extends BaseFragment {
	private View view;
	public RefreshWebView webView;
	private ProgressBar progressBar;
	private WebSettings webSettings;
	private String url;
	private boolean phone;

	public static WebFragment newInstance(Bundle url) {
		WebFragment webFragment = new WebFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url.getString("url"));
		bundle.putBoolean("phone", url.getBoolean("phone", false));
		webFragment.setArguments(bundle);
		return webFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getArguments().getString("url");
		phone = getArguments().getBoolean("phone");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_web, container, false);
		progressBar = (ProgressBar) view.findViewById(R.id.progress);
		webView = (RefreshWebView) view.findViewById(R.id.webview);

		webView.requestFocus();
		webView.setScrollbarFadingEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		webSettings = webView.getRefreshableView().getSettings();
		webSettings.setSupportZoom(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// webSettings
		// .setUserAgentString("(iPad; U; CPU OS 3_2 like Mac OS X;en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334bSafari/531.21.10");

		webView.getRefreshableView().loadUrl(url);
		webView.getRefreshableView().setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (phone) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + url));
					getActivity().startActivity(intent);
				} else {
					webView.getRefreshableView().loadUrl(url);
				}
				return true;
			}
		});
		// 设置进度条
		webView.getRefreshableView().setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
					webView.onRefreshComplete();
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		webView.setOnRefreshListener(new OnRefreshListener<WebView>() {

			@Override
			public void onRefresh(RefreshBase<WebView> refreshView) {
				webView.getRefreshableView().loadUrl(url);
			}
		});

		return view;
	}

	@Override
	public boolean onBackPressed() {
		if (webView.getRefreshableView().canGoBack()) {
			webView.getRefreshableView().goBack();
			return true;
		} else {
			return false;
		}
	}

}
