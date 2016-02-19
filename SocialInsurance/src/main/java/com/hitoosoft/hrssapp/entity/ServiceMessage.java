package com.hitoosoft.hrssapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hitoosoft.hrssapp.util.HrssConstants;

/**
 * 消息推送实体类
 */
public class ServiceMessage implements Serializable{

	private static final long serialVersionUID = 1L;

	private String title;
	private String url;
	
	public static List<ServiceMessage> getDataFromJson(JSONArray jsonArray) {
		if(null == jsonArray){
			return new ArrayList<ServiceMessage>();
		}
		try {
			List<ServiceMessage> msgList = new ArrayList<ServiceMessage>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.get(i);
				ServiceMessage msg = getEntity(obj);
				msgList.add(msg);
			}
			return msgList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ArrayList<ServiceMessage>();
	}

	public static ServiceMessage getEntity(JSONObject obj) {
		ServiceMessage entity = new ServiceMessage();
		try {
			entity.setTitle(obj.getString("title"));
			entity.setUrl(HrssConstants.SERVER_URL + obj.getString("url"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}