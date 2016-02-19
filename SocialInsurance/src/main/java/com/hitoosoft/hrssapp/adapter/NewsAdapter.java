package com.hitoosoft.hrssapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hitoosoft.hrssapp.fragment.ContentFragmentRefresh;

public class NewsAdapter extends FragmentPagerAdapter {
	private final String[] TITLES;

	public NewsAdapter(FragmentManager fm, String[] TITLES) {
		super(fm);
		this.TITLES = TITLES;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

	@Override
	public Fragment getItem(int position) {
		return ContentFragmentRefresh.newInstance(TITLES[position]);
	}

}
