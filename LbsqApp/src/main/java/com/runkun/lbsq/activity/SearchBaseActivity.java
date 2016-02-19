package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.Tools;

import java.util.List;

import feifei.project.view.ClearEditText;

public abstract class SearchBaseActivity<T> extends BaseAcitivity
		implements OnClickListener {
	protected ListView listView;
	protected TextView textView;
	protected ClearEditText edit;
	protected Button search;

	protected String store_id;
	protected View footer;
	protected TextView footerTV;
	protected ProgressBar footerPB;
	protected TextView emptyTextView;
	protected List<T> list;
	protected BaseAdapter adaper;
	protected boolean more = true;
	// protected SharedPreferences sharedPreferences;
	// protected SharedPreferences.Editor editor;
	SharedPreferences sp;

	// protected FlowLayout flowlayout;
	// protected int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		tint();
		ViewUtils.inject(this);
		dialogInit();

		listView = (ListView) findViewById(R.id.list);
		edit = (ClearEditText) findViewById(R.id.edit);
		search = (Button) findViewById(R.id.search);
		footer = LayoutInflater.from(this).inflate(R.layout.listview_footer,
				null);
		footerTV = (TextView) footer.findViewById(R.id.footer_tv);
		footerPB = (ProgressBar) footer.findViewById(R.id.footer_pb);
		listView.addFooterView(footer);

		Tools tools = new Tools();
		View view2 = tools.getEmptyView(this, 0);
		((ViewGroup) listView.getParent()).addView(view2);
		emptyTextView = tools.getEmptyText();
		emptyTextView.setText(Tools.getStr(activity, R.string.SEARCHHINT));
		// emptyTextView.setTextSize(20);
		listView.setEmptyView(view2);

		footerPB.setVisibility(View.GONE);

		// flowlayout = (FlowLayout) findViewById(R.id.flowlayout);
		// sharedPreferences = getSharedPreferences("history",
		// Context.MODE_PRIVATE);
		// editor = sharedPreferences.edit();

		sp = getSharedPreferences("lbsq", Context.MODE_PRIVATE);
		store_id = sp.getString("storeId", null);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Tools.keyboardShow(edit);
			}
		}, 600);

		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						if (more) {
							query();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view != footer) {
					jumpTo(position);

				}
			}
		});

		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = edit.getText().toString();
				if (Tools.isEmpty(s)) {
					Tools.toast(activity,
							Tools.getStr(activity, R.string.SEARCHPLEASE));
					Tools.shake(edit);
				} else {
					search(s);
				}
			}
		});

		edit.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					String s = edit.getText().toString();
					if (Tools.isEmpty(s)) {
						Tools.toast(activity,
								Tools.getStr(activity, R.string.SEARCHPLEASE));
						Tools.shake(edit);
					} else {
						search(s);
					}
				}
				return false;
			}
		});
	}

	// protected void addText(TextView textView, int i) {
	// textView = new TextView(this);
	// LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	// -2, 100);
	// layoutParams.setMargins(20, 20, 0, 0);
	// textView.setLayoutParams(layoutParams);
	// textView.setGravity(Gravity.CENTER);
	// textView.setPadding(20, 20, 20, 20);
	// textView.setTextColor(Color.WHITE);
	// textView.setClickable(true);
	// textView.setTag(i);
	// textView.setBackgroundResource(R.drawable.flag_01);
	// textView.setOnClickListener(this);
	// // if (i == 0) {
	// // textViews[i].setText("清除搜索历史");
	// // } else {
	// // textViews[i].setText(map.get("history" + i));
	// // }
	// textView.setText(array.get(i));
	// flowlayout.addView(textView);
	// }
	//
	// protected void addText(int i) {
	// TextView textView = new TextView(this);
	// LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	// -2, 100);
	// layoutParams.setMargins(20, 20, 0, 0);
	// textView.setLayoutParams(layoutParams);
	// textView.setGravity(Gravity.CENTER);
	// textView.setPadding(20, 20, 20, 20);
	// textView.setTextColor(Color.WHITE);
	// textView.setClickable(true);
	// textView.setTag(i);
	// textView.setBackgroundResource(R.drawable.flag_01);
	// textView.setOnClickListener(this);
	// // if (i == 0) {
	// // textViews[i].setText("清除搜索历史");
	// // } else {
	// // textViews[i].setText(map.get("history" + i));
	// // }
	// textView.setText(array.get(i));
	// flowlayout.addView(textView);
	// }
	//
	// protected void onResume() {
	// super.onResume();
	// array.clear();
	// array.counter_add("清除搜索历史");
	// Map<String, String> maps = (Map<String, String>) sharedPreferences
	// .getAll();
	// for (int i = 0; i < maps.size(); i++) {
	// array.counter_add((maps.get("history" + (i + 1))));
	// }
	// // array.addAll(maps.values());
	// if (array.size() <= 1) {
	// flowlayout.setVisibility(View.GONE);
	// }
	//
	// // map.put("first", "清除搜索历史");
	// // map = (Map<String, String>) sharedPreferences.getAll();
	// // map.putAll((Map<? extends String, ? extends String>)
	// // sharedPreferences
	// // .getAll());
	// // if (map.size() <= 1) {
	// // flowlayout.setVisibility(View.GONE);
	// // }
	//
	// // textViews = new TextView[map.size()];
	// textViews = new TextView[array.size()];
	// for (int i = 0; i < textViews.length; i++) {
	// addText(textViews[i], i);
	// }
	// };
	//
	// // Map<String, String> map = new HashMap<String, String>();
	// List<String> array = new ArrayList<String>();
	// // Set<String> array = new HashSet<String>();
	// TextView[] textViews;
	//
	@Override
	public void onClick(View view) {
		// if ((int) view.getTag() == 0) {
		// SharedPreferences.Editor test = sp.edit();
		// test.remove("count");
		// test.apply();
		// count = 0;
		// editor.clear();
		// editor.apply();
		// array.clear();
		// flowlayout.setVisibility(View.GONE);
		// } else {
		// // edit.setText(map.get("history" + view.getTag()));
		// edit.setText(array.get((int) view.getTag()));
		// }
	}

	protected abstract void query();

	protected abstract void jumpTo(int position);

	protected abstract void search(String s);

	protected void updateFooter(boolean going, String info) {
		if (going) {
			footerTV.setVisibility(View.GONE);
			footerPB.setVisibility(View.VISIBLE);
		} else {
			footerPB.setVisibility(View.GONE);
			footerTV.setVisibility(View.VISIBLE);
			footerTV.setText(info);
		}
	}

}
