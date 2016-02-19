package com.hitoosoft.hrssapp.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.acitivity.BaseActivity;
import com.hitoosoft.hrssapp.database.FavoriteDb;
import com.hitoosoft.hrssapp.util.HrssConstants;

public class WebActivity2 extends BaseActivity {

	Intent intent;
	public WebView webView;
	private WebSettings webSettings;

	// String ss =
	// "<html>\t<html> <head>   <meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"gb2312\">   <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=0\">   <meta http-equiv=\"pragma\" content=\"no-cache\">   <meta http-equiv=\"expires\" content=\"0\">   <script>function resizeTo(img){var width = parseInt(img.width);var target = 300;img.style.width=(width < target)?width:target;var scale=parseInt(img.style.width)/width;img.style.height=parseInt(img.height)*scale;}</script>  </head>  <body>   <p> <strong id=\"title\">我市组团参加第十三届中国国际人才交流大会取得丰硕成果</strong> </p>   <hr>   <div id=\"content\">   <p>&nbsp;</p>    <div style=\"background: white; layout-grid-mode: char; text-indent: 32pt\">     <span style=\"line-height: 36px\"><span style=\"font-size: medium\"><span style=\"font-family: Times New Roman\">4月18日－19日，我市组织市内16家企事业单位参加了在深圳举行的第十三届中国国际人才交流大会，在参会人数、洽谈项目数、达成引进意向人数等方面均创历史新高，取得了丰硕成果。会上，共向与会外国专家组织、人才机构、高等院校、研发中心和猎头机构提交项目需求120余项，涉及新能源新材料、机械制作、生物技术、生态环保、教育卫生等领域。与德国、美国、英国、法国、荷兰、以色列、日本等外国高层次专家组织对接信息40条，其中12个专家项目初步达成合作意向；与各类人才组织、高层次人才对接信息96条，冠铨（山东）光电科技有限公司“LED外延片亮度提升及良率提高技术”等30余条达成意向，其中曲阜天博汽车零部件制造有限公司与著名德国汽车专家签订了合作协议；与俄罗斯、韩国、印度海外机构达成建立国际人才海外联络站合作意向，与伯纳特国际合作中心、俄罗斯沃罗涅日国立大学签订了建立国际人才海外联络站合作意向书。</span></span></span>    </div>    <div style=\"background: white; layout-grid-mode: char; text-indent: 24pt\">     <span style=\"line-height: 36px\"><span style=\"font-size: medium\"><span style=\"font-family: Times New Roman\">会议期间，发放《济宁市国际人才海外联络站引进海外高层次人才奖励政策汇编》100余册，积极宣传我市引进海外高层次人才技术优惠政策，与美中发展促进会举行共同设立济宁市国际人才美国联络站签约仪式，与参会的外企德科集团、万宝盛华集团、诺姆四达集团、广东智通人才连锁集团、锐仕方达（北京）人力资源顾问公司等四家国际猎头机构就我市开展国际猎头业务议题进行了交流，同时学习国内先进省市引进国外智力经验做法，搜集广州、深圳、杭州、大连、厦门等20余个发达城市海外高层次人才引进管理政策及做法100条。 (市国际人才交流协会办公室李晓芹)</span></span></span>    </div>    <div style=\"background: white; layout-grid-mode: char; text-align: center; text-indent: 24pt\">     <span style=\"line-height: 36px\"><span style=\"font-size: medium\"><span style=\"font-family: Times New Roman\"><img width=\"300\" height=\"300\" alt=\"\" src=\"getNewsPicData.do?picID=20150428100002978204\" onload=\"resizeTo(this)\"></span></span></span>    </div>    <p style=\"text-align: center\"><span style=\"font-size: medium\"><span style=\"font-family: Times New Roman\">会议现场</span></span></p>    <p style=\"text-align: center\"><span style=\"font-size: medium\"><span style=\"font-family: Times New Roman\"><img width=\"300\" height=\"300\" alt=\"\" src=\"getNewsPicData.do?picID=20150428100002978205\" onload=\"resizeTo(this)\"></span></span></p>    <p style=\"text-align: center\"><span style=\"font-size: medium\"><span style=\"font-family: Times New Roman\">会议现场</span></span></p>  </div>   </body></html></html>";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_web);
		intent = getIntent();
		webView = (WebView) findViewById(R.id.webview);
		webView.requestFocus();
		webView.setScrollbarFadingEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		webSettings = webView.getSettings();
		webSettings.setSupportZoom(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}
		});

		String ni = intent.getStringExtra(FavoriteDb.newsByteContent);
		String xString = ni.replace("700", "300").replace("466", "200");
		webView.loadDataWithBaseURL(HrssConstants.SERVER_URL
				+ "hrssmsp/newsList/viewNewsContent.do?newsID=", xString,
				"text/html", "utf-8", null);
		Log.e("log", xString);
	}

}
