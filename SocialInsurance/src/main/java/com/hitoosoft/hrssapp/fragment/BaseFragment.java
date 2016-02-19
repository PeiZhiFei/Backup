package com.hitoosoft.hrssapp.fragment;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment implements IFragmentBack {
	protected IFragmentChanger iFragmentChanger;

	@Override
	public void onStart() {
		super.onStart();
		iFragmentChanger = (IFragmentChanger) getActivity();
		iFragmentChanger.setVisiableFragment(this);
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}
}
