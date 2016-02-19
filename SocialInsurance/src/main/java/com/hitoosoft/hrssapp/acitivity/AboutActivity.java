package com.hitoosoft.hrssapp.acitivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		((TextView) findViewById(R.id.titlebar)).setText("关于我们");
		((ImageView) findViewById(R.id.actionbar_right))
				.setVisibility(View.INVISIBLE);

	}

}