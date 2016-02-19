package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;

/**
 * 支付成功回调页面
 */
public class ShowOrderActivity extends BaseAcitivity
{
	@ViewInject(R.id.consigner)
	private TextView consignerTV;
	
	@ViewInject(R.id.mobile)
	private TextView mobileTV;
	
	@ViewInject(R.id.address)
	private TextView addressTV;
	
	@ViewInject(R.id.time)
	private TextView timeTV;
	
	@ViewInject(R.id.orderFee)
	private TextView orderFeeTV;
	
	@ViewInject(R.id.fare)
	private TextView fareTV;
	
	@ViewInject(R.id.pay_method)
	private TextView payMethodTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.activity_show_order, null);
		ViewUtils.inject(this, v);
		initView();
		setContentView(v);
		
	}

	private void initView() {
		Intent intent = getIntent();
		
		consignerTV.setText(intent.getStringExtra("consigner"));
		mobileTV.setText(intent.getStringExtra("mobile"));
		addressTV.setText(intent.getStringExtra("address"));
		timeTV.setText(intent.getStringExtra("time"));
		orderFeeTV.setText(intent.getStringExtra("orderFee"));
		fareTV.setText(intent.getStringExtra("fare"));
		payMethodTV.setText(intent.getStringExtra("payMethod"));
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}
}
