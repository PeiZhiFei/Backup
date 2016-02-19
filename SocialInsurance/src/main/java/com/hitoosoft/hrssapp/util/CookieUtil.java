package com.hitoosoft.hrssapp.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

public class CookieUtil {
	
	public static Map<String, String> CookieContiner = new HashMap<String, String>();// 缓存cookie信息

	/**
	 * 从header中取出Set-Cookie，只有第一次请求服务器会返回，以后不再返回（除非服务器生成了新的sessionid）
	 * @param response
	 */
	public static void saveCookie(HttpResponse response) {
		Header[] headers = response.getHeaders("Set-Cookie");
		if (headers == null)
			return;
		for (int i = 0; i < headers.length; i++) {
			String cookie = headers[i].getValue();
			String[] cookieValues = cookie.split(";");
			for (int j = 0; j < cookieValues.length; j++) {
				String[] keyPair = cookieValues[j].split("=");
				String key = keyPair[0].trim();
				String value = keyPair.length > 1 ? keyPair[1].trim() : "";
				CookieContiner.put(key, value);
			}
		}
	}

	public static void addCookie(HttpPost httpPost) {
		if(CookieContiner.size() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			sb.append(key);
			sb.append("=");
			sb.append(value);
			sb.append(";");
		}
		httpPost.addHeader("Cookie", sb.toString());
	}

	public static void addCookie(HttpGet httpGet) {
		if(CookieContiner.size() == 0)
			return;
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			sb.append(key);
			sb.append("=");
			sb.append(value);
			sb.append(";");
		}
		httpGet.addHeader("Cookie", sb.toString());
	}
}