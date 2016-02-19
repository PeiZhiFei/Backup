package com.hitoosoft.hrssapp.acitivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.SpFactory;
import com.hitoosoft.hrssapp.util.ToastUtil;

public class SubscribeMsgActivity extends BaseActivity {

	private CheckBox cbAllcheck, cbChangealert, cbPaybill;
	private Button btChoose;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribemsg);
		((TextView) findViewById(R.id.titlebar)).setText("消息订阅");
		findViewById(R.id.actionbar_right).setVisibility(View.INVISIBLE);

		cbAllcheck = (CheckBox) findViewById(R.id.cb_allcheck);
		cbChangealert = (CheckBox) findViewById(R.id.cb_chanagealert);
		cbPaybill = (CheckBox) findViewById(R.id.cb_paybill);
		// 设置状态
		boolean[] result = SpFactory.getSubscribeMsg(this);
		cbChangealert.setChecked(result[0]);
		cbPaybill.setChecked(result[1]);
		if (result[0] & result[1]) {
			cbAllcheck.setChecked(true);
		}

		cbAllcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				cbAllcheck.setChecked(isChecked);
				cbChangealert.setChecked(isChecked);
				cbPaybill.setChecked(isChecked);
			}
		});

		btChoose = (Button) findViewById(R.id.bt_choose);
		btChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SpFactory.saveSubscribeMsg(SubscribeMsgActivity.this,
						cbChangealert.isChecked(), cbPaybill.isChecked());
				ToastUtil.toast(SubscribeMsgActivity.this, "订阅成功");
				AnimUtil.animBackSlideFinish(SubscribeMsgActivity.this);
			}
		});
	}

}
