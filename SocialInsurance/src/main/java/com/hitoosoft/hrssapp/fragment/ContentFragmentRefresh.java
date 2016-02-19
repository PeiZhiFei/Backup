package com.hitoosoft.hrssapp.fragment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.acitivity.WebActivity;
import com.hitoosoft.hrssapp.adapter.NewsListAdapter;
import com.hitoosoft.hrssapp.database.ReadedDbHelper;
import com.hitoosoft.hrssapp.entity.News;
import com.hitoosoft.hrssapp.entity.OperResult;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.AsyncHttpGetTask;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.util.ToastUtil;
import com.hitoosoft.hrssapp.view.refresh.RefreshBase;
import com.hitoosoft.hrssapp.view.refresh.RefreshBase.Mode;
import com.hitoosoft.hrssapp.view.refresh.RefreshBase.OnLastItemVisibleListener;
import com.hitoosoft.hrssapp.view.refresh.RefreshBase.OnRefreshListener2;
import com.hitoosoft.hrssapp.view.refresh.RefreshListView;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ContentFragmentRefresh extends BaseFragment implements
		OnItemClickListener, OnRefreshListener2 {

	private RefreshListView listView;
	private NewsListAdapter adapter;
	private String title; // viewpager分页
	private final static int DROP_TYPE_DOWN = 1; // 下拉
	private final static int DROP_TYPE_UP = 2; // 上滑(点击)
	String newsType;
	String 登陆文本框;
	ReadedDbHelper dbHelper2;

	public static ContentFragmentRefresh newInstance(String title) {
		ContentFragmentRefresh cf = new ContentFragmentRefresh();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		cf.setArguments(bundle);
		return cf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.content_fragment_refresh,
				container, false);
		title = getArguments().getString("title");
		newsType = HrssConstants.newsMap.get(title);
		listView = (RefreshListView) view.findViewById(R.id.list);
		// 通过setOnDropDownListener设置下拉的事件，
		// 不过需要在事件结束时手动调用onDropDownComplete恢复状态
		// 注意需要在adapter.notifyDataSetChanged()后面调用
		// 滑动刷新不可得兼
		// listView.setScrollingWhileRefreshingEnabled(false);
		// 上下都可以刷新
		listView.setOnRefreshListener(this);
		listView.setMode(Mode.PULL_FROM_START);
		// Add an end-of-list listener
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				onPullUpToRefresh(listView);
			}
		});

		adapter = new NewsListAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		// 初始加载一次数据
		onPullDownToRefresh(listView);
		dbHelper2 = new ReadedDbHelper(getActivity());
		return view;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			String data = (String) msg.obj;
			if (what == HrssConstants.SUCCESS) {
				OperResult operResult = OperResult.fromJsonToObject(data);
				if ("W0001".equals(operResult.getOperCode())) {
					List<News> newsList = News.getDataFromJson(operResult
							.getDataArray());
					if (DROP_TYPE_DOWN == msg.arg1) {
						adapter.setNewsList(newsList);
					} else {
						adapter.addNewsList(newsList);
						if (null == newsList || newsList.size() == 0) {
							ToastUtil.toast(getActivity(), "没有更多数据了");
						}
					}
					adapter.notifyDataSetChanged();
				} else {
					if (getActivity() != null) {
						// 这个地方基本没弹出过来过
						ToastUtil.toast(getActivity(),
								"result" + operResult.getOperDesc());
					}
				}
			} else {
				if (getActivity() != null && data != null) {
					ToastUtil.toast(getActivity(), "data" + data);
				}
			}
			listView.onRefreshComplete();
			super.handleMessage(msg);
		}

	};

	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	};

	/**
	 * 点击某一个Item时被调用
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (-1 != id) {// id==-1表示点击的footerView
			News news = (News) adapter.getItem(position - 1);
			Log.e("log", position + "");
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra("openUrl",
					HrssConstants.SERVER_URL
							+ "hrssmsp/newsList/viewNewsContent.do?newsID="
							+ news.getNewsID());
			intent.putExtra("page", true);
			intent.putExtra("pageitem", news);
			dbHelper2.insert(news.getNewsID());
			// adapter.notifyDataSetChanged();
			startActivity(intent);
			AnimUtil.animToSlide(getActivity());
		}
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onPullDownToRefresh(RefreshBase refreshView) {
		setTime(refreshView);
		String url = HrssConstants.HRSS_NEWS_URL + "?newsType=" + newsType
				+ "&from=0&sum=" + HrssConstants.PAGE_SIZE_NEWS;
		new Thread(new AsyncHttpGetTask(url, handler, DROP_TYPE_DOWN)).start();
	}

	/**
	 * 上拉翻页
	 */
	@Override
	public void onPullUpToRefresh(RefreshBase refreshView) {
		setTime(refreshView);
		String url = HrssConstants.HRSS_NEWS_URL + "?newsType=" + newsType
				+ "&from=" + adapter.getCount() + "&sum="
				+ HrssConstants.PAGE_SIZE_NEWS;
		new Thread(new AsyncHttpGetTask(url, handler, DROP_TYPE_UP)).start();
	}

	/**
	 * 设置时间
	 * 
	 * @param refreshView
	 */
	public static void setTime(RefreshBase refreshView) {
		SimpleDateFormat newSimpleDate = new SimpleDateFormat("MM-dd HH:mm",
				Locale.getDefault());
		String Time = newSimpleDate.format(new java.util.Date());
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(Time);
	}

}