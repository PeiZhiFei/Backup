package com.hitoosoft.hrssapp.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OperResult {
	
	/*
	 * W0000 初始，未成功返回状态
	 * W0001 成功
	 * W0002 失败
	 * W0003 异常
	 */
	
	private String operCode;// 操作结果状态码
	private String operDesc;// 操作结果状态描述
	private JSONObject data;// 操作结果返回的单个数据
	private JSONArray dataArray;// 操作结果返回的数组数据

	public OperResult(String operCode, String operDesc, JSONObject data, JSONArray dataArray) {
		this.operCode = operCode;
		this.operDesc = operDesc;
		this.data = data;
		this.dataArray = dataArray;
	}

	/**
	 * 将json字符串转化为对象
	 * 
	 * @param dataArray
	 * @return
	 */
	public static OperResult fromJsonToObject(String result) {
		String operCode = "";
		String operDesc = "";
		JSONObject data = null;
		JSONArray dataArray = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			operCode = jsonObject.getString("operCode");
			operDesc = jsonObject.getString("operDesc");
			data = jsonObject.getJSONObject("data");
			dataArray = jsonObject.getJSONArray("dataArray");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new OperResult(operCode, operDesc, data, dataArray);
	}

	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getOperDesc() {
		return operDesc;
	}
	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	public JSONArray getDataArray() {
		return dataArray;
	}
	public void setDataArray(JSONArray dataArray) {
		this.dataArray = dataArray;
	}
}