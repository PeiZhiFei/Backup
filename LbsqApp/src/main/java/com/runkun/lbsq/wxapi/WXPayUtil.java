package com.runkun.lbsq.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.pay.WePayUtil;
import com.runkun.lbsq.utils.Tools;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * This file's encoding must be UTF-8
 *
 * @author yutong
 *
 */
public class WXPayUtil {

	private IWXAPI msgApi;
	private PayReq req;
	private Map<String, String> uionOrderResult;

	private Context mContext;
	private IWXPay mWXPay;

	public WXPayUtil(Context context) {
		mContext = context;
		msgApi = WXAPIFactory.createWXAPI(context, null);
		req = new PayReq();
		((BaseAcitivity) mContext).dialogInit();
	}

	public void pay(IWXPay wxPay) {
		mWXPay = wxPay;
		new GetPrepayIdTask().execute();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		@Override
		protected void onPreExecute() {
			((BaseAcitivity) mContext).dialogProgress((Activity) mContext,
					Tools.getStr(mContext, R.string.PCOMMITING));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			((BaseAcitivity) mContext).dialogDismiss();

			String prePayId = result.get("prepay_id");

			if (prePayId == null) {
				mWXPay.onError(Tools.getStr(mContext, R.string.PCREATFAIL));
			}
			uionOrderResult = result;
			mWXPay.onPrePayIdReady(prePayId);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = mWXPay.genProductArgs();

			byte[] buf = WePayUtil.httpPost(url, entity);
			if (buf == null) {
				return null;
			}

			String content = new String(buf);
			Map<String, String> xml = decodeXml(content);
			return xml;
		}

		private Map<String, String> decodeXml(String content) {

			try {
				Map<String, String> xml = new HashMap<String, String>();
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(new StringReader(content));
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {

					String nodeName = parser.getName();
					switch (event) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if ("xml".equals(nodeName) == false) {
							xml.put(nodeName, parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					}
					event = parser.next();
				}

				return xml;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}
	}

	public void genPayReq() {
		req.appId = WePayUtil.APP_ID;
		req.partnerId = WePayUtil.MCH_ID;
		req.prepayId = uionOrderResult.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = mWXPay.getNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);
		Log.e("orion", signParams.toString());
	}

	/**
	 *
	 */
	public void sendPayReq() {
		msgApi.registerApp(WePayUtil.APP_ID);
		msgApi.sendReq(req);
	}

	/**
	 *
	 *
	 * @param params
	 *
	 * @return
	 */
	public String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WePayUtil.API_KEY);

		String packageSign = WePayUtil.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	/**
	 * xml
	 *
	 * @param params
	 *            NameValuePair
	 * @return xml
	 */
	public String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WePayUtil.API_KEY);

		String appSign = WePayUtil.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}
}
