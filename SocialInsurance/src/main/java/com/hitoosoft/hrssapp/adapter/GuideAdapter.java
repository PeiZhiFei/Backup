package com.hitoosoft.hrssapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hitoosoft.hrssapp.fragment.ContentFragmentRefresh;
import com.hitoosoft.hrssapp.fragment.WebFragment;
import com.hitoosoft.hrssapp.util.HrssConstants;

public class GuideAdapter extends FragmentPagerAdapter {
	private final String[] TITLES;

	public GuideAdapter(FragmentManager fm, String[] TITLES) {
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
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = ContentFragmentRefresh.newInstance(TITLES[position]);
			break;
		case 1:
			Bundle bundle = new Bundle();
			bundle.putString("url", HrssConstants.HRSSMSP_GUIDE_FWDH);
			bundle.putBoolean("phone", true);
			fragment = WebFragment.newInstance(bundle);
			break;
		case 2:
			Bundle bundle2 = new Bundle();
			bundle2.putString("url", HrssConstants.JNSI_12333);
			bundle2.putBoolean("phone", false);
			fragment = WebFragment.newInstance(bundle2);
			break;
		}
		return fragment;
	}

}