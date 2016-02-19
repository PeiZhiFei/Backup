package com.runkun.lbsq.test;

import com.runkun.lbsq.fragment.ShopListBaseFragment;
import com.runkun.lbsq.fragment.ShopListFragment;
import com.runkun.lbsq.utils.MyConstant;

public class LocationFactory {

	public static ShopListBaseFragment getShoplistFragment() {
		if (MyConstant.DEBUG) {
			return new TestShopListFragment();

		} else {
			return new ShopListFragment();
		}

	}

}
