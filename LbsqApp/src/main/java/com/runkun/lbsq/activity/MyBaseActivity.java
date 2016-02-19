package com.runkun.lbsq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.Tools;

public abstract class MyBaseActivity extends BaseAcitivity
{
	@ViewInject(R.id.my_collect_rg)
	protected RadioGroup selectionRG;

	@ViewInject(R.id.nodata)
	protected ImageView nodata;

	@ViewInject(R.id.mc_indicator_1)
	protected View indicator_1;

	@ViewInject(R.id.mc_indicator_2)
	protected View indicator_2;
	@ViewInject(R.id.radio_first)
	protected RadioButton radio_first;

	@ViewInject(R.id.radio_second)
	protected RadioButton radio_second;

	protected int type = 1;
	protected int red;

	protected FragmentManager fragmentManager;
	protected FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_base);
		ViewUtils.inject(this);
		selectionRG = (RadioGroup) findViewById(R.id.my_collect_rg);
		nodata = (ImageView) findViewById(R.id.nodata);
		indicator_1 = findViewById(R.id.mc_indicator_1);
		indicator_2 = findViewById(R.id.mc_indicator_2);
		radio_first = (RadioButton) findViewById(R.id.radio_first);
		radio_second = (RadioButton) findViewById(R.id.radio_second);
		tint();
		initActionbar();
		red = getResources().getColor(R.color.reds);
		actionbar_right2.setBackgroundResource(R.drawable.ic_delete);
		actionbar_right2.setVisibility(View.VISIBLE);
		fragmentManager = getSupportFragmentManager();
		selectionRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_first:
					loads1();
					break;
				case R.id.radio_second:
					loads2();
					break;
				}
			}
		});
		actionbar_right2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getCount() > 0) {
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
	}

	protected abstract void loads1();

	protected abstract void loads2();

	protected abstract void delete();

	protected abstract int getCount();

}
