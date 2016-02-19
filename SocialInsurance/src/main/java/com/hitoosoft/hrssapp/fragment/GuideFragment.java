package com.hitoosoft.hrssapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.adapter.GuideAdapter;
import com.hitoosoft.hrssapp.util.PagerAnim;
import com.hitoosoft.hrssapp.view.PagerSlidingTabStrip;

/**
 * 办事指南
 */
public class GuideFragment extends Fragment {

//	private final String[] TITLES = { "办事流程", "服务电话", "12333" };
	private final String[] TITLES = { "办事流程", "服务电话"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_guide, null);
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabsguide);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pagerguide);
		GuideAdapter adapter = new GuideAdapter(getFragmentManager(), TITLES);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(4);// 一共加载4页，如果此处不指定，默认只加载相邻页
		tabs.setShouldExpand(true);// 让分页title按合适的比例填充
		tabs.setViewPager(pager);
		tabs.setTextColor(Color.parseColor("#006666"));
		tabs.setTextSize(35);
		pager.setPageTransformer(true, new PagerAnim());
		return view;
	}

}