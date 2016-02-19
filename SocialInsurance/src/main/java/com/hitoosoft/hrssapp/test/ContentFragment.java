package com.hitoosoft.hrssapp.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.acitivity.WebActivity;
import com.hitoosoft.hrssapp.adapter.NewsListAdapter;
import com.hitoosoft.hrssapp.entity.News;
import com.hitoosoft.hrssapp.entity.OperResult;
import com.hitoosoft.hrssapp.fragment.BaseFragment;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.AsyncHttpGetTask;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.view.DropDownListView;
import com.hitoosoft.hrssapp.view.DropDownListView.OnDropDownListener;

@SuppressLint("HandlerLeak")
public class ContentFragment extends BaseFragment implements
		OnItemClickListener {

	private DropDownListView listView;
	private NewsListAdapter adapter;
	private String title; // viewpager分页
	private final static int DROP_TYPE_DOWN = 1; // 下拉
	private final static int DROP_TYPE_UP = 2; // 上滑(点击)

	public static ContentFragment newInstance(String title) {
		ContentFragment cf = new ContentFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		cf.setArguments(bundle);
		return cf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.content_fragment, container,
				false);
		title = getArguments().getString("title");
		final String newsType = HrssConstants.newsMap.get(title);
		listView = (DropDownListView) view.findViewById(R.id.list);
		// 通过setOnDropDownListener设置下拉的事件，
		// 不过需要在事件结束时手动调用onDropDownComplete恢复状态
		// 注意需要在adapter.notifyDataSetChanged()后面调用
		listView.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				String url = HrssConstants.HRSS_NEWS_URL + "?newsType="
						+ newsType + "&from=0&sum="
						+ HrssConstants.PAGE_SIZE_NEWS;
				new Thread(new AsyncHttpGetTask(url, handler, DROP_TYPE_DOWN))
						.start();
			}
		});
		// 通过setOnBottomListener设置滚动到底部的事件，不过需要在事件结束时手动调用onBottomComplete恢复状态
		listView.setOnBottomListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = HrssConstants.HRSS_NEWS_URL + "?newsType="
						+ newsType + "&from=" + adapter.getCount() + "&sum="
						+ HrssConstants.PAGE_SIZE_NEWS;
				new Thread(new AsyncHttpGetTask(url, handler, DROP_TYPE_UP))
						.start();
			}
		});
		adapter = new NewsListAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		// 初始加载一次数据
		listView.onDropDown();
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
						adapter.notifyDataSetChanged();
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"MM-dd HH:mm:ss");
						listView.onDropDownComplete(getString(R.string.update_at)
								+ dateFormat.format(new Date()));
						listView.setHasMore(true);
						listView.onBottomComplete();
					} else if (DROP_TYPE_UP == msg.arg1) {
						adapter.addNewsList(newsList);
						adapter.notifyDataSetChanged();
						if (null == newsList || newsList.size() == 0) {
							listView.setHasMore(false);
						}
						listView.onBottomComplete();
					} else {
						adapter.addNewsList(newsList);
						adapter.notifyDataSetChanged();
					}
				} else {
					if (getActivity() != null) {
						Toast.makeText(getActivity(), operResult.getOperDesc(),
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				if (getActivity() != null) {
					// Toast.makeText(getActivity(), data,
					// Toast.LENGTH_SHORT).show();
				}
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * 点击某一个Item时被调用
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (-1 != id) {// id==-1表示点击的footerView
			News news = (News) adapter.getItem(position - 1);
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra("openUrl",
					HrssConstants.SERVER_URL
							+ "hrssmsp/newsList/viewNewsContent.do?newsID="
							+ news.getNewsID());
			intent.putExtra("page", true);
			startActivity(intent);
			AnimUtil.animToSlide(getActivity());
		}
	}

}