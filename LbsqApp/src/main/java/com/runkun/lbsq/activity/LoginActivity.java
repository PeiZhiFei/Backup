package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.LoginFragment;
import com.runkun.lbsq.utils.Tools;

public class LoginActivity extends ContainerActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoginFragment loginFragment = new LoginFragment();
		Intent intent = getIntent();
		String s = intent.getStringExtra("login");
		if (s != null) {
			Bundle bundle = new Bundle();
			bundle.putString("login", s);
			loginFragment.setArguments(bundle);
		}
		ft.replace(R.id.container, loginFragment).commit();
		setTitles(Tools.getStr(this, R.string.TLOGIN));
	}

}
