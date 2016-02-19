package com.hitoosoft.hrssapp.acitivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.database.FavoriteDb;
import com.hitoosoft.hrssapp.database.FavoriteDbHelper;
import com.hitoosoft.hrssapp.entity.News;
import com.hitoosoft.hrssapp.fragment.IFragmentBack;
import com.hitoosoft.hrssapp.fragment.IFragmentChanger;
import com.hitoosoft.hrssapp.fragment.WebFragment;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.util.ToastUtil;

/**
 * 打开网页
 */
@SuppressLint("ClickableViewAccessibility")
public class WebActivity extends BaseActivity implements IFragmentChanger {
	private boolean pushMsg = false;
	WebFragment fragment;
	IFragmentBack webfragment;
	boolean isPage, isFav;
	ImageView imageView;
	News news;
	String pageUrlPreString = HrssConstants.SERVER_URL
			+ "hrssmsp/newsList/viewNewsContent.do?newsID=";
	String picUrlPreString = HrssConstants.SERVER_URL;
	boolean fav;
	FavoriteDbHelper dbHelper;
	String newsidString;
	boolean i = false;
	Intent intent;
	String openUrl;
	SharedPreferences fac;
	SharedPreferences.Editor editor;
	boolean webFirst;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		fac = getSharedPreferences("webFirst", Context.MODE_PRIVATE);
		editor = fac.edit();
		webFirst = fac.getBoolean("webFirst", true);
		if (webFirst) {
			// TextView textView = new TextView(this);
			// textView.setTextColor(Color.GREEN);
			// textView.setTextSize(20);
			// textView.setGravity(Gravity.CENTER);
			// textView.setText("左右滑动可以快速返回");
			ToastUtil.toast(this, "向右滑动可以快速返回");
			// ToastCustom.toastShow(this, textView).show();
			editor.putBoolean("webFirst", false);
			editor.commit();
		}

		intent = getIntent();
		String title = intent.getStringExtra("title") == null ? "智慧人社" : intent
				.getStringExtra("title");
		((TextView) findViewById(R.id.titlebar)).setText(title);
		imageView = (ImageView) findViewById(R.id.actionbar_right);
		imageView.setVisibility(View.INVISIBLE);
		openUrl = intent.getStringExtra("openUrl");
		// 从文章页过来的
		isPage = intent.getBooleanExtra("page", false);
		isFav = intent.getBooleanExtra("isFav", false);
		if (isPage) {
			news = intent.getParcelableExtra("pageitem");
			newsidString = news.getNewsID();

			dbHelper = new FavoriteDbHelper(this);
			fav = dbHelper.isExit(newsidString);

			if (isPage && news != null) {
				imageView.setVisibility(View.VISIBLE);
				if (fav) {
					imageView.setImageResource(R.drawable.selected_pressed);
					Log.e("log", "exist");
				} else {
					imageView.setImageResource(R.drawable.selected);
					Log.e("log", "no-exist");
				}

				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (fav) {
							dbHelper.delete(newsidString);
							imageView.setImageResource(R.drawable.selected);
							fav = !fav;
							ToastUtil.toast(WebActivity.this, "取消收藏");
						} else {
							dbHelper.insert(newsidString, news.getNewsTitle(),
									pageUrlPreString + newsidString,
									picUrlPreString + news.getPicURL(),
									news.getPublishDate());
							imageView
									.setImageResource(R.drawable.selected_pressed);
							fav = !fav;
							ToastUtil.toast(WebActivity.this, "收藏成功");
						}

					}
				});
			}
		}
		if (isFav) {
			dbHelper = new FavoriteDbHelper(this);
			i = true;
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(R.drawable.selected_pressed);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (i) {
						dbHelper.delete(intent
								.getStringExtra(FavoriteDb.newsId));
						imageView.setImageResource(R.drawable.selected);
						i = !i;
						ToastUtil.toast(WebActivity.this, "取消收藏");
					} else {
						dbHelper.insert(
								intent.getStringExtra(FavoriteDb.newsId),
								intent.getStringExtra(FavoriteDb.newsTitle),
								openUrl,
								intent.getStringExtra(FavoriteDb.newsPic),
								intent.getStringExtra(FavoriteDb.newsTime));
						imageView.setImageResource(R.drawable.selected_pressed);
						i = !i;
						ToastUtil.toast(WebActivity.this, "收藏成功");
					}

				}
			});
		}

		Bundle bundle = new Bundle();
		bundle.putString("url", openUrl);
		fragment = WebFragment.newInstance(bundle);
		pushMsg = intent.getBooleanExtra("pushMsg", false);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.web, fragment).commit();

		// gestureDetector = new GestureDetector(new MyGestureDetector());
		// gestureListener = new View.OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// if (gestureDetector.onTouchEvent(event)) {
		// return true;
		// }
		// return false;
		// }
		// };
	}

	// // 左右滑动刚好页面也有滑动效果
	// class MyGestureDetector extends SimpleOnGestureListener {
	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
	// return false;
	// }
	// if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
	// && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	// Log.i("test", "left");
	// AnimUtil.animBackSlideFinish(WebActivity.this);
	// } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
	// && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	// Log.i("test", "right");
	// AnimUtil.animBackSlideFinish(WebActivity.this);
	// }
	// return false;
	// }
	// }
	//
	// private static final int SWIPE_MIN_DISTANCE = 120;
	// private static final int SWIPE_MAX_OFF_PATH = 250;
	// private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	// private GestureDetector gestureDetector;
	// View.OnTouchListener gestureListener;
	//
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// if (gestureDetector.onTouchEvent(event)) {
	// event.setAction(MotionEvent.ACTION_CANCEL);
	// }
	// return super.dispatchTouchEvent(event);
	// }

	public void setting(View view) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (pushMsg) { // 消息推送notification打开的时候，关闭的同时重新启动应用
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 按back键可以回到上个网页
	 */
	@Override
	public void onBackPressed() {
		if (webfragment == null || !webfragment.onBackPressed()) {
			super.onBackPressed();
		}
	}

	@Override
	public void setVisiableFragment(IFragmentBack visiableFragment) {
		webfragment = visiableFragment;

	}

}