package com.hitoosoft.hrssapp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class AsyncHttpPostTask extends Thread{
	private Handler handler;
	private String url;
	private String[] paraPairs;
	
	public AsyncHttpPostTask(Handler handler, String url, String[] paraPairs){
		this.handler = handler;
		this.url = url;
		this.paraPairs = paraPairs;
	}

	@Override
	public void run() {
		Message msg = Message.obtain();
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			if (null != paraPairs && paraPairs.length > 0) {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				for (int i = 0; i < paraPairs.length; i++) {
					String[] pairs = paraPairs[i].split("=");
					if(pairs.length == 2){
						parameters.add(new BasicNameValuePair(pairs[0], pairs[1]));
					}
				}
				httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			}
			CookieUtil.addCookie(httpPost);
			HttpResponse response = httpClient.execute(httpPost);
			if(200 == response.getStatusLine().getStatusCode()){
				CookieUtil.saveCookie(response);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				msg.what = HrssConstants.SUCCESS;
				msg.obj = data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.what = HrssConstants.FAILURE;
			msg.obj = "请求服务器失败，请稍后再试";
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		handler.sendMessage(msg);
	}

	private HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		ClientConnectionManager mgr = httpClient.getConnectionManager();
		HttpParams params = httpClient.getParams();
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
				params, mgr.getSchemeRegistry()), params);
		return httpClient;
	}

}