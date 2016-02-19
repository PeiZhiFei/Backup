package com.runkun.lbsq.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * session
 */
public class HttpSessionUtil
{

	private String PHPSESSID;
	private DefaultHttpClient httpClient;
	private BasicHttpParams httpParams;
	private SharedPreferences sharedPreferences;

	public void getHttpClient(Context context) {
		sharedPreferences = context.getSharedPreferences("wydinit",
				Context.MODE_PRIVATE);
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		this.httpClient = new DefaultHttpClient(httpParams);
	}

	public String doPost(Context context, String url, List<NameValuePair> params)
			throws JSONException {
		getHttpClient(context);
		HttpPost httpRequest = new HttpPost(url);
		String strResult = null;
		if (params != null && params.size() > 0) {
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		long firstTime = sharedPreferences.getLong("dateTime", 0);
		if (firstTime != 0
				&& System.currentTimeMillis() - firstTime < (1000 * 60 * 10)) {
			PHPSESSID = sharedPreferences.getString("PHPSESSID", null);
//			Log.e("log", "sess   " + PHPSESSID);
		}
		if (null != PHPSESSID) {
			httpRequest.setHeader("Cookie", "PHPSESSID=" + PHPSESSID);
			Editor edit = sharedPreferences.edit();
			edit.putLong("dateTime", System.currentTimeMillis());
			edit.apply();
		}
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			CookieStore cookieStore = httpClient.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				if ("PHPSESSID".equals(cookies.get(i).getName())) {
					PHPSESSID = cookies.get(i).getValue();
					Editor edit = sharedPreferences.edit();
					edit.putString("PHPSESSID", PHPSESSID);
					edit.putLong("dateTime", System.currentTimeMillis());
					edit.apply();
//					Log.e("log", "sess   " + PHPSESSID);
					break;
				}
			}
		} else {
			strResult = String.valueOf(httpResponse.getStatusLine()
					.getStatusCode());
			JSONObject resut = new JSONObject();
			resut.put("404", strResult);
			strResult = resut.toString();
		}
		return strResult;
	}

}
