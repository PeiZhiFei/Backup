package com.hitoosoft.hrssapp.util;

import java.util.HashMap;
import java.util.Map;

public class HrssConstants {

	public static final String REGION = "370800";// 部署地市 济宁
	public static final String REGION_NAME = "济宁市";
	public static final String REGION_JINING = "370800";
	public static final String REGION_WEIHAI = "371000";
	public static final String REGION_TAIAN = "370900";
	public static final String REGION_BINZHOU = "371600";// 唉，滨州。。

	public static final String SERVER_URL = "http://60.211.255.251/";
	// http://www.jnsi.gov.cn/hrssapp_release.apk
	// 获取服务器端app版本的文件地址
	// public static final String APP_VERSION_FILE_PATH = SERVER_URL
	// + "hrssmsp/app-version.xml";
	public static final String APP_VERSION_FILE_PATH = "http://www.jnsi.gov.cn/app-version.xml";
	// 资讯请求地址，需要制定两个参数：newsType资讯类型（rsxw人社新闻，tzgg通知公告，bszn办事指南，zxzc最新政策，hmzc惠民政策，zkxx招考信息），from
	public static final String HRSS_NEWS_URL = SERVER_URL
			+ "hrssmsp/news/getNewList.do";
	// 缴费记录请求地址
	public static final String HRSSMSP_URL_JFJL = SERVER_URL
			+ "hrssmsp/siquery/forwardQueryCbjfqk.do";
	// 账户查询请求地址
	public static final String HRSSMSP_URL_ZHCX = SERVER_URL
			+ "hrssmsp/siquery/forwardQueryGrzhqk.do";
	// 待遇查询请求地址
	public static final String HRSSMSP_URL_DYCX = SERVER_URL
			+ "hrssmsp/siquery/forwardQueryDyqk.do";
	// 绑定参保人员
	public static final String HRSSMSP_URL_BIND = SERVER_URL
			+ "hrssmsp/hrssapp/bind.do";
	// 查询需要推送的资讯消息
	public static final String HRSSMSP_SERVICE_MSG_NEWS = SERVER_URL
			+ "hrssmsp/hrssapp/getNewsServiceMsg.do";
	// 服务电话
	public static final String HRSSMSP_GUIDE_FWDH = SERVER_URL
			+ "hrssmsp/weixin/other/jnfwdh.html";
	// 12333
	public static final String JNSI_12333 = "http://www.jnsi.gov.cn/modules/jnhrss/jsp/site/ShowMessage.jsp?";
	// 就业招聘
	public static final String JNSI_KZRCW = "http://www.kzrcw.com/Index/FindJobs";
	// 成绩查询
	public static final String JNSI_CJCX = "http://www.jiningrsks.gov.cn";

	public final static int SUCCESS = 1;// httpPost请求成功返回
	public final static int FAILURE = 2;// httpPost请求失败

	public final static int PAGE_SIZE_NEWS = 10;// 资讯一页显示15条

	public static Map<String, String> newsMap = new HashMap<String, String>();
	static {
		newsMap.put("新闻", "rsxw");
		newsMap.put("公告", "tzgg");
		newsMap.put("政策", "zxzc");
		newsMap.put("招考", "zkxx");
		newsMap.put("办事流程", "bszn");
	}
}