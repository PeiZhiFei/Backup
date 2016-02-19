package com.runkun.lbsq.wxapi;

public interface IWXPay {
	String genProductArgs();

	void onPrePayIdReady(String prePayId);

	void onError(String msg);

	String getNonceStr();
}
