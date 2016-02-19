package com.runkun.lbsq.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runkun.lbsq.R;

@SuppressLint("NewApi")
public class MainFruiFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView (inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_main1_first, container,
				false);
		FragmentManager fragmentManager = getFragmentManager();
		MainAdsFragment mainAdsFragment = new MainAdsFragment();
		FruitShopFragment fruitShopFragment = new FruitShopFragment();
		int[] ids = new int[] { R.id.fragment0, R.id.fragment1 };
		Fragment[] fragments = new Fragment[] { mainAdsFragment,
				fruitShopFragment };
		for (int i = 0; i < 2; i++) {

			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(ids[i], fragments[i]).commit();
		}

		return view;
	}


}
