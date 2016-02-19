package com.hitoosoft.hrssapp.acitivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.fragment.GuideFragment;
import com.hitoosoft.hrssapp.fragment.IFragmentBack;
import com.hitoosoft.hrssapp.fragment.IFragmentChanger;
import com.hitoosoft.hrssapp.fragment.NewsFragment;
import com.hitoosoft.hrssapp.fragment.WebFragment;
import com.hitoosoft.hrssapp.service.HrssService;
import com.hitoosoft.hrssapp.test.SiServiceFragment2;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.util.ToastUtil;
import com.hitoosoft.hrssapp.util.UpdateManager;

public class MainActivity extends BaseActivity implements OnClickListener,
		IFragmentChanger {

	private NewsFragment newsFragment;
	private GuideFragment guideFragment;
//	private SiServiceFragment serviceFragment;
	private SiServiceFragment2 serviceFragment;
	private WebFragment jobFragment;

	private View newsLayout, guideLayout, serviceLayout, jobLayout;
	private ImageView newsImage, guideImage, serviceImage, jobImage;
	private TextView newsText, guideText, serviceText, jobText, title;

	private FragmentManager fragmentManager;// 管理fragment
	private FragmentTransaction transaction;
	private IFragmentBack webFragment;
	private int current = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		initViews();
		setSwipeBackEnable(false);
		setTabSelection(0);// 第一次启动时选中第0个tab
		// 开启消息推送服务
		Intent serviceIntent = new Intent(this, HrssService.class);
		startService(serviceIntent);
		// 检查更新
		UpdateManager updateManager = new UpdateManager(this);
		updateManager.checkUpdate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	// 意外恢复
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		finish();
	}

	/**
	 * 初始化布局元素
	 */
	private void initViews() {
		newsLayout = findViewById(R.id.news_layout);
		guideLayout = findViewById(R.id.guide_layout);
		serviceLayout = findViewById(R.id.siservice_layout);
		jobLayout = findViewById(R.id.job_layout);

		newsImage = (ImageView) findViewById(R.id.news_image);
		guideImage = (ImageView) findViewById(R.id.guide_image);
		serviceImage = (ImageView) findViewById(R.id.siservice_image);
		jobImage = (ImageView) findViewById(R.id.job_image);
		title = (TextView) findViewById(R.id.titlebar);
		title.setText("新闻资讯");
		newsText = (TextView) findViewById(R.id.news_text);
		guideText = (TextView) findViewById(R.id.guide_text);
		serviceText = (TextView) findViewById(R.id.siservice_text);
		jobText = (TextView) findViewById(R.id.job_text);

		newsLayout.setOnClickListener(this);
		guideLayout.setOnClickListener(this);
		serviceLayout.setOnClickListener(this);
		jobLayout.setOnClickListener(this);

		((ImageView) findViewById(R.id.actionbar_left))
				.setVisibility(View.INVISIBLE);

		fragmentManager = getSupportFragmentManager();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_layout:
			setTabSelection(0);
			break;
		case R.id.guide_layout:
			setTabSelection(1);
			break;
		case R.id.siservice_layout:
			setTabSelection(2);
			break;
		case R.id.job_layout:
			setTabSelection(3);
			break;
		}
	}

	// 33cccc
	int layout_normal_color = Color.parseColor("#33cccc");
	int layout_press_color = Color.parseColor("#006666");

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示新闻资讯，1表示办事指南，2表示人社服务，3表示就业招聘
	 */
	private void setTabSelection(int index) {

		switch (index) {
		case 0:
			title.setText("新闻资讯");
			if (current == 0) {
				return;
			}
			current = 0;
			preClick();
			// 当点击了消息tab时，改变控件的文字颜色
			// newsImage.setImageResource(R.drawable.xwzxh);
			newsLayout.setBackgroundColor(layout_press_color);
			newsText.setTextColor(Color.WHITE);
			if (newsFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				newsFragment = new NewsFragment();
				transaction.setCustomAnimations(R.anim.push_left_in,
						R.anim.push_right_out);
				transaction.add(R.id.content, newsFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(newsFragment);
			}

			break;
		case 1:
			title.setText("办事指南");
			if (current == 1) {
				return;
			}
			current = 1;
			preClick();
			// guideImage.setImageResource(R.drawable.bsznh);
			guideLayout.setBackgroundColor(layout_press_color);
			guideText.setTextColor(Color.WHITE);
			if (guideFragment == null) {
				guideFragment = new GuideFragment();
				transaction.add(R.id.content, guideFragment);
			} else {
				transaction.show(guideFragment);
			}

			break;
		case 2:
			title.setText("人社服务");
			if (current == 2) {
				return;
			}
			current = 2;
			preClick();
			// serviceImage.setImageResource(R.drawable.rsfwh);
			serviceLayout.setBackgroundColor(layout_press_color);
			serviceText.setTextColor(Color.WHITE);
			if (serviceFragment == null) {
//				serviceFragment = new SiServiceFragment();
				serviceFragment = new SiServiceFragment2();
				transaction.add(R.id.content, serviceFragment);
			} else {
				transaction.show(serviceFragment);
			}
			break;
		case 3:
			title.setText("就业招聘");
			if (current == 3) {
				return;
			}
			current = 3;
			preClick();
			// jobImage.setImageResource(R.drawable.jyzph);
			jobLayout.setBackgroundColor(layout_press_color);
			jobText.setTextColor(Color.WHITE);
			if (jobFragment == null) {
				Bundle bundle = new Bundle();
				bundle.putString("url", HrssConstants.JNSI_KZRCW);
				bundle.putBoolean("phone", false);
				jobFragment = WebFragment.newInstance(bundle);
				transaction.add(R.id.content, jobFragment);
			} else {
				transaction.show(jobFragment);
			}
			break;
		}
		transaction.commit();
	}

	void preClick() {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		transaction = fragmentManager.beginTransaction();
		// transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.setCustomAnimations(R.anim.zoom_enter, R.anim.zoom_exit);
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
	}

	/**
	 * 将所有的Fragment都置为隐藏状态
	 * 
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (guideFragment != null) {
			transaction.hide(guideFragment);
		}
		if (serviceFragment != null) {
			transaction.hide(serviceFragment);
		}
		if (jobFragment != null) {
			transaction.hide(jobFragment);
		}
	}

	/**
	 * 清除所有选中状态
	 */
	@SuppressLint("NewApi")
	private void clearSelection() {
		newsImage.setImageResource(R.drawable.xwzx);
		newsText.setTextColor(Color.WHITE);
		newsLayout.setBackgroundColor(layout_normal_color);
		guideImage.setImageResource(R.drawable.bszn);
		guideText.setTextColor(Color.WHITE);
		guideLayout.setBackgroundColor(layout_normal_color);
		serviceImage.setImageResource(R.drawable.rsfw);
		serviceText.setTextColor(Color.WHITE);
		serviceLayout.setBackgroundColor(layout_normal_color);
		jobImage.setImageResource(R.drawable.jyzp);
		jobText.setTextColor(Color.WHITE);
		jobLayout.setBackgroundColor(layout_normal_color);
	}

	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		if (webFragment == null || !webFragment.onBackPressed()) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				ToastUtil.toast(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
				AnimUtil.animBackSlide(MainActivity.this);
			}
		}
	}

	@Override
	public void setVisiableFragment(IFragmentBack visiableFragment) {
		webFragment = visiableFragment;

	}

}