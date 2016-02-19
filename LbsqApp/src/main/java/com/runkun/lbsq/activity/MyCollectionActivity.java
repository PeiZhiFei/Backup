package com.runkun.lbsq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.FavGoodsFragment;
import com.runkun.lbsq.utils.Tools;

public class MyCollectionActivity extends ContainerActivity
{

	FavGoodsFragment favGoodsFragment;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setTitles(Tools.getStr(activity, R.string.TMYFAV));

		actionbar_right2.setBackgroundResource(R.drawable.ic_delete);
		actionbar_right2.setVisibility(View.VISIBLE);

		actionbar_right2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (favGoodsFragment.getCount() > 0) {
					new AlertDialog.Builder(activity)
							.setTitle(Tools.getStr(activity, R.string.DTITLE))
							.setMessage(
									Tools.getStr(activity, R.string.DDELFAV))
							.setPositiveButton(
									Tools.getStr(activity, R.string.CONFIM),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											delete();
										}
									})
							.setNegativeButton(
									Tools.getStr(activity, R.string.CANCEL),
									null).show();
				} else {
					Tools.toast(activity, "这里是空的");
				}

			}

		});

		ft.setCustomAnimations(R.anim.activity_in_from_left,
				R.anim.activity_out_to_right);
		if (favGoodsFragment == null) {
			favGoodsFragment = new FavGoodsFragment();
			ft.replace(R.id.container, favGoodsFragment);
		}
		ft.commit();
	}

	protected void delete() {
		favGoodsFragment.deleteFav(-1);
	}

}
