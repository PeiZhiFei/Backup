package com.runkun.lbsq.busi.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;


public class GuideActivity extends BaseAcitivity implements
		OnPageChangeListener {

	private static final int[] resource = new int[] { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	Button button;
	ImageView dot, dots[];
	ViewPager viewPager;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_guide);
		setSwipeBackEnable (false);
		button = (Button) findViewById(R.id.guide_button);
		initDot();
		MyFragmentStatePager adpter = new MyFragmentStatePager(
				getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(adpter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(3);
		button.setOnClickListener (new OnClickListener ()
		{

			@Override
			public void onClick(View v)
			{
				setGuided ();
				goHome ();

			}
		});

	}
	private void goHome() {
		Intent intent = new Intent(activity, LoginActivity.class);
		activity.startActivity (intent);
		AnimUtil.animToFinish (GuideActivity.this);
	}
	//设置已经引导过，下次不再引导
	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean ("isFirstIn", false);
		// 提交修改
		editor.apply ();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		setCurDot(position);
		setCurView(position);
		if (position == resource.length - 1) {
			button.setVisibility(View.VISIBLE);
		} else {
			button.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	private void initDot() {
		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.viewGroup);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				15, 15);
		layoutParams.setMargins(5, 5, 5, 5);
		dots = new ImageView[resource.length];
		for (int i = 0; i < dots.length; i++) {
			dot = new ImageView(this);
			dot.setLayoutParams(layoutParams);
			dots[i] = dot;
			dots[i].setTag(i);
			if (i == 0) {
				dots[i].setBackgroundResource(R.drawable.guide_white_dot);
			} else {
				dots[i].setBackgroundResource(R.drawable.guide_green_dot);
			}
			viewGroup.addView(dots[i]);
		}
	}

	private void setCurDot(int position) {
		for (int i = 0; i < dots.length; i++) {
			if (position == i) {
				dots[i].setBackgroundResource(R.drawable.guide_white_dot);
			} else {
				dots[i].setBackgroundResource(R.drawable.guide_green_dot);
			}
		}
	}

	private void setCurView(int position) {
		if (position < 0 || position > resource.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	public class MyFragmentStatePager extends FragmentStatePagerAdapter {

		public MyFragmentStatePager(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			return new MyFragment(position);
		}

		@Override
		public int getCount() {
			return resource.length;
		}
	}

	@SuppressLint("ValidFragment")
	public class MyFragment extends Fragment {
		private final int position;

		public MyFragment(int position) {
			this.position = position;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setScaleType (ImageView.ScaleType.CENTER_CROP);
			imageView.setImageResource(resource[position]);
			return imageView;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AnimUtil.animBack(this);
	}

}
