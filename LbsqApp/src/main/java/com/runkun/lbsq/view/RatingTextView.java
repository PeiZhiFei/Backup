package com.runkun.lbsq.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.Tools;

public class RatingTextView extends LinearLayout {
	private List<TextView> texts = new ArrayList<TextView>();
	private List<String> string = new ArrayList<String>();
	private Context context;

	@SuppressLint("NewApi")
	public RatingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	private void init() {
		initData();
		setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 0; i < 5; ++i) {
			TextView textView = getStarImageView();
			textView.setText(string.get(i));
			textView.setTextSize(10);
			int x = Tools.getWidth(context) / 5 - 30;
			LinearLayout.LayoutParams layoutParams = new LayoutParams(x, x);
			layoutParams.setMargins(20, 0, 0, 0);
			addView(textView, layoutParams);
		}
		setPadding(5, 0, 5, 0);
		setGravity(Gravity.CENTER);
	}

	private TextView getStarImageView() {
		TextView textView = new TextView(context);
		ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(20, 20);
		textView.setLayoutParams(para);
		textView.setPadding(0, 0, 10, 0);
		textView.setGravity(Gravity.CENTER);
		return textView;

	}

	private void initData() {
		string.add(Tools.getStr(context, R.string.rpoor));
		string.add(Tools.getStr(context, R.string.rcommon));
		string.add(Tools.getStr(context, R.string.rsatisfy));
		string.add(Tools.getStr(context, R.string.rvsatisfy));
		string.add(Tools.getStr(context, R.string.rperfect));
	}

	public RatingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public RatingTextView(Context context) {
		super(context);
		this.context = context;
		init();
	}

}
