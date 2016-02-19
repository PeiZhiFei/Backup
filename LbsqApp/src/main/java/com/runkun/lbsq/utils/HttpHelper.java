package com.runkun.lbsq.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpHelper {


	public static String getURLByCommond(String command) {
		return MyConstant.API_BASE_URL + command;
	}

	@Deprecated
	public static void sendByPost(String url, RequestParams rp,
			RequestCallBack<String> callback) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, url, rp, callback);
	}

	public static HttpHandler<String> postByCommand(String command,
			RequestParams rp, RequestCallBack<String> callback) {
		HttpUtils httpUtils = new HttpUtils(15000);
		return httpUtils.send(HttpMethod.POST, getURLByCommond(command), rp,
				callback);
	}

	public static String getPrefParams(Context context, String key) {
		SharedPreferences sf = context.getSharedPreferences(MyConstant.FILE_NAME,
				Context.MODE_PRIVATE);
		return sf.getString(key, "");
	}

	public static boolean isSuccess(JSONObject result) {
		try {
			return 200 == result.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
}
