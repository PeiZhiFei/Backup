package com.runkun.lbsq.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.runkun.lbsq.R;
import com.runkun.lbsq.pay.WePayUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	public static int ERR_CODE = -3;

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, WePayUtil.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX
				&& resp.errCode == 0) {
			ERR_CODE = 0;
		} else if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX
				&& resp.errCode == -1) {
			ERR_CODE = -1;
		} else if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX
				&& resp.errCode == -2) {
			ERR_CODE = -2;
		}
		finish();
	}
}
