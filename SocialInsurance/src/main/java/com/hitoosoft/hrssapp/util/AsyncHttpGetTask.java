package com.hitoosoft.hrssapp.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class AsyncHttpGetTask implements Runnable {
	private final String url;
	private final Handler handler;

	private int arg = 0;

	public AsyncHttpGetTask(String url, Handler handler) {
		this.url = url;
		this.handler = handler;
	}

	public AsyncHttpGetTask(String url, Handler handler, int arg) {
		this.url = url;
		this.handler = handler;
		this.arg = arg;
	}

	@Override
	public void run() {
		HttpClient httpClient = HttpClientFactory.getHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接网络超时时间
		HttpGet httpGet = new HttpGet(url);
		Message msg = Message.obtain();
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (200 == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				msg.what = HrssConstants.SUCCESS;
				msg.arg1 = arg;
				msg.obj = data;
				if (null != entity) {
					entity.consumeContent();
				}
				httpGet.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.what = HrssConstants.FAILURE;
			msg.obj = "请求服务器失败，请稍后再试";
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