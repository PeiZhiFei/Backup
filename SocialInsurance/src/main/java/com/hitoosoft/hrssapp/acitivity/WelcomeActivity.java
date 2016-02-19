package com.hitoosoft.hrssapp.acitivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.view.TypeTextView;
import com.hitoosoft.hrssapp.view.TypeTextView.OnTypeViewListener;
import com.hitoosoft.hrssapp.view.TypeTextView2;

public class WelcomeActivity extends Activity {
	boolean first;
	SharedPreferences fac;
	SharedPreferences.Editor editor;
	TypeTextView typeTextView;
	TypeTextView2 typeTextView2;

	Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (first) {
				editor.putBoolean("first", false);
				editor.commit();
				Intent intent = new Intent(WelcomeActivity.this,
						GuideActivity.class);

				startActivity(intent);
				// AnimUtil.animToSlideFinish(WelcomeActivity.this);
				AnimUtil.animToGuideFinish(WelcomeActivity.this);
			} else {
				Intent intent = new Intent(WelcomeActivity.this,
						MainActivity.class);
				startActivity(intent);
				AnimUtil.animToSlideFinish(WelcomeActivity.this);
			}

			return false;
		}
	});

	Handler mHandler2 = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			typeTextView.start(getResources().getString(R.string.app_name));
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// LinearLayout linearLayout = new LinearLayout(this);
		// linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
		// Gravity.CENTER));
		// linearLayout.setBackgroundResource(R.drawable.welcome);
		// TypeTextView typeTextView = new TypeTextView(this);
		// typeTextView.setGravity(Gravity.CENTER);
		// typeTextView.setTextSize(40);
		// linearLayout.addView(typeTextView, new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// setContentView(linearLayout);
		setContentView(R.layout.activity_welcome);
		typeTextView = (TypeTextView) findViewById(R.id.typeTxtId);
		typeTextView2 = (TypeTextView2) findViewById(R.id.typeTxtId2);
		AnimUtil.animRightToCenter(typeTextView2);

		// typeTextView2.start("掌上社保·享受生活");
		mHandler2.sendMessageDelayed(Message.obtain(), 1000);

		typeTextView.setOnTypeViewListener(new OnTypeViewListener() {
			@Override
			public void onTypeStart() {
			}

			@Override
			public void onTypeOver() {
				mHandler.sendMessageDelayed(Message.obtain(), 100);

			}
		});

		fac = getSharedPreferences("first", Context.MODE_PRIVATE);
		editor = fac.edit();
		first = fac.getBoolean("first", true);
		typeTextView.setSound(first);

	}
}