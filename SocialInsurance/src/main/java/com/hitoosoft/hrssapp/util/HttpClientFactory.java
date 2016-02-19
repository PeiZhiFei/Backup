package com.hitoosoft.hrssapp.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {

	private static HttpClient httpClient = null;

	public static HttpClient getHttpClient() {
		if (null == httpClient) {
			httpClient = new DefaultHttpClient();
			ClientConnectionManager mgr = httpClient.getConnectionManager();
			HttpParams params = httpClient.getParams();
			httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
					params, mgr.getSchemeRegistry()), params);
		}
		return httpClient;
	}
}