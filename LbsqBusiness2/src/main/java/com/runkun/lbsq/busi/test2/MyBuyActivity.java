package com.runkun.lbsq.busi.test2;/*
package project.base;

import android.graphics.Color;
import android.os.Bundle;



public class MyBuyActivity extends MyBaseActivity {
	MyBuyBasefragment myBuyAlreadyFragment;
	MyBuyBasefragment myBuyNotFragment;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		radio_first.setText("");
		radio_second.setText("");
		loads1();
	}

	@Override
	protected void delete() {
		if (type == 1) {
			myBuyAlreadyFragment.del(-1);
		} else {
			myBuyNotFragment.del(-1);
		}

	}

	@Override
	protected void loads1() {
		transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.activity_in_from_left,
				R.anim.activity_out_to_right);
		if (myBuyNotFragment != null) {
			transaction.hide(myBuyNotFragment);
		}
		if (myBuyAlreadyFragment == null) {
			myBuyAlreadyFragment = MyBuyBasefragment.newInstance(1);
			transaction.add(R.id.container, myBuyAlreadyFragment);
		} else {
			transaction.show(myBuyAlreadyFragment);
		}
		transaction.commit();

		indicator_1.setBackgroundColor(red);
		indicator_2.setBackgroundColor(Color.argb(0, 0, 0, 0));
		radio_first.setTextColor(Color.BLACK);
		radio_second.setTextColor(Color.parseColor("#ADADAD"));
		type = 1;
	}

	@Override
	protected void loads2() {
		transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.activity_in_from_right,
				R.anim.activity_out_to_left);
		if (myBuyAlreadyFragment != null) {
			transaction.hide(myBuyAlreadyFragment);
		}
		if (myBuyNotFragment == null) {
			myBuyNotFragment = MyBuyBasefragment.newInstance(2);
			transaction.add(R.id.container, myBuyNotFragment);
		} else {
			transaction.show(myBuyNotFragment);
		}
		transaction.commit();

		indicator_2.setBackgroundColor(red);
		indicator_1.setBackgroundColor(Color.argb(0, 0, 0, 0));
		radio_second.setTextColor(Color.BLACK);
		radio_first.setTextColor(Color.parseColor("#ADADAD"));
		type = 2;
	}

	@Override
	protected int getCount() {
		return type == 1 ? myBuyAlreadyFragment.getCount() : myBuyNotFragment
				.getCount();
	}

}
*/
