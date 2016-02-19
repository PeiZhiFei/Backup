package com.hitoosoft.hrssapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.adapter.NewsAdapter;
import com.hitoosoft.hrssapp.util.PagerAnim;
import com.hitoosoft.hrssapp.view.PagerSlidingTabStrip;

/**
 * 新闻资讯
 */
public class NewsFragment extends Fragment {
//	private final String[] TITLES = { "新闻", "公告", "政策", "招考" };
	private final String[] TITLES = { "新闻", "公告", "政策" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_news, null);
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		NewsAdapter adapter = new NewsAdapter(getFragmentManager(), TITLES);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(4);
		tabs.setShouldExpand(true);
		tabs.setViewPager(pager);
		tabs.setTextColor(Color.parseColor("#006666"));
		tabs.setTextSize(35);
		pager.setPageTransformer(true, new PagerAnim());
		return view;
	}

}