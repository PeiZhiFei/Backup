package laiyi.tobacco.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 新闻资讯实体类实现了Parcelable序列化，效率更高
 */
public class News implements Parcelable
{

	private String newsID;// 资讯ID
	private String newsTitle;// 标题
	private String publisher;// 发布者
	private String publishDate;// 发布日期
	private String picURL;// 展示图片URL
	private String source;// 资讯来源
	private String publishWay;// 发布渠道 1微信 2APP
	private String newsType;// 资讯类别 11人社新闻 12通知公告 14办事指南 15最新政策 16惠民政策 17招考信息
	private String publishMode;// 发布方式 1是群发，2是单发
	private String perID;// 接收人(资讯单发时指定)
	private String newsSta;// 资讯状态 0草稿 1待审核 2已审核 3已发布 9已过期
	private String msgFlag;// 是否推送服务消息 0不推送 1推送
	private String topFlag;// 是否置顶标识0或null非置顶，1为置顶
	private String descr;// 描述

	public static List<News> getDataFromJson(JSONArray jsonArray) {
		if (null == jsonArray) {
			return new ArrayList<News> ();
		}
		try {
			List<News> newsList = new ArrayList<News> ();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.get(i);
				News news = getEntity(obj);
				newsList.add(news);
			}
			return newsList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ArrayList<News> ();
	}

	public static News getEntity(JSONObject obj) {
		News entity = new News();
		try {
			entity.setNewsID(obj.getString("newsID"));
			entity.setNewsTitle(obj.getString("newsTitle"));
			entity.setPublisher(obj.getString("publisher"));
			Calendar calendar = Calendar.getInstance ();
			calendar.setTimeInMillis(obj.getLong("publishDate"));
			entity.setPublishDate(formatDate(calendar.getTime()));
			entity.setPicURL(obj.getString("picURL"));
			entity.setSource(obj.getString("source"));
			entity.setPublishWay(obj.getString("publishWay"));
			entity.setNewsType(obj.getString("newsType"));
			entity.setPublishMode(obj.getString("publishMode"));
			entity.setPerID(obj.getString("perID"));
			entity.setNewsSta(obj.getString("newsSta"));
			entity.setMsgFlag(obj.getString("msgFlag"));
			entity.setTopFlag(obj.getString("topFlag"));
			entity.setDescr(obj.getString("descr"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}

	public String getNewsID() {
		return newsID;
	}

	public void setNewsID(String newsID) {
		this.newsID = newsID;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPublishWay() {
		return publishWay;
	}

	public void setPublishWay(String publishWay) {
		this.publishWay = publishWay;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getPublishMode() {
		return publishMode;
	}

	public void setPublishMode(String publishMode) {
		this.publishMode = publishMode;
	}

	public String getPerID() {
		return perID;
	}

	public void setPerID(String perID) {
		this.perID = perID;
	}

	public String getNewsSta() {
		return newsSta;
	}

	public void setNewsSta(String newsSta) {
		this.newsSta = newsSta;
	}

	public String getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(String topFlag) {
		this.topFlag = topFlag;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(newsID);
		parcel.writeString(newsTitle);
		parcel.writeString(publisher);
		parcel.writeString(publishDate);
		parcel.writeString(picURL);
		parcel.writeString(source);
		parcel.writeString(publishWay);
		parcel.writeString(newsType);
		parcel.writeString(publishMode);
		parcel.writeString(perID);
		parcel.writeString(newsSta);
		parcel.writeString(msgFlag);
		parcel.writeString(topFlag);
		parcel.writeString(descr);
	}

	private static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat ("yyyy年MM月dd日",
				Locale.getDefault ());
		return format.format(date);
	}

	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
		@Override
		public News createFromParcel(Parcel parcel) {
			News news = new News();
			news.newsID = parcel.readString();
			news.newsTitle = parcel.readString();
			news.publisher = parcel.readString();
			news.publishDate = parcel.readString();
			news.picURL = parcel.readString();
			news.source = parcel.readString();
			news.publishWay = parcel.readString();
			news.newsType = parcel.readString();
			news.publishMode = parcel.readString();
			news.perID = parcel.readString();
			news.newsSta = parcel.readString();
			news.msgFlag = parcel.readString();
			news.topFlag = parcel.readString();
			news.descr = parcel.readString();
			return news;
		}

		@Override
		public News[] newArray(int i) {
			return new News[0];
		}
	};

}