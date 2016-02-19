/**
 * 
 */
package com.hitoosoft.hrssapp.acitivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.view.ColorAnimationView;

public class GuideActivity extends FragmentActivity {
	private static final int[] resource = new int[] { R.drawable.w1,
			R.drawable.w2, R.drawable.w3, R.drawable.w4, R.drawable.w5 };
	private static final int COLOR1 = 0xff00cc99;
	private static final int COLOR2 = 0xff00ffcc;
	private static final int COLOR3 = 0xff00ffff;
	private static final int COLOR4 = 0xff00cccc;
	private static final int COLOR5 = 0xff00ccff;
	private static final int COLOR6 = 0xff00ff99;
	Button button;
	TextView textView;
	ImageView dot, dots[];
	ViewPager viewPager;
	Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		textView = (TextView) findViewById(R.id.guide_title);
		button = (Button) findViewById(R.id.guide_button);

		typeface = Typeface.createFromAsset(getAssets(), "font/texts.TTF");
		textView.setTypeface(typeface);
		textView.setText("各项功能尽在指尖");
		button.setTypeface(typeface);
		initDot();
		MyFragmentStatePager adpter = new MyFragmentStatePager(
				getSupportFragmentManager());
		ColorAnimationView colorAnimationView = (ColorAnimationView) findViewById(R.id.ColorAnimationView);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(adpter);
		// 这里设置就一次都加载好，因为图片质量降低了
		viewPager.setOffscreenPageLimit(5);
		if (getIntent().getBooleanExtra("welcome", true)) {

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GuideActivity.this,
							MainActivity.class);
					startActivity(intent);
					AnimUtil.animToSlideFinish(GuideActivity.this);
				}
			});
		} else {
			button.setVisibility(View.INVISIBLE);

		}

		colorAnimationView.setmViewPager(viewPager, resource.length, COLOR1,
				COLOR2, COLOR3, COLOR4, COLOR5, COLOR6);
		colorAnimationView
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageSelected(int position) {
						setCurDot(position);
						setCurView(position);
						setGuideTitle(position);
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});
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
			dots[i].setOnClickListener(onClick);
			if (i == 0) {
				dots[i].setBackgroundResource(R.drawable.dot2);
			} else {
				dots[i].setBackgroundResource(R.drawable.dot1);
			}
			viewGroup.addView(dots[i]);
		}
	}

	private void setCurDot(int position) {
		for (int i = 0; i < dots.length; i++) {
			if (position == i) {
				dots[i].setBackgroundResource(R.drawable.dot2);
			} else {
				dots[i].setBackgroundResource(R.drawable.dot1);
			}
		}
	}

	OnClickListener onClick = new OnClickListener() {
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			setCurView(position);
		}
	};

	private void setCurView(int position) {
		if (position < 0 || position > resource.length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	private void setGuideTitle(int position) {
		switch (position) {
		case 0:
			textView.setText("各项功能尽在指尖");
			break;
		case 1:
			textView.setText("缴费详情清晰明了");
			break;
		case 2:
			textView.setText("贴心的电话黄页");
			break;
		case 3:
			textView.setText("附件药店随时查询");
			break;
		case 4:
			textView.setText("新闻资讯不再错过");
			break;
		}
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
			imageView.setImageResource(resource[position]);
			return imageView;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AnimUtil.animBackSlide(this);
	}
}
