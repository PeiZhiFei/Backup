package com.hitoosoft.hrssapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class SpFactory {

	/**
	 * 将社保信息的人员id存起来
	 */
	public static boolean saveBindInfo(Context context, String ryId, String sfzh, String name,
			String phone, String pass) {
		boolean flag = false;
		SharedPreferences fac = context.getSharedPreferences("bindinfo", Context.MODE_PRIVATE);
		// 对数据进行编辑
		SharedPreferences.Editor editor = fac.edit();
		editor.putString("ryId", ryId);
		editor.putString("sfzh", sfzh);
		editor.putString("name", name);
		editor.putString("phone", phone);
		editor.putString("pass", pass);
		flag = editor.commit();
		return flag;
	}

	/**
	 * 获取手机中保存的社保信息人员id
	 */
	public static Map<String, String> getBindInfo(Context context) {
		SharedPreferences fac = context.getSharedPreferences("bindinfo", Context.MODE_PRIVATE);
		String ryId = fac.getString("ryId", "");
		if (!"".equals(ryId)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ryId", ryId);
			map.put("sfzh", fac.getString("sfzh", ""));
			map.put("name", fac.getString("name", ""));
			map.put("phone", fac.getString("phone", ""));
			map.put("pass", fac.getString("pass", ""));
			return map;
		}
		return null;
	}

	/**
	 * 记录推送资讯消息下次取值的起始时间
	 * @param nextDate yyyyMMddHHmmss
	 */
	public static boolean saveNextDate(Context context, Date nextDate) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SharedPreferences fac = context.getSharedPreferences("parainfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = fac.edit();
		editor.putString("nextDate", sdf.format(nextDate));
		flag = editor.commit();
		return flag;
	}

	/**
	 * 获取推送资讯消息下次取值的起始时间
	 */
	public static String getNextDate(Context context) {
		SharedPreferences fac = context.getSharedPreferences("parainfo", Context.MODE_PRIVATE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return fac.getString("nextDate", sdf.format(new Date()));
	}

	/**
	 * 记录订阅的消息类型
	 */
	public static boolean saveSubscribeMsg(Context context, boolean changeAlert, boolean payBill) {
		boolean flag = false;
		SharedPreferences fac = context.getSharedPreferences("parainfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = fac.edit();
		editor.putBoolean("changeAlert", changeAlert);
		editor.putBoolean("payBill", payBill);
		flag = editor.commit();
		return flag;

	}

	/**
	 * 获取订阅的消息类型
	 */
	public static boolean[] getSubscribeMsg(Context context) {
		SharedPreferences fac = context.getSharedPreferences("parainfo", Context.MODE_PRIVATE);
		boolean[] result = new boolean[2];
		result[0] = fac.getBoolean("changeAlert", false);
		result[1] = fac.getBoolean("payBill", false);
		return result;
	}

}