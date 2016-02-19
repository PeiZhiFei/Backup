package com.hitoosoft.hrssapp.acitivity;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.entity.OperResult;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.AsyncHttpPostTask;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.util.SpFactory;
import com.hitoosoft.hrssapp.util.ToastUtil;

@SuppressLint("HandlerLeak")
public class BindActivity extends BaseActivity {

	private String sfzh, name, pass, phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		((TextView) findViewById(R.id.titlebar)).setText("用户绑定");
		findViewById(R.id.actionbar_right).setVisibility(View.INVISIBLE);
		final EditText etSfzh = (EditText) findViewById(R.id.et_sfzh);
		final EditText etName = (EditText) findViewById(R.id.et_name);
		final EditText etPhone = (EditText) findViewById(R.id.et_phone);
		final EditText etPass = (EditText) findViewById(R.id.et_pass);
		String oldName = getIntent().getStringExtra("name");
		String oldSfzh = getIntent().getStringExtra("sfzh");
		String oldPhone = getIntent().getStringExtra("phone");
		if (!TextUtils.isEmpty(oldName)) {
			etName.setText(oldName);
		}
		if (!TextUtils.isEmpty(oldSfzh)) {
			etSfzh.setText(oldSfzh);
		}
		if (!TextUtils.isEmpty(oldPhone)) {
			etPhone.setText(oldPhone);
		}
		Button bindButton = (Button) findViewById(R.id.bind_button);
		bindButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sfzh = etSfzh.getText().toString().trim();
				name = etName.getText().toString().trim();
				pass = etPass.getText().toString().trim();
				phone = etPhone.getText().toString().trim();

				if (TextUtils.isEmpty(sfzh)) {
					animShakeText(etSfzh);
					ToastUtil.toast(BindActivity.this, "身份证号码不能为空");
					return;
				}
				if (TextUtils.isEmpty(name)) {
					animShakeText(etName);
					ToastUtil.toast(BindActivity.this, "参保人姓名不能为空");
					return;
				}
				if (TextUtils.isEmpty(pass)
						&& HrssConstants.REGION_JINING
								.equals(HrssConstants.REGION)) {
					animShakeText(etPass);
					ToastUtil.toast(BindActivity.this, "社保查询密码不能为空");
					return;
				}
				String[] paras = new String[] { "sfzh=" + sfzh, "name=" + name,
						"pass=" + pass, "phone=" + phone };
				new AsyncHttpPostTask(handler, HrssConstants.HRSSMSP_URL_BIND,
						paras).start();
			}
		});
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			String data = (String) msg.obj;
			if (what == HrssConstants.SUCCESS) {
				OperResult operResult = OperResult.fromJsonToObject(data);
				if ("W0001".equals(operResult.getOperCode())) {// 登录成功
					String ryId = "";
					try {
						ryId = operResult.getData().getString("ryID");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 将绑定的参保人员信息存储在Sharedperence中
					SpFactory.saveBindInfo(BindActivity.this, ryId, sfzh, name,
							phone, pass);
					Intent reIntent = new Intent();
					reIntent.putExtra("sfzh", sfzh);
					reIntent.putExtra("name", name);
					reIntent.putExtra("phone", phone);
					setResult(Activity.RESULT_OK, reIntent);// 返回绑定的信息
					ToastUtil
							.toast(BindActivity.this, operResult.getOperDesc());
					AnimUtil.animBackSlideFinish(BindActivity.this);
				} else if ("W0002".equals(operResult.getOperCode())) {
					ToastUtil.toast(BindActivity.this,
							"绑定失败：" + operResult.getOperDesc());
				} else if ("W0003".equals(operResult.getOperCode())) {
					ToastUtil.toast(BindActivity.this,
							"服务器异常：" + operResult.getOperDesc());
				}
			} else {
				ToastUtil.toast(BindActivity.this, data);
			}
		}
	};

	public void animShakeText(View view) {
		Animation shakeAnim = new TranslateAnimation(0, 10, 0, 0);
		shakeAnim.setInterpolator(new CycleInterpolator(5));
		shakeAnim.setDuration(500);
		view.startAnimation(shakeAnim);
	}

}